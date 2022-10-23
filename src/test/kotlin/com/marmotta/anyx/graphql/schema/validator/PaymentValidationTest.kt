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

    @Test
    fun `paymentMethods should not accept price modifiers beyond their bounds`() {
        mapOf(
            PaymentMethod.CASH to listOf(BigDecimal("0.99"), BigDecimal("1.03")),
            PaymentMethod.CASH_ON_DELIVERY to listOf(BigDecimal("0.89"), BigDecimal("1.1")),
            PaymentMethod.VISA to listOf(BigDecimal("0.94"), BigDecimal("1.01")),
            PaymentMethod.MASTERCARD to listOf(BigDecimal("0.94"), BigDecimal("1.01")),
            PaymentMethod.AMEX to listOf(BigDecimal("0.97"), BigDecimal("1.02")),
            PaymentMethod.JCB to listOf(BigDecimal("0.94"), BigDecimal("1.01")),
            PaymentMethod.LINE_PAY to listOf(BigDecimal("0.99"), BigDecimal("1.01")),
            PaymentMethod.PAYPAY to listOf(BigDecimal("0.99"), BigDecimal("1.01")),
            PaymentMethod.POINTS to listOf(BigDecimal("0.99"), BigDecimal("1.01")),
            PaymentMethod.GRAB_PAY to listOf(BigDecimal("0.99"), BigDecimal("1.01")),
            PaymentMethod.BANK_TRANSFER to listOf(BigDecimal("0.99"), BigDecimal("1.01")),
            PaymentMethod.CHEQUE to listOf(BigDecimal("0.89"), BigDecimal("1.01")),
        ).forEach { entry ->
            entry.value.forEach {priceModifier ->
                val payment = PaymentRegistrationDTO(
                    customerId = 1L,
                    price = BigDecimal(100),
                    priceModifier = priceModifier,
                    paymentMethod = entry.key,
                    dateTime = OffsetDateTime.MIN,
                    additionalItem = fullyFilledAdditionalDetails()
                )
                PaymentValidation.of(payment) should beInstanceOf<PaymentValidationDetails.Errors>()
            }
        }
    }

    @Test
    fun `payment methods should accept prices within their bounds`() {
        mapOf(
            PaymentMethod.CASH to BigDecimal(1),
            PaymentMethod.CASH_ON_DELIVERY to BigDecimal(1),
            PaymentMethod.VISA to BigDecimal(1),
            PaymentMethod.MASTERCARD to BigDecimal(1),
            PaymentMethod.AMEX to BigDecimal(1),
            PaymentMethod.JCB to BigDecimal(1),
            PaymentMethod.PAYPAY to BigDecimal(1),
            PaymentMethod.POINTS to BigDecimal(1),
            PaymentMethod.GRAB_PAY to BigDecimal(1),
            PaymentMethod.BANK_TRANSFER to BigDecimal(1),
            PaymentMethod.CHEQUE to BigDecimal(1),
        ).forEach {
            val payment = PaymentRegistrationDTO(
                customerId = 1L,
                price = BigDecimal(100),
                priceModifier = it.value,
                paymentMethod = it.key,
                dateTime = OffsetDateTime.MIN,
                additionalItem = fullyFilledAdditionalDetails()
            )
            PaymentValidation.of(payment) should beInstanceOf<PaymentValidationDetails.Valid>()
        }
    }



    private fun fullyFilledAdditionalDetails() =
        AdditionalDetails(
            courierService = Courier.YAMATO,
            lastFourDigits = 1234,
            bankName = "Sumitomo",
            bankAccount = 654321,
            chequeNumber = "CQ-1334"
        )

}