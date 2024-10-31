package com.ransani.gerenciamentotarefas.Entidade;


import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@AllArgsConstructor @Getter @Setter
@RequiredArgsConstructor
@Table(name = "tarefas")
@Entity
public class Tarefa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;
    @Column(name = "titulo", nullable = false, length = 100)
    private String titulo;
    @Column(name = "descricao", nullable = false, length = 250)
    private String descricao;
    @Column(name = "praso", nullable = false)
    private Date praso;
    @Column(name = "cadastro", nullable = false)
    private Date cadastro;
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;
    @OneToOne
    @JoinColumn(name = "responsavel", nullable = false)
    private Usuario responsavel;

    public enum Status{
        PENDENTE,
        EM_ANDAMENTO,
        ATRASADA,
        CONCLUIDA
    }





}
