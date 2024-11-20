-- CREATE DATABASE TicketSuit;
--DROP DATABASE TicketSuit;

CREATE DATABASE TicketSuit;

USE TicketSuit;

SELECT * FROM Usuario;
/*
SELECT 
    princ.name AS Usuario,
    perm.permission_name AS Permiso,
    perm.state_desc AS Estado
FROM sys.database_principals AS princ
JOIN sys.database_permissions AS perm
    ON perm.grantee_principal_id = princ.principal_id
WHERE princ.name = 'usuario1';
USE master;
ALTER SERVER ROLE sysadmin ADD MEMBER [usuario1];
USE master;
GRANT BACKUP DATABASE TO [tu_usuario];
*/


---------------------------------------------------------
--|---------------------------------------------------|--
--|--------------- CREACION DE TABLAS -------------- -|--
--|---------------------------------------------------|--
---------------------------------------------------------


CREATE TABLE Cine
(
  id_cine INT IDENTITY (1,1),
  nombre VARCHAR(200) NOT NULL,
  direccion VARCHAR(200) NOT NULL,
  CONSTRAINT PK_cine PRIMARY KEY (id_cine),
  CONSTRAINT UQ_cine_nombre	UNIQUE (nombre)
);


CREATE TABLE Sala
(
  id_sala INT IDENTITY (1,1),
  nombre VARCHAR(200) NOT NULL,
  capacidad INT NOT NULL,	-- ++
  cantFilas INT NOT NULL,     -- ++
  cantColumnas INT NOT NULL,	-- ++
  id_cine INT NOT NULL,
  estado INT DEFAULT 1 NOT NULL, -- ++
  CONSTRAINT PK_sala PRIMARY KEY (id_sala),
  CONSTRAINT FK_sala_cine FOREIGN KEY (id_cine) REFERENCES Cine(id_cine),
  CONSTRAINT CHK_sala_estado CHECK (estado IN (0, 1)),
  CONSTRAINT UQ_sala_nombre	UNIQUE (nombre),
  CONSTRAINT CHK_sala_capacidad	CHECK (capacidad > 0)
);

CREATE TABLE Asiento
(
  id_asiento INT NOT NULL,
  id_sala INT NOT NULL,
  letra_fila VARCHAR(2) NOT NULL,
  numero_columna INT NOT NULL,
  estado INT NOT NULL,	-- ++
  CONSTRAINT PK_asiento PRIMARY KEY (id_asiento, id_sala),
  CONSTRAINT FK_asiento_sala FOREIGN KEY (id_sala) REFERENCES Sala(id_sala),
  CONSTRAINT CHK_asiento_id_asiento	CHECK (id_asiento > 0)
);

CREATE TABLE Perfil
(
  id_perfil INT IDENTITY(1,1) NOT NULL,
  nombre VARCHAR(200) NOT NULL,
  CONSTRAINT PK_perfil PRIMARY KEY (id_perfil),
  CONSTRAINT UQ_perfil_nombre	UNIQUE (nombre)
);

CREATE TABLE TipoFuncion
(
  id_tipoFuncion INT NOT NULL,
  descripcion VARCHAR(200) NOT NULL,
  precio FLOAT NOT NULL,
  CONSTRAINT PK_tipoFuncion PRIMARY KEY (id_tipoFuncion),
  CONSTRAINT UQ_tipoFuncion_descripcion UNIQUE (descripcion),
  CONSTRAINT CHK_tipoFuncion_precio	CHECK (precio >= 0)
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
  id_director INT IDENTITY (1,1),
  nombre VARCHAR(200) NOT NULL,
  CONSTRAINT PK_director PRIMARY KEY (id_director),
	CONSTRAINT UQ_director_nombre	UNIQUE (nombre)
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
  dni INT NOT NULL,						-- ++
  nombre_completo VARCHAR(200) NOT NULL, -- ++
  password VARCHAR(200) NOT NULL,		
  estado INT NOT NULL,
  id_perfil INT NOT NULL,
  id_cine INT NOT NULL,
  CONSTRAINT PK_usuario PRIMARY KEY (id_usuario),
  CONSTRAINT FK_usuario_perfil FOREIGN KEY (id_perfil) REFERENCES Perfil(id_perfil),
  CONSTRAINT FK_usuario_cine FOREIGN KEY (id_cine) REFERENCES Cine(id_cine),
  CONSTRAINT UQ_usuario_nombre_completo	UNIQUE (nombre_completo),
  CONSTRAINT UQ_usuario_dni	UNIQUE (dni)
);

CREATE TABLE Pelicula
(
  id_pelicula INT IDENTITY (1,1),
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
	CONSTRAINT UQ_pelicula_nombre	UNIQUE (nombre),
  CONSTRAINT CHK_pelicula_duracion CHECK (duracion > 0)
);

CREATE TABLE Compra
(
  id_compra INT IDENTITY (1,1),
  subtotal FLOAT NOT NULL,
  fecha DATE NOT NULL,
  cantidad INT NOT NULL,
  id_usuario INT NOT NULL,
  CONSTRAINT PK_compra PRIMARY KEY (id_compra),
  CONSTRAINT FK_compra_usuario FOREIGN KEY (id_usuario) REFERENCES Usuario(id_usuario),
  CONSTRAINT CHK_compra_subtotal CHECK (subtotal >= 0),
  CONSTRAINT CHK_compra_cantidad CHECK (cantidad > 0)
	
);

CREATE TABLE Funcion --Detalle_Funcion
(
  id_funcion INT IDENTITY (1,1),
  id_pelicula INT NOT NULL,
  id_tipoFuncion INT NOT NULL,
  fecha_ingreso DATE NOT NULL,		-- ++
  fecha_final DATE NOT NULL,		-- ++
  CONSTRAINT PK_funcion PRIMARY KEY (id_funcion),
  CONSTRAINT FK_funcion_pelicula FOREIGN KEY (id_pelicula) REFERENCES Pelicula(id_pelicula),
  CONSTRAINT FK_funcion_tipoFuncion FOREIGN KEY (id_tipoFuncion) REFERENCES TipoFuncion(id_tipoFuncion),
);

select * from usuario

CREATE TABLE Sala_Funcion -- Funcion real
(
  id_funcion INT NOT NULL,
  id_sala INT NOT NULL,
  inicio_funcion DATETIME NOT NULL,			-- ++
  final_funcion DATETIME NOT NULL,			-- ++
  CONSTRAINT PK_sala_funcion PRIMARY KEY (id_funcion, id_sala, inicio_funcion),	-- ++
  CONSTRAINT FK_sala_funcion_funcion FOREIGN KEY (id_funcion) REFERENCES Funcion(id_funcion),
  CONSTRAINT FK_sala_funcion_sala FOREIGN KEY (id_sala) REFERENCES Sala(id_sala)
);

CREATE TABLE Ticket
(
  id_ticket INT IDENTITY (1,1),
  id_funcion INT NOT NULL,
  id_sala INT NOT NULL,
  inicio_funcion DATETIME NOT NULL,		-- ++
  id_asiento INT NOT NULL,
  id_compra INT NOT NULL,
  valor FLOAT NOT NULL,					-- ++
  CONSTRAINT PK_ticket PRIMARY KEY (id_ticket),
  CONSTRAINT FK_ticket_sala_funcion FOREIGN KEY (id_funcion, id_sala, inicio_funcion) REFERENCES Sala_Funcion(id_funcion, id_sala, inicio_funcion),
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

CREATE TABLE Horario (
	id_horario INT IDENTITY(1,1) NOT NULL,
	horario TIME NOT NULL,
	estado INT DEFAULT 1 NOT NULL,
	CONSTRAINT PK_horario PRIMARY KEY (id_horario),
	CONSTRAINT CHK_horario_estado CHECK (estado IN (1,0))
);

---------------------------------------------------------
--|---------------------------------------------------|--
--|------------------ LOTE DE DATOS ------------------|--
--|---------------------------------------------------|--
---------------------------------------------------------

select * from ticket;
select * from usuario;
INSERT INTO Cine (nombre, direccion) VALUES
('TicketSuit Cinema', 'Av. Libertad 1234');

INSERT INTO Perfil (nombre) VALUES 
('Administrador'),
('Vendedor'),
('Gerente');

select * from perfil

INSERT INTO Usuario (dni, nombre_completo, password, estado, id_perfil, id_cine) VALUES
(000000, 'administrador', 'administrador', 1, 1, 1),
(000001, 'vendedor', 'vendedor', 1, 2, 1),
(000002, 'gerente', 'gerente', 1, 3, 1);

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

INSERT INTO Director (nombre) VALUES
('John Lasseter'),
('Jon Watts'),
('Cristopher Nolan'),
('Todd Phillips'),
('Hayao Miyazaki'),
('Steven Spielberg'),
('Las Wachowski'),
('Lee Unkrich'),
('Christopher Nolan'),
('Ridley Scott');

INSERT INTO Pelicula (nombre, duracion, id_clasificacion, id_director, sinopsis, nacionalidad) VALUES
('El viaje de Chihiro', 125, 1, 5, 'Una ni�a que se aventura en un mundo m�gico mientras busca una forma de salvar a sus padres.', 'Jap�n'),
('Los Vengadores', 143, 2, 2, 'Un grupo de superh�roes se une para enfrentar una amenaza global.', 'EE.UU.'),
('Titanic', 195, 3, 3, 'Una historia de amor que surge en medio del desastre del Titanic.', 'EE.UU.'),
('El Padrino', 175, 4, 4, 'La saga de una familia mafiosa liderada por Vito Corleone.', 'EE.UU.'),
('Interestelar', 169, 2, 5, 'Un grupo de astronautas busca un nuevo hogar para la humanidad en el espacio.', 'EE.UU.'),
('Parasite', 132, 3, 6, 'Una familia pobre se infiltra en la vida de una familia rica, con consecuencias inesperadas.', 'Corea del Sur'),
('La La Land', 128, 2, 7, 'Dos artistas en Los �ngeles luchan por lograr sus sue�os mientras enfrentan desaf�os en su relaci�n.', 'EE.UU.'),
('Coco', 105, 1, 8, 'Un ni�o viaja al mundo de los muertos para descubrir la verdad sobre su familia.', 'M�xico'),
('El Origen', 148, 4, 9, 'Un ladr�n con la capacidad de entrar en los sue�os de las personas intenta realizar el trabajo definitivo.', 'EE.UU.'),
('Gladiador', 155, 4, 10, 'Un general romano se convierte en gladiador para vengar la muerte de su familia.', 'EE.UU.');


UPDATE Pelicula SET imagen = 'portada' + CAST(id_pelicula AS VARCHAR);


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
(2, '3D', 3500),
(3, 'IMAX', 7000);

INSERT INTO Horario (horario) VALUES
-- HH:Mm
('14:00'),
('14:30'),
('15:00'),
('15:30'),
('16:00'),
('16:30'),
('17:00'),
('17:30'),
('18:00'),
('18:30'),
('19:00'),
('19:30'),
('20:00'),
('20:30'),
('21:00'),
('21:30'),
('22:00'),
('22:30'),
('23:00'),
('23:30'),
('00:00'),
('00:30'),
('01:00');

-- CREACION DE SALA - FUNCIONES - TICKETS EN LA APP
/*

SELECT * FROM Asiento;

SELECT * FROM Funcion;
SELECT MONTH(C.fecha) AS mes, SUM(C.subtotal) AS total_ingresos
FROM Compra C
GROUP BY MONTH(C.fecha);

-- Calcular el rendimiento por mes en ingresos de todo el a;o
SELECT MONTH(fecha) AS mes, SUM(subtotal) AS total_ingresos
FROM Compra
WHERE fecha >= DATEADD(YEAR, -1, GETDATE()) -- �ltimo a�o
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
    c.fecha >= DATEADD(MONTH, -3, GETDATE()) -- Filtrar por las compras del �ltimo trimestre
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
