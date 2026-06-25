# Proyecto Barberia - Microservicios

Proyecto academico de gestion para una barberia, desarrollado con arquitectura de microservicios usando Spring Boot, Maven Wrapper, Hibernate/JPA y MySQL.

El sistema permite administrar usuarios, barberos, citas, servicios ofrecidos, pagos, notificaciones, resenas, inventario y reportes. Cada modulo esta separado en un microservicio independiente, con su propia base de datos y responsabilidades claras.

## Integrantes

- Sebastian Montupil
- Vicente Martinez

## Objetivo del proyecto

El objetivo es modelar una barberia como un sistema distribuido, donde cada area del negocio se resuelve en un servicio separado. Esto permite demostrar:

- Separacion de responsabilidades.
- Uso del patron Controller-Service-Repository.
- Persistencia con JPA/Hibernate.
- Bases de datos independientes por microservicio.
- Comunicacion REST entre microservicios.
- Validaciones, manejo de errores y reglas de negocio.
- Pruebas manuales mediante Postman.

## Arquitectura general

Cada microservicio es una aplicacion Spring Boot independiente. La estructura comun es:

```text
microservicio/
  src/main/java/com/barberia/{microservicio}/
    controller/     Endpoints REST
    service/        Logica de negocio
    repository/     Acceso a datos con JpaRepository
    model/          Entidades JPA
    dto/            Objetos de entrada/salida
    exception/      Manejo centralizado de errores
    config/         Configuraciones como RestTemplate
  src/main/resources/
    application.properties
  pom.xml
  mvnw.cmd
```

El patron usado es Controller-Service-Repository:

- Controller: recibe las peticiones HTTP y expone los endpoints.
- Service: contiene reglas de negocio, validaciones de flujo y comunicacion con otros servicios.
- Repository: abstrae el acceso a la base de datos usando Spring Data JPA.
- Model: representa las tablas mediante entidades JPA.
- DTO: evita exponer directamente todos los datos internos y permite armar respuestas enriquecidas.

## Microservicios

| Servicio | Puerto | Base URL | Base de datos | Responsabilidad |
| --- | ---: | --- | --- | --- |
| auth | 8081 | `http://localhost:8081/api/usuarios` | `auth_barberia` | Usuarios, roles, login y recuperacion de password |
| barberos | 8082 | `http://localhost:8082/api/barberos` | `barberos_barberia` | Perfil profesional de barberos |
| agendas | 8083 | `http://localhost:8083/api/agendas` | `agendas_barberia` | Reservas, cancelaciones y reprogramaciones |
| servicios | 8084 | `http://localhost:8084/api/servicios` | `servicios_barberia` | Catalogo de servicios de barberia |
| pagos | 8085 | `http://localhost:8085/api/pagos` | `pagos_barberia` | Registro y estado de pagos |
| notificaciones | 8086 | `http://localhost:8086/api/notificaciones` | `notificaciones_barberia` | Notificaciones asociadas a usuarios y citas |
| resenas | 8087 | `http://localhost:8087/api/resenas` | `resenas_barberia` | Calificaciones y comentarios |
| inventario | 8088 | `http://localhost:8088/api/productos` | `inventario_barberia` | Productos, stock y bajo stock |
| reportes | 8089 | `http://localhost:8089/api/reportes` | `reportes_barberia` | Reportes agregados desde otros servicios |

## Flujo principal del negocio

Un flujo completo de uso del sistema puede ser:

1. Se registra un cliente en `auth`.
2. Se registra un usuario con rol `BARBERO` en `auth`.
3. Se crea el perfil del barbero en `barberos`, asociandolo al usuario por `usuarioId`.
4. Se registra un servicio ofrecido en `servicios`, por ejemplo corte de pelo o perfilado de barba.
5. El cliente agenda una cita en `agendas`, indicando `clienteId`, `barberoId`, fecha y hora.
6. El sistema puede registrar una notificacion en `notificaciones` para confirmar o recordar la cita.
7. Se registra el pago de la cita en `pagos`, asociado al cliente y a la agenda.
8. El cliente puede dejar una resena en `resenas`, asociada al cliente y al barbero.
9. `reportes` consulta otros microservicios para generar reportes de ingresos, citas y desempeno.

## Comunicacion entre microservicios

La comunicacion entre microservicios se realiza mediante HTTP usando `RestTemplate`.

Ejemplos:

- `barberos` consulta `auth` para obtener datos del usuario asociado a un barbero.
- `agendas` consulta `auth` y `barberos` para enriquecer la respuesta de una cita.
- `pagos` consulta `auth` y `agendas` para mostrar datos del cliente y de la cita pagada.
- `notificaciones` consulta `auth` y `agendas` para mostrar informacion de usuario y reserva.
- `resenas` consulta `auth` y `barberos` para mostrar nombres y especialidades.
- `reportes` consulta `pagos`, `agendas` y `resenas` para calcular indicadores.

Las llamadas externas tienen manejo de errores en los servicios donde corresponde, para que una caida parcial no rompa todo el flujo.

## Bases de datos

Cada microservicio usa una base de datos independiente. Las URLs incluyen `createDatabaseIfNotExist=true`, por lo que MySQL crea la base automaticamente al iniciar el servicio si el usuario configurado tiene permisos.

Hibernate/JPA crea o actualiza las tablas de cada microservicio a partir de sus entidades Java gracias a:

```properties
spring.jpa.hibernate.ddl-auto=update
```

Bases usadas por el proyecto:

```sql
CREATE DATABASE auth_barberia;
CREATE DATABASE barberos_barberia;
CREATE DATABASE agendas_barberia;
CREATE DATABASE servicios_barberia;
CREATE DATABASE pagos_barberia;
CREATE DATABASE notificaciones_barberia;
CREATE DATABASE resenas_barberia;
CREATE DATABASE inventario_barberia;
CREATE DATABASE reportes_barberia;
```

No es obligatorio ejecutar esos `CREATE DATABASE` manualmente si MySQL permite crearlas desde la conexion. El usuario configurado es `root` sin password. Si tu MySQL usa otra credencial, ajusta el archivo `src/main/resources/application.properties` del microservicio correspondiente.

## Modelado y entidades JPA

Cada microservicio con persistencia tiene entidades JPA anotadas con `@Entity` y repositorios que extienden `JpaRepository`.

Entidades principales:

- `Usuario`: datos de usuarios, credenciales y rol.
- `Barbero`: perfil del profesional, especialidad, horario, experiencia y disponibilidad.
- `Agenda`: cita entre cliente y barbero, con fecha, hora y estado.
- `Servicio`: servicio ofrecido por la barberia, precio, duracion y disponibilidad.
- `Pago`: monto, metodo, estado y fecha de pago.
- `Notificacion`: mensaje, tipo, estado y fecha de envio.
- `Resena`: calificacion, comentario y fecha.
- `Producto`: producto de inventario, categoria, stock, stock minimo y precio.

## Relaciones JPA

Los microservicios usan relaciones `@ManyToOne` donde el dominio necesita referenciar datos relacionados.

Como cada microservicio mantiene su propia base de datos, las relaciones cruzadas se modelan mediante entidades locales de referencia y joins opcionales sin foreign key fisica entre bases. Esto permite evidenciar el modelado JPA sin romper la independencia de los microservicios.

Ejemplos implementados:

- `Barbero` referencia a `UsuarioReferencia`.
- `Agenda` referencia a `UsuarioReferencia` y `BarberoReferencia`.
- `Pago` referencia a `UsuarioReferencia` y `AgendaReferencia`.
- `Notificacion` referencia a `UsuarioReferencia` y `AgendaReferencia`.
- `Resena` referencia a `UsuarioReferencia` y `BarberoReferencia`.

Los campos `clienteId`, `usuarioId`, `barberoId` y `agendaId` se mantienen porque son necesarios para la comunicacion REST entre microservicios y evitan acoplar fisicamente bases de datos distintas.

La relacion se define con:

```java
@ManyToOne(fetch = FetchType.LAZY, optional = true)
@JoinColumn(
    name = "cliente_id",
    referencedColumnName = "id",
    insertable = false,
    updatable = false,
    foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT)
)
private UsuarioReferencia cliente;
```

Esta decision es importante para la defensa tecnica: en microservicios no se recomienda crear claves foraneas entre bases de datos de servicios diferentes. Por eso se usan referencias por ID, comunicacion REST y entidades locales de referencia.

## Reglas de negocio implementadas

- `auth`: no permite registrar emails duplicados y usa BCrypt para guardar passwords.
- `auth`: valida login comparando la password enviada con la password encriptada.
- `agendas`: evita reservar un barbero en la misma fecha y hora si la cita no esta cancelada.
- `agendas`: permite cancelar y reprogramar citas.
- `pagos`: asigna estado y fecha por defecto cuando no vienen en el request.
- `notificaciones`: maneja estados como `PENDIENTE`, `ENVIADA` y `ERROR`.
- `resenas`: valida que la calificacion este entre 1 y 5.
- `inventario`: controla stock, bajo stock y disponibilidad cuando el stock llega a cero.
- `reportes`: calcula ingresos, cantidad de citas, promedio de calificaciones y desempeno por barbero.

## Validaciones

El proyecto usa validaciones con Jakarta Validation:

- `@Valid`
- `@NotNull`
- `@NotBlank`
- `@Email`
- `@Min`
- `@Max`

Estas validaciones se aplican principalmente en DTOs de entrada y en modelos donde corresponde.

Ejemplos:

- Usuario requiere nombre, email, password, telefono y rol.
- Agenda requiere cliente, barbero, fecha y hora.
- Pago requiere cliente, agenda, monto y metodo de pago.
- Resena requiere cliente, barbero y calificacion entre 1 y 5.
- Producto requiere nombre, categoria, stock, stock minimo, precio y disponibilidad.

## Manejo de excepciones y codigos HTTP

Los microservicios tienen manejo centralizado de errores con `@RestControllerAdvice`.

Comportamientos principales:

- Errores de validacion devuelven `400 Bad Request`.
- Errores de negocio devuelven `400 Bad Request` con mensaje claro.
- Busquedas por ID inexistente devuelven `404 Not Found`.
- Endpoints exitosos devuelven `200 OK`.
- Creaciones y modificaciones devuelven el recurso resultante.

Esto mejora la prueba desde Postman porque cada error entrega una respuesta consistente.

## Requisitos

- Java 21 o superior.
- MySQL en `localhost:3306`.
- Windows para usar `levantar-microservicios.bat`.
- Maven no es obligatorio porque cada microservicio incluye `mvnw.cmd`.

## Levantar todo el proyecto

Desde la raiz del repositorio ejecuta:

```bat
levantar-microservicios.bat
```

El script abre una consola por microservicio y ejecuta:

```bat
mvnw.cmd spring-boot:run
```

Para validar los comandos sin abrir ventanas:

```bat
levantar-microservicios.bat --dry-run
```

Si los servicios ya estaban levantados antes de un cambio de codigo, detenlos con `Ctrl + C` y vuelve a ejecutar el script.

## Levantar un microservicio individual

Ejemplo con `auth`:

```bat
cd auth
mvnw.cmd spring-boot:run
```

Repite el mismo flujo cambiando a la carpeta del microservicio que quieras ejecutar.

## Probar con Postman

El proyecto incluye una coleccion Postman lista para importar:

```text
postman/barberia-microservicios.postman_collection.json
```

Recomendacion de uso:

1. Levantar los 9 microservicios.
2. Importar la coleccion en Postman.
3. Ejecutar primero la carpeta `00 - Flujo base`.
4. Probar luego los endpoints por microservicio.

La carpeta `00 - Flujo base` crea datos iniciales y guarda IDs en variables de coleccion, para facilitar las pruebas posteriores.

## Rutas principales

### auth

- `GET /api/usuarios`
- `GET /api/usuarios/{id}`
- `GET /api/usuarios/email/{email}`
- `GET /api/usuarios/rol/{rol}`
- `POST /api/usuarios`
- `POST /api/usuarios/login`
- `PUT /api/usuarios/recuperar-password`
- `DELETE /api/usuarios/{id}`

### barberos

- `GET /api/barberos`
- `GET /api/barberos/{id}`
- `GET /api/barberos/usuario/{usuarioId}`
- `GET /api/barberos/disponible/{disponible}`
- `GET /api/barberos/especialidad/{especialidad}`
- `POST /api/barberos`
- `PUT /api/barberos/{id}`
- `DELETE /api/barberos/{id}`

### agendas

- `GET /api/agendas`
- `GET /api/agendas/{id}`
- `GET /api/agendas/cliente/{clienteId}`
- `GET /api/agendas/barbero/{barberoId}`
- `GET /api/agendas/estado/{estado}`
- `POST /api/agendas`
- `PUT /api/agendas/cancelar/{id}`
- `PUT /api/agendas/reprogramar/{id}`
- `DELETE /api/agendas/{id}`

### servicios

- `GET /api/servicios`
- `GET /api/servicios/{id}`
- `GET /api/servicios/disponible/{disponible}`
- `GET /api/servicios/nombre/{nombre}`
- `POST /api/servicios`
- `PUT /api/servicios/{id}`
- `DELETE /api/servicios/{id}`

### pagos

- `GET /api/pagos`
- `GET /api/pagos/{id}`
- `GET /api/pagos/cliente/{clienteId}`
- `GET /api/pagos/agenda/{agendaId}`
- `GET /api/pagos/estado/{estadoPago}`
- `GET /api/pagos/metodo/{metodoPago}`
- `POST /api/pagos`
- `PUT /api/pagos/estado/{id}/{estadoPago}`
- `DELETE /api/pagos/{id}`

### notificaciones

- `GET /api/notificaciones`
- `GET /api/notificaciones/{id}`
- `GET /api/notificaciones/usuario/{usuarioId}`
- `GET /api/notificaciones/agenda/{agendaId}`
- `GET /api/notificaciones/tipo/{tipo}`
- `GET /api/notificaciones/estado/{estado}`
- `POST /api/notificaciones`
- `PUT /api/notificaciones/enviada/{id}`
- `PUT /api/notificaciones/error/{id}`
- `DELETE /api/notificaciones/{id}`

### resenas

- `GET /api/resenas`
- `GET /api/resenas/{id}`
- `GET /api/resenas/cliente/{clienteId}`
- `GET /api/resenas/barbero/{barberoId}`
- `GET /api/resenas/calificacion/{calificacion}`
- `POST /api/resenas`
- `PUT /api/resenas/{id}`
- `DELETE /api/resenas/{id}`

### inventario

- `GET /api/productos`
- `GET /api/productos/{id}`
- `GET /api/productos/categoria/{categoria}`
- `GET /api/productos/disponible/{disponible}`
- `GET /api/productos/nombre/{nombre}`
- `GET /api/productos/bajo-stock`
- `POST /api/productos`
- `PUT /api/productos/{id}`
- `PUT /api/productos/aumentar/{id}/{cantidad}`
- `PUT /api/productos/disminuir/{id}/{cantidad}`
- `DELETE /api/productos/{id}`

### reportes

- `GET /api/reportes/ingresos`
- `GET /api/reportes/citas`
- `GET /api/reportes/citas/barbero/{barberoId}`
- `GET /api/reportes/calificacion/barbero/{barberoId}`
- `GET /api/reportes/desempeno/barbero/{barberoId}`

## Probar y compilar

Para ejecutar las pruebas de un microservicio:

```bat
cd auth
mvnw.cmd test
```

Para compilarlo:

```bat
cd auth
mvnw.cmd clean package
```

Para probar todos los microservicios, se puede ejecutar `mvnw.cmd test` dentro de cada carpeta.

## Evidencia de trabajo progresivo

El repositorio mantiene historial Git con commits incrementales por microservicio, documentacion, validaciones y manejo de errores.

Tambien existe evidencia colaborativa porque el historial registra participacion de ambos integrantes.

## Puntos para defensa tecnica

Durante la defensa, es recomendable explicar:

- Por que cada microservicio tiene su propia base de datos.
- Por que se usan IDs externos y comunicacion REST entre servicios.
- Por que las relaciones JPA se modelan localmente con entidades de referencia y sin foreign keys fisicas entre bases.
- Como funciona el flujo completo cliente-barbero-agenda-pago-notificacion-resena-reporte.
- Como se validan los requests con `@Valid`.
- Como se manejan errores con `@RestControllerAdvice`.
- Como se prueba el sistema usando la coleccion Postman.

La idea central del proyecto es que cada servicio pueda evolucionar de forma independiente, pero que todos trabajen juntos mediante endpoints REST.

## Evaluación Parcial 3: infraestructura

### Puertos y componentes

| Componente | Puerto | Función |
| --- | ---: | --- |
| Eureka Server | 8761 | Registro y descubrimiento de servicios. |
| API Gateway | 8080 | Punto de entrada único. |
| auth, barberos, agendas, servicios | 8081–8084 | Gestión de usuarios, barberos, reservas y catálogo. |
| pagos, notificaciones, resenas | 8085–8087 | Procesos asociados a una reserva. |
| inventario, reportes | 8088–8089 | Stock e indicadores. |

Cada módulo ahora usa `application.yml` y las variables `DB_HOST`, `DB_PORT`, `DB_USERNAME`, `DB_PASSWORD` y `EUREKA_URL`. Consulta `.env.example`; no subas un archivo `.env` real.

Configuración local probada:

```text
DB_HOST=localhost
DB_PORT=3306
DB_USERNAME=root
DB_PASSWORD=
EUREKA_URL=http://localhost:8761/eureka/
```

### Orden de ejecución

1. Inicia MySQL.
2. Inicia `eureka-server` y abre http://localhost:8761.
3. Inicia los nueve microservicios y espera que aparezcan como `UP` en Eureka.
4. Inicia `api-gateway` en http://localhost:8080.

Desde el IDE ejecuta la clase `*Application` de cada módulo. Desde consola:

```bat
cd api-gateway
mvn spring-boot:run
```

`levantar-microservicios.bat` inicia los componentes en este orden. Si existe Maven Wrapper lo usa; de lo contrario usa `mvn` instalado en el sistema.

### Rutas públicas del Gateway

| Ruta del Gateway | Destino interno |
| --- | --- |
| `/api/auth/usuarios` | `auth` → `/api/usuarios` |
| `/api/barberos/**` | `barberos` → misma ruta |
| `/api/agendas/**` | `agendas` → misma ruta |
| `/api/servicios/**` | `servicios` → misma ruta |
| `/api/pagos/**` | `pagos` → misma ruta |
| `/api/notificaciones/**` | `notificaciones` → misma ruta |
| `/api/resenas/**` | `resenas` → misma ruta |
| `/api/inventario/**` | `inventario` → `/api/productos/**` |
| `/api/reportes/**` | `reportes` → misma ruta |

El Gateway usa `lb://nombre-servicio`, por lo que obtiene las instancias desde Eureka. Solo `auth` e `inventario` usan `RewritePath`, porque sus rutas internas tienen otro nombre.

### URLs, pruebas y solución de errores

- Eureka: http://localhost:8761
- Gateway health: http://localhost:8080/actuator/health
- Swagger: `http://localhost:PUERTO/swagger-ui/index.html`

Para cada módulo:

```bat
mvn test
mvn clean package
```

- **Access denied de MySQL:** revisa `DB_USERNAME` y `DB_PASSWORD`; la instalación validada usa contraseña vacía para `root`.
- **Eureka no disponible:** inicia `eureka-server` primero y verifica `EUREKA_URL`.
- **Gateway 503:** espera que la instancia se registre como `UP` en Eureka.
- **Puerto ocupado:** detén el proceso que usa el puerto o ajusta `server.port`.

Para la defensa, muestra Eureka con los servicios registrados, Swagger de un microservicio, una solicitud directa al Gateway y el resultado de `mvn test`.
