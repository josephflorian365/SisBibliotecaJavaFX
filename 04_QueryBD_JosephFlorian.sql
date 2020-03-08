CREATE DATABASE 04_QueryBD_JosephFlorian;

USE 04_QueryBD_JosephFlorian;


-- tables
-- Table: AUTOR
CREATE TABLE AUTOR (
    IDAUT int NOT NULL AUTO_INCREMENT COMMENT 'Contiene el identificador de cada autor',
    NOMAUT varchar(50) NULL COMMENT 'Contiene el nombre de cada autor',
    APEAUT varchar(50) NULL COMMENT 'Contiene el apellido de cada autor',
    IDPAIS int NOT NULL COMMENT 'Contiene el identificador de cada pais',
    CONSTRAINT AUTOR_pk PRIMARY KEY (IDAUT)
) COMMENT 'Contiene los datos del autor';

-- Table: DETALLE_PRESTAMO
CREATE TABLE DEVOLUCION (
    IDDEV int NOT NULL AUTO_INCREMENT COMMENT 'Contiene el identificador de cada detalle del préstamos',
    FECHPREDEV varchar(50) NOT NULL COMMENT 'Contiene la fecha en que el libro es prestado',
    FECHDEVDEV date NOT NULL COMMENT 'Contiene la fecha en que el libro es devuelto',
    LIBDEV varchar(50) NOT NULL COMMENT 'Contiene el titulo del libro que es devuelto',
    LECDEV varchar(50) NOT NULL COMMENT 'Contiene el nombre del lector que devuelve el libro',
    CONSTRAINT DEVOLUCION_pk PRIMARY KEY (IDDEV)
) COMMENT 'Contiene las dependencias totales';

-- Table: EDITORIAL
CREATE TABLE EDITORIAL (
    IDEDI int NOT NULL AUTO_INCREMENT COMMENT 'Contiene el identificador de cada editorial',
    NOMEDI varchar(200) NULL COMMENT 'Contiene el nombre de cada editorial',
    CONSTRAINT EDITORIAL_pk PRIMARY KEY (IDEDI)
) COMMENT 'Contiene datos de la editorial';

-- Table: LECTOR
CREATE TABLE LECTOR (
    IDLEC int NOT NULL AUTO_INCREMENT COMMENT 'Contiene el identificador de cada usuario',
    NOMLEC varchar(50) NULL COMMENT 'Contiene el nombre de cada lector',
    APELEC varchar(50) NULL COMMENT 'Contiene el apellido de cada lector',
    TELLEC varchar(9) NULL COMMENT 'Contiene el teléfono de cada lector',
    NOMUSULEC varchar(50) NULL COMMENT 'Contiene el nombre con el cual accederá al sistema',
    PASLEC varchar(8) NULL COMMENT 'Contiene la contraseña con el cual accederá al sistema',
	IDPERF int NOT NULL COMMENT 'Contiene el identificador de del perfíl',
    CONSTRAINT LECTOR_pk PRIMARY KEY (IDLEC)
) COMMENT 'Contiene los datos del lector';

-- Table: LIBRO

CREATE TABLE LIBRO (
    IDLIB int NOT NULL AUTO_INCREMENT COMMENT 'Contiene el identificador del libro',
    TITLIB varchar(50) NULL COMMENT 'Contiene el titulo del libro',
    IDEDI int NULL COMMENT 'Contiene el  identificador de cada editorial',
    IDAUT int NULL COMMENT 'Contiene el identificador del autor',
    CONSTRAINT LIBRO_pk PRIMARY KEY (IDLIB)
) COMMENT 'Contiene las propiedades del libro';

-- Table: PRESTAMO
CREATE TABLE PRESTAMO (
    IDPRE int NOT NULL AUTO_INCREMENT COMMENT 'Contiene el identificador de cada préstamo',
    FECHPRE date NOT NULL COMMENT 'Contiene la fecha actual del prestamo',
    IDLEC int NOT NULL COMMENT 'Identificador del lector',
    IDLIB int NOT NULL COMMENT 'Identificador del Libro',
    CONSTRAINT PRESTAMO_pk PRIMARY KEY (IDPRE)
) COMMENT 'Contiene el stock con la salida y entrega de cada libro';

-- Table: PERFIL
CREATE TABLE PERFIL(
	IDPERF int NOT NULL AUTO_INCREMENT COMMENT 'Contiene el identificador del perfíl',
    NOMPERF char(1) NOT NULL COMMENT 'Identifica el nombre del perfil',
    CONSTRAINT PERFIL_pk PRIMARY KEY (IDPERF)
)COMMENT 'Contiene las devoluciones de libros';

CREATE TABLE PAIS(
	IDPAIS int NOT NULL AUTO_INCREMENT COMMENT 'Contiene el identificador de cada pais',
    NOMPAIS varchar(50) NOT NULL COMMENT 'Contiene el nombre del pais',
    CONSTRAINT PAIS_pk PRIMARY KEY (IDPAIS)
)COMMENT 'Tabla para agregar paises';

-- Reference: LIBRO_AUTOR (table: LIBRO)
ALTER TABLE LIBRO ADD CONSTRAINT LIBRO_AUTOR FOREIGN KEY LIBRO_AUTOR (IDAUT)
    REFERENCES AUTOR (IDAUT);

-- Reference: LIBRO_EDITORIAL (table: LIBRO)
ALTER TABLE LIBRO ADD CONSTRAINT LIBRO_EDITORIAL FOREIGN KEY LIBRO_EDITORIAL (IDEDI)
    REFERENCES EDITORIAL (IDEDI);

-- Reference: PRESTAMO_LECTOR (table: PRESTAMO)
ALTER TABLE PRESTAMO ADD CONSTRAINT PRESTAMO_LECTOR FOREIGN KEY PRESTAMO_LECTOR (IDLEC)
    REFERENCES LECTOR (IDLEC);
    
ALTER TABLE PRESTAMO ADD CONSTRAINT PRESTAMO_LIBRO FOREIGN KEY PRESTAMO_LIBRO (IDLIB)
    REFERENCES LIBRO (IDLIB);
 
ALTER TABLE AUTOR ADD CONSTRAINT AUTOR_PAIS FOREIGN KEY AUTOR_PAIS (IDPAIS)
	REFERENCES PAIS(IDPAIS);
    
ALTER TABLE LECTOR ADD CONSTRAINT LECTOR_PERFIL FOREIGN KEY LECTOR_PERFIL(IDPERF)
REFERENCES PERFIL(IDPERF);
    
-- End of file.


/* Mostrar los foreign keys (show create table LIBRO)
*/
INSERT INTO PAIS(NOMPAIS)
VALUES
('España'),
('Colombia'),
('México'),
('Estados Unidos'),
('Venezuela'),
('Italia'),
('Francia'),
('Portugal'),
('Venezuela'),
('Gales'),
('Perú');

INSERT INTO AUTOR (NOMAUT,APEAUT,IDPAIS)
 VALUES
('Miguel de Cervantes','Saavedra','1'),
('William','Faulkner','2'),
('Antoine ','Saint-Exupery','3'),
('Nicolas','Maquiavelo','4'),
('Henry','Kissinger','5'),
('Kitty','Kelley','6'),
('Aisin','Gioro Puyi','7'),
('Pérez','Galdós','8'),
('Galvez','Gamarra','9'),
('Guillen','Cardenas','10');

INSERT INTO EDITORIAL(NOMEDI)
VALUES
('Anaya'),
('Alfaguara'),
('Andina'),
('S.M.'),
('Plaza&Janés'),
('Caralt'),
('Varcadel'),
('Vendieta'),
('Banderas'),
('Junior');

INSERT INTO PERFIL (NOMPERF)
VALUES
('Administrador'),
('Usuario');

INSERT INTO LECTOR(NOMLEC,APELEC,TELLEC,NOMUSULEC,PASLEC,IDPERF)
VALUES
('Jean','Florian','945-871-254','Messi','4578','2'),
('Yisus','Florian','945-871-253','Lector','5412','1'),
('JENNIFER','DAVIS','945-841-252','fenix','64612','2'),
('JOHNNY','LOLLOBRIGIDA','945-871-251','Gix','03166','2'),
('BETTE','PORTA','944-871-440','Six','11251512','1'),
('BATTO','NACO','943-871-250','fenix','151512','2'),
('BITTO','KISTO','942-871-510','fan','12121','1'),
('NIETTO','LIODO','941-871-200','Lucca','56156','2'),
('LUYANCO','MULAN','940-871-110','Fox','561651','1'),
('KIKKO','NICHOLSON','993-871-580','Fix','5454145','2');

INSERT INTO LIBRO(TITLIB,IDEDI,IDAUT)
VALUES 
('Don Quijote de La Mancha I','1','1'),
('Don Quijote de La Mancha II','1','1'),
('Historias de Nueva Orleans','2','2'),
('El principito','3','3'),
('El príncipe','4','4'),
('Diplomacia','4','5'),
('Los Windsor','5','6'),
('El Último Emperador','6','7'),
('Fortunata y Jacinta','6','8'),
('Nuevo Jacinta','5','9'),
('Fina Jey','5','10');

INSERT INTO PRESTAMO(FECHPRE,IDLEC,IDLIB)
VALUES
(STR_TO_DATE('12/1/2020', '%d/%m/%Y'),'1','1'),
(STR_TO_DATE('12/1/2020', '%d/%m/%Y'),'2','2'),
(STR_TO_DATE('12/1/2020', '%d/%m/%Y'),'3','3'),
(STR_TO_DATE('12/1/2020', '%d/%m/%Y'),'4','4'),
(STR_TO_DATE('12/1/2020', '%d/%m/%Y'),'5','5'),
(STR_TO_DATE('12/1/2020', '%d/%m/%Y'),'6','6'),
(STR_TO_DATE('12/1/2020', '%d/%m/%Y'),'7','7'),
(STR_TO_DATE('12/1/2020', '%d/%m/%Y'),'8','8'),
(STR_TO_DATE('12/1/2020', '%d/%m/%Y'),'9','9'),
(STR_TO_DATE('12/1/2020', '%d/%m/%Y'),'10','10');



/*DESHABILITAR MODO SEGURO
 (SET SQL_SAFE_UPDATES = 0;)
*/

/*RESTRICCION CONSTRAINT DESABILITADA*/
ALTER TABLE LIBRO DROP FOREIGN KEY LIBRO_AUTOR;
ALTER TABLE LIBRO DROP FOREIGN KEY LIBRO_EDITORIAL;
ALTER TABLE AUTOR DROP FOREIGN KEY AUTOR_PAIS;
