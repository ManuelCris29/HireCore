package org.example.gestor;

public class TransicionInvalidaException extends  RuntimeException {
    public TransicionInvalidaException(String mensaje) {
        super(mensaje);
    }
}
