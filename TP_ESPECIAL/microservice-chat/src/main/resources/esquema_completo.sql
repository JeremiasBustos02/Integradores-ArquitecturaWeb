-- Esquema de la base de datos del Sistema de Monopatines (Chat IA)
-- Este esquema se usa para que el LLM genere consultas SQL apropiadas

-- Tabla de usuarios
CREATE TABLE usuarios (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(255) NOT NULL,
    apellido VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    celular VARCHAR(255) NOT NULL UNIQUE,
    rol VARCHAR(50) NOT NULL
    -- rol puede ser: ADMIN, USUARIO, PREMIUM
);

-- Tabla de paradas (estaciones de monopatines)
CREATE TABLE paradas (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(255) NOT NULL,
    ubicacion VARCHAR(255) NOT NULL,
    latitud DOUBLE NOT NULL,
    longitud DOUBLE NOT NULL,
    capacidad INT
    -- ubicacion es la dirección completa (ej: "Av. Principal 123, CABA")
);

-- Tabla de viajes
CREATE TABLE viajes (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    usuario_id BIGINT NOT NULL,
    monopatin_id BIGINT NOT NULL,
    parada_origen_id BIGINT,
    parada_destino_id BIGINT,
    fecha_inicio DATETIME NOT NULL,
    fecha_fin DATETIME,
    distancia_km DOUBLE,
    duracion_minutos INT,
    costo_total DOUBLE,
    estado VARCHAR(50)
    -- estado puede ser: EN_CURSO, FINALIZADO, CANCELADO
    -- fecha_inicio es cuando comenzó el viaje
    -- fecha_fin es cuando terminó el viaje (NULL si está en curso)
    -- distancia_km es la distancia total recorrida
    -- duracion_minutos es el tiempo total del viaje
);

-- Relaciones:
-- viajes.usuario_id -> usuarios.id
-- viajes.parada_origen_id -> paradas.id
-- viajes.parada_destino_id -> paradas.id

