package org.main_java.chatpublico.controller;

import org.main_java.chatpublico.model.InvitacionDTO;
import org.main_java.chatpublico.service.InvitacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/invitaciones")
public class InvitacionController {

    @Autowired
    private InvitacionService invitacionService;

    @PostMapping("/enviar")
    public void enviarInvitacion(@RequestBody InvitacionDTO invitacionDTO) {
        invitacionService.enviarInvitacion(invitacionDTO);
    }

    @DeleteMapping("/eliminar/{id}")
    public void eliminarInvitacion(@PathVariable Long id) {
        invitacionService.eliminarInvitacion(id);
    }
}