/*
 * Copyright (c) Steven P. Goldsmith. All rights reserved.
 *
 * Created by Steven P. Goldsmith on July 19, 2017
 * sgoldsmith@codeferm.com
 */
package com.codeferm.jpavsdbutils;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

/**
 * DbUtils movies EJB. Normally you would externalize SQL statements. This is
 * just a simple example, so I'm doing it inline.
 *
 * @author sgoldsmith
 * @version 1.0.0
 * @since 1.0.0
 */
@Transactional
@ApplicationScoped
public class DbUtilsMovies {

    /**
     * The field name "movieDatabase" matches the DataSource we configure in the
     * TestCase via : p.put("movieDatabase", "new://Resource?type=DataSource");
     * <p/>
     * This would also match an equivalent delcaration in an openejb.xml:
     * <Resource id="movieDatabase" type="DataSource"/>
     * <p/>
     * If you'd like the freedom to change the field name without impact on your
     * configuration you can set the "name" attribute of the @Resource
     * annotation to "movieDatabase" instead.
     */
    @Resource
    private DataSource movieDatabase;
    /**
     * QueryRunner is thread safe and can be reused.
     */
    private QueryRunner queryRunner;
    /**
     * Handler for generated key.
     */
    ScalarHandler<Integer> generatedKey;
    /**
     * Bean list handler to map result set to list entities.
     */
    BeanListHandler<DbUtilsMovie> beanListHandler;

    @PostConstruct
    private void construct() throws SQLException {
        queryRunner = new QueryRunner(movieDatabase);
        queryRunner.update("CREATE TABLE movie (id INTEGER IDENTITY PRIMARY KEY, director VARCHAR(255), title VARCHAR(255), year integer)");
        generatedKey = new ScalarHandler();
        beanListHandler = new BeanListHandler(DbUtilsMovie.class);
    }

    public void addMovie(DbUtilsMovie movie) throws SQLException {
        Integer key = queryRunner.insert("INSERT into movie (director, title, year) values (?, ?, ?)", generatedKey, movie.getDirector(), movie.getTitle(), movie.getYear());
        // Set auto generated idenity column
        movie.setId(key);
    }

    public void deleteMovie(DbUtilsMovie movie) throws SQLException {
        queryRunner.update("DELETE from movie where id = ?", movie.getId());
    }

    public List<DbUtilsMovie> getMovies() throws SQLException {
        return queryRunner.query("SELECT id, director, title, year from movie", beanListHandler);
    }

}
