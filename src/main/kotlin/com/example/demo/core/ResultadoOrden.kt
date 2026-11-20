package com.example.demo.core

sealed interface ResultadoOrden

data class Confirmada(val idMercado: String) : ResultadoOrden

data class Rechazada(val motivo: String) : ResultadoOrden

data class FalloTecnico(val causa: Throwable) : ResultadoOrden
