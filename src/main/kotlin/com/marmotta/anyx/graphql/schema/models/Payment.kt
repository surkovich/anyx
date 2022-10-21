package com.marmotta.anyx.graphql.schema.models

import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import com.expediagroup.graphql.generator.annotations.GraphQLName
import java.math.BigDecimal
import java.time.OffsetDateTime


@GraphQLDescription(
    "Payment details"
)
data class Payment(
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

enum class Courier {
    YAMATO,
    SAGAWA
}

enum class PaymentMethod {
    CASH,
    CASH_ON_DELIVERY,
    VISA,
    MASTERCARD,
    AMEX,
    JCB,
    LINE_PAY,
    PAYPAY,
    POINTS,
    GRAB_PAY,
    BANK_TRANSFER,
    CHEQUE
}
