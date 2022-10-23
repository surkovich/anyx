package com.marmotta.anyx.graphql.repository.persistence.impl

import com.marmotta.anyx.graphql.repository.Payment
import com.marmotta.anyx.graphql.repository.persistence.NewPaymentPersistence
import com.marmotta.anyx.graphql.shared.model.OurlyReportItem
import com.marmotta.anyx.graphql.repository.persistence.ReportPersistence
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.ConcurrentSkipListMap


class DummyMapBasedPaymentRepository: NewPaymentPersistence, ReportPersistence {

    private val payments: ConcurrentSkipListMap<String, ConcurrentLinkedQueue<Payment>> = ConcurrentSkipListMap()
    //TODO it will be better to operate only with LocalDateTime in UTC in business logic
    // If so - it is better to store LocalDateTime, and we should convert OffsetDateTime immediately as we receive it -
    // on the controller level
    private val utcZone = ZoneOffset.UTC
    //Lets just store payments as a collection of immutables by hour in UTC
    private val keyDateTimeFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH")

    override suspend fun add(payment: Payment) {
        val key: String = keyDateTimeFormat.format(payment.dateTime.withOffsetSameInstant(utcZone))
        val listToSave = payments.getOrPut(key) {ConcurrentLinkedQueue<Payment>()}
        listToSave.add(payment)
    }

    override fun salesOurly(startDate: OffsetDateTime, endDateTime: OffsetDateTime): List<OurlyReportItem> {
        val keyFrom: String = keyDateTimeFormat.format(startDate.withOffsetSameInstant(utcZone))
        val keyTo: String = keyDateTimeFormat.format(endDateTime.withOffsetSameInstant(utcZone))

        return payments.subMap(keyFrom, keyTo)
            .mapValues { entry ->
                OurlyReportItem(
                    dateTime = entry.value.first().dateTime.withMinute(0).withSecond(0).withHour(0),
                    sales = entry.value.sumOf { it.finalPrice },
                    points = entry.value.sumOf { it.points }
            )
        }.values.toList()
    }

}