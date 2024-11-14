
INSERT INTO macchina(codice, nome, note) VALUES('MA-047', null, null)
INSERT INTO macchina(codice, nome, note) VALUES('MA-081', null, null)
INSERT INTO macchina(codice, nome, note) VALUES('MA-123', null, null)
INSERT INTO macchina(codice, nome, note) VALUES('MA-156', null, null)

INSERT INTO mqttdevice(macchina_codice, topic) VALUES('MA-047', 'v1/devices/ma-047/telemetry')
INSERT INTO mqttdevice(macchina_codice, topic) VALUES('MA-081', 'v1/devices/ma-081/telemetry')

INSERT INTO opcuadevice(macchina_codice, ipaddress, tcpport) VALUES('MA-123', '192.168.220.1', '4840')
INSERT INTO opcuanode(opcuadevice_id, categoriavariabileproduzione, namespaceindex, nodeidentifier, opcuanodes_order) SELECT MAX(id), 0, 4, '14', 1 FROM opcuadevice
INSERT INTO opcuanode(opcuadevice_id, categoriavariabileproduzione, namespaceindex, nodeidentifier, opcuanodes_order) SELECT MAX(id), 1, 4, '11', 2 FROM opcuadevice
INSERT INTO opcuanode(opcuadevice_id, categoriavariabileproduzione, namespaceindex, nodeidentifier, opcuanodes_order) SELECT MAX(id), 2, 4, '12', 3 FROM opcuadevice
INSERT INTO opcuanode(opcuadevice_id, categoriavariabileproduzione, namespaceindex, nodeidentifier, opcuanodes_order) SELECT MAX(id), 3, 4, '13', 4 FROM opcuadevice

INSERT INTO opcuadevice(macchina_codice, ipaddress, tcpport) VALUES('MA-156', '192.168.220.17', '4840')
INSERT INTO opcuanode(opcuadevice_id, categoriavariabileproduzione, namespaceindex, nodeidentifier, opcuanodes_order) SELECT MAX(id), 1, 4, '31', 1 FROM opcuadevice
INSERT INTO opcuanode(opcuadevice_id, categoriavariabileproduzione, namespaceindex, nodeidentifier, opcuanodes_order) SELECT MAX(id), 3, 4, '9', 1 FROM opcuadevice
