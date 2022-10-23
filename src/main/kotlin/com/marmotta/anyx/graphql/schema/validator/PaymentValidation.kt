package com.marmotta.anyx.graphql.schema.validator

import com.marmotta.anyx.graphql.shared.model.Courier
import com.marmotta.anyx.graphql.schema.models.PaymentRegistrationDTO
import com.marmotta.anyx.graphql.shared.model.PaymentMethod
import com.marmotta.anyx.graphql.schema.validator.rule.*
import java.math.BigDecimal

object PaymentValidation {

    private val suitableRules: Map<PaymentMethod, ValidationRule> =
        PaymentMethod.values().associateWith{it.suitableValidator()}

    /**
     * use exhaustive when here, without else branches - if someone forget to add a new payment methods validation -
     * we will see it on the compilation stage
     */
    private fun PaymentMethod.suitableValidator() =
        when(this) {
            PaymentMethod.CASH_ON_DELIVERY -> CourierServiceShouldBeRestricted(
                couriers = setOf(Courier.SAGAWA, Courier.YAMATO),
                priceModifierBounds = ValidationRule.PriceModifierBounds(BigDecimal("0.9"), BigDecimal("1"))
            )
            PaymentMethod.CASH -> NoAdditionalItemsValidationRequired(
                priceModifierBounds = ValidationRule.PriceModifierBounds(BigDecimal("1"), BigDecimal("1.02"))
            )
            PaymentMethod.VISA, PaymentMethod.MASTERCARD -> LastFourDigitsOfCardRequired(
                priceModifierBounds = ValidationRule.PriceModifierBounds(BigDecimal("0.95"), BigDecimal("1"))
            )
            PaymentMethod.AMEX -> LastFourDigitsOfCardRequired(
                priceModifierBounds = ValidationRule.PriceModifierBounds(BigDecimal("0.98"), BigDecimal("1.01"))
            )
            PaymentMethod.JCB -> LastFourDigitsOfCardRequired(
                priceModifierBounds = ValidationRule.PriceModifierBounds(BigDecimal("0.95"), BigDecimal("1"))
            )
            PaymentMethod.LINE_PAY, PaymentMethod.PAYPAY, PaymentMethod.POINTS, PaymentMethod.GRAB_PAY ->
                NoAdditionalItemsValidationRequired(
                    priceModifierBounds = ValidationRule.PriceModifierBounds(BigDecimal("1"), BigDecimal("1"))
            )
            PaymentMethod.BANK_TRANSFER -> BankNameAndChequeRequired(
                priceModifierBounds = ValidationRule.PriceModifierBounds(BigDecimal("1"), BigDecimal("1"))
            )
            PaymentMethod.CHEQUE ->  BankNameAndChequeRequired(
                priceModifierBounds = ValidationRule.PriceModifierBounds(BigDecimal("0.9"), BigDecimal("1"))
            )
        }

    fun of(payment: PaymentRegistrationDTO): PaymentValidationDetails {

        val applicableRule = suitableRules[payment.paymentMethod]?: return PaymentValidationDetails.Valid
        //We'll use exhaustive when here instead of if-else construction
        //to prevent us from losing options, when response from 'applicableRule.check' is
        //unexpectedly extended by adding new possibilities.
        //This is just illustration of such approach and for validation may look a bit over-engineering
        return when(val result = applicableRule.validate(payment)) {
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