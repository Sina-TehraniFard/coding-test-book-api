package com.quocard.codingtestbookapi.domain.model

import java.math.BigDecimal

data class Book(
    val id: Long? = null,
    val title: String,
    val price: BigDecimal,
    val status: PublicationStatus = PublicationStatus.DRAFT
)