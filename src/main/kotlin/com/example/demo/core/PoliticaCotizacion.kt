package com.example.demo.core

interface PoliticaCotizacion {
    suspend fun aplicar(orden: Orden): Orden
}
