package co.com.scalatraining.collections

import org.scalatest.FunSuite

import scala.collection.immutable.Queue

class QueueSuite extends FunSuite{

  test("create queue"){
    val queue=Queue()
    val queue2=Queue(1,2,3,4,5)
    val queue3=Queue(1,2,3,4,5)
    assert(queue!=queue2 && queue2==queue3)
  }

  test("encolar"){
    val queue=Queue(1,2,3,4,5)
    assertResult(Queue(1,2,3,4,5,6)){
      queue.enqueue(6)
    }
  }

  test("desencolar"){
    val queue=Queue(1,2,3,4,5,6)
    assertResult((1,Queue(2,3,4,5,6))){
      queue.dequeue
    }
  }

  test("reverse"){
    assertResult(Queue(5,4,3,2,1)){
      Queue(1,2,3,4,5).reverse
    }
  }

  test("map"){
    assertResult(Queue(2,4,6,8,10)){
      Queue(4,8,12,16,20).map(x=>x/2)
    }
  }

  test("filter"){
    assertResult(Queue(3,6,9)){
      Queue(3,6,9).filter(x=>x%3==0)
    }
  }

  test("contains"){
    assertResult(true){
      Queue(3,6,9).contains(3)
    }
  }

  test("find"){
    assertResult(Some(2)){
      Queue(1,2,3,4).find(x=>x%2==0)
    }
  }

  test("size"){
    assertResult(2){
      Queue(1,2).size
    }
  }
}
