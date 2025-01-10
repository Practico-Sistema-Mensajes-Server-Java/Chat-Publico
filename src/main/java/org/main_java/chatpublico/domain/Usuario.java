package org.main_java.chatpublico.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "Usuario")
@Getter
@Setter
public class Usuario {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(name = "nombre")
        private String nombre;

        @Column(name = "rol")
        private String rol;

        @OneToMany
        @JoinColumn(name = "mensajes")
        private List<MensajePublico> mensajes;

        @ManyToMany
        @JoinColumn(name = "salas")
        private List<SalaChat> salas;

        @OneToMany
        @JoinColumn(name = "invitaciones")
        private List<Invitacion> invitaciones;

        public Usuario() {
        }

        public Usuario(String nombre, String rol, List<MensajePublico> mensajes, List<SalaChat> salas, List<Invitacion> invitaciones) {
            this.nombre = nombre;
            this.rol = rol;
            this.mensajes = mensajes;
            this.salas = salas;
            this.invitaciones = invitaciones;
        }
}
