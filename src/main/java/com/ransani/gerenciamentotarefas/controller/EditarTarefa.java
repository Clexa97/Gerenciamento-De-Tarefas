package com.ransani.gerenciamentotarefas.controller;
import com.ransani.gerenciamentotarefas.entidade.Tarefa;
import com.ransani.gerenciamentotarefas.entidade.Usuario;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

public class EditarTarefa {

    @FXML
    private TableView<Tarefa> tarefaTable;
    @FXML
    private TableColumn<Tarefa, Long> idColum;
    @FXML
    private TableColumn<Tarefa, Date> cadastroColum;
    @FXML
    private TableColumn<Tarefa, String> descricaoColum;
    @FXML
    private TableColumn<Tarefa, Date> prasoColum;
    @FXML
    private TableColumn<Tarefa, Tarefa.Status> statusColum;
    @FXML
    private TableColumn<Tarefa, Usuario> responsavelColum;
    @FXML
    private SplitMenuButton menuSplit;
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
        configurarMenuSplit();
    }

    private void carregarTarefas() {
        em.getTransaction().begin();
        List<Tarefa> listaTarefas = em.createQuery("SELECT t FROM Tarefa t", Tarefa.class).getResultList();
        LocalDate hoje = LocalDate.now();

        for (Tarefa tarefa : listaTarefas) {
            LocalDate prazoTarefa = tarefa.getPraso().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            if (prazoTarefa.isBefore(hoje) && tarefa.getStatus() != Tarefa.Status.CONCLUIDA) {
                tarefa.setStatus(Tarefa.Status.ATRASADA);
                em.merge(tarefa);
            }
        }

        em.getTransaction().commit();
        tarefas.setAll(listaTarefas);
        tarefaTable.setItems(tarefas);
    }

    private void configurarColunas() {
        idColum.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getId()));
        cadastroColum.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getCadastro()));
        descricaoColum.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getDescricao()));
        prasoColum.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getPraso()));
        statusColum.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getStatus()));
        responsavelColum.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getResponsavel()));
    }

    private void configurarMenuSplit() {
        menuSplit.getItems().clear(); // Limpa antes para evitar duplicação

        for (Tarefa.Status status : Tarefa.Status.values()) {
            MenuItem item = new MenuItem(status.name());
            item.setOnAction(e -> menuSplit.setText(status.name()));
            menuSplit.getItems().add(item);
        }
    }

    @FXML
    private void onSalvarClick() {
        Tarefa tarefaSelecionada = tarefaTable.getSelectionModel().getSelectedItem();
        if (tarefaSelecionada == null) {
            mensagemLabel.setText("Selecione uma tarefa antes de salvar!");
            return;
        }

        if (menuSplit.getText().equals("Status")) {
            mensagemLabel.setText("Selecione um status antes de salvar!");
            return;
        }

        Tarefa.Status novoStatus = Tarefa.Status.valueOf(menuSplit.getText());
        LocalDate prazoTarefa = tarefaSelecionada.getPraso().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate hoje = LocalDate.now();

        if (tarefaSelecionada.getStatus() == Tarefa.Status.ATRASADA &&
                (novoStatus == Tarefa.Status.PENDENTE || novoStatus == Tarefa.Status.EM_ANDAMENTO) &&
                prazoTarefa.isBefore(hoje)) {
            mensagemLabel.setText("Uma tarefa atrasada só pode ser alterada para CONCLUIDA!");
            return;
        }

        em.getTransaction().begin();
        tarefaSelecionada.setStatus(novoStatus);
        em.merge(tarefaSelecionada);
        em.getTransaction().commit();

        mensagemLabel.setText("Tarefa atualizada com sucesso!");
        tarefaTable.refresh();
    }
}

