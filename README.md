# A simple REST API for CRM services

The aim of this project is the development of a simple and secure REST API that can be easily used for CRM service purposes.

The application stores users and customers. To each user is assigned a role, that can be either ADMIN or USER. All the users need to be authenticated to have access to the API exposed by the server. 
In particular, administrators have unlimited access, while simple users only can manipulate customers.

The application always starts with the two roles ADMIN and USER already present in the database and, if configured, also with an admin user. 

## Deployment and run of the project

If you want deploy and run the project on your local machine, continue reading this session. These manual steps can be simplified by checking out the project directly in your IDE.

### Prerequisites

- Java 8
- Maven 3.x
- an AWS account with [Amazon S3](https://aws.amazon.com/s3/) service availabl. It is important to set the ```amazonws.s3.*``` properties as described in the configuration section.

### Repository clone

```bash
https://github.com/PeppeMir/CRM_Service_API.git
```

### Run

From the cloned directory:

```
mvn spring-boot:run
```

## Configuration

The application, based on Spring-Boot, keeps all the needed configurations in the files ```application.properties``` and ```logback.xml```.

### logback.xml

With ```logback.xml``` we just configure the logging strategy, together with the text format of each line. Information about time, running thread, level and logging class have been added in addition to the logged message itself.

Furthermore, a rolling policy has been defined for the logging on file: a new log file is generated everytime that either the current file reaches the size of 10MB or the end of the day is passed. This means that we will have files named ```log.2018-07-02.0.log```, ```log.2018-07-02.1.log```, ```log.2018-07-03.0.log```, etc. 
Last but not least, a maximum number of file is maintained in the LOGS folder, in order to avoid an excessive space usage on the disk.

### application.properties

With ```application.properties``` we mainly specify configurations needed to Spring-boot.

In particular:

- ```server.port``` specifies the listening port of the application;
- ```logging.level.root``` specifies the general logging level of the application;
- ```security.oauth2.*``` specify the parameters for the OAuth2 authentication (see next section);
- ```spring.datasource.*``` specify the data source used by the application. In the given file, a MySQL server is configured;
- ```spring.jpa.*``` specify the JPA / Hibernate configuration, i.e. db schema operations (create-drop, update, ...), SQL dialect, etc.;
- ```amazonws.s3.*``` specify the S3-Amazon Web Service in which perform the storage of the pictures of the customers;
- ```datasource.startup.adminUser.*``` specify the parameters for the creation of an admin user just after the application boot. This is intended as a first run utility that allows to create a first admin user with which perform the initial setup of the application.

In particular. here there are some example of configuration  and S3:

#### for the datasource:
```
spring.datasource.url = jdbc:mysql://localhost:3306/crm_service?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC
spring.datasource.username = root
spring.datasource.password = root
spring.datasource.testWhileIdle = true
spring.datasource.validationQuery = SELECT 1

spring.jpa.show-sql = true
spring.jpa.hibernate.ddl-auto = create
spring.jpa.hibernate.naming-strategy = org.hibernate.cfg.ImprovedNamingStrategy
spring.jpa.properties.hibernate.dialect = org.hibernate.dialect.MySQL5Dialect
```

#### for Amazon S3:
```
amazonws.s3.endpointRegion = eu-west-1
amazonws.s3.accessKey = [your access key]
amazonws.s3.secretKey = [your secret key]
amazonws.s3.bucketName = crmservice-bucket
```

## Data model

![ScreenShot](https://github.com/PeppeMir/CRM_Service_API/blob/master/files/images/db-model.png)

For the very first run, it is important to run the application with the setting ```spring.jpa.hibernate.ddl-auto``` enabled to one of the values ```create```, ```create-drop``` or ```update```, depending on the preference.

## Endpoints

### Customers and Users

| Method | Url | Decription |
| ------ | --- | ---------- |
| GET    | /customer/getAll             | Get all the existing customers |
| POST   | /customer/create             | Create a new customer |
| GET    | /customer/get/{id}           | Get the customer with the specified id |
| PATCH  | /customer/update/{id}        | Update the customer with the specified id |
| POST   | /customer/uploadPicture/{id} | Upload a picture for the customer with the specified id |
| GET    | /customer/findPicture/{id}   | Get the picture for the customer with the specified id |
| DELETE | /customer/delete/{id}        | Delete the customer with the specified id |

| Method | Url | Decription |
| ------ | --- | ---------- |
| GET    | /user/getAll             | Get all the existing users |
| POST   | /user/create             | Create a new user |
| GET    | /user/get/{id}           | Get the user with the specified id |
| PATCH  | /user/update/{id}        | Update the user with the specified id |
| DELETE | /user/delete/{id}        | Delete the user with the specified id |


In order to ensure data consistency and to allow future historical/statistical searches, both the ```user/delete/{id}``` and ```customer/delete/{id}``` follow a soft-deletion politic, based on the ```active``` flag available in their data model.

Even if the data are not effectively deleted from the database, they will be not returned to the consumer of the API.
This means that if the user with ```id=1``` has been (soft-)deleted from the system, it will remain in the database but will be not part of the results of a ```/user/getAll```, for example.

### Security

The application supports [Oauth 2.0](https://oauth.net/2/) protocol. All the performed requests must be authorized.

As for OAuth2 specification, the authorization process can be performed with the POST request

```
http://localhost:[configured_server_port]/oauth/authorize
```

by specifying the following parameters

- response_type=**code**
- client_id=[configure_client_id]
- scope=[configured_scope]
- redirect_uri=[a_redirect_uri]

together with username and password in the prompted login page.

Once authenticated and obtained an authorization code **[auth_code]**, a token can be request 

```
http://localhost:[configure_dserver__port]/oauth/token
```

by specifying the following parameters

- client_id=[configure_client_id]
- client_secret=[configured_client_secret]
- grant_type=**authorization_code**
- code=**[auth_code]**

Finally a result like this one will be obtained

```
{ 
  "access_token"  : "1dcbf2a1-5739-4e9e-b0f6-429a743cfebe",
  "token_type"    : "bearer",
  "expires_in"    : "[configured_expiration_seconds]",
  "scope" : "[configured_scope]",
}
```

and the token can be used to perform authorized requests

```
http://localhost:[configured_server_port]/customer/getAll?access_token=1dcbf2a1-5739-4e9e-b0f6-429a743cfebe
```

until its expiration, which will require the user to authenticate again

```
{
    "error": "invalid_token",
    "error_description": "Access token expired: 1dcbf2a1-5739-4e9e-b0f6-429a743cfebe"
}
```

## Example of requests

Samples requests can be easily performed. for example, by using **Postman**, which can be used both as [Chrome extension](https://chrome.google.com/webstore/detail/postman/fhbjgbiflinjbdggehcddcbncdddomop) or [Desktop app](https://www.getpostman.com/).

### Example: use Postman to get all the users

By assuming that the application is running on the local machine and is listening on the port ```8080```, let us prepare the ```GET``` request for the address ```http://localhost:8080/user/getAl```, and perform it by pressing the send button

![ScreenShot](https://github.com/PeppeMir/CRM_Service_API/blob/master/files/images/0.PNG)

As expected, the application first requires the authentication. To do so using OAuth 2.0, it is enough to switch the type in the authorization tab, and press the get new access token button.

![ScreenShot](https://github.com/PeppeMir/CRM_Service_API/blob/master/files/images/1.PNG)

A new window will appear, asking for several parameters. These parameters must be set to the one specified in the configuration of the application, as we have already seen above. 

![ScreenShot](https://github.com/PeppeMir/CRM_Service_API/blob/master/files/images/2.PNG)

When all the parameters have been successfully set, we can press the request token button. Despite from the name, Postman will perform both the authorization and token request.

First you will require to insert username and password, then to approve the access to the resources under the configured scope.

![ScreenShot](https://github.com/PeppeMir/CRM_Service_API/blob/master/files/images/345.png)

At the end of the process, an access token will be returned, and can be used to perform the authenticated request that was denied before. As we can see in the next image, this time the application recognizes that we are authorized, and returns the list of all the users present in the system

![ScreenShot](https://github.com/PeppeMir/CRM_Service_API/blob/master/files/images/6.PNG)

Once obtained, the same token can be use in all the requests, until it expires.
Please remember that, in addition to the authentication, users with role ADMIN have fully access to the API, but users with role USER only have access to the ```/customer``` part.

## Postman collections

The Postman collections that have been used for the tests can be downloaded by using the following links:

- [Customers collection](https://github.com/PeppeMir/CRM_Service_API/blob/master/files/postman/CUSTOMERS.postman_collection.json)
- [Users collection](https://github.com/PeppeMir/CRM_Service_API/blob/master/files/postman/USERS.postman_collection.json)

The JSON files representing the collections can be easily imported into Postman, used and modified to perform all the required tests.

