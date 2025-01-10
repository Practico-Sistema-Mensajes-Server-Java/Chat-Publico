package org.main_java.chatpublico.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class MensajePublicoDTO {

    private Long id;

    @NotNull
    @Size(min = 1, max = 255)
    private String mensaje;

    @NotNull
    private String fecha;

    @NotNull
    private Long id_usuario;

    @NotNull
    private Long id_sala;
}
