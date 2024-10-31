/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mainardisoluzioni.scadaleva.business.comunicazione.control;

/**
 *
 * @author adminavvimpa
 */
public enum CategoriaVariabileProduzione {
    STATO_MACCHINA(0),
    CONTAPEZZI(1);
    
    private final int value;
    
    private CategoriaVariabileProduzione(int value) {
        this.value = value;
    }
}
