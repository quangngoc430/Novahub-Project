-- MySQL dump 10.13  Distrib 5.7.22, for Linux (x86_64)
--
-- Host: localhost    Database: helpdesk
-- ------------------------------------------------------
-- Server version	5.7.22-0ubuntu0.16.04.1

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `account`
--

DROP TABLE IF EXISTS `account`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `account` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `email` varchar(45) NOT NULL,
  `first_name` varchar(45) DEFAULT NULL,
  `last_name` varchar(45) DEFAULT NULL,
<<<<<<< HEAD
  `birth_day` datetime DEFAULT NULL,
  `address` varchar(250) DEFAULT NULL,
  `avatar_url` varchar(500) DEFAULT NULL,
  `password` varchar(200) DEFAULT NULL,
  `status` varchar(100) NOT NULL,
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `joiningDate` datetime DEFAULT CURRENT_TIMESTAMP,
  `vertification_token` char(255) DEFAULT NULL,
  `token` char(255) DEFAULT NULL,
  `role_id` int(11) NOT NULL,
  `joining_date` datetime DEFAULT NULL,
=======
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
>>>>>>> develop
  PRIMARY KEY (`id`),
  KEY `fk_user_role` (`role_id`),
  CONSTRAINT `fk_user_role` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

<<<<<<< HEAD
--
-- Dumping data for table `account`
--

LOCK TABLES `account` WRITE;
/*!40000 ALTER TABLE `account` DISABLE KEYS */;
INSERT INTO `account` VALUES (1,'helpdesk@novahub.vn','desk','help',NULL,NULL,NULL,'$2a$10$A21YwZHzKPMTQy1dnZEFyuA5KOHlGqfIMUdpU5Uk3LehhhfY1/2ja','ACTIVE','2018-07-15 20:11:18','2018-07-15 20:11:18','2018-07-15 20:11:18',NULL,'abcdefghijk123456789',1,NULL),(3,'huong@novahub.vn','bui lam','quang ngoc',NULL,NULL,NULL,'$2a$10$A21YwZHzKPMTQy1dnZEFyuA5KOHlGqfIMUdpU5Uk3LehhhfY1/2ja','ACTIVE','2018-07-15 20:11:18','2018-07-15 20:11:18','2018-07-15 20:11:18',NULL,'abcdefghijk123456789',3,NULL),(4,'linhtran@novahub.vn','bui lam','thanh hai',NULL,NULL,NULL,'$2a$10$A21YwZHzKPMTQy1dnZEFyuA5KOHlGqfIMUdpU5Uk3LehhhfY1/2ja','ACTIVE','2018-07-15 20:11:18','2018-07-15 20:11:18','2018-07-15 20:11:18',NULL,'abcdefghijk123456789',3,NULL);
/*!40000 ALTER TABLE `account` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `account_has_skill`
--
=======
CREATE TABLE `category` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) DEFAULT NULL,
  `created_at` datetime NOT NULL DEFAULT NOW(),
  `updated_at` datetime NOT NULL DEFAULT NOW(),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `skill`;
CREATE TABLE `skill` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) DEFAULT NULL,
  `level` int NOT NULL,
  `category_id` int(11) NOT NULL,
  `created_at` datetime NOT NULL DEFAULT NOW(),
  `updated_at` datetime NOT NULL DEFAULT NOW(),
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_skill_category` FOREIGN KEY (`category_id`) REFERENCES `category` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
>>>>>>> develop

DROP TABLE IF EXISTS `account_has_skill`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `account_has_skill` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `account_id` int(11) NOT NULL,
  `skill_id` int(11) NOT NULL,
<<<<<<< HEAD
  PRIMARY KEY (`id`),
  KEY `fk_account_has_skill_account` (`account_id`),
  KEY `fk_account_has_skill_skill` (`skill_id`),
  CONSTRAINT `fk_account_has_skill_account` FOREIGN KEY (`account_id`) REFERENCES `account` (`id`) ON DELETE CASCADE,
=======
  `created_at` datetime NOT NULL DEFAULT NOW(),
  `updated_at` datetime NOT NULL DEFAULT NOW(),
  PRIMARY KEY(`id`),
  CONSTRAINT `fk_account_has_skill_account` FOREIGN KEY (`account_id`) REFERENCES `account` (`id`) ON DELETE CASCADE ,
>>>>>>> develop
  CONSTRAINT `fk_account_has_skill_skill` FOREIGN KEY (`skill_id`) REFERENCES `skill` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

<<<<<<< HEAD
--
-- Dumping data for table `account_has_skill`
--

LOCK TABLES `account_has_skill` WRITE;
/*!40000 ALTER TABLE `account_has_skill` DISABLE KEYS */;
INSERT INTO `account_has_skill` VALUES (1,1,1),(2,1,2),(3,1,3),(4,1,4);
/*!40000 ALTER TABLE `account_has_skill` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `category`
--

DROP TABLE IF EXISTS `category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `category` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) DEFAULT NULL,
=======
CREATE TABLE `day_off_type` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `type` varchar(20) NOT NULL,
>>>>>>> develop
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `category`
--

LOCK TABLES `category` WRITE;
/*!40000 ALTER TABLE `category` DISABLE KEYS */;
INSERT INTO `category` VALUES (1,'Programming Language'),(2,'Backend Framework'),(3,'Frontend Framework');
/*!40000 ALTER TABLE `category` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `category_skill_list`
--

DROP TABLE IF EXISTS `category_skill_list`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `category_skill_list` (
  `category_id` bigint(20) NOT NULL,
  `skill_list_id` bigint(20) NOT NULL,
  UNIQUE KEY `UK_8gjt8ug4rhoxmnu1gmi1m19da` (`skill_list_id`),
  KEY `FK4n0fc0wmha796g4ekswm4tcgd` (`category_id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `category_skill_list`
--

LOCK TABLES `category_skill_list` WRITE;
/*!40000 ALTER TABLE `category_skill_list` DISABLE KEYS */;
/*!40000 ALTER TABLE `category_skill_list` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `day_off`
--

DROP TABLE IF EXISTS `day_off`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `day_off` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(200) DEFAULT NULL,
  `content` varchar(1000) DEFAULT NULL,
  `start_date` datetime DEFAULT NULL,
  `end_date` datetime DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `number_of_hours` int(11) NOT NULL,
  `status` varchar(45) NOT NULL,
  `token` char(255) NOT NULL,
  `account_id` int(11) NOT NULL,
  `type` varchar(100) NOT NULL,
  `type_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_day_off_account` (`account_id`),
  KEY `fk_day_off_type` (`type_id`),
  CONSTRAINT `fk_day_off_account` FOREIGN KEY (`account_id`) REFERENCES `account` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_day_off_type` FOREIGN KEY (`type_id`) REFERENCES `day_off_type` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `day_off`
--

--
-- Table structure for table `day_off_type`
--

DROP TABLE IF EXISTS `day_off_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `day_off_type` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `type` varchar(20) NOT NULL,
  `year` int(11) NOT NULL,
  `quota` int(11) NOT NULL,
  `remaining_time` int(11) NOT NULL,
  `account_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_day_off_type_account` (`account_id`),
  CONSTRAINT `fk_day_off_type_account` FOREIGN KEY (`account_id`) REFERENCES `account` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `day_off_type`
--

LOCK TABLES `day_off_type` WRITE;
/*!40000 ALTER TABLE `day_off_type` DISABLE KEYS */;
INSERT INTO `day_off_type` VALUES (2,'Nghi phep nam',2018,15,15,4);
/*!40000 ALTER TABLE `day_off_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `issue`
--

DROP TABLE IF EXISTS `issue`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `issue` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(200) NOT NULL,
  `content` varchar(1000) NOT NULL,
  `status` varchar(45) NOT NULL,
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `reply_message` varchar(1000) DEFAULT '',
  `account_id` int(11) NOT NULL,
  `token` varchar(256) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_issue_account` (`account_id`),
  CONSTRAINT `fk_issue_account` FOREIGN KEY (`account_id`) REFERENCES `account` (`id`) ON DELETE CASCADE
<<<<<<< HEAD
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `issue`
--

LOCK TABLES `issue` WRITE;
/*!40000 ALTER TABLE `issue` DISABLE KEYS */;
INSERT INTO `issue` VALUES (1,'title','content','pending','2018-07-15 20:11:18','2018-07-15 20:11:18','',1,NULL),(2,'title1','content1','pending','2018-07-15 20:11:18','2018-07-15 20:11:18','',1,NULL),(5,'title','content','pending','2018-07-15 20:11:18','2018-07-15 20:11:18','',3,NULL),(6,'title1','content1','pending','2018-07-15 20:11:18','2018-07-15 20:11:18','',3,NULL);
/*!40000 ALTER TABLE `issue` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `role`
--

DROP TABLE IF EXISTS `role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `role` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` char(80) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `role`
--

LOCK TABLES `role` WRITE;
/*!40000 ALTER TABLE `role` DISABLE KEYS */;
INSERT INTO `role` VALUES (1,'ADMIN'),(2,'CLERK'),(3,'USER');
/*!40000 ALTER TABLE `role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `skill`
--

DROP TABLE IF EXISTS `skill`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `skill` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) DEFAULT NULL,
  `category_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_skill_category` (`category_id`),
  CONSTRAINT `fk_skill_category` FOREIGN KEY (`category_id`) REFERENCES `category` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `skill`
--

LOCK TABLES `skill` WRITE;
/*!40000 ALTER TABLE `skill` DISABLE KEYS */;
INSERT INTO `skill` VALUES (1,'Java',1),(2,'Ruby',1),(3,'C#',1),(4,'Python',1),(5,'Spring',2),(6,'Rails',2),(7,'Angular',3),(8,'Reactjs',3);
/*!40000 ALTER TABLE `skill` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `skill_account_has_skill_list`
--

DROP TABLE IF EXISTS `skill_account_has_skill_list`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `skill_account_has_skill_list` (
  `skill_id` bigint(20) NOT NULL,
  `account_has_skill_list_id` bigint(20) NOT NULL,
  UNIQUE KEY `UK_nm80ugk2n9dm62v2uqk1q0isl` (`account_has_skill_list_id`),
  KEY `FKhef77hosn2nkn09y3f0fxvdcr` (`skill_id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `skill_account_has_skill_list`
--

LOCK TABLES `skill_account_has_skill_list` WRITE;
/*!40000 ALTER TABLE `skill_account_has_skill_list` DISABLE KEYS */;
/*!40000 ALTER TABLE `skill_account_has_skill_list` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2018-07-17  9:29:07
=======
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



INSERT INTO `role`(name) VALUES ("ADMIN");
INSERT INTO `role`(name) VALUES ("CLERK");
INSERT INTO `role`(name) VALUES ("USER");

INSERT INTO `account`(email, first_name, last_name, password, status, token, role_id)
VALUES("helpdesk@novahub.vn", "desk", "help", "$2a$10$A21YwZHzKPMTQy1dnZEFyuA5KOHlGqfIMUdpU5Uk3LehhhfY1/2ja", "ACTIVE", "abcdefghijk123456789", 1);
INSERT INTO `account`(email, first_name, last_name, password, status, token, role_id)
VALUES("huong@novahub.vn", "mai", "huong", "$2a$10$A21YwZHzKPMTQy1dnZEFyuA5KOHlGqfIMUdpU5Uk3LehhhfY1/2ja", "ACTIVE", "abcdefghijk123456789", 2);
INSERT INTO `account`(email, first_name, last_name, password, status, token, role_id)
VALUES("ngoc@novahub.vn", "bui lam", "quang ngoc", "$2a$10$A21YwZHzKPMTQy1dnZEFyuA5KOHlGqfIMUdpU5Uk3LehhhfY1/2ja", "ACTIVE", "abcdefghijk123456789", 3);
INSERT INTO `account`(email, first_name, last_name, password, status, token, role_id)
VALUES("hai@novahub.vn", "bui lam", "thanh hai", "$2a$10$A21YwZHzKPMTQy1dnZEFyuA5KOHlGqfIMUdpU5Uk3LehhhfY1/2ja", "ACTIVE", "abcdefghijk123456789", 3);

INSERT INTO `category`(name) VALUES
("Programming Language"),
("Backend Framework"),
("Frontend Framework");

INSERT INTO `skill`(name, category_id, level) VALUES
("Java", 1, 7),
("Ruby", 1, 6),
("C#", 1, 5),
("Python", 1, 2),
("Spring", 2, 3),
("Rails", 2, 5),
("Angular", 3, 10),
("Reactjs", 3, 6);

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
>>>>>>> develop
