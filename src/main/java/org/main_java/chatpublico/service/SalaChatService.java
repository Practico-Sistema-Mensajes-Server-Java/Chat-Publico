package org.main_java.chatpublico.service;

import com.netflix.discovery.converters.Auto;
import jakarta.transaction.Transactional;
import org.main_java.chatpublico.domain.Invitacion;
import org.main_java.chatpublico.domain.SalaChat;
import org.main_java.chatpublico.domain.Usuario;
import org.main_java.chatpublico.model.InvitacionDTO;
import org.main_java.chatpublico.model.SalaChatDTO;
import org.main_java.chatpublico.repos.InvitacionRepository;
import org.main_java.chatpublico.repos.MensajePublicoRepository;
import org.main_java.chatpublico.repos.SalaChatRepository;
import org.main_java.chatpublico.repos.UsuarioRepository;
import org.main_java.chatpublico.util.Mensaje;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SalaChatService {

    @Autowired
    private SalaChatRepository salaChatRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private InvitacionRepository invitacionRepository;

    private final InvitacionService invitacionService;

    public SalaChatService(InvitacionService invitacionService) {
        this.invitacionService = invitacionService;
    }

    @Transactional
    public SalaChat create (SalaChatDTO salaChatDTO, Long id_usuario) {

        Usuario usuario = usuarioRepository.findById(id_usuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!usuario.getRol().equals("administrador")) {
            throw new RuntimeException("Usuario no autorizado");
        }

        SalaChat salaChat = mapToEntity(salaChatDTO);

        return salaChatRepository.save(salaChat);
    }

    @Transactional
    public String delete (Long id, Long id_usuario) {

        Usuario usuario = usuarioRepository.findById(id_usuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!usuario.getRol().equals("administrador")) {
            throw new RuntimeException("Usuario no autorizado");
        }

        if (!salaChatRepository.existsById(id)) {
            throw new RuntimeException("Sala de chat no encontrada");
        }

        salaChatRepository.deleteById(id);
        return "Sala de chat eliminada con exito";
    }

    public SalaChatDTO findById(Long id) {

        SalaChat salaChat = salaChatRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sala de chat no encontrada"));

        return mapToDTO(salaChat);
    }

    public SalaChatDTO findByName(String nombre) {

        SalaChat salaChat = salaChatRepository.findAll().stream()
                .filter(sala -> sala.getNombre().equals(nombre))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Sala de chat no encontrada"));

        return mapToDTO(salaChat);
    }

    public List<SalaChatDTO> findAll() {

        List<SalaChat> salas = salaChatRepository.findAll();

        return salas.stream()
                .map(this::mapToDTO)
                .toList();
    }

    public String invitarUsuario (Long id_sala, Long id_emisor, Long id_receptor, InvitacionDTO invitacionDTO) {
        if (!salaChatRepository.existsById(id_sala)) {
            throw new RuntimeException("Sala de chat no encontrada");
        }

        if (!usuarioRepository.existsById(id_emisor)) {
            throw new RuntimeException("Usuario emisor no encontrado");
        }

        if (!usuarioRepository.existsById(id_receptor)) {
            throw new RuntimeException("Usuario receptor no encontrado");
        }

        if (verificarUsuarioEnSala(id_sala, id_receptor)) {
            throw new RuntimeException("Usuario ya se encuentra en la sala de chat");
        }

        if (!id_sala.equals(invitacionDTO.getId_sala())) {
            throw new RuntimeException("Id de sala no coincide con la invitacion");
        }

        if (!id_emisor.equals(invitacionDTO.getId_usuario())) {
            throw new RuntimeException("Id de usuario emisor no coincide con la invitacion");
        }

        if (!id_receptor.equals(invitacionDTO.getId_usuario_receptor())) {
            throw new RuntimeException("Id de usuario receptor no coincide con la invitacion");
        }

        Usuario user = usuarioRepository.getReferenceById(id_receptor);
        Invitacion invitacion = invitacionService.mapToEntity(invitacionDTO);
        user.getInvitaciones().add(invitacion);
        usuarioRepository.save(user);
        SalaChat sala = salaChatRepository.getReferenceById(id_sala);
        sala.getInvitaciones().add(invitacion);
        salaChatRepository.save(sala);

        invitacionService.enviarInvitacion(invitacionDTO);

        return "Invitacion enviada con exito";
    }

    @Transactional
    public SalaChatDTO unirsePorInvitacion(Long id_sala, Long id_usuario, Long id_invitacion) {

        SalaChat salaChat = salaChatRepository.findById(id_sala)
                .orElseThrow(() -> new RuntimeException("Sala de chat no encontrada"));

        Usuario usuario = usuarioRepository.findById(id_usuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Invitacion invitacion = invitacionRepository.findById(id_invitacion)
                .orElseThrow(() -> new RuntimeException("Invitacion no encontrada"));

        if (verificarUsuarioEnSala(id_sala, id_usuario)) {
            throw new RuntimeException("Usuario ya se encuentra en la sala de chat");
        }

        if (!invitacion.getSalaChat().getId().equals(id_sala)) {
            throw new RuntimeException("Invitacion no corresponde a la sala de chat");
        }

        if (!invitacion.getId_usuarioReceptor().equals(id_usuario)) {
            throw new RuntimeException("Invitacion no corresponde al usuario");
        }

        if (usuarioRepository.getReferenceById(id_usuario).getInvitaciones().contains(invitacion)) {
            throw new RuntimeException("Usuario no tiene invitacion para esta sala");
        }

        if (!invitacion.getUsuario().getRol().equals("administrador")) {
            throw new RuntimeException("Esta invitacion no fue enviada por un administrador");
        }

        if (Mensaje.RECHAZADO.equals(invitacion.getMensaje())) {
            System.out.println("Invitacion rechazada para esta sala");
        } else if (Mensaje.ACEPTADO.equals(invitacion.getMensaje())) {
            salaChat.getUsuarios().add(usuario);
            usuario.getSalas().add(salaChat);
            invitacionService.eliminarInvitacion(id_invitacion);
            salaChatRepository.save(salaChat);
            usuarioRepository.save(usuario);
        }

        return mapToDTO(salaChat);
    }

    @Transactional
    public String eliminarUsuarioDeSala(Long id_sala, Long id_usuario) {
        SalaChat salaChat = salaChatRepository.findById(id_sala)
                .orElseThrow(() -> new RuntimeException("Sala de chat no encontrada"));

        Usuario usuario = usuarioRepository.findById(id_usuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!verificarUsuarioEnSala(id_sala, id_usuario)) {
            throw new RuntimeException("Usuario no se encuentra en la sala de chat");
        }

        salaChat.getUsuarios().remove(usuario);
        usuario.getSalas().remove(salaChat);
        salaChatRepository.save(salaChat);
        usuarioRepository.save(usuario);

        return "Usuario eliminado de la sala de chat";
    }

    private boolean verificarUsuarioEnSala(Long id_sala, Long id_usuario) {
        SalaChat salaChat = salaChatRepository.findById(id_sala)
                .orElseThrow(() -> new RuntimeException("Sala de chat no encontrada"));

        Usuario usuario = usuarioRepository.findById(id_usuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (salaChat.getUsuarios().contains(usuario)) {
            return true;
        } else {
            return false;
        }
    }

    private SalaChat mapToEntity(SalaChatDTO salaChatDTO) {

        SalaChat salaChat = new SalaChat();

        salaChat.setId(salaChatDTO.getId());
        salaChat.setNombre(salaChatDTO.getNombre());
        salaChat.setMensajes(salaChatDTO.getMensajes());
        salaChat.setUsuarios(salaChatDTO.getUsuarios());
        salaChat.setFechaCreacion(salaChatDTO.getFechaCreacion());

        return salaChat;
    }

    private SalaChatDTO mapToDTO(SalaChat salaChat) {

        SalaChatDTO salaChatDTO = new SalaChatDTO();

        salaChatDTO.setId(salaChat.getId());
        salaChatDTO.setNombre(salaChat.getNombre());
        salaChatDTO.setMensajes(salaChat.getMensajes());
        salaChatDTO.setUsuarios(salaChat.getUsuarios());
        salaChatDTO.setFechaCreacion(salaChat.getFechaCreacion());

        return salaChatDTO;
    }
}
