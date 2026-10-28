package pe.gob.minedu.sigece.enums;

import lombok.Getter;

/**
 * Homologado con las categorías y subcategorías oficiales de tipificación
 * de violencia escolar de la plataforma SíSeVe - MINEDU.
 */
@Getter
public enum CategoriaSiseve {

    // --- Violencia Física ---
    FIS_CASTIGO_FISICO("Castigo físico"),
    FIS_CON_LESIONES("Violencia física con lesiones"),
    FIS_USO_ARMAS("Violencia física con uso de armas"),
    FIS_SIN_LESIONES("Violencia física sin lesiones"),

    // --- Violencia Psicológica ---
    PSI_ACOSO_ESCOLAR("Acoso escolar (Bullying)"),
    PSI_AISLAMIENTO("Aislamiento o exclusión social"),
    PSI_CIBERACOSO("Ciberacoso o cyberbullying"),
    PSI_DISCRIMINACION("Discriminación"),
    PSI_USO_ARMAS("Amenaza o intimidación psicológica con uso de armas"),
    PSI_INTIMIDACION("Intimidación o coacción"),
    PSI_TRATO_HUMILLANTE("Trato humillante o degradante"),
    PSI_VERVAL("Violencia verbal"), // Nota: Mantiene el identificador oficial del diagrama

    // --- Violencia Sexual ---
    SEX_ACOSO_SEXUAL("Acoso sexual"),
    SEX_ACOSO_MEDIOS_TECNOLOGICOS("Acoso sexual mediante medios tecnológicos"),
    SEX_HOSTIGAMIENTO_SEXUAL("Hostigamiento sexual"),
    SEX_TOCAMIENTOS_ACTOS_SEXUALES("Tocamientos o actos de connotación sexual"),
    SEX_VIOLACION_SEXUAL("Violación sexual"),
    SEX_VIOLENCIA_FIN_SEXUAL("Otras formas de violencia con fin sexual");

    private final String descripcion;

    CategoriaSiseve(String descripcion) {
        this.descripcion = descripcion;
    }
}
