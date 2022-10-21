package com.marmotta.anyx.graphql.repository

import com.marmotta.anyx.graphql.shared.model.Courier
import com.marmotta.anyx.graphql.shared.model.PaymentMethod
import java.math.BigDecimal
import java.time.OffsetDateTime

data class Payment (
    val dateTime: OffsetDateTime,
    val customerId: Long,
    val initialPrice: BigDecimal,
    val finalPrice: BigDecimal,
    val paymentMethod: PaymentMethod,

)

sealed class MethodDependedDetails(
    open val paymentMethod: PaymentMethod,

) {
    data class CashOnDelivery(
        val courierService: Courier
    ): MethodDependedDetails(paymentMethod = PaymentMethod.CASH_ON_DELIVERY)

    sealed class CardPayment(
        open val lastFourDigits: Int,
        override val paymentMethod: PaymentMethod
    ): MethodDependedDetails(paymentMethod = paymentMethod) {
        data class Visa(
            override val lastFourDigits: Int
        ): CardPayment(paymentMethod = PaymentMethod.VISA, lastFourDigits = lastFourDigits)
        data class Mastercard(
            override val lastFourDigits: Int
        ): CardPayment(paymentMethod = PaymentMethod.MASTERCARD, lastFourDigits = lastFourDigits)
        data class Amex(
            override val lastFourDigits: Int
        ): CardPayment(paymentMethod = PaymentMethod.AMEX, lastFourDigits = lastFourDigits)
        data class Jcb(
            override val lastFourDigits: Int
        ): CardPayment(paymentMethod = PaymentMethod.JCB, lastFourDigits = lastFourDigits)

    }
}