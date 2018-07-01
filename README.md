# A simple REST API for CRM services

The aim of this project is the development of a simple, secure REST API that can be easily used for CRM service purposes.

## Deployment and run of the project

If you want deploy and run the project on your local machine, continue reading this session. These manual steps can be simplified by checking out the project directly in your IDE.

### Prerequisites

- Java 8
- Maven 3.x

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

Since the application is based on Spring-Boot, all the configurations are specified in the files ```application.properties``` and ```logback.xml```.

## Endpoints

### Customers

| Method | Url | Decription |
| ------ | --- | ---------- |
| GET    | /customer/getAll             | Get all the existing customers |
| POST   | /customer/create             | Create a new customer |
| GET    | /customer/get/{id}           | Get the customer with the specified id |
| PATCH  | /customer/update/{id}        | Update the customer with the specified id |
| POST   | /customer/uploadPicture/{id} | Upload a picture for the customer with the specified id |
| GET    | /customer/findPicture/{id}   | Get the picture for the customer with the specified id |
| DELETE | /customer/delete/{id}        | Delete the customer with the specified id |


### Users

| Method | Url | Decription |
| ------ | --- | ---------- |
| GET    | /user/getAll             | Get all the existing users |
| POST   | /user/create             | Create a new user |
| GET    | /user/get/{id}           | Get the user with the specified id |
| PATCH  | /user/update/{id}        | Update the user with the specified id |
| DELETE | /user/delete/{id}        | Delete the user with the specified id |


### Security

The application supports [Oauth 2.0](https://oauth.net/2/) protocol. All the performed requests must be authorized.

As for OAuth2 specification, the authorization process can be performed with the request

```
http://localhost:[configured_server_port]/oauth/authorize
```

by specifying the following parameters

- **response_type=code**
- client_id=[configure_client_id]
- scope=[configured_scope]
- redirect_uri=https://xxxxxx.xxx

together with username and password in the prompted login page.

Once authenticated and obtained an authorization code **[auth_code]**, a token can be request 

```
http://localhost:[configure_dserver__port]/oauth/token
```

by specifying the following parameters

- client_id=[configure_client_id]
- client_secret=[configured_client_secret]
- grant_type=authorization_code
- code=**[auth_code]**

Finally a result like this one will be obtained

```
{ 
  "access_token"  : "1dcbf2a1-5739-4e9e-b0f6-429a743cfebe",
  "token_type"    : "bearer",
  "expires_in"    : "120",
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



