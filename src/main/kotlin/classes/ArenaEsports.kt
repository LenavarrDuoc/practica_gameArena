package org.example.classes

class ArenaEsports(codigoSesion: String, jugador: String, tipo: TipoJugador, var reqEqProf: Boolean = false

) : Estacion(codigoSesion, jugador, tipo,
    tarifaBase = 6000
)
{

    override fun costoBase(): Int {
        if(reqEqProf) {
            return (super.costoBase() * 1.3).toInt()
        }
        return super.costoBase()
    }
}