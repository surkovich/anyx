package com.marmotta.anyx.graphql.calculation

import java.math.BigDecimal

fun interface PriceAndPointsCalculator {
    fun calculate(initialPrice: BigDecimal): PriceAndPoints
}

data class PriceAndPoints(
    val finalPrice: BigDecimal,
    val points: Int
)


