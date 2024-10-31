/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mainardisoluzioni.scadaleva.business.energia.entity;

import jakarta.json.bind.annotation.JsonbProperty;

/**
 *
 * @author adminavvimpa
 */
public class PayloadTelemetryTraceAndFollow {
    @JsonbProperty("ts")
    private String timestamp;
    
    @JsonbProperty("values")
    private ContenutoTelemetryTraceAndFollow contenuto;

    public PayloadTelemetryTraceAndFollow() {
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public ContenutoTelemetryTraceAndFollow getContenuto() {
        return contenuto;
    }

    public void setContenuto(ContenutoTelemetryTraceAndFollow contenuto) {
        this.contenuto = contenuto;
    }
    
}
