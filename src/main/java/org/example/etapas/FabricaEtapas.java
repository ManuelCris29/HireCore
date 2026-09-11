package org.example.etapas;

import java.util.EnumMap;
import java.util.Map;

public class FabricaEtapas {

    private static final Map<TipoEtapa, TipoEtapa> SIGUIENTE_POSITIVA = Map.of(
            TipoEtapa.APLICADO,                 TipoEtapa.ENTREVISTA,
            TipoEtapa.ENTREVISTA,               TipoEtapa.PRUEBA_TECNICA,
            TipoEtapa.PRUEBA_TECNICA,           TipoEtapa.OFERTA,
            TipoEtapa.OFERTA,                   TipoEtapa.VERIFICACION_REFERENCIAS,
            TipoEtapa.VERIFICACION_REFERENCIAS, TipoEtapa.CONTRATADO
            // CONTRATADO y RECHAZADO son terminales: sin entrada aquí => sin siguiente positiva
    );

    private final Map<TipoEtapa, Etapa> etapas = new EnumMap<>(TipoEtapa.class);

    public FabricaEtapas() {
        registrar(new EtapaAplicado(siguienteDe(TipoEtapa.APLICADO)));
        registrar(new EtapaEntrevista(siguienteDe(TipoEtapa.ENTREVISTA)));
        registrar(new EtapaPruebaTecnica(siguienteDe(TipoEtapa.PRUEBA_TECNICA)));
        registrar(new EtapaOferta(siguienteDe(TipoEtapa.OFERTA)));
        registrar(new EtapaVerificacionReferencias(siguienteDe(TipoEtapa.VERIFICACION_REFERENCIAS)));
        registrar(new EtapaContratado(siguienteDe(TipoEtapa.CONTRATADO)));
        registrar(new EtapaRechazado(siguienteDe(TipoEtapa.RECHAZADO)));
    }

    private void registrar(Etapa etapa) {
        etapas.put(etapa.getTipoEtapa(), etapa);
    }

    private static TipoEtapa siguienteDe(TipoEtapa tipo) {
        return SIGUIENTE_POSITIVA.get(tipo); // null para etapas terminales
    }

    public Etapa obtener(TipoEtapa tipo) {
        Etapa etapa = etapas.get(tipo);
        if (etapa == null) {
            throw new IllegalArgumentException("Etapa no registrada: " + tipo);
        }
        return etapa;
    }
}
