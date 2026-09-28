-- *************************************************************************************
-- PROYECTO: SIGECE - Sistema Web para el Registro, Seguimiento y Gestión
--           Oportuna de Incidencias de Convivencia Escolar
--
-- MIGRACIÓN: Flyway V2__seed_catalogos.sql
--
-- OBJETIVO:
--   Cargar datos mínimos para desarrollo, pruebas e integración inicial.
--
-- ORDEN DE DEPENDENCIAS:
--   roles
--      ↓
--   usuarios
--
--   estudiantes + apoderados
--      ↓
--   estudiante_apoderado
--
--   tipos_incidencia
--      ↓
--   incidentes
--      ↓
--   incidente_involucrados
--      ↓
--   acciones_seguimiento
--      ↓
--   notificaciones
--
-- NOTA:
--   incidente_adjuntos NO se incluye porque requiere archivos reales
--   almacenados en Azure Blob Storage.
-- *************************************************************************************


-- *************************************************************************************
-- 1. ROLES
-- *************************************************************************************

INSERT INTO roles (nombre, descripcion)
VALUES
    (
        'DIRECTOR',
        'Personal directivo de la institución educativa'
    ),
    (
        'TUTOR',
        'Docente tutor de aula responsable del seguimiento del estudiante'
    ),
    (
        'COMITE_BIENESTAR',
        'Integrante del Comité de Gestión del Bienestar Escolar'
    ),
    (
        'PSICOLOGO',
        'Profesional de apoyo psicopedagógico'
    ),
    (
        'ADMINISTRADOR_SISTEMA',
        'Administrador técnico del sistema'
    );


-- *************************************************************************************
-- 2. USUARIOS
-- *************************************************************************************
--
-- Contraseña de desarrollo para todos los usuarios:
-- password
--
-- El valor almacenado es un hash BCrypt.
-- En producción estos usuarios NO deben utilizarse.
-- *************************************************************************************

INSERT INTO usuarios (
    nombres,
    apellidos,
    dni,
    correo,
    username,
    password_hash,
    telefono,
    role_id,
    activo
)
VALUES
    (
        'Carlos',
        'Quispe Huamán',
        '70000001',
        'director@sigece.test',
        'director',
        '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
        '999111111',
        (SELECT id FROM roles WHERE nombre = 'DIRECTOR'),
        TRUE
    ),
    (
        'María',
        'Condori Quispe',
        '70000002',
        'tutor@sigece.test',
        'tutor',
        '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
        '999222222',
        (SELECT id FROM roles WHERE nombre = 'TUTOR'),
        TRUE
    ),
    (
        'Lucía',
        'Huamán Flores',
        '70000003',
        'psicologia@sigece.test',
        'psicologa',
        '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
        '999333333',
        (SELECT id FROM roles WHERE nombre = 'PSICOLOGO'),
        TRUE
    ),
    (
        'Pedro',
        'Apaza Quispe',
        '70000004',
        'bienestar@sigece.test',
        'bienestar',
        '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
        '999444444',
        (SELECT id FROM roles WHERE nombre = 'COMITE_BIENESTAR'),
        TRUE
    ),
    (
        'Administrador',
        'SIGECE',
        '70000005',
        'admin@sigece.test',
        'admin',
        '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
        '999555555',
        (SELECT id FROM roles WHERE nombre = 'ADMINISTRADOR_SISTEMA'),
        TRUE
    );


-- *************************************************************************************
-- 3. ESTUDIANTES
-- *************************************************************************************

INSERT INTO estudiantes (
    codigo_siagie,
    nombres,
    apellidos,
    fecha_nacimiento,
    nivel,
    grado,
    seccion,
    activo
)
VALUES
    (
        'SIAGIE000001',
        'Juan',
        'Quispe Huamán',
        '2012-05-15',
        'SECUNDARIA',
        '2',
        'A',
        TRUE
    ),
    (
        'SIAGIE000002',
        'Ana',
        'Condori Flores',
        '2011-09-20',
        'SECUNDARIA',
        '3',
        'A',
        TRUE
    ),
    (
        'SIAGIE000003',
        'Luis',
        'Huamán Quispe',
        '2013-02-10',
        'SECUNDARIA',
        '1',
        'B',
        TRUE
    ),
    (
        'SIAGIE000004',
        'Rosa',
        'Apaza Condori',
        '2010-11-03',
        'SECUNDARIA',
        '4',
        'A',
        TRUE
    );


-- *************************************************************************************
-- 4. APODERADOS
-- *************************************************************************************

INSERT INTO apoderados (
    nombres,
    apellidos,
    tipo_documento,
    numero_documento,
    telefono,
    correo
)
VALUES
    (
        'José',
        'Quispe Mamani',
        'DNI',
        '60000001',
        '988111111',
        'jose.quispe@test.com'
    ),
    (
        'Rosa',
        'Flores Condori',
        'DNI',
        '60000002',
        '988222222',
        'rosa.flores@test.com'
    ),
    (
        'Miguel',
        'Huamán Apaza',
        'DNI',
        '60000003',
        '988333333',
        'miguel.huaman@test.com'
    );


-- *************************************************************************************
-- 5. RELACIÓN ESTUDIANTE - APODERADO
-- *************************************************************************************

INSERT INTO estudiante_apoderado (
    estudiante_id,
    apoderado_id,
    parentesco,
    es_contacto_principal
)
VALUES
    (
        (SELECT id
         FROM estudiantes
         WHERE codigo_siagie = 'SIAGIE000001'),

        (SELECT id
         FROM apoderados
         WHERE numero_documento = '60000001'),

        'PADRE',
        TRUE
    ),
    (
        (SELECT id
         FROM estudiantes
         WHERE codigo_siagie = 'SIAGIE000002'),

        (SELECT id
         FROM apoderados
         WHERE numero_documento = '60000002'),

        'MADRE',
        TRUE
    ),
    (
        (SELECT id
         FROM estudiantes
         WHERE codigo_siagie = 'SIAGIE000003'),

        (SELECT id
         FROM apoderados
         WHERE numero_documento = '60000003'),

        'APODERADO',
        TRUE
    );


-- *************************************************************************************
-- 6. TIPOS DE INCIDENCIA
-- *************************************************************************************
--
-- Se utilizan categorías compatibles con la documentación de V1.
-- *************************************************************************************

INSERT INTO tipos_incidencia (
    nombre,
    categoria_siseve,
    descripcion
)
VALUES
    (
        'Violencia física sin armas',
        'FISICA_SIN_ARMAS',
        'Agresión física que no involucra el uso de armas u objetos peligrosos'
    ),
    (
        'Violencia física con armas',
        'FISICA_CON_ARMAS',
        'Agresión física en la que se utiliza un arma u objeto peligroso'
    ),
    (
        'Violencia psicológica',
        'PSICOLOGICA',
        'Agresión verbal, humillación, amenaza, intimidación o exclusión'
    ),
    (
        'Violencia sexual',
        'SEXUAL',
        'Conducta de naturaleza sexual que afecta la integridad del estudiante'
    ),
    (
        'Acoso escolar',
        'BULLYING',
        'Hostigamiento reiterado entre estudiantes'
    ),
    (
        'Ciberacoso',
        'CIBERACOSO',
        'Hostigamiento realizado mediante redes sociales, mensajería u otros medios digitales'
    ),
    (
        'Negligencia o abandono',
        'NEGLIGENCIA',
        'Desatención de necesidades básicas o deberes de protección del estudiante'
    );


-- *************************************************************************************
-- 7. INCIDENTES
-- *************************************************************************************

INSERT INTO incidentes (
    codigo_caso,
    tipo_incidencia_id,
    gravedad,
    descripcion,
    lugar_ocurrencia,
    fecha_ocurrencia,
    estado,
    reportado_por_id,
    responsable_actual_id,
    reincidencia,
    reportado_siseve
)
VALUES
    (
        'INC-2026-000001',

        (
            SELECT id
            FROM tipos_incidencia
            WHERE categoria_siseve = 'PSICOLOGICA'
        ),

        'MODERADA',

        'Se reportó un conflicto verbal entre estudiantes durante el horario de recreo.',

        'Patio de la institución educativa',

        '2026-09-20 10:30:00-05:00',

        'EN_INVESTIGACION',

        (
            SELECT id
            FROM usuarios
            WHERE username = 'tutor'
        ),

        (
            SELECT id
            FROM usuarios
            WHERE username = 'bienestar'
        ),

        FALSE,
        FALSE
    ),
    (
        'INC-2026-000002',

        (
            SELECT id
            FROM tipos_incidencia
            WHERE categoria_siseve = 'CIBERACOSO'
        ),

        'GRAVE',

        'Se reportaron mensajes de hostigamiento mediante una plataforma digital entre estudiantes.',

        'Entorno digital',

        '2026-09-22 18:00:00-05:00',

        'EN_SEGUIMIENTO',

        (
            SELECT id
            FROM usuarios
            WHERE username = 'tutor'
        ),

        (
            SELECT id
            FROM usuarios
            WHERE username = 'psicologa'
        ),

        FALSE,
        TRUE
    );


-- *************************************************************************************
-- 8. INVOLUCRADOS DEL INCIDENTE
-- *************************************************************************************
--
-- INCIDENTE 1:
--   Juan  -> víctima
--   Ana   -> agresora
--   Luis  -> testigo
--
-- INCIDENTE 2:
--   Ana   -> víctima
--   Juan  -> agresor
-- *************************************************************************************

INSERT INTO incidente_involucrados (
    incidente_id,
    estudiante_id,
    usuario_id,
    rol_involucrado,
    observaciones
)
VALUES
    (
        (
            SELECT id
            FROM incidentes
            WHERE codigo_caso = 'INC-2026-000001'
        ),

        (
            SELECT id
            FROM estudiantes
            WHERE codigo_siagie = 'SIAGIE000001'
        ),

        NULL,

        'VICTIMA',

        'Estudiante involucrado como víctima del conflicto reportado.'
    ),
    (
        (
            SELECT id
            FROM incidentes
            WHERE codigo_caso = 'INC-2026-000001'
        ),

        (
            SELECT id
            FROM estudiantes
            WHERE codigo_siagie = 'SIAGIE000002'
        ),

        NULL,

        'AGRESOR',

        'Estudiante señalado como participante de la agresión verbal.'
    ),
    (
        (
            SELECT id
            FROM incidentes
            WHERE codigo_caso = 'INC-2026-000001'
        ),

        (
            SELECT id
            FROM estudiantes
            WHERE codigo_siagie = 'SIAGIE000003'
        ),

        NULL,

        'TESTIGO',

        'Estudiante que presenció el incidente.'
    ),
    (
        (
            SELECT id
            FROM incidentes
            WHERE codigo_caso = 'INC-2026-000002'
        ),

        (
            SELECT id
            FROM estudiantes
            WHERE codigo_siagie = 'SIAGIE000002'
        ),

        NULL,

        'VICTIMA',

        'Estudiante afectada por mensajes de hostigamiento digital.'
    ),
    (
        (
            SELECT id
            FROM incidentes
            WHERE codigo_caso = 'INC-2026-000002'
        ),

        (
            SELECT id
            FROM estudiantes
            WHERE codigo_siagie = 'SIAGIE000001'
        ),

        NULL,

        'AGRESOR',

        'Estudiante identificado como presunto responsable del hostigamiento.'
    );


-- *************************************************************************************
-- 9. ACCIONES DE SEGUIMIENTO
-- *************************************************************************************

INSERT INTO acciones_seguimiento (
    incidente_id,
    usuario_id,
    tipo_accion,
    descripcion,
    fecha_accion,
    fecha_proxima_revision,
    estado_accion
)
VALUES
    (
        (
            SELECT id
            FROM incidentes
            WHERE codigo_caso = 'INC-2026-000001'
        ),

        (
            SELECT id
            FROM usuarios
            WHERE username = 'tutor'
        ),

        'ENTREVISTA_ESTUDIANTE',

        'Entrevista inicial con los estudiantes involucrados para recoger información sobre el incidente.',

        '2026-09-20 11:30:00-05:00',

        '2026-09-23',

        'COMPLETADA'
    ),
    (
        (
            SELECT id
            FROM incidentes
            WHERE codigo_caso = 'INC-2026-000001'
        ),

        (
            SELECT id
            FROM usuarios
            WHERE username = 'psicologa'
        ),

        'DERIVACION_PSICOLOGIA',

        'Evaluación y acompañamiento psicopedagógico de los estudiantes involucrados.',

        '2026-09-21 09:00:00-05:00',

        '2026-09-28',

        'EN_PROCESO'
    ),
    (
        (
            SELECT id
            FROM incidentes
            WHERE codigo_caso = 'INC-2026-000002'
        ),

        (
            SELECT id
            FROM usuarios
            WHERE username = 'bienestar'
        ),

        'ENTREVISTA_PADRES',

        'Reunión con los apoderados para informar sobre el caso y establecer medidas de acompañamiento.',

        '2026-09-23 15:00:00-05:00',

        '2026-09-30',

        'PENDIENTE'
    );


-- *************************************************************************************
-- 10. NOTIFICACIONES
-- *************************************************************************************

INSERT INTO notificaciones (
    incidente_id,
    apoderado_id,
    enviado_por_id,
    canal,
    asunto,
    mensaje,
    fecha_envio,
    estado_envio,
    id_transaccion_externo
)
VALUES
    (
        (
            SELECT id
            FROM incidentes
            WHERE codigo_caso = 'INC-2026-000001'
        ),

        (
            SELECT ea.apoderado_id
            FROM estudiante_apoderado ea
                     INNER JOIN estudiantes e
                                ON e.id = ea.estudiante_id
            WHERE e.codigo_siagie = 'SIAGIE000001'
              AND ea.es_contacto_principal = TRUE
        ),

        (
            SELECT id
            FROM usuarios
            WHERE username = 'tutor'
        ),

        'EMAIL',

        'Comunicación sobre incidente de convivencia escolar',

        'Se comunica al apoderado el registro de un incidente de convivencia escolar y las acciones de seguimiento correspondientes.',

        '2026-09-20 13:00:00-05:00',

        'ENVIADO',

        'DEV-SEED-000001'
    ),
    (
        (
            SELECT id
            FROM incidentes
            WHERE codigo_caso = 'INC-2026-000002'
        ),

        (
            SELECT ea.apoderado_id
            FROM estudiante_apoderado ea
                     INNER JOIN estudiantes e
                                ON e.id = ea.estudiante_id
            WHERE e.codigo_siagie = 'SIAGIE000002'
              AND ea.es_contacto_principal = TRUE
        ),

        (
            SELECT id
            FROM usuarios
            WHERE username = 'bienestar'
        ),

        'EMAIL',

        'Seguimiento de caso de convivencia escolar',

        'Se informa al apoderado sobre el seguimiento del caso y las medidas de acompañamiento programadas.',

        '2026-09-23 16:00:00-05:00',

        'ENTREGADO',

        'DEV-SEED-000002'
    );


-- *************************************************************************************
-- FIN V2
-- *************************************************************************************