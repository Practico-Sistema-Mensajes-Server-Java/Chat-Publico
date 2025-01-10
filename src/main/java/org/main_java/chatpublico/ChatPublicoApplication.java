package org.main_java.chatpublico;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class ChatPublicoApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChatPublicoApplication.class, args);
    }

}
