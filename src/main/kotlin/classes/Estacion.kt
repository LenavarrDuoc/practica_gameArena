package org.example.classes

abstract class Estacion(val codigoSesion: String, val jugador : String, val tipo: TipoJugador, val tarifaBase : Int, var tInit : kotlin.time.TimeMark? = null, var tiempo : Int = 0) {
    init {
        require(codigoSesion.isNotBlank() && codigoSesion.length == 6 && codigoSesion.matches(Regex("^[A-Za-z]{3}\\d{2}[A-Za-z]$"))){
            "Codigo de Sesión no puede estar en blanco y debe ser 2 letras, 2 números, 1 letra. Ej: AA11A."
        }
        require(jugador.isNotBlank() && jugador.length > 2){
            "Nombre de jugador debe ser de al menos 3 caracteres de largo."
        }
        require(tarifaBase > 0){
            "Tarifa base debe ser igual o superior a 0"
        }
        require(tiempo >= 0){}
    }

    open fun costoBase() : Int{
        var costo = tarifaBase * tiempo / 60
        return costo
    }

    open fun costoFinal() : Int{
        var ivaMult : Double = 1.19
        var costoF : Int = (costoBase() * ivaMult).toInt()
        if(tipo == TipoJugador.PREMIUM){
            return (costoF * 0.6).toInt()
        } else {
            return costoF
        }

    }

}