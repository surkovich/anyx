package com.marmotta.anyx.graphql.schema

import com.expediagroup.graphql.generator.annotations.GraphQLName
import com.expediagroup.graphql.server.operations.Query
import com.marmotta.anyx.graphql.repository.persistence.ReportPersistence
import java.time.OffsetDateTime

class ReportController(private val persistence: ReportPersistence): Query {

    fun report(
        startDateTime: OffsetDateTime,
        endDateTime: OffsetDateTime
    ) =
        persistence.salesOurly(startDateTime, endDateTime)


}