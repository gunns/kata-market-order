package com.example.demo.infrastructure

import com.example.demo.core.Orden
import com.example.demo.core.PoliticaCotizacion

class ValidacionObligatoria(val reintentos: Int) : PoliticaCotizacion {
    override suspend fun aplicar(orden: Orden): Orden = orden
}
