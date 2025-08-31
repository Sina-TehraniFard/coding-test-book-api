-- ============================================================
-- V1__init.sql
-- 初期スキーマ: authors / books / book_authors
-- ============================================================

-- 出版ステータス ENUM 型
CREATE TYPE publication_status AS ENUM ('DRAFT', 'PUBLISHED');

-- 著者テーブル
CREATE TABLE authors
(
    id         BIGSERIAL PRIMARY KEY,
    name       TEXT NOT NULL,
    birth_date DATE NOT NULL CHECK (birth_date < CURRENT_DATE)
);

-- 書籍テーブル
CREATE TABLE books
(
    id     BIGSERIAL PRIMARY KEY,
    title  TEXT               NOT NULL,
    price  NUMERIC(12, 2)     NOT NULL CHECK (price >= 0),
    status publication_status NOT NULL DEFAULT 'DRAFT'
);

-- 書籍と著者の多対多関連テーブル
CREATE TABLE book_authors
(
    book_id   BIGINT NOT NULL REFERENCES books (id) ON DELETE CASCADE,
    author_id BIGINT NOT NULL REFERENCES authors (id) ON DELETE CASCADE,
    PRIMARY KEY (book_id, author_id)
);

-- 出版済み → 未出版 を禁止するトリガー
CREATE
OR REPLACE FUNCTION prevent_unpublish()
RETURNS TRIGGER AS $$
BEGIN
    IF
OLD.status = 'PUBLISHED' AND NEW.status = 'DRAFT' THEN
        RAISE EXCEPTION 'Cannot change status from PUBLISHED to DRAFT';
END IF;
RETURN NEW;
END;
$$
LANGUAGE plpgsql;

CREATE TRIGGER trg_prevent_unpublish
    BEFORE UPDATE OF status
    ON books
    FOR EACH ROW
    EXECUTE FUNCTION prevent_unpublish();