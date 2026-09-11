package org.example.etapas;

public interface Etapa {

    TipoEtapa getTipoEtapa();
    TipoEtapa getSiguientePositiva(); // null si es una etapa terminal (CONTRATADO, RECHAZADO)
    boolean puedePasarA(TipoEtapa destino);

}
