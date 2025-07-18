module org.example.vehiclelocation {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.base;
    requires java.sql;
    //requires java.persistence;

    requires jakarta.persistence;
    requires org.hibernate.orm.core;
    requires org.slf4j;
    requires ch.qos.logback.classic;
    requires jbcrypt;

    opens org.location.controllers to javafx.fxml;
    opens org.location.models to org.hibernate.orm.core, javafx.base;

    exports org.location;
    exports org.location.models;
    exports org.location.controllers;
}
