package dev.ifrs;

import java.util.List;

import org.eclipse.microprofile.metrics.annotation.Counted;

import dev.ifrs.data.DataBase;
import dev.ifrs.model.Book;
import io.quarkus.logging.Log;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/books")
public class BookResource {

    @Inject
    private DataBase dataBase;

    @GET
    @Path("/list")
    @PermitAll
    @Counted(displayName = "getBooks")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Book> getBooks() {
        Log.info("dataBase");
        return dataBase.getBooks();
    }

    @GET
    @Path("/test")
    @RolesAllowed("User")
    @Counted(displayName = "getBooks")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Book> getTestBooks() {
        Log.info("Fetching test book list for user");
        return dataBase.getBooks();
    }
}
