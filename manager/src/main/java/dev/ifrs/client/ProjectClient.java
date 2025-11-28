package dev.ifrs.client;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import dev.ifrs.model.Project;
import io.quarkus.oidc.token.propagation.common.AccessToken;
import io.smallrye.mutiny.Uni;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@RegisterRestClient(baseUri = "https://localhost:8446/projects")
@AccessToken
public interface ProjectClient {
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("User")
    public Uni<Response> list();

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("User")
    public Uni<Response> create(Project project);

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("User")
    public Uni<Response> update(@PathParam("id") Long id, Project payload);

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("User")
    public Uni<Response> delete(@PathParam("id") Long id);

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("User")
    public Uni<Response> getProjectById(@PathParam("id") Long id);
}
