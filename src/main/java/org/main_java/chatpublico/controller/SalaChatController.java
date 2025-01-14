package org.main_java.chatpublico.controller;

import org.main_java.chatpublico.model.InvitacionDTO;
import org.main_java.chatpublico.model.SalaChatDTO;
import org.main_java.chatpublico.service.SalaChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/salas-chat")
public class SalaChatController {

    @Autowired
    private SalaChatService salaChatService;

    @PostMapping("/crear/usuario/{idUsuario}")
    public SalaChatDTO crearSala(@PathVariable Long idUsuario, @RequestBody SalaChatDTO salaChatDTO) {
        return salaChatService.create(salaChatDTO, idUsuario);
    }

    @DeleteMapping("/eliminar/{id}/usuario/{idUsuario}")
    public String eliminarSala(@PathVariable Long id, @PathVariable Long idUsuario) {
        return salaChatService.delete(id, idUsuario);
    }

    @GetMapping("/{id}")
    public SalaChatDTO obtenerSalaPorId(@PathVariable Long id) {
        return salaChatService.findById(id);
    }

    @GetMapping
    public List<SalaChatDTO> obtenerTodasLasSalas() {
        return salaChatService.findAll();
    }

    @PostMapping("/invitar/{idSala}/usuario/{idUsuario}/receptor/{idReceptor}")
    public String invitarUsuario(@PathVariable Long idSala, @PathVariable Long idUsuario, @PathVariable Long idReceptor, @RequestBody InvitacionDTO invitacionDTO) {
        return salaChatService.invitarUsuario(idSala, idUsuario, idReceptor, invitacionDTO);
    }

    @PutMapping("/unirse/{idSala}/usuario/{idUsuario}/invitacion/{idInvitacion}")
    public SalaChatDTO unirsePorInvitacion(@PathVariable Long idSala, @PathVariable Long idUsuario, @PathVariable Long idInvitacion) {
        return salaChatService.unirsePorInvitacion(idSala, idUsuario, idInvitacion);
    }

    @PutMapping("/eliminar-usuario/{idSala}/usuario/{idUsuario}")
    public String eliminarUsuarioDeSala(@PathVariable Long idSala, @PathVariable Long idUsuario) {
        return salaChatService.eliminarUsuarioDeSala(idSala, idUsuario);
    }
}