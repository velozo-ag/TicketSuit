CREATE DATABASE TicketSuit;

USE TicketSuit;

CREATE TABLE Cine
(
  id_cine INT NOT NULL,
  nombre VARCHAR(200) NOT NULL,
  direccion VARCHAR(200) NOT NULL,
  CONSTRAINT PK_cine PRIMARY KEY (id_cine)
);

CREATE TABLE Sala
(
  id_sala INT NOT NULL,
  nombre VARCHAR(200) NOT NULL,
  capacidad INT NOT NULL,
  id_cine INT NOT NULL,
  CONSTRAINT PK_sala PRIMARY KEY (id_sala),
  CONSTRAINT FK_sala_cine FOREIGN KEY (id_cine) REFERENCES Cine(id_cine)
);

CREATE TABLE Asiento
(
  id_asiento INT NOT NULL,
  letra_fila VARCHAR(2) NOT NULL,
  numero_columna INT NOT NULL,
  id_sala INT NOT NULL,
  CONSTRAINT PK_asiento PRIMARY KEY (id_asiento, id_sala),
  CONSTRAINT FK_asiento_sala FOREIGN KEY (id_sala) REFERENCES Sala(id_sala)
);

CREATE TABLE Perfil
(
  id_perfil INT NOT NULL,
  nombre VARCHAR(200) NOT NULL,
  CONSTRAINT PK_perfil PRIMARY KEY (id_perfil)
);

CREATE TABLE TipoFuncion
(
  id_tipoFuncion INT NOT NULL,
  descripcion VARCHAR(200) NOT NULL,
  precio FLOAT NOT NULL,
  CONSTRAINT PK_tipoFuncion PRIMARY KEY (id_tipoFuncion)
);

CREATE TABLE Clasificacion
(
  id_clasificacion INT NOT NULL,
  descripcion VARCHAR(200) NOT NULL,
  nombre VARCHAR(4) NOT NULL,
  CONSTRAINT PK_clasificacion PRIMARY KEY (id_clasificacion)
);

CREATE TABLE Director
(
  id_director INT NOT NULL,
  nombre VARCHAR(200) NOT NULL,
  CONSTRAINT PK_director PRIMARY KEY (id_director)
);

CREATE TABLE Genero
(
  id_genero INT NOT NULL,
  descripcion VARCHAR(200) NOT NULL,
  CONSTRAINT PK_genero PRIMARY KEY (id_genero)
);

CREATE TABLE Usuario
(
  id_usuario INT IDENTITY(1,1),
  nombre VARCHAR(200) NOT NULL,
  password VARCHAR(200) NOT NULL,
  estado INT NOT NULL,
  id_perfil INT NOT NULL,
  id_cine INT NOT NULL,
  CONSTRAINT PK_usuario PRIMARY KEY (id_usuario),
  CONSTRAINT FK_usuario_perfil FOREIGN KEY (id_perfil) REFERENCES Perfil(id_perfil),
  CONSTRAINT FK_usuario_cine FOREIGN KEY (id_cine) REFERENCES Cine(id_cine)
);


CREATE TABLE Pelicula
(
  id_pelicula INT NOT NULL,
  nombre VARCHAR(200) NOT NULL,
  duracion INT NOT NULL,
  id_clasificacion INT NOT NULL,
  id_director INT NOT NULL,
  sinopsis VARCHAR (350) NOT NULL,
  imagen VARCHAR (350),
  nacionalidad VARCHAR (100),
  CONSTRAINT PK_pelicula PRIMARY KEY (id_pelicula),
  CONSTRAINT FK_pelicula_clasificacion FOREIGN KEY (id_clasificacion) REFERENCES Clasificacion(id_clasificacion),
  CONSTRAINT FK_pelicula_director FOREIGN KEY (id_director) REFERENCES Director(id_director),
);

CREATE TABLE Compra
(
  id_compra INT NOT NULL,
  subtotal FLOAT NOT NULL,
  fecha DATE NOT NULL,
  cantidad INT NOT NULL,
  id_usuario INT NOT NULL,
  CONSTRAINT PK_compra PRIMARY KEY (id_compra),
  CONSTRAINT FK_compra_usuario FOREIGN KEY (id_usuario) REFERENCES Usuario(id_usuario)
);

CREATE TABLE Funcion
(
  id_funcion INT NOT NULL,
  hora_inicio VARCHAR(6) NOT NULL,
  hora_final VARCHAR(6) NOT NULL,
  fecha DATE NOT NULL,
  id_pelicula INT NOT NULL,
  id_tipoFuncion INT NOT NULL,
  CONSTRAINT PK_funcion PRIMARY KEY (id_funcion),
  CONSTRAINT FK_funcion_pelicula FOREIGN KEY (id_pelicula) REFERENCES Pelicula(id_pelicula),
  CONSTRAINT FK_funcion_tipoFuncion FOREIGN KEY (id_tipoFuncion) REFERENCES TipoFuncion(id_tipoFuncion)
);

CREATE TABLE Sala_Funcion
(
  id_funcion INT NOT NULL,
  id_sala INT NOT NULL,
  CONSTRAINT PK_sala_funcion PRIMARY KEY (id_funcion, id_sala),
  CONSTRAINT FK_sala_funcion_funcion FOREIGN KEY (id_funcion) REFERENCES Funcion(id_funcion),
  CONSTRAINT FK_sala_funcion_sala FOREIGN KEY (id_sala) REFERENCES Sala(id_sala)
);

CREATE TABLE Ticket
(
  id_ticket INT NOT NULL,
  id_funcion INT NOT NULL,
  id_sala INT NOT NULL,
  id_asiento INT NOT NULL,
  id_compra INT NOT NULL,
  CONSTRAINT PK_ticket PRIMARY KEY (id_ticket),
  CONSTRAINT FK_ticket_sala_funcion FOREIGN KEY (id_funcion, id_sala) REFERENCES Sala_Funcion(id_funcion, id_sala),
  CONSTRAINT FK_ticket_asiento FOREIGN KEY (id_asiento, id_sala) REFERENCES Asiento(id_asiento, id_sala),
  CONSTRAINT FK_ticket_compra FOREIGN KEY (id_compra) REFERENCES Compra(id_compra)
);

CREATE TABLE Genero_Pelicula
(
  id_pelicula INT NOT NULL,
  id_genero INT NOT NULL,
  CONSTRAINT PK_genero_pelicula PRIMARY KEY (id_pelicula, id_genero),
  CONSTRAINT FK_genero_pelicula_pelicula FOREIGN KEY (id_pelicula) REFERENCES Pelicula(id_pelicula),
  CONSTRAINT FK_genero_pelicula_genero FOREIGN KEY (id_genero) REFERENCES Genero(id_genero)
);

INSERT INTO Cine (id_cine, nombre, direccion) VALUES
(1, 'TicketSuit', 'Av. Libertad 1234');

INSERT INTO Perfil (id_perfil, nombre) VALUES 
(1, 'Administrador'),
(2, 'Vendedor'),
(3, 'Gerente');

INSERT INTO Usuario (nombre, password, estado, id_perfil, id_cine) VALUES
('administrador', 'administrador', 1, 1, 1),
('vendedor', 'vendedor', 1, 2, 1),
('gerente', 'gerente', 1, 3, 1);


INSERT INTO Clasificacion (id_clasificacion, descripcion, nombre) VALUES
(1, 'Pelicula apta para todo publico', 'A'),
(2, 'Pelicula apta para adolecentes mayores de 12', 'B'),
(3, 'Pelicula apta para adolescentes mayores de 15', 'B15'),
(4, 'Pelicula restringida, no apta para menores de 17', 'R');

INSERT INTO Genero (id_genero, descripcion) VALUES
(1, 'Animacion'),
(2, 'Aventura'),
(3, 'Accion'),
(4, 'Superheroes'),
(5, 'Ciencia Ficcion'),
(6, 'Suspenso'),
(7, 'Crimen'),
(8, 'Drama'),
(9, 'Psicologo'),
(10,'Comedia');

INSERT INTO Director (id_director, nombre) VALUES
(1, 'John Lasseter'),
(2, 'Jon Watts'),
(3, 'Cristopher Nolan'),
(4, 'Todd Phillips'),
(5, 'Hayao Miyazaki'),
(6, 'Steven Spielberg'),
(7, 'Las Wachowski'),
(8, 'Lee Unkrich'),
(9, 'Christopher Nolan'),
(10, 'Ridley Scott');

INSERT INTO Pelicula (id_pelicula, nombre, duracion, id_clasificacion, id_director, sinopsis, nacionalidad) VALUES
(1, 'El viaje de Chihiro', 125, 1, 5, 'Una niña que se aventura en un mundo mágico mientras busca una forma de salvar a sus padres.', 'Japón'),
(2, 'Los Vengadores', 143, 2, 2, 'Un grupo de superhéroes se une para enfrentar una amenaza global.', 'EE.UU.'),
(3, 'Titanic', 195, 3, 3, 'Una historia de amor que surge en medio del desastre del Titanic.', 'EE.UU.'),
(4, 'El Padrino', 175, 4, 4, 'La saga de una familia mafiosa liderada por Vito Corleone.', 'EE.UU.'),
(5, 'Interestelar', 169, 2, 5, 'Un grupo de astronautas busca un nuevo hogar para la humanidad en el espacio.', 'EE.UU.'),
(6, 'Parasite', 132, 3, 6, 'Una familia pobre se infiltra en la vida de una familia rica, con consecuencias inesperadas.', 'Corea del Sur'),
(7, 'La La Land', 128, 2, 7, 'Dos artistas en Los Ángeles luchan por lograr sus sueños mientras enfrentan desafíos en su relación.', 'EE.UU.'),
(8, 'Coco', 105, 1, 8, 'Un niño viaja al mundo de los muertos para descubrir la verdad sobre su familia.', 'México'),
(9, 'El Origen', 148, 4, 9, 'Un ladrón con la capacidad de entrar en los sueños de las personas intenta realizar el trabajo definitivo.', 'EE.UU.'),
(10, 'Gladiador', 155, 4, 10, 'Un general romano se convierte en gladiador para vengar la muerte de su familia.', 'EE.UU.');

INSERT INTO Genero_Pelicula (id_pelicula, id_genero) VALUES
(1, 1),
(2, 2),
(3, 2),
(3, 5),
(4, 3),
(5, 4),
(6, 3),
(7, 3),
(8, 1),
(9, 4), 
(10, 2);

INSERT INTO TipoFuncion (id_tipoFuncion, descripcion, precio) VALUES
(1, '2D', 2500),
(2, '3D', 3500)

INSERT INTO Sala (id_sala, nombre, capacidad, id_cine) VALUES
(1, 'SALA 1', 56, 1),
(2, 'SALA 2', 56, 1)

-- Inserciones para la Sala 1 (56 asientos, 8 por fila)
INSERT INTO Asiento (id_asiento, letra_fila, numero_columna, id_sala) VALUES
(1, 'A', 1, 1), (2, 'A', 2, 1), (3, 'A', 3, 1), (4, 'A', 4, 1), (5, 'A', 5, 1), (6, 'A', 6, 1), (7, 'A', 7, 1), (8, 'A', 8, 1),
(9, 'B', 1, 1), (10, 'B', 2, 1), (11, 'B', 3, 1), (12, 'B', 4, 1), (13, 'B', 5, 1), (14, 'B', 6, 1), (15, 'B', 7, 1), (16, 'B', 8, 1),
(17, 'C', 1, 1), (18, 'C', 2, 1), (19, 'C', 3, 1), (20, 'C', 4, 1), (21, 'C', 5, 1), (22, 'C', 6, 1), (23, 'C', 7, 1), (24, 'C', 8, 1),
(25, 'D', 1, 1), (26, 'D', 2, 1), (27, 'D', 3, 1), (28, 'D', 4, 1), (29, 'D', 5, 1), (30, 'D', 6, 1), (31, 'D', 7, 1), (32, 'D', 8, 1),
(33, 'E', 1, 1), (34, 'E', 2, 1), (35, 'E', 3, 1), (36, 'E', 4, 1), (37, 'E', 5, 1), (38, 'E', 6, 1), (39, 'E', 7, 1), (40, 'E', 8, 1),
(41, 'F', 1, 1), (42, 'F', 2, 1), (43, 'F', 3, 1), (44, 'F', 4, 1), (45, 'F', 5, 1), (46, 'F', 6, 1), (47, 'F', 7, 1), (48, 'F', 8, 1),
(49, 'G', 1, 1), (50, 'G', 2, 1), (51, 'G', 3, 1), (52, 'G', 4, 1), (53, 'G', 5, 1), (54, 'G', 6, 1), (55, 'G', 7, 1), (56, 'G', 8, 1);

-- Inserciones para la Sala 2 (56 asientos, 8 por fila, id_asiento de 1 a 56)
INSERT INTO Asiento (id_asiento, letra_fila, numero_columna, id_sala) VALUES
(1, 'A', 1, 2), (2, 'A', 2, 2), (3, 'A', 3, 2), (4, 'A', 4, 2), (5, 'A', 5, 2), (6, 'A', 6, 2), (7, 'A', 7, 2), (8, 'A', 8, 2),
(9, 'B', 1, 2), (10, 'B', 2, 2), (11, 'B', 3, 2), (12, 'B', 4, 2), (13, 'B', 5, 2), (14, 'B', 6, 2), (15, 'B', 7, 2), (16, 'B', 8, 2),
(17, 'C', 1, 2), (18, 'C', 2, 2), (19, 'C', 3, 2), (20, 'C', 4, 2), (21, 'C', 5, 2), (22, 'C', 6, 2), (23, 'C', 7, 2), (24, 'C', 8, 2),
(25, 'D', 1, 2), (26, 'D', 2, 2), (27, 'D', 3, 2), (28, 'D', 4, 2), (29, 'D', 5, 2), (30, 'D', 6, 2), (31, 'D', 7, 2), (32, 'D', 8, 2),
(33, 'E', 1, 2), (34, 'E', 2, 2), (35, 'E', 3, 2), (36, 'E', 4, 2), (37, 'E', 5, 2), (38, 'E', 6, 2), (39, 'E', 7, 2), (40, 'E', 8, 2),
(41, 'F', 1, 2), (42, 'F', 2, 2), (43, 'F', 3, 2), (44, 'F', 4, 2), (45, 'F', 5, 2), (46, 'F', 6, 2), (47, 'F', 7, 2), (48, 'F', 8, 2),
(49, 'G', 1, 2), (50, 'G', 2, 2), (51, 'G', 3, 2), (52, 'G', 4, 2), (53, 'G', 5, 2), (54, 'G', 6, 2), (55, 'G', 7, 2), (56, 'G', 8, 2);

INSERT INTO Funcion (id_funcion, hora_inicio, hora_final, fecha, id_pelicula, id_tipoFuncion) VALUES
(1, '10:00', '12:00', '2024-10-01', 1, 1),  -- Función de "El viaje de Chihiro"
(2, '14:00', '16:30', '2024-10-01', 2, 1),  -- Función de "Los Vengadores"
(3, '16:45', '19:30', '2024-10-01', 3, 2),  -- Función de "Titanic"
(4, '19:45', '22:00', '2024-10-01', 4, 1),  -- Función de "El Padrino"
(5, '12:00', '14:30', '2024-10-02', 5, 2),  -- Función de "Interestelar"
(6, '15:00', '17:30', '2024-10-02', 6, 1),  -- Función de "Parasite"
(7, '18:00', '20:00', '2024-10-02', 7, 2),  -- Función de "La La Land"
(8, '20:30', '22:15', '2024-10-02', 8, 1),  -- Función de "Coco"
(9, '11:00', '13:30', '2024-10-03', 9, 2),  -- Función de "El Origen"
(10, '14:00', '16:30', '2024-10-03', 10, 1); -- Función de "Gladiador"

INSERT INTO Sala_Funcion (id_funcion, id_sala) VALUES
(1, 1),
(2, 1),
(3, 2),
(4, 2),
(5, 1),
(6, 1),
(7, 2),
(8, 1),
(9, 2),
(10, 1);

-- Insertar compras en Cine 1
INSERT INTO Compra (id_compra, subtotal, fecha, cantidad, id_usuario) VALUES
(1, 2500, '2022-01-15', 1, 2),
(2, 3500, '2022-02-10', 2, 2),
(3, 4500, '2022-03-05', 3, 2),
(4, 3000, '2022-04-20', 1, 2),
(5, 4000, '2022-05-15', 4, 2),
(6, 2500, '2022-06-25', 1, 2),
(7, 5000, '2022-07-30', 5, 2),
(8, 3600, '2022-08-10', 2, 2),
(9, 2800, '2022-09-15', 1, 2),
(10, 3200, '2022-10-20', 3, 2),
(11, 2900, '2022-11-30', 2, 2),
(12, 3700, '2022-12-25', 4, 2),
(13, 2600, '2023-01-15', 1, 2),
(14, 4800, '2023-02-10', 3, 2),
(15, 3900, '2023-03-05', 2, 2),
(16, 3100, '2023-04-20', 2, 2),
(17, 3300, '2023-05-15', 3, 2),
(18, 4500, '2023-06-25', 5, 2),
(19, 4700, '2023-07-30', 4, 2),
(20, 2900, '2023-08-10', 1, 2),
(21, 3700, '2023-09-15', 2, 2),
(22, 3800, '2023-10-20', 3, 2),
(23, 4300, '2023-11-30', 5, 2),
(24, 2600, '2023-12-25', 1, 2),
(25, 3000, '2024-01-15', 2, 2),
(26, 4100, '2024-02-10', 4, 2),
(27, 3400, '2024-03-05', 3, 2),
(28, 2700, '2024-04-20', 1, 2),
(29, 3900, '2024-05-15', 2, 2),
(30, 4300, '2024-06-25', 5, 2),
(31, 5200, '2024-07-30', 6, 2),
(32, 3300, '2024-08-10', 3, 2),
(33, 4000, '2024-09-15', 4, 2),
(34, 4500, '2024-10-20', 5, 2),
(35, 4800, '2024-11-30', 6, 2),
(36, 3700, '2024-12-25', 4, 2),
(37, 2800, '2022-01-10', 1, 2),
(38, 3900, '2022-02-20', 3, 2),
(39, 4100, '2022-03-12', 2, 2),
(40, 5000, '2022-04-30', 5, 2),
(41, 3000, '2022-05-25', 1, 2),
(42, 3600, '2022-06-15', 2, 2),
(43, 2700, '2022-07-18', 1, 2),
(44, 3300, '2022-08-22', 3, 2),
(45, 4500, '2022-09-09', 4, 2),
(46, 4900, '2022-10-16', 5, 2),
(47, 3100, '2022-11-11', 2, 2),
(48, 4400, '2022-12-05', 4, 2),
(49, 3900, '2023-01-20', 2, 2),
(50, 4800, '2023-02-15', 3, 2);
SELECT * FROM usuario;

----------------------
----------------------
-- Hasta aca todo bien, el problema es cuando quiero ingresar los tickets, solo me deja ingresar los que tengan id compra 1 o 2
----------------------
----------------------
INSERT INTO Ticket (id_ticket, id_funcion, id_sala, id_asiento, id_compra) VALUES
(1, 1, 1, 5, 1),
(2, 2, 1, 12, 2),
(3, 2, 1, 15, 2),
(4, 3, 2, 7, 3),
(5, 3, 2, 9, 3),
(6, 3, 2, 11, 3),
(7, 4, 2, 3, 4),
(8, 5, 1, 18, 5),
(9, 5, 1, 20, 5),
(10, 5, 1, 22, 5),
(11, 5, 1, 24, 5),
(12, 6, 1, 1, 6),
(13, 7, 2, 12, 7),
(14, 7, 2, 14, 7),
(15, 7, 2, 16, 7),
(16, 7, 2, 18, 7),
(17, 7, 2, 20, 7),
(18, 8, 1, 2, 8),
(19, 8, 1, 4, 8),
(20, 9, 2, 6, 9),
(21, 10, 1, 8, 10),
(22, 10, 1, 10, 10),
(23, 10, 1, 12, 10),
(24, 4, 2, 2, 11),
(25, 4, 2, 4, 11),
(26, 5, 1, 5, 12),
(27, 5, 1, 7, 12),
(28, 6, 1, 9, 12),
(29, 1, 1, 6, 13),
(30, 2, 1, 13, 14),
(31, 2, 1, 16, 14),
(32, 3, 2, 8, 15),
(33, 3, 2, 10, 15),
(34, 4, 2, 13, 16),
(35, 5, 1, 21, 17),
(36, 5, 1, 23, 17),
(37, 6, 1, 3, 18),
(38, 7, 2, 19, 19),
(39, 7, 2, 21, 19),
(40, 8, 1, 6, 20),
(41, 9, 2, 8, 21),
(42, 10, 1, 10, 22),
(43, 10, 1, 14, 22),
(44, 4, 2, 5, 23),
(45, 5, 1, 25, 24),
(46, 6, 1, 11, 25),
(47, 7, 2, 22, 26),
(48, 8, 1, 17, 27),
(49, 9, 2, 18, 28),
(50, 10, 1, 26, 29),
(51, 4, 2, 9, 30),
(52, 4, 2, 10, 30),
(53, 5, 1, 15, 31),
(54, 5, 1, 19, 31),
(55, 6, 1, 24, 32),
(56, 6, 1, 26, 32),
(57, 7, 2, 3, 33),
(58, 7, 2, 4, 33),
(59, 8, 1, 8, 34),
(60, 8, 1, 14, 34),
(61, 9, 2, 12, 35),
(62, 9, 2, 15, 35),
(63, 10, 1, 5, 36),
(64, 10, 1, 7, 36),
(65, 10, 1, 9, 36),
(66, 4, 2, 1, 37),
(67, 5, 1, 11, 38),
(68, 6, 1, 13, 39),
(69, 7, 2, 17, 40),
(70, 8, 1, 21, 41),
(71, 9, 2, 23, 42),
(72, 10, 1, 27, 43),
(73, 10, 1, 29, 43),
(74, 4, 2, 8, 44),
(75, 5, 1, 30, 45),
(76, 6, 1, 15, 46),
(77, 7, 2, 25, 47),
(78, 8, 1, 3, 48),
(79, 9, 2, 14, 49),
(80, 10, 1, 11, 50);

select * from perfil
select * from usuario;

/*

SELECT * FROM Asiento;

SELECT * FROM Funcion;
SELECT MONTH(C.fecha) AS mes, SUM(C.subtotal) AS total_ingresos
FROM Compra C
GROUP BY MONTH(C.fecha);

-- Calcular el rendimiento por mes en ingresos de todo el a;o
SELECT MONTH(fecha) AS mes, SUM(subtotal) AS total_ingresos
FROM Compra
WHERE fecha >= DATEADD(YEAR, -1, GETDATE()) -- Último año
GROUP BY MONTH(fecha)
ORDER BY MONTH(fecha);

SELECT Pelicula.nombre, 
		Pelicula.duracion, 
		Clasificacion.nombre as Clasificacion, 
		Clasificacion.descripcion , 
		Director.nombre as Director,
		STRING_AGG(Genero.descripcion, ', ') AS generos 
FROM Pelicula
INNER JOIN Clasificacion
ON Pelicula.id_clasificacion = Clasificacion.id_clasificacion
INNER JOIN Director
ON Pelicula.id_director = Director.id_director
INNER JOIN Genero_Pelicula
ON Pelicula.id_pelicula = Genero_Pelicula.id_pelicula
INNER JOIN Genero
ON Genero_Pelicula.id_genero = Genero.id_genero
GROUP BY 
    Pelicula.nombre, 
    Pelicula.duracion, 
    Clasificacion.nombre, 
    Clasificacion.descripcion, 
    Director.nombre;

-- Calcular el total de ingresos por cada tipo de funcion
SELECT 
    T.descripcion AS tipo_funcion,
    SUM(C.subtotal) AS total_ingresos
FROM 
    Compra C
JOIN 
    Ticket TICK ON C.id_compra = TICK.id_compra
JOIN 
    Sala_Funcion SF ON TICK.id_funcion = SF.id_funcion
JOIN 
    Funcion F ON SF.id_funcion = F.id_funcion
JOIN 
    TipoFuncion T ON F.id_tipoFuncion = T.id_tipoFuncion
WHERE 
    T.descripcion IN ('2D', '3D')
GROUP BY 
    T.descripcion;

-- Calcular la proporcion de sala utilizada vs total de la sala
SELECT 
    S.nombre AS sala,
    S.capacidad AS capacidad_total,
    COUNT(T.id_ticket) AS capacidad_utilizada
FROM 
    Sala S
LEFT JOIN 
    Sala_Funcion SF ON S.id_sala = SF.id_sala
LEFT JOIN 
    Ticket T ON SF.id_funcion = T.id_funcion
GROUP BY 
    S.nombre, S.capacidad
ORDER BY 
    S.nombre;

-- Calcular los tickets vendidos por semana del ultimo trimestre
SELECT 
    DATEPART(YEAR, c.fecha) AS anio,
    DATEPART(WEEK, c.fecha) AS semana,
    COUNT(t.id_ticket) AS total_tickets_vendidos
FROM 
    Ticket t
JOIN 
    Compra c ON t.id_compra = c.id_compra
WHERE 
    c.fecha >= DATEADD(MONTH, -3, GETDATE()) -- Filtrar por las compras del último trimestre
GROUP BY 
    DATEPART(YEAR, c.fecha), 
    DATEPART(WEEK, c.fecha)
ORDER BY 
    anio, 
    semana;
-- select ingresos totales por el ultimo a;o de la pelicula
SELECT p.nombre AS nombre_pelicula, SUM(c.subtotal) AS total_ingresos
FROM Compra c
JOIN Ticket t ON c.id_compra = t.id_compra
JOIN Funcion f ON t.id_funcion = f.id_funcion
JOIN Pelicula p ON f.id_pelicula = p.id_pelicula
WHERE YEAR(c.fecha) = YEAR(GETDATE())
GROUP BY p.nombre;

-- ingresos totales del ultimo a;o, dividido en meses y tags del trimestre
SELECT YEAR(c.fecha) AS anio,
       MONTH(c.fecha) AS mes,
       SUM(c.subtotal) AS total_ingresos,
       CASE 
           WHEN MONTH(c.fecha) IN (1, 2, 3) THEN 'Q1'
           WHEN MONTH(c.fecha) IN (4, 5, 6) THEN 'Q2'
           WHEN MONTH(c.fecha) IN (7, 8, 9) THEN 'Q3'
           WHEN MONTH(c.fecha) IN (10, 11, 12) THEN 'Q4'
       END AS trimestre
FROM Compra c
GROUP BY YEAR(c.fecha), MONTH(c.fecha)
ORDER BY anio, mes;

-- ingresos totales divididos por a;os
SELECT YEAR(c.fecha) AS anio, SUM(c.subtotal) AS total_ingresos
FROM Compra c
GROUP BY YEAR(c.fecha)
ORDER BY anio;

-- ingresos totales del ultimo a;o divididos por meses
SELECT MONTH(c.fecha) AS mes, SUM(c.subtotal) AS total_ingresos
FROM Compra c
WHERE YEAR(c.fecha) = YEAR(GETDATE())
GROUP BY MONTH(c.fecha)
ORDER BY mes;

-- Ingresos Totales por Trimestre (dividido por semanas del trimestre actual)
SELECT DATEPART(WEEK, c.fecha) AS semana, SUM(c.subtotal) AS total_ingresos
FROM Compra c
WHERE YEAR(c.fecha) = YEAR(GETDATE()) 
  AND DATEPART(QUARTER, c.fecha) = DATEPART(QUARTER, GETDATE())
GROUP BY DATEPART(WEEK, c.fecha)
ORDER BY semana;

-- cantidad de peliculas por cada genero (contador)
SELECT g.descripcion AS genero, COUNT(gp.id_pelicula) AS cantidad_peliculas
FROM Genero g
JOIN Genero_Pelicula gp ON g.id_genero = gp.id_genero
GROUP BY g.descripcion
ORDER BY cantidad_peliculas DESC;

-- cantidad de funciones en la semana actual
SELECT f.fecha AS fecha, COUNT(f.id_funcion) AS cantidad_funciones
FROM Funcion f
WHERE DATEPART(WEEK, f.fecha) = DATEPART(WEEK, GETDATE())
  AND YEAR(f.fecha) = YEAR(GETDATE()) -- busca en esta semana y en este a;o
GROUP BY f.fecha
ORDER BY f.fecha;

SELECT p.nombre AS pelicula, COUNT(t.id_ticket) AS total_asistencia
FROM Pelicula p
JOIN Funcion f ON p.id_pelicula = f.id_pelicula
JOIN Ticket t ON f.id_funcion = t.id_funcion
GROUP BY p.nombre
ORDER BY total_asistencia DESC;

SELECT g.descripcion AS genero, COUNT(t.id_ticket) AS total_asistencia
FROM Genero g
JOIN Genero_Pelicula gp ON g.id_genero = gp.id_genero
JOIN Pelicula p ON gp.id_pelicula = p.id_pelicula
JOIN Funcion f ON p.id_pelicula = f.id_pelicula
JOIN Ticket t ON f.id_funcion = t.id_funcion
GROUP BY g.descripcion
ORDER BY total_asistencia DESC;

SELECT cl.nombre AS clasificacion, COUNT(t.id_ticket) AS total_asistencia
FROM Clasificacion cl
JOIN Pelicula p ON cl.id_clasificacion = p.id_clasificacion
JOIN Funcion f ON p.id_pelicula = f.id_pelicula
JOIN Ticket t ON f.id_funcion = t.id_funcion
GROUP BY cl.nombre
ORDER BY total_asistencia DESC;

SELECT MONTH(f.fecha) AS mes, COUNT(t.id_ticket) AS total_asistencia
FROM Funcion f
JOIN Ticket t ON f.id_funcion = t.id_funcion
WHERE YEAR(f.fecha) = YEAR(GETDATE())
GROUP BY MONTH(f.fecha)
ORDER BY mes;

SELECT YEAR(f.fecha) AS anio, COUNT(t.id_ticket) AS total_asistencia
FROM Funcion f
JOIN Ticket t ON f.id_funcion = t.id_funcion
GROUP BY YEAR(f.fecha)
ORDER BY anio;

SELECT DATEPART(WEEK, f.fecha) AS semana, COUNT(t.id_ticket) AS total_asistencia
FROM Funcion f
JOIN Ticket t ON f.id_funcion = t.id_funcion
WHERE YEAR(f.fecha) = YEAR(GETDATE()) 
AND DATEPART(QUARTER, f.fecha) = DATEPART(QUARTER, GETDATE())
GROUP BY DATEPART(WEEK, f.fecha)
ORDER BY semana;

SELECT DATEPART(WEEKDAY, f.fecha) AS dia, COUNT(t.id_ticket) AS total_asistencia
FROM Funcion f
JOIN Ticket t ON f.id_funcion = t.id_funcion
WHERE YEAR(f.fecha) = YEAR(GETDATE()) 
AND DATEPART(WEEK, f.fecha) = DATEPART(WEEK, GETDATE())
GROUP BY DATEPART(WEEKDAY, f.fecha)
ORDER BY dia;

SELECT * FROM Asiento;
*/
