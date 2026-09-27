-- =====================================================================
-- SIGECE - Sistema de Gestión de la Convivencia Escolar
-- Migración Flyway V1: Esquema inicial (PostgreSQL)
-- =====================================================================

CREATE EXTENSION IF NOT EXISTS pgcrypto; -- requerido para gen_random_uuid()

-- ---------------------------------------------------------------------
-- Función utilitaria para mantener updated_at automáticamente
-- ---------------------------------------------------------------------
CREATE OR REPLACE FUNCTION fn_set_updated_at()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = now();
RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- ---------------------------------------------------------------------
-- 1. ROLES (Directivo, Tutor, Comité de Gestión del Bienestar, Psicólogo(a), Admin)
-- ---------------------------------------------------------------------
CREATE TABLE roles (
    id              BIGSERIAL PRIMARY KEY,
    nombre          VARCHAR(50)  NOT NULL UNIQUE,
    descripcion     VARCHAR(255),
    created_at      TIMESTAMP    NOT NULL DEFAULT now(),
    updated_at      TIMESTAMP    NOT NULL DEFAULT now()
);

CREATE TRIGGER trg_roles_updated_at
    BEFORE UPDATE ON roles
    FOR EACH ROW EXECUTE FUNCTION fn_set_updated_at();
COMMENT ON TABLE roles IS 'Catálogo de roles funcionales según D.S. N.° 004-2018-MINEDU (Directivo, Tutor, Comité de Bienestar, etc.)';

-- ---------------------------------------------------------------------
-- 2. USUARIOS (personal de la IE con acceso al sistema)
-- ---------------------------------------------------------------------
CREATE TABLE usuarios (
    id              BIGSERIAL PRIMARY KEY,
    nombres         VARCHAR(100) NOT NULL,
    apellidos       VARCHAR(100) NOT NULL,
    dni             VARCHAR(15)  UNIQUE,
    correo          VARCHAR(150) NOT NULL UNIQUE,
    username        VARCHAR(50)  NOT NULL UNIQUE,
    password_hash   VARCHAR(255) NOT NULL,
    telefono        VARCHAR(20),
    role_id         BIGINT       NOT NULL REFERENCES roles(id) ON DELETE RESTRICT,
    activo          BOOLEAN      NOT NULL DEFAULT TRUE,
    created_at      TIMESTAMP    NOT NULL DEFAULT now(),
    updated_at      TIMESTAMP    NOT NULL DEFAULT now()
);
CREATE INDEX idx_usuarios_role_id ON usuarios(role_id);
CREATE INDEX idx_usuarios_correo  ON usuarios(correo);
CREATE INDEX idx_usuarios_activo  ON usuarios(activo);

CREATE TRIGGER trg_usuarios_updated_at
    BEFORE UPDATE ON usuarios
    FOR EACH ROW EXECUTE FUNCTION fn_set_updated_at();
COMMENT ON TABLE usuarios IS 'Personal autorizado (directivos, tutores, integrantes del Comité de Gestión del Bienestar) que registra y da seguimiento a incidencias.';

-- ---------------------------------------------------------------------
-- 3. ESTUDIANTES
-- ---------------------------------------------------------------------
CREATE TABLE estudiantes (
    id                  BIGSERIAL PRIMARY KEY,
    codigo_siagie       VARCHAR(20) UNIQUE,
    nombres             VARCHAR(100) NOT NULL,
    apellidos           VARCHAR(100) NOT NULL,
    fecha_nacimiento    DATE NOT NULL,
    nivel               VARCHAR(20)  NOT NULL CHECK (nivel IN ('INICIAL','PRIMARIA','SECUNDARIA')),
    grado               VARCHAR(20)  NOT NULL,
    seccion             VARCHAR(10)  NOT NULL,
    activo              BOOLEAN      NOT NULL DEFAULT TRUE,
    created_at          TIMESTAMP    NOT NULL DEFAULT now(),
    updated_at          TIMESTAMP    NOT NULL DEFAULT now()
);

CREATE INDEX idx_estudiantes_grado_seccion ON estudiantes(grado, seccion);
CREATE INDEX idx_estudiantes_nivel         ON estudiantes(nivel);
CREATE TRIGGER trg_estudiantes_updated_at
    BEFORE UPDATE ON estudiantes
    FOR EACH ROW EXECUTE FUNCTION fn_set_updated_at();
COMMENT ON TABLE estudiantes IS 'Datos mínimos necesarios del estudiante. Dato personal de menor de edad protegido bajo Ley N.° 29733.';

-- ---------------------------------------------------------------------
-- 4. APODERADOS (padre/madre/tutor legal) y su relación N:M con estudiantes
-- ---------------------------------------------------------------------
CREATE TABLE apoderados (
    id                  BIGSERIAL PRIMARY KEY,
    nombres             VARCHAR(100) NOT NULL,
    apellidos           VARCHAR(100) NOT NULL,
    tipo_documento      VARCHAR(10)  NOT NULL DEFAULT 'DNI',
    numero_documento    VARCHAR(15)  NOT NULL UNIQUE,
    telefono            VARCHAR(20),
    correo              VARCHAR(150),
    created_at          TIMESTAMP    NOT NULL DEFAULT now(),
    updated_at          TIMESTAMP    NOT NULL DEFAULT now()
);
CREATE TRIGGER trg_apoderados_updated_at
    BEFORE UPDATE ON apoderados
    FOR EACH ROW EXECUTE FUNCTION fn_set_updated_at();

CREATE TABLE estudiante_apoderado (
    id                      BIGSERIAL PRIMARY KEY,
    estudiante_id           BIGINT NOT NULL REFERENCES estudiantes(id) ON DELETE CASCADE,
    apoderado_id            BIGINT NOT NULL REFERENCES apoderados(id) ON DELETE CASCADE,
    parentesco              VARCHAR(30) NOT NULL, -- Padre, Madre, Tutor Legal
    es_contacto_principal   BOOLEAN NOT NULL DEFAULT FALSE,
    created_at              TIMESTAMP NOT NULL DEFAULT now(),
    UNIQUE (estudiante_id, apoderado_id)
);
CREATE INDEX idx_est_apod_estudiante ON estudiante_apoderado(estudiante_id);
CREATE INDEX idx_est_apod_apoderado  ON estudiante_apoderado(apoderado_id);
COMMENT ON TABLE estudiante_apoderado IS 'Relación N:M entre estudiantes y sus apoderados, con indicador de contacto principal para notificaciones.';

-- ---------------------------------------------------------------------
-- 5. TIPOS DE INCIDENCIA (catálogo alineado a categorías SíSeVe)
-- ---------------------------------------------------------------------
CREATE TABLE tipos_incidencia (
    id                  BIGSERIAL PRIMARY KEY,
    nombre              VARCHAR(100) NOT NULL UNIQUE,
    categoria_siseve    VARCHAR(50),
    descripcion         VARCHAR(255)
);
COMMENT ON TABLE tipos_incidencia IS 'Catálogo de tipos de violencia/incidencia (física, psicológica, sexual, bullying, etc.) mapeado a categorías SíSeVe.';

-- ---------------------------------------------------------------------
-- 6. INCIDENTES (caso principal)
-- ---------------------------------------------------------------------
CREATE TABLE incidentes (
    id                      UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    codigo_caso             VARCHAR(30)  NOT NULL UNIQUE, -- ej. INC-2026-000001
    tipo_incidencia_id      BIGINT       NOT NULL REFERENCES tipos_incidencia(id),
    gravedad                VARCHAR(20)  NOT NULL CHECK (gravedad IN ('LEVE','MODERADA','GRAVE','MUY_GRAVE')),
    descripcion             TEXT         NOT NULL,
    lugar_ocurrencia        VARCHAR(150),
    fecha_ocurrencia        TIMESTAMP    NOT NULL,
    fecha_registro          TIMESTAMP    NOT NULL DEFAULT now(),
    estado                  VARCHAR(20)  NOT NULL DEFAULT 'REGISTRADO'
        CHECK (estado IN ('REGISTRADO','EN_INVESTIGACION','EN_SEGUIMIENTO','RESUELTO','DERIVADO_SISEVE','CERRADO')),
    reportado_por_id        BIGINT       NOT NULL REFERENCES usuarios(id),
    responsable_actual_id   BIGINT       REFERENCES usuarios(id),
    reincidencia            BOOLEAN      NOT NULL DEFAULT FALSE,
    reportado_siseve        BOOLEAN      NOT NULL DEFAULT FALSE,
    fecha_reporte_siseve    TIMESTAMP,

    created_at              TIMESTAMP    NOT NULL DEFAULT now(),
    updated_at              TIMESTAMP    NOT NULL DEFAULT now()
);

CREATE INDEX idx_incidentes_estado            ON incidentes(estado);
CREATE INDEX idx_incidentes_tipo              ON incidentes(tipo_incidencia_id);
CREATE INDEX idx_incidentes_fecha_ocurrencia  ON incidentes(fecha_ocurrencia);
CREATE INDEX idx_incidentes_responsable       ON incidentes(responsable_actual_id);
CREATE INDEX idx_incidentes_reincidencia      ON incidentes(reincidencia);

CREATE TRIGGER trg_incidentes_updated_at
    BEFORE UPDATE ON incidentes
    FOR EACH ROW EXECUTE FUNCTION fn_set_updated_at();
COMMENT ON TABLE incidentes IS 'Caso de convivencia escolar. Clave primaria UUID para evitar exposición secuencial de casos sensibles.';

-- ---------------------------------------------------------------------
-- 7. INVOLUCRADOS EN EL INCIDENTE (víctima, agresor, testigo) - N:M
-- ---------------------------------------------------------------------
CREATE TABLE incidente_involucrados (
    id                  BIGSERIAL PRIMARY KEY,
    incidente_id        UUID   NOT NULL REFERENCES incidentes(id) ON DELETE CASCADE,
    estudiante_id       BIGINT NOT NULL REFERENCES estudiantes(id),
    rol_involucrado     VARCHAR(20) NOT NULL CHECK (rol_involucrado IN ('VICTIMA','AGRESOR','TESTIGO')),
    observaciones       VARCHAR(255),
    created_at          TIMESTAMP NOT NULL DEFAULT now(),
    UNIQUE (incidente_id, estudiante_id, rol_involucrado)
);

CREATE INDEX idx_involucrados_incidente  ON incidente_involucrados(incidente_id);
CREATE INDEX idx_involucrados_estudiante ON incidente_involucrados(estudiante_id);
COMMENT ON TABLE incidente_involucrados IS 'Permite identificar patrones de reincidencia por estudiante (agresor recurrente / víctima recurrente).';

-- ---------------------------------------------------------------------
-- 8. ACCIONES DE SEGUIMIENTO / MEDIDAS CORRECTIVAS
-- ---------------------------------------------------------------------
CREATE TABLE acciones_seguimiento (
    id                          BIGSERIAL PRIMARY KEY,
    incidente_id                UUID    NOT NULL REFERENCES incidentes(id) ON DELETE CASCADE,
    usuario_id                  BIGINT  NOT NULL REFERENCES usuarios(id),
    tipo_accion                 VARCHAR(50) NOT NULL, -- Entrevista, Derivación psicológica, Medida correctiva, etc.
    descripcion                 TEXT    NOT NULL,
    fecha_accion                TIMESTAMP NOT NULL DEFAULT now(),
    fecha_proxima_revision      DATE,
    estado_accion               VARCHAR(20) NOT NULL DEFAULT 'PENDIENTE'
        CHECK (estado_accion IN ('PENDIENTE','EN_PROCESO','COMPLETADA')),

    created_at                  TIMESTAMP NOT NULL DEFAULT now(),
    updated_at                  TIMESTAMP NOT NULL DEFAULT now()
);

CREATE INDEX idx_acciones_incidente ON acciones_seguimiento(incidente_id);
CREATE INDEX idx_acciones_usuario   ON acciones_seguimiento(usuario_id);
CREATE INDEX idx_acciones_estado    ON acciones_seguimiento(estado_accion);
CREATE TRIGGER trg_acciones_updated_at
    BEFORE UPDATE ON acciones_seguimiento
    FOR EACH ROW EXECUTE FUNCTION fn_set_updated_at();
COMMENT ON TABLE acciones_seguimiento IS 'Bitácora de trazabilidad de las medidas correctivas/formativas aplicadas a cada caso.';

-- ---------------------------------------------------------------------
-- 9. NOTIFICACIONES A PADRES DE FAMILIA
-- ---------------------------------------------------------------------
CREATE TABLE notificaciones (
    id                  BIGSERIAL PRIMARY KEY,
    incidente_id        UUID    NOT NULL REFERENCES incidentes(id) ON DELETE CASCADE,
    apoderado_id        BIGINT  NOT NULL REFERENCES apoderados(id),
    enviado_por_id      BIGINT  NOT NULL REFERENCES usuarios(id),
    canal               VARCHAR(20) NOT NULL CHECK (canal IN ('EMAIL','SMS','LLAMADA','PRESENCIAL','APP')),
    asunto              VARCHAR(150),
    mensaje             TEXT    NOT NULL,
    fecha_envio         TIMESTAMP,
    estado_envio        VARCHAR(20) NOT NULL DEFAULT 'PENDIENTE'
        CHECK (estado_envio IN ('PENDIENTE','ENVIADO','FALLIDO','LEIDO')),
    created_at          TIMESTAMP NOT NULL DEFAULT now(),
    updated_at          TIMESTAMP NOT NULL DEFAULT now()
);

CREATE INDEX idx_notif_incidente ON notificaciones(incidente_id);
CREATE INDEX idx_notif_apoderado ON notificaciones(apoderado_id);
CREATE INDEX idx_notif_estado    ON notificaciones(estado_envio);
CREATE TRIGGER trg_notificaciones_updated_at
    BEFORE UPDATE ON notificaciones
    FOR EACH ROW EXECUTE FUNCTION fn_set_updated_at();
COMMENT ON TABLE notificaciones IS 'Registro auditable de comunicaciones con los padres/apoderados, resolviendo el retraso de comunicación identificado en el problema de investigación.';


-- ---------------------------------------------------------------------
-- 10. EVIDENCIAS Y ADJUNTOS (Soporte directo para Azure Blob Storage)
-- Resuelve: Ciberacoso (capturas) y digitalización de actas físicas (pág. 62)
-- ---------------------------------------------------------------------
CREATE TABLE incidente_adjuntos (
    id                  BIGSERIAL PRIMARY KEY,
    incidente_id        UUID NOT NULL REFERENCES incidentes(id) ON DELETE CASCADE,
    accion_id           BIGINT REFERENCES acciones_seguimiento(id) ON DELETE CASCADE, -- opcional, si el adjunto es de una entrevista/acta
    subido_por_id       BIGINT NOT NULL REFERENCES usuarios(id),
    nombre_archivo      VARCHAR(255) NOT NULL,
    tipo_archivo        VARCHAR(50) NOT NULL, -- image/png, application/pdf, etc.
    tamano_bytes        BIGINT NOT NULL,
    url_storage         TEXT NOT NULL, -- URL apuntando a Azure Blob Storage
    hash_sha256         VARCHAR(64), -- Garantiza integridad probatoria (cadena de custodia)
    created_at          TIMESTAMP NOT NULL DEFAULT now()
);

CREATE INDEX idx_adjuntos_incidente ON incidente_adjuntos(incidente_id);

COMMENT ON TABLE incidente_adjuntos IS 'Metadatos de evidencias (capturas de ciberacoso, actas firmadas escaneadas) alojadas en Azure Blob Storage.';

-- ---------------------------------------------------------------------
-- AJUSTE A INCIDENTE_INVOLUCRADOS (Para cubrir D.S. 004-2018 Personal -> Alumno)
-- ---------------------------------------------------------------------
-- Permitir que un involucrado pueda ser un estudiante O un usuario/personal del colegio:
ALTER TABLE incidente_involucrados ALTER COLUMN estudiante_id DROP NOT NULL;
ALTER TABLE incidente_involucrados ADD COLUMN usuario_id BIGINT REFERENCES usuarios(id);
ALTER TABLE incidente_involucrados ADD CONSTRAINT chk_involucrado_tipo
    CHECK ((estudiante_id IS NOT NULL AND usuario_id IS NULL) OR (estudiante_id IS NULL AND usuario_id IS NOT NULL));
