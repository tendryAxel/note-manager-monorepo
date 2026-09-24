CREATE TABLE IF NOT EXISTS session_token
(
    token       VARCHAR PRIMARY KEY NOT NULL,
    user_id     VARCHAR REFERENCES user_info(id) NOT NULL,
    create_at   TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now() ,
    expire_at   TIMESTAMP WITH TIME ZONE NOT NULL
);