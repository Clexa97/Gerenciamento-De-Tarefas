package com.ransani.gerenciamentotarefas.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import com.ransani.gerenciamentotarefas.entidade.Tarefa;
import com.ransani.gerenciamentotarefas.entidade.Tarefa.Status;
import com.ransani.gerenciamentotarefas.entidade.Usuario;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

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
    private TextField responsavelField;
    @FXML
    private Label retorno;
    private EntityManagerFactory emf;
    private EntityManager em;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @FXML
    public void initialize() {
        emf = Persistence.createEntityManagerFactory("gerenciamentoTarefas");
        em = emf.createEntityManager();
        for (MenuItem item : statusComboBox.getItems()) {
            item.setOnAction(event -> statusComboBox.setText(item.getText()));
        }
    }

    @FXML
    private void onSalvarClick() {
        String titulo = tituloField.getText();
        String descricao = descricaoField.getText();
        LocalDate prazo = prazoField.getValue();
        LocalDate cadastro = cadastroField.getValue();
        String statusText = statusComboBox.getText();
        String responsavelIdText = responsavelField.getText();
        if (titulo.isEmpty() || descricao.isEmpty() || prazo == null || cadastro == null || statusText.equals("Status") || responsavelIdText.isEmpty()) {
            retorno.setText("Todos os campos devem ser preenchidos!");
            return;
        }
        if (prazo.isBefore(cadastro)) {
            retorno.setText("A data de prazo não pode ser anterior à data de cadastro!");
            return;
        }
        try {
            long responsavelId = Long.parseLong(responsavelIdText);
            Usuario responsavel = em.find(Usuario.class, responsavelId);
            if (responsavel == null) {
                retorno.setText("Responsável não encontrado!");
                return;
            }
            Status status = Status.valueOf(statusText);
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
            retorno.setText("Tarefa cadastrada com sucesso! ID da tarefa: " + tarefa.getId() + "\nPrazo: " + DATE_FORMATTER.format(prazo) + "\nCadastro: " + DATE_FORMATTER.format(cadastro));
        } catch (NumberFormatException e) {
            retorno.setText("ID do responsável inválido!");
        } catch (IllegalArgumentException e) {
            retorno.setText("Status inválido!");
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