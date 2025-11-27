package dev.ifrs.client;

import java.util.List;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import dev.ifrs.model.Book;
import io.quarkus.oidc.token.propagation.common.AccessToken;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@RegisterRestClient(baseUri = "https://localhost:8445/books")
@AccessToken
public interface BookClient {

    @GET
    @Path("/list")
    @Produces(MediaType.APPLICATION_JSON)
    List<Book> listBooks();

}