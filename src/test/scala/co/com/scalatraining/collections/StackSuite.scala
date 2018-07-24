package co.com.scalatraining.collections

import org.scalatest.FunSuite

import scala.collection.immutable.Stack

class StackSuite extends FunSuite{

  test("construir pila"){
    val pila: Stack[Int] =Stack()
    val pila2: Stack[Int]= Stack(1,2,3,4,5)
    val pila3:Stack[Int]= Stack(1,2,3,4,5)
    assert(pila2===pila3)
  }

  test("pop in stack: remover el ultimo elemento de la pila"){

    val pila=Stack(1,2,3,4,5)
    assertResult(Stack(2,3,4,5)){
      pila.pop
    }
  }

  test("push in stack: apilar un elemento"){
    val pila=Stack(1,2,3,4)
    assertResult(Stack(6,5,1,2,3,4)){
      pila.push(5,6)
    }

  }

  test("reverse in stack: Reversar una pila"){
    val pila=Stack(1,2,3,4,5)
    assertResult(Stack(5,4,3,2,1)){
      pila.reverse
    }
  }

  test("mapReverse in stack: Aplicar una funcion y reversarla"){
    val pila=Stack(1,2,3,4,5)
    assertResult(Stack(6,5,4,3,2)){
      pila.reverseMap(x => x + 1)
    }
  }

  test("product: multiplicar elementos de la pila"){
    val pila=Stack(1,2,3,4,5)
    assertResult(120){
      pila.product
    }
  }

  test("max: encontrar el elemento más grande"){
    val pila=Stack(1,2,3,4,5)
      assertResult(5){
        pila.max
    }
  }

  test("map: ejecutar una funcion sobre todos los datos."){
    val pila=Stack(1,2,3,4,5)
    assertResult(Stack(2,3,4,5,6)){
      pila.map(x => x+1 )
    }
  }

  test("obtener el tamaño de la pila"){
    val pila=Stack(1,2,3,4,5)
    assertResult(5){
      pila.length
    }
  }

  test("GroupBy use"){
    val pila=Stack(1,2,3,4,5,5)
    assertResult(Map(5 -> Stack(5, 5), 1 -> Stack(1), 2 -> Stack(2), 3 -> Stack(3), 4 -> Stack(4))){
      pila.groupBy(identity)
    }
  }

  test("filter"){
    val pila=Stack(1,2,3,4,5,6)
    assertResult(Stack(2,4,6)){
      pila.filter(x=>x%2==0)
    }
  }

  test("filterNot"){
    val pila=Stack(1,2,3,4,5,6)
    assertResult(Stack(1,3,5)){
      pila.filterNot(x=>x%2==0)
    }
  }

  test("exist use"){
    val pila=Stack(1,2,3,4,5)
    assertResult(true){
      pila.exists(x=> x%2==0)
    }
  }

  test("count use"){
    val pila=Stack(1,2,3,4,5)
    assertResult(3){
      pila.count(x=>x%2!=0)
    }
  }

  test("contains"){
    val pila=Stack(1,2,3,4,5)
    assertResult(true){
      pila.contains(5)
    }
  }

  test("apply use"){
    val pila=Stack(1,2,3,4,5)
    assertResult(5){
      pila.apply(4)
    }
  }
  test("diference"){
    assertResult(Stack()){
      Stack(1,2,3,4,5) diff(Stack(1,2,3,4,5))
    }
  }

  test("find use"){
    assertResult(Some(1)){
      Stack(1,1,1,1,2).find(x=>x%2!=0)
    }
  }



}
