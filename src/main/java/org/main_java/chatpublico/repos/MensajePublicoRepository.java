package org.main_java.chatpublico.repos;

import org.main_java.chatpublico.domain.MensajePublico;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MensajePublicoRepository extends JpaRepository<MensajePublico, Long> {
}
