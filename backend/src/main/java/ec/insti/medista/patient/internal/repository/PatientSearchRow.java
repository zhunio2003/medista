package ec.insti.medista.patient.internal.repository;

public interface PatientSearchRow {

    Long getId();
    String getCedula();
    String getFirstName();
    String getLastName();
    Long getCareerId();
    Double getSimilarity();
}
