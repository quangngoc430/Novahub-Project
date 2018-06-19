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
  `birth_date` int(11),
  `address` varchar(250),
  `avatar_url` varchar(300) DEFAULT NULL,
  `password` varchar(200) NOT NULL,
  `total_number_of_hours` INT NOT NULL,
  `remain_number_of_hours` INT NOT NULL,
  `status` varchar(100) NOT NULL,
  `created_at` datetime NOT NULL DEFAULT NOW(),
  `updated_at` datetime NOT NULL DEFAULT NOW(),
  `token` char(255) NOT NULL,
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
  CONSTRAINT `fk_skill_category` FOREIGN KEY (`category_id`) REFERENCES `category` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;	

CREATE TABLE `account_has_skill` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `account_id` int(11) NOT NULL,
  `skill_id` int(11) NOT NULL,
  PRIMARY KEY(`id`), 
  CONSTRAINT `fk_account_has_skill_account` FOREIGN KEY (`account_id`) REFERENCES `account` (`id`),
  CONSTRAINT `fk_account_has_skill_skill` FOREIGN KEY (`skill_id`) REFERENCES `skill` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `day_off_type` (
  `id` int(11) NOT NULL AUTO_INCREMENT, 
  `type` varchar(20) NOT NULL,
  PRIMARY KEY (`id`)
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
  `type_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_day_off_account` FOREIGN KEY (`account_id`) REFERENCES `account` (`id`),
  CONSTRAINT `fk_day_off_type` FOREIGN KEY (`type_id`) REFERENCES `day_off_type` (`id`)
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
  CONSTRAINT `fk_issue_account` FOREIGN KEY (`account_id`) REFERENCES `account` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



INSERT INTO `role`(name) VALUES ("ADMIN");
INSERT INTO `role`(name) VALUES ("CLERK");
INSERT INTO `role`(name) VALUES ("USER");


INSERT INTO `account`(email, first_name, last_name, password, total_number_of_hours, remain_number_of_hours, status, token, role_id) 
VALUES("admin@gmail.com", "ngoc", "bui", "password", 14, 14, "NONE", "abcdefghijk123456789", 1);
INSERT INTO `account`(email, first_name, last_name, password, total_number_of_hours, remain_number_of_hours, status, token, role_id) 
VALUES("huong@gmail.com", "mai", "huong", "password", 14, 14, "NONE", "abcdefghijk123456789", 1);
INSERT INTO `account`(email, first_name, last_name, password, total_number_of_hours, remain_number_of_hours, status, token, role_id) 
VALUES("ngoc@gmail.com", "bui lam", "quang ngoc", "password", 14, 14, "NONE", "abcdefghijk123456789", 3);
INSERT INTO `account`(email, first_name, last_name, password, total_number_of_hours, remain_number_of_hours, status, token, role_id) 
VALUES("hai@gmail.com", "bui lam", "thanh hai", "password", 14, 14, "NONE", "abcdefghijk123456789", 3); 


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