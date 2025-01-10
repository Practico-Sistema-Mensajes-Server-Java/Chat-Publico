package org.main_java.chatpublico.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Data
public class MensajePublicoDTO {

    private Long id;

    @NotNull
    @Size(min = 1, max = 255)
    private String mensaje;

    @NotNull
    private LocalDateTime fecha;

    @NotNull
    private Long id_usuario;

    @NotNull
    private Long id_sala;
}
