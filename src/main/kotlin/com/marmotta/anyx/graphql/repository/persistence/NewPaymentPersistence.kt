package com.marmotta.anyx.graphql.repository.persistence

import com.marmotta.anyx.graphql.repository.Payment

fun interface NewPaymentPersistence {
    //TODO think about adding non-successfull save
    suspend fun add(payment: Payment)

}