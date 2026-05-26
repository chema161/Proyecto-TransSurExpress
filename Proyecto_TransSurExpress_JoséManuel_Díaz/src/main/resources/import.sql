
INSERT INTO vehiculo (id, matricula, capacidad) VALUES (1, '1234-BBB', 1500.0);
INSERT INTO vehiculo (id, matricula, capacidad) VALUES (2, '5678-CXC', 3500.0);
INSERT INTO vehiculo (id, matricula, capacidad) VALUES (3, '9101-DXD', 12000.0);
INSERT INTO vehiculo (id, matricula, capacidad) VALUES (4, '4321-FFF', 2000.0);

INSERT INTO conductor (id, nombre, experiencia, vehiculo_id) VALUES (1, 'Juan Pérez Gómez', 5, 1);
INSERT INTO conductor (id, nombre, experiencia, vehiculo_id) VALUES (2, 'María López Torres', 8, 2);
INSERT INTO conductor (id, nombre, experiencia, vehiculo_id) VALUES (3, 'Carlos Mendoza Ruiz', 12, 3);
INSERT INTO conductor (id, nombre, experiencia, vehiculo_id) VALUES (4, 'Ana Silva Ortiz', 2, NULL);

INSERT INTO envio (id, codigo, origen, destino, peso, coste) VALUES (1, 'ENV-2026-001', 'Sevilla', 'Madrid', 45.5, 120.00);
INSERT INTO envio (id, codigo, origen, destino, peso, coste) VALUES (2, 'ENV-2026-002', 'Málaga', 'Barcelona', 180.0, 450.50);
INSERT INTO envio (id, codigo, origen, destino, peso, coste) VALUES (3, 'ENV-2026-003', 'Cádiz', 'Valencia', 320.0, 680.00);
INSERT INTO envio (id, codigo, origen, destino, peso, coste) VALUES (4, 'ENV-2026-004', 'Huelva', 'Bilbao', 15.0, 75.25);
INSERT INTO envio (id, codigo, origen, destino, peso, coste) VALUES (5, 'ENV-2026-005', 'Córdoba', 'Zaragoza', 540.0, 1150.00);

INSERT INTO envio_vehiculo (id, fecha, envio_id, vehiculo_id, ubicacion_actual, distancia, estado_envio) 
VALUES (1, '2026-05-25', 1, 1, 'Manzanares', 210.5, 'EN_TRANSITO');

INSERT INTO envio_vehiculo (id, fecha, envio_id, vehiculo_id, ubicacion_actual, distancia, estado_envio) 
VALUES (2, '2026-05-25', 2, 2, 'Antequera', 45.0, 'EN_TRANSITO');

INSERT INTO envio_vehiculo (id, fecha, envio_id, vehiculo_id, ubicacion_actual, distancia, estado_envio) 
VALUES (3, '2026-05-24', 3, 3, 'Valencia', 680.0, 'ENTREGADO');

INSERT INTO envio_vehiculo (id, fecha, envio_id, vehiculo_id, ubicacion_actual, distancia, estado_envio)
VALUES (3, '2026-05-22', 4, 4, 'Sevilla' ,111.0, 'ENTREGADO')

INSERT INTO envio_vehiculo (estado, fecha, lugar, envio_id, vehiculo_id) 
VALUES ('PREPARADO', '2023-11-01 08:00:00', 'Almacén Central Sevilla', 1, 1);

INSERT INTO envio_vehiculo (estado, fecha, lugar, envio_id, vehiculo_id) 
VALUES ('EN RUTA', '2023-11-01 14:30:00', 'Centro Logístico Córdoba', 1, 2);

INSERT INTO envio_vehiculo (estado, fecha, lugar, envio_id, vehiculo_id)
VALUES ('EN REPARTO', '2023-11-02 09:15:00', 'Delegación Sur Madrid', 1, 3);

INSERT INTO envio_vehiculo (estado, fecha, lugar, envio_id, vehiculo_id) 
VALUES ('ENTREGADO', '2023-11-02 11:45:00', 'Domicilio Cliente Madrid', 1, 3);

INSERT INTO envio_vehiculo (estado, fecha, lugar, envio_id, vehiculo_id) 
VALUES ('PREPARADO', '2023-11-05 10:00:00', 'Puerto de Huelva', 2, 4);

INSERT INTO envio_vehiculo (estado, fecha, lugar, envio_id, vehiculo_id) 
VALUES ('EN RUTA', '2023-11-05 18:00:00', 'Centro Logístico Sevilla', 2, 2);

INSERT INTO envio_vehiculo (estado, fecha, lugar, envio_id, vehiculo_id) 
VALUES ('PREPARADO', '2023-11-10 07:30:00', 'Oficina Cádiz', 3, 1);

ALTER SEQUENCE conductor_seq RESTART WITH 10;
ALTER SEQUENCE vehiculo_seq RESTART WITH 10;
ALTER SEQUENCE envio_seq RESTART WITH 10;
ALTER SEQUENCE envio_vehiculo_seq RESTART WITH 10;

ALTER TABLE conductor ALTER COLUMN id RESTART WITH 10;
ALTER TABLE vehiculo ALTER COLUMN id RESTART WITH 10;
ALTER TABLE envio ALTER COLUMN id RESTART WITH 10;
ALTER TABLE envio_vehiculo ALTER COLUMN id RESTART WITH 10;