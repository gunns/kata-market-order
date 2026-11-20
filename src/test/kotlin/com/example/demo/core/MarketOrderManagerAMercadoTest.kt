package com.example.demo.core

import com.example.demo.infrastructure.AMercado
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import java.io.IOException
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class MarketOrderManagerAMercadoTest {

    @Test
    fun `una orden a mercado aceptada devuelve la confirmacion del mercado`() = runTest {
        val gateway = GatewayFake(respuesta = Aceptada(idMercado = "MKT-1"))
        val manager = MarketOrderManager(gateway, AMercado())
        val orden = Orden("GGAL", Lado.COMPRA, cantidad = 100, precioLimite = null)

        val resultado = manager.enviarOrden(orden)

        assertEquals(Confirmada(idMercado = "MKT-1"), resultado)
    }

    @Test
    fun `una orden a mercado denegada por el mercado devuelve el motivo del rechazo`() = runTest {
        val gateway = GatewayFake(respuesta = Denegada(motivo = "Saldo insuficiente"))
        val manager = MarketOrderManager(gateway, AMercado())
        val orden = Orden("GGAL", Lado.COMPRA, cantidad = 100, precioLimite = null)

        val resultado = manager.enviarOrden(orden)

        assertEquals(Rechazada(motivo = "Saldo insuficiente"), resultado)
    }

    @Test
    fun `una falla tecnica del gateway devuelve FalloTecnico sin reintentar`() = runTest {
        val causa = IOException("Connection reset")
        val gateway = GatewayFake(falla = causa)
        val manager = MarketOrderManager(gateway, AMercado())
        val orden = Orden("GGAL", Lado.COMPRA, cantidad = 100, precioLimite = null)

        val resultado = manager.enviarOrden(orden)

        assertEquals(FalloTecnico(causa), resultado)
        assertEquals(1, gateway.ordenesRecibidas.size)
    }

    @Test
    fun `cancelar la coroutine durante el envio propaga la cancelacion`() = runTest {
        val gateway = GatewayFake(colgado = true)
        val manager = MarketOrderManager(gateway, AMercado())
        val orden = Orden("GGAL", Lado.COMPRA, cantidad = 100, precioLimite = null)

        var resultado: ResultadoOrden? = null
        val envio = launch { resultado = manager.enviarOrden(orden) }
        runCurrent()
        envio.cancel()
        envio.join()

        assertTrue(envio.isCancelled)
        assertNull(resultado)
        assertEquals(1, gateway.ordenesRecibidas.size)
    }
}
