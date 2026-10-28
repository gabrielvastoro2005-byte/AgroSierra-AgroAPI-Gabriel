INSERT INTO rol(codigo, descripcion) VALUES ('TECNICO', 'Técnico de campo');
INSERT INTO rol(codigo, descripcion) VALUES ('JEFE_CAMPO', 'Jefe de campo');
INSERT INTO rol(codigo, descripcion) VALUES ('ADMINISTRADOR', 'Administrador');

INSERT INTO privilegio(codigo, descripcion) VALUES ('LOTE_LEER', 'Consultar padrón y detalle de lotes');
INSERT INTO privilegio(codigo, descripcion) VALUES ('LABOR_LEER', 'Consultar labores');
INSERT INTO privilegio(codigo, descripcion) VALUES ('LABOR_CREAR', 'Registrar labores');
INSERT INTO privilegio(codigo, descripcion) VALUES ('LABOR_EDITAR', 'Corregir labores propias según RN-02');
INSERT INTO privilegio(codigo, descripcion) VALUES ('LABOR_EDITAR_TODAS', 'Corregir cualquier labor como jefe de campo');
INSERT INTO privilegio(codigo, descripcion) VALUES ('LABOR_ELIMINAR', 'Eliminar labores');
INSERT INTO privilegio(codigo, descripcion) VALUES ('CAMPANIA_CERRAR', 'Cerrar campañas de lote');
INSERT INTO privilegio(codigo, descripcion) VALUES ('USUARIO_GESTIONAR', 'Gestionar usuarios y roles');

INSERT INTO rol_privilegio VALUES ('TECNICO', 'LOTE_LEER');
INSERT INTO rol_privilegio VALUES ('TECNICO', 'LABOR_LEER');
INSERT INTO rol_privilegio VALUES ('TECNICO', 'LABOR_CREAR');
INSERT INTO rol_privilegio VALUES ('TECNICO', 'LABOR_EDITAR');
INSERT INTO rol_privilegio VALUES ('JEFE_CAMPO', 'LOTE_LEER');
INSERT INTO rol_privilegio VALUES ('JEFE_CAMPO', 'LABOR_LEER');
INSERT INTO rol_privilegio VALUES ('JEFE_CAMPO', 'LABOR_CREAR');
INSERT INTO rol_privilegio VALUES ('JEFE_CAMPO', 'LABOR_EDITAR');
INSERT INTO rol_privilegio VALUES ('JEFE_CAMPO', 'LABOR_EDITAR_TODAS');
INSERT INTO rol_privilegio VALUES ('JEFE_CAMPO', 'LABOR_ELIMINAR');
INSERT INTO rol_privilegio VALUES ('JEFE_CAMPO', 'CAMPANIA_CERRAR');
INSERT INTO rol_privilegio VALUES ('ADMINISTRADOR', 'LOTE_LEER');
INSERT INTO rol_privilegio VALUES ('ADMINISTRADOR', 'LABOR_LEER');
INSERT INTO rol_privilegio VALUES ('ADMINISTRADOR', 'USUARIO_GESTIONAR');
