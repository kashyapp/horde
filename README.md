horde
=====

Sample app with Jetty

Running
-------
Redis needs to be running on port `28300`

    $ mvn compile exec:java
    
Adjust the jetty `poolsize` by modifying the `systemProperties` configuration in `pom.xml`
