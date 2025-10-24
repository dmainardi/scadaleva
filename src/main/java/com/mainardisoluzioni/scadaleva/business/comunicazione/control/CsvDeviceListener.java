/*
 * Copyright (C) 2025 maina
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
package com.mainardisoluzioni.scadaleva.business.comunicazione.control;

import com.mainardisoluzioni.scadaleva.business.comunicazione.boundary.CsvDeviceService;
import com.mainardisoluzioni.scadaleva.business.comunicazione.entity.CsvDevice;
import jakarta.ejb.Schedule;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import jakarta.inject.Inject;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author maina
 */
@Startup
@Singleton
public class CsvDeviceListener {
    private static final Logger LOGGER = Logger.getLogger(CsvDeviceListener.class.getName());
    
    @Inject
    CsvDeviceController controller;
    
    @Inject
    CsvDeviceService service;
    
    @Schedule(minute = "*/1", hour = "*/1", persistent = false)
    protected void checkForNewCsvData() {
        LOGGER.log(Level.FINE, "CsvDeviceListener:checkForNewCsvData - Adesso provo a vedere se ci sono nuovi dati dai CSV dei macchinari");
        
        List<CsvDevice> csvDevices = service.list();
        for (CsvDevice csvDevice : csvDevices)
            controller.leggiDatiDaCsvEPopolaEventoProduzione(csvDevice);
    }
}
