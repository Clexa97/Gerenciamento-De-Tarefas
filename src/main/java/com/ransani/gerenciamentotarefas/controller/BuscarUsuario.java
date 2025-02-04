package com.ransani.gerenciamentotarefas.controller;

import com.ransani.gerenciamentotarefas.entidade.Usuario;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.beans.property.ReadOnlyObjectWrapper;

import java.util.List;

public class BuscarUsuario {


    @FXML
    private TextField idLabel;

    @FXML
    private Label mensagemLabel;
    @FXML
    private TableView<Usuario> tableView;
    @FXML
    private TableColumn<Usuario, Long> lineId;
    @FXML
    private TableColumn<Usuario, String> lineUsuario;
    @FXML
    private TableColumn<Usuario, String> lineEmail;

    private EntityManagerFactory emf;
    private EntityManager em;

    @FXML
    public void initialize() {
        emf = Persistence.createEntityManagerFactory("gerenciamentoTarefas");
        em = emf.createEntityManager();

        lineId.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getId()));
        lineUsuario.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getNome()));
        lineEmail.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getEmail()));
    }

    @FXML
    private void onBuscarClick() {
        limparMensagem();
        String idTexto = idLabel.getText().trim();

        if (idTexto.isEmpty()) {
            mensagemLabel.setText("O campo ID não pode estar vazio!");
            return;
        }

        try {
            long id = Long.parseLong(idTexto);
            Usuario usuario = em.find(Usuario.class, id);

            if (usuario != null) {
                tableView.setItems(FXCollections.observableArrayList(usuario));
            } else {
                mensagemLabel.setText("Usuário não encontrado!");
                tableView.setItems(FXCollections.observableArrayList());
            }
        } catch (NumberFormatException e) {
            mensagemLabel.setText("ID inválido! Insira um número válido.");
        } catch (Exception e) {
            mensagemLabel.setText("Erro ao buscar usuário.");
            e.printStackTrace();
        }
    }

    @FXML
    private void onBuscarTClick() {
        limparMensagem();
        try {
            TypedQuery<Usuario> query = em.createQuery("SELECT u FROM Usuario u", Usuario.class);
            List<Usuario> usuarios = query.getResultList();
            tableView.setItems(FXCollections.observableArrayList(usuarios));
        } catch (Exception e) {
            mensagemLabel.setText("Erro ao buscar todos os usuários.");
            e.printStackTrace();
        }
    }

    private void limparMensagem() {
        mensagemLabel.setText("");
    }

    @FXML
    public void close() {
        if (em != null && em.isOpen()) {
            em.close();
        }
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }
}
