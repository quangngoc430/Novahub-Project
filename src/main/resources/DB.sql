DROP DATABASE IF EXISTS `helpdesk`;
CREATE DATABASE `helpdesk`;

USE `helpdesk`;

DROP TABLE IF EXISTS `role`;
CREATE TABLE `role` (
  id int NOT NULL AUTO_INCREMENT,
  name char(80) NOT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `account`;
CREATE TABLE `account` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `email` varchar(45) UNIQUE NOT NULL,
  `first_name` varchar(45) DEFAULT NULL,
  `last_name` varchar(45) DEFAULT NULL,
  `birth_day` datetime,
  `address` varchar(250),
  `phone` varchar(20),
  `title` varchar(250),
  `introduction` varchar(1000),
  `avatar_url` varchar(500) DEFAULT NULL,
  `password` varchar(200),
  `status` varchar(100) NOT NULL,
  `created_at` datetime NOT NULL DEFAULT NOW(),
  `updated_at` datetime NOT NULL DEFAULT NOW(),
  `joiningDate` datetime DEFAULT NOW(),
  `verification_token` char(255),
  `role_id` int NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_user_role` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `token`;
CREATE TABLE `token` (
  `id` int NOT NULL AUTO_INCREMENT,
  `access_token` varchar(256) NOT NULL,
  `expired_in` int NOT NULL,
  `expired_at` datetime NOT NULL,
  `account_id` int NOT NULL,
  `created_at` datetime NOT NULL,
  `updated_at` datetime NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_token_account` FOREIGN KEY (`account_id`) REFERENCES `account`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `category`;
CREATE TABLE `category` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) UNIQUE DEFAULT NULL,
  `created_at` datetime NOT NULL DEFAULT NOW(),
  `updated_at` datetime NOT NULL DEFAULT NOW(),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `skill`;
CREATE TABLE `skill` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) DEFAULT NULL,
  `category_id` int(11) NOT NULL,
  `created_at` datetime NOT NULL DEFAULT NOW(),
  `updated_at` datetime NOT NULL DEFAULT NOW(),
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_skill_category` FOREIGN KEY (`category_id`) REFERENCES `category` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `account_has_skill`;
CREATE TABLE `account_has_skill` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `level` int NOT NULL,
  `account_id` int(11) NOT NULL,
  `skill_id` int(11) NOT NULL,
  `created_at` datetime NOT NULL DEFAULT NOW(),
  `updated_at` datetime NOT NULL DEFAULT NOW(),
  PRIMARY KEY(`id`),
  CONSTRAINT `fk_account_has_skill_account` FOREIGN KEY (`account_id`) REFERENCES `account` (`id`) ON DELETE CASCADE ,
  CONSTRAINT `fk_account_has_skill_skill` FOREIGN KEY (`skill_id`) REFERENCES `skill` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `day_off_type`;
CREATE TABLE `day_off_type` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `type` varchar(20) NOT NULL,
  `default_quota` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `day_off_account`;
CREATE TABLE day_off_account (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `year` int NOT NULL,
  `remaining_time` int NOT NULL,
  `private_quota` int NOT NULL,
  `day_off_type_id` int NOT NULL,
  `account_id` int NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_day_off_account_day_off_type` FOREIGN KEY (`day_off_type_id`) REFERENCES day_off_type(id) ON DELETE CASCADE ,
  CONSTRAINT `fk_day_off_account_account` FOREIGN KEY (`account_id`) REFERENCES account(id) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `day_off`;
CREATE TABLE `day_off` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `comment` VARCHAR(1000) DEFAULT NULL,
  `start_date` datetime DEFAULT NULL,
  `end_date` datetime DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `number_of_hours` INT NOT NULL,
  `status` VARCHAR(45) NOT NULL,
  `token` VARCHAR(255) NOT NULL,
  `account_id` int NOT NULL,
  `day_off_account_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_day_off_account` FOREIGN KEY (`account_id`) REFERENCES account(`id`) ON DELETE CASCADE ,
  CONSTRAINT `fk_day_off_day_off_account` FOREIGN KEY (`day_off_account_id`) REFERENCES day_off_account(`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `issue`;
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