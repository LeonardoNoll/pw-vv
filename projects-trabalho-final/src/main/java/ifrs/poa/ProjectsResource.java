package ifrs.poa;

import java.net.URI;
import java.util.List;

import ifrs.poa.model.Project;
import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.quarkus.logging.Log;
import io.smallrye.mutiny.Uni;
import jakarta.annotation.security.PermitAll;
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

@Path("/projects")
public class ProjectsResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @WithTransaction
    @RolesAllowed("User")
    public Uni<Response> list() {
        return Project.<Project>findAll().list()
                .map(list -> Response.ok(list).build());
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @WithTransaction
    @RolesAllowed("User")
    public Uni<Response> create(Project project) {
        return project.persistAndFlush()
                .map(v -> {
                    URI location = URI.create("projects/" + project.id);
                    return Response.created(location)
                            .entity(project)
                            .build();
                });
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @WithTransaction
    @RolesAllowed("User")
    public Uni<Response> update(@PathParam("id") Long id, Project payload) {
        return Project.<Project>findById(id)
                .flatMap(existing -> {
                    if (existing == null) {
                        return Uni.createFrom().item(Response.status(Response.Status.NOT_FOUND).build());
                    }

                    existing.setName(payload.getName());
                    existing.setDescription(payload.getDescription());
                    existing.setStatus(payload.getStatus());
                    Log.debug(existing);
                    return Uni.createFrom().item(Response.ok(existing).build());
                });
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @WithTransaction
    @RolesAllowed("User")
    public Uni<Response> delete(@PathParam("id") Long id) {
        return Project.<Project>findById(id)
                .flatMap(existing -> {
                    if (existing == null) {
                        return Uni.createFrom().item(Response.status(Response.Status.NOT_FOUND).build());
                    }
                    return existing.delete()
                            .map(v -> Response.ok().build());
                });
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @WithTransaction
    @RolesAllowed("User")
    public Uni<Response> getProjectById(@PathParam("id") Long id) {
        return Project.<Project>findById(id)
                .flatMap(p -> Uni.createFrom().item(Response.ok(p).build()));
    }
}