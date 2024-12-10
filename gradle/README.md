# Training Routine App

Training Routine es una aplicación Android para la gestión de entrenamientos, sesiones y jugadores. Utiliza persistencia local con Room y sincronización remota con Strapi para mejorar el rendimiento y la experiencia del usuario.

## **Características**
- **Roles de Usuario:**
  - **Jugador:** Ver sesiones asignadas y entrenamientos disponibles.
  - **Entrenador:** Gestionar sesiones, listar jugadores y ver entrenamientos.
- **Persistencia Local con Room:** 
  - Datos sincronizados desde Strapi y almacenados localmente para consultas rápidas.
- **Sincronización con Strapi:**
  - Integración con API remota para entrenamientos, sesiones y jugadores.
- **Autenticación con Token JWT:**
  - Login seguro con almacenamiento de token en `SharedPreferences`.

---

## **Tecnologías Utilizadas**
- **Kotlin**
- **Android Architecture Components**
  - Room, ViewModel, LiveData.
- **Hilt:** Para la inyección de dependencias.
- **Retrofit:** Para la comunicación con Strapi.
- **Coroutines:** Manejo de operaciones asincrónicas.
- **Material Design:** Para una interfaz moderna y amigable.

---

## **Estructura del Proyecto**

### **1. Persistencia Local**
- **Entidades (Entities):**
  - `EntrenamientoEntity`
  - `PersonaEntity`
  - `SesionEntity`
- **DAOs:**
  - `EntrenamientoDao`
  - `PersonaDao`
  - `SesionDao`
- **Base de Datos:**
  - `TrainingRoutineDatabase`

### **2. Sincronización Remota**
- **API Service:** 
  - `ApiService` define las llamadas a Strapi.
  - `RetrofitInstance` configura Retrofit.
- **Repositorios:**
  - `EntrenamientoRepository`
  - `PersonaRepository`
  - `SesionRepository`

### **3. Gestión de Tokens**
- Almacenamiento en `SharedPreferences`.
- Uso dinámico del token para sincronización.

---

## **Instalación y Configuración**

### **1. Requisitos Previos**
- Android Studio instalado.
- Backend configurado en Strapi.

### **2. Clonar el Repositorio**
```bash
git clone <url-del-repositorio>
