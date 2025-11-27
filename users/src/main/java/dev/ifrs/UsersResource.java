package dev.ifrs;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.eclipse.microprofile.jwt.Claims;

import dev.ifrs.model.User;
import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.quarkus.logging.Log;
import io.smallrye.jwt.build.Jwt;
import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/users")
public class UsersResource {

    private static final String ISSUER = "users-issuer";

    @POST
    @Path("/jwt")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public String getToken(User user) {
        return Jwt.issuer(ISSUER)
                .upn(user.getEmail())
                .groups(new HashSet<>(Arrays.asList("User", "Admin")))
                .claim(Claims.nickname, user.getName())
                .sign();
    }

    // NOTE: LOGIN AND REGISTER WORKING
    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_PLAIN)
    public Uni<String> loginUser(@FormParam("email") String email,
            @FormParam("password") String password) {
        Log.info("Logging in user with email: " + email);
        return User.find("email = ?1 and password = ?2", email, password).firstResult()
                .onItem().ifNotNull().transform(item -> {
                    User user = (User) item;
                    return Jwt.issuer(ISSUER)
                            .upn(user.getEmail())
                            .groups(new HashSet<>(Arrays.asList("User", "Admin")))
                            .claim(Claims.nickname, user.getName())
                            .sign();
                })
                .onItem().ifNull().failWith(new IllegalArgumentException("Invalid credentials"));
    }

    @POST
    @Path("/register")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    @WithTransaction
    public Uni<User> registerUser(@FormParam("name") String name,
            @FormParam("email") String email,
            @FormParam("password") String password,
            @FormParam("confirmPassword") String confirmPassword) {
        if (!password.equals(confirmPassword)) {
            throw new IllegalArgumentException("Passwords do not match");
        }
        Log.info("Creating user: " + name + " with email: " + email);
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(password);

        return user.persistAndFlush();
    }

    @GET
    @Path("/list")
    @Produces(MediaType.APPLICATION_JSON)
    @WithTransaction
    public Uni<List<User>> listUser() {
        return User.findAll().list();
    }

    @POST
    @Path("/update")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @WithTransaction
    public Uni<User> updateUser(User user) {
        return User.<User>findById(user.id)
                .onItem().ifNotNull()
                .call(item -> {
                    item.setName(user.getName());
                    item.setEmail(user.getEmail());
                    item.setPassword(user.getPassword());
                    return item.persistAndFlush();
                });
    }

    @POST
    @Path("/delete")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @WithTransaction
    public Uni<User> deleteUser(User user) {
        return User.<User>findById(user.id)
                .onItem().ifNotNull()
                .call(item -> {
                    return item.delete();
                });
    }

}
