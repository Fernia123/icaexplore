🔧 Bitácora técnica — Equipo Mapas (Parte B)
Registro vivo de decisiones, errores y avances del equipo.El informe formal para entrega va en el Word; este documento es eldetalle técnico que vive en el repositorio.

Integrante Parte B: Carlos Milan Rojas Irigoyen
Rama: MAPAS
Última actualización: [24/09/26]

1. Avances
   Tarea	Estado	Archivo
   Modelo PlaceUiModel	✅ Terminado y verificado en dispositivo	model/PlaceUiModel.kt
   Datos semilla de Ica	✅ Hecho (tarea luego reasignada)	data/DatosSemilla.kt
   Dependencias del build (MapLibre + Compose + compileSdk 37.1)	✅ Terminado	app/build.gradle.kts, gradle/libs.versions.toml
   Algoritmo de distancia (Haversine)	✅ Validado: Plaza→Huacachina = 4.32 km	util/GeoUtils.kt
   Conversión PlaceUiModel → GeoJSON	⬜ Pendiente	(en MapVM)
   MapVM con corrutinas (hilos de fondo)	⬜ Pendiente	vm/MapVM.kt
   UI Compose: chips + BottomSheet	⬜ Pendiente	ui/

2. Decisiones técnicas
   D1 — Haversine para distancias: calcula km entre 2 coordenadas sin API externa; suficiente para ordenar lugares por cercanía (motor de recomendaciones).
   D2 — Limitación aceptada: Haversine mide distancia "en línea recta" (vuelo de pájaro), no por calles. Las rutas reales son alcance del equipo de Rutas.
   D3 — Pruebas en dispositivo físico: mi PC tiene 8 GB RAM (6 en uso con el IDE); el emulador necesita 2-4 GB extra. Se usa depuración USB.
   D4 — Convenciones: nombres según doc 24.1 (PascalCase/camelCase) y 24.2 (prefijos: UiModel, VM, Act).

3. Errores encontrados y soluciones
   3.1 Unresolved reference 'maplibre'
   Causa: el código de MapClusterManager usa la librería pero no estaba declarada en Gradle.
   Solución: agregar org.maplibre.gl:android-sdk:11.7.1 + android-plugin-annotation-v9:3.0.2 y sincronizar.
   Lección: la librería se sube en el MISMO commit que el código que la usa.
   3.2 Unresolved reference 'Composable'
   Causa: proyecto creado con plantilla Views/XML, sin Jetpack Compose habilitado.
   Solución: 3 piezas en Gradle — plugin org.jetbrains.kotlin.plugin.compose, BOM compose-bom + libs ui/material3, y buildFeatures { compose = true }.
   3.3 AAR metadata: core-ktx 1.19.0 exige API 37
   Causa: compileSdk del proyecto en 36.1; la librería requiere compilar contra API 37+.
   Solución: subir compileSdk a 37.1 en build.gradle.kts. (No se tocó minSdk para no limitar dispositivos.)
   3.4 No target device found
   Causa: sin emulador (PC con 8 GB RAM no lo soporta) ni celular conectado.
   Solución: depuración USB en dispositivo físico (opciones de desarrollador + permiso).
   3.5 Archivos .idea/ colando en commits
   Causa: configuración del IDE quedó versionada; git add la metió al staging area.
   Solución inmediata: git restore --staged .idea.
   Pendiente de equipo: agregar .idea/ al .gitignore + git rm -r --cached .idea.

4. Próximos pasos (Parte B)
   Conversión a GeoJSON (⚠️ en GeoJSON las coordenadas van [lng, lat], orden invertido respecto a Google Maps).
   MapVM con StateFlow y corrutinas: conversión + distancias en hilos de fondo (evita el aviso "Skipped frames / too much work on main thread" que ya observamos en Logcat).
   Chips de filtro por categoría y BottomSheet de detalle en Compose (material3).
   Integración final con MapClusterManager.updatePlacesData() (Parte A).