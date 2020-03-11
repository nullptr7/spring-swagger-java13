module models {

    requires static lombok;
    requires java.persistence;
    requires java.validation;
    requires swagger.annotations;

    exports com.github.nullptr7.models to services, database, spring.beans, webclient;
    opens com.github.nullptr7.models to com.fasterxml.jackson.databind, org.hibernate.orm.core, spring.core, org.hibernate.validator;
}