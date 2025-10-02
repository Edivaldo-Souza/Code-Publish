CREATE TABLE tb_publication(
    id SERIAL PRIMARY KEY,
    title VARCHAR(500),
    programing_language VARCHAR(100),
    category VARCHAR(255),
    description TEXT,
    user_id BIGINT,

    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES tb_user(id)
)