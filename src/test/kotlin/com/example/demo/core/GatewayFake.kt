package com.example.demo.core

import kotlinx.coroutines.awaitCancellation

class GatewayFake(
    private val respuesta: RespuestaMercado? = null,
    private val falla: Throwable? = null,
    private val colgado: Boolean = false,
) : GatewayMercado {
    val ordenesRecibidas = mutableListOf<Orden>()

    override suspend fun enviar(orden: Orden): RespuestaMercado {
        ordenesRecibidas += orden
        if (colgado) awaitCancellation()
        falla?.let { throw it }
        return checkNotNull(respuesta) { "GatewayFake needs a respuesta, a falla or colgado" }
    }
}
