package com.marmotta.anyx.graphql.schema

import com.expediagroup.graphql.server.operations.Mutation
import com.marmotta.anyx.graphql.calculation.PriceAndPointsCalculation
import com.marmotta.anyx.graphql.repository.Payment
import com.marmotta.anyx.graphql.repository.persistence.NewPaymentPersistence
import com.marmotta.anyx.graphql.schema.models.PaymentRegistrationDTO
import com.marmotta.anyx.graphql.schema.validator.PaymentValidation
import com.marmotta.anyx.graphql.schema.validator.PaymentValidationDetails
import java.math.BigDecimal


class PaymentReceiveController(
    private val persistence: NewPaymentPersistence
): Mutation {

    suspend fun registerPayment(payment: PaymentRegistrationDTO): PaymentRegistrationResult =
        when(val result = PaymentValidation.of(payment)) {
            is PaymentValidationDetails.Valid -> {
                payment.saveWithPoints(PriceAndPointsCalculation.of(
                    paymentMethod = payment.paymentMethod,
                    price = payment.price
                ))
            }
            is PaymentValidationDetails.Errors -> PaymentRegistrationResult.ValidationFailed(
                message = result.description
            )
        }

    private suspend fun PaymentRegistrationDTO.saveWithPoints(points: Int): PaymentRegistrationResult.SuccessfullyRegistered {
        val finalPrice = this.priceModifier * this.price
        persistence.add(this.asPersistenceModel(finalPrice, points))
        return PaymentRegistrationResult.SuccessfullyRegistered(
            points = points,
            finalPrice = finalPrice
        )
    }

    private fun PaymentRegistrationDTO.asPersistenceModel(finalPrice: BigDecimal, points: Int): Payment =
        Payment(
            dateTime = this.dateTime,
            customerId = this.customerId,
            initialPrice = this.price,
            finalPrice = finalPrice,
            paymentMethod = this.paymentMethod,
            points = points
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
