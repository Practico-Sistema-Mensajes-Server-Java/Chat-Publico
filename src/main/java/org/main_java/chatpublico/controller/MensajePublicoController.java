package org.main_java.chatpublico.controller;

import org.main_java.chatpublico.domain.MensajePublico;
import org.main_java.chatpublico.model.MensajePublicoDTO;
import org.main_java.chatpublico.service.MensajePublicoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mensajes-publicos")
public class MensajePublicoController {

    @Autowired
    private MensajePublicoService mensajePublicoService;

    @GetMapping("/sala/{idSala}")
    public List<MensajePublico> obtenerMensajesPorSala(@PathVariable Long idSala) {
        return mensajePublicoService.getMensajesPublicosPorSala(idSala);
    }

    @GetMapping("/usuario/{idUsuario}")
    public List<MensajePublico> obtenerMensajesPorUsuario(@PathVariable Long idUsuario) {
        return mensajePublicoService.getMensajesPublicosPorUsuario(idUsuario);
    }

    @GetMapping
    public List<MensajePublico> obtenerTodosLosMensajes() {
        return mensajePublicoService.getMensajesPublicos();
    }

    @PostMapping("/crear/usuario/{idUsuario}")
    public void crearMensaje(@PathVariable Long idUsuario, @RequestBody MensajePublicoDTO mensajePublicoDTO) {
        mensajePublicoService.create(idUsuario, mensajePublicoDTO);
    }

    @PutMapping("/actualizar/{id}")
    public void actualizarMensaje(@PathVariable Long id, @RequestParam String mensaje, @RequestParam Long idUsuario, @RequestParam Long idSala) {
        mensajePublicoService.update(id, mensaje, idUsuario, idSala);
    }

    @DeleteMapping("/eliminar/{id}")
    public void eliminarMensaje(@PathVariable Long id) {
        mensajePublicoService.delete(id);
    }
}
