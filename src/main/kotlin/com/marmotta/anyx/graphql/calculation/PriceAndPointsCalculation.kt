package com.marmotta.anyx.graphql.calculation

import com.marmotta.anyx.graphql.shared.model.PaymentMethod
import java.math.BigDecimal

object PriceAndPointsCalculation {

    fun of(price: BigDecimal, paymentMethod: PaymentMethod): PriceAndPoints =
        // Maybe not the most elegant solution, but safe for new payment methods adding.
        // If we'll forget about adding new algorithm we will see it on the syntax level,
        // far before this mistake reach any stand
        when(paymentMethod) {
            PaymentMethod.CASH -> TODO()
            PaymentMethod.CASH_ON_DELIVERY -> TODO()
            PaymentMethod.VISA -> TODO()
            PaymentMethod.MASTERCARD -> TODO()
            PaymentMethod.AMEX -> TODO()
            PaymentMethod.JCB -> TODO()
            PaymentMethod.LINE_PAY -> TODO()
            PaymentMethod.PAYPAY -> TODO()
            PaymentMethod.POINTS -> TODO()
            PaymentMethod.GRAB_PAY -> TODO()
            PaymentMethod.BANK_TRANSFER -> TODO()
            PaymentMethod.CHEQUE -> TODO()
        }



}