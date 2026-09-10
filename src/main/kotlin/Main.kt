package org.example

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.example.classes.ArenaEsports
import org.example.classes.Consola
import org.example.classes.Estacion
import org.example.classes.Estado
import org.example.classes.RealidadVirtual
import org.example.classes.Ticket
import org.example.classes.TipoJugador
import org.example.sesionesActivas
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.TimeSource


//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
fun main() {
    runBlocking{

        while (true) {
            println("""
                Menú GameArena:
                1. Sesiones de jugadores MIEMBROS.
                2. Códigos de sesiones finalizadas.
                3. Sesiones realizadas por jugadores PREMIUM.
                4. Ingreso total de la jornada.
                5. Ingreso promedio por sesión.
                6. Sesión con mayor tiempo de utilización.
                7. Sesiones de ArenaEsports.
                8. Sesiones con monto pagado superior a $5000.
                9. Iniciar sesión.
                10. Finalizar sesión.
                11. Imprimir Ticket.
                0. Salir.
                Ingrese opción:
            """.trimIndent())
            var opcion : Int? = readln().toIntOrNull()

            when (opcion) {
                1 -> buscarJugadoresMiembro()
                2 -> buscarSesionesFinalizadas()
                3 -> buscarSesionesRealizadasJugadoresPremium()
                4 -> calcularIngresoTotalJornada()
                5 -> calcularIngresoPromedioPorSesion()
                6 -> buscarSesionMayorTiempo()
                7 -> buscarSesionesArenaEsports()
                8 -> buscarSesionesPagosobreCincoMil()
                9 ->  iniciarSesion(this)
                10 -> finalizarSesion(this)
                11 -> imprimirTicket()
                0 -> break

                else -> println("Opcion invalida. Debe ser una opción numérica del menú.")
            }
            println("Presione una tecla para continuar:")
            readln()
        }

    }

}

//Colecciones:
val sesionesActivas = mutableListOf<Estacion>()
val sesionesFinalizadas = mutableListOf<Estacion>()

val ticketsGenerados = mutableListOf<Ticket>()

val estFisicas = MutableList<Estado>(8) { Estado.Disponible }


//FUNCIONES:
    //Funciones para consultas:
//Consulta 1: Búsqueda de jugadores MIEMBRO en sesiones activas.
fun buscarJugadoresMiembro() {
    println("Jugadores MIEMBRO en sesiones activas:")
    sesionesActivas.filter { it.tipo == TipoJugador.MIEMBRO }.forEach { print("\n---------------\n-Código: ${it.codigoSesion}. \n-Jugador: ${it.jugador}. \n-Tipo: ${it.tipo}.") }
    println("\n==========================================")
    println("Jugadores MIEMBRO en sesiones finalizadas:")
    sesionesFinalizadas.filter { it.tipo == TipoJugador.MIEMBRO }.forEach { print("\n---------------\n-Código: ${it.codigoSesion}. \n-Jugador: ${it.jugador}. \n-Tipo: ${it.tipo}.") }

}

//Consulta 2: Búsqueda de sesiones finalizadas:
fun buscarSesionesFinalizadas() {
    val codigos = sesionesFinalizadas.map { it.codigoSesion }
    print("CódigosSesiones finalizadas:")
    println("$codigos")

}

//Consulta 3: Búsqueda de jugadores PREMIUM en sesiones finalizadas:
fun buscarSesionesRealizadasJugadoresPremium(){
    val sesiones = sesionesFinalizadas.count { it.tipo == TipoJugador.PREMIUM }
    println("Sesiones realizadas por jugadores premium: $sesiones.")
}

//Consulta 4: Calcula ingreso total de la jornada
fun calcularIngresoTotalJornada() {
    var accum: Int = 0


    if(sesionesFinalizadas.isNotEmpty()) {
        accum = sesionesFinalizadas.sumOf { it.costoFinal() }
    }
    println("Total jornada: $${accum}.")
}

//Consulta 5: calcula ingreso promedio por sesión:
fun calcularIngresoPromedioPorSesion() {
    var accum: Int = 0
    var prom: Int = 0

    if(sesionesFinalizadas.isNotEmpty()) {
        accum = sesionesFinalizadas.sumOf { it.costoFinal() }
        prom = accum / sesionesFinalizadas.size
    }
    println("Monto promedio jornada: $${prom}")
}

//Consulta 6: Busca sesión con el mayor tiempo de registrado.
fun buscarSesionMayorTiempo(){
    val sesionMaxTime = sesionesFinalizadas.maxByOrNull { it.tiempo }
    if(sesionMaxTime != null) {
         println("""Sesión finalizada con mayor tiempo:
             Código: ${sesionMaxTime.codigoSesion}.
             Tiempo: ${sesionMaxTime.tiempo} mins.
         """.trimMargin())
    } else{
        println("No hay sesiones finalizadas.")
    }

}

//Consulta 7:Busca si existe, al menos, una sesión (activa y/o finalizada) en ArenaESports:
fun buscarSesionesArenaEsports() {
    if (sesionesActivas.any { it is ArenaEsports } || sesionesFinalizadas.any { it is ArenaEsports }){
        println("Existe al menos una sesión en estación ArenaEsports.")
    }else{
        println("No existen sesiones en estación ArenaEsports.")
    }

}

//Consulta 8: Muestra sesiones cuyo monto pagado sea superior a $5000":
fun buscarSesionesPagosobreCincoMil(){
    if (ticketsGenerados.isNotEmpty()) {
        val sesiones = ticketsGenerados.filter { it.costoFinal > 5000 }
        if (sesiones.isNotEmpty()) {
            println("Sesiones finalizadas con monto pagado superior a $5000:")
            sesiones.forEach { println("\n------------------\n-Código de sesión:${it.codigoSesion}: \n-Monto pagado: $${it.costoFinal}") }

        }else{
            println("No hay sesiones finalizadas con monto pagado superior a $5000")
        }
    }else
        println("No hay sesiones finalizadas aún.")
}

//Consulta 11: Imprimir Ticket:

fun imprimirTicket(){
    println("Ingrese un código de sesión:")
    if (mostrarTicketsGenerados()){
        println("Ingrese el código de sesión a finalizar.")
        val codigo : String = readln().trim()
        ticketsGenerados.find { it.codigoSesion == codigo }?.mostrarTicket()

    }
}

//Sesiones Activas:
fun buscarSesionesActivas() : Boolean{
    if (sesionesActivas.isNotEmpty()) {
        val codigos = sesionesActivas.map { it.codigoSesion }
        print("Códigos Sesiones activas:")
        println("\n$codigos")
        return true
    }else{
        println("No hay sesiones activas aún.")
        return false
    }

}

fun mostrarTicketsGenerados() : Boolean{
    if (ticketsGenerados.isNotEmpty()) {
        val codigos = ticketsGenerados.map { it.codigoSesion }
        print("Códigos Tickets generados:")
        println("\n$codigos")
        return true
    }else{
        println("No hay tickets generados aún.")
        return false
    }

}

//CORRUTINAS:
fun iniciarSesion(scope: CoroutineScope) {
    println("Iniciar sesión para estación.")
    val estacionDisponible = estFisicas.indexOfFirst { it is Estado.Disponible }

    if (estacionDisponible == -1) {
        println("No hay estaciones disponibles por ahora.")
    }else{
        println("Estación nro: ${estacionDisponible + 1}  = disponible y asignada.")
        var tipoEstacion : Int = 0
        var opcion : Int?
        var tipo : TipoJugador
        while(true){
            println("""
                Seleccione el tipo de estación a establecer:
                1. Consola.
                2. Realidad virtual.
                3. ArenaEsports.
                0. Salir
                Ingrese una opción:
                """.trimIndent())
            opcion = readln().trim().toIntOrNull()
            when (opcion){
                1 -> tipoEstacion = 1
                2 -> tipoEstacion = 2
                3 -> tipoEstacion = 3
                0 -> break
                else -> println("Opción no válida. Ingrese una de las opciones del menú:")

                }
            if (opcion == 1 || opcion == 2 || opcion == 3) {
                break
            }
        }

        if (opcion != 0){
            println("Ingrese código de sesión (ej: AAA11A):")
            val codigoSesion = readln().trim()

            println("Ingrese Nombre de jugador")
            val jugador = readln().trim()

            while(true){
                println("""
                    Seleccione el tipo de jugador:
                    1. OCASIONAL.
                    2. MIEMBRO.
                    3. PREMIUM.
                    0. Salir.
                    Ingrese una opción:
                    """.trimIndent())
                opcion = readln().trim().toIntOrNull()

                if (opcion == 1 || opcion == 2 || opcion == 3 || opcion == 0) {
                    break
                } else{
                    println("Opción Inválida. Debe elegir una opción del menú.")
                }
            }
            if (opcion !=0){
                tipo = when (opcion){
                    1 -> TipoJugador.OCASIONAL
                    2 -> TipoJugador.MIEMBRO
                    else -> TipoJugador.PREMIUM
                }

                try {

                    val sesionEstacion : Estacion = when (tipoEstacion){
                        1 -> Consola(codigoSesion, jugador, tipo, tiempo = 0)
                        2 -> RealidadVirtual(codigoSesion,jugador,tipo, tiempo = 0, )
                        else -> {
                            //Tenía el valor reqEqPro dentro del while, al igual que la instanciación de ArenaEsports, por lo que no se retornaba la instanciación fuera del while. También tuve que sacar la instanciación fuera y luego del while.
                            val reqEqPro : Boolean
                            while(true){
                                println(
                                    """
                                ¿Requiere equipo profesional?
                                1. Sí.
                                2. No.
                                """.trimMargin()
                                )
                                opcion = readln().trim().toIntOrNull()
                                if (opcion == 1 || opcion == 2) {

                                    reqEqPro = when (opcion) { //
                                        1 -> true
                                        else -> false
                                    }

                                    break
                                } else {
                                    println("Opción incorrecta. Debe elegir una opción del menú.")

                                }
                            }
                            ArenaEsports(
                                codigoSesion,
                                jugador,
                                tipo,
                                reqEqPro

                            )
                        }
                    } as Estacion
                    scope.launch(Dispatchers.IO) { //Si no se despacha por Input/Output, la corrutina no se lanza por quedar congelada a causa del readln() puesto para presionar enter para continnuar.
                        print("""Conectando con dispositivo físico en Estación nro ${estacionDisponible + 1}.
                            La sesión estará lista dentro de algunos segundos.
                        """.trimMargin())
                        instanciarEstacion(estacionDisponible, sesionEstacion)
                    }
                } catch (e: IllegalArgumentException) {
                    println("Error al crear sesión. \n${e.message}")
                }
            }

        }
    }
}

suspend fun instanciarEstacion(estacionDisponible: Int, sesionEstacion: Estacion) {
    estFisicas[estacionDisponible] = Estado.Procesando("Iniciando sesión de $sesionEstacion...")
    //print("Conectando con dispositivo físico en Estación nro ${estacionDisponible + 1}.")
    delay(10000.milliseconds)

    //Se cambia estado de est física a ocupada y asociada a la estación
    estFisicas[estacionDisponible] = Estado.Ocupada(sesionEstacion)
    sesionEstacion.tInit = TimeSource.Monotonic.markNow()
    //print("Sesión de Estación nro ${estacionDisponible + 1} establecida. \nTiempo de inicio: ${sesionEstacion.tInit}.\n")


    //Se agrega a sesión activa:
    sesionesActivas.add(sesionEstacion)
}

fun finalizarSesion(scope : CoroutineScope){
    println("Seleccione una Estación activa a finalizar:")
    if (buscarSesionesActivas()){
        println("Ingrese el código de sesión a finalizar.")
        val codigo : String = readln().trim()
        if(codigo.isNotBlank()){
            println("Finalizando sesión...")

            scope.launch(Dispatchers.IO) { //Si no se despacha por Input/Output, la corrutina no se lanza por quedar congelada a causa del readln() puesto para presionar enter para continnuar.
                finishSession(codigo)
        }

        }
    }
}

suspend fun finishSession(codigo : String){
    delay(5000.milliseconds)

    val estacion = sesionesActivas.find { estacion -> estacion.codigoSesion == codigo } //referenciamos el objeto clase estación con código brindado.
    if(estacion != null && sesionesActivas.removeIf { estacion -> estacion.codigoSesion == codigo }) { //busca y remueve por código si es que existe.
        val marcaT = estacion.tInit
        if (marcaT != null){
            estacion.tiempo = marcaT.elapsedNow().inWholeMinutes.toInt()
        }
        sesionesFinalizadas.add(estacion)
        generarTicket(estacion)
        //println("Sesión código: ${codigo} finalizada.")
        //println("Liberando estación...")
        delay(5000.milliseconds)
        val index = estFisicas.indexOfFirst { estado -> estado is Estado.Ocupada && estado.estacion.codigoSesion == codigo }
        estFisicas[index] = Estado.Disponible
        //println("Estación nro ${index + 1} liberada y disponible.")
    }
}


suspend fun generarTicket(estacion: Estacion){
    //println("Generando ticket para estación código ${estacion.codigoSesion}...")
    delay(5000.milliseconds)
    if (sesionesFinalizadas.contains(estacion)) {
        val ticket = Ticket(estacion)
        ticketsGenerados.add(ticket)
    }
}


