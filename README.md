# ConsumoDatos

Aplicación Android en Kotlin y Jetpack Compose implementada a partir de las guías de `docs/`. El paquete es `com.rojas.consumodatos`.

## Funciones

- **Contactos:** guardar nombre y teléfono, listar alfabéticamente y eliminar por identificador.
- **Tareas:** crear con estado pendiente y listar desde la más reciente. El estado se muestra como texto.
- **Navegación:** barra inferior con las rutas `contacts` y `tasks`; la pantalla inicial es Contactos.
- **Persistencia:** Room conserva ambas listas en la base local `contacts_db`.

Las entradas se recortan con `trim()` y no se guardan contactos con campos vacíos ni tareas sin nombre.

## Documentación

- [Inicialización y librerías](docs/init.md).
- [Guía de contactos, Room, Retrofit y Hilt](docs/guia-room.md).
- [Guía de tareas y navegación](docs/guia-room-2.md).
- [Detalle de la implementación](docs/implementacion.md).
- [Verificación del proyecto](docs/verificacion.md).

## Ejecutar

Abrir la carpeta raíz en Android Studio, sincronizar Gradle y ejecutar el módulo `app` en un dispositivo o emulador con Android API 27 o superior. El proyecto configura compileSdk y targetSdk 37; requiere ese SDK instalado. `local.properties` debe indicar la ubicación local del Android SDK y no se debe compartir entre equipos.

Desde PowerShell en la raíz:

```powershell
.\gradlew.bat :app:assembleDebug
.\gradlew.bat :app:testDebugUnitTest
```

El APK de depuración se genera en `app/build/outputs/apk/debug/app-debug.apk`. El proyecto incluye configuración de JVM de Gradle con versión 25 y compilación Java con compatibilidad 17.

## Alcance de la red

Se incluyen DTO, servicio Retrofit, fuente remota y `syncRemoteContacts()` como indican las guías. La interfaz guarda localmente y no llama automáticamente a la red. La URL de ejemplo es `https://jsonplaceholder.typicode.com/` y el contrato declara `GET contacts` y `POST contacts`; debe existir un servidor compatible para utilizar esas operaciones. No se cambia el contrato por otro recurso ni se agrega un servidor.
