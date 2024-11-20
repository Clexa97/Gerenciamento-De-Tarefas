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


public class CadastroUsuario {

    @FXML
    private TextField nomeField;
    @FXML
    private TextField emailField;
    @FXML
    private Button salvarBotton;
    @FXML
    private Label statusLabel;
    private EntityManagerFactory emf;
    private EntityManager em;
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

    @FXML
    public void initialize() {
        emf = Persistence.createEntityManagerFactory("gerenciamentoTarefas");
        em = emf.createEntityManager();
    }

    @FXML
    private void onSalvarClick() {
        String nome = nomeField.getText();
        String email = emailField.getText();
        if (nome.isEmpty()) {
            statusLabel.setText("Nome não pode estar vazio!");
            return;
        }
        if (email.isEmpty()) {
            statusLabel.setText("E-mail não pode estar vazio!");
            return;
        }
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            statusLabel.setText("E-mail inválido!");
            return;
        }
        try {
            Usuario usuario = new Usuario();
            usuario.setNome(nome);
            usuario.setEmail(email);
            em.getTransaction().begin();
            em.persist(usuario);
            em.getTransaction().commit();
            statusLabel.setText("Usuário cadastrado com sucesso com o ID:"+ usuario.getId());
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            statusLabel.setText("Erro ao cadastrar usuário!");
            e.printStackTrace();
        }
    }

    @FXML
    public void close() {
        em.close();
        emf.close();
    }
}
