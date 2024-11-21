
INSERT INTO macchina(codice, nome, note) VALUES('MA-047', null, null)
INSERT INTO macchina(codice, nome, note) VALUES('MA-081', null, null)
INSERT INTO macchina(codice, nome, note) VALUES('MA-000', null, null)

INSERT INTO mqttdevice(macchina_codice, topic) VALUES('MA-047', 'v1/devices/ma-047/telemetry')
INSERT INTO mqttdevice(macchina_codice, topic) VALUES('MA-081', 'v1/devices/ma-081/telemetry')

INSERT INTO opcuadevice(macchina_codice, ipaddress, tcpport) VALUES('MA-000', '192.168.220.1', '4840')
INSERT INTO opcuanode(opcuadevice_id, categoriavariabileproduzione, namespaceindex, nodeidentifier, opcuanodes_order) SELECT MAX(id), 5, 4, '17', 1 FROM opcuadevice
INSERT INTO opcuanode(opcuadevice_id, categoriavariabileproduzione, namespaceindex, nodeidentifier, opcuanodes_order) SELECT MAX(id), 0, 4, '11', 1 FROM opcuadevice
INSERT INTO opcuanode(opcuadevice_id, categoriavariabileproduzione, namespaceindex, nodeidentifier, opcuanodes_order) SELECT MAX(id), 4, 4, '15', 1 FROM opcuadevice
INSERT INTO opcuanode(opcuadevice_id, categoriavariabileproduzione, namespaceindex, nodeidentifier, opcuanodes_order) SELECT MAX(id), 6, 4, '16', 1 FROM opcuadevice
INSERT INTO opcuanode(opcuadevice_id, categoriavariabileproduzione, namespaceindex, nodeidentifier, opcuanodes_order) SELECT MAX(id), 3, 4, '14', 1 FROM opcuadevice
INSERT INTO opcuanode(opcuadevice_id, categoriavariabileproduzione, namespaceindex, nodeidentifier, opcuanodes_order) SELECT MAX(id), 8, 4, '19', 1 FROM opcuadevice
INSERT INTO opcuanode(opcuadevice_id, categoriavariabileproduzione, namespaceindex, nodeidentifier, opcuanodes_order) SELECT MAX(id), 7, 4, '18', 1 FROM opcuadevice
INSERT INTO opcuanode(opcuadevice_id, categoriavariabileproduzione, namespaceindex, nodeidentifier, opcuanodes_order) SELECT MAX(id), 2, 4, '13', 1 FROM opcuadevice
INSERT INTO opcuanode(opcuadevice_id, categoriavariabileproduzione, namespaceindex, nodeidentifier, opcuanodes_order) SELECT MAX(id), 1, 4, '12', 1 FROM opcuadevice
