package pe.gob.minedu.sigece.utils;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.DateTimeSchema;
import io.swagger.v3.oas.models.media.IntegerSchema;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    private static final String SECURITY_SCHEME_NAME = "Bearer Authentication";

    @Value("${server.port:8080}")
    private String serverPort;

    @Bean
    public OpenAPI sigeceOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("SIGECE · API REST de Convivencia Escolar")
                        .version("1.0.0")
                        .summary("API para la administración de expedientes, involucrados, medidas de seguimiento y alertas de convivencia escolar.")
                        .description("""
                                ### Especificación Técnica de la API SIGECE
                                Servicio centralizado para el procesamiento y control de incidentes escolares. La API expone operaciones RESTful organizadas por dominios funcionales:

                                ---
                                #### ¿Qué operaciones permite realizar esta API?

                                1. **Control de Sesiones y Credenciales**
                                   - Iniciar sesión con validación de credenciales y bloqueo temporal por intentos fallidos.
                                   - Emisión y rotación de tokens seguros (`Access Token` y `Refresh Token`).
                                   - Revocación de refresh tokens y cierre de sesión activo.
                                   - Consulta del perfil y permisos de la sesión actual.

                                2. **Administración de Cuentas y Personal (RBAC)**
                                   - Registrar usuarios con DNI, correo institucional y rol asignado.
                                   - Modificar datos del perfil y actualizar estados de activación de cuentas.
                                   - Cambio manual de contraseña y restablecimiento asistido con clave temporal.
                                   - Listar usuarios por rol (tutores, psicólogos) y consultar la carga de casos activos asignados.

                                3. **Padrón de Estudiantes**
                                   - Registrar y actualizar fichas de estudiantes con validación de código SIAGIE y fecha de nacimiento.
                                   - Consultar el perfil consolidado del estudiante con historial de casos y apoderados asignados.
                                   - Búsqueda paginada y filtrada por grado, sección, nivel y documento.
                                   - Asignar o desvincular apoderados definiendo el tipo de parentesco y el contacto principal.
                                   - Desactivar o reactivar registros escolares.

                                4. **Directorio de Apoderados**
                                   - Registrar y actualizar datos de contacto (teléfono, correo) de padres o tutores legales.
                                   - Buscar apoderados por número de documento de identidad con validación de duplicados.
                                   - Listar apoderados asignados a un estudiante y viceversa (estudiantes a cargo de un apoderado).
                                   - Marcar o alternar el apoderado designado como contacto principal.

                                5. **Catálogo de Tipologías de Violencia**
                                   - Mantenimiento (creación, edición, activación y baja) de tipos de incidencia.
                                   - Clasificación y filtrado de tipologías homologadas con las categorías del portal SíSeVe.

                                6. **Expedientes e Incidentes**
                                   - Aperturar expedientes con generación automática de código correlativo anual.
                                   - Clasificar oficialmente el nivel de gravedad del hecho (Leve, Moderado, Grave).
                                   - Asignar o reasignar el profesional responsable del seguimiento (Tutor o Psicólogo).
                                   - Ejecutar transiciones de estado controladas (`REGISTRADO` ➔ `EN_INVESTIGACION` ➔ `EN_SEGUIMIENTO` ➔ `RESUELTO` / `DERIVADO_SISEVE` ➔ `CERRADO`).
                                   - Consultar el expediente integral unificado (datos, involucrados, bitácora y evidencias).
                                   - Evaluar checklist obligatorio de requisitos previos al cierre formal de un caso.
                                   - Registrar el cierre definitivo adjuntando acta final y conclusiones.
                                   - Detectar e identificar reincidencias de conductas en una ventana de 30 días.
                                   - Monitorear casos estancados que superen umbrales de días sin atención.

                                7. **Gestión de Involucrados**
                                   - Incorporar participantes al expediente clasificándolos como víctimas, agresores o testigos (tanto estudiantes como personal).
                                   - Retirar involucrados registrando sustento y justificación técnica.
                                   - Consultar los involucrados de un caso agrupados por su rol correspondiente.
                                   - Consultar el historial de apariciones de un estudiante según su rol en incidentes previos.

                                8. **Bitácora de Seguimiento y Acuerdos**
                                   - Registrar medidas socioeducativas, compromisos y citas de atención (la primera medida pasa el caso a `EN_SEGUIMIENTO`).
                                   - Actualizar el estado de cumplimiento de los compromisos (`COMPLETADA`, `CANCELADA`) con marca de fecha.
                                   - Reprogramar fechas límite de revisión justificando el aplazamiento.
                                   - Listar acciones pendientes asignadas a un usuario específico y detectar compromisos vencidos.
                                   - Verificar automáticamente si el 100% de medidas pactadas han sido cumplidas.

                                9. **Evidencias Digitales y Cadena de Custodia**
                                   - Cargar archivos individuales o por lotes con cálculo automático de hash **SHA-256** para evitar duplicidad y garantizar custodia.
                                   - Generar URLs de descarga temporal firmadas (SAS) con tiempo de expiración programable en minutos.
                                   - Verificar la integridad criptográfica comparando el archivo almacenado contra el hash original.
                                   - Eliminación lógica de adjuntos con registro obligatorio del motivo.

                                10. **Comunicaciones y Notificaciones**
                                    - Disparar notificaciones y citaciones a los apoderados vía Email, SMS o WhatsApp.
                                    - Registrar respuestas de pasarela y estados de entrega (Enviado, Fallido, Entregado, Leído).
                                    - Reintentar envíos fallidos manteniendo el conteo de intentos y trazabilidad del ID externo.
                                    - Validar si los apoderados principales cuentan con notificación efectiva para autorizar el cierre del expediente.

                                11. **Derivaciones Externas (SíSeVe)**
                                    - Determinar mediante reglas de negocio si un caso exige derivación obligatoria (gravedad alta o reincidencia).
                                    - Obtener la evaluación técnica con la recomendación formal de elevación.
                                    - Registrar el código oficial de derivación externa generado en SíSeVe.
                                    - Listar expedientes pendientes de derivación obligatoria que continúan sin trámite externo.

                                12. **Motor de Alertas Tempranas**
                                    - Ejecución programada o manual de rutinas para detectar inacción (+72 horas en registro) y compromisos vencidos.
                                    - Listado paginado de alertas activas con filtros por criticidad y tipo.
                                    - Contador en tiempo real de alertas activas pendientes de atención.
                                    - Levantamiento y resolución de alertas registrando descargo y acciones correctivas.

                                13. **Dashboard e Indicadores**
                                    - Cálculo en tiempo real de KPIs: porcentaje de resolución, total de reincidencias, casos derivados a SíSeVe.
                                    - Distribución estadística consolidada de casos por estado y por nivel de gravedad.

                                14. **Motor de Reportes y Exportación**
                                    - Previsualizar datos tabulares consolidados (Libro de Registro de Incidencias, balances periódicos).
                                    - Exportar reportes en formatos **PDF** y **Excel** con soporte de anonimización de datos de menores.

                                15. **Auditoría Transversal**
                                    - Registro inmutable y aislado de eventos sensibles (valores anteriores, valores nuevos, usuario, IP y User-Agent).
                                    - Búsqueda avanzada de trazas y consulta de historial cronológico por entidad y registro.
                                """)
                        .contact(new Contact()
                                .name("Soporte Técnico SIGECE")
                                .email("soporte.sigece@minedu.gob.pe"))
                        .license(new License()
                                .name("Privada e Institucional · MINEDU")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:" + serverPort)
                                .description("Servidor Local de Desarrollo"),
                        new Server()
                                .url("https://sigece-qa.minedu.gob.pe/api/v1")
                                .description("Ambiente de Pruebas (QA / Testing)"),
                        new Server()
                                .url("https://sigece.minedu.gob.pe/api/v1")
                                .description("Ambiente de Producción")))
                .components(construirComponentes())
                .addSecurityItem(new SecurityRequirement().addList(SECURITY_SCHEME_NAME))
                .tags(construirTags());
    }

    private Components construirComponentes() {
        Schema<?> apiErrorSchema = new Schema<>()
                .type("object")
                .description("Formato unificado para respuestas de error de la API")
                .addProperty("codigo", new StringSchema().example("INCIDENTE_NO_ENCONTRADO"))
                .addProperty("mensaje", new StringSchema().example("El expediente especificado no existe o no se encuentra disponible."))
                .addProperty("status", new IntegerSchema().example(404))
                .addProperty("timestamp", new DateTimeSchema().example("2026-10-15T16:20:00Z"))
                .addProperty("detalles", new StringSchema().example("Identificador UUID no registrado en la base de datos."));

        MediaType errorMediaType = new MediaType().schema(apiErrorSchema);

        return new Components()
                .addSecuritySchemes(SECURITY_SCHEME_NAME, new SecurityScheme()
                        .name(SECURITY_SCHEME_NAME)
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT")
                        .description("""
                                Autenticación estándar con Token JWT.
                                Ingrese el token obtenido en `/api/auth/login`.
                                Formato: `Bearer <token>` (Swagger UI inserta el prefijo automáticamente).
                                """))
                .addResponses("400BadRequest", new ApiResponse()
                        .description("Petición inválida, error de validación de campos o regla de negocio no superada.")
                        .content(new Content().addMediaType("application/json", errorMediaType)))
                .addResponses("401Unauthorized", new ApiResponse()
                        .description("No autorizado. Token JWT ausente, expirado o con firma no válida.")
                        .content(new Content().addMediaType("application/json", errorMediaType)))
                .addResponses("403Forbidden", new ApiResponse()
                        .description("Acceso denegado. El usuario no cuenta con el rol necesario para esta operación.")
                        .content(new Content().addMediaType("application/json", errorMediaType)))
                .addResponses("404NotFound", new ApiResponse()
                        .description("El recurso solicitado no fue encontrado.")
                        .content(new Content().addMediaType("application/json", errorMediaType)))
                .addResponses("409Conflict", new ApiResponse()
                        .description("Conflicto por duplicidad (documento, usuario, SIAGIE o evidencia ya registrada).")
                        .content(new Content().addMediaType("application/json", errorMediaType)))
                .addResponses("500InternalError", new ApiResponse()
                        .description("Error no controlado en el servidor durante el procesamiento.")
                        .content(new Content().addMediaType("application/json", errorMediaType)));
    }

    private List<Tag> construirTags() {
        return List.of(
                new Tag()
                        .name("Autenticación")
                        .description("Inicio de sesión, refresco de tokens, revocación de sesión y consulta de usuario autenticado."),

                new Tag()
                        .name("Usuarios")
                        .description("Gestión de personal educativo: registro, edición de perfiles, cambio/blanqueo de passwords y control de cuentas activas."),

                new Tag()
                        .name("Estudiantes")
                        .description("Control del padrón escolar: altas, modificación de datos, vinculación con apoderados y consulta de perfil con historial de casos."),

                new Tag()
                        .name("Apoderados")
                        .description("Directorio de padres y tutores legales: registro, teléfonos/correos, filiación y designación de contacto de emergencia principal."),

                new Tag()
                        .name("Tipos de Incidencia")
                        .description("Catálogo de conductas e incidentes homologados con la clasificación oficial de SíSeVe."),

                new Tag()
                        .name("Incidentes")
                        .description("Ciclo de vida del caso: apertura con correlativo, clasificación de gravedad, asignación de responsable, transiciones de estado y cierre definitivo."),

                new Tag()
                        .name("Involucrados")
                        .description("Administración de participantes de un incidente: registro y retiro de víctimas, agresores y testigos (estudiantes o personal)."),

                new Tag()
                        .name("Seguimiento")
                        .description("Bitácora de compromisos y medidas: registro de acuerdos, actualización de estado, reprogramación de citas y control de vencimientos."),

                new Tag()
                        .name("Evidencias")
                        .description("Gestión de archivos adjuntos: subida individual/lote, cálculo y comprobación de hash SHA-256 y descarga temporal firmada."),

                new Tag()
                        .name("Notificaciones")
                        .description("Envío y control de comunicaciones a apoderados por Email/SMS/WhatsApp, auditoría de entrega y reintentos."),

                new Tag()
                        .name("SíSeVe")
                        .description("Evaluación de obligatoriedad y registro de la derivación formal de incidentes a la plataforma SíSeVe."),

                new Tag()
                        .name("Alertas")
                        .description("Motor de detección proactiva: ejecución de chequeo diario por inacción (+72h) o plazos vencidos, conteo y levantamiento de alertas."),

                new Tag()
                        .name("Dashboard")
                        .description("Indicadores y métricas en tiempo real: tasa de resolución, reincidencias, derivaciones y distribución por gravedad."),

                new Tag()
                        .name("Reportes")
                        .description("Previsualización de datos y exportación física en PDF/Excel (Libro de Registro, balances) con anonimización de menores."),

                new Tag()
                        .name("Auditoría")
                        .description("Consulta inmutable de trazas históricas de operaciones sensibles por entidad, identificador o rango temporal.")
        );
    }
}