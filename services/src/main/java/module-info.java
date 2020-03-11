module services {

    // User Defined Modules
    requires models;
    requires database;

    // For annotations processing
    requires org.slf4j;
    requires static lombok;

    requires org.apache.commons.lang3;
    requires org.apache.commons.collections4;

    // Spring Related
    requires spring.web;
    requires spring.orm;
    requires spring.boot;
    requires spring.beans;
    requires spring.context;
    requires spring.data.jpa;
    requires spring.boot.autoconfigure;

    requires net.bytebuddy;

    // Swagger Related
    requires springfox.swagger2;
    requires swagger.annotations;
    requires springfox.spring.web;
    requires springfox.swagger.ui;

    // User Combined modules to avoid split-packages
    requires swagger.combined;

    // Mapping related
    requires com.fasterxml.classmate;
    requires com.fasterxml.jackson.databind;


    // Deep Reflection
    opens com.github.nullptr7.initilizer to spring.core;
    opens com.github.nullptr7 to spring.core, spring.beans, spring.context;
    opens com.github.nullptr7.conf to spring.core, spring.beans, spring.context;
    opens com.github.nullptr7.controller to spring.beans, spring.web, java.persistence;

    // Creating beans
    exports com.github.nullptr7.initilizer to spring.beans;
    exports com.github.nullptr7 to webclient;

}