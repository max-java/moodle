-- TEMPLATE
-- DROP TABLE IF EXISTS ``;
-- CREATE TABLE `` (
--
-- );
-- INSERT INTO `` VALUES ();
DROP TABLE IF EXISTS user_role;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS roles;
DROP TABLE IF EXISTS `lecture` cascade ;
DROP TABLE IF EXISTS `practice_question` cascade ;


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

INSERT INTO roles VALUES (1, 'ROLE_ADMIN'), (2, 'ROLE_GUEST'), (3, 'ROLE_FREE_STUDENT'), (4, 'ROLE_STUDENT'), (5, 'ROLE_SCRUM_MASTER'), (6, 'ROLE_ALUMNUS'), (80, 'ROLE_STREAM');

INSERT INTO users VALUES
(1, TRUE, 'user@dev', 'Adic', 'Administrator', '$2a$10$XizdxtpAW8xUbxSW.3q4k.bwhqMOcac8OTm8/vKo0VwzKux/8Smnu', 'userdev', 'User Developer', '123123123'),
(2, TRUE, 'guest@dev', 'Gosha', 'Guest', '$2a$10$j3WpiyKaAmAbLU//BnrAb.8bijT23gsqBSwLs1hobq8L6b3JPUqmG', 'guest', 'User Guest', '123123123'),
(3, TRUE, 'free@dev', 'fedor', 'Free', '$2a$10$j3WpiyKaAmAbLU//BnrAb.8bijT23gsqBSwLs1hobq8L6b3JPUqmG', 'free', 'User FreeStudent', '123123123'),
(4, TRUE, 'student@dev', 'Tema', 'Student', '$2a$10$j3WpiyKaAmAbLU//BnrAb.8bijT23gsqBSwLs1hobq8L6b3JPUqmG', 'student', 'User Student', '123123123'),
(5, TRUE, 'master@dev', 'Stepa', 'Master', '$2a$10$j3WpiyKaAmAbLU//BnrAb.8bijT23gsqBSwLs1hobq8L6b3JPUqmG', 'master', 'User ScrumMaster', '123123123'),
(6, TRUE, 'alumnus@dev', 'Alex', 'Alumnus', '$2a$10$j3WpiyKaAmAbLU//BnrAb.8bijT23gsqBSwLs1hobq8L6b3JPUqmG', 'alumnus', 'User Alumnus', '123123123'),
(90, TRUE, 'user@dev', 'streamCourse_1', 'TeamCourse1', '$2a$10$IZgkJaRT8VtWSq9z.9ZjK.6xn1gMn3LiJGscXZDVehkUYoxVW.cva', 'course1User', NULL, '123123123'),
(91, TRUE, 'user@dev', 'streamCourse_2', 'TeamCourse2', '$2a$10$HNyQR8JlouePUG7VX9XHgep25SNu1K3djrJ3Nq6GBCI6TgnWzmHrG', 'course2User', NULL, '123123123'),
(92, TRUE, 'user@dev', 'streamCourse_3', 'TeamCourse3', '$2a$10$j0pPnEbToIFgbSJPnyUlzeyPHeOxvlVoZQpx8m/Vd/68xg5/QAUYG', 'course3User', NULL, '123123123'),
(93, TRUE, 'user@dev', 'streamCourse_4', 'TeamCourse4', '$2a$10$WokgfVPM8AbCvMHwus6C2eqxLh4RYE1bupMwN8gh08CQhS2i9AIv6', 'course4User', NULL, '123123123');

INSERT INTO user_role VALUES
(1, 1),
(2, 2),
(3, 3),
(4, 4),
(5, 5),
(6, 6),
(90, 80),
(91, 80),
(92, 80),
(93, 80);


DROP TABLE IF EXISTS `PROFILE`;
CREATE TABLE `PROFILE` (    "ID" BIGINT NOT NULL,
                            "ABOUT" CLOB,
                            "AVATAR_FILE_NAME" VARCHAR(255),
                            "COURSE_ID" BIGINT,
                            "DATE_END" DATE,
                            "DATE_START" DATE,
                            "IS_ENROL_OPEN" BOOLEAN,
                            "OWNER_PROFILE_ID" BIGINT,
                            "TELEGRAM_LINK" VARCHAR(255),
                            "TELEGRAM_LINK_TEXT" VARCHAR(255),
                            "ZOOM_LINK" VARCHAR(255),
                            "ZOOM_LINK_TEXT" VARCHAR(255),
                            "GIT_LINK" VARCHAR(255),
                            "GIT_USERNAME" VARCHAR(255),
                            "FEEDBACK_LINK" VARCHAR(255),
                            "FEEDBACK_NAME" VARCHAR(255),
                            "USER_ID" BIGINT NOT NULL);
INSERT INTO `PROFILE` VALUES (10, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1),
                             (11, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 2),
                             (12, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 4),
                             (13, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 3),
                             (14, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 5),
                             (15, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 6),
                             (16, '', NULL, 30, NULL, NOW()+INTERVAL 5 DAY, NULL, 10, '', '', '', '', '', '', '', '', 90),
                             (17, '', NULL, 31, NULL, NOW()+INTERVAL 5 DAY, NULL, 10, '', '', '', '', '', '', '', '', 91),
                             (18, '', NULL, 32, NULL, NOW()+INTERVAL 5 DAY, NULL, 10, '', '', '', '', '', '', '', '', 92),
                             (19, '', NULL, 33, NULL, NOW()+INTERVAL 5 DAY, NULL, 10, '', '', '', '', '', '', '', '', 93);


DROP TABLE IF EXISTS `PROFILE_POSSESSES`;
CREATE TABLE `PROFILE_POSSESSES` (    "ID" BIGINT NOT NULL,
                            "ENTITY_ID" BIGINT,
                            "ENTITY_TYPE" VARCHAR(255),
                            "PROFILE_ID" BIGINT);
INSERT INTO `PROFILE_POSSESSES` VALUES (20, 10, 'PROFILE', 10),
                             (21, 30, 'COURSE', 10),
                             (22, 40, 'LECTURE', 10),
                             (23, 41, 'LECTURE', 10),
                             (24, 42, 'LECTURE', 10),
                             (27, 11, 'PROFILE', 11),
                             (28, 12, 'PROFILE', 12),
                             (29, 15, 'PROFILE', 10),
                             (71, 31, 'COURSE', 10),
                             (72, 32, 'COURSE', 10),
                             (73, 33, 'COURSE', 10),
                             (74, 16, 'PROFILE', 10),
                             (75, 17, 'PROFILE', 10),
                             (76, 18, 'PROFILE', 10),
                             (77, 19, 'PROFILE', 10),
                             (101, 43, 'LECTURE', 10),
                             (102, 44, 'LECTURE', 10),
                             (103, 45, 'LECTURE', 10),
                             (104, 46, 'LECTURE', 10),
                             (105, 47, 'LECTURE', 10),
                             (106, 48, 'LECTURE', 10);


--
-- Table structure for table `practice_question`
--

-- DROP TABLE IF EXISTS `practice_question`;
CREATE TABLE `practice_question` (
                                     `id` bigint(20) NOT NULL,
                                     `description` longtext,
                                     `name` varchar(255) DEFAULT NULL,
                                     `repro_steps` longtext,
                                     `summary` varchar(255) DEFAULT NULL,
                                     `theme` varchar(255) DEFAULT NULL,
                                     PRIMARY KEY (`id`)
);

--
-- Dumping data for table `practice_question`
--

INSERT INTO `practice_question` VALUES (27,'','Проверочная работа: Объекты среди нас','','','Знакомство с языком Java'),(29,'','Проверочная работа: Алгоритмы','','','Знакомство с языком Java'),(31,'','Проверочная работа: Названия переменных','','','Типы данных, математические операции, консольный ввод/вывод'),(33,'','Проверочная работа: Тип переменных','','','Типы данных, математические операции, консольный ввод/вывод'),(35,'','Арифметические операции','','','Типы данных, математические операции, консольный ввод/вывод'),(37,'','Генерация случайных чисел','','','Типы данных, математические операции, консольный ввод/вывод'),(39,'','класс Dog','','','Методы, классы и объекты'),(41,'','class Human','','','Методы, классы и объекты'),(43,'','Circle','','','Методы, классы и объекты'),(45,'','Encoder','','','Методы, классы и объекты'),(47,'','Product','','','Методы, классы и объекты'),(49,'','Sign Comparator','','','Условные операторы'),(51,'','Phrase Analyser','','','Условные операторы'),(53,'','Color Detector','','','Условные операторы'),(55,'','Stock','','','Условные операторы'),(57,'','UserLoginService','','','Условные операторы'),(59,'','Счетчик','','','Объекты в памяти'),(61,'','Book','','','Объекты в памяти'),(63,'','TV','','','Объекты в памяти'),(65,'','TV and TVController','','','Объекты в памяти'),(67,'','Credit Card','','','Объекты в памяти'),(69,'','Таблица нечетных чисел','','','Циклы for/while/do...while'),(71,'','Сумма простых чисел','','','Циклы for/while/do...while'),(73,'','Number Service','','','Циклы for/while/do...while'),(75,'','Car','','','Циклы for/while/do...while'),(77,'','Power Calculator','','','Циклы for/while/do...while'),(79,'','Работа с массивами','','','Массивы'),(81,'','Array Service','','','Массивы'),(83,'','Sort, Swap','','','Массивы'),(85,'','Палиндром','','','Массивы'),(87,'','Копирование диапазона','','','Массивы'),(89,'','Animals','','','Наследование, интерфейсы'),(91,'','Shape','','','Наследование, интерфейсы'),(93,'','Math operations','','','Наследование, интерфейсы'),(95,'','Unique word vocabulary','','','Коллекции'),(97,'','Unique Word Counter','','','Коллекции'),(99,'','Библиотека','','','Коллекции'),(101,'','Shop','','','Коллекции'),(103,'','User Validation Service','','','Исключения'),(105,'','Простая База Данных','','','Исключения'),(107,'','UserService and UserRepository Unit testing','','','jUnit'),(109,'<p class=\"p2\" style=\"margin: 0px; font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 12px; line-height: normal; font-family: Helvetica; min-height: 14px;\"><span style=\"font-size: medium;\">В приложении должны быть функции добавление/удаление/получение продукта (</span><strong style=\"font-size: medium;\">CRUD operations</strong><span style=\"font-size: medium;\">).</span></p>\r\n<p class=\"p2\" style=\"margin: 0px; font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 12px; line-height: normal; font-family: Helvetica; min-height: 14px;\"><span style=\"font-size: medium;\">&nbsp;</span></p>\r\n<p class=\"p1\" style=\"margin: 0px; font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 12px; line-height: normal; font-family: Helvetica;\"><span style=\"font-size: medium;\"><strong><span style=\"color: #ff0000;\">Минимальные требования</span></strong>&nbsp;(возможные пункты меню):</span></p>\r\n<p class=\"p1\" style=\"margin: 0px; font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 12px; line-height: normal; font-family: Helvetica;\"><span style=\"font-size: medium;\">&nbsp;</span></p>\r\n<p class=\"p1\" style=\"margin: 0px; font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 12px; line-height: normal; font-family: Helvetica;\"><span style=\"font-size: medium;\"><strong>1. Добавление продукта</strong></span></p>\r\n<p class=\"p1\" style=\"margin: 0px; font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 12px; line-height: normal; font-family: Helvetica;\"><span style=\"font-size: medium;\"><strong>2. Получение продукта по id</strong></span></p>\r\n<p class=\"p1\" style=\"margin: 0px; font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 12px; line-height: normal; font-family: Helvetica;\"><span style=\"font-size: medium;\"><strong>3. Получение списка всех продуктов</strong></span></p>\r\n<p class=\"p1\" style=\"margin: 0px; font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 12px; line-height: normal; font-family: Helvetica;\"><span style=\"font-size: medium;\"><strong>4. Удаление продукта по id</strong></span></p>\r\n<p class=\"p2\" style=\"margin: 0px; font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 12px; line-height: normal; font-family: Helvetica; min-height: 14px;\"><span style=\"font-size: medium;\">&nbsp;</span></p>\r\n<p class=\"p1\" style=\"margin: 0px; font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 12px; line-height: normal; font-family: Helvetica;\"><span style=\"font-size: medium;\">Как&nbsp;<strong>минимум</strong>&nbsp;должен быть 1 интерфейс или абстрактный класс.&nbsp;<span style=\"color: #ff0000;\">Использование должно быть подкреплено необходимостью.</span></span></p>\r\n<p class=\"p2\" style=\"margin: 0px; font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 12px; line-height: normal; font-family: Helvetica; min-height: 14px;\"><span style=\"font-size: medium;\">&nbsp;</span></p>\r\n<p class=\"p1\" style=\"margin: 0px; font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 12px; line-height: normal; font-family: Helvetica;\"><span style=\"font-size: medium;\">Как&nbsp;<strong>минимум</strong>&nbsp;1 коллекция (любая из List, Map, Set)</span></p>\r\n<p class=\"p2\" style=\"margin: 0px; font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 12px; line-height: normal; font-family: Helvetica; min-height: 14px;\"><span style=\"font-size: medium;\">&nbsp;</span></p>\r\n<p class=\"p1\" style=\"margin: 0px; font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 12px; line-height: normal; font-family: Helvetica;\"><span style=\"font-size: medium;\">Названия должны соответствовать логике приложения. (<strong>Clean Code</strong>&nbsp;в помощь)</span></p>\r\n<p class=\"p1\" style=\"margin: 0px; font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 12px; line-height: normal; font-family: Helvetica;\"><span style=\"font-size: medium;\">Не должно быть комментариев - код должен быть понятен без комментариев.</span></p>\r\n<p class=\"p2\" style=\"margin: 0px; font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 12px; line-height: normal; font-family: Helvetica; min-height: 14px;\"><span style=\"font-size: medium;\">&nbsp;</span></p>\r\n<p class=\"p1\" style=\"margin: 0px; font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 12px; line-height: normal; font-family: Helvetica;\"><span style=\"font-size: medium;\"><strong>Основная сущность - Product.</strong></span></p>\r\n<p class=\"p1\" style=\"margin: 0px; font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 12px; line-height: normal; font-family: Helvetica;\"><span style=\"font-size: medium;\">&nbsp;</span></p>\r\n<p class=\"p1\" style=\"margin: 0px; font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 12px; line-height: normal; font-family: Helvetica;\"><span style=\"font-size: medium;\">Основные поля (<strong>mandatory fields</strong>):</span></p>\r\n<p class=\"p1\" style=\"margin: 0px; font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 12px; line-height: normal; font-family: Helvetica;\"><span style=\"font-size: medium;\">Название (<strong>name</strong>) - тип данных&nbsp;<strong>String</strong>&nbsp;(example: Apple)</span></p>\r\n<p class=\"p1\" style=\"margin: 0px; font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 12px; line-height: normal; font-family: Helvetica;\"><span style=\"font-size: medium;\">Идентификационный номер (<strong>id</strong>) - тип данных&nbsp;<strong>Long</strong>&nbsp;(example: 123)</span></p>\r\n<p class=\"p1\" style=\"margin: 0px; font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 12px; line-height: normal; font-family: Helvetica;\"><span style=\"font-size: medium;\">Цена (<strong>price</strong>) - тип данных&nbsp;<strong>BigDecimal</strong>&nbsp;(example: 0.14)</span></p>\r\n<p class=\"p1\" style=\"margin: 0px; font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 12px; line-height: normal; font-family: Helvetica;\"><span style=\"font-size: medium;\">Категоря (<strong>category</strong>) -&nbsp;<strong>enum&nbsp;</strong>(example: FRUIT)</span></p>\r\n<p class=\"p2\" style=\"margin: 0px; font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 12px; line-height: normal; font-family: Helvetica; min-height: 14px;\"><span style=\"font-size: medium;\">&nbsp;</span></p>\r\n<p class=\"p1\" style=\"margin: 0px; font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 12px; line-height: normal; font-family: Helvetica;\"><span style=\"font-size: medium;\">Опциональные поля (<strong>optional fields</strong>):</span></p>\r\n<p class=\"p1\" style=\"margin: 0px; font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 12px; line-height: normal; font-family: Helvetica;\"><span style=\"font-size: medium;\">Скидка (<strong>discount</strong>) - тип данных&nbsp;<strong>BigDecimal</strong>&nbsp;(example: 0.05) (т.е 5%)</span></p>\r\n<p class=\"p1\" style=\"margin: 0px; font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 12px; line-height: normal; font-family: Helvetica;\"><span style=\"font-size: medium;\">Описание (<strong>description</strong>) - тип данных&nbsp;<strong>String</strong>&nbsp;(example: Tasty apples from Latvia)</span></p>\r\n<p class=\"p2\" style=\"margin: 0px; font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 12px; line-height: normal; font-family: Helvetica; min-height: 14px;\"><span style=\"font-size: medium;\">&nbsp;</span></p>\r\n<p class=\"p1\" style=\"margin: 0px; font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 12px; line-height: normal; font-family: Helvetica;\"><span style=\"font-size: medium;\">В случае если основное поле не было введено - не записывать продукт, а сообщить пользователю о том, что поле не введено или введено некорректно.</span></p>\r\n<p class=\"p2\" style=\"margin: 0px; font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 12px; line-height: normal; font-family: Helvetica; min-height: 14px;\"><span style=\"font-size: medium;\">&nbsp;</span></p>\r\n<p class=\"p1\" style=\"margin: 0px; font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 12px; line-height: normal; font-family: Helvetica;\"><span style=\"font-size: medium;\">При добавлении продукта пользователь не указывает id.&nbsp;<strong>Id присваивается в коде</strong>.</span></p>\r\n<p class=\"p1\" style=\"margin: 0px; font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 12px; line-height: normal; font-family: Helvetica;\"><span style=\"font-size: medium;\">Например: при первом добавлении продукта id будет 0, при добавлении следующего продукта у нового будет уже 1 и т.д.</span></p>\r\n<p class=\"p2\" style=\"margin: 0px; font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 12px; line-height: normal; font-family: Helvetica; min-height: 14px;\"><span style=\"font-size: medium;\">&nbsp;</span></p>\r\n<p class=\"p1\" style=\"margin: 0px; font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 12px; line-height: normal; font-family: Helvetica;\"><span style=\"font-size: medium;\">В случае если на продукт есть скидка то при отображении информации на консоле должно быть понятно, что есть скидка. Например:</span></p>\r\n<p class=\"p2\" style=\"margin: 0px; font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 12px; line-height: normal; font-family: Helvetica; min-height: 14px;\"><span style=\"font-size: medium;\">&nbsp;</span></p>\r\n<p class=\"p1\" style=\"margin: 0px; font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 12px; line-height: normal; font-family: Helvetica;\"><span style=\"font-size: medium;\">Product information:</span></p>\r\n<p class=\"p1\" style=\"margin: 0px; font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 12px; line-height: normal; font-family: Helvetica;\"><span style=\"font-size: medium;\">Id: 123</span></p>\r\n<p class=\"p1\" style=\"margin: 0px; font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 12px; line-height: normal; font-family: Helvetica;\"><span style=\"font-size: medium;\">Name: Apple</span></p>\r\n<p class=\"p1\" style=\"margin: 0px; font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 12px; line-height: normal; font-family: Helvetica;\"><span style=\"font-size: medium;\">Regular price: 0.14</span></p>\r\n<p class=\"p1\" style=\"margin: 0px; font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 12px; line-height: normal; font-family: Helvetica;\"><span style=\"font-size: medium;\">Discount: 50%</span></p>\r\n<p class=\"p1\" style=\"margin: 0px; font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 12px; line-height: normal; font-family: Helvetica;\"><span style=\"font-size: medium;\">Actual price: 0.07</span></p>\r\n<p class=\"p2\" style=\"margin: 0px; font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 12px; line-height: normal; font-family: Helvetica; min-height: 14px;\">&nbsp;</p>\r\n<p class=\"p1\" style=\"margin: 0px; font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 12px; line-height: normal; font-family: Helvetica;\"><span style=\"font-size: medium;\">В качестве&nbsp;<strong>InMemoryDatabase</strong>&nbsp;можно использовать любую&nbsp;<strong>коллекцию</strong>.</span></p>\r\n<p class=\"p1\" style=\"margin: 0px; font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 12px; line-height: normal; font-family: Helvetica;\">&nbsp;</p>\r\n<p class=\"p2\" style=\"margin: 0px; font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 12px; line-height: normal; font-family: Helvetica; min-height: 14px;\"><span style=\"font-size: medium;\"><strong><span style=\"color: #ff0000;\">Усложнения</span></strong>:</span></p>\r\n<p class=\"p1\" style=\"margin: 0px; font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 12px; line-height: normal; font-family: Helvetica;\"><span style=\"font-size: medium;\">Необходимо иметь возможность легко внедрять новые фичи (<strong>features</strong>) и легко убирать.</span></p>\r\n<p class=\"p2\" style=\"margin: 0px; font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 12px; line-height: normal; font-family: Helvetica; min-height: 14px;\"><span style=\"font-size: medium;\">&nbsp;</span></p>\r\n<p class=\"p1\" style=\"margin: 0px; font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 12px; line-height: normal; font-family: Helvetica;\"><span style=\"font-size: medium;\">Необходимо разделить функционал таким образом, чтобы UI Console / бизнес логика (service) и база (Database) были разделены по уровням (используйте разделение по packages).</span></p>\r\n<p class=\"p1\" style=\"margin: 0px; font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 12px; line-height: normal; font-family: Helvetica;\"><span style=\"font-size: medium;\">На уровне UI-console только общение с пользователем (т.е меню)</span></p>\r\n<p class=\"p1\" style=\"margin: 0px; font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 12px; line-height: normal; font-family: Helvetica;\"><span style=\"font-size: medium;\">На уровне service вся бизнес логика приложения (так же валидация)</span></p>\r\n<p class=\"p1\" style=\"margin: 0px; font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 12px; line-height: normal; font-family: Helvetica;\"><span style=\"font-size: medium;\">На уровне database только база данных (коллекция)</span></p>\r\n<p class=\"p2\" style=\"margin: 0px; font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 12px; line-height: normal; font-family: Helvetica; min-height: 14px;\"><span style=\"font-size: medium;\">&nbsp;</span></p>\r\n<p class=\"p1\" style=\"margin: 0px; font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 12px; line-height: normal; font-family: Helvetica;\"><span style=\"color: #ff0000; font-size: medium;\"><strong>Функциональные усложнения:</strong></span></p>\r\n<p class=\"p1\" style=\"margin: 0px; font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 12px; line-height: normal; font-family: Helvetica;\"><span style=\"font-size: medium;\">1. Добавить возможность получить список определенной категории</span></p>\r\n<p class=\"p1\" style=\"margin: 0px; font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 12px; line-height: normal; font-family: Helvetica;\"><span style=\"font-size: medium;\">2. Добавить возможность менять информацию о продукте. Например поменять цену или добавить/убрать скидку</span></p>\r\n<p class=\"p1\" style=\"margin: 0px; font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 12px; line-height: normal; font-family: Helvetica;\"><span style=\"font-size: medium;\">3. Добавить возможность поставить скидку на все товары определенной категории</span></p>\r\n<p class=\"p1\" style=\"margin: 0px; font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 12px; line-height: normal; font-family: Helvetica;\"><span style=\"font-size: medium;\">4.&nbsp;</span><span style=\"font-size: medium;\">Программа должны быть протестирована при помощи&nbsp;<strong>jUnit</strong>. (Можно и при помощи самописных тестов)</span></p>\r\n<p class=\"p1\" style=\"margin: 0px; font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 12px; line-height: normal; font-family: Helvetica;\"><span style=\"font-size: medium;\">*Меню не тестировать</span></p>\r\n<p class=\"p1\" style=\"margin: 0px; font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 12px; line-height: normal; font-family: Helvetica;\"><span style=\"font-size: medium;\">5. Покрытие тестами&nbsp;<strong>(code coverage</strong>) &gt; 70%</span></p>\r\n<p class=\"p2\" style=\"margin: 0px; font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 12px; line-height: normal; font-family: Helvetica; min-height: 14px;\"><span style=\"font-size: medium;\">&nbsp;</span></p>\r\n<p class=\"p1\" style=\"margin: 0px; font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 12px; line-height: normal; font-family: Helvetica;\"><span style=\"color: #ff0000; font-size: medium;\"><strong>Супер уровень:</strong></span></p>\r\n<p class=\"p1\" style=\"margin: 0px; font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 12px; line-height: normal; font-family: Helvetica;\"><span style=\"font-size: medium;\">1. Познакомиться с&nbsp;<strong>build tool maven</strong>&nbsp;и внедрить в проект</span></p>\r\n<p class=\"p1\" style=\"margin: 0px; font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 12px; line-height: normal; font-family: Helvetica;\"><span style=\"font-size: medium;\">Убедиться что работают команды&nbsp;<strong>clean</strong>,&nbsp;<strong>install</strong></span></p>\r\n<p class=\"p1\" style=\"margin: 0px; font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 12px; line-height: normal; font-family: Helvetica;\"><span style=\"font-size: medium;\">2. Залить проект на&nbsp;<strong>github</strong></span></p>','Система учета продуктов','','','Final Project');


DROP TABLE IF EXISTS `COURSE`;
CREATE TABLE `COURSE` (
                          "ID" BIGINT NOT NULL,
                          "SUBTITLE" VARCHAR(255),
                          "TEXT" CLOB,
                          "TITLE" VARCHAR(255),
                          "IMG_SRC" VARCHAR(255)
);
INSERT INTO `COURSE` VALUES (30, 'SubTitleCourse_1', '<p>TEXT</p>', 'Course 1', ''),
                            (31, 'SubTitleCourse_2', '', 'Course 2', ''),
                            (32, 'SubTitleCourse_3', '', 'Course 3', ''),
                            (33, 'SubTitleCourse_4', '', 'Course 4', '');


DROP TABLE IF EXISTS `COURSE_TO_LECTURE`;
CREATE TABLE `COURSE_TO_LECTURE` (
    "ID" BIGINT NOT NULL,
    "COURSE_ID" BIGINT,
    "LECTURE_ID" BIGINT
);
INSERT INTO `COURSE_TO_LECTURE` VALUES (60, 30, 41),
                                       (61, 30, 41),
                                       (62, 30, 42),
                                       (63, 30, 41),
                                       (64, 30, 42),
                                       (65, 30, 41),
                                       (66, 30, 42),
                                       (67, 30, 40),
                                       (68, 30, 41),
                                       (69, 30, 42),
                                       (130, 31, 43),
                                       (131, 31, 44),
                                       (132, 31, 45),
                                       (133, 31, 46),
                                       (134, 32, 45),
                                       (135, 32, 46),
                                       (136, 32, 47),
                                       (137, 32, 48),
                                       (138, 33, 40),
                                       (139, 33, 41),
                                       (140, 33, 42),
                                       (141, 33, 46),
                                       (142, 33, 47),
                                       (143, 33, 48);


DROP TABLE IF EXISTS `LECTURE`;
CREATE TABLE `LECTURE`(    "ID" BIGINT NOT NULL,
                           "SUBTITLE" VARCHAR(255),
                           "TEXT" CLOB,
                           "TITLE" VARCHAR(255)
);
INSERT INTO `LECTURE` VALUES (40, 'subtitleLecture_1', '<p>fasdfasdf</p>', 'Lecture_1'),
                             (41, 'subtitleLecture_2', '', 'Lecture_2'),
                             (42, 'subtitleLecture_3', '', 'Lecture_3'),
                             (43, 'subtitleLecture_4', '', 'Lecture_4'),
                             (44, 'subtitleLecture_5', '', 'Lecture_5'),
                             (45, 'subtitleLecture_6', '', 'Lecture_6'),
                             (46, 'subtitleLecture_7', '', 'Lecture_7'),
                             (47, 'subtitleLecture_8', '', 'Lecture_8'),
                             (48, 'subtitleLecture_9', '', 'Lecture_9');
drop sequence HIBERNATE_SEQUENCE;
CREATE SEQUENCE "PUBLIC"."HIBERNATE_SEQUENCE" START WITH 1000;
-- CREATE SEQUENCE "PUBLIC"."SYSTEM_SEQUENCE_9F3699A7_FBAF_4CDF_830E_AFF4F749549D" START WITH 94 BELONGS_TO_TABLE;
-- CREATE SEQUENCE "PUBLIC"."SYSTEM_SEQUENCE_5E0BE822_A5E8_486E_BD00_D9455A2C0412" START WITH 81 BELONGS_TO_TABLE;

DROP TABLE  IF EXISTS `book`;

CREATE TABLE `book` (
    `id` int(20) NOT NULL,
    `img` varchar(255) DEFAULT NULL,
    `name` varchar(255) DEFAULT NULL,
    `author` varchar(100) DEFAULT NULL,
    `edition` varchar(32) DEFAULT NULL,
    `publisher` varchar(255) DEFAULT NULL,
    `published` varchar(255) DEFAULT NULL,
    `isbn` varchar(30) DEFAULT NULL
);

INSERT INTO `book` VALUES (1, 'https://www.iconninja.com/files/854/364/1/develop-programming-software-java-language-code-command-icon.png',
                              'book1',
                              'author1',
                              'edition1',
                              'publisher1',
                              'published1',
                              'isbn1');
