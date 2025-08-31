package com.quocard.codingtestbookapi.infrastructure.jooq

import com.quocard.codingtestbookapi.domain.model.Author
import com.quocard.codingtestbookapi.domain.repository.AuthorRepository
import org.jooq.DSLContext
import org.springframework.stereotype.Repository
import com.quocard.generated.jooq.tables.Authors.AUTHORS
import java.time.LocalDate

@Repository
class JooqAuthorRepository(
    private val dsl: DSLContext
) : AuthorRepository {

    override fun save(author: Author): Author {
        val id = dsl.insertInto(AUTHORS)
            .set(AUTHORS.NAME, author.name)
            .set(AUTHORS.BIRTH_DATE, author.birthDate)
            .returning(AUTHORS.ID)
            .fetchOne()
            ?.id

        return author.copy(id = id)
    }

    override fun findById(id: Long): Author? {
        return dsl.selectFrom(AUTHORS)
            .where(AUTHORS.ID.eq(id))
            .fetchOne()
            ?.let {
                Author(
                    id = it.id,
                    name = it.name,
                    birthDate = it.birthDate ?: LocalDate.MIN
                )
            }
    }
}