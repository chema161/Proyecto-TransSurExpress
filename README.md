# TransSur Express

TransSur Express es una aplicación web desarrollada con Spring Boot para gestionar un sistema de logística y reparto. La aplicación permite monitorizar la flota de vehículos, los envíos registrados y las operaciones de transporte, con trazabilidad completa del historial de estados y control de capacidad por vehículo.

## Funcionalidades

### Panel de control
- Dashboard con métricas clave: envíos, vehículos, conductores, operaciones, facturación acumulada y peso en tránsito.
- Distribución visual de operaciones por estado.
- Listado de las últimas 5 operaciones registradas.

### Gestión de envíos
- Listado, creación, edición y eliminación de envíos.
- Validación de código único y peso mínimo de 0,5 kg.
- Cálculo automático del coste al planificar: `peso × distancia × 0,02 €`.

### Gestión de vehículos
- Listado de la flota con matrícula, capacidad máxima y capacidad disponible en tiempo real.
- Alta, edición y baja lógica de vehículos.

### Gestión de conductores
- Listado de la plantilla con nombre, experiencia y vehículo asignado.
- Alta, edición y baja lógica de conductores.

### Planificación de operaciones
- Listado, creación, edición y eliminación de operaciones con validación de reglas de negocio.
- Reasignación del vehículo de una operación en curso.
- Trazabilidad del envío: historial cronológico de puntos de control.
- Historial de cambios de estado con fecha, hora y usuario responsable.

## Tecnologías y dependencias usadas

El proyecto está construido con Java 21 y Spring Boot. Las dependencias principales declaradas en `pom.xml` son:

- `spring-boot-starter-web`: desarrollo web MVC con controladores y rutas.
- `spring-boot-starter-thymeleaf`: motor de plantillas HTML.
- `spring-boot-starter-data-jpa`: persistencia con JPA/Hibernate.
- `spring-boot-starter-security`: autenticación, roles y protección de rutas.
- `spring-boot-starter-validation`: validación de formularios y entidades.
- `h2`: base de datos en memoria usada en desarrollo.
- `lombok`: reducción de código repetitivo en modelos, servicios y controladores.

En el frontend se usan:

- Thymeleaf para renderizar vistas del servidor.
- Bootstrap 5 para estilos y componentes.
- Bootstrap Icons para iconos.
- CSS propio en `src/main/resources/static/css`.

## Herramientas usadas

- Java 21
- Maven
- Spring Boot
- Spring Tools for Eclipse como entorno de trabajo del proyecto
- Git para control de versiones
- H2 Console para inspeccionar la base de datos durante el desarrollo
- Navegador web para probar la aplicación

Cuando la aplicación esté arrancada, se puede abrir en:

```
http://localhost:9000
```

La consola de H2 está disponible en:

```
http://localhost:9000/h2-console
```

Datos de conexión de H2:

- JDBC URL: `jdbc:h2:mem:transsurdb`
- Usuario: `sa`
- Contraseña: vacía

## Usuarios de prueba

La aplicación carga datos iniciales desde `src/main/resources/import.sql`.

| Usuario | Contraseña | Rol           |
|---------|------------|---------------|
| admin   | admin      | ADMINISTRADOR |
| user    | user       | OPERADOR      |

## Base de datos

La base de datos usada es H2 en memoria. En cada arranque se crea de nuevo el esquema y se cargan los datos iniciales. Esto facilita las pruebas, aunque los datos se pierden al detener la aplicación.

## Seguridad

Las rutas públicas son `/login` y los recursos estáticos. Las rutas de edición y eliminación requieren rol **ADMINISTRADOR**. El resto de rutas requieren un usuario autenticado.

