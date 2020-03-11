module database {
    requires models;

    requires spring.data.commons;
    requires spring.context;
    requires spring.data.jpa;

    exports com.github.nullptr7.repo to services;
}