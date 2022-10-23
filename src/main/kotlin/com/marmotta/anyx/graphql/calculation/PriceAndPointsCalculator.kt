package com.marmotta.anyx.graphql.calculation

import java.math.BigDecimal

fun interface PriceAndPointsCalculator {
    fun calculate(initialPrice: BigDecimal): Int
}

