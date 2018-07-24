package co.com.scalatraining.historialLaboral

class Historial(cotizacion: List[Cotizacion]) {

  //Ignorar Cotización con IBC en 0 o si hay cotizaciones con días en 0
  def reglaUno(cotizaciones: List[Cotizacion]): List[Cotizacion] ={
    cotizaciones.filter(x=>x.ibc!=0 && x.diasCotizados!=0)
  }

  //calcular el salario mensual para los cotizantes -- recalcular
  def reglaDos(cotizaciones: List[Cotizacion]): List[Cotizacion] = {
    cotizaciones.map(x=>
      Cotizacion(x.periodo,x.aportante,x.diasCotizados,((x.ibc)/(x.diasCotizados))*30)
    )
  }

  //Tomar solo una cotización si el IBC, los dias cotizados, y el aportante son los mismos
  def reglaTres(cotizacion: List[Cotizacion]): List[Cotizacion] =
  {
    cotizacion.distinct
  }

  //Elegir el mayor IBC
  def reglaCuatro(cotizacion: List[Cotizacion])={

    
  }

}
