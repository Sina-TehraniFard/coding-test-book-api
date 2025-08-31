package com.quocard.codingtestbookapi.domain.model

import java.time.LocalDate

data class Author (
    val id: Long? = null,
    val name: String,
    val birthDate: LocalDate,
)