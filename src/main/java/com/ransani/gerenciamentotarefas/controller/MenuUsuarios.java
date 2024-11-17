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


    public void onCadastroUClick(ActionEvent actionEvent) {
        try {
            // Carrega o arquivo FXML do menu
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/ransani/gerenciamentotarefas/cadastro-usuario.fxml"));
            Parent root = fxmlLoader.load();

            // Obtém a janela atual e substitui a cena
            Stage stage = (Stage) cadastroButton.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            centerStage(stage);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onBuscarUClick(ActionEvent actionEvent) {
    }

    public void onEmailClick(ActionEvent actionEvent) {
    }

    public void onDeletarUClick(ActionEvent actionEvent) {
    }

    private static void centerStage(Stage stage) {
        double screenWidth = Screen.getPrimary().getBounds().getWidth();
        double screenHeight = Screen.getPrimary().getBounds().getHeight();

        double windowWidth = stage.getWidth();
        double windowHeight = stage.getHeight();

        double x = (screenWidth - windowWidth) / 2;
        double y = (screenHeight - windowHeight) / 2;

        stage.setX(x);
        stage.setY(y);
    }
}
