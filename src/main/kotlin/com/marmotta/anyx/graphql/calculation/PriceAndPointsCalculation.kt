package com.marmotta.anyx.graphql.calculation

import com.marmotta.anyx.graphql.calculation.impl.MultiplyingPriceAndPointsCalculator
import com.marmotta.anyx.graphql.shared.model.PaymentMethod
import java.math.BigDecimal
object PriceAndPointsCalculation {

    private val calculators: Map<PaymentMethod, PriceAndPointsCalculator> =
        PaymentMethod.values().associateWith { it.suitableCalculator() }

    private fun PaymentMethod.suitableCalculator() : PriceAndPointsCalculator =
    // Maybe not the most elegant solution, but safe for new payment methods adding.
    // If we'll forget about adding new algorithm we will see it on the syntax level,
    // far before this mistake reach any stand
    //This approach is not used for validation, but maybe it can be.
    // At this point validation doesn't seem so critical. But if so - we can always
    //add some AlwaysTrueValidator and use exhaustive when even there.
    when(this) {
        PaymentMethod.CASH -> MultiplyingPriceAndPointsCalculator(BigDecimal("0.05"))
        PaymentMethod.CASH_ON_DELIVERY -> MultiplyingPriceAndPointsCalculator(BigDecimal("0.05"))
        PaymentMethod.VISA -> MultiplyingPriceAndPointsCalculator(BigDecimal("0.03"))
        PaymentMethod.MASTERCARD -> MultiplyingPriceAndPointsCalculator(BigDecimal("0.03"))
        PaymentMethod.AMEX -> MultiplyingPriceAndPointsCalculator(BigDecimal("0.02"))
        PaymentMethod.JCB -> MultiplyingPriceAndPointsCalculator(BigDecimal("0.05"))
        PaymentMethod.LINE_PAY -> MultiplyingPriceAndPointsCalculator(BigDecimal("0.01"))
        PaymentMethod.PAYPAY -> MultiplyingPriceAndPointsCalculator(BigDecimal.ZERO)
        PaymentMethod.POINTS -> MultiplyingPriceAndPointsCalculator(BigDecimal.ZERO)
        PaymentMethod.GRAB_PAY -> MultiplyingPriceAndPointsCalculator(BigDecimal("0.01"))
        PaymentMethod.BANK_TRANSFER -> MultiplyingPriceAndPointsCalculator(BigDecimal.ZERO)
        PaymentMethod.CHEQUE -> MultiplyingPriceAndPointsCalculator(BigDecimal.ZERO)
    }

    //TODO rewrite if have time, 3 parameters is too much
    //Shouldn't calculate final price here
    fun of(price: BigDecimal, priceModifier: BigDecimal, paymentMethod: PaymentMethod): PriceAndPoints =
        calculators[paymentMethod]!!.calculate(price, priceModifier)
}

