package com.ransani.gerenciamentotarefas.controller;

import com.ransani.gerenciamentotarefas.strategy.MenuLoadStrategy;
import com.ransani.gerenciamentotarefas.strategy.TarefaMenuStrategy;
import com.ransani.gerenciamentotarefas.strategy.UsuarioMenuStrategy;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.scene.control.Button;

import javafx.stage.Stage;

import java.io.IOException;

public class MenuController {
    @FXML
    private Button tarefatButton;

    @FXML
    private Button usuariotButton;

    private MenuLoadStrategy strategy;

    @FXML
    public void onUsuarioClick(ActionEvent actionEvent) {
        strategy = new UsuarioMenuStrategy();
        applyStrategy();
    }

    @FXML
    public void onTarefaClick(ActionEvent actionEvent) {
        strategy = new TarefaMenuStrategy();
        applyStrategy();
    }

    private void applyStrategy() {
        Stage stage = (Stage) tarefatButton.getScene().getWindow();
        strategy.loadMenu(stage);
    }
}