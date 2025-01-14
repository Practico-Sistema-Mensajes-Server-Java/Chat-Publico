package org.main_java.chatpublico.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "MensajePublico")
@Getter
@Setter
public class MensajePublico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "mensaje", nullable = false)
    private String mensaje;

    @Column(name = "fecha", nullable = false)
    private LocalDateTime fecha;

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "id_sala", nullable = false)
    private SalaChat salaChat;

    public MensajePublico() {}

    public MensajePublico(String mensaje, LocalDateTime fecha, Usuario usuario, SalaChat salaChat) {
        this.mensaje = mensaje;
        this.fecha = fecha;
        this.usuario = usuario;
        this.salaChat = salaChat;
    }
}
