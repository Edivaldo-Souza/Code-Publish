CREATE TABLE tb_publication_tag(
    publication_id BIGINT,
    tag_id BIGINT,
    PRIMARY KEY (publication_id,tag_id),

    CONSTRAINT fk_publication FOREIGN KEY (publication_id) REFERENCES tb_publication(id),
    CONSTRAINT fk_tag FOREIGN KEY (tag_id) REFERENCES tb_tag(id)
)