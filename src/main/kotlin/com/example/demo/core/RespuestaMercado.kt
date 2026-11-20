package com.example.demo.core

sealed interface RespuestaMercado

data class Aceptada(val idMercado: String) : RespuestaMercado

data class Denegada(val motivo: String) : RespuestaMercado
