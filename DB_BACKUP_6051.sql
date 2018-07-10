DROP DATABASE IF EXISTS `helpdesk`;
CREATE DATABASE `helpdesk`;

USE `helpdesk`;

DROP TABLE IF EXISTS `day_off`;
DROP TABLE IF EXISTS `day_off_type`;
DROP TABLE IF EXISTS `issue`;
DROP TABLE IF EXISTS `account_has_skill`;
DROP TABLE IF EXISTS `skill`;
DROP TABLE IF EXISTS `category`;
DROP TABLE IF EXISTS `account`;
DROP TABLE IF EXISTS `role`;

CREATE TABLE `role` (
  id int NOT NULL AUTO_INCREMENT,
  name char(80) NOT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE `account` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `email` varchar(45) NOT NULL,
  `first_name` varchar(45) DEFAULT NULL,
  `last_name` varchar(45) DEFAULT NULL,
  `birth_day` datetime,
  `address` varchar(250),
  `avatar_url` varchar(500) DEFAULT NULL,
  `password` varchar(200),
  `status` varchar(100) NOT NULL,
  `created_at` datetime NOT NULL DEFAULT NOW(),
  `updated_at` datetime NOT NULL DEFAULT NOW(),
  `joiningDate` datetime DEFAULT NOW(),
  `vertification_token` char(255),
  `token` char(255),
  `role_id` int NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_user_role` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `category` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `skill`;
CREATE TABLE `skill` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) DEFAULT NULL,
  `category_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_skill_category` FOREIGN KEY (`category_id`) REFERENCES `category` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `account_has_skill` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `account_id` int(11) NOT NULL,
  `skill_id` int(11) NOT NULL,
  PRIMARY KEY(`id`),
  CONSTRAINT `fk_account_has_skill_account` FOREIGN KEY (`account_id`) REFERENCES `account` (`id`) ON DELETE CASCADE ,
  CONSTRAINT `fk_account_has_skill_skill` FOREIGN KEY (`skill_id`) REFERENCES `skill` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `day_off_type` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `type` varchar(20) NOT NULL,
   `quota` int(11) NOT NULL,
   `remaining_time` int(11) NOT NULL,
  `account_id` int(11) NOT NULL,
  PRIMARY KEY (`id`), 
  CONSTRAINT `fk_day_off_type_account` FOREIGN KEY (`account_id`) REFERENCES `account` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE `day_off` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(200) DEFAULT NULL,
  `content` varchar(1000) DEFAULT NULL,
  `start_date` datetime DEFAULT NULL,
  `end_date` datetime DEFAULT NULL,
  `created_at` datetime NOT NULL DEFAULT NOW(),
  `updated_at` datetime NOT NULL DEFAULT NOW(),
  `number_of_hours` int(11) NOT NULL,
  `status` varchar(45) NOT NULL,
  `token` char(255) NOT NULL,
  `account_id` int(11) NOT NULL,
  `type` varchar(100) NOT NULL,
  `type_id` int(11),
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_day_off_account` FOREIGN KEY (`account_id`) REFERENCES `account` (`id`) ON DELETE CASCADE ,
  CONSTRAINT `fk_day_off_type` FOREIGN KEY (`type_id`) REFERENCES `day_off_type` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `issue` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(200) NOT NULL,
  `content` varchar(1000) NOT NULL,
  `status` varchar(45) NOT NULL,
  `created_at` datetime NOT NULL DEFAULT NOW(),
  `updated_at` datetime NOT NULL DEFAULT NOW(),
  `reply_message` varchar(1000) DEFAULT "",
  `account_id` int(11) NOT NULL,
  `token` varchar(256),
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_issue_account` FOREIGN KEY (`account_id`) REFERENCES `account` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



INSERT INTO `role`(name) VALUES ("ADMIN");
INSERT INTO `role`(name) VALUES ("CLERK");
INSERT INTO `role`(name) VALUES ("USER");

INSERT INTO `account`(email, first_name, last_name, password, status, token, role_id)
VALUES("helpdesk@novahub.vn", "desk", "help", "$2a$10$A21YwZHzKPMTQy1dnZEFyuA5KOHlGqfIMUdpU5Uk3LehhhfY1/2ja", "ACTIVE", "abcdefghijk123456789", 1);
INSERT INTO `account`(email, first_name, last_name, password, status, token, role_id)
<<<<<<< HEAD
VALUES("huong@novahub.vn", "mai", "huong", "$2a$10$A21YwZHzKPMTQy1dnZEFyuA5KOHlGqfIMUdpU5Uk3LehhhfY1/2ja", "ACTIVE", "abcdefghijk123456789", 2);
INSERT INTO `account`(email, first_name, last_name, password, status, token, role_id)
VALUES("ngoc@novahub.vn", "bui lam", "quang ngoc", "$2a$10$A21YwZHzKPMTQy1dnZEFyuA5KOHlGqfIMUdpU5Uk3LehhhfY1/2ja", "ACTIVE", "abcdefghijk123456789", 3);
=======
VALUES("ngocbui@novahub.vn", "mai", "huong", "$2a$10$A21YwZHzKPMTQy1dnZEFyuA5KOHlGqfIMUdpU5Uk3LehhhfY1/2ja", "ACTIVE", "abcdefghijk123456789", 2);
INSERT INTO `account`(email, first_name, last_name, password, status, token, role_id)
VALUES("huong@novahub.vn", "bui lam", "quang ngoc", "$2a$10$A21YwZHzKPMTQy1dnZEFyuA5KOHlGqfIMUdpU5Uk3LehhhfY1/2ja", "ACTIVE", "abcdefghijk123456789", 3);
>>>>>>> HD-25-ngoc
INSERT INTO `account`(email, first_name, last_name, password, status, token, role_id)
VALUES("hai@novahub.vn", "bui lam", "thanh hai", "$2a$10$A21YwZHzKPMTQy1dnZEFyuA5KOHlGqfIMUdpU5Uk3LehhhfY1/2ja", "ACTIVE", "abcdefghijk123456789", 3);

INSERT INTO `category`(name) VALUES
("Programming Language"),
("Backend Framework"),
("Frontend Framework");

INSERT INTO `skill`(name, category_id) VALUES 
("Java", 1),
("Ruby", 1),
("C#", 1),
("Python", 1),
("Spring", 2),
("Rails", 2),
("Angular", 3),
("Reactjs", 3);

INSERT INTO `account_has_skill`(account_id, skill_id) VALUES
(1, 1),
(1, 2),
(1, 3),
(1, 4),
(2, 2),
(2, 3),
(2, 6);

INSERT INTO `issue`(title, content, status, account_id)
VALUES("title", "content", "pending", 1);
INSERT INTO `issue`(title, content, status, account_id)
VALUES("title1", "content1", "pending", 1);
INSERT INTO `issue`(title, content, status, account_id)
VALUES("title", "content", "pending", 2);
INSERT INTO `issue`(title, content, status, account_id)
VALUES("title1", "content1", "pending", 2);
INSERT INTO `issue`(title, content, status, account_id)
VALUES("title", "content", "pending", 3);
INSERT INTO `issue`(title, content, status, account_id)
VALUES("title1", "content1", "pending", 3);
