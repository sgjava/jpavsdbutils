# JPA vs DbUtils

### Using TomEE 7.0.4-SNAPSHOT
This compares using JPA+Hibernate to DBUtils for adding, selecting and deleting
rows of random data. This give you an option when you run into Object-relational
impedance mismatch or situations where JPQL may not be robust enough to handle a
legacy RDBMS.

Here's the results using 10,000 entities with an HSQL in memory database:

```
Jun 20, 2017 9:39:40 PM com.codeferm.jpavsdbutils.PerformanceTest performance
INFO: JPA add elapsed milliseconds: 4765
Jun 20, 2017 9:39:40 PM com.codeferm.jpavsdbutils.PerformanceTest performance
INFO: JPA get elapsed milliseconds: 323
Jun 20, 2017 9:39:45 PM com.codeferm.jpavsdbutils.PerformanceTest performance
INFO: JPA delete elapsed milliseconds: 5035
```

```
Jun 20, 2017 9:39:47 PM com.codeferm.jpavsdbutils.PerformanceTest performance
INFO: DbUtils add elapsed milliseconds: 1757
Jun 20, 2017 9:39:47 PM com.codeferm.jpavsdbutils.PerformanceTest performance
INFO: DbUtils get elapsed milliseconds: 67
Jun 20, 2017 9:39:48 PM com.codeferm.jpavsdbutils.PerformanceTest performance
INFO: DbUtils delete elapsed milliseconds: 895
```
- DBUtils is 2.71 times faster than JPA+Hibernate for inserts
- DBUtils is 4.82 times faster than JPA+Hibernate for selecting list
- DBUtils is 5.62 times faster than JPA+Hibernate for deletes


This [thread](http://tomee-openejb.979440.n4.nabble.com/JPA-vs-DBUtils-td4681918.html#a4681931)
discussesthis project and this [blog](https://virgo47.wordpress.com/2014/10/09/jpa-is-it-worth-it-horror-stories-with-eclipselink-and-hibernate)
discusses issues with JPA. This isn't designed to trash JPA, but rather give you
some options.