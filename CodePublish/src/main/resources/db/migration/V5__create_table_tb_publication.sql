CREATE TABLE tb_publication(
    id SERIAL PRIMARY KEY,
    title VARCHAR(500),
    programing_language_id BIGINT,
    category_id BIGINT,
    description TEXT,
    upvotes_amount INT,
    downvotes_amount INT,
    user_id BIGINT,

    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES tb_user(id),
    CONSTRAINT fk_programing_language FOREIGN KEY (programing_language_id) REFERENCES tb_programing_language(id),
    CONSTRAINT fk_category FOREIGN KEY (category_id) REFERENCES tb_category(id)
)