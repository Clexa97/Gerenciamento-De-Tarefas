module com.ransani.gerenciamentotarefas {
    requires javafx.controls;
    requires javafx.fxml;
    requires static lombok;
    requires jakarta.persistence;


    opens com.ransani.gerenciamentotarefas to javafx.fxml;
    exports com.ransani.gerenciamentotarefas;
}