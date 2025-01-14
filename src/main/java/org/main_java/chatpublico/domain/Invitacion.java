package org.main_java.chatpublico.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.main_java.chatpublico.util.Mensaje;

import java.time.LocalDateTime;

@Entity
@Table(name = "Invitacion")
@Getter
@Setter
public class Invitacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    @Column(name = "mensaje")
    private Mensaje mensaje;

    @Column(name = "usuario_receptor")
    private Long id_usuarioReceptor;

    @ManyToOne
    @JoinColumn(name = "id_sala")
    private SalaChat salaChat;

    public Invitacion() {
    }

    public Invitacion(Usuario usuario, SalaChat salaChat) {
        this.usuario = usuario;
        this.salaChat = salaChat;
    }
}
