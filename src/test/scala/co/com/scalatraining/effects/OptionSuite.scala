package co.com.scalatraining.effects

import java.io

import org.scalatest.FunSuite

import scala.collection.immutable.Seq

//Ausencia o presencia de valor

class OptionSuite extends FunSuite {

  test("Se debe poder crear un Option con valor"){
    val s = Option{
      1
    }
    assert(s == Some(1))
  }

  test("Se debe poder crear un Option con valor Some"){
    val s = Some{
      1
    }
    assert(s == Some(1))
  }

  test("Se debe poder crear un Option con valor Some Null"){
    val s = Some{
      null
    }
    assert(s == Some(null))
  }

  test("Se debe poder crear un Option con valor option Null"){
    val s = Option{
      null
    }
    assert(s == None)
  }

  test("Se debe poder crear un Option para denotar que no hay valor"){
    val s = None
    assert(s == None)
  }

  test("Es inseguro acceder al valor de un Option con get"){
    val s = None
    assertThrows[NoSuchElementException]{
      val r = s.get
    }


  }

  test("Se debe poder hacer pattern match sobre un Option") {
    val lista: Seq[Option[String]] = List(Some("Andres"), None, Some("Luis"), Some("Pedro"))
    val nombre: Option[String] = lista(1)
    var res = ""
    res = nombre match {
      case Some(nom) => nom
      case None => "NONAME"
    }
    assert(res == "NONAME")
  }

  test("Fold en Option"){
    val o = Option(1)

    val res: Int = o.fold{
      10
    }{
      x => x + 20
    }

    assert(res == 21)
  }

  test("Fold en Option de null"){
    def f(x:Int):Option[Int]={
      if(x%2==0){
        Some(x)
      }
      else None
    }
    val o:Option[Int] = f(3)

    val res: Int = o.fold{
      10
    }{
      x => x + 20
    }

    assert(res == 10)
  }

  test("Se debe poder saber si un Option tiene valor con isDefined") {
    val lista = List(Some("Andres"), None, Some("Luis"), Some("Pedro"))
    val nombre = lista(0)
    assert(nombre.isDefined)
  }

  test("Se debe poder acceder al valor de un Option de forma segura con getOrElse") {
    val lista = List(Some("Andres"), None, Some("Luis"), Some("Pedro"))
    val nombre = lista(1)
    val res = nombre.getOrElse("NONAME")
    assert(res == "NONAME")
  }

  test("acceder a un valor con fold"){
    val list=List(Some("Andres"), None, Some("Luis"), Some("Pedro"))
    val nombre:Option[String]=list(0)
    val res=nombre.fold{
      "NONAME"
    }{
      x=> x
    }
    assert(res==="Andres")
  }

  test("Un Option se debe poder transformar con un map") {
    val lista = List(Some("Andres"), None, Some("Luis"), Some("Pedro"))
    val nombre = lista(0)
    val nombreCompleto: Option[String] = nombre.map(s => s + " Felipe")
    assert(nombreCompleto.getOrElse("NONAME") == "Andres Felipe")
  }

  test("Un Option se debe poder transformar con flatMap en otro Option") {
    val lista = List(Some("Andres"), None, Some("Luis"), Some("Pedro"))
    val nombre = lista(0)

    val resultado: Option[String] = nombre.flatMap(s => Option(s.toUpperCase))
    resultado.map( s => assert( s == "ANDRES"))
  }

  test("Un Option se debe poder filtrar con una hof con filter") {
    val lista = List(Some(5), None, Some(40), Some(20))
    val option0 = lista(0)
    val option1 = lista(1)
    val res0 = option0.filter(_>10)
    val res1 = option1.filter(_>10)
    val res2= option0.filter(_<10)

    assert(res0 == None)
    assert(res1 == None)
    assert(res2===Some(5))
  }


  test("for comprehensions en Option") {
    val lista = List(Some(5), None, Some(40), Some(20))
    val s1 = lista(0)
    val s2 = lista(2)

    val resultado = for {
      x <- s1
      y <- s2
    } yield x+y

    assert(resultado == Some(45))
  }

  test("for comprehensions en Option con tres Some") {
    val lista = List(Some(5), None, Some(40), Some(20))
    val s1 = lista(0)
    val s3 = lista(3)
    val s2 = lista(2)

    val resultado = for {
      x <- s1
      y <- s2
      z <- s3
    } yield x+y+z

    assert(resultado == Some(65))
  }

  test("for comprehensions en Option equivalente con flatMap") {
    val o1=Some(1)
    val o2=Some(2)
    val o3=Some(3)
    val res=o1.flatMap{
      x=> o2.flatMap(
        y=> o3.flatMap(
        z=> Option(x+y+z)))
    }


    assert(res == Some(6))
  }

  test("for comprehensions en Option con some and None") {

    def foo(a:Int):Option[Int]={
      println(s"ejecutando foo con ${a}")
      Some(a)

    }

    def bar(a:Int):Option[Int]={
      println(s"ejecutando bar con ${a}")
      None

    }

    val lista = List(Some(5), None, Some(40), Some(20))
    val s1 = lista(0)
    val s3 = lista(3)
    val s2 = lista(2)

    val resultado = for {
      x <- foo(1)
      z <- foo(1)
      a <- foo(1)
      b <- foo(1)
      c <- foo(1)
      d <- foo(1)
      e <- foo(1)
      f <- foo(1)
      y <- bar(1)

    } yield x+y+z

    assert(resultado == None)
  }

  test("for comprehesions None en Option") {
    val consultarNombre = Some("Andres")
    val consultarApellido = Some("Estrada")
    val consultarEdad = None
    val consultarSexo = Some("M")

    val resultado = for {
      nom <- consultarNombre
      ape <- consultarApellido
      eda <- consultarEdad
      sex <- consultarSexo
    //} yield (nom+","+ape+","+eda+","+sex)
    } yield (s"$nom $ape, $eda,$sex")

    assert(resultado == None)
  }

  test("for comprehesions None en Option 2") {

    def consultarNombre(dni:String): Option[String] = Some("Felix")
    def consultarApellido(dni:String): Option[String] = Some("Vergara")
    def consultarEdad(dni:String): Option[String] = None
    def consultarSexo(dni:String): Option[String] = Some("M")

    val dni = "8027133"
    val resultado = for {
      nom <- consultarNombre(dni)
      ape <- consultarApellido(dni)
      eda <- consultarEdad(dni)
      sex <- consultarSexo(dni)
    //} yield (nom+","+ape+","+eda+","+sex)
    } yield (s"$nom $ape, $eda,$sex")

    assert(resultado == None)
  }


  //Tony Morris ejemplos implementación
  //---------------------------------------------------------------------------------------------//

  test("Pattern Match equivalente a flatMap") {
    val lista: Seq[Option[String]] = List(Some("Andres"), None, Some("Luis"), Some("Pedro"))
    val nombre: Option[String] = lista(0)
    var res:Option[String]=Some("")
    res = nombre match {
      case Some(nom) => Some(nom)
      case None => None
    }
    val res2=nombre.flatMap(x=>Option(x))

    assert(res === res2)
  }

  test("Pattern Match equivalente a flatten"){
    val nombre:Option[Option[String]]=Option(Option("Juan"))
    val nombre2:Option[Option[String]]=None
    val res= nombre2 match{
      case Some(Some(nom)) => Some(nom)
      case None=> None
    }
    val res2=nombre2.flatten
    assert(res===res2)
  }

  test("Pattern Match equivalente a map"){
    val nombre:Option[String]=Option("Daniel")
    val res=nombre match {
      case Some(nom)=> Some(s"D${nom}")
      case None=> None
    }
    val res2=nombre.map(x=> s"D${x}")
    assert(res===res2)
  }

  test("Pattern Match equivalente a forEach"){
    val nombre:Option[String]=Option("Daniel")
    var nombreAux:String=""
    val res:String=nombre match{
      case Some(nom)=> nom
      case None => ""
    }
    nombre.foreach(x=> nombreAux=x)
    assert(res===nombreAux)
  }

  test("Pattern Match equivalente a isDefined"){
    val nombre:Option[String]=Option("Daniel")
    val res= nombre match {
      case Some(nom)=> true
      case None=> false
    }
    val res2=nombre.isDefined
    assert(res===res2)
  }

  test("Pattern Match equivalente a isEmpty"){
    val nombre:Option[String]=Option("Daniel")
    val res= nombre match {
      case Some(nom)=> false
      case None=> true
    }
    val res2=nombre.isEmpty
    assert(res===res2)
  }

  test("Pattern Match equivalente a forAll"){
    val nombre:Option[String]=Option("Daniel")
    val res=nombre match {
      case Some(nom)=> {nom.equals("Daniel")}
      case None=> true
    }
    val res2=nombre.forall(x=>x.equals("Daniel"))
    assert(res===res2)
  }

  test("Pattern Match equivalente a exists"){
    val nombre:Option[String]=Option("Daniel")
    val res=nombre match {
      case Some(nom)=> {nom.equals("Daniel")}
      case None=> false
    }
    val res2=nombre.exists(x=> x.equals("Daniel"))
    assert(res===res2)
  }

  test("Pattern March equivalente a orElse"){
    val nombre:Option[String]=Option("Daniel")
    val res=nombre match{
      case Some(nom)=> Some(nom)
      case None=> Some(1)
    }
    val res2=nombre.orElse(Some(1))

    assert(res===res2)

  }

  test("Pattern Match equivalente a getOrElse"){
    val nombre:Option[String]=None
    val res= nombre match {
      case Some(nom)=> nom
      case None=> 0
    }
    val res2=nombre getOrElse(0)
  }

  test("Pattern Match equivalente a toList"){
    val nombre:Option[String]=None
    val res=nombre match {
      case Some(nom)=> List(nom)
      case None=> List()
    }
    val res2=nombre.toList

    assert(res===res2)
  }
  



}


