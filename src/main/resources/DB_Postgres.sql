DROP DATABASE IF EXISTS helpdesk;
CREATE DATABASE helpdesk;
\c helpdesk;

DROP TABLE IF EXISTS day_off;
DROP TABLE IF EXISTS day_off_type;
DROP TABLE IF EXISTS issue;
DROP TABLE IF EXISTS account_has_skill;
DROP TABLE IF EXISTS skill;
DROP TABLE IF EXISTS category;
DROP TABLE IF EXISTS account;
DROP TABLE IF EXISTS role;

CREATE TABLE role (
  id SERIAL PRIMARY KEY,
  name VARCHAR(80) NOT NULL
);

CREATE TABLE account (
  id BIGSERIAL PRIMARY KEY,
  email VARCHAR(45) NOT NULL,
  first_name VARCHAR(45) DEFAULT NULL,
  last_name VARCHAR(45) DEFAULT NULL,
  birth_day TIMESTAMP WITH TIME ZONE,
  address VARCHAR(250),
  avatar_url VARCHAR(500) DEFAULT NULL,
  password VARCHAR(200),
  status VARCHAR(100) NOT NULL,
  created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
  updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
  joiningDate TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
  vertification_token VARCHAR(255),
  token VARCHAR(255),
  role_id INT REFERENCES role(id)
);

CREATE TABLE category (
  id SERIAL PRIMARY KEY,
  name VARCHAR(100) DEFAULT NULL,
  created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
  updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now()
);

CREATE TABLE skill (
  id SERIAL PRIMARY KEY,
  name VARCHAR(45) DEFAULT NULL,
  level INT NOT NULL,
  created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
  updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
  category_id INT REFERENCES category(id)
);

CREATE TABLE account_has_skill (
  id BIGSERIAL PRIMARY KEY,
  account_id BIGINT REFERENCES account(id),
  skill_id BIGINT REFERENCES skill(id),
  created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
  updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now()
);

CREATE TABLE common_day_off_type (
  id SERIAL PRIMARY KEY,
  type VARCHAR(20) NOT NULL,
  quota INT NOT NULL
);

CREATE TABLE day_off_type (
  id BIGSERIAL PRIMARY KEY,
  year INT NOT NULL,
  remaining_time INT NOT NULL,
  common_type_id INT REFERENCES common_day_off_type(id),
  account_id INT REFERENCES account(id)
);


CREATE TABLE day_off (
  id BIGSERIAL PRIMARY KEY,
  comment VARCHAR(1000) DEFAULT NULL,
  start_date TIMESTAMP WITH TIME ZONE DEFAULT NULL,
  end_date TIMESTAMP WITH TIME ZONE DEFAULT NULL,
  created_at TIMESTAMP WITH TIME ZONE DEFAULT NULL,
  updated_at TIMESTAMP WITH TIME ZONE DEFAULT NULL,
  number_of_hours INT NOT NULL,
  status VARCHAR(45) NOT NULL,
  token VARCHAR(255) NOT NULL,
  account_id BIGINT REFERENCES account(id),
  type_id BIGINT REFERENCES day_off_type(id)
);

CREATE TABLE issue (
  id BIGSERIAL PRIMARY KEY,
  title VARCHAR(200) NOT NULL,
  content VARCHAR(1000) NOT NULL,
  status VARCHAR(45) NOT NULL,
  created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
  updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
  reply_message VARCHAR(1000) DEFAULT NULL,
  token VARCHAR(256),
  account_id BIGINT REFERENCES account(id)
);



INSERT INTO role(name) VALUES ('ADMIN');
INSERT INTO role(name) VALUES ('CLERK');
INSERT INTO role(name) VALUES ('USER');

INSERT INTO account(email, first_name, last_name, password, status, role_id)
VALUES('helpdesk@novahub.vn', 'help', 'desk', '$2a$10$A21YwZHzKPMTQy1dnZEFyuA5KOHlGqfIMUdpU5Uk3LehhhfY1/2ja', 'ACTIVE', 1);
INSERT INTO account(email, first_name, last_name, password, status, role_id)
VALUES('ngocbui@novahub.vn', 'ngoc', 'bui', '$2a$10$A21YwZHzKPMTQy1dnZEFyuA5KOHlGqfIMUdpU5Uk3LehhhfY1/2ja', 'ACTIVE', 3);
INSERT INTO account(email, first_name, last_name, password, status, role_id)
VALUES('linhtran@novahub.vn', 'linh', 'tran', '$2a$10$A21YwZHzKPMTQy1dnZEFyuA5KOHlGqfIMUdpU5Uk3LehhhfY1/2ja', 'ACTIVE', 3);


INSERT INTO category(name) VALUES
('Programming Language'),
('Backend Framework'),
('Frontend Framework'),
('Web Design');

INSERT INTO skill(name, category_id, level) VALUES
('Java', 1, 7),
('Ruby', 1, 6),
('C#', 1, 5),
('Python', 1, 2),
('Spring', 2, 3),
('Rails', 2, 5),
('Angular', 3, 10),
('Reactjs', 3, 6);

INSERT INTO account_has_skill(account_id, skill_id) VALUES
(1, 1),
(1, 2),
(1, 3),
(1, 4),
(2, 2),
(2, 3),
(2, 6);

INSERT INTO issue(title, content, status, account_id)
VALUES('title', 'content', 'PENDING', 1);
INSERT INTO issue(title, content, status, account_id)
VALUES('title1', 'content1', 'PENDING', 1);
INSERT INTO issue(title, content, status, account_id)
VALUES('title', 'content', 'PENDING', 2);
INSERT INTO issue(title, content, status, account_id)
VALUES('title1', 'content1', 'PENDING', 2);
INSERT INTO issue(title, content, status, account_id)
VALUES('title', 'content', 'PENDING', 3);
INSERT INTO issue(title, content, status, account_id)
VALUES('title1', 'content1', 'PENDING', 3);
