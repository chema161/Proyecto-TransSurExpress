-- Vehiculos
INSERT INTO vehiculo (id, matricula, capacidad, activo) VALUES (1, '1234-BBB', 1500.0, TRUE);
INSERT INTO vehiculo (id, matricula, capacidad, activo) VALUES (2, '5678-CXC', 3500.0, TRUE);
INSERT INTO vehiculo (id, matricula, capacidad, activo) VALUES (3, '9101-DXD', 12000.0, TRUE);
INSERT INTO vehiculo (id, matricula, capacidad, activo) VALUES (4, '4321-FFF', 2000.0, TRUE);
INSERT INTO vehiculo (id, matricula, capacidad, activo) VALUES (5, '7777-GGG', 800.0, TRUE);

-- Conductores
INSERT INTO conductor (id, nombre, experiencia, vehiculo_id, activo) VALUES (1, 'Juan Perez Gomez', 5, 1, TRUE);
INSERT INTO conductor (id, nombre, experiencia, vehiculo_id, activo) VALUES (2, 'Maria Lopez Torres', 8, 2, TRUE);
INSERT INTO conductor (id, nombre, experiencia, vehiculo_id, activo) VALUES (3, 'Carlos Mendoza Ruiz', 12, 3, TRUE);
INSERT INTO conductor (id, nombre, experiencia, vehiculo_id, activo) VALUES (4, 'Ana Silva Ortiz', 2, 4, TRUE);
INSERT INTO conductor (id, nombre, experiencia, vehiculo_id, activo) VALUES (5, 'Pedro Romero Diaz', 6, 5, TRUE);

-- Envios
INSERT INTO envio (id, codigo, origen, destino, peso, coste) VALUES (1, 'ENV-2026-001', 'Sevilla', 'Madrid', 45.5, 120.00);
INSERT INTO envio (id, codigo, origen, destino, peso, coste) VALUES (2, 'ENV-2026-002', 'Malaga', 'Barcelona', 180.0, 450.50);
INSERT INTO envio (id, codigo, origen, destino, peso, coste) VALUES (3, 'ENV-2026-003', 'Cadiz', 'Valencia', 320.0, 680.00);
INSERT INTO envio (id, codigo, origen, destino, peso, coste) VALUES (4, 'ENV-2026-004', 'Huelva', 'Bilbao', 15.0, 75.25);
INSERT INTO envio (id, codigo, origen, destino, peso, coste) VALUES (5, 'ENV-2026-005', 'Cordoba', 'Zaragoza', 540.0, 1150.00);

-- Operaciones / Seguimiento
INSERT INTO envio_vehiculo (id, estado, fecha, fecha_estimada_entrega, lugar, distancia, envio_id, vehiculo_id) VALUES (1, 'PREPARADO',  '2026-05-20', '2026-05-21', 'Almacen Central Sevilla', 210.5, 1, 1);
INSERT INTO envio_vehiculo (id, estado, fecha, fecha_estimada_entrega, lugar, distancia, envio_id, vehiculo_id) VALUES (2, 'EN_RUTA',    '2026-05-21', '2026-05-23', 'Centro Logistico Cordoba', 350.0, 2, 2);
INSERT INTO envio_vehiculo (id, estado, fecha, fecha_estimada_entrega, lugar, distancia, envio_id, vehiculo_id) VALUES (3, 'EN_REPARTO', '2026-05-22', '2026-05-23', 'Delegacion Sur Madrid', 45.0, 3, 3);
INSERT INTO envio_vehiculo (id, estado, fecha, fecha_estimada_entrega, lugar, distancia, envio_id, vehiculo_id) VALUES (4, 'ENTREGADO',  '2026-05-23', '2026-05-23', 'Domicilio Cliente Bilbao', 10.0, 4, 4);
INSERT INTO envio_vehiculo (id, estado, fecha, fecha_estimada_entrega, lugar, distancia, envio_id, vehiculo_id) VALUES (5, 'EN_RUTA',    '2026-05-24', '2026-05-25', 'Aeropuerto de Zaragoza', 300.0, 5, 5);

-- Historial de estados
INSERT INTO historial_estado (id, envio_vehiculo_id, estado_anterior, estado_nuevo, fecha_hora, usuario)
    VALUES (1, 1, NULL,        'PREPARADO',  '2026-05-20 08:00:00', 'admin');
INSERT INTO historial_estado (id, envio_vehiculo_id, estado_anterior, estado_nuevo, fecha_hora, usuario)
    VALUES (2, 2, NULL,        'EN_RUTA',    '2026-05-21 09:15:00', 'admin');
INSERT INTO historial_estado (id, envio_vehiculo_id, estado_anterior, estado_nuevo, fecha_hora, usuario)
    VALUES (3, 2, 'EN_RUTA',   'EN_REPARTO', '2026-05-22 11:30:00', 'user');
INSERT INTO historial_estado (id, envio_vehiculo_id, estado_anterior, estado_nuevo, fecha_hora, usuario)
    VALUES (4, 3, NULL,        'EN_REPARTO', '2026-05-22 08:45:00', 'admin');
INSERT INTO historial_estado (id, envio_vehiculo_id, estado_anterior, estado_nuevo, fecha_hora, usuario)
    VALUES (5, 4, NULL,        'ENTREGADO',  '2026-05-23 16:00:00', 'user');
INSERT INTO historial_estado (id, envio_vehiculo_id, estado_anterior, estado_nuevo, fecha_hora, usuario)
    VALUES (6, 5, NULL,        'EN_RUTA',    '2026-05-24 07:30:00', 'admin');

-- Reiniciar secuencias
ALTER TABLE vehiculo ALTER COLUMN id RESTART WITH 6;
ALTER TABLE conductor ALTER COLUMN id RESTART WITH 6;
ALTER TABLE envio ALTER COLUMN id RESTART WITH 6;
ALTER TABLE envio_vehiculo ALTER COLUMN id RESTART WITH 6;
ALTER TABLE historial_estado ALTER COLUMN id RESTART WITH 11;
