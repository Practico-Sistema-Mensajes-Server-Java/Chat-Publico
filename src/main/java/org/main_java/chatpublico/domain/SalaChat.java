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

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;

    @OneToMany
    @JoinColumn(name = "mensajes")
    private List<MensajePublico> mensajes;

    @ManyToMany
    @JoinColumn(name = "usuarios")
    private List<Usuario> usuarios;

    @OneToMany
    @JoinColumn(name = "invitaciones")
    private List<Invitacion> invitaciones;

    public SalaChat() {
    }

    public SalaChat(String nombre, LocalDateTime fechaCreacion, List<MensajePublico> mensajes, List<Usuario> usuarios, List<Invitacion> invitaciones) {
        this.nombre = nombre;
        this.fechaCreacion = fechaCreacion;
        this.mensajes = mensajes;
        this.usuarios = usuarios;
        this.invitaciones = invitaciones;
    }
}
