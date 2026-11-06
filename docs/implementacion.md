# Detalle de implementación

## Fuente y alcance

El código materializa `guia-room.md` y después las ampliaciones de `guia-room-2.md`. Se conservan sus modelos, operaciones, textos y estructura por capas. La segunda guía reemplaza el esquema de Room por la versión 2 y el acceso directo a contactos por `AppNavigation`.

## Organización

La raíz de código es `app/src/main/java/com/rojas/consumodatos/`.

| Carpeta o archivo | Responsabilidad |
| --- | --- |
| `domain/model/Contact.kt`, `Task.kt` | Modelos sin anotaciones de persistencia o red |
| `domain/repository/` | Contratos que consume la presentación |
| `data/local/ContactEntity.kt`, `TaskEntity.kt` | Tablas `contacts` y `tasks`, con identificador autogenerado |
| `data/local/ContactDao.kt`, `TaskDao.kt` | Consultas SQL e inserciones suspendidas |
| `data/local/AppDatabase.kt` | Base Room versión 2 con ambos DAO |
| `data/local/*LocalDataSource.kt` | Interfaces e implementaciones que delegan en los DAO |
| `data/remote/` | DTO de contacto, contrato HTTP y fuente remota |
| `data/mapper/` | Conversiones entre entidades, dominio y DTO |
| `data/repository/` | Implementaciones de los contratos del dominio |
| `di/DatabaseModule.kt` | Base de datos, DAO y fuentes locales |
| `di/NetworkModule.kt` | OkHttp, Retrofit y fuente remota |
| `di/RepositoryModule.kt` | Vinculación de repositorios mediante Hilt |
| `ui/contact/`, `ui/task/` | Estado, ViewModel y pantalla de cada función |
| `ui/navigation/AppNavigation.kt` | Rutas y barra inferior |
| `ConsumoDatosApp.kt` | Inicialización de Hilt con `@HiltAndroidApp` |
| `MainActivity.kt` | Activity con `@AndroidEntryPoint`, tema y navegación |

## Contactos

`Contact` y `ContactEntity` contienen `id: Long`, `name: String` y `phone: String`. El DAO ordena por `name ASC`, inserta con reemplazo ante conflicto y elimina por `id`. La inserción múltiple permite guardar resultados remotos.

`ContactRepositoryImpl.getContacts()` transforma el `Flow` local en modelos de dominio. `addContact()` guarda únicamente en Room; `deleteContact()` elimina únicamente en Room. `syncRemoteContacts()` descarga los DTO y los inserta localmente, como en la guía. Su `runCatching` no propaga el fallo al consumidor; no existe una acción visual de sincronización.

`ContactViewModel` observa los contactos desde su inicialización y publica `ContactUiState` mediante un `StateFlow` de solo lectura. El formulario delega los cambios de texto, guardado y eliminación al ViewModel. La pantalla representa carga, lista vacía y tarjetas con nombre, teléfono y botón Eliminar.

## Tareas

`Task` y `TaskEntity` contienen `id`, `name` e `isCompleted`, inicialmente `false`. El DAO expone listar por `id DESC` e insertar. La fuente local y el repositorio convierten la persistencia en un `Flow<List<Task>>`.

`TaskViewModel` observa esa lista, valida el nombre y limpia el campo después de guardar. `TaskScreen` muestra el formulario y las tarjetas con su estado. No se implementan completar, editar ni eliminar tareas porque la segunda guía limita expresamente el alcance a crear y listar.

## Actualizaciones de la interfaz

La secuencia de guardado es Pantalla → ViewModel → Repositorio → Fuente local → DAO → Room. Tras la escritura, Room emite la lista actualizada; el repositorio la transforma, el ViewModel actualiza su estado y Compose vuelve a dibujarla. Las pantallas usan `collectAsStateWithLifecycle()`.

## Integración necesaria

- Se registra `.ConsumoDatosApp` en el manifest y se anota la Activity para Hilt.
- Se añaden los proveedores de `TaskDao`, `TaskLocalDataSource` y `TaskRepository` indicados en la segunda guía.
- Se declaran explícitamente Lifecycle Runtime Compose y ViewModel KTX en el catálogo y módulo para las API usadas en los ejemplos. Se reutiliza la versión Lifecycle del proyecto.
- Se mantiene la configuración de dependencias existente; `init.md` es un registro histórico y sus versiones pueden diferir del catálogo actual, que es la configuración efectiva.

## Límites de los ejemplos originales

El proveedor de Room conserva `fallbackToDestructiveMigration()` de la primera guía. Si se abre una base anterior sin una migración compatible, puede recrearla y perder sus registros; no se ha definido una migración de conservación en el material.

Los ViewModel mantienen el manejo de errores de las guías: Contactos captura errores del flujo de lectura, pero las escrituras y la pantalla de tareas no tienen un tratamiento específico de fallos. El estado `errorMessage` de Contactos no se representa visualmente en el ejemplo. El interceptor de red conserva nivel BODY.
