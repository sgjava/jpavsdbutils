/*
 * Copyright (c) Steven P. Goldsmith. All rights reserved.
 *
 * Created by Steven P. Goldsmith on July 19, 2017
 * sgoldsmith@codeferm.com
 */
package com.codeferm.jpavsdbutils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.Query;
import java.util.List;
import javax.ejb.Singleton;

/**
 * JPA movies EJB.
 *
 * @author sgoldsmith
 * @version 1.0.0
 * @since 1.0.0
 */
@Singleton
public class JpaMovies {

    @PersistenceContext(unitName = "movie-unit", type = PersistenceContextType.TRANSACTION)
    private EntityManager entityManager;

    public void addMovie(JpaMovie movie) throws Exception {
        entityManager.persist(movie);
    }

    public void deleteMovie(JpaMovie movie) throws Exception {
        entityManager.remove(entityManager.contains(movie) ? movie : entityManager.merge(movie));
    }

    public List<JpaMovie> getMovies() throws Exception {
        Query query = entityManager.createQuery("SELECT m from JpaMovie as m");
        return query.getResultList();
    }

}
