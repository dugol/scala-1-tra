import java.util.concurrent.Executors

import scala.concurrent.{Await, ExecutionContext, Future}

import scala.concurrent.duration._

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
implicit val ecParaRepos2 = ExecutionContext.fromExecutorService(Executors.newFixedThreadPool(5))

def obtenerRepositorios(usuario:String):List[Repositorio]={
  repositorios.filter(x=>x.dueño==usuario)

}

def FuturoRepositorios(usuario:String):Future[List[Repositorio]]= {
  //implicit val ecParaRepos1 = ExecutionContext.fromExecutorService(Executors.newFixedThreadPool(5))
  Future{obtenerRepositorios(usuario)}
}


val a = FuturoRepositorios("Daniel").map{
  x => x
}

Await.result(a, 1 seconds)

def obtenerRepositorioEspecifico(dueño:String,nombre:String):Repositorio={
  obtenerRepositorios(dueño).filter(x=>x.nombre==nombre).head
}

def futuroRepositorioEspecifico(dueño:String, nombre:String):Future[Repositorio]={
  //implicit val ec = ExecutionContext.fromExecutorService(Executors.newFixedThreadPool(2))
  Future{obtenerRepositorioEspecifico(dueño,nombre)}
}

//implicit val ec = ExecutionContext.fromExecutorService(Executors.newFixedThreadPool(5))
val b=futuroRepositorioEspecifico("Daniel","Hello Word")

Await.result(b,1 seconds)

def obtenerUsuarioCompleto(nombreUsuario: String): UsuarioCompleto={
  val repos=obtenerRepositorios(nombreUsuario)
  val lenguajes=repos.groupBy(x=>x.lenguaje).mapValues(_.length)
  UsuarioCompleto(repos,lenguajes)
}

def futuroObtenerUsuarioCompleto(nombreUsuario:String):Future[UsuarioCompleto]={
  Future{obtenerUsuarioCompleto("Daniel")}
}

val c= futuroObtenerUsuarioCompleto("Daniel")
Await.result(c,1 seconds)
