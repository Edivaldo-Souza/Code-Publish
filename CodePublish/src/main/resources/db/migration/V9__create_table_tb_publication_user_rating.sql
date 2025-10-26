CREATE TABLE tb_publication_user_rating(
    publication_id BIGINT,
    user_id BIGINT,
    positive BOOLEAN,
    PRIMARY KEY (publication_id,user_id),

    CONSTRAINT fk_publication FOREIGN KEY (publication_id) REFERENCES tb_publication(id),
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES tb_user(id)
);