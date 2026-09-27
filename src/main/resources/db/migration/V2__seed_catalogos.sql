-- =====================================================================
-- SIGECE - Migración V2: Catálogos base (roles y tipos de incidencia)
-- =====================================================================

INSERT INTO roles (nombre, descripcion) VALUES
    ('DIRECTOR',              'Personal directivo de la institución educativa'),
    ('TUTOR',                 'Docente tutor de aula, responsable del seguimiento del estudiante'),
    ('COMITE_BIENESTAR',      'Integrante del Comité de Gestión del Bienestar Escolar (D.S. N.° 004-2018-MINEDU)'),
    ('PSICOLOGO',             'Profesional de apoyo psicopedagógico'),
    ('ADMINISTRADOR_SISTEMA', 'Administrador técnico del sistema');

INSERT INTO tipos_incidencia (nombre, categoria_siseve, descripcion) VALUES
    ('Violencia física',        'FISICA',       'Agresión que causa daño corporal'),
    ('Violencia psicológica',   'PSICOLOGICA',  'Agresión verbal, humillación o exclusión'),
    ('Violencia sexual',        'SEXUAL',       'Cualquier forma de acoso o abuso sexual'),
    ('Acoso escolar (bullying)','BULLYING',     'Hostigamiento reiterado entre pares'),
    ('Ciberacoso',              'CIBERACOSO',   'Hostigamiento a través de medios digitales'),
    ('Negligencia/Abandono',    'NEGLIGENCIA',  'Desatención de necesidades básicas del estudiante');
