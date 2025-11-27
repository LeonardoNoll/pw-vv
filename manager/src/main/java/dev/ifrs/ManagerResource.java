package dev.ifrs;

import java.util.List;

import org.eclipse.microprofile.rest.client.inject.RestClient;

import dev.ifrs.client.BookClient;
import dev.ifrs.client.UserClient;
import dev.ifrs.model.Book;
import dev.ifrs.model.User;
import io.quarkus.logging.Log;
import io.smallrye.mutiny.Uni;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/manager")
public class ManagerResource {

    @Inject
    @RestClient
    BookClient bookClient;

    @Inject
    @RestClient
    UserClient userClient;

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
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @PermitAll
    public Uni<String> loginUser(@FormParam("email") String email,
            @FormParam("password") String password) {
        Log.info("Logging in user through manager with email: " + email);
        return userClient.loginUser(email, password);
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
}
