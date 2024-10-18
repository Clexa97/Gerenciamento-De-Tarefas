module com.ransani.gerenciamentotarefas {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.ransani.gerenciamentotarefas to javafx.fxml;
    exports com.ransani.gerenciamentotarefas;
}