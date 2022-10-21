package com.marmotta.anyx.graphql.schema

import com.expediagroup.graphql.server.operations.Mutation
import com.marmotta.anyx.graphql.schema.models.Payment
import com.marmotta.anyx.graphql.schema.validator.PaymentValidation
import com.marmotta.anyx.graphql.schema.validator.PaymentValidationDetails
import java.math.BigDecimal


class PaymentReceiveController: Mutation {

    suspend fun registerPayment(payment: Payment): PaymentRegistrationResult {
        return when(val result = PaymentValidation.of(payment)) {
            is PaymentValidationDetails.Valid -> PaymentRegistrationResult.SuccessfullyRegistered(
                finalPrice = BigDecimal.valueOf(0.95),
                points = 1
            )
            is PaymentValidationDetails.Errors -> PaymentRegistrationResult.ValidationFailed(
                message = result.description
            )
        }
    }
}



sealed interface PaymentRegistrationResult {

    data class SuccessfullyRegistered(
        val finalPrice: BigDecimal,
        val points: Int
    ): PaymentRegistrationResult

    data class ValidationFailed(
        val message: String
    ): PaymentRegistrationResult
}
