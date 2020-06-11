DROP TABLE IF EXISTS user_role;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS roles;

CREATE TABLE users
(
    user_id   int(11) NOT NULL AUTO_INCREMENT,
    active    boolean      DEFAULT false,
    email     varchar(255) DEFAULT NULL,
    last_name varchar(255) DEFAULT NULL,
    name      varchar(255) DEFAULT NULL,
    password  varchar(255) DEFAULT NULL,
    user_name varchar(255) DEFAULT NULL,
    first_and_last_name varchar(255) DEFAULT NULL,
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

INSERT INTO roles VALUES (1, 'ROLE_ADMIN'), (2, 'ROLE_GUEST'), (3, 'ROLE_FREE_STUDENT'), (4, 'ROLE_STUDENT'), (5, 'ROLE_SCRUM_MASTER'), (6, 'ROLE_ALUMNUS');

INSERT INTO users VALUES
                         (1, TRUE, 'user@dev', 'Adic', 'Administrator', '$2a$10$XizdxtpAW8xUbxSW.3q4k.bwhqMOcac8OTm8/vKo0VwzKux/8Smnu', 'userdev', 'User Developer', '123123123'),
                         (2, TRUE, 'guest@dev', 'Gosha', 'Guest', '$2a$10$j3WpiyKaAmAbLU//BnrAb.8bijT23gsqBSwLs1hobq8L6b3JPUqmG	', 'guest', 'User Guest', '123123123'),
                         (3, TRUE, 'free@dev', 'fedor', 'Free', '$2a$10$j3WpiyKaAmAbLU//BnrAb.8bijT23gsqBSwLs1hobq8L6b3JPUqmG	', 'free', 'User FreeStudent', '123123123'),
                         (4, TRUE, 'student@dev', 'Tema', 'Student', '$2a$10$j3WpiyKaAmAbLU//BnrAb.8bijT23gsqBSwLs1hobq8L6b3JPUqmG	', 'student', 'User Student', '123123123'),
                         (5, TRUE, 'master@dev', 'Stepa', 'Master', '$2a$10$j3WpiyKaAmAbLU//BnrAb.8bijT23gsqBSwLs1hobq8L6b3JPUqmG	', 'master', 'User ScrumMaster', '123123123'),
                         (6, TRUE, 'alumnus@dev', 'Alex', 'Alumnus', '$2a$10$j3WpiyKaAmAbLU//BnrAb.8bijT23gsqBSwLs1hobq8L6b3JPUqmG	', 'alumnus', 'User Alumnus', '123123123');

INSERT INTO user_role VALUES
                             (1, 1),
                             (2, 2),
                             (3, 3),
                             (4, 4),
                             (5, 5),
                             (6, 6);
