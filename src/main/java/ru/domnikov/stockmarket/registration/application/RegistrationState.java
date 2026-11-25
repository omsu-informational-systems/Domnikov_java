package ru.domnikov.stockmarket.registration.application;

import org.springframework.stereotype.Component;

@Component
public class RegistrationState {

    private volatile String eventId;
    private volatile String correlationId;
    private volatile boolean registered = false;

    public void save(String eventId, String correlationId) {
        this.eventId = eventId;
        this.correlationId = correlationId;
        this.registered = false;
    }

    public boolean matches(String correlationId, String causationId) {
        return this.correlationId != null
                && this.eventId != null
                && this.correlationId.equals(correlationId)
                && this.eventId.equals(causationId);
    }

    public void markRegistered() {
        this.registered = true;
    }

    public boolean isRegistered() {
        return registered;
    }
}