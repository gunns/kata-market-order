package com.example.demo.core

import java.io.IOException

class MarketOrderManager(
    private val gateway: GatewayMercado,
    private val politica: PoliticaCotizacion,
) {
    suspend fun enviarOrden(orden: Orden): ResultadoOrden =
        try {
            when (val respuesta = gateway.enviar(politica.aplicar(orden))) {
                is Aceptada -> Confirmada(respuesta.idMercado)
                is Denegada -> Rechazada(respuesta.motivo)
            }
        } catch (e: IOException) {
            FalloTecnico(e)
        }
}
