-- 1. Insertar Vehículos (No tienen dependencias de otras tablas)
INSERT INTO vehiculo (matricula, capacidad) VALUES ('1234-ABC', 1500.0);
INSERT INTO vehiculo (matricula, capacidad) VALUES ('5678-DEF', 12000.0);
INSERT INTO vehiculo (matricula, capacidad) VALUES ('9012-GHI', 3500.0);
INSERT INTO vehiculo (matricula, capacidad) VALUES ('3456-JKL', 1500.0);

-- 2. Insertar Conductores (Dependen de Vehículos -> vehiculo_id)
INSERT INTO conductor (nombre, experiencia, vehiculo_id) VALUES ('José Díaz', 5, 1);
INSERT INTO conductor (nombre, experiencia, vehiculo_id) VALUES ('Laura Sánchez', 12, 2);
INSERT INTO conductor (nombre, experiencia, vehiculo_id) VALUES ('Carlos Ruiz', 2, 3);
INSERT INTO conductor (nombre, experiencia, vehiculo_id) VALUES ('Ana Martín', 8, 4);
INSERT INTO conductor (nombre, experiencia, vehiculo_id) VALUES ('Luis López', 1, 1); -- Otro conductor que usa el mismo vehículo en otro turno

-- 3. Insertar Envíos (No tienen dependencias directas para crearse)
INSERT INTO envio (codigo, origen, destino, peso, coste) VALUES ('ENV-2023-001', 'Sevilla', 'Madrid', 120.5, 45.50);
INSERT INTO envio (codigo, origen, destino, peso, coste) VALUES ('ENV-2023-002', 'Huelva', 'Barcelona', 450.0, 150.00);
INSERT INTO envio (codigo, origen, destino, peso, coste) VALUES ('ENV-2023-003', 'Cádiz', 'Valencia', 25.0, 15.75);

-- 4. Insertar las Rutas/Etapas en la tabla intermedia EnvioVehiculo 
-- (Dependen de Envios -> envio_id y de Vehículos -> vehiculo_id)
-- Nota: H2 acepta el formato 'YYYY-MM-DD HH:MM:SS' para LocalDateTime

-- Trazabilidad del Envío 1 (Sevilla -> Madrid)
INSERT INTO envio_vehiculo (estado, fecha, lugar, envio_id, vehiculo_id) 
VALUES ('PREPARADO', '2023-11-01 08:00:00', 'Almacén Central Sevilla', 1, 1);

INSERT INTO envio_vehiculo (estado, fecha, lugar, envio_id, vehiculo_id) 
VALUES ('EN RUTA', '2023-11-01 14:30:00', 'Centro Logístico Córdoba', 1, 2);

INSERT INTO envio_vehiculo (estado, fecha, lugar, envio_id, vehiculo_id) 
VALUES ('EN REPARTO', '2023-11-02 09:15:00', 'Delegación Sur Madrid', 1, 3);

INSERT INTO envio_vehiculo (estado, fecha, lugar, envio_id, vehiculo_id) 
VALUES ('ENTREGADO', '2023-11-02 11:45:00', 'Domicilio Cliente Madrid', 1, 3);

-- Trazabilidad del Envío 2 (Huelva -> Barcelona)
INSERT INTO envio_vehiculo (estado, fecha, lugar, envio_id, vehiculo_id) 
VALUES ('PREPARADO', '2023-11-05 10:00:00', 'Puerto de Huelva', 2, 4);

INSERT INTO envio_vehiculo (estado, fecha, lugar, envio_id, vehiculo_id) 
VALUES ('EN RUTA', '2023-11-05 18:00:00', 'Centro Logístico Sevilla', 2, 2);

-- Trazabilidad del Envío 3 (Cádiz -> Valencia - Apenas empezando)
INSERT INTO envio_vehiculo (estado, fecha, lugar, envio_id, vehiculo_id) 
VALUES ('PREPARADO', '2023-11-10 07:30:00', 'Oficina Cádiz', 3, 1);