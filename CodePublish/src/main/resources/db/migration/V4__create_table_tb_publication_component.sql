CREATE TABLE tb_publication_component(
    id BIGSERIAL PRIMARY KEY,
    publication_id BIGINT,
    file_id BIGINT,
    description TEXT,

    CONSTRAINT fk_publication FOREIGN KEY (publication_id) REFERENCES tb_publication(id),
    CONSTRAINT fk_file FOREIGN KEY (file_id) REFERENCES tb_file(id)
)