package com.marmotta.anyx.graphql.schema

import com.expediagroup.graphql.server.operations.Mutation
import com.marmotta.anyx.graphql.repository.Payment
import com.marmotta.anyx.graphql.repository.persistence.NewPaymentPersistence
import com.marmotta.anyx.graphql.schema.models.PaymentRegistrationDTO
import com.marmotta.anyx.graphql.schema.validator.PaymentValidation
import com.marmotta.anyx.graphql.schema.validator.PaymentValidationDetails
import org.koin.core.component.KoinComponent
import org.koin.java.KoinJavaComponent
import java.math.BigDecimal


class PaymentReceiveController(
    private val persistence: NewPaymentPersistence
): Mutation {


    suspend fun registerPayment(payment: PaymentRegistrationDTO): PaymentRegistrationResult {
        return when(val result = PaymentValidation.of(payment)) {
            is PaymentValidationDetails.Valid -> {
                persistence.add(payment.asPersistenceModel())
                PaymentRegistrationResult.SuccessfullyRegistered(
                    finalPrice = BigDecimal.valueOf(0.95),
                    points = 1
                )
            }
            is PaymentValidationDetails.Errors -> PaymentRegistrationResult.ValidationFailed(
                message = result.description
            )
        }
    }

    private fun PaymentRegistrationDTO.asPersistenceModel(): Payment =
        Payment(
            dateTime = this.dateTime,
            customerId = this.customerId,
            initialPrice = this.price,
            //TODO!!!
            finalPrice = BigDecimal.valueOf(0.95),
            paymentMethod = this.paymentMethod
        )
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
