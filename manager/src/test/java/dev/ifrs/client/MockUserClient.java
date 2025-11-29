package dev.ifrs.client;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.microprofile.rest.client.inject.RestClient;
import jakarta.enterprise.inject.Alternative;
import jakarta.annotation.Priority;

import dev.ifrs.model.User;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;

@ApplicationScoped
@RestClient
@Alternative
@Priority(1)
public class MockUserClient implements UserClient {

    @Override
    public String getToken(User user) {
        return "mock-token-for-" + (user != null ? user.getEmail() : "anonymous");
    }

    @Override
    public Uni<String> loginUser(@FormParam("email") String email, @FormParam("password") String password) {
        if (email == null || password == null) {
            throw new WebApplicationException("Missing parameters", Response.Status.BAD_REQUEST);
        }
        if ("testpass".equals(password)) {
            return Uni.createFrom().item("mock-token");
        }
        throw new WebApplicationException("Invalid credentials", Response.Status.UNAUTHORIZED);
    }

    @Override
    public Uni<User> registerUser(@FormParam("name") String name, @FormParam("email") String email,
            @FormParam("password") String password, @FormParam("confirmPassword") String confirmPassword) {
        if (name == null || email == null || password == null || confirmPassword == null) {
            throw new WebApplicationException("Missing fields", Response.Status.BAD_REQUEST);
        }
        if (!password.equals(confirmPassword)) {
            throw new WebApplicationException("Passwords do not match", Response.Status.BAD_REQUEST);
        }
        User u = new User();
        u.setName(name);
        u.setEmail(email);
        return Uni.createFrom().item(u);
    }

    @Override
    public Uni<List<User>> listUser() {
        return Uni.createFrom().item(new ArrayList<>());
    }

    @Override
    public Uni<User> updateUser(User user) {
        if (user == null) {
            throw new WebApplicationException("Missing body", Response.Status.BAD_REQUEST);
        }
        return Uni.createFrom().item(user);
    }

    @Override
    public Uni<User> deleteUser(User user) {
        if (user == null) {
            throw new WebApplicationException("Missing body", Response.Status.BAD_REQUEST);
        }
        return Uni.createFrom().item(user);
    }

}
