package dev.ifrs.client;

import org.eclipse.microprofile.rest.client.inject.RestClient;
import jakarta.enterprise.inject.Alternative;
import jakarta.annotation.Priority;

import dev.ifrs.model.Project;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.PathParam;

@ApplicationScoped
@RestClient
@Alternative
@Priority(1)
public class MockProjectClient implements ProjectClient {

    @Override
    public Uni<Response> list() {
        return Uni.createFrom().item(Response.ok().build());
    }

    @Override
    public Uni<Response> create(Project project) {
        if (project == null) {
            throw new WebApplicationException("Missing body", Response.Status.BAD_REQUEST);
        }
        return Uni.createFrom().item(Response.status(Response.Status.CREATED).entity(project).build());
    }

    @Override
    public Uni<Response> update(@PathParam("id") Long id, Project payload) {
        if (payload == null) {
            throw new WebApplicationException("Missing body", Response.Status.BAD_REQUEST);
        }
        return Uni.createFrom().item(Response.ok().entity(payload).build());
    }

    @Override
    public Uni<Response> delete(@PathParam("id") Long id) {
        return Uni.createFrom().item(Response.noContent().build());
    }

    @Override
    public Uni<Response> getProjectById(@PathParam("id") Long id) {
        return Uni.createFrom().item(Response.ok().build());
    }

}
