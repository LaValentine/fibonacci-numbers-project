package lav.valentine.handlerfibonaccinumbers.exception;

public enum ErrorProperty {
    STATUS("status"), MESSAGE("message");

    private String property;

    ErrorProperty(String property) {
        this.property = property;
    }

    public String getProperty() {
        return property;
    }
}
