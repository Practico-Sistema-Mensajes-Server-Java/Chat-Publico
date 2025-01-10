package org.main_java.chatpublico.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.main_java.chatpublico.util.Mensaje;

@Getter
@Setter
@Data
public class InvitacionDTO {

    private Long id;
    private Long id_usuario;
    private Long id_sala;
    private Long id_usuario_receptor;
    private Mensaje mensaje;
}
