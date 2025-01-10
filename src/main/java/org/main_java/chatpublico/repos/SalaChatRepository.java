package org.main_java.chatpublico.repos;

import org.main_java.chatpublico.domain.SalaChat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SalaChatRepository extends JpaRepository<SalaChat, Long> {
}
