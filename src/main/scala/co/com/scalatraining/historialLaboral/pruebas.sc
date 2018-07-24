

import co.com.scalatraining.historialLaboral.Cotizacion

import scala.collection.immutable.Stack

val cotizacion:Cotizacion=Cotizacion("2018/07","S4N",10,1000000)
val cotizacion2:Cotizacion=Cotizacion("2018/07","S4N",15,1500000)
val cotizaciones:List[Cotizacion]=List(cotizacion,cotizacion2)

cotizaciones.filter(x=>x.ibc!=0 && x.diasCotizados!=0)