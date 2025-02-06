package com.ransani.gerenciamentotarefas.controller;

import com.ransani.gerenciamentotarefas.entidade.Usuario;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class DeletarUsuario {

    @FXML
    private TextField idTextField;
    @FXML
    private Label idLabel;
    @FXML
    private Label usuarioLabel;
    @FXML
    private Label emailLabel;
    @FXML
    private Label mensagemLabel;

    private EntityManagerFactory emf;
    private EntityManager em;
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
        String idTexto = idTextField.getText().trim();

        if (idTexto.isEmpty()) {
            mensagemLabel.setText("O campo ID não pode estar vazio!");
            return;
        }

        try {
            long id = Long.parseLong(idTexto);
            usuarioAtual = em.find(Usuario.class, id);

            if (usuarioAtual != null) {
                idLabel.setText("ID: " + usuarioAtual.getId());
                usuarioLabel.setText("Usuário: " + usuarioAtual.getNome());
                emailLabel.setText("E-mail: " + usuarioAtual.getEmail());
                mensagemLabel.setText("Usuário encontrado.");
            } else {
                mensagemLabel.setText("Usuário não encontrado!");
            }
        } catch (NumberFormatException e) {
            mensagemLabel.setText("ID inválido! Insira um número válido.");
        } catch (Exception e) {
            mensagemLabel.setText("Erro ao buscar usuário.");
            e.printStackTrace();
        }
    }

    @FXML
    private void onDeletarClick() {
        if (usuarioAtual == null) {
            mensagemLabel.setText("Nenhum usuário foi selecionado para exclusão.");
            return;
        }

        try {
            em.getTransaction().begin();
            em.remove(em.contains(usuarioAtual) ? usuarioAtual : em.merge(usuarioAtual));
            em.getTransaction().commit();

            mensagemLabel.setText("Usuário deletado com sucesso!");
            usuarioAtual = null;

            idLabel.setText("ID");
            usuarioLabel.setText("Usuário");
            emailLabel.setText("E-mail");
            idTextField.clear();

        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            mensagemLabel.setText("Erro ao deletar o usuário.");
            e.printStackTrace();
        }
    }

    private void limparLabels() {
        idLabel.setText("ID");
        usuarioLabel.setText("Usuário");
        emailLabel.setText("E-mail");
        mensagemLabel.setText("");
        usuarioAtual = null;
    }

    @FXML
    public void close() {
        em.close();
        emf.close();
    }
}

