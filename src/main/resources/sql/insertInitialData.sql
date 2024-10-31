
INSERT INTO macchina(codice, nome, note) VALUES('MA-047', null, null)
INSERT INTO macchina(codice, nome, note) VALUES('MA-081', null, null)

INSERT INTO mqttdevice(macchina_codice, topic) VALUES('MA-047', 'v1/devices/ma-047/telemetry')
INSERT INTO mqttdevice(macchina_codice, topic) VALUES('MA-081', 'v1/devices/ma-081/telemetry')
