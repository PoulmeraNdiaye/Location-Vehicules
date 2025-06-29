module org.example.vehiclelocation {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.base;
    requires org.hibernate.orm.core;
    requires jakarta.persistence;
    requires java.sql;
    requires org.slf4j;
    requires ch.qos.logback.classic;
    requires java.persistence;
    requires jbcrypt;

    opens org.mnjaay.controllers to javafx.fxml;
    opens org.mnjaay.models to org.hibernate.orm.core;
    exports org.mnjaay;
}
