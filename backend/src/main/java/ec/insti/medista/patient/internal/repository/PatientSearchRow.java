package ec.insti.medista.patient.internal.repository;

public interface PatientSearchRow {

    Long getId();
    Long getCedula();
    String getFirstName();
    String getLastName();
    String getCareerId();
    Double getSimilarity();
}
