package org.example.comandos;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class HistorialComandos {

    private final Deque<Comando> pila = new ArrayDeque<>();
    private final List<String> bitacora = new ArrayList<>();

    public void registrar(Comando comando) {
        pila.push(comando);
        bitacora.add(comando.auditoria());
    }

    public void deshacerUltimo() {
        if (pila.isEmpty()) {
            throw new IllegalStateException("No hay nada que deshacer");
        }
        Comando ultimo = pila.pop();
        ultimo.deshacer();
        bitacora.add("DESHECHO: " + ultimo.auditoria());
    }

    public List<String> getHistorial() {
        return List.copyOf(bitacora);
    }
}
