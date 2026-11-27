# Catálogo de constelaciones — API REST

Aplicación de práctica con Spring Boot, Java 25 y PostgreSQL. Guarda constelaciones en una tabla con `id`, `nombre`, `hemisferio` y `estrellaPrincipal`. Permite nombres repetidos.

## Ejecutar con Docker Compose

Instala Docker con Compose y, desde esta carpeta, ejecuta:

```bash
docker compose up --build -d
```

La API queda disponible en `http://localhost:8081/api/constelaciones`. PostgreSQL no publica su puerto al exterior, y `compose.override.yaml` (solo local) es el que publica el puerto de la API. Los datos persisten en un volumen, incluso al detener los contenedores con `docker compose down`. En el primer inicio se crean la tabla y dos ejemplos. Si se eliminan, no reaparecen al reiniciar.

Para cambiar puerto o credenciales, crea un archivo `.env` en esta carpeta antes del primer inicio:

```dotenv
APP_PORT=8081
POSTGRES_USER=postgres
POSTGRES_PASSWORD=elige-una-clave
```

El usuario y la contraseña solo inicializan una base nueva; cambiarlos en `.env` no modifica las credenciales guardadas en un volumen existente. Para eliminar todos los datos y reiniciar desde cero, usa `docker compose down -v` y después vuelve a iniciar.

## Operaciones

| Método | Ruta | Acción |
|---|---|---|
| GET | `/api/constelaciones` | Listar |
| GET | `/api/constelaciones/{id}` | Consultar por ID |
| POST | `/api/constelaciones` | Crear |
| PUT | `/api/constelaciones/{id}` | Reemplazar los tres campos |
| DELETE | `/api/constelaciones/{id}` | Eliminar |

Ejemplo de cuerpo JSON para POST y PUT:

```json
{
  "nombre": "Escorpio",
  "hemisferio": "Sur",
  "estrellaPrincipal": "Antares",
  "descubiertoPor": "Ptolomeo"
}
```

`nombre`, `hemisferio` y `estrellaPrincipal` son obligatorios; `descubiertoPor` es opcional. Los campos de texto admiten hasta 100 caracteres. El hemisferio debe ser exactamente `Norte`, `Sur` o `Ambos`. Se responde con 201 al crear, 204 al eliminar, 400 si los datos son inválidos y 404 si no existe el ID.

Ejemplos con curl:

```bash
curl http://localhost:8081/api/constelaciones
curl -X POST http://localhost:8081/api/constelaciones -H "Content-Type: application/json" -d '{"nombre":"Escorpio","hemisferio":"Sur","estrellaPrincipal":"Antares"}'
curl -X PUT http://localhost:8081/api/constelaciones/1 -H "Content-Type: application/json" -d '{"nombre":"Orión","hemisferio":"Ambos","estrellaPrincipal":"Betelgeuse"}'
curl -X DELETE http://localhost:8081/api/constelaciones/1
```

En PowerShell utiliza `curl.exe` o un cliente como Postman para evitar el alias `curl` de algunas versiones.

## Ejecución local sin Docker

Con Java 25 y PostgreSQL disponibles, crea una base vacía llamada `constelaciones` y configura `DATABASE_URL` (JDBC), `DATABASE_USER` y `DATABASE_PASSWORD` si son distintos de `jdbc:postgresql://localhost:5432/constelaciones`, `postgres` y `postgres`. Luego ejecuta `./gradlew bootRun` (Windows: `gradlew.bat bootRun`). Spring crea la tabla automáticamente; los ejemplos de `docker/init.sql` se cargan automáticamente solo con Compose y un volumen nuevo.

## Despliegue en Dokploy

Crea un servicio de tipo *Compose* con este repositorio (rama `main`) y ruta de compose `./actividad_individual/compose.yaml`; define `POSTGRES_PASSWORD` en las variables de entorno y asigna un dominio al servicio `app` (puerto 8081) para obtener HTTPS. `compose.override.yaml` no se usa allí, por lo que la API solo es accesible mediante el dominio.

Para el uso desde Apps Script, usa la URL HTTPS del dominio. Este proyecto es una práctica sin autenticación; antes de exponerlo considera controlar quién puede modificar los datos.
