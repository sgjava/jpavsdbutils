/*
 * Copyright (c) Steven P. Goldsmith. All rights reserved.
 *
 * Created by Steven P. Goldsmith on July 19, 2017
 * sgoldsmith@codeferm.com
 */
package com.codeferm.jpavsdbutils;

import java.util.ArrayList;
import junit.framework.TestCase;

import javax.ejb.embeddable.EJBContainer;
import javax.naming.Context;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 * JPA test.
 *
 * @author sgoldsmith
 * @version 1.0.0
 * @since 1.0.0
 */
public class JpaTest extends TestCase {

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
    private static final Logger log = Logger.getLogger(JpaTest.class.
            getName());

    public final String randomString(final String chars, final int length) {
        Random rand = new Random();
        StringBuilder buf = new StringBuilder();
        for (int i = 0; i < length; i++) {
            buf.append(chars.charAt(rand.nextInt(chars.length())));
        }
        return buf.toString();
    }

    public final List<JpaMovie> generateData(final int maxItems) {
        List<JpaMovie> dataList = new ArrayList<>();
        for (int i = 0; i < maxItems; i++) {
            dataList.add(new JpaMovie(randomString(CHARS, 20), randomString(CHARS, 50), 1950 + (int) (Math.random() * 67)));
        }
        return dataList;
    }

    public final void test() throws Exception {
        final Properties p = new Properties();
        p.put("movieDatabase", "new://Resource?type=DataSource");
        p.put("movieDatabase.JdbcDriver", "org.hsqldb.jdbcDriver");
        p.put("movieDatabase.JdbcUrl", "jdbc:hsqldb:mem:moviedb");
        final EJBContainer ejbContainer = EJBContainer.createEJBContainer(p);
        final Context context = ejbContainer.getContext();
        JpaMovies movies = (JpaMovies) context.lookup("java:global/jpavsdbutils/JpaMovies");
        // Add movies
        long startTime = System.nanoTime();
        List<JpaMovie> dataList = generateData(MAX_ITEMS);
        for (JpaMovie movie : dataList) {
            movies.addMovie(movie);
        }
        long difference = System.nanoTime() - startTime;
        log.info(String.format("Add elapsed milliseconds: %s", TimeUnit.NANOSECONDS.toMillis(difference)));
        // Get movies
        startTime = System.nanoTime();
        List<JpaMovie> list = movies.getMovies();
        difference = System.nanoTime() - startTime;
        log.info(String.format("Get elapsed milliseconds: %s", TimeUnit.NANOSECONDS.toMillis(difference)));
        assertEquals("List.size()", MAX_ITEMS, list.size());
        // Delete movies
        startTime = System.nanoTime();
        for (JpaMovie movie : list) {
            movies.deleteMovie(movie);
        }
        difference = System.nanoTime() - startTime;
        log.info(String.format("Delete elapsed milliseconds: %s", TimeUnit.NANOSECONDS.toMillis(difference)));
        assertEquals("Movies.getMovies()", 0, movies.getMovies().size());
        ejbContainer.close();
    }
}
