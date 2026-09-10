package org.example.classes


class RealidadVirtual(codigoSesion: String, jugador: String, tipo: TipoJugador, tiempo: Int) : Estacion(codigoSesion, jugador, tipo,
    tarifaBase = 4500,

)
{
    override fun costoBase(): Int {
        if (tiempo < 20){
            return 0
        }
        return super.costoBase()
    }
}