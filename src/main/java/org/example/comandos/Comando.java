package org.example.comandos;

public interface Comando {
    void ejecutar();
    void deshacer();
    String auditoria();
}
