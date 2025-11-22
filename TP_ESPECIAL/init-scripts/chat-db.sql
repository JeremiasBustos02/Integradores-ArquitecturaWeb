-- Inicialización de base de datos para microservicio de chat
-- Este script se ejecuta automáticamente cuando MySQL inicia por primera vez

CREATE DATABASE IF NOT EXISTS chat_db;
USE chat_db;

-- Tabla de usuarios
CREATE TABLE IF NOT EXISTS usuarios (
  id bigint NOT NULL AUTO_INCREMENT,
  nombre varchar(255) NOT NULL,
  apellido varchar(255) NOT NULL,
  email varchar(255) NOT NULL,
  celular varchar(255) NOT NULL,
  rol varchar(50) NOT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY UK_email (email),
  UNIQUE KEY UK_celular (celular)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Tabla de paradas
CREATE TABLE IF NOT EXISTS paradas (
  id bigint NOT NULL AUTO_INCREMENT,
  nombre varchar(255) NOT NULL,
  ubicacion varchar(255) NOT NULL,
  latitud double NOT NULL,
  longitud double NOT NULL,
  capacidad int DEFAULT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Tabla de viajes
CREATE TABLE IF NOT EXISTS viajes (
  id bigint NOT NULL AUTO_INCREMENT,
  usuario_id bigint NOT NULL,
  monopatin_id bigint NOT NULL,
  parada_origen_id bigint DEFAULT NULL,
  parada_destino_id bigint DEFAULT NULL,
  fecha_inicio datetime NOT NULL,
  fecha_fin datetime DEFAULT NULL,
  distancia_km double DEFAULT NULL,
  duracion_minutos int DEFAULT NULL,
  costo_total double DEFAULT NULL,
  estado varchar(50) DEFAULT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Insertar datos de usuarios (5 PREMIUM, 5 USUARIO)
INSERT INTO usuarios (id, nombre, apellido, email, celular, rol) VALUES 
(1,'Juan','Pérez','juan.perez@example.com','1134567890','PREMIUM'),
(2,'María','González','maria.gonzalez@example.com','1145678901','PREMIUM'),
(3,'Carlos','Rodríguez','carlos.rodriguez@example.com','1156789012','USUARIO'),
(4,'Ana','Martínez','ana.martinez@example.com','1167890123','PREMIUM'),
(5,'Luis','Fernández','luis.fernandez@example.com','1178901234','USUARIO'),
(6,'Laura','López','laura.lopez@example.com','1189012345','PREMIUM'),
(7,'Diego','Sánchez','diego.sanchez@example.com','1190123456','USUARIO'),
(8,'Sofía','Ramírez','sofia.ramirez@example.com','1101234567','PREMIUM'),
(9,'Martín','Torres','martin.torres@example.com','1112345678','USUARIO'),
(10,'Valentina','Flores','valentina.flores@example.com','1123456789','PREMIUM');

-- Insertar datos de paradas
INSERT INTO paradas (id, nombre, ubicacion, latitud, longitud, capacidad) VALUES 
(1,'Parada Central','Av. Principal 123, CABA',-34.6037,-58.3816,20),
(2,'Parada Norte','Calle Norte 456, CABA',-34.5892,-58.3974,15),
(3,'Parada Sur','Av. Sur 789, CABA',-34.6158,-58.3682,18),
(4,'Parada Este','Calle Este 321, CABA',-34.6025,-58.3701,12),
(5,'Parada Oeste','Av. Oeste 654, CABA',-34.6048,-58.3931,16),
(6,'Parada Plaza','Plaza San Martín s/n, CABA',-34.5955,-58.3753,25),
(7,'Parada Puerto','Av. Costanera 987, CABA',-34.6083,-58.3634,20),
(8,'Parada Universidad','Av. Universidad 246, CABA',-34.5987,-58.3845,22),
(9,'Parada Parque','Parque Centenario s/n, CABA',-34.6065,-58.4317,18),
(10,'Parada Terminal','Terminal de Ómnibus s/n, CABA',-34.6116,-58.3699,30);

-- Insertar datos de viajes (30 viajes de ejemplo)
INSERT INTO viajes (id, usuario_id, monopatin_id, parada_origen_id, parada_destino_id, fecha_inicio, fecha_fin, distancia_km, duracion_minutos, costo_total, estado) VALUES 
(1,1,101,1,2,'2025-01-15 08:30:00','2025-01-15 08:55:00',5.2,25,450.00,'FINALIZADO'),
(2,1,102,2,3,'2025-01-16 09:15:00','2025-01-16 09:40:00',6.8,25,520.00,'FINALIZADO'),
(3,1,103,3,1,'2025-01-17 10:00:00','2025-01-17 10:35:00',7.5,35,680.00,'FINALIZADO'),
(4,2,104,1,4,'2025-01-15 11:20:00','2025-01-15 11:50:00',8.2,30,720.00,'FINALIZADO'),
(5,2,105,4,5,'2025-01-16 14:30:00','2025-01-16 15:10:00',10.5,40,890.00,'FINALIZADO'),
(6,2,106,5,1,'2025-01-17 16:45:00','2025-01-17 17:20:00',9.3,35,800.00,'FINALIZADO'),
(7,3,107,2,6,'2025-01-15 07:00:00','2025-01-15 07:25:00',4.5,25,380.00,'FINALIZADO'),
(8,3,108,6,7,'2025-01-18 08:30:00','2025-01-18 09:00:00',6.2,30,550.00,'FINALIZADO'),
(9,4,109,1,8,'2025-01-15 12:00:00','2025-01-15 12:40:00',11.2,40,950.00,'FINALIZADO'),
(10,4,110,8,9,'2025-01-16 13:15:00','2025-01-16 14:00:00',12.8,45,1050.00,'FINALIZADO'),
(11,4,111,9,1,'2025-01-17 15:30:00','2025-01-17 16:15:00',13.5,45,1100.00,'FINALIZADO'),
(12,5,112,3,4,'2025-01-15 09:00:00','2025-01-15 09:20:00',3.2,20,280.00,'FINALIZADO'),
(13,6,113,1,10,'2025-01-15 10:30:00','2025-01-15 11:20:00',15.6,50,1250.00,'FINALIZADO'),
(14,6,114,10,1,'2025-01-16 17:00:00','2025-01-16 17:55:00',16.2,55,1320.00,'FINALIZADO'),
(15,7,115,5,6,'2025-01-15 08:00:00','2025-01-15 08:30:00',5.8,30,490.00,'FINALIZADO'),
(16,8,116,1,7,'2025-01-15 14:00:00','2025-01-15 14:45:00',10.8,45,920.00,'FINALIZADO'),
(17,8,117,7,8,'2025-01-16 15:30:00','2025-01-16 16:10:00',8.5,40,760.00,'FINALIZADO'),
(18,8,118,8,1,'2025-01-17 18:00:00','2025-01-17 18:50:00',14.2,50,1180.00,'FINALIZADO'),
(19,9,119,2,9,'2025-01-15 11:00:00','2025-01-15 11:40:00',9.7,40,830.00,'FINALIZADO'),
(20,10,120,1,3,'2025-01-15 13:00:00','2025-01-15 13:35:00',7.8,35,690.00,'FINALIZADO'),
(21,10,121,3,5,'2025-01-16 16:00:00','2025-01-16 16:45:00',11.5,45,980.00,'FINALIZADO'),
(22,10,122,5,10,'2025-01-17 19:00:00','2025-01-17 20:00:00',18.3,60,1480.00,'FINALIZADO'),
(23,1,123,2,4,'2025-01-18 08:00:00','2025-01-18 08:30:00',6.5,30,570.00,'FINALIZADO'),
(24,2,124,4,6,'2025-01-18 10:00:00','2025-01-18 10:40:00',9.2,40,810.00,'FINALIZADO'),
(25,4,125,1,9,'2025-01-18 12:00:00','2025-01-18 13:00:00',17.5,60,1420.00,'FINALIZADO'),
(26,6,126,10,5,'2025-01-18 14:00:00','2025-01-18 14:55:00',16.8,55,1360.00,'FINALIZADO'),
(27,8,127,7,2,'2025-01-18 16:00:00','2025-01-18 16:50:00',14.9,50,1230.00,'FINALIZADO'),
(28,10,128,3,8,'2025-01-18 18:00:00','2025-01-18 19:00:00',19.2,60,1550.00,'FINALIZADO'),
(29,1,129,1,NULL,'2025-01-19 09:00:00',NULL,NULL,NULL,NULL,'EN_CURSO'),
(30,6,130,6,NULL,'2025-01-19 10:30:00',NULL,NULL,NULL,NULL,'EN_CURSO');
