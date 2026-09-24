CREATE TABLE IF NOT EXISTS note
(
    id          VARCHAR PRIMARY KEY,
    user_id     VARCHAR REFERENCES user_info(id) NOT NULL,
    create_at   TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now() ,
    title       VARCHAR,
    content     TEXT
);