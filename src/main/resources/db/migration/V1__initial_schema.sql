--base de datos actualizada
CREATE EXTENSION IF NOT EXISTS pgcrypto;

-- =====================================================================================
-- 1. ROLES
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
-- 3. ESTUDIANTES
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
-- 4. APODERADOS
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
-- 5. ESTUDIANTE_APODERADO
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
-- 6. TIPOS_INCIDENCIA
-- =====================================================================================
CREATE TABLE tipos_incidencia (
    id                  BIGSERIAL PRIMARY KEY,
    nombre              VARCHAR(100) NOT NULL UNIQUE,
    categoria_siseve    VARCHAR(50)  NOT NULL,
    descripcion         VARCHAR(255),
    activo              BOOLEAN      NOT NULL DEFAULT TRUE
);

-- =====================================================================================
-- 7. INCIDENTES
-- =====================================================================================
CREATE TABLE incidentes (
    id                          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    codigo_caso                 VARCHAR(30)  NOT NULL UNIQUE,
    tipo_incidencia_id          BIGINT       NOT NULL,
    gravedad                    VARCHAR(20)  NOT NULL,
    descripcion                 TEXT         NOT NULL,
    lugar_ocurrencia            VARCHAR(150) NOT NULL,
    fecha_ocurrencia            TIMESTAMP WITH TIME ZONE NOT NULL,
    fecha_registro              TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT clock_timestamp(),
    estado                      VARCHAR(25)  NOT NULL DEFAULT 'REGISTRADO',

    reportado_por_id            BIGINT       NOT NULL,
    responsable_actual_id       BIGINT,
    reincidencia                BOOLEAN      NOT NULL DEFAULT FALSE,

    acta_cierre                 TEXT,
    fecha_cierre                TIMESTAMP WITH TIME ZONE,
    cerrado_por_id              BIGINT,

    reportado_siseve            BOOLEAN      NOT NULL DEFAULT FALSE,
    estado_derivacion           VARCHAR(20)  NOT NULL DEFAULT 'NO_DERIVADA',
    modo_derivacion             VARCHAR(20),
    fecha_reporte_siseve        TIMESTAMP WITH TIME ZONE,
    codigo_siseve               VARCHAR(50),
    observaciones_siseve        TEXT,
    derivado_por_id             BIGINT,
    error_integracion_siseve    VARCHAR(500),

    created_at                  TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT clock_timestamp(),
    updated_at                  TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT clock_timestamp(),

    CONSTRAINT fk_incidentes_tipo FOREIGN KEY (tipo_incidencia_id) REFERENCES tipos_incidencia(id) ON DELETE RESTRICT,
    CONSTRAINT fk_incidentes_reportado_por FOREIGN KEY (reportado_por_id) REFERENCES usuarios(id) ON DELETE RESTRICT,
    CONSTRAINT fk_incidentes_responsable FOREIGN KEY (responsable_actual_id) REFERENCES usuarios(id) ON DELETE SET NULL,
    CONSTRAINT fk_incidentes_cerrado_por FOREIGN KEY (cerrado_por_id) REFERENCES usuarios(id) ON DELETE RESTRICT,
    CONSTRAINT fk_incidentes_derivado_por FOREIGN KEY (derivado_por_id) REFERENCES usuarios(id) ON DELETE RESTRICT,

    CONSTRAINT chk_incidentes_codigo_formato CHECK (codigo_caso ~ '^INC-[0-9]{4}-[0-9]{6}$'),
    CONSTRAINT chk_incidentes_gravedad CHECK (gravedad IN ('LEVE', 'MODERADA', 'GRAVE', 'MUY_GRAVE')),
    CONSTRAINT chk_incidentes_estado CHECK (estado IN ('REGISTRADO', 'EN_INVESTIGACION', 'EN_SEGUIMIENTO', 'RESUELTO', 'DERIVADO_SISEVE', 'CERRADO')),

    CONSTRAINT chk_incidentes_cierre_completo CHECK (
        estado <> 'CERRADO'
        OR (acta_cierre IS NOT NULL AND fecha_cierre IS NOT NULL AND cerrado_por_id IS NOT NULL)),
    CONSTRAINT chk_incidentes_acta_longitud CHECK (acta_cierre IS NULL OR length(trim(acta_cierre)) >= 10),
    CONSTRAINT chk_incidentes_fecha_cierre CHECK (fecha_cierre IS NULL OR fecha_cierre >= fecha_registro),

    CONSTRAINT chk_incidentes_estado_derivacion CHECK (estado_derivacion IN ('NO_DERIVADA', 'REGISTRADA', 'ENVIADA', 'FALLIDA')),
    CONSTRAINT chk_incidentes_modo_derivacion CHECK (modo_derivacion IS NULL OR modo_derivacion IN ('MANUAL', 'INTEGRADO')),

    CONSTRAINT chk_incidentes_derivacion_coherente CHECK (
        (reportado_siseve = FALSE
            AND estado_derivacion = 'NO_DERIVADA'
            AND modo_derivacion IS NULL
            AND fecha_reporte_siseve IS NULL
            AND codigo_siseve IS NULL
            AND derivado_por_id IS NULL)
        OR
        (reportado_siseve = TRUE
            AND estado_derivacion <> 'NO_DERIVADA'
            AND modo_derivacion IS NOT NULL
            AND fecha_reporte_siseve IS NOT NULL
            AND derivado_por_id IS NOT NULL)),
    CONSTRAINT chk_incidentes_derivacion_fallida CHECK (estado_derivacion <> 'FALLIDA' OR modo_derivacion = 'INTEGRADO'),
    CONSTRAINT chk_incidentes_estado_derivado CHECK (estado <> 'DERIVADO_SISEVE' OR reportado_siseve = TRUE)
);

CREATE INDEX idx_incidentes_estado            ON incidentes(estado);
CREATE INDEX idx_incidentes_tipo              ON incidentes(tipo_incidencia_id);
CREATE INDEX idx_incidentes_fecha_ocurrencia  ON incidentes(fecha_ocurrencia);
CREATE INDEX idx_incidentes_fecha_registro    ON incidentes(fecha_registro);
CREATE INDEX idx_incidentes_gravedad          ON incidentes(gravedad);
CREATE INDEX idx_incidentes_reportado_por     ON incidentes(reportado_por_id);
CREATE INDEX idx_incidentes_responsable       ON incidentes(responsable_actual_id);
CREATE INDEX idx_incidentes_reincidencia      ON incidentes(reincidencia);
CREATE INDEX idx_incidentes_siseve            ON incidentes(reportado_siseve);
CREATE INDEX idx_incidentes_derivacion_activa ON incidentes(estado_derivacion) WHERE estado_derivacion <> 'NO_DERIVADA';

-- =====================================================================================
-- 8. INCIDENTE_INVOLUCRADOS
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
-- 9. ACCIONES_SEGUIMIENTO
-- =====================================================================================
CREATE TABLE acciones_seguimiento (
    id                          BIGSERIAL PRIMARY KEY,
    incidente_id                UUID         NOT NULL,
    usuario_id                  BIGINT       NOT NULL,
    tipo_accion                 VARCHAR(50)  NOT NULL,
    descripcion                 TEXT         NOT NULL,
    fecha_accion                TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT clock_timestamp(),
    fecha_proxima_revision      DATE,
    estado_accion               VARCHAR(20)  NOT NULL DEFAULT 'PENDIENTE',
    fecha_completada            TIMESTAMP WITH TIME ZONE,
    created_at                  TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT clock_timestamp(),
    updated_at                  TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT clock_timestamp(),

    CONSTRAINT fk_acciones_incidente FOREIGN KEY (incidente_id) REFERENCES incidentes(id) ON DELETE CASCADE,
    CONSTRAINT fk_acciones_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE RESTRICT,
    CONSTRAINT chk_acciones_tipo CHECK (tipo_accion IN ('ENTREVISTA_ESTUDIANTE', 'ENTREVISTA_PADRES', 'DERIVACION_PSICOLOGIA','MEDIDA_CORRECTIVA_FORMATIVA', 'DERIVACION_DEMUNA_CEM', 'ACTA_COMPROMISO', 'OTRO')),
    CONSTRAINT chk_acciones_estado CHECK (estado_accion IN ('PENDIENTE', 'EN_PROCESO', 'COMPLETADA')),
    CONSTRAINT chk_acciones_completada CHECK ((estado_accion = 'COMPLETADA') = (fecha_completada IS NOT NULL))
);

CREATE INDEX idx_acciones_incidente ON acciones_seguimiento(incidente_id);
CREATE INDEX idx_acciones_usuario   ON acciones_seguimiento(usuario_id);
CREATE INDEX idx_acciones_estado    ON acciones_seguimiento(estado_accion);

CREATE INDEX idx_acciones_revision_pendiente
    ON acciones_seguimiento(fecha_proxima_revision)
    WHERE estado_accion <> 'COMPLETADA' AND fecha_proxima_revision IS NOT NULL;

-- =====================================================================================
-- 10. INCIDENTE_ADJUNTOS
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

CREATE UNIQUE INDEX uq_adjuntos_incidente_hash
    ON incidente_adjuntos (incidente_id, hash_sha256)
    WHERE activo = TRUE;

-- =====================================================================================
-- 11. ALERTAS
-- =====================================================================================
CREATE TABLE alertas (
    id                          BIGSERIAL PRIMARY KEY,
    incidente_id                UUID         NOT NULL,
    estudiante_id               BIGINT,
    usuario_destino_id          BIGINT,
    tipo_alerta                 VARCHAR(30)  NOT NULL,
    origen                      VARCHAR(15)  NOT NULL DEFAULT 'AUTOMATICA',
    mensaje                     VARCHAR(500) NOT NULL,
    parametros                  JSONB,
    estado_alerta               VARCHAR(15)  NOT NULL DEFAULT 'PENDIENTE',
    fecha_generacion            TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT clock_timestamp(),
    fecha_resolucion            TIMESTAMP WITH TIME ZONE,
    resuelta_por_id             BIGINT,
    observacion_resolucion      VARCHAR(255),
    created_at                  TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT clock_timestamp(),
    updated_at                  TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT clock_timestamp(),

    CONSTRAINT fk_alertas_incidente FOREIGN KEY (incidente_id) REFERENCES incidentes(id) ON DELETE CASCADE,
    CONSTRAINT fk_alertas_estudiante FOREIGN KEY (estudiante_id) REFERENCES estudiantes(id) ON DELETE CASCADE,
    CONSTRAINT fk_alertas_usuario_destino FOREIGN KEY (usuario_destino_id) REFERENCES usuarios(id) ON DELETE SET NULL,
    CONSTRAINT fk_alertas_resuelta_por FOREIGN KEY (resuelta_por_id) REFERENCES usuarios(id) ON DELETE RESTRICT,
    CONSTRAINT chk_alertas_tipo CHECK (tipo_alerta IN ('REINCIDENCIA', 'DEMORA_ATENCION', 'SEGUIMIENTO_VENCIDO', 'CASO_SIN_CIERRE')),
    CONSTRAINT chk_alertas_origen CHECK (origen IN ('AUTOMATICA', 'MANUAL')),
    CONSTRAINT chk_alertas_estado CHECK (estado_alerta IN ('PENDIENTE', 'VISTA', 'ATENDIDA', 'DESCARTADA')),
    CONSTRAINT chk_alertas_reincidencia_estudiante CHECK (tipo_alerta <> 'REINCIDENCIA' OR estudiante_id IS NOT NULL),
    CONSTRAINT chk_alertas_resolucion CHECK (
        estado_alerta NOT IN ('ATENDIDA', 'DESCARTADA')
        OR (fecha_resolucion IS NOT NULL AND resuelta_por_id IS NOT NULL))
);

CREATE INDEX idx_alertas_incidente ON alertas(incidente_id);
CREATE INDEX idx_alertas_estudiante ON alertas(estudiante_id) WHERE estudiante_id IS NOT NULL;
CREATE INDEX idx_alertas_destino_estado ON alertas(usuario_destino_id, estado_alerta);
CREATE INDEX idx_alertas_tipo ON alertas(tipo_alerta);
CREATE INDEX idx_alertas_activas ON alertas(fecha_generacion) WHERE estado_alerta IN ('PENDIENTE', 'VISTA');

CREATE UNIQUE INDEX uq_alertas_activa_unica
    ON alertas (incidente_id, tipo_alerta, COALESCE(estudiante_id, 0))
    WHERE estado_alerta IN ('PENDIENTE', 'VISTA');

-- =====================================================================================
-- 12. NOTIFICACIONES
-- =====================================================================================
CREATE TABLE notificaciones (
    id                      BIGSERIAL PRIMARY KEY,
    incidente_id            UUID         NOT NULL,
    apoderado_id            BIGINT       NOT NULL,
    enviado_por_id          BIGINT       NOT NULL,
    alerta_id               BIGINT,
    canal                   VARCHAR(20)  NOT NULL,
    destino                 VARCHAR(150),
    asunto                  VARCHAR(150) NOT NULL,
    mensaje                 TEXT         NOT NULL,
    fecha_envio             TIMESTAMP WITH TIME ZONE,
    estado_envio            VARCHAR(20)  NOT NULL DEFAULT 'PENDIENTE',
    intentos_envio          INTEGER      NOT NULL DEFAULT 0,
    detalle_resultado       VARCHAR(500),
    id_transaccion_externo  VARCHAR(150),
    created_at              TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT clock_timestamp(),
    updated_at              TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT clock_timestamp(),

    CONSTRAINT fk_notif_incidente FOREIGN KEY (incidente_id) REFERENCES incidentes(id) ON DELETE CASCADE,
    CONSTRAINT fk_notif_apoderado FOREIGN KEY (apoderado_id) REFERENCES apoderados(id) ON DELETE RESTRICT,
    CONSTRAINT fk_notif_enviado_por FOREIGN KEY (enviado_por_id) REFERENCES usuarios(id) ON DELETE RESTRICT,
    CONSTRAINT fk_notif_alerta FOREIGN KEY (alerta_id) REFERENCES alertas(id) ON DELETE SET NULL,
    CONSTRAINT chk_notif_canal CHECK (canal IN ('EMAIL', 'SMS', 'WHATSAPP', 'LLAMADA_TELEFONICA', 'CITACION_PRESENCIAL')),
    CONSTRAINT chk_notif_estado CHECK (estado_envio IN ('PENDIENTE', 'ENVIADO', 'ENTREGADO', 'FALLIDO', 'LEIDO')),
    CONSTRAINT chk_notif_intentos CHECK (intentos_envio >= 0),
    CONSTRAINT chk_notif_fecha_envio CHECK (estado_envio IN ('PENDIENTE', 'FALLIDO') OR fecha_envio IS NOT NULL)
);

CREATE INDEX idx_notif_incidente ON notificaciones(incidente_id);
CREATE INDEX idx_notif_apoderado ON notificaciones(apoderado_id);
CREATE INDEX idx_notif_estado    ON notificaciones(estado_envio);
CREATE INDEX idx_notif_alerta    ON notificaciones(alerta_id) WHERE alerta_id IS NOT NULL;

-- =====================================================================================
-- 13. AUDITORIA_EVENTOS
-- =====================================================================================
CREATE TABLE auditoria_eventos (
    id                  BIGSERIAL PRIMARY KEY,
    fecha_evento        TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT clock_timestamp(),
    usuario_id          BIGINT,
    username_intentado  VARCHAR(50),
    accion              VARCHAR(30)  NOT NULL,
    entidad             VARCHAR(30)  NOT NULL,
    entidad_id          VARCHAR(64),
    incidente_id        UUID,
    valor_anterior      JSONB,
    valor_nuevo         JSONB,
    descripcion         VARCHAR(500),
    ip_origen           VARCHAR(45),
    user_agent          VARCHAR(255),
    resultado           VARCHAR(15)  NOT NULL DEFAULT 'EXITO',

    CONSTRAINT fk_auditoria_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE RESTRICT,
    CONSTRAINT fk_auditoria_incidente FOREIGN KEY (incidente_id) REFERENCES incidentes(id) ON DELETE RESTRICT,
    CONSTRAINT chk_auditoria_accion CHECK (accion IN (
        'LOGIN_EXITOSO', 'LOGIN_FALLIDO', 'LOGOUT', 'CAMBIO_PASSWORD',
        'CREAR', 'MODIFICAR', 'DESACTIVAR', 'CONSULTAR',
        'CAMBIO_ESTADO', 'CAMBIO_GRAVEDAD', 'ASIGNAR_RESPONSABLE', 'CIERRE_CASO', 'DERIVACION_SISEVE',
        'SUBIR_EVIDENCIA', 'ELIMINAR_EVIDENCIA',
        'ENVIAR_NOTIFICACION', 'GENERAR_ALERTA', 'RESOLVER_ALERTA', 'EXPORTAR_REPORTE')),
    CONSTRAINT chk_auditoria_entidad CHECK (entidad IN (
        'SESION', 'USUARIO', 'ROL', 'ESTUDIANTE', 'APODERADO', 'ESTUDIANTE_APODERADO', 'TIPO_INCIDENCIA',
        'INCIDENTE', 'INVOLUCRADO', 'ACCION_SEGUIMIENTO', 'ADJUNTO', 'NOTIFICACION', 'ALERTA',
        'REPORTE', 'PARAMETRO')),
        CONSTRAINT chk_auditoria_resultado CHECK (resultado IN ('EXITO', 'FALLIDO', 'DENEGADO'))
);

CREATE INDEX idx_auditoria_fecha     ON auditoria_eventos(fecha_evento DESC);
CREATE INDEX idx_auditoria_usuario   ON auditoria_eventos(usuario_id, fecha_evento DESC);
CREATE INDEX idx_auditoria_incidente ON auditoria_eventos(incidente_id, fecha_evento DESC) WHERE incidente_id IS NOT NULL;
CREATE INDEX idx_auditoria_entidad   ON auditoria_eventos(entidad, entidad_id);
CREATE INDEX idx_auditoria_accion    ON auditoria_eventos(accion);

-- =====================================================================================
-- 14. PARAMETROS_SISTEMA
-- =====================================================================================
CREATE TABLE parametros_sistema (
    clave           VARCHAR(60)  PRIMARY KEY,
    valor           VARCHAR(255) NOT NULL,
    descripcion     VARCHAR(255),
    created_at      TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT clock_timestamp(),
    updated_at      TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT clock_timestamp(),

    CONSTRAINT chk_parametros_clave CHECK (clave ~ '^[A-Z0-9_]+$')
);