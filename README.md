# 🚛 TransSur Express

**TransSur Express** es una aplicación web desarrollada con **Spring Boot** para gestionar un sistema de logística y reparto. Permite monitorizar la flota de vehículos, los envíos registrados y las operaciones de transporte, con trazabilidad completa del historial de estados y control de capacidad por vehículo en tiempo real.


## ✨ Funcionalidades

### 📊 Panel de control
- Dashboard con métricas clave: total de envíos, vehículos activos, conductores en plantilla, operaciones registradas, facturación acumulada y kg en tránsito.
- Distribución visual de operaciones por estado (barras de progreso por PREPARADO / EN_RUTA / EN_REPARTO / ENTREGADO).
- Listado de las últimas 5 operaciones registradas con acceso directo.

### 📦 Gestión de envíos
- Listado, creación, edición y eliminación de envíos.
- Validación de código único y peso mínimo de **0,5 kg**.
- Cálculo automático del coste al planificar una operación: `peso × distancia × 0,02 €`.

### 🚚 Gestión de vehículos
- Listado de la flota con matrícula, capacidad máxima y capacidad disponible en tiempo real.
- Alta, edición y **baja lógica** de vehículos (no se eliminan físicamente de la base de datos).
- Validación del formato de matrícula: `1234-ABC`.

### 👤 Gestión de conductores
- Listado de la plantilla con nombre, años de experiencia y vehículo asignado.
- Alta, edición y **baja lógica** de conductores.
- Posibilidad de asignar un vehículo a cada conductor de forma opcional.

### 🗺️ Planificación de operaciones
- Listado, creación, edición y eliminación de operaciones con validación de reglas de negocio:
  - Peso mínimo del envío.
  - Peso máximo según capacidad disponible del vehículo.
  - Sin tramos duplicados (mismo vehículo + fecha + envío).
  - Exclusividad del envío (un envío activo no puede estar en dos vehículos a la vez).
  - Conductor no puede conducir dos vehículos el mismo día.
- **Reasignación de vehículo** de una operación en curso con validaciones completas.
- **Trazabilidad del envío**: historial cronológico de puntos de control con fecha, vehículo y estado.
- **Historial de cambios de estado**: registro de cada transición con fecha, hora y usuario responsable.
- Cálculo automático de la fecha estimada de entrega según distancia (300 km/día estimados).


## 🛠️ Tecnologías y dependencias

El proyecto está construido con **Java 21** y **Spring Boot**. Las dependencias principales declaradas en `pom.xml` son:

| Dependencia | Uso |
|---|---|
| `spring-boot-starter-web` | Desarrollo web MVC con controladores y rutas |
| `spring-boot-starter-thymeleaf` | Motor de plantillas HTML |
| `spring-boot-starter-data-jpa` | Persistencia con JPA/Hibernate |
| `spring-boot-starter-security` | Autenticación, roles y protección de rutas |
| `spring-boot-starter-validation` | Validación de formularios y entidades |
| `h2` | Base de datos en memoria usada en desarrollo |
| `lombok` | Reducción de código repetitivo en modelos, servicios y controladores |

**Frontend:**
- Thymeleaf para renderizar vistas desde el servidor.
- Bootstrap 5 para estilos y componentes.
- Bootstrap Icons para iconografía.
- CSS propio en `src/main/resources/static/css`.

## 🧰 Herramientas usadas

- Java 21
- Maven
- Spring Boot
- Spring Tools for Eclipse (entorno de desarrollo)
- Git para control de versiones
- H2 Console para inspeccionar la base de datos durante el desarrollo
- Navegador web para probar la aplicación


## 🚀 Puesta en marcha

### Requisitos previos
- Java 21 instalado
- Maven instalado (o usar el wrapper `./mvnw`)

### Pasos

1. Clona el repositorio:
   ```bash
   git clone <url-del-repositorio>
   cd proyectotranssurexpressjosemanueldiaz
   ```

2. Compila y arranca la aplicación:
   ```bash
   ./mvnw spring-boot:run
   ```
   O con Maven instalado globalmente:
   ```bash
   mvn spring-boot:run
   ```

3. Abre el navegador en:
   ```
   http://localhost:9000
   ```

## 🗄️ Base de datos

Se usa **H2 en memoria**. En cada arranque se crea de nuevo el esquema y se cargan los datos iniciales desde `src/main/resources/import.sql`. Esto facilita las pruebas, aunque los datos se pierden al detener la aplicación.

La **consola H2** está disponible en:
```
http://localhost:9000/h2-console
```

Datos de conexión:
- **JDBC URL:** `jdbc:h2:mem:transsurdb`
- **Usuario:** `sa`
- **Contraseña:** *(vacía)*

## 👥 Usuarios de prueba

La aplicación carga los siguientes usuarios al arrancar:

| Usuario | Contraseña | Rol |
|---|---|---|
| `admin` | `admin` | ADMINISTRADOR |
| `user` | `user` | OPERADOR |


## 🔐 Seguridad y roles

Las rutas públicas son `/login` y los recursos estáticos. El resto requiere autenticación.

| Acción | ADMINISTRADOR | OPERADOR |
|---|:---:|:---:|
| Ver listas y dashboards | ✅ | ✅ |
| Crear envíos, vehículos, conductores y operaciones | ✅ | ✅ |
| Editar y eliminar cualquier entidad | ✅ | ❌ |
| Reasignar vehículo de una operación | ✅ | ❌ |
| Ver conductores e vehículos inactivos (baja lógica) | ✅ | ❌ |


## 📁 Estructura del proyecto

```
src/
├── main/
│   ├── java/
│   │   └── .../proyectotranssurexpressjosemanueldiaz/
│   │       ├── config/          # WebConfig (conversores de formulario)
│   │       ├── controllers/     # Controladores MVC
│   │       ├── exceptions/      # Excepciones de negocio personalizadas
│   │       ├── modelos/         # Entidades JPA y enumerados
│   │       ├── repository/      # Repositorios Spring Data JPA
│   │       ├── security/        # Configuración de Spring Security
│   │       └── services/        # Servicios de negocio
│   └── resources/
│       ├── static/css/          # Hojas de estilo propias
│       ├── templates/           # Vistas Thymeleaf
│       ├── application.properties
│       └── import.sql           # Datos iniciales
```


## 📐 Decisiones de diseño destacadas

- **Baja lógica:** Vehículos y conductores no se eliminan físicamente. Se marcan como `activo = false` para conservar la integridad referencial con operaciones históricas.
- **Historial de estados:** Cada cambio de estado en una operación queda registrado en `HistorialEstado` con el usuario y timestamp, permitiendo auditoría completa.
- **Cálculo de coste automático:** Al guardar una operación, el coste del envío se recalcula siempre como `peso × distancia × 0,02 €`, garantizando coherencia.
- **Control de capacidad en tiempo real:** La capacidad disponible de cada vehículo se calcula dinámicamente sumando el peso de sus envíos activos (PREPARADO, EN_RUTA, EN_REPARTO).
- **Validaciones en doble capa:** Bean Validation en entidades + validaciones de negocio en los servicios + validación JavaScript en el frontend para una UX fluida.
