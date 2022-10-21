package com.marmotta.anyx.graphql.schema.validator

import com.marmotta.anyx.graphql.schema.models.Courier
import com.marmotta.anyx.graphql.schema.models.Payment
import com.marmotta.anyx.graphql.schema.models.PaymentMethod
import com.marmotta.anyx.graphql.schema.validator.rule.*

object PaymentValidation {

    private val appliableRules: Map<PaymentMethod, ValidationRule> = mapOf(
        PaymentMethod.CASH_ON_DELIVERY to CourierServiceShouldBeRestricted(setOf(Courier.SAGAWA, Courier.YAMATO)),
        PaymentMethod.VISA to LastFourDigitsOfCardRequired,
        PaymentMethod.MASTERCARD to LastFourDigitsOfCardRequired,
        PaymentMethod.AMEX to LastFourDigitsOfCardRequired,
        PaymentMethod.JCB to LastFourDigitsOfCardRequired,
        PaymentMethod.BANK_TRANSFER to BankNameAndAccountRequired,
        PaymentMethod.CHEQUE to BankNameAndChequeRequired
    )

    fun of(payment: Payment): PaymentValidationDetails {
        val applicableRule = appliableRules[payment.paymentMethod]?: return PaymentValidationDetails.Valid
        //We'll use exhaustive when here instead of if-else construction
        //to prevent us from losing options, when response from 'applicableRule.check' is
        //unexpectedly extended by adding new possibilities.
        //This is just illustration of such aproach and for validation looks a bit over-engineering
        return when(val result = applicableRule.check(payment)) {
            is ValidationResult.Valid -> PaymentValidationDetails.Valid
            is ValidationResult.Errors -> PaymentValidationDetails.Errors(
                description = "The following error(s) encountered while using the ${payment.paymentMethod} " +
                        "payment method: ${result.errors}"
            )
        }
    }
}

sealed interface PaymentValidationDetails {
    object Valid: PaymentValidationDetails
    data class Errors(
        val description: String
    ): PaymentValidationDetails
}