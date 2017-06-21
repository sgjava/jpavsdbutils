/*
 * Copyright (c) Steven P. Goldsmith. All rights reserved.
 *
 * Created by Steven P. Goldsmith on July 19, 2017
 * sgoldsmith@codeferm.com
 */
package com.codeferm.jpavsdbutils;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.TableGenerator;

/**
 * JPA movie entity.
 *
 * @author sgoldsmith
 * @version 1.0.0
 * @since 1.0.0
 */
@Entity
@NamedQuery(name = "Movie.findAll", query = "SELECT m from JpaMovie as m")
public class JpaMovie {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "JpaMovieSeq")
    @TableGenerator(name = "JpaMovieSeq", allocationSize = 1000)
    private long id;

    private String director;
    private String title;
    private int year;

    public JpaMovie() {
    }

    public JpaMovie(String director, String title, int year) {
        this.director = director;
        this.title = title;
        this.year = year;
    }

    public long getId() {
        return id;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

}
