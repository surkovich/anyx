package com.marmotta.anyx.graphql.schema.validator

import com.marmotta.anyx.graphql.schema.models.AdditionalDetails
import com.marmotta.anyx.graphql.schema.models.PaymentRegistrationDTO
import com.marmotta.anyx.graphql.schema.validator.rule.ValidationResult
import com.marmotta.anyx.graphql.shared.model.PaymentMethod
import io.kotest.matchers.shouldBe
import io.ktor.util.reflect.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.time.OffsetDateTime

internal class PaymentValidationTest{

    @Test
    fun `Cash on delivery should require courier service`() {
        val payment = PaymentRegistrationDTO(
            customerId = 1L,
            price = BigDecimal(100),
            priceModifier = BigDecimal(0.95),
            paymentMethod = PaymentMethod.CASH_ON_DELIVERY,
            dateTime = OffsetDateTime.MIN,
            additionalItem = AdditionalDetails()
        )

        PaymentValidation.of(payment) shouldBe instanceOf(ValidationResult.Errors::class)
    }

//    @Test
//    fun

}