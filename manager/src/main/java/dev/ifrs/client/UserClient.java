package dev.ifrs.client;

import java.util.List;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import dev.ifrs.model.User;
import io.quarkus.oidc.token.propagation.common.AccessToken;
import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@RegisterRestClient(baseUri = "https://localhost:8443/users")
// @AccessToken
public interface UserClient {
    @POST
    @Path("/jwt")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public String getToken(User user);

    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_PLAIN)
    public Uni<String> loginUser(@FormParam("email") String email,
            @FormParam("password") String password);

    @POST
    @Path("/register")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<User> registerUser(@FormParam("name") String name,
            @FormParam("email") String email,
            @FormParam("password") String password,
            @FormParam("confirmPassword") String confirmPassword);

    @GET
    @Path("/list")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<List<User>> listUser();

    @POST
    @Path("/update")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<User> updateUser(User user);

    @POST
    @Path("/delete")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<User> deleteUser(User user);
}
