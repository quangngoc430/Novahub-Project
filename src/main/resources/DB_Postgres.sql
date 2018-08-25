DROP DATABASE IF EXISTS helpdesk;
CREATE DATABASE helpdesk;
\c helpdesk;

DROP TABLE IF EXISTS role CASCADE;
DROP TABLE IF EXISTS account CASCADE ;
DROP TABLE IF EXISTS day_off CASCADE ;
DROP TABLE IF EXISTS day_off_account CASCADE ;
DROP TABLE IF EXISTS day_off_type CASCADE ;
DROP TABLE IF EXISTS issue CASCADE ;
DROP TABLE IF EXISTS account_has_skill CASCADE ;
DROP TABLE IF EXISTS skill CASCADE ;
DROP TABLE IF EXISTS level CASCADE ;
DROP TABLE IF EXISTS category CASCADE ;
DROP TABLE IF EXISTS token CASCADE ;

CREATE TABLE role (
  id BIGSERIAL PRIMARY KEY,
  name VARCHAR(80) NOT NULL
);

CREATE TABLE account (
  id BIGSERIAL PRIMARY KEY,
  email VARCHAR(45) NOT NULL,
  first_name VARCHAR(45) DEFAULT NULL,
  last_name VARCHAR(45) DEFAULT NULL,
  birth_day TIMESTAMP WITH TIME ZONE,
  address VARCHAR(250),
  phone VARCHAR(20),
  title VARCHAR(250),
  introduction VARCHAR(1000),
  avatar_url VARCHAR(500) DEFAULT NULL,
  password VARCHAR(200),
  status VARCHAR(100) NOT NULL,
  created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
  updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
  joiningDate TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
  vertification_token VARCHAR(255),
  role_id BIGINT REFERENCES role(id)
);

CREATE TABLE token (
  id BIGSERIAL PRIMARY KEY,
  access_token VARCHAR(256) NOT NULL,
  expired_in INT NOT NULL,
  expired_at TIMESTAMP WITH TIME ZONE NOT NULL,
  account_id BIGINT REFERENCES account(id),
  created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
  updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now()
);

CREATE TABLE category (
  id BIGSERIAL PRIMARY KEY,
  name VARCHAR(100) DEFAULT NULL,
  created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
  updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now()
);

CREATE TABLE skill (
  id BIGSERIAL PRIMARY KEY,
  name VARCHAR(45) DEFAULT NULL,
  category_id BIGINT REFERENCES category(id),
  created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
  updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now()
);

CREATE TABLE account_has_skill (
  id BIGSERIAL PRIMARY KEY,
  level INT NOT NULL,
  account_id BIGINT REFERENCES account(id),
  skill_id BIGINT REFERENCES skill(id),
  created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
  updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now()
);

CREATE TABLE day_off_type (
  id SERIAL PRIMARY KEY,
  type VARCHAR(20) NOT NULL,
  default_quota INT NOT NULL
);

CREATE TABLE day_off_account (
  id BIGSERIAL PRIMARY KEY,
  year INT NOT NULL,
  remaining_time INT NOT NULL,
  private_quota INT NOT NULL,
  day_off_type_id INT REFERENCES day_off_type(id),
  account_id BIGINT REFERENCES account(id)
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
  day_off_account_id BIGINT REFERENCES day_off_account(id)
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
VALUES('linhtran@novahub.vn', 'linh', 'tran', '$2a$10$A21YwZHzKPMTQy1dnZEFyuA5KOHlGqfIMUdpU5Uk3LehhhfY1/2ja', 'ACTIVE', 2);

INSERT INTO category(name) VALUES
('Programming Language'),
('Backend Framework'),
('Frontend Framework'),
('Web Design');

INSERT INTO skill(name, category_id) VALUES
('Java', 1),
('Javascript', 3),
('Ruby', 1),
('Perl', 1),
('ReactJS', 3),
('HTML', 4);

INSERT INTO account_has_skill(account_id, skill_id, level) VALUES
(1, 1, 6),
(1, 2, 5),
(1, 3, 9),
(1, 4, 5),
(2, 2, 8),
(2, 3, 3),
(2, 6, 10);

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