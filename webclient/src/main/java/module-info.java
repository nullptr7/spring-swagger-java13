module webclient {

    requires static lombok;

    // Required to provide explicit since normal run doesn't work
    requires org.slf4j;
    requires reactor.core;
    requires reactor.extra;

    // I really do not know why adding this works and commenting does not. Need to explore
    requires net.bytebuddy;

    // Spring based modules
    requires spring.boot.autoconfigure;
    requires spring.boot;
    requires spring.webflux;
    requires spring.web;

    // For unit testing
    //requires org.junit.jupiter.api;

    // This is required by web client to convert the response object to json
    requires transitive com.fasterxml.classmate;
    requires transitive com.fasterxml.jackson.databind;

    // User defined modules
    requires models;
    requires services;

    opens com.github1.nullptr7 to spring.core, spring.beans, spring.context;
}