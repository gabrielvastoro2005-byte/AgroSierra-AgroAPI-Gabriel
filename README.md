# AgroAPI - AgroSierra

Autor: **Gabriel Vasquez Toro**  
Caso: Examen Parcial II, Unidad 2 - Versión A  
Framework: Spring Boot + Spring Security + JWT + Oracle Free + Flyway

## Arranque local

1. Copia `.env.example` como `.env` si quieres personalizar las credenciales de contenedor.
2. Desde este directorio ejecuta `docker compose -p agrosierra-gabriel up --build -d`.
3. Espera que Oracle termine la inicialización; luego el backend aplica Flyway y carga los datos de prueba.
4. API base: `http://localhost:8081/api/v1`.
5. Para detener los contenedores ejecuta `docker compose -p agrosierra-gabriel down`.

El backend usa Oracle Free en Docker, con usuario dedicado `AGRODB`; la aplicación no se conecta directamente a la base del servidor. Esta copia aislada publica la API en el puerto 8081 y Oracle en 1524 (para convivir con otra instancia local).

## Paquetes

- `config`: firma/verificación JWT, seguridad HTTP, formato de error y datos semilla.
- `seguridad`: usuario, rol, privilegios, filtro Bearer y carga de identidad.
- `dominio`: entidades `Lote`, `Labor` y `TipoLabor`.
- `repositorio`: acceso JPA a usuarios, lotes y labores.
- `servicio`: autenticación, paginación y reglas de negocio RN-01 a RN-04.
- `web`: controladores versionados y DTO de contrato.
- `src/main/resources/db/migration`: esquema y matriz de privilegios.

## Usuarios de demostración

| Usuario | Contraseña | Rol |
| --- | --- | --- |
| `tecnico1` | `Agro2026*` | TECNICO |
| `jefe1` | `Agro2026*` | JEFE_CAMPO |
| `admin` | `Agro2026*` | ADMINISTRADOR |

Solo son cuentas locales de evaluación. El JWT lleva identificador, rol, privilegios, emisión y expiración a 60 minutos. En cada petición se verifica firma/expiración y el usuario se vuelve a cargar desde base para aplicar privilegios vigentes.

## Contrato principal

- `POST /auth/login`: `{ "usuario": "tecnico1", "contrasena": "Agro2026*" }`.
- `GET /lotes?pagina=0&tamanio=20&valle=Chanchamayo&campaniaCerrada=false`.
- `GET /lotes/{id}` y `GET /lotes/{id}/labores?pagina=0&tamanio=20`.
- `POST /labores`, `PUT /labores/{id}`, `DELETE /labores/{id}`.
- `POST /lotes/{id}/cerrar-campania`.
- `GET /usuarios/me`.

Paginación: `{contenido,pagina,tamanio,totalElementos,totalPaginas,ultima}`. Errores: `{timestamp,status,error,message,path,validationErrors}`. La colección de pruebas está en `postman/AgroSierra_API.postman_collection.json`.

## Autorización y reglas

Las rutas requieren JWT salvo login. La seguridad evalúa privilegios (`LOTE_LEER`, `LABOR_CREAR`, etc.), no compara nombres de rol en controladores. El rol JEFE_CAMPO recibe además `LABOR_EDITAR_TODAS` para expresar alcance de edición sin un `if` de rol.

- RN-01: no crear/corregir labor con campaña cerrada -> 409.
- RN-02: técnico solo corrige su propio registro en las primeras 24 h -> 403 si incumple.
- RN-03: cierre exige cosecha en el lote -> 409 si no existe.
- RN-04: fumigación no excede dosis máxima × hectáreas -> 400 con error `cantidad`.

## Datos semilla

Se cargan 8 lotes de tres valles, 20 labores y tres usuarios. Dos lotes tienen campaña cerrada; también hay lotes abiertos con y sin Cosecha para demostrar ambos resultados de RN-03.

## Pruebas

Ejecuta `./mvnw test`. No se versionan contraseñas productivas ni secretos reales: `.env` está ignorado; el `.env.example` solo tiene valores de demostración.
