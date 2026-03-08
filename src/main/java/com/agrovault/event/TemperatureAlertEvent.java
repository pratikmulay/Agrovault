package com.agrovault.event;

import com.agrovault.entity.Storage;
import org.springframework.context.ApplicationEvent;

public class TemperatureAlertEvent extends ApplicationEvent {

    private final Storage storage;
    private final double currentTemperature;
    private final String alertMessage;

    public TemperatureAlertEvent(Object source, Storage storage, double currentTemperature, String alertMessage) {
        super(source);
        this.storage = storage;
        this.currentTemperature = currentTemperature;
        this.alertMessage = alertMessage;
    }

    public Storage getStorage() {
        return storage;
    }

    public double getCurrentTemperature() {
        return currentTemperature;
    }

    public String getAlertMessage() {
        return alertMessage;
    }
}
