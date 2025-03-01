package com.ransani.gerenciamentotarefas.entidade;


import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@Entity
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String nome;
    private String email;

    @OneToMany(mappedBy = "responsavel")
    private List<Tarefa> tarefas;
}
