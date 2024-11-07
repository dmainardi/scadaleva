
INSERT INTO macchina(codice, nome, note) VALUES('MA-047', null, null)
INSERT INTO macchina(codice, nome, note) VALUES('MA-081', null, null)
INSERT INTO macchina(codice, nome, note) VALUES('MA-123Stella!!!', null, null)

INSERT INTO mqttdevice(macchina_codice, topic) VALUES('MA-047', 'v1/devices/ma-047/telemetry')
INSERT INTO mqttdevice(macchina_codice, topic) VALUES('MA-081', 'v1/devices/ma-081/telemetry')

INSERT INTO opcuadevice(macchina_codice, ipaddress, tcpport) VALUES('MA-123Stella!!!', '192.168.220.1', '4840')
INSERT INTO opcuanode(opcuadevice_id, categoriavariabileproduzione, namespaceindex, nodeid, opcuanodes_order) SELECT MAX(id), 0, 'maina', 'guaina', 1 FROM opcuadevice