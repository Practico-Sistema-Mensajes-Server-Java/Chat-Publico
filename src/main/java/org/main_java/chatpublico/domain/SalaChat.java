package org.main_java.chatpublico.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "SalaChat")
@Getter
@Setter
public class SalaChat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion;

    @OneToMany(mappedBy = "salaChat", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MensajePublico> mensajes;

    @ManyToMany(mappedBy = "salas")
    private List<Usuario> usuarios;

    @OneToMany(mappedBy = "salaChat", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Invitacion> invitaciones;

    public SalaChat() {}

    public SalaChat(String nombre, LocalDateTime fechaCreacion, List<MensajePublico> mensajes, List<Usuario> usuarios, List<Invitacion> invitaciones) {
        this.nombre = nombre;
        this.fechaCreacion = fechaCreacion;
        this.mensajes = mensajes;
        this.usuarios = usuarios;
        this.invitaciones = invitaciones;
    }
}
