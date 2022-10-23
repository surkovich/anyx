package com.marmotta.anyx.graphql.repository.persistence.impl

import com.marmotta.anyx.graphql.repository.Payment
import com.marmotta.anyx.graphql.repository.persistence.NewPaymentPersistence
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.ConcurrentSkipListMap


class DummyMapBasedPaymentRepository: NewPaymentPersistence {

    private val payments: ConcurrentSkipListMap<String, ConcurrentLinkedQueue<Payment>> = ConcurrentSkipListMap()
    private val utcZone = ZoneOffset.UTC
    //Lets just store payments as a collection of immutables by hour in UTC
    private val keyDateTimeFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH")

    override suspend fun add(payment: Payment) {
        val key: String = keyDateTimeFormat.format(payment.dateTime.withOffsetSameInstant(utcZone))
        val listToSave = payments.getOrPut(key) {ConcurrentLinkedQueue<Payment>()}
        listToSave.add(payment)
    }

}