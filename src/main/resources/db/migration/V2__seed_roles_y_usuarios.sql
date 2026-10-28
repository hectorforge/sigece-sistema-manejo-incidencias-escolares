-- =====================================================================================
-- SIGECE - MIGRACIÓN V2: SEED DATA PARA ROLES Y USUARIOS
-- Fecha: 25/10/2026
-- Objetivo: Poblar el catálogo de roles mapeados con RolNombre y usuarios de prueba.
-- =====================================================================================
INSERT INTO roles (nombre, descripcion) VALUES
    ('ROLE_ADMINISTRADOR', 'Personal directivo o administrativo con acceso total, gestión de usuarios, cierre formal y derivación a SíseVe.'),
    ('ROLE_TUTOR',          'Docente tutor de aula encargado del registro de incidencias, involucrados y consulta de sus secciones asignadas.'),
    ('ROLE_PSICOLOGO',      'Profesional de psicología que realiza el seguimiento clínico/formativo, entrevistas, citaciones y resolución de casos.'),
    ('ROLE_INVITADO',       'Usuario institucional sin permisos operativos en el flujo de incidencias.');

INSERT INTO usuarios (
    nombres,
    apellidos,
    dni,
    correo,
    username,
    password_hash,
    telefono,
    role_id,
    activo,
    debe_cambiar_password,
    intentos_fallidos,
    created_at,
    updated_at
) VALUES
    (
        'Carlos Alberto',
        'Mendoza Paredes',
        '10234567',
        'carlos.mendoza@minedu.gob.pe',
        'admin.mendoza',
        crypt('Password@123', gen_salt('bf', 10)),
        '987111222',
        (SELECT id FROM roles WHERE nombre = 'ROLE_ADMINISTRADOR'),
        TRUE,
        FALSE,
        0,
        clock_timestamp(),
        clock_timestamp()
    ),

    -- Usuario Tutor 1 (CU05, CU10, CU11, CU12, CU13, CU14)
    (
        'María Elena',
        'Vargas Rojas',
        '45678912',
        'maria.vargas@minedu.gob.pe',
        'tutor.vargas',
        crypt('Password@123', gen_salt('bf', 10)),
        '987333444',
        (SELECT id FROM roles WHERE nombre = 'ROLE_TUTOR'),
        TRUE,
        FALSE,
        0,
        clock_timestamp(),
        clock_timestamp()
    ),

    -- Usuario Tutor 2
    (
        'Jorge Luis',
        'Castro Huamán',
        '41239874',
        'jorge.castro@minedu.gob.pe',
        'tutor.castro',
        crypt('Password@123', gen_salt('bf', 10)),
        '987555666',
        (SELECT id FROM roles WHERE nombre = 'ROLE_TUTOR'),
        TRUE,
        FALSE,
        0,
        clock_timestamp(),
        clock_timestamp()
    ),

    -- Usuario Psicólogo 1 (CU11, CU15, CU16, resolución técnica de incidentes)
    (
        'Ana Lucía',
        'Flores Quispe',
        '70891234',
        'ana.flores@minedu.gob.pe',
        'psico.flores',
        crypt('Password@123', gen_salt('bf', 10)),
        '987777888',
        (SELECT id FROM roles WHERE nombre = 'ROLE_PSICOLOGO'),
        TRUE,
        FALSE,
        0,
        clock_timestamp(),
        clock_timestamp()
    ),

    -- Usuario Psicólogo 2
    (
        'David Fernando',
        'Salazar Soto',
        '72345678',
        'david.salazar@minedu.gob.pe',
        'psico.salazar',
        crypt('Password@123', gen_salt('bf', 10)),
        '987999000',
        (SELECT id FROM roles WHERE nombre = 'ROLE_PSICOLOGO'),
        TRUE,
        FALSE,
        0,
        clock_timestamp(),
        clock_timestamp()
    ),

    -- Usuario Invitado (Sin permisos para probar 403 Forbidden)
    (
        'Pedro Manuel',
        'Gómez Rivas',
        '48765432',
        'pedro.gomez@minedu.gob.pe',
        'invitado.gomez',
        crypt('Password@123', gen_salt('bf', 10)),
        '987000111',
        (SELECT id FROM roles WHERE nombre = 'ROLE_INVITADO'),
        TRUE,
        FALSE,
        0,
        clock_timestamp(),
        clock_timestamp()
    ),

    -- Usuario Inactivo (Para probar rechazo por cuenta deshabilitada: ACCOUNT_DISABLED)
    (
        'Rosa Inés',
        'Chávez Morales',
        '43567890',
        'rosa.chavez@minedu.gob.pe',
        'inactivo.chavez',
        crypt('Password@123', gen_salt('bf', 10)),
        '987222333',
        (SELECT id FROM roles WHERE nombre = 'ROLE_TUTOR'),
        FALSE, -- ACTIVO = FALSE
        FALSE,
        0,
        clock_timestamp(),
        clock_timestamp()
    );

-- -------------------------------------------------------------------------------------
-- 3. ASIGNACIÓN INICIAL DE TUTORÍA (Para pruebas del alcance RN04 / CU05 / CU11)
-- -------------------------------------------------------------------------------------
INSERT INTO tutor_aula (usuario_id, nivel, grado, seccion, anio_escolar, activo) VALUES
    -- La tutora María Vargas tiene asignado 1° "A" y 2° "B" de Secundaria para el 2026
    (
        (SELECT id FROM usuarios WHERE username = 'tutor.vargas'),
        'SECUNDARIA', '1', 'A', 2026, TRUE
    ),
    (
        (SELECT id FROM usuarios WHERE username = 'tutor.vargas'),
        'SECUNDARIA', '2', 'B', 2026, TRUE
    ),
    -- El tutor Jorge Castro tiene asignado 3° "A" de Secundaria para el 2026
    (
        (SELECT id FROM usuarios WHERE username = 'tutor.castro'),
        'SECUNDARIA', '3', 'A', 2026, TRUE
    );

-- -------------------------------------------------------------------------------------
-- 4. INICIALIZACIÓN DEL CONTADOR ANUAL DE INCIDENTES (Año 2026)
-- -------------------------------------------------------------------------------------
INSERT INTO contadores_incidente (anio, ultimo_numero)
VALUES (2026, 0)
ON CONFLICT (anio) DO NOTHING;
