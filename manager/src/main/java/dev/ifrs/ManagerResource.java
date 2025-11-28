package dev.ifrs;

import java.util.List;

import org.eclipse.microprofile.rest.client.inject.RestClient;

import dev.ifrs.client.BookClient;
import dev.ifrs.client.ProjectClient;
import dev.ifrs.client.UserClient;
import dev.ifrs.model.Book;
import dev.ifrs.model.Project;
import dev.ifrs.model.User;
import io.quarkus.logging.Log;
import io.smallrye.mutiny.Uni;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/manager")
public class ManagerResource {

    @Inject
    @RestClient
    BookClient bookClient;

    @Inject
    @RestClient
    UserClient userClient;

    @Inject
    @RestClient
    ProjectClient projectClient;

    // USER MANAGEMENT
    @POST
    @Path("/jwt")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    @PermitAll
    public String getJwt(User user) {
        Log.info("Generating JWT for manager");
        return userClient.getToken(user);
    }

    @POST
    @Path("users/register")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @PermitAll
    public Uni<User> registerUser(@FormParam("name") String name,
            @FormParam("email") String email,
            @FormParam("password") String password,
            @FormParam("confirmPassword") String confirmPassword) {
        Log.info("Registering user through manager: " + name + " with email: " + email);
        return userClient.registerUser(name, email, password, confirmPassword);
    }

    @POST
    @Path("users/login")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_PLAIN)
    @PermitAll
    public Uni<String> loginUser(@FormParam("email") String email,
            @FormParam("password") String password) {
        Log.info("Logging in user through manager with email: " + email);
        return userClient.loginUser(email, password);
    }

    @GET
    @Path("/users/list")
    // @RolesAllowed("Admin")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<List<User>> getUsers() {
        Log.info("Fetching user list for manager");
        return userClient.listUser();
    }

    @POST
    @Path("/users/update")
    @RolesAllowed("Admin")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<User> updateUser(User user) {
        Log.info("Updating user through manager: " + user.getName() + " with email: " + user.getEmail());
        return userClient.updateUser(user);
    }

    @POST
    @Path("/users/delete")
    @RolesAllowed("Admin")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<User> deleteUser(User user) {
        Log.info("Deleting user through manager: " + user.getName() + " with email: " + user.getEmail());
        return userClient.deleteUser(user);
    }

    // BOOK MANAGEMENT
    @GET
    @Path("/list")
    @RolesAllowed("User")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Book> getBooks() {
        Log.info("Fetching book list for manager");
        return bookClient.listBooks();
    }

    // Project MANAGEMENT
    @GET
    @Path("/projects/list")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("User")
    public Uni<Response> getProjects() {
        Log.info("Fetching project list for manager");
        return projectClient.list();
    }

    @POST
    @Path("/projects/create")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("User")
    public Uni<Response> createProject(Project project) {
        Log.info("Creating project through manager: " + project.getName());
        return projectClient.create(project);
    }

    @PUT
    @Path("/projects/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("User")
    public Uni<Response> updateProject(@PathParam("id") Long id, Project payload) {
        Log.info("Updating project through manager with ID: " + id);
        return projectClient.update(id, payload);
    }

    @DELETE
    @Path("/projects/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("User")
    public Uni<Response> deleteProject(@PathParam("id") Long id) {
        Log.info("Deleting project through manager with ID: " + id);
        return projectClient.delete(id);
    }

    @GET
    @Path("/projects/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("User")
    public Uni<Response> getProjectById(@PathParam("id") Long id) {
        Log.info("Fetching project details for manager with ID: " + id);
        return projectClient.getProjectById(id);
    }
}
