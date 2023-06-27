# jdbc-template-api

Spring Boot Rest API using JDBC template, connection pool, database migration Flyway. We have a many-to-many relationship between two tables actor and movie.

We have the ability to create, view, update or delete actors and movies on various endpoints. At the output of information about the actor, we additionally have a list of films in which he starred. Conversely, when we preview the movie information, we can see the cast list.

In addition to the main data source, we can connect any other through yml.

We change the database by the method of migrations, creating a new version of the file with changes.

Services and controllers are covered by 59 tests.
