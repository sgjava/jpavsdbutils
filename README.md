# JPA vs DbUtils

### Using TomEE 7.0.4-SNAPSHOT
This compares using JPA+Hibernate to DBUtils for adding, selecting and deleting
rows of random data. I always knew DBUtils was faster than JPA, but I wanted to
have an example, so I knew exactly how much faster DBUtils was. I borrowed the
code from the TomEE examples for the JPA+Hibernate and built the DBUtils
equivalent. EntityManager does generate fewer lines of code, but the DBUtils
code was not that much larger or complicated.

Here's the results using 10,000 entities with an HSQL in memory database:

```
Jun 19, 2017 10:41:02 PM com.codeferm.jpavsdbutils.DbUtilsTest test
INFO: Add elapsed milliseconds: 2712
Jun 19, 2017 10:41:02 PM com.codeferm.jpavsdbutils.DbUtilsTest test
INFO: Get elapsed milliseconds: 124
Jun 19, 2017 10:41:04 PM com.codeferm.jpavsdbutils.DbUtilsTest test
INFO: Delete elapsed milliseconds: 1655
```

```
Jun 19, 2017 10:41:31 PM com.codeferm.jpavsdbutils.JpaTest test
INFO: Add elapsed milliseconds: 26733
Jun 19, 2017 10:41:31 PM com.codeferm.jpavsdbutils.JpaTest test
INFO: Get elapsed milliseconds: 183
Jun 19, 2017 10:41:56 PM com.codeferm.jpavsdbutils.JpaTest test
INFO: Delete elapsed milliseconds: 25022
```
- DBUtils is 9.86 times faster than JPA+Hibernate for inserts
- DBUtils is 1.47 times faster than JPA+Hibernate for selecting list
- DBUtils is 15.11 times faster than JPA+Hibernate for deletes

This isn't just a rounding error it's a substantial difference! There may be
some tweaks to get JPA to perform better, but when you are looking at Internet
scale application this can be a big problem. I'll profile the tests to see
exactly where JPA is slowing down since it doesn't appear to be CPU bound.