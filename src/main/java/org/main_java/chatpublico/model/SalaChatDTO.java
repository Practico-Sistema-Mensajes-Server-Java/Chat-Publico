package org.main_java.chatpublico.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.main_java.chatpublico.domain.Invitacion;
import org.main_java.chatpublico.domain.MensajePublico;
import org.main_java.chatpublico.domain.Usuario;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Data
public class SalaChatDTO {

    private Long id;

    @NotNull
    @Size(min = 1, max = 255)
    private String nombre;

    @NotNull
    private LocalDateTime fechaCreacion;


    private List<MensajePublico> mensajes;

    @NotNull
    private List<Usuario> usuarios;

    private List<Invitacion> invitaciones;
}
