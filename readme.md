# Helpdesk

Helpdesk is a website which helps people to find help from other guys, request day off and send some issues to admin.

Features of the website:

* Authentication:
    * Allow users to login in with google account which ends with '@novahub.vn'.

* Users management:
    * Having three types of a role into the system:
        * admin
        * clerk
        * user (staff)
    * The role only edit directly into the database (Cannot edit with the website).

* Profile management:
    * Allow users to edit their information.

* Skills management:
    * Allow admin to create group skills (category):
    * Allow users to add skills to their profile which follow the categories were created by admin.
    * Allow users to search, filer skills.
    * Allow the user to see all of the skills of other users.
* Day-offs support:
    * Allow the managers to edit the number of days of users.
    * Displays the remaining number of annual leave days
    * Allow users to create day-off request and email to admin (helpdesk@novahub.vn). Having more types of day-off, you can see more on the policy website of the company:
    https://confluence.novahub.vn/pages/viewpage.action?spaceKey=POL&title=Policy
    * The manager can approve or deny requests,  an email will send back to the user requested
    * In a situation, the request was approved, the website will send an email to notify the clerk

*  Issues support:
    * Allow users to suggest an issue to admin, such as:
        * Salary advance  
        * Request company to sponsor for organizing a party
        * Request to go to the company be late/ leave early
        * Request to refund Insurance money
        * ........

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
### Run project with dev profile and auto seeding data
```sh 
$ mvn spring-boot:run -Dspring-boot.run.profiles=dev
```
## Deploy to server
### Step 1: Build and upload .jar file to server
- Modify application.properties
```
spring.datasource.driver-class-name=your_db_driver
spring.datasource.url=your_db_url
spring.datasource.username=your_db_username
spring.datasource.password=yor_db_password
server.port=8888
logging.file=/var/log/helpdesk.log
```
- Build application
```
mvn clean package -Dmaven.test.skip=true

```
- Connect server
```
ssh helpdesk@helpdesk.novahub.vn
```
- Make a new folder
```
$ cd /apps/helpdesk/be/releases/
$ mkdir $(date + %Y%m%d_%H%M%S)
```
- Make a symbolic link current to new folder
```
ln -s /apps/helpdesk/be/releases/new_folder_name_just_created_above /apps/helpdesk/be/current
```
- Copy .jar file from local to server 
```
scp path/to/jar/file helpdesk@helpdesk.novahub.vn:/apps/helpdesk/be/releases/new_folder_name_just_created_above
```
### Step 2: Helpdesk configuration
- Copy file helpdesk to server
```
sudo scp path/to/helpdesk/file helpdesk@helpdesk.novahub.vn:/etc/nginx/sites-available/helpdesk
```
- Make a symbolic link of helpdesk file inside sites-enabled folder
```
sudo ln -s /etc/nginx/sites-available/helpdesk /etc/nginx/sites-enabled/helpdesk
```
### Step 3: Modify nginx.conf (IMPORTANT)
Add this line to http context of nginx.conf
```
underscores_in_headers on;
```
### Step 4: Make a service file
Copy helpdesk.service to server
```
sudo scp path/to/helpdesk.service helpdesk@helpdesk.novahub.vn:/etc/systemd/system/helpdesk.service
```
### Step 5: Restart nginx and start service
- Start helpdesk service
```
sudo systemctl start helpdesk
```
- Restart nginx
```
sudo systemctl restart nginx
```
### NOTE: Log file
Log file of helpdesk app is stored in /var/log/helpdesk.log





