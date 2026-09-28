-- *************************************************************************************
-- PROYECTO: SIGECE - Sistema Web para el Registro, Seguimiento y Gestión
--           Oportuna de Incidencias de Convivencia Escolar
-- INSTITUCIÓN: Instituto Superior Tecnológico Privado CIBERTEC
-- CARRERA: Computación e Informática / Redes y Telecomunicaciones
-- GESTOR BD: PostgreSQL 14+ / Azure Database for PostgreSQL
-- MIGRACIÓN: Flyway V1__initial_schema.sql
-- *************************************************************************************
CREATE EXTENSION IF NOT EXISTS pgcrypto;

-- *************************************************************************************
-- FUNCIÓN GLOBAL: Disparador para actualización automática de 'updated_at'
-- *************************************************************************************
CREATE OR REPLACE FUNCTION fn_set_updated_at()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = clock_timestamp();
RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- *************************************************************************************
    -- 1. TABLA: ROLES
    -- (Catálogo de Control de Acceso RBAC)
    -- Define los privilegios dentro de la IE: Directivo, Tutor, Psicólogo, Comité, etc.
-- *************************************************************************************
CREATE TABLE roles (
    id              BIGSERIAL PRIMARY KEY,
    nombre          VARCHAR(50)  NOT NULL UNIQUE,
    descripcion     VARCHAR(255),
    created_at      TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT clock_timestamp(),
    updated_at      TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT clock_timestamp(),

    CONSTRAINT chk_roles_nombre_formato CHECK (length(trim(nombre)) >= 3)
);

CREATE TRIGGER trg_roles_updated_at
    BEFORE UPDATE ON roles
    FOR EACH ROW EXECUTE FUNCTION fn_set_updated_at();
COMMENT ON TABLE roles IS 'Catálogo de roles funcionales autorizados para la gestión de incidentes escolares (RBAC).';
COMMENT ON COLUMN roles.nombre IS 'Nombre estandarizado: ROLE_DIRECTOR, ROLE_TUTOR, ROLE_COMITE_BIENESTAR, ROLE_PSICOLOGO, ROLE_ADMIN.';

-- *************************************************************************************
    -- 2. TABLA: USUARIOS
    -- (Personal de la Institución Educativa)
    -- Docentes, auxiliares, directivos y psicólogos que operan el sistema.
-- *************************************************************************************
CREATE TABLE usuarios (
    id              BIGSERIAL PRIMARY KEY,
    nombres         VARCHAR(100) NOT NULL,
    apellidos       VARCHAR(100) NOT NULL,
    dni             VARCHAR(15)  NOT NULL UNIQUE,
    correo          VARCHAR(150) NOT NULL UNIQUE,
    username        VARCHAR(50)  NOT NULL UNIQUE,
    password_hash   VARCHAR(255) NOT NULL,
    telefono        VARCHAR(20),
    role_id         BIGINT       NOT NULL,
    activo          BOOLEAN      NOT NULL DEFAULT TRUE,
    created_at      TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT clock_timestamp(),
    updated_at      TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT clock_timestamp(),

    CONSTRAINT fk_usuarios_roles FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE RESTRICT,
    CONSTRAINT chk_usuarios_dni CHECK (dni ~ '^[0-9]{8,15}$'),
    CONSTRAINT chk_usuarios_correo CHECK (correo ~* '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$')
);

CREATE INDEX idx_usuarios_role_id ON usuarios(role_id);
CREATE INDEX idx_usuarios_correo  ON usuarios(correo);
CREATE INDEX idx_usuarios_activo  ON usuarios(activo);

CREATE TRIGGER trg_usuarios_updated_at
    BEFORE UPDATE ON usuarios
    FOR EACH ROW EXECUTE FUNCTION fn_set_updated_at();

COMMENT ON TABLE usuarios IS 'Personal autorizado de la IE con acceso a la gestión de incidentes y auditoría.';
COMMENT ON COLUMN usuarios.password_hash IS 'Hash de contraseña encriptado mediante BCrypt desde Spring Security.';

-- *************************************************************************************
    -- 3. TABLA: ESTUDIANTES
    -- (Padrón Escolar Protegido)
    -- Sujeto de protección. Información sensible bajo tutela de la Ley N.° 29733.
-- *************************************************************************************
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

CREATE TRIGGER trg_estudiantes_updated_at
    BEFORE UPDATE ON estudiantes
    FOR EACH ROW EXECUTE FUNCTION fn_set_updated_at();

COMMENT ON TABLE estudiantes IS 'Datos de los estudiantes matriculados. Protegidos por Ley N.° 29733 (datos personales de menores).';
COMMENT ON COLUMN estudiantes.codigo_siagie IS 'Código único de matrícula asignado por el sistema oficial SIAGIE de MINEDU.';

-- *************************************************************************************
    -- 4. TABLA: APODERADOS
    -- (Padres y Tutores Legales)
    -- Sujetos receptores de las notificaciones y responsables civiles del menor.
-- *************************************************************************************
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

CREATE TRIGGER trg_apoderados_updated_at
    BEFORE UPDATE ON apoderados
    FOR EACH ROW EXECUTE FUNCTION fn_set_updated_at();

COMMENT ON TABLE apoderados IS 'Padre, madre o apoderado legal responsable del menor de edad ante la IE.';

-- *************************************************************************************
    -- 5. TABLA INTERMEDIA: ESTUDIANTE_APODERADO
    -- (Núcleo Familiar N:M)
    -- Resuelve la asignación de tutores y prioriza el contacto de emergencia.
-- *************************************************************************************
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
    CONSTRAINT chk_est_apod_parentesco CHECK (parentesco IN ('PADRE', 'MADRE', 'TUTOR_LEGAL', 'APODERADO'))
);

CREATE INDEX idx_est_apod_estudiante ON estudiante_apoderado(estudiante_id);
CREATE INDEX idx_est_apod_apoderado  ON estudiante_apoderado(apoderado_id);

COMMENT ON TABLE estudiante_apoderado IS 'Relación multifamiliar entre estudiantes y apoderados, con flag para el contacto prioritario.';

-- *************************************************************************************
    -- 6. TABLA: TIPOS_INCIDENCIA
    -- (Catálogo Homologado con SíSeVe - MINEDU)
    -- Permite estandarizar la clasificación sin ambigüedades técnicas o legales.
-- *************************************************************************************
CREATE TABLE tipos_incidencia (
    id                  BIGSERIAL PRIMARY KEY,
    nombre              VARCHAR(100) NOT NULL UNIQUE,
    categoria_siseve    VARCHAR(50)  NOT NULL,
    descripcion         VARCHAR(255),
    activo              BOOLEAN      NOT NULL DEFAULT TRUE
);

COMMENT ON TABLE tipos_incidencia IS 'Catálogo de tipologías de violencia escolar alineadas a los protocolos oficiales de SíSeVe.';
COMMENT ON COLUMN tipos_incidencia.categoria_siseve IS 'Categoría oficial: FISICA_SIN_ARMAS, FISICA_CON_ARMAS, PSICOLOGICA, SEXUAL, CIBERACOSO.';

-- *************************************************************************************
    -- 7. TABLA: INCIDENTES
    -- (Expediente Digital del Caso)
    -- Corazón del sistema. PK UUID para no exponer numeración secuencial sensible.
-- *************************************************************************************
CREATE TABLE incidentes (
    id                      UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    codigo_caso             VARCHAR(30)  NOT NULL UNIQUE, -- Formato: INC-YYYY-XXXXXX
    tipo_incidencia_id      BIGINT       NOT NULL,
    gravedad                VARCHAR(20)  NOT NULL,
    descripcion             TEXT         NOT NULL,
    lugar_ocurrencia        VARCHAR(150) NOT NULL,
    fecha_ocurrencia        TIMESTAMP WITH TIME ZONE NOT NULL,
    fecha_registro          TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT clock_timestamp(),
    estado                  VARCHAR(25)  NOT NULL DEFAULT 'REGISTRADO',
    reportado_por_id        BIGINT       NOT NULL,
    responsable_actual_id   BIGINT,
    reincidencia            BOOLEAN      NOT NULL DEFAULT FALSE,
    reportado_siseve        BOOLEAN      NOT NULL DEFAULT FALSE,
    fecha_reporte_siseve    TIMESTAMP WITH TIME ZONE,
    codigo_siseve           VARCHAR(50), -- Código retornado por la plataforma nacional si se derivó
    created_at              TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT clock_timestamp(),
    updated_at              TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT clock_timestamp(),

    CONSTRAINT fk_incidentes_tipo FOREIGN KEY (tipo_incidencia_id) REFERENCES tipos_incidencia(id) ON DELETE RESTRICT,
    CONSTRAINT fk_incidentes_reportado_por FOREIGN KEY (reportado_por_id) REFERENCES usuarios(id) ON DELETE RESTRICT,
    CONSTRAINT fk_incidentes_responsable FOREIGN KEY (responsable_actual_id) REFERENCES usuarios(id) ON DELETE SET NULL,
    CONSTRAINT chk_incidentes_gravedad CHECK (gravedad IN ('LEVE', 'MODERADA', 'GRAVE', 'MUY_GRAVE')),
    CONSTRAINT chk_incidentes_estado CHECK (estado IN ('REGISTRADO', 'EN_INVESTIGACION', 'EN_SEGUIMIENTO', 'RESUELTO', 'DERIVADO_SISEVE', 'CERRADO')),
    /*
    CONSTRAINT chk_incidentes_fecha_coherente CHECK (fecha_ocurrencia <= clock_timestamp())
    */
);

CREATE INDEX idx_incidentes_estado            ON incidentes(estado);
CREATE INDEX idx_incidentes_tipo              ON incidentes(tipo_incidencia_id);
CREATE INDEX idx_incidentes_fecha_ocurrencia  ON incidentes(fecha_ocurrencia);
CREATE INDEX idx_incidentes_responsable       ON incidentes(responsable_actual_id);
CREATE INDEX idx_incidentes_reincidencia      ON incidentes(reincidencia);
CREATE INDEX idx_incidentes_siseve            ON incidentes(reportado_siseve);

CREATE TRIGGER trg_incidentes_updated_at
    BEFORE UPDATE ON incidentes
    FOR EACH ROW EXECUTE FUNCTION fn_set_updated_at();

COMMENT ON TABLE incidentes IS 'Registro central del caso de convivencia escolar. Reemplaza el cuaderno físico de incidencias.';
COMMENT ON COLUMN incidentes.codigo_caso IS 'Identificador correlativo institucional para auditoría e inspección UGEL.';

-- *************************************************************************************
    -- 8. TABLA: INCIDENTE_INVOLUCRADOS
    -- (Mapeo de Roles y Actores)
    -- Soporta violencia entre pares (Alumno-Alumno) y de Personal a Alumno.
-- *************************************************************************************
CREATE TABLE incidente_involucrados (
    id                  BIGSERIAL PRIMARY KEY,
    incidente_id        UUID         NOT NULL,
    estudiante_id       BIGINT,      -- Si el involucrado es un menor
    usuario_id          BIGINT,      -- Si el involucrado es personal de la IE (docente, auxiliar)
    rol_involucrado     VARCHAR(20)  NOT NULL,
    observaciones       VARCHAR(255),
    created_at          TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT clock_timestamp(),

    CONSTRAINT fk_involucrados_incidente FOREIGN KEY (incidente_id) REFERENCES incidentes(id) ON DELETE CASCADE,
    CONSTRAINT fk_involucrados_estudiante FOREIGN KEY (estudiante_id) REFERENCES estudiantes(id) ON DELETE CASCADE,
    CONSTRAINT fk_involucrados_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE,
    CONSTRAINT chk_involucrados_rol CHECK (rol_involucrado IN ('VICTIMA', 'AGRESOR', 'TESTIGO')),
    -- Regla de exclusión: Debe ser un estudiante O un personal docente/administrativo, nunca ambos ni ninguno
    CONSTRAINT chk_involucrado_entidad_exclusiva CHECK (
        (estudiante_id IS NOT NULL AND usuario_id IS NULL)
            OR
        (estudiante_id IS NULL AND usuario_id IS NOT NULL))
);

CREATE INDEX idx_involucrados_incidente  ON incidente_involucrados(incidente_id);
CREATE INDEX idx_involucrados_estudiante ON incidente_involucrados(estudiante_id);
CREATE INDEX idx_involucrados_usuario    ON incidente_involucrados(usuario_id);

-- Índices únicos parciales para prevenir registros duplicados de la misma persona con el mismo rol en un caso
CREATE UNIQUE INDEX uq_involucrado_estudiante
    ON incidente_involucrados (incidente_id, estudiante_id, rol_involucrado)
    WHERE estudiante_id IS NOT NULL;

CREATE UNIQUE INDEX uq_involucrado_usuario
    ON incidente_involucrados (incidente_id, usuario_id, rol_involucrado)
    WHERE usuario_id IS NOT NULL;

COMMENT ON TABLE incidente_involucrados IS 'Identificación de partes en conflicto (víctimas, agresores, testigos) para análisis de reincidencia.';

-- *************************************************************************************
    -- 9. TABLA: ACCIONES_SEGUIMIENTO
    -- (Bitácora de Trazabilidad y Medidas)
    -- Evita el estancamiento de expedientes y la ruptura entre registro y cierre.
-- *************************************************************************************
CREATE TABLE acciones_seguimiento (
    id                          BIGSERIAL PRIMARY KEY,
    incidente_id                UUID         NOT NULL,
    usuario_id                  BIGINT       NOT NULL, -- Funcionario que ejecuta la acción
    tipo_accion                 VARCHAR(50)  NOT NULL,
    descripcion                 TEXT         NOT NULL,
    fecha_accion                TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT clock_timestamp(),
    fecha_proxima_revision      DATE,
    estado_accion               VARCHAR(20)  NOT NULL DEFAULT 'PENDIENTE',
    created_at                  TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT clock_timestamp(),
    updated_at                  TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT clock_timestamp(),

    CONSTRAINT fk_acciones_incidente FOREIGN KEY (incidente_id) REFERENCES incidentes(id) ON DELETE CASCADE,
    CONSTRAINT fk_acciones_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE RESTRICT,
    CONSTRAINT chk_acciones_tipo CHECK (tipo_accion IN ('ENTREVISTA_ESTUDIANTE', 'ENTREVISTA_PADRES', 'DERIVACION_PSICOLOGIA','MEDIDA_CORRECTIVA_FORMATIVA', 'DERIVACION_DEMUNA_CEM', 'ACTA_COMPROMISO', 'OTRO')),
    CONSTRAINT chk_acciones_estado CHECK (estado_accion IN ('PENDIENTE', 'EN_PROCESO', 'COMPLETADA'))
);

CREATE INDEX idx_acciones_incidente ON acciones_seguimiento(incidente_id);
CREATE INDEX idx_acciones_usuario   ON acciones_seguimiento(usuario_id);
CREATE INDEX idx_acciones_estado    ON acciones_seguimiento(estado_accion);

CREATE TRIGGER trg_acciones_updated_at
    BEFORE UPDATE ON acciones_seguimiento
    FOR EACH ROW EXECUTE FUNCTION fn_set_updated_at();

COMMENT ON TABLE acciones_seguimiento IS 'Bitácora de acciones formativas, entrevistas y derivaciones psicológicas ejecutadas en cada caso.';

-- *************************************************************************************
    -- 10. TABLA: INCIDENTE_ADJUNTOS
    -- (Soporte Digital para Azure Blob Storage)
    -- Resuelve la pérdida probatoria en ciberacoso y digitaliza actas firmadas.
-- *************************************************************************************
CREATE TABLE incidente_adjuntos (
    id                      BIGSERIAL PRIMARY KEY,
    incidente_id            UUID         NOT NULL,
    accion_id               BIGINT,      -- Opcional: si el archivo pertenece a un acta de seguimiento específica
    subido_por_id           BIGINT       NOT NULL,
    nombre_original         VARCHAR(255) NOT NULL,
    nombre_almacenamiento   VARCHAR(255) NOT NULL UNIQUE, -- Nombre GUID dentro del contenedor Azure Blob
    tipo_mime               VARCHAR(100) NOT NULL,
    tamano_bytes            BIGINT       NOT NULL,
    url_storage             TEXT         NOT NULL,
    hash_sha256             VARCHAR(64)  NOT NULL, -- Garantiza la no alteración del documento (integridad probatoria)
    created_at              TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT clock_timestamp(),

    CONSTRAINT fk_adjuntos_incidente FOREIGN KEY (incidente_id) REFERENCES incidentes(id) ON DELETE CASCADE,
    CONSTRAINT fk_adjuntos_accion FOREIGN KEY (accion_id) REFERENCES acciones_seguimiento(id) ON DELETE CASCADE,
    CONSTRAINT fk_adjuntos_usuario FOREIGN KEY (subido_por_id) REFERENCES usuarios(id) ON DELETE RESTRICT,
    CONSTRAINT chk_adjuntos_tamano CHECK (tamano_bytes > 0 AND tamano_bytes <= 10485760) -- Límite preventivo de 10 MB por archivo
);

CREATE INDEX idx_adjuntos_incidente ON incidente_adjuntos(incidente_id);
CREATE INDEX idx_adjuntos_accion    ON incidente_adjuntos(accion_id);

COMMENT ON TABLE incidente_adjuntos IS 'Metadatos de evidencias (capturas de ciberacoso, actas escaneadas) alojadas en Azure Blob Storage.';
COMMENT ON COLUMN incidente_adjuntos.hash_sha256 IS 'Firma criptográfica SHA-256 para preservar la cadena de custodia del documento probatorio.';

-- *************************************************************************************
    -- 11. TABLA: NOTIFICACIONES
    -- (Comunicación Inmediata y Auditable a Familias)
    -- Erradica la falla estructural de la agenda escolar y genera constancia legal.
-- *************************************************************************************
CREATE TABLE notificaciones (
    id                      BIGSERIAL PRIMARY KEY,
    incidente_id            UUID         NOT NULL,
    apoderado_id            BIGINT       NOT NULL,
    enviado_por_id          BIGINT       NOT NULL,
    canal                   VARCHAR(20)  NOT NULL,
    asunto                  VARCHAR(150) NOT NULL,
    mensaje                 TEXT         NOT NULL,
    fecha_envio             TIMESTAMP WITH TIME ZONE,
    estado_envio            VARCHAR(20)  NOT NULL DEFAULT 'PENDIENTE',
    id_transaccion_externo  VARCHAR(150), -- Message-ID de proveedor (SendGrid, Twilio, WhatsApp Cloud API)
    created_at              TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT clock_timestamp(),
    updated_at              TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT clock_timestamp(),

    CONSTRAINT fk_notif_incidente FOREIGN KEY (incidente_id) REFERENCES incidentes(id) ON DELETE CASCADE,
    CONSTRAINT fk_notif_apoderado FOREIGN KEY (apoderado_id) REFERENCES apoderados(id) ON DELETE RESTRICT,
    CONSTRAINT fk_notif_enviado_por FOREIGN KEY (enviado_por_id) REFERENCES usuarios(id) ON DELETE RESTRICT,
    CONSTRAINT chk_notif_canal CHECK (canal IN ('EMAIL', 'SMS', 'WHATSAPP', 'LLAMADA_TELEFONICA', 'CITACION_PRESENCIAL')),
    CONSTRAINT chk_notif_estado CHECK (estado_envio IN ('PENDIENTE', 'ENVIADO', 'ENTREGADO', 'FALLIDO', 'LEIDO'))
);

CREATE INDEX idx_notif_incidente ON notificaciones(incidente_id);
CREATE INDEX idx_notif_apoderado ON notificaciones(apoderado_id);
CREATE INDEX idx_notif_estado    ON notificaciones(estado_envio);

CREATE TRIGGER trg_notificaciones_updated_at
    BEFORE UPDATE ON notificaciones
    FOR EACH ROW EXECUTE FUNCTION fn_set_updated_at();

COMMENT ON TABLE notificaciones IS 'Trazabilidad y auditoría de avisos a padres de familia. Elimina la pérdida de citaciones por agenda escolar.';
COMMENT ON COLUMN notificaciones.id_transaccion_externo IS 'ID de entrega devuelto por la pasarela de mensajería (garantía probatoria de comunicación).';