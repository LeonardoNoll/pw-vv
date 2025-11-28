
    package dev.ifrs;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;


@QuarkusTest
    class ManagerResourceTest {

        @BeforeAll
        static void setup() {
            try {
                given()
                    .formParam("username", "testuser")
                    .formParam("email", "testuser@example.com")
                    .formParam("password", "testpass")
                    .formParam("confirmPassword", "testpass")
                    .when().post("/manager/users/register")
                    .then().statusCode(201);
            } catch (AssertionError | Exception ignored) {
                // User might already exist from previous test runs
            }
        }

        @Test
        void testHelloEndpoint() {
            given()
                    .when().get("/hello")
                    .then()
                    .statusCode(200)
                    .body(is("Hello from Quarkus REST"));
        }

        // User Management Tests
        @Test
        void testUserRegistration() {
            given()
                    .formParam("username", "testuser")
                    .formParam("email", "testuser@example.com")
                    .formParam("password", "testpass")
                    .formParam("confirmPassword", "testpass")
                    .when().post("/manager/users/register")
                    .then()
                    .statusCode(201);
        }

        @Test
        void testUserRegistrationUnmatchedPasswords() {
            given()
                    .formParam("email", "testuser@example.com")
                    .formParam("password", "testpass")
                    .formParam("confirmPassword", "differentpass")
                    .when().post("/manager/users/register")
                    .then()
                    .statusCode(400);
        }

        @Test
        void testUserLogin() {
            given()
                    .formParam("email", "testuser@example.com")
                    .formParam("password", "testpass")
                    .when().post("/manager/users/login")
                    .then()
                    .statusCode(200);
        }

        @Test
        void testUserLoginInvalid() {
            given()
                    .formParam("email", "testuser@example.com")
                    .formParam("password", "wrongpass")
                    .when().post("/manager/users/login")
                    .then()
                    .statusCode(401);
        }

        // Additional tests

        @Test
        void testRegisterMissingFields() {
            given()
                    .formParam("username", "incomplete")
                    // missing email and passwords
                    .when().post("/manager/users/register")
                    .then()
                    .statusCode(400);
        }

        @Test
        void testLoginMissingParams() {
            given()
                    .formParam("email", "no-password@example.com")
                    .when().post("/manager/users/login")
                    .then()
                    .statusCode(400);
        }

        @Test
        void testJwtMissingBody() {
            given()
                    .when().post("/manager/jwt")
                    .then()
                    .statusCode(400);
        }

        // Endpoints protected by roles: expect unauthorized when not authenticated
        @Test
        void testGetUsersUnauthorized() {
            given()
                    .when().get("/manager/users/list")
                    .then()
                    .statusCode(401);
        }

        @Test
        void testUpdateUserUnauthorized() {
            String payload = "{\"name\":\"name\",\"email\":\"e@example.com\",\"password\":\"p\"}";
            given()
                    .contentType("application/json")
                    .body(payload)
                    .when().post("/manager/users/update")
                    .then()
                    .statusCode(401);
        }

        @Test
        void testDeleteUserUnauthorized() {
            String payload = "{\"name\":\"name\",\"email\":\"e@example.com\"}";
            given()
                    .contentType("application/json")
                    .body(payload)
                    .when().post("/manager/users/delete")
                    .then()
                    .statusCode(401);
        }

        @Test
        void testGetBooksUnauthorized() {
            given()
                    .when().get("/manager/list")
                    .then()
                    .statusCode(401);
        }

        @Test
        void testGetProjectsUnauthorized() {
            given()
                    .when().get("/manager/projects/list")
                    .then()
                    .statusCode(401);
        }

        @Test
        void testCreateProjectUnauthorized() {
            String proj = "{\"name\":\"proj\",\"description\":\"desc\"}";
            given()
                    .contentType("application/json")
                    .body(proj)
                    .when().post("/manager/projects/create")
                    .then()
                    .statusCode(401);
        }

        @Test
        void testUpdateProjectUnauthorized() {
            String proj = "{\"name\":\"updated\"}";
            given()
                    .contentType("application/json")
                    .body(proj)
                    .when().put("/manager/projects/1")
                    .then()
                    .statusCode(401);
        }

        @Test
        void testDeleteProjectUnauthorized() {
            given()
                    .when().delete("/manager/projects/1")
                    .then()
                    .statusCode(401);
        }

        @Test
        void testGetProjectByIdUnauthorized() {
            given()
                    .when().get("/manager/projects/1")
                    .then()
                    .statusCode(401);
        }
    }