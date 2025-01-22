package com.ransani.gerenciamentotarefas.controller;

import com.ransani.gerenciamentotarefas.entidade.Usuario;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.util.regex.Pattern;

public class EditarUsuario {

    @FXML
    private TextField idTextField;
    @FXML
    private Button buscarButton;
    @FXML
    private Label idLabel;
    @FXML
    private Label usuarioLabel;
    @FXML
    private Label emailLabel;
    @FXML
    private Label erroLabel;
    @FXML
    private TextField emailTextField;
    @FXML
    private Button saveButton;
    @FXML
    private Label respostaLabel;

    private EntityManagerFactory emf;
    private EntityManager em;
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

    private Usuario usuarioAtual = null;

    @FXML
    public void initialize() {
        emf = Persistence.createEntityManagerFactory("gerenciamentoTarefas");
        em = emf.createEntityManager();
        limparLabels();
    }

    @FXML
    private void onBuscarClick() {
        limparLabels();
        String idTexto = idTextField.getText();

        if (idTexto.isEmpty()) {
            erroLabel.setText("O campo ID não pode estar vazio!");
            return;
        }

        try {
            long id = Long.parseLong(idTexto);
            usuarioAtual = em.find(Usuario.class, id);

            if (usuarioAtual != null) {
                idLabel.setText("ID: " + usuarioAtual.getId());
                usuarioLabel.setText("Usuário: " + usuarioAtual.getNome());
                emailLabel.setText("E-mail: " + usuarioAtual.getEmail());
            } else {
                erroLabel.setText("Usuário não encontrado!");
            }
        } catch (NumberFormatException e) {
            erroLabel.setText("ID inválido! Insira um número.");
        } catch (Exception e) {
            erroLabel.setText("Erro ao buscar usuário.");
            e.printStackTrace();
        }
    }

    @FXML
    private void onSalvarClick() {
        if (usuarioAtual == null) {
            respostaLabel.setText("Nenhum usuário foi selecionado.");
            return;
        }

        String novoEmail = emailTextField.getText();
        if (novoEmail.isEmpty()) {
            respostaLabel.setText("O campo de e-mail não pode estar vazio.");
            return;
        }
        if (!EMAIL_PATTERN.matcher(novoEmail).matches()) {
            respostaLabel.setText("E-mail inválido! Insira um e-mail válido.");
            return;
        }

        try {
            em.getTransaction().begin();
            usuarioAtual.setEmail(novoEmail);
            em.merge(usuarioAtual);
            em.getTransaction().commit();

            respostaLabel.setText("E-mail atualizado com sucesso!");

            // Atualize as informações exibidas
            emailLabel.setText("E-mail: " + novoEmail);
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            respostaLabel.setText("Erro ao atualizar o e-mail.");
            e.printStackTrace();
        }
    }

    private void limparLabels() {
        idLabel.setText("ID");
        usuarioLabel.setText("Usuário");
        emailLabel.setText("E-mail");
        erroLabel.setText("");
        respostaLabel.setText("");
    }

    @FXML
    public void close() {
        em.close();
        emf.close();
    }
}
