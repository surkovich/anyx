package com.marmotta.anyx.graphql.schema.validator

import com.marmotta.anyx.graphql.schema.models.AdditionalDetails
import com.marmotta.anyx.graphql.schema.models.PaymentRegistrationDTO
import com.marmotta.anyx.graphql.schema.validator.rule.ValidationResult
import com.marmotta.anyx.graphql.shared.model.Courier
import com.marmotta.anyx.graphql.shared.model.PaymentMethod
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.beInstanceOf
import io.kotest.matchers.types.shouldBeSameInstanceAs
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

        PaymentValidation.of(payment) should beInstanceOf<PaymentValidationDetails.Errors>()
    }

    @Test
    fun `Cash on delivery should not accept price modifier beyond bounds`() {
        listOf(BigDecimal(0.89), BigDecimal(1.1))
            .forEach {
                val payment = PaymentRegistrationDTO(
                    customerId = 1L,
                    price = BigDecimal(100),
                    priceModifier = it,
                    paymentMethod = PaymentMethod.CASH_ON_DELIVERY,
                    dateTime = OffsetDateTime.MIN,
                    additionalItem = AdditionalDetails()
                )
                PaymentValidation.of(payment) should beInstanceOf<PaymentValidationDetails.Errors>()
            }
    }

    @Test
    fun `Cash on delivery should fail validation on couriers beyond restricted`(){
        Courier.values().filter { !setOf(Courier.SAGAWA, Courier.YAMATO).contains(it) }
            .forEach {
                val payment = PaymentRegistrationDTO(
                    customerId = 1L,
                    price = BigDecimal(100),
                    priceModifier = BigDecimal("0.91"),
                    paymentMethod = PaymentMethod.CASH_ON_DELIVERY,
                    dateTime = OffsetDateTime.MIN,
                    additionalItem = AdditionalDetails(courierService = it)
                )
                PaymentValidation.of(payment) should beInstanceOf<PaymentValidationDetails.Errors>()
            }
    }

    @Test
    fun `Cash on delivery with correct delivery and price modifier should pass validation`() {
        listOf(Courier.SAGAWA, Courier.YAMATO)
            .forEach {
                val payment = PaymentRegistrationDTO(
                    customerId = 1L,
                    price = BigDecimal(100),
                    priceModifier = BigDecimal("0.91"),
                    paymentMethod = PaymentMethod.CASH_ON_DELIVERY,
                    dateTime = OffsetDateTime.MIN,
                    additionalItem = AdditionalDetails(courierService = it)
                )
                PaymentValidation.of(payment) should beInstanceOf<PaymentValidationDetails.Valid>()
            }
    }



}