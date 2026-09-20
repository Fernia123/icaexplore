# INFORME DE DESARROLLO Y ARQUITECTURA - EQUIPO 3 (MAPAS)

## 1. Division de Responsabilidades (50/50)

Se ha establecido la siguiente division tecnica para el desarrollo del modulo de mapas:

### Parte A: Motor Grafico y Sensores (Desarrollador 1)
* Implementacion de `MapViewContainer` en Jetpack Compose con integracion estricta de ciclo de vida (`DisposableEffect` y persistencia de estado).
* Administracion de capas visuales y clustering nativo a traves de `MapClusterManager` (Manejo de fuentes `GeoJsonSource` en GPU).
* Contrato de geolocalizacion segura `LocationProvider` con corrutinas tolerantes a fallos y manejo de tiempo de espera.

### Parte B: Logica de Datos, Estado y UI Superpuesta (Desarrollador 2)
* Construccion del componente `MapVM` (ViewModel) para la conversion de estados reactivos (`PlaceUiModel` a GeoJSON) en hilos de procesamiento de fondo.
* Desarrollo de la interfaz superpuesta en Compose (Chips de busqueda, BottomSheet de descripcion de lugares).
* Integracion con el motor de recomendaciones locales y algoritmos de distancia.

## 2. Resolucion de Vulnerabilidades Arquitectonicas

Se han solucionado tres vulnerabilidades criticas que amenazaban el rendimiento y la estabilidad del sistema:

* **Control de Inicializacion OpenGL:** Se reemplazaron las vistas basadas en Fragments por la funcion `AndroidView`. Se incluyo `mapView.onCreate(savedInstanceState)` para forzar el encendido del motor grafico e impedir pantallas negras. Se aplico `rememberSaveable` garantizando que las coordenadas no se pierdan al rotar la pantalla.
* **Tolerancia a Fallos en GPS:** Se modifico el contrato de ubicacion para evitar bloqueos por operaciones sincronas. Se implemento el metodo `awaitUserLocation()` devolviendo un patron `Result<Location>`, envuelto en un `withTimeoutOrNull` (5000ms), protegiendo al Equipo 5 de suspender procesos infinitamente ante falta de permisos o mala senal satelital.
* **Prevencion de Colapso por Rendering (ANR):** Se desacoplo la creacion de capas visuales de la inyeccion de datos. El `MapClusterManager` registra los identificadores de capa en `onMapReady` una sola vez y expone `updatePlacesData()` para realizar inyeccion en caliente mediante `setGeoJson()`. El trabajo matematico del clustering se delego por completo al motor C++ nativo de MapLibre, asegurando una ejecucion de 60 fotogramas por segundo sin obstruir el hilo de la interfaz de usuario en Kotlin.

## 3. Estado de Avance e Integracion

Los componentes base correspondientes a la **Parte A** han sido codificados y generados en la rama actual (`MAPAS`). Los archivos respetan las politicas de nomenclatura y se encuentran en los siguientes directorios:

* `app/src/main/java/com/example/ica_explore/presentation/map/MapViewContainer.kt`
* `app/src/main/java/com/example/ica_explore/presentation/map/MapClusterManager.kt`
* `app/src/main/java/com/example/ica_explore/domain/location/LocationProvider.kt`

Para su correcto ensamblaje y evaluacion de calidad por el Equipo 1, el archivo de configuracion Gradle debera incluir las dependencias declaradas del SDK de MapLibre y Jetpack Compose. El codigo actual se encuentra listo para someterse a validacion estatica (ktlint/detekt) y a pruebas en dispositivos fisicos.
