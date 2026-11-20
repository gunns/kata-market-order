package com.example.demo.core

import java.math.BigDecimal

enum class Lado { COMPRA, VENTA }

data class Orden(
    val activo: String,
    val lado: Lado,
    val cantidad: Int,
    val precioLimite: BigDecimal?,
)
