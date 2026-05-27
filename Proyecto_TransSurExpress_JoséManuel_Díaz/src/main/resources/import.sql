-- Vehículos 
INSERT INTO vehiculo (id, matricula, capacidad) VALUES (1, '1234-BBB', 1500.0);
INSERT INTO vehiculo (id, matricula, capacidad) VALUES (2, '5678-CXC', 3500.0);
INSERT INTO vehiculo (id, matricula, capacidad) VALUES (3, '9101-DXD', 12000.0);
INSERT INTO vehiculo (id, matricula, capacidad) VALUES (4, '4321-FFF', 2000.0);
INSERT INTO vehiculo (id, matricula, capacidad) VALUES (5, '7777-GGG', 800.0);

-- Conductores 
INSERT INTO conductor (id, nombre, experiencia, vehiculo_id) VALUES (1, 'Juan Pérez Gómez', 5, 1);
INSERT INTO conductor (id, nombre, experiencia, vehiculo_id) VALUES (2, 'María López Torres', 8, 2);
INSERT INTO conductor (id, nombre, experiencia, vehiculo_id) VALUES (3, 'Carlos Mendoza Ruiz', 12, 3);
INSERT INTO conductor (id, nombre, experiencia, vehiculo_id) VALUES (4, 'Ana Silva Ortiz', 2, 4);
INSERT INTO conductor (id, nombre, experiencia, vehiculo_id) VALUES (5, 'Pedro Romero Díaz', 6, 5);

-- Envíos 
INSERT INTO envio (id, codigo, origen, destino, peso, coste) VALUES (1, 'ENV-2026-001', 'Sevilla', 'Madrid', 45.5, 120.00);
INSERT INTO envio (id, codigo, origen, destino, peso, coste) VALUES (2, 'ENV-2026-002', 'Málaga', 'Barcelona', 180.0, 450.50);
INSERT INTO envio (id, codigo, origen, destino, peso, coste) VALUES (3, 'ENV-2026-003', 'Cádiz', 'Valencia', 320.0, 680.00);
INSERT INTO envio (id, codigo, origen, destino, peso, coste) VALUES (4, 'ENV-2026-004', 'Huelva', 'Bilbao', 15.0, 75.25);
INSERT INTO envio (id, codigo, origen, destino, peso, coste) VALUES (5, 'ENV-2026-005', 'Córdoba', 'Zaragoza', 540.0, 1150.00);

-- Operaciones / Seguimiento 
INSERT INTO envio_vehiculo (id, estado, fecha, lugar, distancia, envio_id, vehiculo_id) VALUES (1, 'PREPARADO',  '2026-05-20', 'Almacén Central Sevilla',   210.5, 1, 1);
INSERT INTO envio_vehiculo (id, estado, fecha, lugar, distancia, envio_id, vehiculo_id) VALUES (2, 'EN_RUTA',    '2026-05-21', 'Centro Logístico Córdoba',  350.0, 2, 2);
INSERT INTO envio_vehiculo (id, estado, fecha, lugar, distancia, envio_id, vehiculo_id) VALUES (3, 'EN_REPARTO', '2026-05-22', 'Delegación Sur Madrid',      45.0, 3, 3);
INSERT INTO envio_vehiculo (id, estado, fecha, lugar, distancia, envio_id, vehiculo_id) VALUES (4, 'ENTREGADO',  '2026-05-23', 'Domicilio Cliente Bilbao',   10.0, 4, 4);
INSERT INTO envio_vehiculo (id, estado, fecha, lugar, distancia, envio_id, vehiculo_id) VALUES (5, 'EN_RUTA',    '2026-05-24', 'Aeropuerto de Zaragoza',    300.0, 5, 5);

-- Reiniciar secuencias
ALTER TABLE conductor ALTER COLUMN id RESTART WITH 10;
ALTER TABLE vehiculo ALTER COLUMN id RESTART WITH 10;
ALTER TABLE envio ALTER COLUMN id RESTART WITH 10;
ALTER TABLE envio_vehiculo ALTER COLUMN id RESTART WITH 10;