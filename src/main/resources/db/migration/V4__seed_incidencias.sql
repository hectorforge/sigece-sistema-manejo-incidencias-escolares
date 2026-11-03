
-- =====================================================================================
-- SIGECE - MIGRACIÓN V4: SEED DATA DE INCIDENCIAS Y SEGUIMIENTO
-- Autores: Hector Julio Hernandez Tacas, Alex Ayamamani
-- Fecha: 02/11/2026
-- Objetivo:
--   - Registrar 10 incidencias de distintas características.
--   - Asociar estudiantes y usuarios involucrados.
--   - Registrar acciones de seguimiento.
--   - Registrar notificaciones a los apoderados.
-- Dependencia: V1 (estructura), V2 (usuarios y roles), V3 (datos maestros).
-- =====================================================================================

BEGIN;

-- =====================================================================================
-- 1. INCIDENCIAS
-- =====================================================================================
-- Se utilizan UUID fijos para que las relaciones posteriores sean estables.
-- Los códigos respetan el formato: INC-YYYY-NNNNNN.
--
-- Estados incluidos:
-- REGISTRADO, EN_INVESTIGACION, EN_SEGUIMIENTO, RESUELTO y CERRADO.
--
-- Las incidencias RESUELTAS y CERRADAS tienen fecha_resolucion y resuelto_por_id.
-- Las incidencias CERRADAS también tienen acta_cierre, fecha_cierre y cerrado_por_id.
-- =====================================================================================

INSERT INTO incidentes (
    id,
    codigo_caso,
    tipo_incidencia_id,
    gravedad,
    descripcion,
    lugar_ocurrencia,
    medio_ocurrencia,
    fecha_ocurrencia,
    fecha_registro,
    estado,
    reportado_por_id,
    responsable_actual_id,
    notificar_apoderado,
    fecha_resolucion,
    resuelto_por_id,
    acta_cierre,
    conclusiones_finales,
    fecha_cierre,
    cerrado_por_id,
    reportado_siseve,
    fecha_reporte_siseve,
    codigo_siseve,
    observaciones_siseve,
    derivado_por_id,
    created_at,
    updated_at
)
VALUES

-- -------------------------------------------------------------------------------------
-- INC-2026-000001
-- Agresión física sin lesiones entre estudiantes.
-- Estado: EN_SEGUIMIENTO.
-- -------------------------------------------------------------------------------------
(
    'a1000000-0000-4000-8000-000000000001',
    'INC-2026-000001',
    (SELECT id FROM tipos_incidencia WHERE nombre = 'Agresión física sin lesiones'),
    'MODERADO',
    'Durante el recreo, en el patio principal, se produjo un altercado entre dos estudiantes. Según los testimonios recogidos, uno de ellos empujó al otro luego de una discusión relacionada con un desacuerdo durante un juego. El estudiante afectado no presentó lesiones visibles. El tutor intervino inmediatamente, separó a los involucrados y comunicó el hecho al área responsable de convivencia escolar. Se requiere seguimiento para prevenir nuevos enfrentamientos y fortalecer la resolución pacífica de conflictos.',
    'Patio principal de la institución educativa',
    'PRESENCIAL',
    '2026-10-19 10:15:00-05',
    '2026-10-19 10:35:00-05',
    'EN_SEGUIMIENTO',
    (SELECT id FROM usuarios WHERE username = 'tutor.vargas'),
    (SELECT id FROM usuarios WHERE username = 'psico.flores'),
    TRUE,
    NULL,
    NULL,
    NULL,
    NULL,
    NULL,
    NULL,
    FALSE,
    NULL,
    NULL,
    NULL,
    NULL,
    '2026-10-19 10:35:00-05',
    '2026-10-19 10:35:00-05'
),

-- -------------------------------------------------------------------------------------
-- INC-2026-000002
-- Acoso escolar sostenido (Bullying).
-- Estado: RESUELTO.
-- -------------------------------------------------------------------------------------
(
    'a1000000-0000-4000-8000-000000000002',
    'INC-2026-000002',
    (SELECT id FROM tipos_incidencia WHERE nombre = 'Acoso escolar sostenido (Bullying)'),
    'GRAVE',
    'Se recibió un reporte sobre conductas reiteradas de intimidación hacia una estudiante durante varias semanas. Los hechos descritos incluyen burlas frecuentes, comentarios despectivos y exclusión deliberada de actividades grupales. La estudiante manifestó sentirse incómoda y evitar determinados espacios durante los recreos. El tutor recopiló información inicial y derivó el caso al área de psicología para la evaluación correspondiente. Se realizaron entrevistas, orientación individual y acciones de intervención con los estudiantes involucrados. Se establecieron compromisos de convivencia y medidas de protección y seguimiento.',
    'Aula de 1° A de secundaria y patio principal',
    'PRESENCIAL',
    '2026-10-20 08:30:00-05',
    '2026-10-20 09:00:00-05',
    'RESUELTO',
    (SELECT id FROM usuarios WHERE username = 'tutor.vargas'),
    (SELECT id FROM usuarios WHERE username = 'psico.flores'),
    TRUE,
    '2026-10-28 16:00:00-05',
    (SELECT id FROM usuarios WHERE username = 'psico.flores'),
    NULL,
    'Se realizaron entrevistas individuales, orientación psicológica y compromisos de convivencia. El seguimiento inicial permitió verificar una mejora en la interacción entre los estudiantes.',
    NULL,
    NULL,
    TRUE,
    '2026-10-21 11:00:00-05',
    'SSE-2026-000245',
    'Caso comunicado para registro y seguimiento conforme al procedimiento institucional. Se conserva la documentación de las actuaciones realizadas.',
    (SELECT id FROM usuarios WHERE username = 'admin.mendoza'),
    '2026-10-20 09:00:00-05',
    '2026-10-28 16:00:00-05'
),

-- -------------------------------------------------------------------------------------
-- INC-2026-000003
-- Ciberacoso escolar mediante redes sociales.
-- Estado: EN_INVESTIGACION.
-- -------------------------------------------------------------------------------------
(
    'a1000000-0000-4000-8000-000000000003',
    'INC-2026-000003',
    (SELECT id FROM tipos_incidencia WHERE nombre = 'Ciberacoso escolar mediante redes sociales'),
    'GRAVE',
    'Una estudiante comunicó que recibió mensajes ofensivos y burlas a través de una plataforma digital utilizada por compañeros de aula. También señaló que se compartieron comentarios denigrantes relacionados con su apariencia personal. La estudiante presentó capturas de pantalla como evidencia inicial. El tutor registró el reporte y comunicó el caso al responsable de psicología. Se encuentra pendiente la evaluación integral de los hechos, la identificación de los participantes y la adopción de medidas para evitar la difusión de contenido perjudicial.',
    'Entorno digital y aula de 1° A de secundaria',
    'DIGITAL',
    '2026-10-21 18:20:00-05',
    '2026-10-22 08:10:00-05',
    'EN_INVESTIGACION',
    (SELECT id FROM usuarios WHERE username = 'tutor.vargas'),
    (SELECT id FROM usuarios WHERE username = 'psico.flores'),
    TRUE,
    NULL,
    NULL,
    NULL,
    NULL,
    NULL,
    NULL,
    TRUE,
    '2026-10-22 11:30:00-05',
    'SSE-2026-000246',
    'Se comunicó el caso para su registro y evaluación. Se encuentran en proceso las entrevistas y la revisión de la información proporcionada por la estudiante.',
    (SELECT id FROM usuarios WHERE username = 'admin.mendoza'),
    '2026-10-22 08:10:00-05',
    '2026-10-22 11:30:00-05'
),

-- -------------------------------------------------------------------------------------
-- INC-2026-000004
-- Discriminación por origen, género o condición.
-- Estado: EN_SEGUIMIENTO.
-- -------------------------------------------------------------------------------------
(
    'a1000000-0000-4000-8000-000000000004',
    'INC-2026-000004',
    (SELECT id FROM tipos_incidencia WHERE nombre = 'Discriminación por origen, género o condición'),
    'MODERADO',
    'Se reportaron expresiones discriminatorias dirigidas hacia un estudiante durante una actividad grupal. Los comentarios estuvieron relacionados con su procedencia familiar y generaron incomodidad en el estudiante afectado. El docente tutor intervino, informó al área de convivencia y solicitó orientación para abordar el hecho desde un enfoque preventivo y formativo. Se realizaron conversaciones con los participantes y se programaron actividades de sensibilización sobre respeto, diversidad y convivencia escolar.',
    'Aula de 2° B de secundaria',
    'PRESENCIAL',
    '2026-10-22 09:40:00-05',
    '2026-10-22 10:00:00-05',
    'EN_SEGUIMIENTO',
    (SELECT id FROM usuarios WHERE username = 'tutor.vargas'),
    (SELECT id FROM usuarios WHERE username = 'psico.salazar'),
    TRUE,
    NULL,
    NULL,
    NULL,
    NULL,
    NULL,
    NULL,
    FALSE,
    NULL,
    NULL,
    NULL,
    NULL,
    '2026-10-22 10:00:00-05',
    '2026-10-22 10:00:00-05'
),

-- -------------------------------------------------------------------------------------
-- INC-2026-000005
-- Agresión verbal reiterada y ofensas.
-- Estado: REGISTRADO.
-- -------------------------------------------------------------------------------------
(
    'a1000000-0000-4000-8000-000000000005',
    'INC-2026-000005',
    (SELECT id FROM tipos_incidencia WHERE nombre = 'Agresión verbal reiterada y ofensas'),
    'LEVE',
    'El docente tutor comunicó que durante una actividad académica se produjeron expresiones ofensivas entre dos estudiantes. El hecho consistió en insultos y comentarios despectivos relacionados con el desempeño escolar. La situación fue interrumpida por el docente y se registró el reporte para su evaluación. Se requiere verificar si se trata de un hecho aislado o si existen antecedentes de comportamientos similares.',
    'Aula de 1° A de secundaria',
    'PRESENCIAL',
    '2026-10-23 11:20:00-05',
    '2026-10-23 11:45:00-05',
    'REGISTRADO',
    (SELECT id FROM usuarios WHERE username = 'tutor.vargas'),
    (SELECT id FROM usuarios WHERE username = 'psico.flores'),
    FALSE,
    NULL,
    NULL,
    NULL,
    NULL,
    NULL,
    NULL,
    FALSE,
    NULL,
    NULL,
    NULL,
    NULL,
    '2026-10-23 11:45:00-05',
    '2026-10-23 11:45:00-05'
),

-- -------------------------------------------------------------------------------------
-- INC-2026-000006
-- Agresión física con lesiones.
-- Estado: CERRADO.
-- -------------------------------------------------------------------------------------
(
    'a1000000-0000-4000-8000-000000000006',
    'INC-2026-000006',
    (SELECT id FROM tipos_incidencia WHERE nombre = 'Agresión física con lesiones'),
    'GRAVE',
    'Durante una actividad deportiva, se produjo una agresión física entre estudiantes. Uno de ellos recibió un golpe que ocasionó una lesión que requirió evaluación y atención por parte del personal responsable de la institución. Se comunicó inmediatamente el hecho a la dirección y se contactó al apoderado del estudiante afectado. Se realizaron las entrevistas correspondientes, se documentaron las actuaciones y se adoptaron medidas de protección. El expediente cuenta con las actuaciones de seguimiento y el acta administrativa de cierre.',
    'Losa deportiva de la institución educativa',
    'PRESENCIAL',
    '2026-10-18 10:00:00-05',
    '2026-10-18 10:25:00-05',
    'CERRADO',
    (SELECT id FROM usuarios WHERE username = 'tutor.castro'),
    (SELECT id FROM usuarios WHERE username = 'psico.flores'),
    TRUE,
    '2026-10-25 15:00:00-05',
    (SELECT id FROM usuarios WHERE username = 'psico.flores'),
    'Acta de cierre: se deja constancia de las entrevistas realizadas, las medidas de protección adoptadas, la comunicación con los apoderados y los compromisos establecidos para evitar la repetición de los hechos.',
    'Se completaron las acciones institucionales previstas para este expediente. Se documentaron las medidas adoptadas y se verificó el cumplimiento de los compromisos iniciales. El caso queda cerrado administrativamente, sin perjuicio de las actuaciones que correspondan ante nuevos hechos.',
    '2026-10-26 10:00:00-05',
    (SELECT id FROM usuarios WHERE username = 'admin.mendoza'),
    TRUE,
    '2026-10-19 12:00:00-05',
    'SSE-2026-000247',
    'Se registró la derivación correspondiente. Se documentaron las actuaciones institucionales y la atención brindada al estudiante afectado.',
    (SELECT id FROM usuarios WHERE username = 'admin.mendoza'),
    '2026-10-18 10:25:00-05',
    '2026-10-26 10:00:00-05'
),

-- -------------------------------------------------------------------------------------
-- INC-2026-000007
-- Hostigamiento sexual institucional o entre pares.
-- Estado: EN_INVESTIGACION.
-- -------------------------------------------------------------------------------------
(
    'a1000000-0000-4000-8000-000000000007',
    'INC-2026-000007',
    (SELECT id FROM tipos_incidencia WHERE nombre = 'Hostigamiento sexual institucional o entre pares'),
    'MUY_GRAVE',
    'Se recibió una comunicación reservada sobre presuntas conductas de connotación sexual no deseadas en el entorno escolar. La institución registró el reporte con carácter reservado y activó la atención correspondiente, priorizando la protección, privacidad y bienestar del estudiante. El caso fue derivado al responsable designado y a la dirección para la aplicación de los protocolos institucionales y la comunicación con las instancias competentes. Se evita incorporar detalles personales sensibles en esta descripción general.',
    'Entorno escolar - ubicación específica bajo reserva',
    'PRESENCIAL',
    '2026-10-24 09:00:00-05',
    '2026-10-24 09:20:00-05',
    'EN_INVESTIGACION',
    (SELECT id FROM usuarios WHERE username = 'tutor.castro'),
    (SELECT id FROM usuarios WHERE username = 'psico.flores'),
    TRUE,
    NULL,
    NULL,
    NULL,
    NULL,
    NULL,
    NULL,
    TRUE,
    '2026-10-24 12:00:00-05',
    'SSE-2026-000248',
    'Reporte derivado para la atención conforme a los protocolos correspondientes. Información personal y documentación detallada bajo acceso restringido.',
    (SELECT id FROM usuarios WHERE username = 'admin.mendoza'),
    '2026-10-24 09:20:00-05',
    '2026-10-24 12:00:00-05'
),

-- -------------------------------------------------------------------------------------
-- INC-2026-000008
-- Intimidación, chantaje o coacción moral.
-- Estado: RESUELTO.
-- -------------------------------------------------------------------------------------
(
    'a1000000-0000-4000-8000-000000000008',
    'INC-2026-000008',
    (SELECT id FROM tipos_incidencia WHERE nombre = 'Intimidación, chantaje o coacción moral'),
    'MODERADO',
    'Un estudiante informó que otro compañero le exigía entregar parte de sus materiales escolares bajo amenazas de excluirlo de su grupo de amigos. El tutor recibió el reporte y realizó una primera intervención para detener la conducta. Posteriormente, psicología efectuó entrevistas individuales y trabajó con los estudiantes involucrados en el reconocimiento de conductas de intimidación, establecimiento de límites y resolución pacífica de conflictos. Se verificó el cumplimiento de los compromisos establecidos.',
    'Aula de 2° B de secundaria',
    'PRESENCIAL',
    '2026-10-25 08:40:00-05',
    '2026-10-25 09:00:00-05',
    'RESUELTO',
    (SELECT id FROM usuarios WHERE username = 'tutor.vargas'),
    (SELECT id FROM usuarios WHERE username = 'psico.salazar'),
    TRUE,
    '2026-10-29 14:30:00-05',
    (SELECT id FROM usuarios WHERE username = 'psico.salazar'),
    NULL,
    'Se realizaron entrevistas individuales y una intervención formativa. Los estudiantes asumieron compromisos de respeto y convivencia. En la revisión de seguimiento no se reportaron nuevos hechos relacionados con este incidente.',
    NULL,
    NULL,
    FALSE,
    NULL,
    NULL,
    NULL,
    NULL,
    '2026-10-25 09:00:00-05',
    '2026-10-29 14:30:00-05'
),

-- -------------------------------------------------------------------------------------
-- INC-2026-000009
-- Aislamiento y marginación social intencional.
-- Estado: EN_SEGUIMIENTO.
-- -------------------------------------------------------------------------------------
(
    'a1000000-0000-4000-8000-000000000009',
    'INC-2026-000009',
    (SELECT id FROM tipos_incidencia WHERE nombre = 'Aislamiento y marginación social intencional'),
    'MODERADO',
    'Se comunicó que un estudiante viene siendo excluido de manera intencional de determinadas actividades grupales. El reporte señala que algunos compañeros organizan equipos sin permitir su participación y realizan comentarios que refuerzan su aislamiento. El tutor registró el caso y solicitó intervención del área de psicología. Se planificaron actividades de integración y observación de la dinámica del aula para determinar la continuidad de la conducta y evaluar el impacto en el bienestar del estudiante.',
    'Aula de 2° B de secundaria',
    'PRESENCIAL',
    '2026-10-26 10:10:00-05',
    '2026-10-26 10:30:00-05',
    'EN_SEGUIMIENTO',
    (SELECT id FROM usuarios WHERE username = 'tutor.vargas'),
    (SELECT id FROM usuarios WHERE username = 'psico.salazar'),
    TRUE,
    NULL,
    NULL,
    NULL,
    NULL,
    NULL,
    NULL,
    FALSE,
    NULL,
    NULL,
    NULL,
    NULL,
    '2026-10-26 10:30:00-05',
    '2026-10-26 10:30:00-05'
),

-- -------------------------------------------------------------------------------------
-- INC-2026-000010
-- Castigo físico corporal.
-- Estado: CERRADO.
-- -------------------------------------------------------------------------------------
(
    'a1000000-0000-4000-8000-000000000010',
    'INC-2026-000010',
    (SELECT id FROM tipos_incidencia WHERE nombre = 'Castigo físico corporal'),
    'GRAVE',
    'Se recibió un reporte sobre un presunto acto de castigo físico corporal ocurrido en el contexto escolar. La institución registró la comunicación, priorizó la protección del estudiante y puso los hechos en conocimiento de la dirección y del responsable de convivencia. Se documentaron las actuaciones institucionales, las comunicaciones efectuadas y las medidas de protección implementadas. El caso fue derivado para su atención conforme a los procedimientos correspondientes. Se mantiene la reserva de los datos personales y de los detalles sensibles.',
    'Entorno escolar - ubicación específica bajo reserva',
    'PRESENCIAL',
    '2026-10-27 08:00:00-05',
    '2026-10-27 08:20:00-05',
    'CERRADO',
    (SELECT id FROM usuarios WHERE username = 'tutor.castro'),
    (SELECT id FROM usuarios WHERE username = 'psico.flores'),
    TRUE,
    '2026-10-30 15:00:00-05',
    (SELECT id FROM usuarios WHERE username = 'psico.flores'),
    'Acta de cierre: se documentan las medidas de protección, las comunicaciones realizadas y la derivación del caso a las instancias competentes para la atención correspondiente.',
    'Se completó el registro de las actuaciones institucionales y se formalizó el cierre administrativo del expediente. La derivación y las medidas de protección quedan documentadas para garantizar la continuidad de las actuaciones que correspondan.',
    '2026-10-31 10:00:00-05',
    (SELECT id FROM usuarios WHERE username = 'admin.mendoza'),
    TRUE,
    '2026-10-27 12:00:00-05',
    'SSE-2026-000249',
    'Caso derivado para atención conforme a los protocolos aplicables. Información detallada de acceso restringido por tratarse de un estudiante menor de edad.',
    (SELECT id FROM usuarios WHERE username = 'admin.mendoza'),
    '2026-10-27 08:20:00-05',
    '2026-10-31 10:00:00-05'
)
    ON CONFLICT (id) DO NOTHING;


-- =====================================================================================
-- 2. INCIDENTE_INVOLUCRADOS
-- =====================================================================================
-- Se registran los estudiantes participantes de cada incidente.
-- La relación utiliza los códigos SIAGIE existentes en V3.
-- Los roles permitidos son VICTIMA, AGRESOR y TESTIGO.
-- =====================================================================================

INSERT INTO incidente_involucrados (
    incidente_id,
    estudiante_id,
    usuario_id,
    rol_involucrado,
    observaciones
)
VALUES

-- Incidente 1: agresión física sin lesiones.
(
    'a1000000-0000-4000-8000-000000000001',
    (SELECT id FROM estudiantes WHERE codigo_siagie = '202601001A'),
    NULL,
    'VICTIMA',
    'Estudiante que recibió el empujón durante el recreo. No presentó lesiones visibles.'
),
(
    'a1000000-0000-4000-8000-000000000001',
    (SELECT id FROM estudiantes WHERE codigo_siagie = '202601003A'),
    NULL,
    'AGRESOR',
    'Estudiante señalado como participante en el empujón. Pendiente de seguimiento formativo.'
),
(
    'a1000000-0000-4000-8000-000000000001',
    (SELECT id FROM estudiantes WHERE codigo_siagie = '202601002A'),
    NULL,
    'TESTIGO',
    'Estudiante que presenció el altercado durante el recreo.'
),

-- Incidente 2: bullying.
(
    'a1000000-0000-4000-8000-000000000002',
    (SELECT id FROM estudiantes WHERE codigo_siagie = '202601002A'),
    NULL,
    'VICTIMA',
    'Estudiante que comunicó conductas reiteradas de intimidación y exclusión.'
),
(
    'a1000000-0000-4000-8000-000000000002',
    (SELECT id FROM estudiantes WHERE codigo_siagie = '202601003A'),
    NULL,
    'AGRESOR',
    'Estudiante señalado en el reporte como participante en las conductas de intimidación.'
),
(
    'a1000000-0000-4000-8000-000000000002',
    (SELECT id FROM estudiantes WHERE codigo_siagie = '202601004A'),
    NULL,
    'TESTIGO',
    'Estudiante entrevistado para aportar información sobre la dinámica del aula.'
),

-- Incidente 3: ciberacoso.
(
    'a1000000-0000-4000-8000-000000000003',
    (SELECT id FROM estudiantes WHERE codigo_siagie = '202601004A'),
    NULL,
    'VICTIMA',
    'Estudiante que comunicó la recepción de mensajes ofensivos en una plataforma digital.'
),
(
    'a1000000-0000-4000-8000-000000000003',
    (SELECT id FROM estudiantes WHERE codigo_siagie = '202601005A'),
    NULL,
    'AGRESOR',
    'Estudiante señalado en la comunicación inicial. Participación pendiente de verificación.'
),

-- Incidente 4: discriminación.
(
    'a1000000-0000-4000-8000-000000000004',
    (SELECT id FROM estudiantes WHERE codigo_siagie = '202602001B'),
    NULL,
    'VICTIMA',
    'Estudiante que recibió expresiones discriminatorias durante una actividad grupal.'
),
(
    'a1000000-0000-4000-8000-000000000004',
    (SELECT id FROM estudiantes WHERE codigo_siagie = '202602002B'),
    NULL,
    'AGRESOR',
    'Estudiante señalado en el reporte inicial. Se encuentra en intervención formativa.'
),
(
    'a1000000-0000-4000-8000-000000000004',
    (SELECT id FROM estudiantes WHERE codigo_siagie = '202602003B'),
    NULL,
    'TESTIGO',
    'Estudiante que presenció los comentarios durante la actividad.'
),

-- Incidente 5: agresión verbal.
(
    'a1000000-0000-4000-8000-000000000005',
    (SELECT id FROM estudiantes WHERE codigo_siagie = '202601005A'),
    NULL,
    'VICTIMA',
    'Estudiante que recibió expresiones ofensivas durante la actividad académica.'
),
(
    'a1000000-0000-4000-8000-000000000005',
    (SELECT id FROM estudiantes WHERE codigo_siagie = '202601001A'),
    NULL,
    'AGRESOR',
    'Estudiante señalado como participante en el intercambio de expresiones ofensivas.'
),

-- Incidente 6: agresión física con lesiones.
(
    'a1000000-0000-4000-8000-000000000006',
    (SELECT id FROM estudiantes WHERE codigo_siagie = '202602001B'),
    NULL,
    'VICTIMA',
    'Estudiante que recibió la agresión física y requirió evaluación.'
),
(
    'a1000000-0000-4000-8000-000000000006',
    (SELECT id FROM estudiantes WHERE codigo_siagie = '202602003B'),
    NULL,
    'AGRESOR',
    'Estudiante señalado como participante en la agresión física.'
),
(
    'a1000000-0000-4000-8000-000000000006',
    (SELECT id FROM estudiantes WHERE codigo_siagie = '202602002B'),
    NULL,
    'TESTIGO',
    'Estudiante que presenció el incidente durante la actividad deportiva.'
),

-- Incidente 7: hostigamiento sexual.
(
    'a1000000-0000-4000-8000-000000000007',
    (SELECT id FROM estudiantes WHERE codigo_siagie = '202602004B'),
    NULL,
    'VICTIMA',
    'Estudiante protegido cuya información detallada se mantiene bajo reserva.'
),
(
    'a1000000-0000-4000-8000-000000000007',
    (SELECT id FROM estudiantes WHERE codigo_siagie = '202602005B'),
    NULL,
    'AGRESOR',
    'Persona señalada en el reporte inicial. Su participación se encuentra bajo investigación.'
),

-- Incidente 8: intimidación y coacción moral.
(
    'a1000000-0000-4000-8000-000000000008',
    (SELECT id FROM estudiantes WHERE codigo_siagie = '202602002B'),
    NULL,
    'VICTIMA',
    'Estudiante que comunicó presiones y amenazas relacionadas con sus materiales escolares.'
),
(
    'a1000000-0000-4000-8000-000000000008',
    (SELECT id FROM estudiantes WHERE codigo_siagie = '202602003B'),
    NULL,
    'AGRESOR',
    'Estudiante señalado en el reporte inicial.'
),

-- Incidente 9: aislamiento y marginación.
(
    'a1000000-0000-4000-8000-000000000009',
    (SELECT id FROM estudiantes WHERE codigo_siagie = '202602004B'),
    NULL,
    'VICTIMA',
    'Estudiante que comunicó situaciones de exclusión en actividades grupales.'
),
(
    'a1000000-0000-4000-8000-000000000009',
    (SELECT id FROM estudiantes WHERE codigo_siagie = '202602001B'),
    NULL,
    'AGRESOR',
    'Estudiante señalado como participante en las conductas de exclusión.'
),
(
    'a1000000-0000-4000-8000-000000000009',
    (SELECT id FROM estudiantes WHERE codigo_siagie = '202602005B'),
    NULL,
    'TESTIGO',
    'Estudiante que puede aportar información sobre la dinámica grupal.'
),

-- Incidente 10: castigo físico corporal.
(
    'a1000000-0000-4000-8000-000000000010',
    (SELECT id FROM estudiantes WHERE codigo_siagie = '202602005B'),
    NULL,
    'VICTIMA',
    'Estudiante cuya identidad y detalles del caso deben tratarse de forma reservada.'
)

    ON CONFLICT DO NOTHING;


-- =====================================================================================
-- 3. ACCIONES_SEGUIMIENTO
-- =====================================================================================
-- Se registran intervenciones vinculadas a cada expediente.
-- Las acciones de citación y entrevista incluyen fecha_programada.
-- Las acciones COMPLETADAS incluyen fecha_completada.
-- =====================================================================================

INSERT INTO acciones_seguimiento (
    incidente_id,
    usuario_id,
    tipo_accion,
    descripcion,
    fecha_accion,
    fecha_programada,
    fecha_proxima_revision,
    estado_accion,
    fecha_completada,
    created_at,
    updated_at
)
SELECT
    v.incidente_id,
    u.id,
    v.tipo_accion,
    v.descripcion,
    v.fecha_accion,
    v.fecha_programada,
    v.fecha_proxima_revision,
    v.estado_accion,
    v.fecha_completada,
    v.fecha_accion,
    v.fecha_accion
FROM (
         VALUES

             (
                 'a1000000-0000-4000-8000-000000000001'::UUID,
                 'psico.flores',
                 'ENTREVISTA_ESTUDIANTE',
                 'Entrevista individual con los estudiantes involucrados para conocer sus versiones de los hechos, identificar factores desencadenantes y establecer compromisos de convivencia.',
                 '2026-10-20 09:00:00-05'::TIMESTAMPTZ,
                 '2026-10-20 09:00:00-05'::TIMESTAMPTZ,
                 '2026-11-05'::DATE,
                 'COMPLETADA',
                 '2026-10-20 10:00:00-05'::TIMESTAMPTZ
             ),

             (
                 'a1000000-0000-4000-8000-000000000002'::UUID,
                 'psico.flores',
                 'ENTREVISTA_PADRES',
                 'Entrevista con los apoderados para comunicar la situación, recoger información relevante y establecer compromisos de acompañamiento familiar.',
                 '2026-10-21 15:00:00-05'::TIMESTAMPTZ,
                 '2026-10-21 15:00:00-05'::TIMESTAMPTZ,
                 NULL,
                 'COMPLETADA',
                 '2026-10-21 16:00:00-05'::TIMESTAMPTZ
             ),

             (
                 'a1000000-0000-4000-8000-000000000003'::UUID,
                 'psico.flores',
                 'APUNTE_INTERNO',
                 'Se inició la revisión de las capturas de pantalla proporcionadas y se programó la identificación de los participantes. Se debe preservar la evidencia digital y limitar su acceso al personal autorizado.',
                 '2026-10-22 12:00:00-05'::TIMESTAMPTZ,
                 NULL,
                 '2026-11-04'::DATE,
                 'EN_PROCESO',
                 NULL
             ),

             (
                 'a1000000-0000-4000-8000-000000000004'::UUID,
                 'psico.salazar',
                 'MEDIDA_CORRECTIVA_FORMATIVA',
                 'Se realizó una intervención formativa sobre respeto, igualdad de trato y prevención de expresiones discriminatorias. Se acordó observar la interacción grupal durante las siguientes semanas.',
                 '2026-10-23 10:00:00-05'::TIMESTAMPTZ,
                 NULL,
                 '2026-11-06'::DATE,
                 'COMPLETADA',
                 '2026-10-23 11:00:00-05'::TIMESTAMPTZ
             ),

             (
                 'a1000000-0000-4000-8000-000000000005'::UUID,
                 'psico.flores',
                 'APUNTE_INTERNO',
                 'Pendiente de ampliar la información con entrevistas individuales para determinar si las expresiones ofensivas constituyen un hecho aislado o una conducta reiterada.',
                 '2026-10-23 12:00:00-05'::TIMESTAMPTZ,
                 NULL,
                 '2026-11-03'::DATE,
                 'PENDIENTE',
                 NULL
             ),

             (
                 'a1000000-0000-4000-8000-000000000006'::UUID,
                 'psico.flores',
                 'ACTA_COMPROMISO',
                 'Se documentaron los compromisos de convivencia y las medidas de protección acordadas con los participantes y sus apoderados. Se verificó su cumplimiento antes de la formalización del cierre.',
                 '2026-10-25 11:00:00-05'::TIMESTAMPTZ,
                 NULL,
                 NULL,
                 'COMPLETADA',
                 '2026-10-25 12:00:00-05'::TIMESTAMPTZ
             ),

             (
                 'a1000000-0000-4000-8000-000000000007'::UUID,
                 'psico.flores',
                 'ENTREVISTA_ESTUDIANTE',
                 'Se programó una entrevista reservada para la atención y protección del estudiante. La intervención debe realizarse conforme a los protocolos correspondientes y evitando la revictimización.',
                 '2026-10-24 13:00:00-05'::TIMESTAMPTZ,
                 '2026-11-03 09:00:00-05'::TIMESTAMPTZ,
                 NULL,
                 'PENDIENTE',
                 NULL
             ),

             (
                 'a1000000-0000-4000-8000-000000000008'::UUID,
                 'psico.salazar',
                 'ENTREVISTA_ESTUDIANTE',
                 'Se efectuó una entrevista individual para identificar las circunstancias de intimidación y trabajar habilidades de comunicación, establecimiento de límites y convivencia respetuosa.',
                 '2026-10-27 10:00:00-05'::TIMESTAMPTZ,
                 '2026-10-27 10:00:00-05'::TIMESTAMPTZ,
                 NULL,
                 'COMPLETADA',
                 '2026-10-27 11:00:00-05'::TIMESTAMPTZ
             ),

             (
                 'a1000000-0000-4000-8000-000000000009'::UUID,
                 'psico.salazar',
                 'CITACION_APODERADO',
                 'Se programó una citación para comunicar la situación de exclusión social, recoger información familiar y establecer acciones coordinadas de acompañamiento.',
                 '2026-10-27 09:00:00-05'::TIMESTAMPTZ,
                 '2026-11-04 09:00:00-05'::TIMESTAMPTZ,
                 '2026-11-10'::DATE,
                 'PENDIENTE',
                 NULL
             ),

             (
                 'a1000000-0000-4000-8000-000000000010'::UUID,
                 'psico.flores',
                 'DERIVACION_PSICOLOGIA',
                 'Se documentó la derivación para la atención correspondiente, priorizando la protección del estudiante y la coordinación con las instancias competentes.',
                 '2026-10-28 10:00:00-05'::TIMESTAMPTZ,
                 NULL,
                 NULL,
                 'COMPLETADA',
                 '2026-10-28 11:00:00-05'::TIMESTAMPTZ
             )

     ) AS v(
            incidente_id,
            username,
            tipo_accion,
            descripcion,
            fecha_accion,
            fecha_programada,
            fecha_proxima_revision,
            estado_accion,
            fecha_completada
    )
         JOIN usuarios u ON u.username = v.username
WHERE EXISTS (
    SELECT 1
    FROM incidentes i
    WHERE i.id = v.incidente_id
)
  AND NOT EXISTS (
    SELECT 1
    FROM acciones_seguimiento a
    WHERE a.incidente_id = v.incidente_id
      AND a.tipo_accion = v.tipo_accion
      AND a.descripcion = v.descripcion
);


-- =====================================================================================
-- 4. NOTIFICACIONES
-- =====================================================================================
-- Las notificaciones se dirigen al contacto principal vigente de cada estudiante.
-- Se utilizan las plantillas registradas en V3.
-- Las notificaciones de tipo CITACION y ENTREVISTA deben tener accion_id.
-- =====================================================================================

INSERT INTO notificaciones (
    incidente_id,
    estudiante_id,
    apoderado_id,
    accion_id,
    plantilla_id,
    enviado_por_id,
    tipo_notificacion,
    origen,
    canal,
    destino,
    asunto,
    mensaje,
    fecha_envio,
    fecha_entrega,
    estado_envio,
    intentos_envio,
    detalle_resultado,
    id_transaccion_externo,
    created_at,
    updated_at
)
SELECT
    i.id,
    e.id,
    ea.apoderado_id,
    a.id,
    p.id,
    NULL,
    v.tipo_notificacion,
    'AUTOMATICA',
    'WHATSAPP',
    ap.telefono,
    v.asunto,
    v.mensaje,
    v.fecha_envio,
    v.fecha_entrega,
    v.estado_envio,
    1,
    NULL,
    v.id_transaccion_externo,
    v.fecha_envio,
    v.fecha_envio
FROM (
         VALUES

             -- Aviso de incidencia física.
             (
                 'INC-2026-000001',
                 '202601001A',
                 'AVISO_INCIDENCIA',
                 'AVISO_INCIDENCIA',
                 NULL::VARCHAR,
                 'Aviso de incidencia escolar',
                 'Estimado(a) apoderado(a): Se le comunica que el estudiante Sebastián Mateo se encuentra involucrado en una incidencia registrada bajo el código INC-2026-000001 con nivel de gravedad MODERADO. Para mayor información, comuníquese con el área de convivencia de la IE.',
                 '2026-10-19 11:00:00-05'::TIMESTAMPTZ,
                 '2026-10-19 11:02:00-05'::TIMESTAMPTZ,
                 'ENTREGADO',
                 'WA-SEED-000001'
             ),

             -- Entrevista a apoderados por bullying.
             (
                 'INC-2026-000002',
                 '202601002A',
                 'ENTREVISTA',
                 'ENTREVISTA',
                 'ENTREVISTA_PADRES',
                 'Programación de entrevista escolar',
                 'Estimado(a) apoderado(a): Le informamos que se ha programado una sesión de entrevista y seguimiento para la estudiante Valentina Sofía el día 21/10/2026 a las 15:00 horas con el responsable del caso.',
                 '2026-10-20 10:00:00-05'::TIMESTAMPTZ,
                 '2026-10-20 10:03:00-05'::TIMESTAMPTZ,
                 'LEIDO',
                 'WA-SEED-000002'
             ),

             -- Aviso por ciberacoso.
             (
                 'INC-2026-000003',
                 '202601004A',
                 'AVISO_INCIDENCIA',
                 'AVISO_INCIDENCIA',
                 NULL::VARCHAR,
                 'Aviso de incidencia escolar',
                 'Estimado(a) apoderado(a): Se le comunica que la estudiante Camila Lucía se encuentra involucrada en una incidencia registrada bajo el código INC-2026-000003 con nivel de gravedad GRAVE. Para mayor información, comuníquese con el área de convivencia de la IE.',
                 '2026-10-22 09:00:00-05'::TIMESTAMPTZ,
                 '2026-10-22 09:05:00-05'::TIMESTAMPTZ,
                 'ENTREGADO',
                 'WA-SEED-000003'
             ),

             -- Aviso por discriminación.
             (
                 'INC-2026-000004',
                 '202602001B',
                 'AVISO_INCIDENCIA',
                 'AVISO_INCIDENCIA',
                 NULL::VARCHAR,
                 'Aviso de incidencia escolar',
                 'Estimado(a) apoderado(a): Se le comunica que el estudiante Joaquín Alonso se encuentra involucrado en una incidencia registrada bajo el código INC-2026-000004 con nivel de gravedad MODERADO. Para mayor información, comuníquese con el área de convivencia de la IE.',
                 '2026-10-22 11:00:00-05'::TIMESTAMPTZ,
                 NULL,
                 'ENVIADO',
                 'WA-SEED-000004'
             ),

             -- Aviso por agresión física con lesiones.
             (
                 'INC-2026-000006',
                 '202602001B',
                 'AVISO_INCIDENCIA',
                 'AVISO_INCIDENCIA',
                 NULL::VARCHAR,
                 'Comunicación de incidencia grave',
                 'Estimado(a) apoderado(a): Se le comunica que el estudiante Joaquín Alonso se encuentra involucrado en una incidencia registrada bajo el código INC-2026-000006 con nivel de gravedad GRAVE. La institución ha adoptado las medidas de atención correspondientes. Para mayor información, comuníquese con la dirección de la IE.',
                 '2026-10-18 11:00:00-05'::TIMESTAMPTZ,
                 '2026-10-18 11:04:00-05'::TIMESTAMPTZ,
                 'ENTREGADO',
                 'WA-SEED-000005'
             ),

             -- Entrevista reservada por hostigamiento sexual.
             (
                 'INC-2026-000007',
                 '202602004B',
                 'ENTREVISTA',
                 'ENTREVISTA',
                 'ENTREVISTA_ESTUDIANTE',
                 'Comunicación reservada de atención',
                 'Estimado(a) apoderado(a): Le informamos que se ha programado una sesión de atención y seguimiento para la estudiante Fabiana Nicole el día 03/11/2026 a las 09:00 horas. Para mayor información, comuníquese directamente con la dirección de la institución educativa.',
                 '2026-10-27 09:00:00-05'::TIMESTAMPTZ,
                 NULL,
                 'ENVIADO',
                 'WA-SEED-000006'
             ),

             -- Aviso por intimidación.
             (
                 'INC-2026-000008',
                 '202602002B',
                 'AVISO_INCIDENCIA',
                 'AVISO_INCIDENCIA',
                 NULL::VARCHAR,
                 'Aviso de incidencia escolar',
                 'Estimado(a) apoderado(a): Se le comunica que la estudiante Mariana Isabel se encuentra involucrada en una incidencia registrada bajo el código INC-2026-000008 con nivel de gravedad MODERADO. Para mayor información, comuníquese con el área de convivencia de la IE.',
                 '2026-10-25 10:00:00-05'::TIMESTAMPTZ,
                 '2026-10-25 10:02:00-05'::TIMESTAMPTZ,
                 'LEIDO',
                 'WA-SEED-000007'
             ),

             -- Citación por aislamiento social.
             (
                 'INC-2026-000009',
                 '202602004B',
                 'CITACION',
                 'CITACION',
                 'CITACION_APODERADO',
                 'Citación para reunión con el área de convivencia',
                 'Estimado(a) apoderado(a): Se solicita su presencia en la institución educativa para tratar el caso de la estudiante Fabiana Nicole. Fecha y hora programada: 04/11/2026 a las 09:00 horas. Motivo: seguimiento de convivencia escolar.',
                 '2026-10-27 10:00:00-05'::TIMESTAMPTZ,
                 NULL,
                 'ENVIADO',
                 'WA-SEED-000008'
             ),

             -- Comunicación de cierre.
             (
                 'INC-2026-000010',
                 '202602005B',
                 'AVISO_INCIDENCIA',
                 'CIERRE_CASO',
                 NULL::VARCHAR,
                 'Comunicación de cierre de caso',
                 'Estimado(a) apoderado(a): Se le notifica formalmente que el caso INC-2026-000010 correspondiente al estudiante Álvaro Martín ha concluido su proceso de atención y ha sido CERRADO mediante acta administrativa formal.',
                 '2026-10-31 11:00:00-05'::TIMESTAMPTZ,
                 '2026-10-31 11:05:00-05'::TIMESTAMPTZ,
                 'ENTREGADO',
                 'WA-SEED-000009'
             )

     ) AS v(
            codigo_caso,
            codigo_siagie,
            tipo_notificacion,
            plantilla_codigo,
            tipo_accion_requerida,
            asunto,
            mensaje,
            fecha_envio,
            fecha_entrega,
            estado_envio,
            id_transaccion_externo
    )
         JOIN incidentes i
              ON i.codigo_caso = v.codigo_caso
         JOIN estudiantes e
              ON e.codigo_siagie = v.codigo_siagie
         JOIN estudiante_apoderado ea
              ON ea.estudiante_id = e.id
                  AND ea.es_contacto_principal = TRUE
                  AND ea.esta_eliminado = FALSE
         JOIN apoderados ap
              ON ap.id = ea.apoderado_id
         JOIN plantillas_mensaje p
              ON p.codigo = v.plantilla_codigo
         LEFT JOIN LATERAL (
    SELECT ac.id
    FROM acciones_seguimiento ac
    WHERE ac.incidente_id = i.id
      AND ac.tipo_accion = v.tipo_accion_requerida
    ORDER BY ac.id
        LIMIT 1
) a ON TRUE
WHERE (
    v.tipo_notificacion NOT IN ('CITACION', 'ENTREVISTA')
   OR a.id IS NOT NULL
    )
  AND NOT EXISTS (
    SELECT 1
    FROM notificaciones n
    WHERE n.incidente_id = i.id
  AND n.tipo_notificacion = v.tipo_notificacion
  AND n.plantilla_id = p.id
  AND n.apoderado_id = ea.apoderado_id
    );


-- =====================================================================================
-- 5. VALIDACIONES DEL SEED
-- =====================================================================================
-- Estas consultas permiten verificar que las incidencias y sus relaciones
-- quedaron registradas correctamente.
-- =====================================================================================

-- 5.1. Resumen de incidencias por estado.
SELECT
    estado,
    COUNT(*) AS total
FROM incidentes
WHERE codigo_caso BETWEEN 'INC-2026-000001' AND 'INC-2026-000010'
GROUP BY estado
ORDER BY estado;

-- 5.2. Resumen de incidencias por gravedad.
SELECT
    gravedad,
    COUNT(*) AS total
FROM incidentes
WHERE codigo_caso BETWEEN 'INC-2026-000001' AND 'INC-2026-000010'
GROUP BY gravedad
ORDER BY gravedad;

-- 5.3. Detalle de incidencias y responsables.
SELECT
    i.codigo_caso,
    ti.nombre AS tipo_incidencia,
    i.gravedad,
    i.estado,
    u.username AS responsable,
    i.fecha_ocurrencia,
    i.fecha_registro,
    i.reportado_siseve
FROM incidentes i
         JOIN tipos_incidencia ti ON ti.id = i.tipo_incidencia_id
         JOIN usuarios u ON u.id = i.responsable_actual_id
WHERE i.codigo_caso BETWEEN 'INC-2026-000001' AND 'INC-2026-000010'
ORDER BY i.codigo_caso;

-- 5.4. Total de participantes por incidencia.
SELECT
    i.codigo_caso,
    COUNT(ii.id) AS total_involucrados
FROM incidentes i
         LEFT JOIN incidente_involucrados ii ON ii.incidente_id = i.id
WHERE i.codigo_caso BETWEEN 'INC-2026-000001' AND 'INC-2026-000010'
GROUP BY i.codigo_caso
ORDER BY i.codigo_caso;

-- 5.5. Total de acciones de seguimiento por incidencia.
SELECT
    i.codigo_caso,
    COUNT(a.id) AS total_acciones
FROM incidentes i
         LEFT JOIN acciones_seguimiento a ON a.incidente_id = i.id
WHERE i.codigo_caso BETWEEN 'INC-2026-000001' AND 'INC-2026-000010'
GROUP BY i.codigo_caso
ORDER BY i.codigo_caso;

-- 5.6. Total de notificaciones por incidencia.
SELECT
    i.codigo_caso,
    COUNT(n.id) AS total_notificaciones
FROM incidentes i
         LEFT JOIN notificaciones n ON n.incidente_id = i.id
WHERE i.codigo_caso BETWEEN 'INC-2026-000001' AND 'INC-2026-000010'
GROUP BY i.codigo_caso
ORDER BY i.codigo_caso;

COMMIT;

-- =====================================================================================
-- FIN DE MIGRACIÓN V4
-- =====================================================================================
