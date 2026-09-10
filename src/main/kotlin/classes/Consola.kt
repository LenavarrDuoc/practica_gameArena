package org.example.classes

class Consola(codigoSesion: String, jugador: String, tipo: TipoJugador, tiempo: Int) : Estacion(codigoSesion, jugador, tipo,
    tarifaBase = 2400,

)

{
    override fun costoBase(): Int {
        if(tipo == TipoJugador.MIEMBRO){
            return (super.costoBase() * 0.8).toInt()   //por 0.8 para descontar 20% del valor calculado.
        }
        return super.costoBase()
    }
}