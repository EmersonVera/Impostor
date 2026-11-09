# Impostor Android: distribución de entregas

Migración de la aplicación web *El Impostor* a Kotlin con Jetpack Compose. La estructura sigue el proyecto de referencia StreamFlix: una sola Activity, Navigation Compose, MVVM y capas `data`, `domain` y `presentacion`.

| Funcionalidad | Entrega | Justificación | Estado |
|---|---|---|---|
| Estructura por capas, Activity única, tema visual (colores, Plus Jakarta Sans, botones y tarjetas neo) | 1 | Base de todo lo demás | Implementada |
| Navegación con `NavHost`, `sealed class Screen`, barra inferior (Lobby, Reglas) | 1 | Concepto fundamental de clase | Implementada |
| Pantallas Home y Reglas | 1 | Contenido estático, Compose básico | Implementada |
| Configuración: agregar y quitar jugadores con validaciones (mín. 3, sin duplicados, 15 caracteres) | 1 | Estado, formularios, validación en caso de uso | Implementada |
| Número de impostores (máx. `(n-1)/2`, ajuste al quitar jugadores) | 1 | Las condiciones de victoria dependen de esta regla | Implementada |
| Palabras locales: baraja General con 3 subtemas (Videojuegos, Comida, Peliculas; 40 palabras) | 1 | Repositorio simple, datos sin red | Implementada |
| Asignación aleatoria de roles, palabra y pista por impostor | 1 | Núcleo del juego, testeable | Implementada |
| Entrega secreta con mantener presionado para ver | 1 | Interacción distintiva del juego | Implementada |
| Ronda de pistas con jugador inicial aleatorio | 1 | Parte del ciclo mínimo | Implementada |
| Votación, eliminación, condiciones de victoria, rondas sucesivas | 1 | Sin esto no hay juego completo | Implementada |
| Pantallas de resultado (eliminado, civiles ganan, impostor gana) y jugar otra vez | 1 | Cierra el ciclo jugable | Implementada |
| Baraja completa desde CSV en `assets` (135 palabras, incluye UFPS y los 7 subtemas) | 2 | Lectura y parseo de archivos | Pendiente |
| Selección de categoría UFPS/General y subtemas (mín. 1 activo) | 2 | Estado más complejo sobre la base anterior | Pendiente |
| Pantalla Barajas con filtros y activación, pestaña Packs | 2 | Listas y pantalla adicional | Pendiente |
| Modo con pista / sin pista | 2 | Variante de reglas sobre el flujo ya estable | Pendiente |
| Rotación justa de impostores e iniciadores, palabras recientes (16) | 2 | Algoritmos con estado de sesión | Pendiente |
| Ronda secreta de paranoia (5 %, contadores simulados) | 3 | Rama compleja que atraviesa toda la lógica | Pendiente |
| Confeti | 3 | Animación avanzada | Pendiente |
| Sincronización con Google Sheets, caché local y cadena de respaldo | 3 | Red, corrutinas reales y persistencia | Pendiente |
| Animaciones finas y pulido final | 3 | Acabado | Pendiente |

## Fuera de alcance

Estas partes existen en el código o la documentación de la web, pero no funcionan como característica visible, por lo que no se migran: temporizador de debate (retirado), `PrivacyWarningScreen` y `RoleRevealScreen` (código muerto), carga de archivos y sincronización manual (sin interfaz), y herramientas de depuración.
