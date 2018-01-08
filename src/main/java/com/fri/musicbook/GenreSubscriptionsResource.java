package com.fri.musicbook;

import com.kumuluz.ee.logs.cdi.Log;
import com.kumuluz.ee.logs.cdi.LogParams;
import org.eclipse.microprofile.metrics.annotation.Metered;
import org.eclipse.microprofile.metrics.annotation.Timed;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.List;

@RequestScoped
@Path("/genreSubscription")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
//@Log(LogParams.METRICS)
public class GenreSubscriptionsResource {


    //@Inject
   // private ConfigProperties properties;

    @Context
    protected UriInfo uriInfo;

    @Inject
    private GenreSubscriptionsBean gsb;

    @Metered(name = "getGenreSubscriptions")
    @GET
    @Log(value = LogParams.METRICS)
    public Response getGenreSubscriptions() {
        System.out.println("getVsiGen");
        List<GenreSubscription> subs = gsb.getGenreSubscriptions();

        return Response.ok(subs).build();
    }
/*
    @GET
    @Path("/config")
    public Response test() {
        String response =
                "{" +
                        "\"stringProperty\": \"%s\"," +
                        "\"booleanProperty\": %b," +
                        "\"integerProperty\": %d" +
                        "}";

        response = String.format(
                response,
                properties.getStringProperty(),
                properties.getBooleanProperty(),
                properties.getIntegerProperty()
        );

        return Response.ok(response).build();
    }
*/
    @Metered(name = "getGenreSubscription")
    @GET
    @Path("/{gsId}")
    @Log(value = LogParams.METRICS, methodCall = false)
    public Response getGenre(@PathParam("gsId") String gsId) {
        System.out.println("get1Gen");
        GenreSubscription sub = gsb.getGenreSubscription(gsId);

        if (sub == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.status(Response.Status.OK).entity(sub).build();
    }

    @Timed(name = "getGenreSubscriptionsFiltered_time")
    @Metered(name = "getGenreSubscriptionsFiltered")
    @GET
    @Path("/filtered")
    public Response getGenreSubscriptionsFiltered() {

        List<GenreSubscription> genres;

        genres = gsb.getGenreSubscriptionsFilter(uriInfo);

        return Response.status(Response.Status.OK).entity(genres).build();
    }

    @Metered(name = "createGenreSubscription")
    @POST
    public Response createGenre(GenreSubscription gs) {

        if ((gs.getId_genre() == null || gs.getId_genre().isEmpty() || gs.getId_user() == null || gs.getId_user().isEmpty())) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        } else {
            gs = gsb.createGenre(gs);
        }

        if (gs.getId() != null) {
            return Response.status(Response.Status.CREATED).entity(gs).build();
        } else {
            return Response.status(Response.Status.CONFLICT).entity(gs).build();
        }
    }

    @Metered(name = "putGenreSubscription")
    @PUT
    @Path("{gsId}")
    public Response putGenreSubscription(@PathParam("gsId") String gsId, GenreSubscription gs) {

        gs = gsb.putGenreSubscription(gsId, gs);

        if (gs == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        } else {
            if (gs.getId() != null)
                return Response.status(Response.Status.OK).entity(gs).build();
            else
                return Response.status(Response.Status.NOT_MODIFIED).build();
        }
    }

    @Metered(name = "deleteGenre")
    @DELETE
    @Path("{genreId}")
    public Response deleteGenre(@PathParam("genreId") String genreId) {

        boolean deleted = gsb.deleteGenreSubscription(genreId);

        if (deleted) {
            return Response.status(Response.Status.GONE).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
}
