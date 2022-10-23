package com.marmotta.anyx.graphql.schema.validator.rule

import com.marmotta.anyx.graphql.schema.models.PaymentRegistrationDTO
import java.math.BigDecimal

abstract class ValidationRule {

    abstract val priceModifierBounds: PriceModifierBounds

    fun validate(payment: PaymentRegistrationDTO): ValidationResult {
        val errorEncountered = listOfNotNull(checkAdditionalItems(payment), checkPriceModifier(payment))
        return if (errorEncountered.isEmpty()) {
            ValidationResult.Valid
        } else {
            ValidationResult.Errors(errorEncountered.joinToString(separator = "; "))
        }
    }

    protected abstract fun checkAdditionalItems(payment: PaymentRegistrationDTO): String?

    fun checkPriceModifier(payment: PaymentRegistrationDTO) : String? =
        if (payment.priceModifier < priceModifierBounds.min) {
            "price_modifier for ${payment.paymentMethod} should be within " +
                    "[${priceModifierBounds.min}, ${priceModifierBounds.max}]"
        } else null


    data class PriceModifierBounds(
        val min: BigDecimal,
        val max: BigDecimal
    )

}

sealed interface ValidationResult {

    object Valid: ValidationResult

    data class Errors(
        val errors: String
    ): ValidationResult

}


