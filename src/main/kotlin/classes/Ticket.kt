package org.example.classes

import java.time.LocalDateTime

data class Ticket (val codigoSesion : String, val fecha : LocalDateTime = LocalDateTime.now(), val tipoEstacion : String, val jugador: String, val tipo : TipoJugador, val tiempo : Int, val costoFinal : Int) {

    constructor(estacion: Estacion) : this( //2do constructor toma un objeto del tipo Estacion como parámetro de entrada.
        codigoSesion = estacion.codigoSesion,
        tipoEstacion = estacion::class.simpleName ?: "Estacion", //Si no detecta nombre de tipo de subclase, lo deja como "Estacion".
        jugador = estacion.jugador,
        tipo = estacion.tipo,
        tiempo = estacion.tiempo,
        costoFinal = estacion.costoFinal()
    )

    fun mostrarTicket(){
        println("""
            ===== TICKET SESIÓN: $codigoSesion ====
            -fecha: $fecha.
            -Estación: $tipoEstacion.
            -Jugador: $jugador.
            -Tipo de jugador: $tipo.
            -Tiempo de sesión: $tiempo mins.
            -Costo total: $$costoFinal.
            =============================
        """.trimIndent())
    }
}