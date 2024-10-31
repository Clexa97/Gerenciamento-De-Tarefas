package com.ransani.gerenciamentotarefas.Entidade;


import java.util.Date;

public class Tarefa {

    private long id;
    private String titulo;
    private String descricao;
    private Date praso;
    private Date cadastro;
    private Status status;
    private Usuario responsavel;

    public enum Status{
        PENDENTE,
        EM_ANDAMENTO,
        ATRASADA,
        CONCLUIDA
    }





}
