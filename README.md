# **Training Routine App**  
**Autor**: Pablo Camino Vázquez  
**Correo Personal**: [pablocavaz2004@gmail.com](mailto:pablocavaz2004@gmail.com)  
**Correo Corporativo**: [pcamvaz2106@g.educaand.es](mailto:pcamvaz2106@g.educaand.es)  
**GitHub**: [pablitocavaz04](https://github.com/pablitocavaz04)  

---

## 📋 **Descripción**
**Training Routine App** es una aplicación Android diseñada para la gestión de entrenamientos, sesiones y jugadores.  
Utiliza persistencia local con **Room** y sincronización remota con **Strapi**, ofreciendo una experiencia de usuario fluida y eficiente.

---

## ⭐ **Características Principales**
### 🏋️ **Roles de Usuario**
- **Jugador**:  
  - Visualizar sesiones asignadas y detalles de entrenamientos.
- **Entrenador**:  
  - Crear, gestionar y asignar sesiones a jugadores.
  - Visualizar jugadores y entrenamientos disponibles.
- **Gestor** *(disponible en la versión web)*:  
  - Crear, editar y eliminar jugadores, entrenadores y entrenamientos.

### 🔑 **Funcionalidades Comunes**
- **Autenticación segura con JWT**.
- **Persistencia local** con **Room** para manejo de datos offline.
- Navegación intuitiva con **Navigation Component**.
- Interfaz moderna basada en **Material Design**.

---

## 🛠 **Tecnologías Utilizadas**
### 📱 **Frontend**
- **Android**:  
  - **Kotlin**, **Jetpack Navigation**, **Room**, **Material Design**.
- **Web** *(para gestores)*:  
  - **Ionic Angular**.

### ⚙️ **Backend**
- **Strapi**:  
  - Gestión de modelos, autenticación y API REST.

### 💾 **Persistencia Local**
- **Room Database** para almacenar y gestionar datos offline.

### 🔧 **Herramientas Complementarias**
- **Retrofit**: Comunicación con el backend.  
- **Hilt**: Inyección de dependencias.  
- **Lottie**: Animaciones interactivas.  
- **EncryptedSharedPreferences**: Manejo seguro de datos de usuario.  

---

## 📂 **Estructura del Proyecto**
```plaintext
app/
├── data/
│   ├── local/            # Configuración de Room (DAO, entidades, base de datos)
│   ├── remote/           # Configuración de Retrofit (API Service, modelos)
│   ├── repository/       # Repositorios para gestionar datos (local y remoto)
│   └── ui/               # Fragmentos y lógica de la interfaz de usuario
├── di/                   # Configuración de Hilt para inyección de dependencias
├── models/               # Modelos de datos compartidos
└── utils/                # Utilidades y funciones auxiliares
