-- =====================================================================================
-- SIGECE - MIGRACIÓN V3: DATOS MAESTROS
-- Autores : Hector Julio Hernandez Tacas, Alex Ayamamani
-- Fecha: 25/10/2026
-- Contenido: Catálogo SíSeVe (18 tipos), Plantillas WhatsApp, 10 Estudiantes,
--            Apoderados y Asignaciones familiares con Contacto Principal.
-- =====================================================================================

-- -------------------------------------------------------------------------------------
-- 1. TIPOS DE INCIDENCIA (Mapeo 1 a 1 con CategoriaSiseve)
-- -------------------------------------------------------------------------------------
INSERT INTO tipos_incidencia (nombre, categoria_siseve, gravedad_base, descripcion, activo) VALUES
    -- Violencia Física
    ('Castigo físico corporal', 'FIS_CASTIGO_FISICO', 'GRAVE', 'Uso de la fuerza física con intención de causar dolor o corrección corporal.', TRUE),
    ('Agresión física con lesiones', 'FIS_CON_LESIONES', 'GRAVE', 'Golpes, empujones o ataques físicos que producen daño corporal comprobable.', TRUE),
    ('Agresión física con uso de armas u objetos peligrosos', 'FIS_USO_ARMAS', 'MUY_GRAVE', 'Ataque físico empleando objetos punzocortantes, armas blancas o contundentes.', TRUE),
    ('Agresión física sin lesiones', 'FIS_SIN_LESIONES', 'MODERADO', 'Empujones, jaloneos o forcejeos que no causan lesiones visibles inmediatas.', TRUE),

    -- Violencia Psicológica
    ('Acoso escolar sostenido (Bullying)', 'PSI_ACOSO_ESCOLAR', 'GRAVE', 'Intimidación y maltrato sistemático, repetitivo y continuo entre pares.', TRUE),
    ('Aislamiento y marginación social intencional', 'PSI_AISLAMIENTO', 'MODERADO', 'Exclusión deliberada de un estudiante de actividades grupales o de clase.', TRUE),
    ('Ciberacoso escolar mediante redes sociales', 'PSI_CIBERACOSO', 'GRAVE', 'Difusión de contenido denigrante, amenazas o burlas a través de plataformas digitales.', TRUE),
    ('Discriminación por origen, género o condición', 'PSI_DISCRIMINACION', 'MODERADO', 'Trato desigual o vejatorio motivado por motivos socioeconómicos, étnicos o personales.', TRUE),
    ('Amenaza psicológica armada', 'PSI_USO_ARMAS', 'MUY_GRAVE', 'Intimidación moral o coacción extrema mostrando o insinuando el uso de armas.', TRUE),
    ('Intimidación, chantaje o coacción moral', 'PSI_INTIMIDACION', 'MODERADO', 'Presión verbal o amenazas explícitas para forzar u obligar la voluntad del menor.', TRUE),
    ('Trato humillante y denigrante público', 'PSI_TRATO_HUMILLANTE', 'MODERADO', 'Acciones o comentarios dirigidos a menoscabar la dignidad del estudiante ante sus pares.', TRUE),
    ('Agresión verbal reiterada y ofensas', 'PSI_VERVAL', 'LEVE', 'Insultos, apodos ofensivos, burlas puntuales o lenguaje despectivo sin daño físico.', TRUE),

    -- Violencia Sexual
    ('Acoso sexual escolar presencial', 'SEX_ACOSO_SEXUAL', 'GRAVE', 'Comentarios o conductas no deseadas de naturaleza sexual en el entorno escolar.', TRUE),
    ('Acoso sexual mediado por tecnologías digitales', 'SEX_ACOSO_MEDIOS_TECNOLOGICOS', 'GRAVE', 'Envío de mensajes, imágenes o solicitudes inapropiadas de índole sexual por internet.', TRUE),
    ('Hostigamiento sexual institucional o entre pares', 'SEX_HOSTIGAMIENTO_SEXUAL', 'MUY_GRAVE', 'Conducta física o verbal reiterada de naturaleza sexual que crea un ambiente hostil.', TRUE),
    ('Tocamientos indebidos o actos libidinosos', 'SEX_TOCAMIENTOS_ACTOS_SEXUALES', 'MUY_GRAVE', 'Contacto físico invasivo o tocamientos corporales no consentidos sobre el menor.', TRUE),
    ('Violación sexual o ultraje', 'SEX_VIOLACION_SEXUAL', 'MUY_GRAVE', 'Vulneración gravísima de la libertad e indemnidad sexual del menor.', TRUE),
    ('Otras formas de violencia con connotación sexual', 'SEX_VIOLENCIA_FIN_SEXUAL', 'MUY_GRAVE', 'Cualquier acto vejatorio no tipificado previamente con motivación o fin sexual.', TRUE);

-- -------------------------------------------------------------------------------------
-- 2. PLANTILLAS DE MENSAJE (WhatsApp Business Platform - RN01, RN02, RN03)
-- -------------------------------------------------------------------------------------
INSERT INTO plantillas_mensaje (codigo, nombre_whatsapp, contenido, activo) VALUES
    (
        'AVISO_INCIDENCIA',
        'plantilla_aviso_incidencia_v1',
        'Estimado(a) apoderado(a): Se le comunica que el estudiante {{1}} se encuentra involucrado en una incidencia registrada bajo el código {{2}} con nivel de gravedad {{3}}. Para mayor información, comuníquese con el área de convivencia de la IE.',
        TRUE
    ),
    (
        'CITACION',
        'plantilla_citacion_apoderado_v1',
        'Estimado(a) apoderado(a): Se solicita su presencia obligatoria en la institución educativa para tratar el caso del estudiante {{1}}. Fecha y hora programada: {{2}}. Motivo: {{3}}.',
        TRUE
    ),
    (
        'ENTREVISTA',
        'plantilla_entrevista_seguimiento_v1',
        'Estimado(a) apoderado(a): Le informamos que se ha programado una sesión de entrevista y seguimiento psicológico para el estudiante {{1}} el día {{2}} a las {{3}} horas con el responsable del caso.',
        TRUE
    ),
    (
        'CIERRE_CASO',
        'plantilla_cierre_formal_v1',
        'Estimado(a) apoderado(a): Se le notifica formalmente que el caso {{1}} correspondiente al estudiante {{2}} ha concluido su proceso de atención y ha sido CERRADO mediante acta administrativa formal.',
        TRUE
    );

-- -------------------------------------------------------------------------------------
-- 3. ESTUDIANTES (10 alumnos matriculados en aulas a cargo de tutor.vargas: 1°A y 2°B)
-- -------------------------------------------------------------------------------------
INSERT INTO estudiantes (codigo_siagie, nombres, apellidos, fecha_nacimiento, nivel, grado, seccion, activo) VALUES
    -- 1° "A" Secundaria
    ('202601001A', 'Sebastián Mateo', 'Huamán Castro',      '2014-04-12', 'SECUNDARIA', '1', 'A', TRUE),
    ('202601002A', 'Valentina Sofía', 'Quispe Navarro',     '2014-06-25', 'SECUNDARIA', '1', 'A', TRUE),
    ('202601003A', 'Diego Alejandro', 'Torres Benavides',   '2014-01-18', 'SECUNDARIA', '1', 'A', TRUE),
    ('202601004A', 'Camila Lucía',    'Reyes Alarcón',      '2014-09-05', 'SECUNDARIA', '1', 'A', TRUE),
    ('202601005A', 'Matías Gabriel',  'Zavala Morales',     '2014-11-20', 'SECUNDARIA', '1', 'A', TRUE),

    -- 2° "B" Secundaria
    ('202602001B', 'Joaquín Alonso',  'Palacios Meza',      '2013-03-15', 'SECUNDARIA', '2', 'B', TRUE),
    ('202602002B', 'Mariana Isabel',  'Chávez Córdova',     '2013-08-30', 'SECUNDARIA', '2', 'B', TRUE),
    ('202602003B', 'Rodrigo Andrés',  'Sánchez Medina',     '2013-05-10', 'SECUNDARIA', '2', 'B', TRUE),
    ('202602004B', 'Fabiana Nicole',  'Gutiérrez Ríos',     '2013-10-02', 'SECUNDARIA', '2', 'B', TRUE),
    ('202602005B', 'Álvaro Martín',   'Contreras Luján',    '2013-12-14', 'SECUNDARIA', '2', 'B', TRUE);

-- -------------------------------------------------------------------------------------
-- 4. APODERADOS (14 Padres o tutores legales con teléfono para WhatsApp)
-- -------------------------------------------------------------------------------------
INSERT INTO apoderados (nombres, apellidos, tipo_documento, numero_documento, telefono, correo) VALUES
    ('Roberto Carlos',  'Huamán Soto',        'DNI', '09811223', '983943719', 'roberto.huaman@gmail.com'),
    ('Patricia Elena',  'Castro de Huamán',   'DNI', '09811224', '981913119', 'patricia.castro@gmail.com'),
    ('Marcos Antonio',  'Quispe Flores',      'DNI', '10922334', '900943019', 'marcos.quispe@gmail.com'),
    ('Elena Beatriz',   'Navarro Ramos',      'DNI', '10922335', '983943019', 'elena.navarro@gmail.com'),
    ('Hugo Hernán',     'Torres Vega',        'DNI', '21033445', '980943019', 'hugo.torres@gmail.com'),
    ('Rosa Angélica',   'Alarcón Peña',       'DNI', '32144556', '983943019', 'rosa.alarcon@gmail.com'),
    ('César Augusto',   'Zavala Pinto',       'DNI', '43255667', '983943019', 'cesar.zavala@gmail.com'),
    ('Manuel Jesús',    'Palacios Cárdenas',  'DNI', '54366778', '983949019', 'manuel.palacios@gmail.com'),
    ('Silvia Raquel',   'Meza de Palacios',   'DNI', '54366779', '981946019', 'silvia.meza@gmail.com'),
    ('Víctor Raúl',     'Chávez Salazar',     'DNI', '65477889', '983543019', 'victor.chavez@gmail.com'),
    ('Nancy Graciela',  'Medina Cruz',        'DNI', '76588990', '982943019', 'nancy.medina@gmail.com'),
    ('Jorge Andrés',    'Gutiérrez Prado',    'DNI', '87699001', '983949119', 'jorge.gutierrez@gmail.com'),
    ('Carmen Julia',    'Ríos Villalobos',    'DNI', '87699002', '983943119', 'carmen.rios@gmail.com'),
    ('Luis Alberto',    'Contreras Dávila',   'DNI', '98700112', '983943319', 'luis.contreras@gmail.com');

-- -------------------------------------------------------------------------------------
-- 5. ASIGNACIÓN ESTUDIANTE - APODERADO
-- Nota: Exactamente 1 contacto principal por estudiante (es_contacto_principal = TRUE)
-- -------------------------------------------------------------------------------------
INSERT INTO estudiante_apoderado (estudiante_id, apoderado_id, parentesco, es_contacto_principal) VALUES
    -- Estudiante 1: Sebastián Huamán (Papá contacto principal, Mamá secundario)
    ((SELECT id FROM estudiantes WHERE codigo_siagie = '202601001A'),
     (SELECT id FROM apoderados WHERE numero_documento = '09811223'), 'PADRE', TRUE),
    ((SELECT id FROM estudiantes WHERE codigo_siagie = '202601001A'),
     (SELECT id FROM apoderados WHERE numero_documento = '09811224'), 'MADRE', FALSE),

    -- Estudiante 2: Valentina Quispe (Mamá contacto principal, Papá secundario)
    ((SELECT id FROM estudiantes WHERE codigo_siagie = '202601002A'),
     (SELECT id FROM apoderados WHERE numero_documento = '10922335'), 'MADRE', TRUE),
    ((SELECT id FROM estudiantes WHERE codigo_siagie = '202601002A'),
     (SELECT id FROM apoderados WHERE numero_documento = '10922334'), 'PADRE', FALSE),

    -- Estudiante 3: Diego Torres (Papá)
    ((SELECT id FROM estudiantes WHERE codigo_siagie = '202601003A'),
     (SELECT id FROM apoderados WHERE numero_documento = '21033445'), 'PADRE', TRUE),

    -- Estudiante 4: Camila Reyes (Mamá)
    ((SELECT id FROM estudiantes WHERE codigo_siagie = '202601004A'),
     (SELECT id FROM apoderados WHERE numero_documento = '32144556'), 'MADRE', TRUE),

    -- Estudiante 5: Matías Zavala (Papá)
    ((SELECT id FROM estudiantes WHERE codigo_siagie = '202601005A'),
     (SELECT id FROM apoderados WHERE numero_documento = '43255667'), 'PADRE', TRUE),

    -- Estudiante 6: Joaquín Palacios (Mamá contacto principal, Papá secundario)
    ((SELECT id FROM estudiantes WHERE codigo_siagie = '202602001B'),
     (SELECT id FROM apoderados WHERE numero_documento = '54366779'), 'MADRE', TRUE),
    ((SELECT id FROM estudiantes WHERE codigo_siagie = '202602001B'),
     (SELECT id FROM apoderados WHERE numero_documento = '54366778'), 'PADRE', FALSE),

    -- Estudiante 7: Mariana Chávez (Papá)
    ((SELECT id FROM estudiantes WHERE codigo_siagie = '202602002B'),
     (SELECT id FROM apoderados WHERE numero_documento = '65477889'), 'PADRE', TRUE),

    -- Estudiante 8: Rodrigo Sánchez (Mamá)
    ((SELECT id FROM estudiantes WHERE codigo_siagie = '202602003B'),
     (SELECT id FROM apoderados WHERE numero_documento = '76588990'), 'MADRE', TRUE),

    -- Estudiante 9: Fabiana Gutiérrez (Papá contacto principal, Mamá secundario)
    ((SELECT id FROM estudiantes WHERE codigo_siagie = '202602004B'),
     (SELECT id FROM apoderados WHERE numero_documento = '87699001'), 'PADRE', TRUE),
    ((SELECT id FROM estudiantes WHERE codigo_siagie = '202602004B'),
     (SELECT id FROM apoderados WHERE numero_documento = '87699002'), 'MADRE', FALSE),

    -- Estudiante 10: Álvaro Contreras (Papá)
    ((SELECT id FROM estudiantes WHERE codigo_siagie = '202602005B'),
     (SELECT id FROM apoderados WHERE numero_documento = '98700112'), 'PADRE', TRUE);
