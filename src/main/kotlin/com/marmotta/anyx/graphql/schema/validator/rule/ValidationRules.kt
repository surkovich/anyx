package com.marmotta.anyx.graphql.schema.validator.rule

import com.marmotta.anyx.graphql.shared.model.Courier
import com.marmotta.anyx.graphql.schema.models.PaymentRegistrationDTO

internal object LastFourDigitsOfCardRequired: ValidationRule {
    override fun check(payment: PaymentRegistrationDTO): ValidationResult {
        if (payment.additionalItem?.lastFourDigits == null)
            return ValidationResult.Errors(
                errors = "Last four digits of credit card number required"
            )

        val digitsLength = payment.additionalItem.lastFourDigits.toString().length
        if (digitsLength != 4) {
            return ValidationResult.Errors(
                errors = "Exactly four digits of card number required, but [$digitsLength] provided"
            )
        }

        return ValidationResult.Valid
    }
}

internal class CourierServiceShouldBeRestricted(private val couriers: Set<Courier>): ValidationRule {
    override fun check(payment: PaymentRegistrationDTO): ValidationResult {
        val courierService = payment.additionalItem?.courierService
            ?: return ValidationResult.Errors(
                errors = "Courier service is not provided. Available courier services are [$couriers]"
            )

        if (!couriers.contains(courierService)) {
            return ValidationResult.Errors(
                errors = "Courier service should be one of [$couriers], but [$courierService] found"
            )
        }

        return ValidationResult.Valid
    }

}

internal object BankNameAndAccountRequired: ValidationRule {
    override fun check(payment: PaymentRegistrationDTO): ValidationResult {
        val emptyRequirements: List<String> = listOfNotNull(
            if (payment.additionalItem?.bankName == null) {
                "Bank Name"
            } else null,
            if (payment.additionalItem?.bankAccount == null) {
                "Bank account"
            } else null
        )

        return if(emptyRequirements.isEmpty()) {
            ValidationResult.Valid
        } else {
            ValidationResult.Errors(
                errors = "The field(s) [$emptyRequirements] should not be null"
            )
        }

    }
}

object BankNameAndChequeRequired: ValidationRule {
    override fun check(payment: PaymentRegistrationDTO): ValidationResult {
        val emptyRequirements: List<String> = listOfNotNull(
            if (payment.additionalItem?.bankName == null) {
                "Bank Name"
            } else null,
            if (payment.additionalItem?.chequeNumber == null) {
                "Cheque number"
            } else null
        )

        return if(emptyRequirements.isEmpty()) {
            ValidationResult.Valid
        } else {
            ValidationResult.Errors(
                errors = "The field(s) [$emptyRequirements] should not be null"
            )
        }

    }
}
