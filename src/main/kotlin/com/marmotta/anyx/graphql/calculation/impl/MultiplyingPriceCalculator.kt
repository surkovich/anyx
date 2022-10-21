package com.marmotta.anyx.graphql.calculation.impl

import com.marmotta.anyx.graphql.calculation.PriceAndPoints
import com.marmotta.anyx.graphql.calculation.PriceAndPointsCalculator
import java.math.BigDecimal
import java.math.RoundingMode



internal class MultiplyingPriceCalculator(
    private val priceModifier: BigDecimal,
    private val pointsMultiplier: BigDecimal
): PriceAndPointsCalculator {
    override fun calculate(initialPrice: BigDecimal) =
        PriceAndPoints(
            finalPrice = initialPrice *  priceModifier,
            points = (initialPrice * pointsMultiplier).setScale(0, RoundingMode.FLOOR).intValueExact()
        )

}