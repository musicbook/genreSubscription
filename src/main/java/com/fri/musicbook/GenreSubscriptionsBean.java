package com.fri.musicbook;

import com.kumuluz.ee.rest.beans.QueryParameters;
import com.kumuluz.ee.rest.utils.JPAUtils;

import javax.enterprise.context.RequestScoped;
//import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.UriInfo;
import java.util.List;

@RequestScoped
public class GenreSubscriptionsBean {

/*    @Inject
    private GenreSubscriptionsBean gsb;
*/
    @PersistenceContext(unitName = "genresubscriptions-jpa")
    private EntityManager em;

    public List<GenreSubscription> getGenreSubscriptions(){
        Query query = em.createNamedQuery("GenreSubscription.getAll", GenreSubscription.class);
        return query.getResultList();
    }

    public GenreSubscription getGenreSubscription(String gsId) {
        GenreSubscription gs = em.find(GenreSubscription.class, gsId);
        if (gs == null) {
            throw new NotFoundException();
        }
        return gs;
    }

    public List<GenreSubscription> getGenreSubscriptionsFilter(UriInfo uriInfo) {
        QueryParameters queryParameters = QueryParameters.query(uriInfo.getRequestUri().getQuery()).defaultOffset(0)
                .build();
        List<GenreSubscription> gs = JPAUtils.queryEntities(em, GenreSubscription.class, queryParameters);
        return gs;
    }

    public GenreSubscription createGenre(GenreSubscription gs) {
        try {
            beginTx();
            em.persist(gs);
            commitTx();
        } catch (Exception e) {
            rollbackTx();
        }
        return gs;
    }

    public GenreSubscription putGenreSubscription(String gsId, GenreSubscription genre) {

        GenreSubscription c = em.find(GenreSubscription.class, gsId);

        if (c == null) {
            return null;
        }

        try {
            beginTx();
            genre.setId(c.getId());
            genre = em.merge(genre);
            commitTx();
        } catch (Exception e) {
            rollbackTx();
        }

        return genre;
    }

    public boolean deleteGenreSubscription(String gsId) {

        GenreSubscription gs = em.find(GenreSubscription.class, gsId);

        if (gs != null) {
            try {
                beginTx();
                em.remove(gs);
                commitTx();
            } catch (Exception e) {
                rollbackTx();
            }
        } else
            return false;

        return true;
    }

    private void beginTx() {
        if (!em.getTransaction().isActive())
            em.getTransaction().begin();
    }

    private void commitTx() {
        if (em.getTransaction().isActive())
            em.getTransaction().commit();
    }

    private void rollbackTx() {
        if (em.getTransaction().isActive())
            em.getTransaction().rollback();
    }
}
