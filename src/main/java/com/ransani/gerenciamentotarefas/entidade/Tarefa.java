package com.ransani.gerenciamentotarefas.entidade;


import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@AllArgsConstructor @Getter @Setter
@NoArgsConstructor
@Entity
public class Tarefa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String titulo;
    private String descricao;
    private Date praso;
    private Date cadastro;
    @Enumerated(EnumType.STRING)
    private Status status;
    @OneToOne(mappedBy = "usuario")
    private Usuario responsavel;

    public enum Status{
        PENDENTE,
        EM_ANDAMENTO,
        ATRASADA,
        CONCLUIDA
    }





}
