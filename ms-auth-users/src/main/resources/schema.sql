-- ============ SCHEMAS ============
CREATE SCHEMA IF NOT EXISTS auth_users;
CREATE SCHEMA IF NOT EXISTS content;

-- ============ TABLA USERS ============
CREATE TABLE IF NOT EXISTS auth_users.users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    nombres VARCHAR(100),
    apellidos VARCHAR(100),
    fecha_nacimiento DATE,
    alias VARCHAR(50),
    created_at TIMESTAMP NOT NULL DEFAULT now()
);

-- ============ TABLA POSTS ============
CREATE TABLE IF NOT EXISTS content.posts (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    username VARCHAR(50) NOT NULL,
    message VARCHAR(1000) NOT NULL,
    like_count INT NOT NULL DEFAULT 0,
    created_at TIMESTAMP NOT NULL DEFAULT now()
);

-- ============ TABLA LIKES ============
CREATE TABLE IF NOT EXISTS content.likes (
    id BIGSERIAL PRIMARY KEY,
    post_id BIGINT NOT NULL REFERENCES content.posts(id) ON DELETE CASCADE,
    user_id BIGINT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT now(),
    CONSTRAINT uq_post_user UNIQUE (post_id, user_id)
);

-- ============ PROCEDURE 1: dar like ============
CREATE OR REPLACE PROCEDURE content.sp_add_like(p_post_id BIGINT, p_user_id BIGINT)
LANGUAGE plpgsql AS $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM content.likes WHERE post_id = p_post_id AND user_id = p_user_id
    ) THEN
        INSERT INTO content.likes(post_id, user_id) VALUES (p_post_id, p_user_id);
        UPDATE content.posts SET like_count = like_count + 1 WHERE id = p_post_id;
    END IF;
END;
$$;

-- ============ PROCEDURE 2: crear publicación ============
CREATE OR REPLACE PROCEDURE content.sp_create_post(
    p_user_id BIGINT,
    p_username VARCHAR,
    p_message VARCHAR
)
LANGUAGE plpgsql AS $$
BEGIN
    INSERT INTO content.posts(user_id, username, message, like_count, created_at)
    VALUES (p_user_id, p_username, p_message, 0, now());
END;
$$;