package ifrs.poa;

import static io.restassured.RestAssured.given;

import org.jboss.logging.Logger;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import ifrs.poa.model.Project;
import ifrs.poa.model.Status;
import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
class ProjectResourceTest {
    private static final Logger LOGGER = Logger.getLogger(ProjectResourceTest.class);

    @BeforeAll
    void setup() {
        Project baseProject = new Project("Projeto inicial", "Base", Status.FINISHED);
        given()
                .body(baseProject)
                .header("Content-Type", "application/json")
                .when()
                .post("/projects/create");
    }

    @Test
    void createSuccess() {
        Project successfullProject = new Project("Projeto teste", "testando endpoint", Status.ACTIVE);
        given()
                .body(successfullProject)
                .header("Content-Type", "application/json")
                .when()
                .post("/projects/create")
                .then()
                .statusCode(200);
    }

    @Test
    void createFailure() {
        Project failureProject = new Project();
        given()
                .body(failureProject)
                .header("Content-Type", "application/json")
                .when()
                .post("/projects/create")
                .then()
                .statusCode(200);
    }

    @Test
    void listSuccess() {
        // exemplo usando Logger (JBoss Logging/Quarkus)
        LOGGER.info("Iniciando requisição GET /projects/list");
        given()
                .when()
                .get("/projects/list")
                .then()
                .log().all() // loga resposta do RestAssured
                .statusCode(200);
        LOGGER.info("Requisição finalizada");
    }
}