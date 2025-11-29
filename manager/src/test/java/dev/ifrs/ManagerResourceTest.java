
package dev.ifrs;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
class ManagerResourceTest {
    private static final String USERNAME_PARAM = "username";
    private static final String EMAIL_PARAM = "email";
    private static final String PASSWORD_PARAM = "password";
    private static final String CONFIRM_PASSWORD_PARAM = "confirmPassword";

    private static final String TEST_USER_NAME = "testuser";
    private static final String TEST_USER_EMAIL = "testuser@example.com";
    private static final String TEST_USER_PASSWORD = "testpass";

    @BeforeAll
    static void setup() {
        try {
            given()
                    .formParam(USERNAME_PARAM, TEST_USER_NAME)
                    .formParam(EMAIL_PARAM, TEST_USER_EMAIL)
                    .formParam(PASSWORD_PARAM, TEST_USER_PASSWORD)
                    .formParam(CONFIRM_PASSWORD_PARAM, TEST_USER_PASSWORD)
                    .when().post("/manager/users/register")
                    .then().statusCode(201);
        } catch (AssertionError | Exception ignored) {
            // User might already exist from previous test runs
        }
    }

    // User Management Tests
    @Test
    void testUserRegistration() {
        given()
                .formParam(USERNAME_PARAM, TEST_USER_NAME)
                .formParam(EMAIL_PARAM, TEST_USER_EMAIL)
                .formParam(PASSWORD_PARAM, TEST_USER_PASSWORD)
                .formParam(CONFIRM_PASSWORD_PARAM, TEST_USER_PASSWORD)
                .when().post("/manager/users/register")
                .then()
                .statusCode(201);
    }

    @Test
    void testUserRegistrationUnmatchedPasswords() {
        given()
                .formParam(EMAIL_PARAM, TEST_USER_EMAIL)
                .formParam(PASSWORD_PARAM, TEST_USER_PASSWORD)
                .formParam(CONFIRM_PASSWORD_PARAM, "differentpass")
                .when().post("/manager/users/register")
                .then()
                .statusCode(400);
    }

    @Test
    void testUserLogin() {
        given()
                .formParam(EMAIL_PARAM, TEST_USER_EMAIL)
                .formParam(PASSWORD_PARAM, TEST_USER_PASSWORD)
                .when().post("/manager/users/login")
                .then()
                .statusCode(200);
    }

    @Test
    void testUserLoginInvalid() {
        given()
                .formParam(EMAIL_PARAM, TEST_USER_EMAIL)
                .formParam(PASSWORD_PARAM, "wrongpass")
                .when().post("/manager/users/login")
                .then()
                .statusCode(401);
    }

    // Additional tests

    @Test
    void testRegisterMissingFields() {
        given()
                .formParam(USERNAME_PARAM, "incomplete")
                // missing email and passwords
                .when().post("/manager/users/register")
                .then()
                .statusCode(400);
    }

    @Test
    void testLoginMissingParams() {
        given()
                .formParam(EMAIL_PARAM, "no-password@example.com")
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