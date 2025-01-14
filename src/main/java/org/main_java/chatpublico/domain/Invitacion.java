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
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @Column(name = "mensaje", nullable = false)
    private Mensaje mensaje;

    @Column(name = "usuario_receptor", nullable = false)
    private Long idUsuarioReceptor;

    @ManyToOne
    @JoinColumn(name = "id_sala", nullable = false)
    private SalaChat salaChat;

    @Column(name = "fecha", nullable = false)
    private LocalDateTime fecha;

    public Invitacion() {}

    public Invitacion(Usuario usuario, Mensaje mensaje, Long idUsuarioReceptor, SalaChat salaChat, LocalDateTime fecha) {
        this.usuario = usuario;
        this.mensaje = mensaje;
        this.idUsuarioReceptor = idUsuarioReceptor;
        this.salaChat = salaChat;
        this.fecha = fecha;
    }
}
