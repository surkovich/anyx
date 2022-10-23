package com.marmotta.anyx.graphql.schema.validator.rule

import com.marmotta.anyx.graphql.shared.model.Courier
import com.marmotta.anyx.graphql.schema.models.PaymentRegistrationDTO

internal class NoAdditionalItemsValidationRequired(
    override val priceModifierBounds: ValidationRule.PriceModifierBounds
) : ValidationRule() {
    override fun checkAdditionalItems(payment: PaymentRegistrationDTO): String? {
        return null
    }
}

internal class LastFourDigitsOfCardRequired(override val priceModifierBounds: PriceModifierBounds) : ValidationRule() {

    override fun checkAdditionalItems(payment: PaymentRegistrationDTO): String? {
        if (payment.additionalItem?.lastFourDigits == null)
            return "Last four digits of credit card number required"

        val digitsLength = payment.additionalItem.lastFourDigits.toString().length
        if (digitsLength != 4) {
            return "Exactly four digits of card number required, but [$digitsLength] provided"

        }

        return null
    }
}

internal class CourierServiceShouldBeRestricted(
    private val couriers: Set<Courier>,
    override val priceModifierBounds: PriceModifierBounds
) : ValidationRule() {
    override fun checkAdditionalItems(payment: PaymentRegistrationDTO): String? {
        val courierService = payment.additionalItem?.courierService
            ?: return "Courier service is not provided. Available courier services are [$couriers]"


        if (!couriers.contains(courierService)) {
            return "Courier service should be one of [$couriers], but [$courierService] found"
        }

        return null
    }

}

internal class BankNameAndAccountRequired(override val priceModifierBounds: PriceModifierBounds) : ValidationRule() {
    override fun checkAdditionalItems(payment: PaymentRegistrationDTO): String? {
        val emptyRequirements: List<String> = listOfNotNull(
            if (payment.additionalItem?.bankName == null) {
                "Bank Name"
            } else null,
            if (payment.additionalItem?.bankAccount == null) {
                "Bank account"
            } else null
        )

        return if (emptyRequirements.isEmpty()) {
            null
        } else {
            "The field(s) [$emptyRequirements] should not be null"
        }

    }
}

internal class BankNameAndChequeRequired(override val priceModifierBounds: PriceModifierBounds) : ValidationRule() {
    override fun checkAdditionalItems(payment: PaymentRegistrationDTO): String? {
        val emptyRequirements: List<String> = listOfNotNull(
            if (payment.additionalItem?.bankName == null) {
                "Bank Name"
            } else null,
            if (payment.additionalItem?.chequeNumber == null) {
                "Cheque number"
            } else null
        )

        return if (emptyRequirements.isEmpty()) {
            null
        } else {
            "The field(s) [$emptyRequirements] should not be null"
        }

    }
}
