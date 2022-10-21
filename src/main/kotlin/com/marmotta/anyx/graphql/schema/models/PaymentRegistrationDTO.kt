package com.marmotta.anyx.graphql.schema.models

import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import com.expediagroup.graphql.generator.annotations.GraphQLName
import com.marmotta.anyx.graphql.shared.model.Courier
import com.marmotta.anyx.graphql.shared.model.PaymentMethod
import java.math.BigDecimal
import java.time.OffsetDateTime


/**
 * DTO for new payment registration
 * Used only for controller-level interaction,
 * Shouldn't be ever transferred to any other levels
 */
@GraphQLDescription(
    "Payment details"
)
@GraphQLName("payment")
data class PaymentRegistrationDTO(
    @GraphQLName("customer_id")
    val customerId: Long,
    val price: BigDecimal,
    @GraphQLName("price_modifier")
    val priceModifier: BigDecimal,
    @GraphQLName("payment_method")
    val paymentMethod: PaymentMethod,
    @GraphQLName("datetime")
    val dateTime: OffsetDateTime,
    @GraphQLName("additional_item")
    val additionalItem: AdditionalDetails?
)

class AdditionalDetails(
    @GraphQLName("last_4")
    val lastFourDigits: Int?,
    @GraphQLName("courier_service")
    val courierService: Courier?,
    @GraphQLName("bank_name")
    val bankName: String?,
    @GraphQLName("account_number")
    val bankAccount: Long?,
    @GraphQLName("cheque_number")
    val chequeNumber: String?
)

