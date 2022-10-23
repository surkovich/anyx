package com.marmotta.anyx.graphql.calculation.impl

import com.marmotta.anyx.graphql.calculation.PriceAndPointsCalculator
import java.math.BigDecimal
import java.math.RoundingMode



internal class MultiplyingPriceAndPointsCalculator(

    private val pointsMultiplier: BigDecimal
): PriceAndPointsCalculator {
    override fun calculate(initialPrice: BigDecimal) =
        (initialPrice * pointsMultiplier).setScale(0, RoundingMode.FLOOR).intValueExact()
}