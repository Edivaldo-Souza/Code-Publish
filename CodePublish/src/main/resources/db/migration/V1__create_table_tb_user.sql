CREATE TABLE tb_user(
    id SERIAL PRIMARY KEY,
    username VARCHAR(255) NOT NULL,
    role VARCHAR(50),
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(500) NOT NULL,
    publication_amount INT NOT NULL,
    rating_amount INT NOT NULL
)