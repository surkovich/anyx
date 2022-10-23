package com.marmotta.anyx.graphql.shared.model

import java.math.BigDecimal
import java.time.OffsetDateTime

data class OurlyReportItem(
    val dateTime: OffsetDateTime,
    val sales: BigDecimal,
    val points: Int
)