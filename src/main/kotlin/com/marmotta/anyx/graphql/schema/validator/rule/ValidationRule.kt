package com.marmotta.anyx.graphql.schema.validator.rule

import com.marmotta.anyx.graphql.schema.models.PaymentRegistrationDTO

fun interface ValidationRule {
    fun check(payment: PaymentRegistrationDTO): ValidationResult
}

sealed interface ValidationResult {

    object Valid: ValidationResult

    data class Errors(
        val errors: String
    ): ValidationResult

}


