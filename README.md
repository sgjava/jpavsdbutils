# JPA vs DbUtils

### Using TomEE 7.0.4-SNAPSHOT
This compares using JPA to DBUtils for adding, selecting and deleting rows of
random data. This gives you an option when you run into Object-relational
impedance mismatch or situations where JPQL may not be robust enough to handle a
legacy RDBMS. This is not an exhaustive performance analysis, but more of a
starting point for discussion. 

Here's the results using 10,000 entities with an HSQL in memory database (this
is the second run so JIT could be factored in):

```
Jun 21, 2017 11:44:22 AM com.codeferm.jpavsdbutils.PerformanceTest performance
INFO: JPA add elapsed milliseconds: 1111
Jun 21, 2017 11:44:22 AM com.codeferm.jpavsdbutils.PerformanceTest performance
INFO: JPA get elapsed milliseconds: 55
Jun 21, 2017 11:44:23 AM com.codeferm.jpavsdbutils.PerformanceTest performance
INFO: JPA delete elapsed milliseconds: 641

```

```
Jun 21, 2017 11:44:23 AM com.codeferm.jpavsdbutils.PerformanceTest performance
INFO: DbUtils add elapsed milliseconds: 375
Jun 21, 2017 11:44:23 AM com.codeferm.jpavsdbutils.PerformanceTest performance
INFO: DbUtils get elapsed milliseconds: 40
Jun 21, 2017 11:44:23 AM com.codeferm.jpavsdbutils.PerformanceTest performance
INFO: DbUtils delete elapsed milliseconds: 306
```
- DBUtils is 296% faster than JPA for inserts
- DBUtils is 37% faster than JPA for selecting list
- DBUtils is 209% faster than JPA for deletes


This [thread](http://tomee-openejb.979440.n4.nabble.com/JPA-vs-DBUtils-td4681918.html)
discusses this project and this [blog](https://virgo47.wordpress.com/2014/10/09/jpa-is-it-worth-it-horror-stories-with-eclipselink-and-hibernate)
discusses issues with JPA. This isn't designed to trash JPA, but rather give you
some options.