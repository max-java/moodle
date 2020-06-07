DROP TABLE IF EXISTS user_role;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS roles;

CREATE TABLE users
(
    user_id   int(11) NOT NULL,
    active    boolean      DEFAULT false,
    email     varchar(255) DEFAULT NULL,
    last_name varchar(255) DEFAULT NULL,
    name      varchar(255) DEFAULT NULL,
    password  varchar(255) DEFAULT NULL,
    user_name varchar(255) DEFAULT NULL,
    phone varchar(255) DEFAULT NULL
);


CREATE TABLE roles
(
    role_id INT NOT NULL AUTO_INCREMENT,
    role    VARCHAR(32)
);


CREATE TABLE user_role
(
    user_id INT NOT NULL,
    role_id INT NOT NULL
);

INSERT INTO roles
VALUES (1, 'ADMIN');

INSERT INTO users
VALUES (1, TRUE, 'user@dev', 'dev', 'user', '$2a$10$XizdxtpAW8xUbxSW.3q4k.bwhqMOcac8OTm8/vKo0VwzKux/8Smnu', 'userdev', '123123123');


INSERT INTO user_role
VALUES (1, 1);
