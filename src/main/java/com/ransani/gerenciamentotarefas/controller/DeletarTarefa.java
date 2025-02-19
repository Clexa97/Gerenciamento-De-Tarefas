package com.ransani.gerenciamentotarefas.controller;
import com.ransani.gerenciamentotarefas.entidade.Tarefa;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import jakarta.persistence.*;

import java.util.List;

public class DeletarTarefa {
    @FXML
    private TableView<Tarefa> tarefaTable;
    @FXML
    private TableColumn<Tarefa, Long> idColum;
    @FXML
    private TableColumn<Tarefa, String> descricaoColum;
    @FXML
    private TableColumn<Tarefa, String> prasoColum;
    @FXML
    private TableColumn<Tarefa, String> statusColum;
    @FXML
    private TableColumn<Tarefa, String> responsavelColum;
    @FXML
    private Label mensagemLabel;
    @FXML
    private Button salvarButton;

    private EntityManagerFactory emf;
    private EntityManager em;
    private ObservableList<Tarefa> tarefas;

    @FXML
    public void initialize() {
        emf = Persistence.createEntityManagerFactory("gerenciamentoTarefas");
        em = emf.createEntityManager();
        tarefas = FXCollections.observableArrayList();

        carregarTarefas();
        configurarColunas();
    }

    private void carregarTarefas() {
        List<Tarefa> listaTarefas = em.createQuery("SELECT t FROM Tarefa t", Tarefa.class).getResultList();
        tarefas.setAll(listaTarefas);
        tarefaTable.setItems(tarefas);
    }

    private void configurarColunas() {
        idColum.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getId()));
        descricaoColum.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getDescricao()));
        prasoColum.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getPraso().toString()));
        statusColum.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getStatus().name()));
        responsavelColum.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getResponsavel().getNome()));
    }

    @FXML
    private void onSalvarClick() {
        Tarefa tarefaSelecionada = tarefaTable.getSelectionModel().getSelectedItem();

        if (tarefaSelecionada == null) {
            mensagemLabel.setText("Selecione uma tarefa antes de confirmar a exclusão!");
            return;
        }

        Alert confirmacao = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacao.setTitle("Confirmação de Exclusão");
        confirmacao.setHeaderText("Tem certeza que deseja excluir esta tarefa?");
        confirmacao.setContentText("ID: " + tarefaSelecionada.getId() + "\nDescrição: " + tarefaSelecionada.getDescricao());

        confirmacao.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                excluirTarefa(tarefaSelecionada);
            }
        });
    }

    private void excluirTarefa(Tarefa tarefa) {
        try {
            em.getTransaction().begin();
            Tarefa tarefaParaRemover = em.find(Tarefa.class, tarefa.getId());
            if (tarefaParaRemover != null) {
                em.remove(tarefaParaRemover);
            }
            em.getTransaction().commit();

            tarefas.remove(tarefa);
            tarefaTable.refresh();
            mensagemLabel.setText("Tarefa excluída com sucesso!");
        } catch (Exception e) {
            mensagemLabel.setText("Erro ao excluir a tarefa!");
            e.printStackTrace();
        }
    }
}