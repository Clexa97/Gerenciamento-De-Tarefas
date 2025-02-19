package com.ransani.gerenciamentotarefas.controller;
import com.ransani.gerenciamentotarefas.entidade.Tarefa;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import jakarta.persistence.*;
import java.util.List;
public class BuscarTarefa {

    @FXML
    private TableView<Tarefa> tarefaTable;
    @FXML
    private TableColumn<Tarefa, Long> idColum;
    @FXML
    private TableColumn<Tarefa, String> descricaoColum;
    @FXML
    private TableColumn<Tarefa, String> responsavelColum;
    @FXML
    private TableColumn<Tarefa, String> statusColum;
    @FXML
    private TableColumn<Tarefa, String> cadastroColum;
    @FXML
    private TableColumn<Tarefa, String> prasoColum;

    @FXML
    private SplitMenuButton menuSplit;
    @FXML
    private TextField idTextField;
    @FXML
    private Button buscarButton;
    @FXML
    private Button buscarTodosButton;
    @FXML
    private Label mensagemLabel;

    private EntityManagerFactory emf;
    private EntityManager em;
    private ObservableList<Tarefa> tarefas;

    @FXML
    public void initialize() {
        emf = Persistence.createEntityManagerFactory("gerenciamentoTarefas");
        em = emf.createEntityManager();
        tarefas = FXCollections.observableArrayList();

        configurarColunas();
        configurarMenuStatus();
        tarefaTable.setItems(FXCollections.observableArrayList());
    }

    private void configurarColunas() {
        idColum.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getId()));
        descricaoColum.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getDescricao()));
        responsavelColum.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getResponsavel().getNome()));
        statusColum.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getStatus().name()));
        cadastroColum.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getCadastro().toString()));
        prasoColum.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getPraso().toString()));
    }

    private void configurarMenuStatus() {
        menuSplit.getItems().clear();

        for (Tarefa.Status status : Tarefa.Status.values()) {
            MenuItem item = new MenuItem(status.name());
            item.setOnAction(event -> menuSplit.setText(status.name()));
            menuSplit.getItems().add(item);
        }
    }

    @FXML
    private void onBuscarTodosClick() {
        carregarTodasTarefas();
        mensagemLabel.setText("Todas as tarefas foram carregadas.");
    }

    private void carregarTodasTarefas() {
        List<Tarefa> listaTarefas = em.createQuery("SELECT t FROM Tarefa t", Tarefa.class).getResultList();
        tarefas.setAll(listaTarefas);
        tarefaTable.setItems(tarefas);
    }

    @FXML
    private void onBuscarClick() {
        String idTexto = idTextField.getText().trim();
        String statusSelecionado = menuSplit.getText();

        if (idTexto.isEmpty() && statusSelecionado.equals("Status")) {
            mensagemLabel.setText("Informe um ID ou selecione um status para buscar.");
            return;
        }

        try {
            Long id = idTexto.isEmpty() ? null : Long.parseLong(idTexto);
            Tarefa.Status status = statusSelecionado.equals("Status") ? null : Tarefa.Status.valueOf(statusSelecionado);

            List<Tarefa> listaTarefas;
            if (id != null && status != null) {
                listaTarefas = em.createQuery("SELECT t FROM Tarefa t WHERE t.id = :id AND t.status = :status", Tarefa.class)
                        .setParameter("id", id)
                        .setParameter("status", status)
                        .getResultList();
            } else if (id != null) {
                listaTarefas = em.createQuery("SELECT t FROM Tarefa t WHERE t.id = :id", Tarefa.class)
                        .setParameter("id", id)
                        .getResultList();
            } else {
                listaTarefas = em.createQuery("SELECT t FROM Tarefa t WHERE t.status = :status", Tarefa.class)
                        .setParameter("status", status)
                        .getResultList();
            }

            if (listaTarefas.isEmpty()) {
                mensagemLabel.setText("Nenhuma tarefa encontrada.");
                tarefaTable.setItems(FXCollections.observableArrayList());
            } else {
                tarefas.setAll(listaTarefas);
                tarefaTable.setItems(tarefas);
                mensagemLabel.setText("Busca concluída.");
            }
        } catch (NumberFormatException e) {
            mensagemLabel.setText("O ID deve ser um número válido.");
        } catch (Exception e) {
            mensagemLabel.setText("Erro ao buscar tarefas.");
            e.printStackTrace();
        }
    }
}