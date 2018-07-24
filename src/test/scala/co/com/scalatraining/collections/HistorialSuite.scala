package co.com.scalatraining.collections

import co.com.scalatraining.historialLaboral.{Cotizacion, Historial}
import org.scalatest.FunSuite

class HistorialSuite extends FunSuite{

  test("prueba regla 1"){
    val cotizacion:Cotizacion=Cotizacion("2018/07","S4N",10,1000000)
    val cotizacion2:Cotizacion=Cotizacion("2018/07","S4N",15,1500000)
    val cotizacion3:Cotizacion=Cotizacion("2018/08","S4N",0,0)
    val cotizaciones:List[Cotizacion]=List(cotizacion,cotizacion2,cotizacion3)
    val historial: Historial=new Historial(cotizaciones)

    assertResult(List(Cotizacion("2018/07","S4N",10,1000000),Cotizacion("2018/07","S4N",15,1500000))){
      historial.reglaUno(cotizaciones)
    }
  }

  test("prueba regla 2"){
    val cotizacion:Cotizacion=Cotizacion("2018/07","S4N",10,1000000)
    val cotizacion2:Cotizacion=Cotizacion("2018/07","S4N",15,1500000)
    val cotizaciones:List[Cotizacion]=List(cotizacion,cotizacion2)
    val historial: Historial=new Historial(cotizaciones)
    assertResult(List(Cotizacion("2018/07","S4N",10,3000000),Cotizacion("2018/07","S4N",15,3000000))){
      historial.reglaDos(cotizaciones)
    }
  }


}
