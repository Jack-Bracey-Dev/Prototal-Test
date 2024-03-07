# Given Specification:

**Requirements:**
Implement a backend REST API using Spring Boot where a user can sign up and sign in using postman. Once the user has signed in, they can view the secret message.

**User Cases**
- As a user, I would like to **SIGN UP** to the service so I can gain access to view the *Secret Message* once **SIGNED IN**.
- As a user, I would like to **SIGN IN** to the service so I can view the *secret message*.
- As a user, I can only view the **SECRET MESSAGE** once logged in. If I try to call the endpoint when *not authenticated*, the response should throw an **unauthorised error response (401)**.
- As a user, I can ***reset my password whilst logged in**. Once I reset my password, I will be **unauthenticated** and asked to sign in again. If I try to reset my password *without logging in*, the system will **throw an unauthorised error response (401)**.
- As a user I **shouldn't** be able to create *duplicate credentials*, if I attempt to create again the API should respond with **400 Bad Request with an explanation of why its bad request**.
- As a user I need to supply a **JWT/bearer token to authenticate** to the backend.

**Data Model**
| Object | Required Fields | Data Sample |
|--|--|--|
| User | First Name | Bob |
|  | Last Name | Martin |
|  | Email Address | dev@prototaluk.com |
|  | Password | Password123!2 |
|--|--|--|
| Secret | Name | First Name + Last Name |
| Message | Secret Message | A computer would deserve to be called intelligent if it could deceive a human into believing that it was human. |

**Endpoint Management**
**POST** */sign-up* responds
**POST** */sign-in* responds
**POST** */sign-out*
**GET** */secret-message*
**POST** */reset-my-password*

**Considerations**
- Ensure the code is unit tested/integration tested.
- The password needs to be encrypted and decrypted during /sign in, /sign-out
- Adhere to OOP Principles/ SOLID Principles
- Can use a database, our preference is MongoDB.
- Simple solutions are best.
- Solution should be portable for us to review – consider utilising docker.

**Bonus Points**
- Use of DDD/Hexagonal Architecture
- Use of Spring WebFlux for reactive design
- Correct use of Mocking
- Correct HTTP Status Codes Returned
- Implemented Swagger to Document API

**End of specification**

# Design decisions:
- Used MongoDB - Never used a NoSQL database, so was interested to see its capabilities and wanted to learn a bit about the technology during the development stage.
- Swagger - To generate documentation for the API - Useful to maintaining up-to-date documentation
- io.jsonwebtoken - For generating JWTs for tracking authenticated sessions - Provides in-built functionality to easily generate JWTs that contain an expiry time. Expiry time can be set (In milliseconds) in the application.properties `runtime.jwt.expires` 
- JUnit - Unit tests for writing test cases for controllers and any testable methods.

**Note:** I didn't use WebFlux as I had a small window to build the project and wanted to meet the deadline. I had also decided to use MongoDB and didn't want to add too many unknowns to the development process.

# Setup:
- Clone the repository
- Open the project in an ide (I'm using IntelliJ)
- Import maven imports
- Maven compile
- In the terminal, run `docker-compose up -d` to spin up a docker container with the MongoDB database.
- The application.properties is already setup, in a normal project, I'd commit a template application.properties file with the values removed, but for this I didn't see the setup complication as worth it.
- Run the run configuration `Runtime`

**Running the JUnit tests**
There should be a stored runtime configuration named `All in prototal-test`. This will run through the JUnit tests.

**API Details**
When the project is running, **swagger** should spin up and be accessible on the following link.
**Swagger Docs**: http://localhost:8080/swagger-ui/index.html

There is also a **postman collection** found in the project files `prototal-test/postman_collection.json`. You can import this into postman by either dragging the file straight into the collections panel in postman or by pressing `Import` and then drag the file into the box that pops up.

---

***POST /sign-up***: Endpoint for signing up new users to the service.
**Body** *(Json)*:

    { 
	    "firstName": "",
	    "surname": "",
	    "email": "",
	    "password": ""
    }
    
---
***POST /sign-in***: Endpoint for signing in to a user account.
**Authorization**:
*Basic Auth*: Email : Password
**Returns**: JWT token that should be used in further requests to authenticate the request.

---

***GET /secret-message***: Returns the secret message if the request is authenticated.
**Authorization**:
*Bearer Token*: JWT token taken from the response body of /sign-in

---

***POST /sign-out***: Sign the currently logged in user.
**Authorization**:
*Bearer Token*: JWT token taken from the response body of /sign-in

---

***POST /reset-my-password***: Change the password on the currently logged in account.
**Authentication**:
*Bearer Token*: JWT token taken from the response body of /sign-in
**Body** *(Json)*:

    { 
	    "currentPassword": "",
	    "newPassword": "",
	    "newPasswordCopy": ""
    }
