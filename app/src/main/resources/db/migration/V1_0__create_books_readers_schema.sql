--H2
--CREATE TABLE IF NOT EXISTS reader (
--    `id` INTEGER PRIMARY KEY AUTO_INCREMENT NOT NULL ,
--    `first_name` varchar(30) NOT NULL ,
--    `last_name` varchar(30) NOT NULL ,
--    `punishment` real
--);
--
--CREATE TABLE IF NOT EXISTS book (
--    `id` INTEGER PRIMARY KEY AUTO_INCREMENT NOT NULL,
--    `title` varchar(30)  NOT NULL,
--    `author` varchar(30) NOT NULL,
--    `return_date` timestamp,
--    `update_punishment_date` timestamp,
--    `reader_id` INTEGER  references reader(id)
--
--);

--POSTGRES
CREATE TABLE IF NOT EXISTS reader (
    id SERIAL PRIMARY KEY NOT NULL ,
    first_name varchar(30) NOT NULL ,
    last_name varchar(30) NOT NULL ,
    punishment real
);

CREATE TABLE IF NOT EXISTS book (
    id SERIAL PRIMARY KEY NOT NULL,
    title varchar(30)  NOT NULL,
    author varchar(30) NOT NULL,
    return_date timestamp,
    update_punishment_date timestamp,
    reader_id INTEGER  references reader(id)

);

INSERT INTO reader (first_name, last_name, punishment)
VALUES ('Adam', 'Niezgodka', 0.0);

INSERT INTO reader (first_name, last_name, punishment)
VALUES ('Piotr', 'Wielki', 0.0);


INSERT INTO book (title, author, return_date, update_punishment_date, reader_id)
VALUES ('Dziady', 'Mickiewicz', null , null, null);

INSERT INTO book (title, author, return_date, update_punishment_date, reader_id)
VALUES ('Potop', 'Sienkiewicz', null, null, null);

INSERT INTO book (title, author, return_date, update_punishment_date, reader_id)
VALUES ('Cyberiada', 'Lem', '2019-05-30', '2019-05-29', 2);

INSERT INTO book (title, author, return_date, update_punishment_date, reader_id)
VALUES ('Dżuma', 'Cumas', '2019-05-30', '2019-05-29', 1);

INSERT INTO book (title, author, return_date, update_punishment_date, reader_id)
VALUES ('Cesarz', 'Kapuscinski', null, null, null);





