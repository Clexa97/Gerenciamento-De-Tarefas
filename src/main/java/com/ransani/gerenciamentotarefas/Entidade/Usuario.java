package com.ransani.gerenciamentotarefas.Entidade;


import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor @Getter @Setter
@RequiredArgsConstructor
@Table(name = "usuarios")
@Entity
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;
    @Column(name = "nome", nullable = false, length = 100)
    private String nome;
    @Column(name = "email", nullable = false, length = 100)
    private String email;
}
