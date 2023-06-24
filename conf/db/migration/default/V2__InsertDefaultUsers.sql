CREATE EXTENSION "uuid-ossp";
INSERT INTO users
    (id, name, email)
SELECT uuid_generate_v4(), 'Kim', 'kim@doe.com'
WHERE
    NOT EXISTS (
        SELECT name FROM users WHERE name = 'Kim'
    );
