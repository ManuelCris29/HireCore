package org.example.comandos;

import java.time.LocalDateTime;

public interface Comando {
    void ejecutar();
    void deshacer(String usuario, LocalDateTime fecha);
    String auditoria();
}
