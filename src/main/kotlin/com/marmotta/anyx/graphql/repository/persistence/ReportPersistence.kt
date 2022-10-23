package com.marmotta.anyx.graphql.repository.persistence

import com.marmotta.anyx.graphql.shared.model.OurlyReportItem
import java.time.OffsetDateTime

interface ReportPersistence {
    fun salesOurly(startDate: OffsetDateTime, endDateTime: OffsetDateTime) : List<OurlyReportItem>
}

