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


import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

public class CadastroTarefa {

    @FXML
    private TextField tituloField;
    @FXML
    private TextArea descricaoField;
    @FXML
    private DatePicker prazoField;
    @FXML
    private DatePicker cadastroField;
    @FXML
    private SplitMenuButton statusComboBox;
    @FXML
    private TableView<Usuario> tableUsuario;
    @FXML
    private TableColumn<Usuario, Long> idColum;
    @FXML
    private TableColumn<Usuario, String> usuarioColum;
    @FXML
    private Label retorno;

    private EntityManagerFactory emf;
    private EntityManager em;

    @FXML
    public void initialize() {
        emf = Persistence.createEntityManagerFactory("gerenciamentoTarefas");
        em = emf.createEntityManager();

        statusComboBox.getItems().clear();

        for (Tarefa.Status status : Tarefa.Status.values()) {
            MenuItem item = new MenuItem(status.name());
            item.setOnAction(e -> statusComboBox.setText(status.name()));
            statusComboBox.getItems().add(item);
        }

        idColum.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getId()));
        usuarioColum.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getNome()));

        carregarUsuarios();
    }

    private void carregarUsuarios() {
        List<Usuario> usuarios = em.createQuery("SELECT u FROM Usuario u", Usuario.class).getResultList();
        ObservableList<Usuario> usuariosObservable = FXCollections.observableArrayList(usuarios);
        tableUsuario.setItems(usuariosObservable);
    }

    @FXML
    private void onSalvarClick() {
        String titulo = tituloField.getText().trim();
        String descricao = descricaoField.getText().trim();
        LocalDate prazo = prazoField.getValue();
        LocalDate cadastro = cadastroField.getValue();
        Tarefa.Status status = Tarefa.Status.valueOf(statusComboBox.getText());
        Usuario responsavel = tableUsuario.getSelectionModel().getSelectedItem();

        if (titulo.isEmpty() || descricao.isEmpty() || prazo == null || cadastro == null || status == null || responsavel == null) {
            retorno.setText("Todos os campos devem ser preenchidos!");
            return;
        }
        if (prazo.isBefore(cadastro)) {
            retorno.setText("A data de prazo não pode ser anterior à data de cadastro!");
            return;
        }

        try {
            Tarefa tarefa = new Tarefa();
            tarefa.setTitulo(titulo);
            tarefa.setDescricao(descricao);
            tarefa.setPraso(Date.from(prazo.atStartOfDay(ZoneId.systemDefault()).toInstant()));
            tarefa.setCadastro(Date.from(cadastro.atStartOfDay(ZoneId.systemDefault()).toInstant()));
            tarefa.setStatus(status);
            tarefa.setResponsavel(responsavel);

            em.getTransaction().begin();
            em.persist(tarefa);
            em.getTransaction().commit();

            retorno.setText("Tarefa cadastrada com sucesso! ID: " + tarefa.getId());
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            retorno.setText("Erro ao cadastrar tarefa!");
            e.printStackTrace();
        }
    }

    public void close() {
        em.close();
        emf.close();
    }
}
