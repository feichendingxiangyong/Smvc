CREATE SCHEMA IF NOT EXISTS `test` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci ;
SHOW WARNINGS;
USE `test` ;

DROP TABLE `test`.`student`;
CREATE TABLE `test`.`student` (
    id integer primary key auto_increment,
    name varchar(32) not null,
    password varchar(64) not null default  '',
    school  varchar(64) not null
    );
    
DROP TABLE `test`.`grade`;
CREATE TABLE `test`.`grade` (
    id integer primary key auto_increment,
    class_name varchar(32) not null,
    score integer not null,
    student_id integer
    );