/*
 * Copyright (C) 2024 adminavvimpa
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
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
