package com.example.demo.core

interface GatewayMercado {
    suspend fun enviar(orden: Orden): RespuestaMercado
}
