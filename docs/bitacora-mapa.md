# 🔧 Bitácora técnica — Equipo Mapas (Parte B)

> Registro vivo de decisiones, errores y avances del equipo. El informe formal para entrega va en el Word; este documento es el detalle técnico que vive en el repositorio.

**Integrante Parte B:** Carlos Milan Rojas Irigoyen  
**Rama:** MAPAS  
**Última actualización:** [24/09/26]

## 1. Avances
- [x] Modelo `PlaceUiModel` — verificado en dispositivo — `model/PlaceUiModel.kt`
- [x] Datos semilla de Ica — hecho (luego reasignado) — `data/DatosSemilla.kt`
- [x] Dependencias del build (MapLibre + Compose + compileSdk 37.1) — `app/build.gradle.kts`
- [x] Algoritmo de distancia (Haversine) — validado: Plaza→Huacachina = 4.32 km — `util/GeoUtils.kt`
- [x] Conversión a GeoJSON — validada: orden `[lng,lat]` correcto — `util/GeoJsonConverter.kt`
- [ ] `MapVM` con corrutinas (hilos de fondo) — pendiente — `vm/MapVM.kt`
- [ ] UI Compose: chips + BottomSheet — pendiente — `ui/`

## 2. Decisiones técnicas
- **D1** — Haversine para distancias: calcula km entre 2 coordenadas sin API externa; suficiente para ordenar lugares por cercanía (motor de recomendaciones).
- **D2** — Limitación aceptada: Haversine mide distancia "en línea recta" (vuelo de pájaro), no por calles. Las rutas reales son alcance del equipo de Rutas.
- **D3** — Pruebas en dispositivo físico: mi PC tiene 8 GB RAM (6 en uso con el IDE); el emulador necesita 2-4 GB extra. Se usa depuración USB.
- **D4** — Convenciones: nombres según doc 24.1 (PascalCase/camelCase) y 24.2 (prefijos: UiModel, VM, Act).

## 3. Errores encontrados y soluciones

**3.1 Unresolved reference 'maplibre'**
- **Causa:** El código de `MapClusterManager` usa la librería pero no estaba declarada en Gradle.
- **Solución:** Agregar `org.maplibre.gl:android-sdk:11.7.1` + `android-plugin-annotation-v9:3.0.2` y sincronizar.
- **Lección:** La librería se sube en el MISMO commit que el código que la usa.

**3.2 Unresolved reference 'Composable'**
- **Causa:** Proyecto creado con plantilla Views/XML, sin Jetpack Compose habilitado.
- **Solución:** 3 piezas en Gradle — plugin `org.jetbrains.kotlin.plugin.compose`, BOM `compose-bom` + libs `ui/material3`, y `buildFeatures { compose = true }`.

**3.3 AAR metadata: core-ktx 1.19.0 exige API 37**
- **Causa:** `compileSdk` del proyecto en 36.1; la librería requiere compilar contra API 37+.
- **Solución:** Subir `compileSdk` a 37.1 en `build.gradle.kts`. (No se tocó `minSdk` para no limitar dispositivos).

**3.4 No target device found**
- **Causa:** Sin emulador (PC con 8 GB RAM no lo soporta) ni celular conectado.
- **Solución:** Depuración USB en dispositivo físico (opciones de desarrollador + permiso).

**3.5 Archivos `.idea/` colando en commits**
- **Causa:** Configuración del IDE quedó versionada; `git add` la metió al staging area.
- **Solución inmediata:** `git restore --staged .idea`.
- **Pendiente de equipo:** Agregar `.idea/` al `.gitignore` + `git rm -r --cached .idea`.

## 4. Próximos pasos
- [ ] `MapVM` con StateFlow y corrutinas (hilos de fondo)
- [ ] Chips de filtro y BottomSheet en Compose (Material3)
- [ ] Integración con `MapClusterManager.updatePlacesData()` (Parte A)