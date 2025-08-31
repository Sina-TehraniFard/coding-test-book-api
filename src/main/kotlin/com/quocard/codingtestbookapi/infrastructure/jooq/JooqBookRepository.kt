package com.quocard.codingtestbookapi.infrastructure.jooq

import com.quocard.codingtestbookapi.domain.model.Book
import com.quocard.codingtestbookapi.domain.model.PublicationStatus
import com.quocard.generated.jooq.enums.PublicationStatus as JooqPublicationStatus
import com.quocard.codingtestbookapi.domain.repository.BookRepository
import org.jooq.DSLContext
import org.springframework.stereotype.Repository
import com.quocard.generated.jooq.tables.Books.BOOKS
import com.quocard.generated.jooq.tables.BookAuthors.BOOK_AUTHORS
import java.math.BigDecimal

@Repository
class JooqBookRepository(
    private val dsl: DSLContext
) : BookRepository {

    override fun save(book: Book): Book {
        val id = dsl.insertInto(BOOKS)
            .set(BOOKS.TITLE, book.title)
            .set(BOOKS.PRICE, book.price)
            .set(BOOKS.STATUS, JooqPublicationStatus.valueOf(book.status.name))
            .returning(BOOKS.ID)
            .fetchOne()
            ?.id

        return book.copy(id = id)
    }

    override fun findByAuthorId(authorId: Long): List<Book> {
        return dsl.select(BOOKS.ID, BOOKS.TITLE, BOOKS.PRICE, BOOKS.STATUS)
            .from(BOOKS)
            .join(BOOK_AUTHORS).on(BOOKS.ID.eq(BOOK_AUTHORS.BOOK_ID))
            .where(BOOK_AUTHORS.AUTHOR_ID.eq(authorId))
            .fetch { record ->
                Book(
                    id = record.get(BOOKS.ID),
                    title = record.get(BOOKS.TITLE),
                    price = record.get(BOOKS.PRICE) ?: BigDecimal.ZERO,
                    status = PublicationStatus.valueOf(record.get(BOOKS.STATUS)?.name ?: "DRAFT")
                )
            }
    }
}