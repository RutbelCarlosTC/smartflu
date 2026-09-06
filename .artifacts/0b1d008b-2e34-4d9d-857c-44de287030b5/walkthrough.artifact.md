# Resumen de cambios: Solución de Firestore y Registro

Se han realizado cambios para solucionar los errores de permisos y asegurar que los datos del usuario se guarden correctamente en Firestore.

## Cambios realizados

### 1. Inicialización de Usuario en Registro
Se modificó [RegisterUser.kt](file:///home/carlos/Documents/smartflu/app/src/main/java/com/unsa/smartflu/login/RegisterUser.kt) para que, al crear una cuenta nueva, se inicialice automáticamente un documento en la colección `users` con el email del usuario y una lista de dispositivos (`devs`) vacía.

### 2. Vinculación de Dispositivos más robusta
Se actualizó [ConnectDevice.kt](file:///home/carlos/Documents/smartflu/app/src/main/java/com/unsa/smartflu/components/ConnectDevice.kt) para:
- Crear el documento del usuario si no existe al momento de vincular un dispositivo.
- Evitar duplicados en la lista de dispositivos vinculados.
- Usar `SetOptions.merge()` para no sobrescribir otros posibles datos del usuario.

### 3. Corrección de Crash en MainActivity
Se corrigió un error que causaba que la aplicación se cerrara (`IllegalArgumentException`) cuando un usuario nuevo no tenía dispositivos vinculados. Ahora la aplicación maneja correctamente las listas vacías antes de consultar a Firestore.

## Pasos Siguientes Importantes

> [!IMPORTANT]
> **El error `DEVELOPER_ERROR` y `SecurityException` confirman que falta el SHA-1.**
> Tu aplicación está fallando al comunicarse con los servicios de Google porque no reconoce la "firma" de tu app.
> 1. Ejecuta `./gradlew signingReport` en la terminal de Android Studio.
> 2. Busca el código **SHA-1** de la variante `debug`.
> 3. Agrégalo en la **Consola de Firebase** -> Configuración del proyecto -> Tus aplicaciones.

## Verificación
- El proyecto compila correctamente (`assembleDebug`).
- La lógica de Firestore ahora maneja la creación de documentos faltantes.
