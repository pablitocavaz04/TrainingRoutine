# Autor: Pablo Camino Vázquez
# Correo Personal: pablocavaz2004@gmail.com
# Correo Corporativo: pcamvaz2106@g.educaand.es
# Github: https://github.com/pablitocavaz04

# Training Routine App

Training Routine es una aplicación Android para la gestión de entrenamientos, sesiones y jugadores.
Utiliza persistencia local con Room y sincronización remota con Strapi para mejorar el rendimiento y
la experiencia del usuario.

---

## **Características Principales**
- **Roles de Usuario**:
  - **Jugador**:
    - Visualizar sesiones asignadas y detalles de entrenamientos.
  - **Entrenador**:
    - Crear, gestionar y asignar sesiones a jugadores.
    - Visualizar jugadores y entrenamientos disponibles.
  - **Gestor** (disponible en la versión web):
    - Crear, editar y eliminar jugadores, entrenadores y entrenamientos.

- **Funcionalidades Comunes**:
  - Autenticación mediante JWT.
  - Persistencia local con **Room** para mejorar el rendimiento y manejar datos offline.
  - Navegación entre pantallas con **Navigation Component**.
  - Interfaz moderna basada en **Material Design**.

---

## **Tecnologías Usadas**
- **Frontend**:
  - Android: Kotlin, Jetpack Navigation, Room, Material Design.
  - Web: Ionic Angular.

- **Backend**:
  - Strapi para la gestión de modelos, autenticación y API REST.

- **Persistencia Local**:
  - Room Database.

- **Otras Herramientas**:
  - Retrofit para las llamadas API.
  - Hilt para inyección de dependencias.
  - Lottie para animaciones.
  - EncryptedSharedPreferences para manejo seguro de datos de usuario.

---

## **Estructura del Proyecto**
app/
├── data/
│   ├── local/            # Configuración de Room (DAO, entidades, base de datos)
│   ├── remote/           # Configuración de Retrofit (API Service, modelos)
│   ├── repository/       # Repositorios para gestionar datos (local y remoto)
│   └── ui/               # Fragmentos y lógica de la interfaz de usuario
├── di/                   # Configuración de Hilt para inyección de dependencias
├── models/               # Modelos de datos compartidos
└── utils/                # Utilidades y funciones auxiliares
