# Helpdesk

Helpdesk is a website which helps people to find help from other guys, request day off and send some issues to admin.

## Getting Started

These instructions will get you a copy of the project up and run on your local machine for development and testing purposes. See deployment for notes on how to deploy the project on a live system.

### Setup project
Helpdesk requires:  
Java version 8
```sh
$ sudo apt-get install oracle-java8-installer
```

Mysql server
```sh
$ sudo apt-get update
$ sudo apt-get install mysql-server
```

Maven
```sh
$ sudo apt-get install maven
```

### Build project

A step by step series of examples that tell you how to get a development env running

Clone project from bitbucket
```sh
$ git clone https://<account namet>@bitbucket.org/novahub_intern/java.git
```

Then:
```sh
$ mvn install clean
```

### Execute sql files

Run two files data into the project
file 1: /src/main/resources/DB.sql
file 2: /src/main/resources/data-int.sql

Copy content into two files

```sh
$ mysql -uroot -p<your mysql password>
$ <ctrl + C all content of two files data>
```

### Run project

```sh
$ mvn spring-boot:run
```

### Guide login via normal or google account




