INSERT INTO `role`(name) VALUES ("ADMIN");
INSERT INTO `role`(name) VALUES ("CLERK");
INSERT INTO `role`(name) VALUES ("USER");

INSERT INTO `account`(email, first_name, last_name, password, status, role_id)
VALUES("helpdesk@novahub.vn", "help", "desk", "$2a$10$A21YwZHzKPMTQy1dnZEFyuA5KOHlGqfIMUdpU5Uk3LehhhfY1/2ja", "ACTIVE", 1);
INSERT INTO `account`(email, first_name, last_name, password, status, role_id)
VALUES("ngocbui@novahub.vn", "ngoc", "bui", "$2a$10$A21YwZHzKPMTQy1dnZEFyuA5KOHlGqfIMUdpU5Uk3LehhhfY1/2ja", "ACTIVE", 2);
INSERT INTO `account`(email, first_name, last_name, password, status, role_id)
VALUES("linhtran@novahub.vn", "linh", "tran", "$2a$10$A21YwZHzKPMTQy1dnZEFyuA5KOHlGqfIMUdpU5Uk3LehhhfY1/2ja", "ACTIVE", 3);
INSERT INTO `account`(email, first_name, last_name, password, status, role_id)
VALUES("vutran@novahub.vn", "vu", "tran", "$2a$10$A21YwZHzKPMTQy1dnZEFyuA5KOHlGqfIMUdpU5Uk3LehhhfY1/2ja", "ACTIVE", 3);

INSERT INTO `category`(name) VALUES
("Programming Language"),
("Backend Framework"),
("Frontend Framework"),
("Web Design");

INSERT INTO `skill`(name, category_id) VALUES
("Java", 1),
("Javascript", 3),
("Ruby", 1),
("Perl", 1),
("ReactJS", 3),
("HTML", 4);

INSERT INTO `level`(value, account_id, skill_id) VALUES
(6, 1, 1),
(5, 1, 2),
(9, 1, 3),
(5, 1, 4),
(8, 2, 2),
(3, 2, 3),
(10, 2, 6);

INSERT INTO `account_has_skill`(account_id, skill_id) VALUES
(1, 1),
(1, 2),
(1, 3),
(1, 4),
(2, 2),
(2, 3),
(2, 6);

INSERT INTO `issue`(title, content, status, account_id)
VALUES("title", "content", "PENDING", 1);
INSERT INTO `issue`(title, content, status, account_id)
VALUES("title1", "content1", "PENDING", 1);
INSERT INTO `issue`(title, content, status, account_id)
VALUES("title", "content", "PENDING", 2);
INSERT INTO `issue`(title, content, status, account_id)
VALUES("title1", "content1", "PENDING", 2);
INSERT INTO `issue`(title, content, status, account_id)
VALUES("title", "content", "PENDING", 3);
INSERT INTO `issue`(title, content, status, account_id)
VALUES("title1", "content1", "PENDING", 3);