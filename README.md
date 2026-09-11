# HireCore — seguimiento de candidatos

Refactorización del sistema de seguimiento de candidatos (ATS) del reto evaluativo del Bloque 1.

Ejecutar: `mvn compile exec:java -Dexec.mainClass=org.example.Main`

El diagrama de clases completo está en `uml-hirecore.png` (fuente editable en `uml-hirecore.puml`, versión vectorial en `uml-hirecore.svg`).

El código no lleva comentarios: el diseño se explica por su estructura. Este documento identifica los patrones, que es lo que el reto pide razonar aparte.

---

## Los patrones, y qué requisito resuelve cada uno

### Requisito 1 · Nuevas etapas → **State**

Cada etapa es un objeto con comportamiento propio que responde si puede pasar a un destino, en lugar de un condicional central que las conozca a todas.

| Rol del patrón | Clase |
|---|---|
| Contexto | `Candidato` (guarda su `Etapa` actual) |
| Estado abstracto | `Etapa` |
| Estados concretos | `EtapaAplicado`, `EtapaEntrevista`, `EtapaPruebaTecnica`, `EtapaOferta`, `EtapaVerificacionReferencias`, `EtapaContratado`, `EtapaRechazado` |

El grafo de transiciones vive centralizado en `FabricaEtapas.TRANSICIONES`, y cada etapa recibe sus destinos por constructor. Así, **agregar una etapa nueva no obliga a modificar ninguna etapa existente**. Los pasos son:

1. Añadir el valor al enum `TipoEtapa`.
2. Crear su clase `EtapaEnEspera implements Etapa`, que recibe sus destinos por constructor.
3. En `FabricaEtapas`, añadir su fila a `TRANSICIONES` y su línea `registrar(...)` en el constructor.
4. Si la etapa debe ser *alcanzable*, añadirla al `Set` de destinos de su etapa origen — que también es una fila de `TRANSICIONES`.

Los cuatro pasos tocan un archivo nuevo, el enum y la fábrica. **Ninguna clase de etapa existente se abre**, que es justo lo que el requisito pide: antes, el paso 4 obligaba a editar `EtapaEntrevista` porque cada etapa llevaba sus destinos escritos dentro.

`FabricaEtapas` cumple además el rol de **Factory**: traduce la etiqueta `TipoEtapa` que recibe el gestor al objeto `Etapa` correspondiente, y concentra todos los `new` de etapas en un único archivo.

### Requisito 2 · Notificaciones diferenciadas → **Observer**

El publicador avisa a una lista de suscriptores sin conocer a ninguno por su nombre concreto.

| Rol del patrón | Clase |
|---|---|
| Sujeto / publicador | `PublicadorEventos` |
| Observador abstracto | `ObservadorCandidato` |
| Observadores concretos | `ObservadorReclutador`, `ObservadorGerente`, `ObservadorNomina`, `ObservadorPortalCandidato` |
| Mensaje | `EventoCambioEtapa` |

Cada observador aplica su propio filtro sobre el evento que recibe:

| Destinatario | Recibe |
|---|---|
| Reclutador | todos los cambios |
| Gerente de contratación | solo `OFERTA` y `CONTRATADO` |
| Nómina | solo `CONTRATADO` |
| Portal del candidato | todos los cambios excepto notas internas |

Una transición puede marcarse como **nota interna** con la sobrecarga `avanzarEstado(candidato, tipoDestino, usuario, esNotaInterna)`. El indicador viaja dentro del `EventoCambioEtapa`, de modo que el gestor sigue sin saber quién lo consume: es `ObservadorPortalCandidato` —y solo él— quien decide callarse. La bitácora de auditoría marca esas transiciones con `NOTA INTERNA`.

`GestorDeCandidato` no nombra a ninguno de estos destinatarios: solo publica el evento. Añadir un quinto observador no obliga a tocar el gestor.

El envío se abstrae aparte con `CanalNotificacion` (**Strategy**), implementado por `CanalEmail` y `CanalPortal`. Un observador decide *a quién* avisar; el canal decide *por dónde*.

### Requisito 3 · Deshacer con auditoría → **Command + Memento**

Los dos patrones que el enunciado señala como habituales en pareja: uno ejecuta la orden, el otro guarda lo necesario para revertirla.

| Rol del patrón | Clase |
|---|---|
| Comando abstracto | `Comando` |
| Comando concreto | `ComandoAvanzarEtapa` |
| Invocador / historial | `HistorialComandos` |
| Cliente | `GestorDeCandidato` |
| Originador | `Candidato` |
| Recuerdo (memento) | `CandidatoMemento` |

`ComandoAvanzarEtapa.ejecutar()` sigue un orden que no es casual:

1. **Valida** la transición contra la etapa actual — si es inválida lanza `TransicionInvalidaException` y no se toca nada.
2. **Guarda** el `CandidatoMemento` con la etapa anterior — después de validar, para no ensuciar nada en un cambio ilegal; antes de cambiar, para que la foto sea del estado viejo.
3. **Cambia** la etapa del candidato.
4. **Publica** el `EventoCambioEtapa`.

El comando se registra en el historial *después* de ejecutarse, de modo que solo se apilan transiciones que ocurrieron de verdad.

`HistorialComandos` mantiene **dos estructuras**, porque responde a dos preguntas distintas:

- La **pila** (`Deque<Comando>`) responde «¿qué deshago ahora?», y por eso tiene que perder elementos al deshacer.
- La **bitácora** (`List<String>`) responde «¿qué pasó aquí?», y por eso no puede perder ninguno: solo crece. Cada línea la aporta el propio comando a través de `Comando.auditoria()`, con fecha, usuario, candidato y etapa destino. El deshacer también se anota, marcado como `DESHECHO`.

Si fueran la misma estructura, deshacer un cambio lo borraría del registro — precisamente el rechazo accidental que el enunciado pone como ejemplo sería el que no dejaría rastro.

---

## Cómo se cumple «el gestor no debe conocer»

El enunciado exige que `GestorDeCandidato` no conozca nombres concretos de etapas, ni destinatarios de notificación, ni la lógica de deshacer. Su implementación completa:

```java
public void avanzarEstado(Candidato candidato, TipoEtapa tipoDestino, String usuario) {
    avanzarEstado(candidato, tipoDestino, usuario, false);
}

public void avanzarEstado(Candidato candidato, TipoEtapa tipoDestino, String usuario, boolean esNotaInterna) {
    Etapa nuevaEtapa = fabrica.obtener(tipoDestino);
    Comando comando = new ComandoAvanzarEtapa(candidato, nuevaEtapa, usuario,
            LocalDateTime.now(), publicador, esNotaInterna);
    comando.ejecutar();
    historial.registrar(comando);
}

public void deshacerUltimaTransicion() {
    historial.deshacerUltimo();
}
```

- **Etapas**: recibe una etiqueta del enum y pide el objeto a la fábrica. No nombra ninguna clase de etapa.
- **Destinatarios**: no aparecen. El comando publica el evento y el publicador reparte.
- **Deshacer**: delega en el historial. No sabe qué es un memento.

---

## Estructura del proyecto

```
org.example
├── Main                          raíz de composición: monta las piezas y ejecuta la demo
├── dominio
│   └── Candidato                 originador del Memento; guarda su etapa actual
├── etapas                        ── State + Factory
│   ├── TipoEtapa                 enum: las siete etiquetas
│   ├── Etapa                     interfaz: getTipoEtapa, puedePasarA
│   ├── Etapa*                    las siete etapas concretas
│   └── FabricaEtapas             registro único: grafo de transiciones + catálogo
├── comandos                      ── Command + Memento
│   ├── Comando                   interfaz: ejecutar, deshacer, auditoria
│   ├── ComandoAvanzarEtapa
│   ├── CandidatoMemento
│   └── HistorialComandos         pila de deshacer + bitácora de auditoría
├── gestor
│   ├── GestorDeCandidato         coordinador; no conoce etapas, destinatarios ni mementos
│   └── TransicionInvalidaException
└── notificaciones                ── Observer + Strategy
    ├── PublicadorEventos
    ├── EventoCambioEtapa
    ├── ObservadorCandidato       interfaz
    ├── Observador*               los cuatro destinatarios, cada uno con su filtro
    ├── CanalNotificacion         interfaz
    └── CanalEmail, CanalPortal
```

---

## Qué demuestra la ejecución

`Main` recorre el proceso completo de Ana Torres:

1. Aplicado → Entrevista → Prueba Técnica.
2. Intento de saltar a Contratado: **rechazado** con `TransicionInvalidaException`.
3. Avance a Oferta, que dispara además la notificación al gerente.
4. **Deshacer**: Ana vuelve a Prueba Técnica, y el deshacer queda anotado en la bitácora.
5. Se rehace el avance y se completa hasta Contratado, pasando por Verificación de Referencias — al llegar a Contratado se disparan los cuatro observadores, nómina incluida.
6. Un segundo candidato, Bruno Díaz, es rechazado **como nota interna**: el reclutador recibe su correo y el portal del candidato no recibe nada.
7. Se imprime la bitácora de auditoría completa: quién hizo cada cambio y cuándo.
