# Verificación

## Comandos

Ejecutar desde la raíz en PowerShell:

```powershell
.\gradlew.bat :app:assembleDebug :app:testDebugUnitTest :app:lintDebug --console=plain
```

## Pruebas automatizadas

`RepositoryTest.kt` usa fuentes en memoria para comprobar el comportamiento del repositorio sin dispositivo ni servidor:

1. Crear y eliminar un contacto opera en la fuente local sin consultar la red.
2. Sincronizar explícitamente descarga contactos y los guarda en la fuente local.
3. Crear una tarea guarda el nombre y el estado pendiente y permite leerla desde el repositorio.

Estas pruebas no sustituyen la ejecución de Room real ni las pruebas de interacción de Compose. La prueba aritmética de plantilla se conserva.

## Comprobación manual en dispositivo

- Abrir la app: debe aparecer Contactos y su lista vacía en una instalación limpia.
- Intentar guardar sin nombre o teléfono: no debe insertar filas.
- Guardar dos contactos con distintos nombres: deben aparecer ordenados alfabéticamente.
- Eliminar uno: debe desaparecer de la lista.
- Cambiar a Tareas y agregar una tarea: debe mostrar el nombre y Pendiente.
- Agregar otra: la más reciente debe aparecer primero.
- Intentar agregar una tarea vacía: no debe insertarse.
- Cambiar entre ambas pestañas: debe mostrarse la pantalla seleccionada.
- Cerrar y volver a abrir la app: los contactos y tareas guardados deben persistir.

## Resultado

La ejecución final de `:app:assembleDebug :app:testDebugUnitTest :app:lintDebug` terminó con `BUILD SUCCESSFUL`.

- APK de depuración generado correctamente.
- Cuatro pruebas unitarias aprobadas: tres de repositorio y una de la plantilla.
- Android Lint completado sin errores que bloqueen la compilación.
- `git diff --check` no detectó errores de espacios en los cambios rastreados.

Gradle muestra avisos de configuración experimental, compatibilidad del objetivo JVM y uso obsoleto de `fallbackToDestructiveMigration()`; se conserva la configuración del material original. No se ha realizado una ejecución visual en emulador o dispositivo.
