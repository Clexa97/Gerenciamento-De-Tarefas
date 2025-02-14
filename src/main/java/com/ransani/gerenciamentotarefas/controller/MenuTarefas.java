package com.ransani.gerenciamentotarefas.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;

public class MenuTarefas {

    @FXML
    private Button cadastroButton;

    @FXML
    public void onCadastroTClick(ActionEvent actionEvent) {
        carregarTela("/com/ransani/gerenciamentotarefas/cadastro-tarefa.fxml");
    }

    @FXML
    public void onBuscarTClick(ActionEvent actionEvent) {
        carregarTela("/com/ransani/gerenciamentotarefas/buscar-tarefa.fxml");
    }

    @FXML
    public void onStatusTClick(ActionEvent actionEvent) {
        carregarTela("/com/ransani/gerenciamentotarefas/editar-tarefa.fxml");
    }

    @FXML
    public void onDeletarTClick(ActionEvent actionEvent) {
        carregarTela("/com/ransani/gerenciamentotarefas/deletar-tarefa.fxml");
    }

    private void carregarTela(String caminhoFXML) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(caminhoFXML));
            Parent root = fxmlLoader.load();
            Stage stage = (Stage) cadastroButton.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            centerStage(stage);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void centerStage(Stage stage) {
        double screenWidth = Screen.getPrimary().getBounds().getWidth();
        double screenHeight = Screen.getPrimary().getBounds().getHeight();
        double windowWidth = stage.getWidth();
        double windowHeight = stage.getHeight();

        stage.setX((screenWidth - windowWidth) / 2);
        stage.setY((screenHeight - windowHeight) / 2);
    }
}
