-- =====================================================================================
-- SIGECE - V3
-- Autores : Hector Julio Hernandez Tacas, Alex Ayamamani
-- Ultima actualizacion: 25/10/2026
-- =====================================================================================
CREATE EXTENSION IF NOT EXISTS pgcrypto;

-- =====================================================================================
-- 1. ROLES
-- Descripción: Catálogo maestro de perfiles de usuario del sistema (ADMINISTRADOR,
--              TUTOR, PSICOLOGO). Define los niveles de autorización y delimita las
--              operaciones y módulos accesibles para cada actor. Respalda CU01
--              (registrar usuarios y roles) y el control de acceso basado en roles.
-- =====================================================================================
CREATE TABLE roles (
    id              BIGSERIAL PRIMARY KEY,
    nombre          VARCHAR(50)  NOT NULL UNIQUE,
    descripcion     VARCHAR(255),
    created_at      TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT clock_timestamp(),
    updated_at      TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT clock_timestamp(),

    CONSTRAINT chk_roles_nombre_formato CHECK (length(trim(nombre)) >= 3)
);


-- =====================================================================================
-- 2. USUARIOS
-- Descripción: Repositorio central de cuentas del personal institucional autorizado
--              (administrador, tutor, psicólogo) con sus credenciales seguras, datos
--              de contacto, rol y estado operativo. Controla la autenticación, los
--              bloqueos por intentos fallidos y la trazabilidad de auditoría. Respalda
--              CU01 (registrar usuarios), CU02 (consultar) y CU03 (cambiar estado).
-- =====================================================================================
CREATE TABLE usuarios (
    id                          BIGSERIAL PRIMARY KEY,
    nombres                     VARCHAR(100) NOT NULL,
    apellidos                   VARCHAR(100) NOT NULL,
    dni                         VARCHAR(15)  NOT NULL UNIQUE,
    correo                      VARCHAR(150) NOT NULL UNIQUE,
    username                    VARCHAR(50)  NOT NULL UNIQUE,
    password_hash               VARCHAR(255) NOT NULL,
    telefono                    VARCHAR(20),
    role_id                     BIGINT       NOT NULL,
    activo                      BOOLEAN      NOT NULL DEFAULT TRUE,
    ultimo_acceso               TIMESTAMP WITH TIME ZONE,
    intentos_fallidos           INTEGER      NOT NULL DEFAULT 0,
    bloqueado_hasta             TIMESTAMP WITH TIME ZONE,
    password_actualizado_at     TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT clock_timestamp(),
    debe_cambiar_password       BOOLEAN      NOT NULL DEFAULT FALSE,
    created_at                  TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT clock_timestamp(),
    updated_at                  TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT clock_timestamp(),

    CONSTRAINT fk_usuarios_roles FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE RESTRICT,
    CONSTRAINT chk_usuarios_dni CHECK (dni ~ '^[0-9]{8,15}$'),
    CONSTRAINT chk_usuarios_correo CHECK (correo ~* '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$'),
    CONSTRAINT chk_usuarios_intentos CHECK (intentos_fallidos >= 0)
);
CREATE INDEX idx_usuarios_role_id ON usuarios(role_id);
CREATE INDEX idx_usuarios_correo  ON usuarios(correo);
CREATE INDEX idx_usuarios_activo  ON usuarios(activo);


-- =====================================================================================
-- 3. TUTOR_AULA
-- Descripción: Tabla asociativa que asigna a cada docente con rol TUTOR las aulas
--              (nivel, grado y sección) a su cargo por cada año escolar. Aplica la
--              regla RN04: el tutor únicamente consulta estudiantes e incidencias
--              pertenecientes a sus aulas asignadas. Respalda CU05 y CU11.
-- =====================================================================================
CREATE TABLE tutor_aula (
    id                  BIGSERIAL PRIMARY KEY,
    usuario_id          BIGINT       NOT NULL,
    nivel               VARCHAR(20)  NOT NULL,
    grado               VARCHAR(20)  NOT NULL,
    seccion             VARCHAR(10)  NOT NULL,
    anio_escolar        INTEGER      NOT NULL,
    activo              BOOLEAN      NOT NULL DEFAULT TRUE,
    created_at          TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT clock_timestamp(),

    CONSTRAINT fk_tutor_aula_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE RESTRICT,
    CONSTRAINT chk_tutor_aula_nivel CHECK (nivel IN ('INICIAL', 'PRIMARIA', 'SECUNDARIA')),
    CONSTRAINT chk_tutor_aula_anio CHECK (anio_escolar BETWEEN 2000 AND 2100),
    CONSTRAINT uq_tutor_aula UNIQUE (usuario_id, nivel, grado, seccion, anio_escolar)
);
CREATE INDEX idx_tutor_aula_usuario ON tutor_aula(usuario_id) WHERE activo = TRUE;
CREATE INDEX idx_tutor_aula_aula    ON tutor_aula(nivel, grado, seccion);


-- =====================================================================================
-- 4. ESTUDIANTES
-- Descripción: Padrón de alumnos matriculados en la institución educativa con su
--              código oficial SIAGIE, datos personales y ubicación escolar (nivel,
--              grado y sección). Constituye la entidad base para el seguimiento
--              longitudinal de casos. Respalda CU04 (registrar) y CU05 (consultar).
-- =====================================================================================
CREATE TABLE estudiantes (
    id                  BIGSERIAL PRIMARY KEY,
    codigo_siagie       VARCHAR(20)  NOT NULL UNIQUE,
    nombres             VARCHAR(100) NOT NULL,
    apellidos           VARCHAR(100) NOT NULL,
    fecha_nacimiento    DATE         NOT NULL,
    nivel               VARCHAR(20)  NOT NULL,
    grado               VARCHAR(20)  NOT NULL,
    seccion             VARCHAR(10)  NOT NULL,
    activo              BOOLEAN      NOT NULL DEFAULT TRUE,
    created_at          TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT clock_timestamp(),
    updated_at          TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT clock_timestamp(),

    CONSTRAINT chk_estudiantes_nivel CHECK (nivel IN ('INICIAL', 'PRIMARIA', 'SECUNDARIA')),
    CONSTRAINT chk_estudiantes_fecha_nac CHECK (fecha_nacimiento < CURRENT_DATE)
);
CREATE INDEX idx_estudiantes_grado_seccion ON estudiantes(grado, seccion);
CREATE INDEX idx_estudiantes_nivel         ON estudiantes(nivel);
CREATE INDEX idx_estudiantes_codigo_siagie ON estudiantes(codigo_siagie);


-- =====================================================================================
-- 5. APODERADOS
-- Descripción: Directorio de padres de familia o apoderados legales de los estudiantes.
--              Registra sus documentos de identidad y números telefónicos como canal
--              destino de las comunicaciones oficiales por WhatsApp. No son usuarios
--              directos del sistema. Respalda CU06 (registrar) y CU07 (consultar).
-- =====================================================================================
CREATE TABLE apoderados (
    id                  BIGSERIAL PRIMARY KEY,
    nombres             VARCHAR(100) NOT NULL,
    apellidos           VARCHAR(100) NOT NULL,
    tipo_documento      VARCHAR(10)  NOT NULL DEFAULT 'DNI',
    numero_documento    VARCHAR(15)  NOT NULL UNIQUE,
    telefono            VARCHAR(20)  NOT NULL,
    correo              VARCHAR(150),
    created_at          TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT clock_timestamp(),
    updated_at          TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT clock_timestamp(),

    CONSTRAINT chk_apoderados_tipo_doc CHECK (tipo_documento IN ('DNI', 'CE', 'PASAPORTE')),
    CONSTRAINT chk_apoderados_num_doc CHECK (length(trim(numero_documento)) >= 8)
);
CREATE INDEX idx_apoderados_documento ON apoderados(numero_documento);


-- =====================================================================================
-- 6. ESTUDIANTE_APODERADO
-- Descripción: Tabla asociativa que vincula a los estudiantes con sus apoderados,
--              especificando el parentesco legal y designando al contacto principal
--              exclusivo para la entrega automatizada de notificaciones y citaciones.
--              Respalda CU06 (registrar apoderados), CU07 y CU17 (enviar notificación).
-- =====================================================================================
CREATE TABLE estudiante_apoderado (
    id                      BIGSERIAL PRIMARY KEY,
    estudiante_id           BIGINT NOT NULL,
    apoderado_id            BIGINT NOT NULL,
    parentesco              VARCHAR(30) NOT NULL,
    es_contacto_principal   BOOLEAN NOT NULL DEFAULT FALSE,
    created_at              TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT clock_timestamp(),

    CONSTRAINT fk_est_apod_estudiante FOREIGN KEY (estudiante_id) REFERENCES estudiantes(id) ON DELETE CASCADE,
    CONSTRAINT fk_est_apod_apoderado FOREIGN KEY (apoderado_id) REFERENCES apoderados(id) ON DELETE CASCADE,
    CONSTRAINT uq_estudiante_apoderado UNIQUE (estudiante_id, apoderado_id),
    CONSTRAINT chk_est_apod_parentesco CHECK (parentesco IN ('PADRE', 'MADRE', 'TIO','TIA','ABUELO','ABUELA' ,'TUTOR_LEGAL', 'APODERADO'))
);

CREATE INDEX idx_est_apod_estudiante ON estudiante_apoderado(estudiante_id);
CREATE INDEX idx_est_apod_apoderado  ON estudiante_apoderado(apoderado_id);

CREATE UNIQUE INDEX uq_est_apod_contacto_principal
    ON estudiante_apoderado (estudiante_id)
    WHERE es_contacto_principal = TRUE;


-- =====================================================================================
-- 7. TIPOS_INCIDENCIA
-- Descripción: Catálogo maestro y parametrizable de faltas a la convivencia escolar,
--              homologado con la taxonomía oficial del portal SíseVe y su nivel de
--              gravedad base (LEVE, MODERADO, GRAVE, MUY_GRAVE). Aplica la regla RN01
--              para determinar la obligatoriedad de la notificación. Respalda CU08 y CU09.
-- =====================================================================================
CREATE TABLE tipos_incidencia (
    id                  BIGSERIAL PRIMARY KEY,
    nombre              VARCHAR(100) NOT NULL UNIQUE,
    categoria_siseve    VARCHAR(50)  NOT NULL, -- Convertir en un enum
    gravedad_base       VARCHAR(20)  NOT NULL DEFAULT 'LEVE',
    descripcion         VARCHAR(255),
    activo              BOOLEAN      NOT NULL DEFAULT TRUE,

    CONSTRAINT chk_tipos_incidencia_gravedad CHECK (gravedad_base IN ('LEVE', 'MODERADO', 'GRAVE', 'MUY_GRAVE'))
);


-- =====================================================================================
-- 8. INCIDENTES
-- Descripción: Expediente digital central de cada hecho o conflicto de convivencia.
--              Registra el código correlativo formal, gravedad, descripción, lugar,
--              medio de ocurrencia (presencial o digital), usuario reportante y
--              responsable asignado. Gestiona el ciclo de vida del caso, la resolución
--              técnica por psicología, el cierre formal administrativo con acta (RN06)
--              y las derivaciones a SíseVe. Respalda CU10, CU11, CU13, CU19, CU20, CU21,
--              las reglas RN01, RN03, RN06, RN07 y los objetivos ON01 y ON03.
-- =====================================================================================
CREATE TABLE incidentes (
    id                          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    codigo_caso                 VARCHAR(30)  NOT NULL UNIQUE,
    tipo_incidencia_id          BIGINT       NOT NULL,
    gravedad                    VARCHAR(20)  NOT NULL,
    descripcion                 TEXT         NOT NULL,
    lugar_ocurrencia            VARCHAR(150) NOT NULL,
    medio_ocurrencia            VARCHAR(20)  NOT NULL DEFAULT 'PRESENCIAL',
    fecha_ocurrencia            TIMESTAMP WITH TIME ZONE NOT NULL,
    fecha_registro              TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT clock_timestamp(),
    estado                      VARCHAR(25)  NOT NULL DEFAULT 'REGISTRADO',

    reportado_por_id            BIGINT       NOT NULL,
    responsable_actual_id       BIGINT       NOT NULL,
    notificar_apoderado         BOOLEAN      NOT NULL DEFAULT FALSE,

    fecha_resolucion            TIMESTAMP WITH TIME ZONE,
    resuelto_por_id             BIGINT,

    acta_cierre                 TEXT,
    conclusiones_finales        TEXT,
    fecha_cierre                TIMESTAMP WITH TIME ZONE,
    cerrado_por_id              BIGINT,

    reportado_siseve            BOOLEAN      NOT NULL DEFAULT FALSE,
    fecha_reporte_siseve        TIMESTAMP WITH TIME ZONE,
    codigo_siseve               VARCHAR(50),
    observaciones_siseve        TEXT,
    derivado_por_id             BIGINT,

    created_at                  TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT clock_timestamp(),
    updated_at                  TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT clock_timestamp(),

    CONSTRAINT fk_incidentes_tipo FOREIGN KEY (tipo_incidencia_id) REFERENCES tipos_incidencia(id) ON DELETE RESTRICT,
    CONSTRAINT fk_incidentes_reportado_por FOREIGN KEY (reportado_por_id) REFERENCES usuarios(id) ON DELETE RESTRICT,
    CONSTRAINT fk_incidentes_responsable FOREIGN KEY (responsable_actual_id) REFERENCES usuarios(id) ON DELETE RESTRICT,
    CONSTRAINT fk_incidentes_resuelto_por FOREIGN KEY (resuelto_por_id) REFERENCES usuarios(id) ON DELETE RESTRICT,
    CONSTRAINT fk_incidentes_cerrado_por FOREIGN KEY (cerrado_por_id) REFERENCES usuarios(id) ON DELETE RESTRICT,
    CONSTRAINT fk_incidentes_derivado_por FOREIGN KEY (derivado_por_id) REFERENCES usuarios(id) ON DELETE RESTRICT,

    CONSTRAINT chk_incidentes_codigo_formato CHECK (codigo_caso ~ '^INC-[0-9]{4}-[0-9]{6}$'),
    CONSTRAINT chk_incidentes_gravedad CHECK (gravedad IN ('LEVE', 'MODERADO', 'GRAVE', 'MUY_GRAVE')),
    CONSTRAINT chk_incidentes_medio CHECK (medio_ocurrencia IN ('PRESENCIAL', 'DIGITAL')),
    CONSTRAINT chk_incidentes_estado CHECK (estado IN ('REGISTRADO', 'EN_INVESTIGACION', 'EN_SEGUIMIENTO', 'RESUELTO', 'CERRADO')),

    CONSTRAINT chk_incidentes_resolucion_completa CHECK (
        estado NOT IN ('RESUELTO', 'CERRADO')
        OR (fecha_resolucion IS NOT NULL AND resuelto_por_id IS NOT NULL)),
    CONSTRAINT chk_incidentes_fecha_resolucion CHECK (fecha_resolucion IS NULL OR fecha_resolucion >= fecha_registro),

    CONSTRAINT chk_incidentes_cierre_completo CHECK (
        estado <> 'CERRADO'
        OR (acta_cierre IS NOT NULL AND fecha_cierre IS NOT NULL AND cerrado_por_id IS NOT NULL)),
    CONSTRAINT chk_incidentes_acta_longitud CHECK (acta_cierre IS NULL OR length(trim(acta_cierre)) >= 10),
    CONSTRAINT chk_incidentes_fecha_cierre CHECK (fecha_cierre IS NULL OR fecha_cierre >= fecha_registro),

    CONSTRAINT chk_incidentes_derivacion_coherente CHECK (
        (reportado_siseve = FALSE
            AND fecha_reporte_siseve IS NULL
            AND codigo_siseve IS NULL
            AND derivado_por_id IS NULL)
        OR
        (reportado_siseve = TRUE
            AND fecha_reporte_siseve IS NOT NULL
            AND codigo_siseve IS NOT NULL
            AND derivado_por_id IS NOT NULL))
);
CREATE INDEX idx_incidentes_estado            ON incidentes(estado);
CREATE INDEX idx_incidentes_tipo              ON incidentes(tipo_incidencia_id);
CREATE INDEX idx_incidentes_fecha_ocurrencia  ON incidentes(fecha_ocurrencia);
CREATE INDEX idx_incidentes_fecha_registro    ON incidentes(fecha_registro);
CREATE INDEX idx_incidentes_gravedad          ON incidentes(gravedad);
CREATE INDEX idx_incidentes_reportado_por     ON incidentes(reportado_por_id);
CREATE INDEX idx_incidentes_responsable       ON incidentes(responsable_actual_id);
CREATE INDEX idx_incidentes_siseve            ON incidentes(reportado_siseve);

-- =====================================================================================
-- 9. CONTADORES_INCIDENTE
-- Descripción: Mecanismo de persistencia de control transaccional para la generación
--              atómica de la secuencia numérica correlativa anual de los códigos de
--              caso (INC-YYYY-NNNNNN). Previene condiciones de carrera y claves
--              duplicadas ante registros concurrentes. Da soporte a CU10.
-- =====================================================================================
CREATE TABLE contadores_incidente (
    anio            INTEGER PRIMARY KEY,
    ultimo_numero   INTEGER NOT NULL DEFAULT 0,
    CONSTRAINT chk_contadores_anio CHECK (anio BETWEEN 2000 AND 2100)
);


-- =====================================================================================
-- 10. INCIDENTE_INVOLUCRADOS
-- Descripción: Registro de participantes implicados en un incidente (estudiantes o
--              usuarios institucionales) con su rol específico en el suceso (VICTIMA,
--              AGRESOR, TESTIGO). Aplica la regla RN07 de obligatoriedad de actores al
--              aperturar el expediente y permite el análisis de reincidencias (CU16).
--              Respalda CU12 (registrar involucrados) y CU16 (consultar historial).
-- =====================================================================================
CREATE TABLE incidente_involucrados (
    id                  BIGSERIAL PRIMARY KEY,
    incidente_id        UUID         NOT NULL,
    estudiante_id       BIGINT,
    usuario_id          BIGINT,
    rol_involucrado     VARCHAR(20)  NOT NULL,
    observaciones       VARCHAR(255),
    created_at          TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT clock_timestamp(),

    CONSTRAINT fk_involucrados_incidente FOREIGN KEY (incidente_id) REFERENCES incidentes(id) ON DELETE CASCADE,
    CONSTRAINT fk_involucrados_estudiante FOREIGN KEY (estudiante_id) REFERENCES estudiantes(id) ON DELETE CASCADE,
    CONSTRAINT fk_involucrados_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE,
    CONSTRAINT chk_involucrados_rol CHECK (rol_involucrado IN ('VICTIMA', 'AGRESOR', 'TESTIGO')),
    CONSTRAINT chk_involucrado_entidad_exclusiva CHECK (
        (estudiante_id IS NOT NULL AND usuario_id IS NULL)
        OR
        (estudiante_id IS NULL AND usuario_id IS NOT NULL))
);
CREATE INDEX idx_involucrados_incidente  ON incidente_involucrados(incidente_id);
CREATE INDEX idx_involucrados_estudiante ON incidente_involucrados(estudiante_id);
CREATE INDEX idx_involucrados_usuario    ON incidente_involucrados(usuario_id);
CREATE UNIQUE INDEX uq_involucrado_estudiante
    ON incidente_involucrados (incidente_id, estudiante_id, rol_involucrado)
    WHERE estudiante_id IS NOT NULL;
CREATE UNIQUE INDEX uq_involucrado_usuario
    ON incidente_involucrados (incidente_id, usuario_id, rol_involucrado)
    WHERE usuario_id IS NOT NULL;


-- =====================================================================================
-- 11. ACCIONES_SEGUIMIENTO
-- Descripción: Bitácora de intervenciones pedagógicas, disciplinarias y psicológicas
--              registradas sobre una incidencia. Aplica la regla RN02: citaciones y
--              entrevistas activan notificación automática al apoderado, mientras que
--              apuntes internos no notifican. Constituye el requisito previo para que
--              el psicólogo marque el caso como resuelto. Respalda CU15, CU16 y RN06.
-- =====================================================================================
CREATE TABLE acciones_seguimiento (
    id                          BIGSERIAL PRIMARY KEY,
    incidente_id                UUID         NOT NULL,
    usuario_id                  BIGINT       NOT NULL,
    tipo_accion                 VARCHAR(50)  NOT NULL,
    descripcion                 TEXT         NOT NULL,
    fecha_accion                TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT clock_timestamp(),
    fecha_programada            TIMESTAMP WITH TIME ZONE,
    fecha_proxima_revision      DATE,
    estado_accion               VARCHAR(20)  NOT NULL DEFAULT 'PENDIENTE',
    fecha_completada            TIMESTAMP WITH TIME ZONE,
    created_at                  TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT clock_timestamp(),
    updated_at                  TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT clock_timestamp(),

                                      CONSTRAINT fk_acciones_incidente FOREIGN KEY (incidente_id) REFERENCES incidentes(id) ON DELETE CASCADE,
                                      CONSTRAINT fk_acciones_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE RESTRICT,
                                      CONSTRAINT chk_acciones_tipo CHECK (tipo_accion IN (
                                                                                          'ENTREVISTA_ESTUDIANTE', 'ENTREVISTA_PADRES', 'CITACION_APODERADO',
                                                                                          'DERIVACION_PSICOLOGIA', 'MEDIDA_CORRECTIVA_FORMATIVA', 'DERIVACION_DEMUNA_CEM',
                                                                                          'ACTA_COMPROMISO', 'APUNTE_INTERNO', 'OTRO')),
                                      CONSTRAINT chk_acciones_estado CHECK (estado_accion IN ('PENDIENTE', 'EN_PROCESO', 'COMPLETADA')),
                                      CONSTRAINT chk_acciones_completada CHECK ((estado_accion = 'COMPLETADA') = (fecha_completada IS NOT NULL)),
                                      CONSTRAINT chk_acciones_fecha_programada CHECK (
                                          tipo_accion NOT IN ('CITACION_APODERADO', 'ENTREVISTA_PADRES', 'ENTREVISTA_ESTUDIANTE')
                                              OR fecha_programada IS NOT NULL)
);

CREATE INDEX idx_acciones_incidente ON acciones_seguimiento(incidente_id);
CREATE INDEX idx_acciones_usuario   ON acciones_seguimiento(usuario_id);
CREATE INDEX idx_acciones_estado    ON acciones_seguimiento(estado_accion);
CREATE INDEX idx_acciones_revision_pendiente
    ON acciones_seguimiento(fecha_proxima_revision)
    WHERE estado_accion <> 'COMPLETADA' AND fecha_proxima_revision IS NOT NULL;


-- =====================================================================================
-- 12. INCIDENTE_ADJUNTOS
-- Descripción: Repositorio de evidencias digitales (fotografías, documentos, audios o
--              actas escaneadas) vinculadas a un caso o a una acción de seguimiento.
--              Almacena el hash criptográfico SHA-256 para preservar la cadena de
--              custodia y soporta borrado lógico auditable. Respalda CU14 (evidencias).
-- =====================================================================================
CREATE TABLE incidente_adjuntos (
    id                      BIGSERIAL PRIMARY KEY,
    incidente_id            UUID         NOT NULL,
    accion_id               BIGINT,
    subido_por_id           BIGINT       NOT NULL,
    nombre_original         VARCHAR(255) NOT NULL,
    nombre_almacenamiento   VARCHAR(255) NOT NULL UNIQUE,
    descripcion             VARCHAR(500) NOT NULL,
    tipo_mime               VARCHAR(100) NOT NULL,
    tamano_bytes            BIGINT       NOT NULL,
    url_storage             TEXT         NOT NULL,
    hash_sha256             VARCHAR(64)  NOT NULL,

    activo                  BOOLEAN      NOT NULL DEFAULT TRUE,
    fecha_eliminacion       TIMESTAMP WITH TIME ZONE,
    eliminado_por_id        BIGINT,
    motivo_eliminacion      VARCHAR(255),

    created_at              TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT clock_timestamp(),
    updated_at              TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT clock_timestamp(),

                                    CONSTRAINT fk_adjuntos_incidente FOREIGN KEY (incidente_id) REFERENCES incidentes(id) ON DELETE CASCADE,
                                    CONSTRAINT fk_adjuntos_accion FOREIGN KEY (accion_id) REFERENCES acciones_seguimiento(id) ON DELETE CASCADE,
                                    CONSTRAINT fk_adjuntos_usuario FOREIGN KEY (subido_por_id) REFERENCES usuarios(id) ON DELETE RESTRICT,
                                    CONSTRAINT fk_adjuntos_eliminado_por FOREIGN KEY (eliminado_por_id) REFERENCES usuarios(id) ON DELETE RESTRICT,
                                    CONSTRAINT chk_adjuntos_tamano CHECK (tamano_bytes > 0 AND tamano_bytes <= 10485760),
                                    CONSTRAINT chk_adjuntos_hash CHECK (hash_sha256 ~* '^[0-9a-f]{64}$'),
    CONSTRAINT chk_adjuntos_descripcion CHECK (length(trim(descripcion)) >= 3),
    CONSTRAINT chk_adjuntos_eliminacion_coherente CHECK (
        (activo = TRUE
            AND fecha_eliminacion IS NULL
            AND eliminado_por_id IS NULL
            AND motivo_eliminacion IS NULL)
        OR
        (activo = FALSE
            AND fecha_eliminacion IS NOT NULL
            AND eliminado_por_id IS NOT NULL
            AND motivo_eliminacion IS NOT NULL))
);

CREATE INDEX idx_adjuntos_incidente ON incidente_adjuntos(incidente_id);
CREATE INDEX idx_adjuntos_accion    ON incidente_adjuntos(accion_id);
CREATE INDEX idx_adjuntos_activo    ON incidente_adjuntos(incidente_id) WHERE activo = TRUE;
CREATE UNIQUE INDEX uq_adjuntos_incidente_hash ON incidente_adjuntos (incidente_id, hash_sha256)
    WHERE activo = TRUE;


-- =====================================================================================
-- 13. PLANTILLAS_MENSAJE
-- Descripción: Catálogo de plantillas oficiales de mensajería aprobadas para el canal
--              de WhatsApp Business Platform (AVISO_INCIDENCIA, CITACION, ENTREVISTA,
--              CIERRE_CASO). Garantiza redacciones estandarizadas y la protección de
--              datos sensibles de menores. Respalda CU17 y la regla RN03.
-- =====================================================================================
CREATE TABLE plantillas_mensaje (
    id                  BIGSERIAL PRIMARY KEY,
    codigo              VARCHAR(30)  NOT NULL UNIQUE,
    nombre_whatsapp     VARCHAR(100) NOT NULL,
    contenido           TEXT         NOT NULL,
    activo              BOOLEAN      NOT NULL DEFAULT TRUE,
    created_at          TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT clock_timestamp(),
    updated_at          TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT clock_timestamp(),

    CONSTRAINT chk_plantillas_codigo CHECK (codigo IN ('AVISO_INCIDENCIA', 'CITACION', 'ENTREVISTA', 'CIERRE_CASO')),
    CONSTRAINT chk_plantillas_contenido CHECK (length(trim(contenido)) >= 10)
);

-- =====================================================================================
-- 14. NOTIFICACIONES
-- Descripción: Bitácora transaccional y de auditoría de los mensajes enviados a los
--              apoderados por el Servicio de Mensajería. Registra estudiante, apoderado,
--              plantilla, canal (WHATSAPP), contenido, marcas de tiempo de envío y
--              entrega, estados (PENDIENTE, ENVIADO, ENTREGADO, LEIDO, FALLIDO) y fallos.
--              Respalda CU17, CU18 y sustenta la medición del objetivo ON02 (< 5 min).
-- =====================================================================================
CREATE TABLE notificaciones (
    id                      BIGSERIAL PRIMARY KEY,
    incidente_id            UUID         NOT NULL,
    estudiante_id           BIGINT       NOT NULL,
    apoderado_id            BIGINT ,
    accion_id               BIGINT,
    plantilla_id            BIGINT       NOT NULL,
    enviado_por_id          BIGINT,
    tipo_notificacion       VARCHAR(30)  NOT NULL,
    origen                  VARCHAR(15)  NOT NULL DEFAULT 'AUTOMATICA',
    canal                   VARCHAR(20)  NOT NULL DEFAULT 'WHATSAPP',
    destino                 VARCHAR(150),
    asunto                  VARCHAR(150),
    mensaje                 TEXT         NOT NULL,
    fecha_envio             TIMESTAMP WITH TIME ZONE,
    fecha_entrega           TIMESTAMP WITH TIME ZONE,
    estado_envio            VARCHAR(20)  NOT NULL DEFAULT 'PENDIENTE',
    intentos_envio          INTEGER      NOT NULL DEFAULT 0,
    detalle_resultado       VARCHAR(500),
    id_transaccion_externo  VARCHAR(150),
    created_at              TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT clock_timestamp(),

    updated_at              TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT clock_timestamp(),

                                CONSTRAINT fk_notif_incidente FOREIGN KEY (incidente_id) REFERENCES incidentes(id) ON DELETE CASCADE,
                                CONSTRAINT fk_notif_estudiante FOREIGN KEY (estudiante_id) REFERENCES estudiantes(id) ON DELETE CASCADE,
                                CONSTRAINT fk_notif_apoderado FOREIGN KEY (apoderado_id) REFERENCES apoderados(id) ON DELETE RESTRICT,
                                CONSTRAINT fk_notif_accion FOREIGN KEY (accion_id) REFERENCES acciones_seguimiento(id) ON DELETE SET NULL,
                                CONSTRAINT fk_notif_plantilla FOREIGN KEY (plantilla_id) REFERENCES plantillas_mensaje(id) ON DELETE RESTRICT,
                                CONSTRAINT fk_notif_enviado_por FOREIGN KEY (enviado_por_id) REFERENCES usuarios(id) ON DELETE RESTRICT,
                                CONSTRAINT chk_notif_tipo CHECK (tipo_notificacion IN ('AVISO_INCIDENCIA', 'CITACION', 'ENTREVISTA', 'CIERRE_CASO')),
                                CONSTRAINT chk_notif_origen CHECK (origen IN ('AUTOMATICA', 'MANUAL')),
                                CONSTRAINT chk_notif_canal CHECK (canal IN ('WHATSAPP')),
                                CONSTRAINT chk_notif_estado CHECK (estado_envio IN ('PENDIENTE', 'ENVIADO', 'ENTREGADO', 'FALLIDO', 'LEIDO')),
                                CONSTRAINT chk_notif_intentos CHECK (intentos_envio >= 0),
                                CONSTRAINT chk_notif_apoderado_estado CHECK (apoderado_id IS NOT NULL OR estado_envio = 'FALLIDO'),
                                CONSTRAINT chk_notif_detalle_obligatorio CHECK (estado_envio <> 'FALLIDO' OR detalle_resultado IS NOT NULL),
                                CONSTRAINT chk_notif_fecha_envio CHECK (estado_envio IN ('PENDIENTE', 'FALLIDO') OR fecha_envio IS NOT NULL),
                                CONSTRAINT chk_notif_fecha_entrega CHECK (estado_envio <> 'ENTREGADO' OR fecha_entrega IS NOT NULL),
                                CONSTRAINT chk_notif_orden_fechas CHECK (fecha_entrega IS NULL OR fecha_envio IS NULL OR fecha_entrega >= fecha_envio),
                                CONSTRAINT chk_notif_accion_requerida CHECK (
                                    tipo_notificacion NOT IN ('CITACION', 'ENTREVISTA') OR accion_id IS NOT NULL),
                                CONSTRAINT chk_notif_manual_usuario CHECK (origen <> 'MANUAL' OR enviado_por_id IS NOT NULL)
);
CREATE INDEX idx_notif_incidente ON notificaciones(incidente_id);
CREATE INDEX idx_notif_estudiante ON notificaciones(estudiante_id);
CREATE INDEX idx_notif_apoderado ON notificaciones(apoderado_id);
CREATE INDEX idx_notif_estado    ON notificaciones(estado_envio);
CREATE INDEX idx_notif_tipo      ON notificaciones(tipo_notificacion);
CREATE INDEX idx_notif_accion    ON notificaciones(accion_id) WHERE accion_id IS NOT NULL;
CREATE INDEX idx_notif_created   ON notificaciones(created_at DESC);


-- =====================================================================================
-- 15. REPORTES_GENERADOS
-- Descripción: Repositorio histórico y de control de los informes estadísticos y de
--              indicadores solicitados por la dirección y administración. Almacena
--              filtros aplicados (JSONB), resultados de indicadores calculados (JSONB),
--              formato (PDF o EXCEL) y URL del archivo. Respalda CU22 y la auditoría
--              del cumplimiento del objetivo ON03 (informes emitidos en < 48 horas).
-- =====================================================================================
CREATE TABLE reportes_generados (
    id                  BIGSERIAL PRIMARY KEY,
    tipo_reporte        VARCHAR(50) NOT NULL,
    fecha_inicio        DATE NOT NULL,
    fecha_fin           DATE NOT NULL,
    filtros             JSONB,
    indicadores         JSONB,
    formato             VARCHAR(10),
    archivo_url         TEXT,
    generado_por_id     BIGINT NOT NULL,
    fecha_generacion    TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT clock_timestamp(),

    CONSTRAINT fk_reporte_usuario
        FOREIGN KEY (generado_por_id)
        REFERENCES usuarios(id)
        ON DELETE RESTRICT,

    CONSTRAINT chk_reporte_formato
        CHECK (formato IS NULL OR formato IN ('PDF', 'EXCEL')),

    CONSTRAINT chk_reporte_fechas
        CHECK (fecha_inicio <= fecha_fin),

    CONSTRAINT chk_reporte_tipo
        CHECK (tipo_reporte IN (
            'INCIDENCIAS_POR_PERIODO',
            'INCIDENCIAS_POR_TIPO',
            'INCIDENCIAS_POR_GRAVEDAD',
            'INCIDENCIAS_POR_GRADO',
            'INCIDENCIAS_POR_SECCION',
            'INCIDENCIAS_POR_ESTADO',
            'INCIDENCIAS_POR_RESPONSABLE',
            'INDICADORES_ON01',
            'INDICADORES_ON02',
            'INDICADORES_ON03'
        ))
);
CREATE INDEX idx_reportes_usuario ON reportes_generados(generado_por_id);
CREATE INDEX idx_reportes_fecha   ON reportes_generados(fecha_generacion DESC);
CREATE INDEX idx_reportes_tipo    ON reportes_generados(tipo_reporte);