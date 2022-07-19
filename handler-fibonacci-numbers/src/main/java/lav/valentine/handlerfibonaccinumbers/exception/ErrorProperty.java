package lav.valentine.handlerfibonaccinumbers.exception;

public enum ErrorProperty {
    STATUS("status"), MESSAGE("message");

    private final String property;

    ErrorProperty(String property) {
        this.property = property;
    }

    public String getProperty() {
        return property;
    }
}
