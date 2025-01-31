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

public class MenuUsuarios {

    @FXML
    private Button cadastroButton;
    @FXML
    private Button buscarButton;
    @FXML
    private Button emailButton;
    @FXML
    private Button deletarButton;

    @FXML
    public void onCadastroUClick(ActionEvent actionEvent) {
        carregarTela("/com/ransani/gerenciamentotarefas/cadastro-usuario.fxml");
    }

    @FXML
    public void onBuscarUClick(ActionEvent actionEvent) {
        carregarTela("/com/ransani/gerenciamentotarefas/buscar-usuario.fxml");
    }

    @FXML
    public void onEmailClick(ActionEvent actionEvent) {
        carregarTela("/com/ransani/gerenciamentotarefas/editar-usuario.fxml");
    }

    @FXML
    public void onDeletarUClick(ActionEvent actionEvent) {
        carregarTela("/com/ransani/gerenciamentotarefas/deletar-usuario.fxml");
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
        Screen screen = Screen.getPrimary();
        double screenWidth = screen.getBounds().getWidth();
        double screenHeight = screen.getBounds().getHeight();
        double windowWidth = stage.getWidth();
        double windowHeight = stage.getHeight();

        stage.setX((screenWidth - windowWidth) / 2);
        stage.setY((screenHeight - windowHeight) / 2);
    }
}


