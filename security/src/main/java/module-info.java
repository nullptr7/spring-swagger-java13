module security {

    requires spring.security.core;
    requires spring.security.config;
    requires spring.boot.starter.security;
    requires spring.boot.autoconfigure;
    requires spring.beans;
    requires spring.context;
    requires jjwt;
    requires spring.web;
    requires spring.data.jpa;
    requires org.apache.tomcat.embed.core;

    requires database;
    requires models;
    requires spring.security.web;

    requires java.xml.bind;

    exports com.github.nullptr7.services to services, spring.beans;
    exports com.github.nullptr7.config to services, spring.beans, spring.context;
    exports com.github.nullptr7.filters to spring.beans;

    opens com.github.nullptr7.config to spring.core;
    opens com.github.nullptr7.filters to spring.core;
}