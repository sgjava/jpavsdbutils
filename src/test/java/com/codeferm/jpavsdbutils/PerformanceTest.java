/*
 * Copyright (c) Steven P. Goldsmith. All rights reserved.
 *
 * Created by Steven P. Goldsmith on July 19, 2017
 * sgoldsmith@codeferm.com
 */
package com.codeferm.jpavsdbutils;

import java.sql.SQLException;
import java.util.ArrayList;

import javax.ejb.embeddable.EJBContainer;
import javax.naming.Context;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import javax.inject.Inject;
import javax.naming.NamingException;
import static junit.framework.TestCase.assertEquals;
import org.junit.Test;

/**
 * JPA test.
 *
 * @author sgoldsmith
 * @version 1.0.0
 * @since 1.0.0
 */
public class PerformanceTest {

    /**
     * Maximum entities.
     */
    private static final int MAX_ITEMS = 10000;
    /**
     * Used for random string generation.
     */
    private static final String CHARS = "abcdefghijklmnopqrstuvwxyz";
    /**
     * Logger.
     */
    private static final Logger log = Logger.getLogger(Test.class.
            getName());

    @Inject
    private DbUtilsMovies dbUtilsMovies;
    @Inject
    private JpaMovies jpaMovies;

    public final String randomString(final String chars, final int length) {
        final Random rand = new Random();
        final StringBuilder buf = new StringBuilder();
        for (int i = 0; i < length; i++) {
            buf.append(chars.charAt(rand.nextInt(chars.length())));
        }
        return buf.toString();
    }

    public final List<JpaMovie> generateData(final int maxItems) {
        final List<JpaMovie> dataList = new ArrayList<>();
        for (int i = 0; i < maxItems; i++) {
            dataList.add(new JpaMovie(randomString(CHARS, 20), randomString(CHARS, 50), 1950 + (int) (Math.random() * 67)));
        }
        return dataList;
    }

    public final void performance() throws NamingException, SQLException {
        // Add movies
        long startTime = System.nanoTime();
        final List<JpaMovie> jpaDataList = generateData(MAX_ITEMS);
        jpaDataList.forEach((movie) -> {
            jpaMovies.addMovie(movie);
        });
        long difference = System.nanoTime() - startTime;
        log.info(String.format("JPA add elapsed milliseconds: %s", TimeUnit.NANOSECONDS.toMillis(difference)));
        // Get movies
        startTime = System.nanoTime();
        final List<JpaMovie> jpaList = jpaMovies.getMovies();
        difference = System.nanoTime() - startTime;
        log.info(String.format("JPA get elapsed milliseconds: %s", TimeUnit.NANOSECONDS.toMillis(difference)));
        assertEquals("List.size()", MAX_ITEMS, jpaList.size());
        // Delete movies
        startTime = System.nanoTime();
        jpaList.forEach((movie) -> {
            jpaMovies.deleteMovie(movie);
        });
        difference = System.nanoTime() - startTime;
        log.info(String.format("JPA delete elapsed milliseconds: %s", TimeUnit.NANOSECONDS.toMillis(difference)));
        assertEquals("Movies.getMovies()", 0, jpaMovies.getMovies().size());
        // Run DBUtils tests with same data as JPA
        final List<DbUtilsMovie> dbUtilsDataList = new ArrayList<>();
        jpaDataList.forEach((movie) -> {
            dbUtilsDataList.add(new DbUtilsMovie(movie.getDirector(), movie.getTitle(), movie.getYear()));
        });
        // Add movies
        startTime = System.nanoTime();
        for (DbUtilsMovie movie : dbUtilsDataList) {
            dbUtilsMovies.addMovie(movie);
        }
        difference = System.nanoTime() - startTime;
        log.info(String.format("DbUtils add elapsed milliseconds: %s", TimeUnit.NANOSECONDS.toMillis(difference)));
        // Get movies
        startTime = System.nanoTime();
        final List<DbUtilsMovie> dbUtilslist = dbUtilsMovies.getMovies();
        difference = System.nanoTime() - startTime;
        log.info(String.format("DbUtils get elapsed milliseconds: %s", TimeUnit.NANOSECONDS.toMillis(difference)));
        assertEquals("List.size()", MAX_ITEMS, dbUtilslist.size());
        // Delete movies
        startTime = System.nanoTime();
        for (DbUtilsMovie movie : dbUtilslist) {
            dbUtilsMovies.deleteMovie(movie);
        }
        difference = System.nanoTime() - startTime;
        log.info(String.format("DbUtils delete elapsed milliseconds: %s", TimeUnit.NANOSECONDS.toMillis(difference)));
        assertEquals("Movies.getMovies()", 0, dbUtilsMovies.getMovies().size());
    }
    
    @Test
    public final void performanceTest() throws Exception {
        final Properties p = new Properties();
        p.put("movieDatabase", "new://Resource?type=DataSource");
        p.put("movieDatabase.JdbcDriver", "org.hsqldb.jdbcDriver");
        p.put("movieDatabase.JdbcUrl", "jdbc:hsqldb:mem:moviedb");
        try (EJBContainer ejbContainer = EJBContainer.createEJBContainer(p)) {
            final Context context = ejbContainer.getContext();
            context.bind("inject", this);
            performance();
            performance();
        }
    }
}
