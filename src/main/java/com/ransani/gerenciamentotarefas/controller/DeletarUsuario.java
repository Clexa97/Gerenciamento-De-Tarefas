package com.ransani.gerenciamentotarefas.controller;

import com.ransani.gerenciamentotarefas.entidade.Tarefa;
import com.ransani.gerenciamentotarefas.entidade.Usuario;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.List;

public class DeletarUsuario {

    @FXML
    private TableView<Usuario> usuarioTable;
    @FXML
    private TableColumn<Usuario, Long> idColumn;
    @FXML
    private TableColumn<Usuario, String> usuarioColumn;
    @FXML
    private TableColumn<Usuario, String> emailColumn;
    @FXML
    private Button deletarButton;
    @FXML
    private Label mensagemLabel;

    private EntityManagerFactory emf;
    private EntityManager em;
    private ObservableList<Usuario> usuarios;

    @FXML
    public void initialize() {
        emf = Persistence.createEntityManagerFactory("gerenciamentoTarefas");
        em = emf.createEntityManager();
        usuarios = FXCollections.observableArrayList();

        carregarUsuarios();
        configurarColunas();
    }

    private void carregarUsuarios() {
        List<Usuario> listaUsuarios = em.createQuery("SELECT u FROM Usuario u", Usuario.class).getResultList();
        usuarios.setAll(listaUsuarios);
        usuarioTable.setItems(usuarios);
    }

    private void configurarColunas() {
        idColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getId()));
        usuarioColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getNome()));
        emailColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getEmail()));
    }

    @FXML
    private void onDeletarClick() {
        Usuario usuarioSelecionado = usuarioTable.getSelectionModel().getSelectedItem();
        if (usuarioSelecionado == null) {
            mensagemLabel.setText("Selecione um usuário para deletar!");
            return;
        }

        List<Tarefa> tarefasVinculadas = em.createQuery("SELECT t FROM Tarefa t WHERE t.responsavel = :usuario", Tarefa.class)
                .setParameter("usuario", usuarioSelecionado)
                .getResultList();

        if (!tarefasVinculadas.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmação de exclusão");
            alert.setHeaderText("O usuário possui tarefas vinculadas!");
            alert.setContentText("Se continuar, todas as tarefas associadas serão deletadas. Deseja prosseguir?");

            ButtonType confirmButton = new ButtonType("Sim", ButtonBar.ButtonData.OK_DONE);
            ButtonType cancelButton = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);
            alert.getButtonTypes().setAll(confirmButton, cancelButton);

            alert.showAndWait().ifPresent(response -> {
                if (response == confirmButton) {
                    deletarUsuarioETarefas(usuarioSelecionado, tarefasVinculadas);
                }
            });
        } else {
            deletarUsuario(usuarioSelecionado);
        }
    }

    private void deletarUsuarioETarefas(Usuario usuario, List<Tarefa> tarefas) {
        em.getTransaction().begin();
        for (Tarefa tarefa : tarefas) {
            em.remove(tarefa);
        }
        em.remove(usuario);
        em.getTransaction().commit();

        mensagemLabel.setText("Usuário e suas tarefas foram deletados com sucesso!");
        carregarUsuarios();
    }

    private void deletarUsuario(Usuario usuario) {
        em.getTransaction().begin();
        em.remove(usuario);
        em.getTransaction().commit();

        mensagemLabel.setText("Usuário deletado com sucesso!");
        carregarUsuarios();
    }
}


