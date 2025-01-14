package org.main_java.chatpublico.service;

import jakarta.transaction.Transactional;
import org.main_java.chatpublico.domain.Invitacion;
import org.main_java.chatpublico.domain.SalaChat;
import org.main_java.chatpublico.domain.Usuario;
import org.main_java.chatpublico.model.InvitacionDTO;
import org.main_java.chatpublico.repos.InvitacionRepository;
import org.main_java.chatpublico.repos.SalaChatRepository;
import org.main_java.chatpublico.repos.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Service
public class InvitacionService {

    @Autowired
    private InvitacionRepository invitacionRepository;

    private final UsuarioRepository usuarioRepository;

    private final SalaChatRepository salaChatRepository;

    public InvitacionService(@Lazy UsuarioRepository usuarioRepository, @Lazy SalaChatRepository salaChatRepository) {
        this.usuarioRepository = usuarioRepository;
        this.salaChatRepository = salaChatRepository;
    }

    @Transactional
    public void enviarInvitacion(InvitacionDTO invitacionDTO) {
        Invitacion invitacion = mapToEntity(invitacionDTO);
        invitacionRepository.save(invitacion);

    }

    @Transactional
    public void eliminarInvitacion(Long id) {
        invitacionRepository.deleteById(id);
    }

    private InvitacionDTO mapToDTO(Invitacion invitacion) {
        if (invitacion == null) {
            throw new IllegalArgumentException("La invitación no puede ser nula");
        }

        InvitacionDTO invitacionDTO = new InvitacionDTO();
        invitacionDTO.setId(invitacion.getId());

        Usuario usuario = invitacion.getUsuario();
        if (usuario == null || usuario.getId() == null) {
            throw new IllegalArgumentException("El usuario asociado a la invitación no es válido");
        }
        invitacionDTO.setId_usuario(usuario.getId());

        SalaChat salaChat = invitacion.getSalaChat();
        if (salaChat == null || salaChat.getId() == null) {
            throw new IllegalArgumentException("La sala asociada a la invitación no es válida");
        }
        invitacionDTO.setId_sala(salaChat.getId());
        invitacionDTO.setId_usuario_receptor(invitacion.getIdUsuarioReceptor());
        invitacionDTO.setMensaje(invitacion.getMensaje());

        return invitacionDTO;
    }

    public Invitacion mapToEntity(InvitacionDTO invitacionDTO) {
        if (invitacionDTO == null) {
            throw new IllegalArgumentException("El DTO de invitación no puede ser nulo");
        }

        Invitacion invitacion = new Invitacion();

        invitacion.setId(invitacionDTO.getId());

        Usuario usuario = usuarioRepository.findById(invitacionDTO.getId_usuario())
                .orElseThrow(() -> new IllegalArgumentException("El usuario con ID " + invitacionDTO.getId_usuario() + " no existe"));
        invitacion.setUsuario(usuario);

        SalaChat salaChat = salaChatRepository.findById(invitacionDTO.getId_sala())
                .orElseThrow(() -> new IllegalArgumentException("La sala con ID " + invitacionDTO.getId_sala() + " no existe"));
        invitacion.setSalaChat(salaChat);
        invitacion.setIdUsuarioReceptor(invitacionDTO.getId_usuario_receptor());
        invitacion.setMensaje(invitacionDTO.getMensaje());


        return invitacion;
    }


}
