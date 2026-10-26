# 📘 Documentación de Arquitectura y Capa de Datos (Guía para Desarrolladores)

¡Hola, equipo! Este documento explica de forma sencilla y directa cómo está organizado el código de la aplicación **Ica Explorer** (nuestra capa de datos, lógica y conexión con Supabase). 
Android Gradle Plugin (AGP) version : 9.3.2

---

## 1. ¿Qué arquitectura estamos usando?
Usamos **Arquitectura Limpia (Clean Architecture)** orientada a **Funcionalidades (Feature-First)**. 

*   **¿Qué significa "Limpia"?** Significa que separamos estrictamente "las reglas de nuestro negocio" de "las herramientas externas". El núcleo de nuestra app no debe saber si usamos Supabase, Firebase o Room; solo le importan los datos.
*   **¿Qué significa "Feature-First"?** En lugar de tener una carpeta gigante llamada `data` con todos los archivos del proyecto mezclados, organizamos el código por **módulos o funcionalidades** (ej. `auth`, `places`, `categories`). Si necesitas arreglar algo del login, vas a la carpeta `auth` y ahí está todo lo que necesitas.

---

## 2. Estructura de Carpetas Explicada

Nuestra app (`app/src/main/java/com/example/ica_explore/`) se divide principalmente en tres grandes bloques:

### ⚙️ 1. `core/` (El Núcleo Compartido)
Aquí va todo el código genérico que se usa en toda la aplicación.
*   `common/result/AppResult.kt`: Es una clase especial que envuelve nuestras respuestas. Si pedimos datos a internet, esta clase nos dirá si fue `Success` (Éxito con datos), `Error` (Falló) o `Loading` (Cargando).
*   `database/AppDatabase.kt`: La configuración base para nuestra base de datos local (Room).

### 💉 2. `di/` (Inyección de Dependencias)
Aquí configuraremos herramientas como *Koin* o *Hilt*. Su trabajo es simple: cuando la app necesita un "Repositorio", el DI se encarga de crearlo y entregarlo automáticamente.

### 🚀 3. `features/` (Las Funcionalidades)
Este es el lugar donde pasarás el 90% de tu tiempo. Cada funcionalidad (ej. `places`) tiene tres subcapas estrictas:

#### 🟢 A. `domain/` (El cerebro - Lógica de Negocio)
Es la capa más pura. **No puede importar NADA de Android ni de Supabase.**
*   **`model/`** (ej. `Place.kt`): Clases de datos limpias. Representan cómo ve la app a un lugar.
*   **`repository/`** (ej. `PlaceRepository.kt`): Son *Interfaces* (contratos). Le dicen al resto de la app: *"Oye, necesito una función para buscar lugares populares"*, pero NO dicen CÓMO hacerlo.
*   **`usecase/`** (ej. `GetPopularPlacesUseCase.kt`): Son acciones únicas. Son como los botones lógicos de la app. Si la pantalla principal (UI) quiere datos, llama a un UseCase. Esto hace que el código sea facilísimo de leer.

#### 🔴 B. `data/` (Los Obreros - Trabajo sucio y Red)
Esta capa es la que "se ensucia las manos". Se conecta a internet o a la base de datos local.
*   **`datasource/remote/`**: Aquí está el código que llama a Supabase (ej. `AuthRemoteDataSourceImpl.kt`).
*   **`datasource/remote/model/` (DTOs):** Clases que mapean el JSON de la base de datos. Aquí usamos cosas como `@SerialName("rating_avg")` para convertir los nombres feos de la base de datos (snake_case) a nombres bonitos en Kotlin (camelCase).
*   **`datasource/local/`**: Aquí están las Entidades (`PlaceEntity`) y los DAOs (`PlaceDao`) de Room, preparados para guardar cosas sin internet en el futuro (Modo Offline).
*   **`mapper/`**: Funciones traductoras. Convierten el DTO (que viene de internet) a una entidad pura de `domain` para que el resto de la app no vea el DTO contaminado.
*   **`repository/`**: Son las implementaciones reales de los contratos del `domain`. Usan el DataSource para traer los datos y luego usan el Mapper para traducirlos y enviarlos a la app envueltos en un `Result`.

#### 🔵 C. `presentation/` (La Interfaz de Usuario)
Aquí trabajarán los desarrolladores de UI (Jetpack Compose, ViewModels). Esta capa "pinta" en pantalla lo que el `domain` le entrega.

---

## 3. Seguridad de Credenciales (IMPORTANTE)
**NUNCA pongas contraseñas o URLs de la API directamente en el código Kotlin.**
Para solucionar esto, hemos configurado el proyecto de forma profesional:
1.  Debes poner tus credenciales en el archivo `local.properties` (que no se sube a GitHub):
    ```properties
    SUPABASE_URL=tu_url
    SUPABASE_ANON_KEY=tu_clave
    ```
2.  Al darle "Build", Android generará una clase llamada `BuildConfig`. Puedes usarlas en el código así: `BuildConfig.SUPABASE_URL`.

---

## 4. Flujo de Trabajo: ¿Cómo leer o escribir código aquí?

Si te asignan crear el **Sistema de Favoritos**, este es el orden en que debes programarlo:
1.  **Dominio:** Crea la interfaz `FavoriteRepository.kt` con la función `addFavorite()`. Luego crea el UseCase `AddFavoriteUseCase`.
2.  **Datos (Red):** Ve a `data/datasource/remote`, crea una función que haga el `INSERT` en Supabase usando el SDK en Kotlin.
3.  **Datos (Repositorio):** Ve a `data/repository/FavoriteRepositoryImpl.kt`, implementa la interfaz del dominio y llama a tu función de red.
4.  **Presentación:** En tu ViewModel, inyectas el `AddFavoriteUseCase` y lo llamas cuando el usuario toca el icono del corazón.

¡Es así de simple! Esta separación asegura que si mañana cambiamos Supabase por Firebase, **solo tocaremos la carpeta `data`**. El `domain` y la `presentation` se quedarán intactos.
