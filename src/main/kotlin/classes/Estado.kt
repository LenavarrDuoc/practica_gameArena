package org.example.classes

sealed class Estado {
    data object Disponible : Estado()

    data class Ocupada(var estacion: Estacion) : Estado()

    data class Procesando(var motivo : String) : Estado()

    data class FueraDeServicio(var motivo : String) : Estado()
}