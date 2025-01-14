package org.main_java.chatpublico.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "Usuario")
@Getter
@Setter
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "rol", nullable = false)
    private String rol;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MensajePublico> mensajes;

    @ManyToMany
    @JoinTable(
            name = "usuario_sala",
            joinColumns = @JoinColumn(name = "usuario_id"),
            inverseJoinColumns = @JoinColumn(name = "sala_id")
    )
    private List<SalaChat> salas;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Invitacion> invitaciones;

    public Usuario() {}

    public Usuario(String nombre, String rol, List<MensajePublico> mensajes, List<SalaChat> salas, List<Invitacion> invitaciones) {
        this.nombre = nombre;
        this.rol = rol;
        this.mensajes = mensajes;
        this.salas = salas;
        this.invitaciones = invitaciones;
    }
}
