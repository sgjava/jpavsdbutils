/*
 * Copyright (c) Steven P. Goldsmith. All rights reserved.
 *
 * Created by Steven P. Goldsmith on July 19, 2017
 * sgoldsmith@codeferm.com
 */
package com.codeferm.jpavsdbutils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;

/**
 * JPA movies EJB.
 *
 * @author sgoldsmith
 * @version 1.0.0
 * @since 1.0.0
 */
@Transactional
@ApplicationScoped
public class JpaMovies {

    @PersistenceContext(unitName = "movie-unit")
    private EntityManager entityManager;

    public void addMovie(JpaMovie movie) {
        entityManager.persist(movie);
    }

    public void deleteMovie(JpaMovie movie) {
        entityManager.remove(entityManager.getReference(JpaMovie.class, movie.getId()));
    }

    public List<JpaMovie> getMovies() {
        Query query = entityManager.createNamedQuery("Movie.findAll");
        return query.getResultList();
    }

}
