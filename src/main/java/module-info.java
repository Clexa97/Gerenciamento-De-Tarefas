module com.ransani.gerenciamentotarefas {
    requires javafx.controls;
    requires javafx.fxml;
    requires static lombok;
    requires org.hibernate.orm.core;
    requires jakarta.persistence;


    opens com.ransani.gerenciamentotarefas to javafx.fxml;
    exports com.ransani.gerenciamentotarefas;
    exports com.ransani.gerenciamentotarefas.controller;
    opens com.ransani.gerenciamentotarefas.entidade to org.hibernate.orm.core, javafx.base;
    opens com.ransani.gerenciamentotarefas.controller to javafx.fxml;
}