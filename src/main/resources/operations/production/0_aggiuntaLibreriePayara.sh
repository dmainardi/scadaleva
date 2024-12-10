#!/usr/bin/env bash

# Aggiunge le nuove librerie per l'applicazione
${AS_HOME}/bin/asadmin add-library ~/*.jar
rm ~/*.jar

