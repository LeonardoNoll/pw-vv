
package dev.ifrs;

import static io.restassured.RestAssured.given;

import java.util.logging.Logger;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dev.ifrs.model.User;
import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
class ManagerResourceTest {
    private static Logger logger = Logger.getLogger("AnnotationsTest");

    private static final String USERNAME_PARAM = "username";
    private static final String EMAIL_PARAM = "email";
    private static final String PASSWORD_PARAM = "password";
    private static final String CONFIRM_PASSWORD_PARAM = "confirmPassword";

    private static final String TEST_USER_NAME = "testuser";
    private static final String TEST_USER_EMAIL = "testuser@example.com";
    private static final String TEST_USER_PASSWORD = "testpass";

    private static final User unauthorizedUser = new User();
    static {
        unauthorizedUser.setName("name");
        unauthorizedUser.setEmail("e@example.com");
        unauthorizedUser.setPassword("p");
    }

    @BeforeAll
    static void setup() {
        try {
            logger.info("Setting up test user");
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
    @DisplayName("Test User Registration")
    void testUserRegistration() {
        logger.info("Testing user registration");
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
    @DisplayName("Test User Registration With Unmatched Passwords")
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
    @DisplayName("Test User Login")
    void testUserLogin() {
        given()
                .formParam(EMAIL_PARAM, TEST_USER_EMAIL)
                .formParam(PASSWORD_PARAM, TEST_USER_PASSWORD)
                .when().post("/manager/users/login")
                .then()
                .statusCode(200);
    }

    @Test
    @DisplayName("Test User Login With Invalid Credentials")
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
    @DisplayName("Test User Register With Missing Fields")
    void testRegisterMissingFields() {
        given()
                .formParam(USERNAME_PARAM, "incomplete")
                // missing email and passwords
                .when().post("/manager/users/register")
                .then()
                .statusCode(400);
    }

    @Test
    @DisplayName("Test User Login With Missing Parameters")
    void testLoginMissingParams() {
        given()
                .formParam(EMAIL_PARAM, "no-password@example.com")
                .when().post("/manager/users/login")
                .then()
                .statusCode(400);
    }

    @Test
    @DisplayName("Test JWT Generation With Missing Body")
    void testJwtMissingBody() {
        given()
                .when().post("/manager/jwt")
                .then()
                .statusCode(400);
    }

    // Endpoints protected by roles: expect unauthorized when not authenticated
    @Test
    @DisplayName("Test Get Users Unauthorized")
    void testGetUsersUnauthorized() {
        given()
                .when().get("/manager/users/list")
                .then()
                .statusCode(401);
    }

    @Test
    @DisplayName("Test Update User Unauthorized")
    void testUpdateUserUnauthorized() {
        given()
                .contentType("application/json")
                .body(unauthorizedUser)
                .when().post("/manager/users/update")
                .then()
                .statusCode(401);
    }

    @Test
    void testDeleteUserUnauthorized() {
        given()
                .contentType("application/json")
                .body(unauthorizedUser)
                .when().post("/manager/users/delete")
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