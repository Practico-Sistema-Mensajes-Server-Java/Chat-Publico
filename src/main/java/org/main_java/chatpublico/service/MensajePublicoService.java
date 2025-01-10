package org.main_java.chatpublico.service;

import jakarta.transaction.Transactional;
import org.main_java.chatpublico.config.RabbitMQConfig;
import org.main_java.chatpublico.domain.MensajePublico;
import org.main_java.chatpublico.domain.SalaChat;
import org.main_java.chatpublico.domain.Usuario;
import org.main_java.chatpublico.model.MensajePublicoDTO;
import org.main_java.chatpublico.rabbitMQ.RabbitMQProducer;
import org.main_java.chatpublico.repos.MensajePublicoRepository;
import org.main_java.chatpublico.repos.SalaChatRepository;
import org.main_java.chatpublico.repos.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MensajePublicoService {

    @Autowired
    private MensajePublicoRepository mensajePublicoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RabbitMQProducer rabbitMQProducer;

    private final SalaChatRepository salaChatRepository;

    public MensajePublicoService(@Lazy SalaChatRepository salaChatRepository) {
        this.salaChatRepository = salaChatRepository;
    }

    public List<MensajePublico> getMensajesPublicosPorSala(Long id_sala) {
        SalaChat salaChat = salaChatRepository.findById(id_sala)
                .orElseThrow(() -> new IllegalArgumentException("La sala no existe"));

        return salaChat.getMensajes();
    }

    public List<MensajePublico> getMensajesPublicosPorUsuario(Long id_usuario) {
        Usuario usuario = usuarioRepository.findById(id_usuario)
                .orElseThrow(() -> new IllegalArgumentException("El usuario no existe"));

        return usuario.getMensajes();
    }

    public List<MensajePublico> getMensajesPublicos() {
        return mensajePublicoRepository.findAll();
    }

    public MensajePublico findMensajePublicoById(Long id) {
        return mensajePublicoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Mensaje no encontrado"));
    }

    @Transactional
    public void create (Long id_usuario, MensajePublicoDTO mensajePublicoDTO) {
        if (mensajePublicoDTO.getId() == null) {
            throw new RuntimeException("El id es requerido para crear un mensaje");
        }

        if (mensajePublicoDTO.getMensaje() == null || mensajePublicoDTO.getMensaje().trim().isEmpty()) {
            throw new IllegalArgumentException("El mensaje no puede estar en blanco");
        }

        if (mensajePublicoDTO.getId_usuario() == null) {
            throw new IllegalArgumentException("El id del usuario es requerido para crear un mensaje");
        }

        if (mensajePublicoDTO.getId_sala() == null) {
            throw new IllegalArgumentException("El id de la sala es requerido para crear un mensaje");
        }

        usuarioRepository.findById(mensajePublicoDTO.getId_usuario())
                .orElseThrow(() -> new IllegalArgumentException("El usuario no existe"));

        salaChatRepository.findById(mensajePublicoDTO.getId_sala())
                .orElseThrow(() -> new IllegalArgumentException("La sala no existe"));

        Usuario usuario = usuarioRepository.findById(id_usuario)
                .orElseThrow(() -> new IllegalArgumentException("El usuario no existe"));

        if (usuario.getSalas() == null || usuario.getSalas().isEmpty()) {
            throw new IllegalArgumentException("El usuario no pertenece a ninguna sala");
        }

        if (usuario.getSalas().stream().noneMatch(s -> s.getId().equals(mensajePublicoDTO.getId_sala()))) {
            throw new IllegalArgumentException("El usuario no pertenece a la sala");
        }

        MensajePublico mensajePublico = mapToEntity(mensajePublicoDTO);
        usuarioRepository.getReferenceById(mensajePublicoDTO.getId_usuario())
                .getMensajes().add(mensajePublico);
        salaChatRepository.getReferenceById(mensajePublicoDTO.getId_sala())
                .getMensajes().add(mensajePublico);
        mensajePublicoRepository.save(mensajePublico);

        // Notificar a RabbitMQ
        rabbitMQProducer.enviarMensaje(
                RabbitMQConfig.NOMBRE_INTERCAMBIO,
                RabbitMQConfig.CLAVE_ENRUTAMIENTO,
                "Mensaje creado [ " + "ID: " + mensajePublico.getId() + "Mensaje: " + mensajePublico.getMensaje() + " ]"
        );
    }

    @Transactional
    public void update(Long id, String mensaje, Long id_usuario, Long id_sala) {
        if (id == null) {
            throw new IllegalArgumentException("El id es requerido para actualizar");
        }

        MensajePublico mensajePublico = mensajePublicoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("El mensaje no existe"));

        if (mensaje == null || mensaje.trim().isEmpty()) {
            throw new IllegalArgumentException("El mensaje no puede estar en blanco");
        }

        if (mensaje.equals(mensajePublico.getMensaje())) {
            throw new IllegalArgumentException("El mensaje es igual al anterior, no se puede actualizar");
        }

        if (id_usuario == null) {
            throw new IllegalArgumentException("El id del usuario es requerido para actualizar");
        }

        Usuario usuario = usuarioRepository.findById(id_usuario)
                .orElseThrow(() -> new IllegalArgumentException("El usuario no existe"));

        if (!usuario.equals(mensajePublico.getUsuario())) {
            throw new IllegalArgumentException("El mensaje no pertenece al usuario");
        }

        if (id_sala == null) {
            throw new IllegalArgumentException("El id de la sala es requerido para actualizar");
        }

        SalaChat salaChat = salaChatRepository.findById(id_sala)
                .orElseThrow(() -> new IllegalArgumentException("La sala no existe"));

        boolean usuarioPerteneceSala = salaChat.getUsuarios().stream()
                .anyMatch(u -> u.getId().equals(id_usuario));

        if (!usuarioPerteneceSala) {
            throw new IllegalArgumentException("El usuario no pertenece a la sala");
        }

        if (!salaChat.equals(mensajePublico.getSalaChat())) {
            throw new IllegalArgumentException("El mensaje no pertenece a la sala");
        }

        mensajePublico.setMensaje(mensaje);
        mensajePublicoRepository.save(mensajePublico);

        // Notificar a RabbitMQ
        rabbitMQProducer.enviarMensaje(
                RabbitMQConfig.NOMBRE_INTERCAMBIO,
                RabbitMQConfig.CLAVE_ENRUTAMIENTO,
                "Mensaje actualizado [ " + "ID: " + mensajePublico.getId() + "Mensaje: " + mensajePublico.getMensaje() + " ]"
        );
    }

    @Transactional
    public void delete(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("El id es requerido para eliminar");
        }

        MensajePublico mensajePublico = mensajePublicoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("El mensaje no existe"));

        usuarioRepository.getReferenceById(mensajePublico.getUsuario().getId())
                .getMensajes().remove(mensajePublico);
        salaChatRepository.getReferenceById(mensajePublico.getSalaChat().getId())
                .getMensajes().remove(mensajePublico);
        mensajePublicoRepository.delete(mensajePublico);

        // Notificar a RabbitMQ
        rabbitMQProducer.enviarMensaje(
                RabbitMQConfig.NOMBRE_INTERCAMBIO,
                RabbitMQConfig.CLAVE_ENRUTAMIENTO,
                "Mensaje eliminado: [ " + "ID: " + id + " ]"
        );
    }

    private MensajePublico mapToEntity (MensajePublicoDTO mensajePublicoDTO) {
        MensajePublico mensajePublico = new MensajePublico();
        mensajePublico.setId(mensajePublicoDTO.getId());
        mensajePublico.setMensaje(mensajePublicoDTO.getMensaje());
        mensajePublico.setFecha(mensajePublicoDTO.getFecha());
        usuarioRepository.findById(mensajePublicoDTO.getId_usuario())
                .ifPresent(mensajePublico::setUsuario);
        salaChatRepository.findById(mensajePublicoDTO.getId_sala())
                .ifPresent(mensajePublico::setSalaChat);
        return mensajePublico;
    }

    private MensajePublicoDTO mapToDTO (MensajePublico mensajePublico) {
        MensajePublicoDTO mensajePublicoDTO = new MensajePublicoDTO();
        mensajePublicoDTO.setId(mensajePublico.getId());
        mensajePublicoDTO.setMensaje(mensajePublico.getMensaje());
        mensajePublicoDTO.setFecha(mensajePublico.getFecha());
        mensajePublicoDTO.setId_usuario(mensajePublico.getUsuario().getId());
        mensajePublicoDTO.setId_sala(mensajePublico.getSalaChat().getId());
        return mensajePublicoDTO;
    }
}
