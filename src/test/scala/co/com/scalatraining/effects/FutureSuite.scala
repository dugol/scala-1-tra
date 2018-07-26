package co.com.scalatraining.effects

import java.util.concurrent.Executors

import org.scalatest.FunSuite
import scala.language.postfixOps
import scala.util.{Failure, Success}
import scala.concurrent.{Await, ExecutionContext, Future}
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

class FutureSuite extends FunSuite {

  test("Un futuro se puede crear") {

    val hiloPpal = Thread.currentThread().getName

    var hiloFuture = ""

    println(s"Test 1 - El hilo ppal es ${hiloPpal}")

    val saludo: Future[String] = Future {
      hiloFuture = Thread.currentThread().getName
      println(s"Test 1 - El hilo del future es ${hiloFuture}")

      Thread.sleep(500)
      "Hola"
    }
    val resultado: String = Await.result(saludo, 10 seconds)
    assert(resultado == "Hola")
    assert(hiloPpal != hiloFuture)
  }

  test("map en Future") {


    val t1 = Thread.currentThread().getName
    println(s"Test 2 - El hilo del ppal es ${t1}")


    val saludo = Future {
      val t2 = Thread.currentThread().getName
      println(s"Test 2 - El hilo del future es ${t2}")

      Thread.sleep(500)
      "Hola"
    }

    Thread.sleep(5000)

    val saludo2 = Future{
      println(s"Test 2 - Hilo normal ${Thread.currentThread().getName}")
    }

    val saludoCompleto = saludo.map(mensaje => {
      val t3 = Thread.currentThread().getName
      println(s"Test 2 - El hilo del map es ${t3}")

      mensaje + " muchachos"
    })


    val resultado = Await.result(saludoCompleto, 10 seconds)
    assert(resultado == "Hola muchachos")
  }

  test("Se debe poder encadenar Future con for-comp") {
    val f1 = Future {
      Thread.sleep(200)
      1
    }

    val f2 = Future {
      Thread.sleep(200)
      2
    }

    val f3: Future[Int] = for {
      res1 <- f1
      res2 <- f2
    } yield res1 + res2

    val res = Await.result(f3, 10 seconds)

    assert(res == 3)



  }


  test("Se debe poder encadenar Future con for-comp FAILED ONE") {
    val f1 = Future {
      Thread.sleep(200)
      1/0
    }

    val f2 = Future {
      Thread.sleep(200)
      2
    }

    val f3: Future[Int] = for {
      res1 <- f1.recover{case e: Exception=> 0}
      res2 <- f2
    } yield res1 + res2
    println(s"el valor de f3 es ${f3}")

    val res = Await.result(f3, 10 seconds)

    assert(res == 2)



  }

  test("Se debe poder manejar el error de un Future de forma imperativa") {
    val divisionCero = Future {
      Thread.sleep(100)
      10 / 0
    }
    var error = false

    val r: Unit = divisionCero.onFailure {
      case e: Exception => error = true
    }



    Thread.sleep(1000)

    assert(error == true)
  }

  test("Se debe poder manejar el exito de un Future de forma imperativa") {

    val division = Future {
      5
    }

    var r = 0

    val f: Unit = division.onComplete {
      case Success(res) => r = res
      case Failure(e) => r = 1
    }

    Thread.sleep(150)

    val res = Await.result(division, 10 seconds)

    assert(r == 5)
  }

  test("Se debe poder manejar el error de un Future de forma funcional sincronicamente") {

    var threadName1 = ""
    var threadName2 = ""

    val divisionPorCero = Future {
      threadName1 = Thread.currentThread().getName
      Thread.sleep(100)
      10 / 0
    }.recover {
      case e: ArithmeticException => {
        threadName2 = Thread.currentThread().getName
        "No es posible dividir por cero"
      }
    }

    val res = Await.result(divisionPorCero, 10 seconds)

    assert(threadName1 == threadName2)
    assert(res == "No es posible dividir por cero")

  }

  test("Se debe poder manejar el error de un Future de forma funcional asincronamente") {

    var threadName1 = ""
    var threadName2 = ""

    implicit val ecParaPrimerHilo = ExecutionContext.fromExecutorService(Executors.newFixedThreadPool(1))

    val f1 = Future {
      threadName1 = Thread.currentThread().getName
      2/0
    }(ecParaPrimerHilo)
    .recoverWith {
      case e: ArithmeticException => {

        implicit val ecParaRecuperacion = ExecutionContext.fromExecutorService(Executors.newFixedThreadPool(1))

        Future{
          threadName2 = Thread.currentThread().getName
          1
        }(ecParaRecuperacion)
      }
    }

    val res = Await.result(f1, 10 seconds)

    println(s"Test en recoverWith thread del fallo: $threadName1")
    println(s"Test en recoverWith thread de recuperacion: $threadName2")

    assert(threadName1 != threadName2)
    assert(res==1)
  }

  /*
  test("propuesta Future"){

    val ecParaServicioClima = ExecutionContext.fromExecutorService(Executors.newFixedThreadPool(5))
    val ecParaAccesoBD=ExecutionContext.fromExecutorService(Executors.newFixedThreadPool(1))

    object clima {

      def getClima():String={
      println(s"Clima: ${System.currentTimeMillis()} ${Thread.currentThread().getName}")
      "26/07/2018-10:00, 18°C"
    }

      def returnFuture():Future[String]= {
        val f1 = Future {

          Thread.sleep(100)
          getClima()
        }(ecParaServicioClima)

        f1
      }

    }

    object guardar{
      def save():String={
        println(s"Guardar: ${System.currentTimeMillis()} ${Thread.currentThread().getName}")
        "Guardé en la base de datos."

      }

      def returnFuture():Future[String]= {
        val f2 = Future {

          Thread.sleep(20)
          save()
        }(ecParaAccesoBD)
        f2
      }
    }
    Range(1,30).map{
      x=>x
      clima.returnFuture()
      guardar.returnFuture()
      //Await.result(clima.returnFuture(),10 seconds)
     // Await.result(guardar.returnFuture(),10 seconds)

    }


  }
  */

  test("Ejercicio 2 Futuros"){
    case class Repositorio(dueño:String,nombre:String, lenguaje:String, lineas: Int)
    case class Usuario(nombre: String)
    case class UsuarioCompleto(repositorios: List[Repositorio], lenguajes: Map[String,Int])
    val repo1: Repositorio=Repositorio("Daniel","Hello Word", "Java", 1000)
    val repo2: Repositorio=Repositorio("Juan","Hola Mundo", "Scala", 2000)
    val repo3: Repositorio=Repositorio("Daniel","FizzBuzz", "Java", 3000)
    val repo4: Repositorio=Repositorio("Juan","foo", "Java", 1000)
    val repo5: Repositorio=Repositorio("Daniel","calculadora", "Scala", 500)
    val repo6: Repositorio=Repositorio("Juan","Banco", "Scala", 700)
    val repositorios:List[Repositorio]=List(repo1,repo2,repo3,repo4,repo5,repo6)
    val usuario1:Usuario=Usuario("Daniel")
    val Usuario2:Usuario=Usuario("Juan")
    implicit val ecParaRepos = ExecutionContext.fromExecutorService(Executors.newFixedThreadPool(5))

    //Obtener los repositorios
    def obtenerRepositorios(usuario:String):List[Repositorio]={
      repositorios.filter(x=>x.dueño==usuario)

    }

    //Obtener futuro de repositorios
    def FuturoRepositorios(usuario:String):Future[List[Repositorio]]= {
      //implicit val ecParaRepos = ExecutionContext.fromExecutorService(Executors.newFixedThreadPool(5))
      Future{obtenerRepositorios(usuario)}
    }


    //Evaluación de la funcion
    val a = FuturoRepositorios("Daniel")

    //Comprobar el retorno.
    val repositoriosDaniel=Await.result(a, 1 seconds)

    assert(repositoriosDaniel===List(Repositorio("Daniel","Hello Word","Java",1000), Repositorio("Daniel","FizzBuzz","Java",3000), Repositorio("Daniel","calculadora","Scala",500)))
    //---------------------------------------------punto 2---------------------------------------------------------------

    //Obtener un repositorio especifico
    def obtenerRepositorioEspecifico(dueño:String,nombre:String):Repositorio={
      obtenerRepositorios(dueño).filter(x=>x.nombre==nombre).head
    }

    //Obtener un Future del repositorio especifico
    def futuroRepositorioEspecifico(dueño:String, nombre:String):Future[Repositorio]={
      //implicit val ecParaReposEspecificos1 = ExecutionContext.fromExecutorService(Executors.newFixedThreadPool(5))
      Future{obtenerRepositorioEspecifico(dueño,nombre)}
    }

    //implicit val ecParaReposEspecificos2 = ExecutionContext.fromExecutorService(Executors.newFixedThreadPool(5))

    //Evaluacion del método
    val b=futuroRepositorioEspecifico("Daniel","Hello Word").map(x=>x)

    //Comprobar el resultado
    val repositorioEspecificoDaniel=Await.result(b,1 seconds)

    assert(repositorioEspecificoDaniel===Repositorio("Daniel","Hello Word","Java",1000))

    //--------------------Punto 3--------------------------------------------
    def obtenerUsuarioCompleto(nombreUsuario: String): UsuarioCompleto={
      val repos=obtenerRepositorios(nombreUsuario).sortBy(x=>x.lineas).reverse
      val lenguajes=repos.groupBy(x=>x.lenguaje).mapValues(_.length)
      UsuarioCompleto(repos,lenguajes)
    }

    def futuroObtenerUsuarioCompleto(nombreUsuario:String):Future[UsuarioCompleto]={
      Future{obtenerUsuarioCompleto("Daniel")}
    }

    val c= futuroObtenerUsuarioCompleto("Daniel")
    val uc=Await.result(c,1 seconds)
    assert(uc===UsuarioCompleto(List(Repositorio("Daniel","FizzBuzz","Java",3000), Repositorio("Daniel","Hello Word","Java",1000), Repositorio("Daniel","calculadora","Scala",500)),Map("Scala" -> 1, "Java" -> 2)))



  }

  test("Los future **iniciados** fuera de un for-comp deben iniciar al mismo tiempo") {

    val timeForf1 = 100
    val timeForf2 = 200
    val timeForf3 = 100

    val additionalTime = 50D

    val estimatedElapsed = (Math.max(Math.max(timeForf1, timeForf2), timeForf3) + additionalTime)/1000

    val f1 = Future {
      Thread.sleep(timeForf1)
      1
    }
    val f2 = Future {
      Thread.sleep(timeForf2)
      2
    }
    val f3 = Future {
      Thread.sleep(timeForf3)
      3
    }

    val t1: Long = System.nanoTime()

    val resultado = for {
      a <- f1
      b <- f2
      c <- f3
    } yield (a+b+c)

    val res = Await.result(resultado, 10 seconds)
    val elapsed = (System.nanoTime() - t1) / 1.0E09

    println(s"Future **iniciados** fuera del for-comp estimado: $estimatedElapsed real: $elapsed")
    assert(elapsed <= estimatedElapsed)
    assert(res == 6)

  }

  test("Los future **definidos** fuera de un for-comp deben iniciar secuencialmente") {

    val timeForf1 = 100
    val timeForf2 = 300
    val timeForf3 = 500

    val estimatedElapsed:Double = (timeForf1 + timeForf2 + timeForf3)/1000

    def f1 = Future {
      Thread.sleep(timeForf1)
      1
    }
    def f2 = Future {
      Thread.sleep(timeForf2)
      2
    }
    def f3 = Future {
      Thread.sleep(timeForf3)
      3
    }

    val t1 = System.nanoTime()

    val resultado = for {
      a <- f1
      b <- f2
      c <- f3
    } yield (a+b+c)

    val res = Await.result(resultado, 10 seconds)
    val elapsed = (System.nanoTime() - t1) / 1.0E09

    println(s"Future **definidos** fuera del for-comp estimado: $estimatedElapsed real: $elapsed")

    assert(elapsed >= estimatedElapsed)
    assert(res == 6)

  }

  test("Los future declarados dentro de un for-comp deben iniciar secuencialmente") {

    val t1 = System.nanoTime()

    val timeForf1 = 100
    val timeForf2 = 100
    val timeForf3 = 100

    val estimatedElapsed = (timeForf1 + timeForf2 + timeForf3)/1000

    val resultado = for {
      a <- Future {
        Thread.sleep(timeForf1)
        1
      }
      b <- Future {
        Thread.sleep(timeForf2)
        2
      }
      c <- Future {
        Thread.sleep(timeForf3)
        3
      }
    } yield (a+b+c)

    val res = Await.result(resultado, 10 seconds)
    val elapsed = (System.nanoTime() - t1) / 1.0E09

    assert(elapsed >= estimatedElapsed)
    assert(res == 6)
  }

  test("Future.sequence"){

    val listOfFutures: List[Future[Int]] = Range(1, 11).map(Future.successful(_)).toList

    val resSequence: Future[List[Int]] = Future.sequence {
      listOfFutures
    }

    val resFuture = resSequence.map(l => l.sum/l.size)

    val res = Await.result(resFuture, 10 seconds)

    assert(res ==  Range(1,11).sum/Range(1,11).size)

  }

  test("Future.traverse"){
    //def foo(i:List[Int]):Future[Int]=Future.successful(i.sum/i.size)
    val resFuture = Future.traverse(Range(1,11).map(Future.successful(_))){
      x => x
    }.map(l => l.sum/l.size)

    val res = Await.result(resFuture, 10 seconds)

    assert(res ==  Range(1,11).sum/Range(1,11).size)

  }




}