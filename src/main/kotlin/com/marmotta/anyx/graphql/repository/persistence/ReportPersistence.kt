package com.marmotta.anyx.graphql.repository.persistence

import java.math.BigDecimal
import java.time.OffsetDateTime

interface ReportPersistence {
    fun salesOurly(startDate: OffsetDateTime, endDateTime: OffsetDateTime) : List<OurlyReportItem>
}

data class OurlyReportItem(
    val dateTime: OffsetDateTime,
    val sales: BigDecimal,
    val points: Int
)