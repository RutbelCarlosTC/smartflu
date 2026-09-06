# Solución de errores de Permisos de Firestore y SecurityException

El usuario está experimentando dos errores principales:
1. `PERMISSION_DENIED` en Firestore al intentar leer de la colección `users`.
2. `java.lang.SecurityException: Unknown calling package name 'com.google.android.gms'` relacionado con Google Play Services.

## Análisis de Causas

### 1. Firestore PERMISSION_DENIED
Este error ocurre porque las **Reglas de Seguridad de Firestore** han expirado.
*   **Fecha de expiración actual:** 15 de octubre de 2024.
*   **Fecha actual:** Septiembre de 2025.
*   **Causa:** La regla `allow read, write: if request.time < timestamp.date(2024, 10, 15);` está bloqueando todo el tráfico porque ya pasó esa fecha.

### 2. SecurityException (com.google.android.gms)
Este error suele ocurrir por una configuración incompleta en la Consola de Firebase, específicamente la falta del **huella digital del certificado SHA-1** para la aplicación de depuración (debug), o problemas con los Google Play Services en el emulador.

## Plan de Acción

### Paso 1: Actualizar Reglas de Firestore (Manual)
El usuario debe actualizar las reglas en la Consola de Firebase para permitir el acceso a usuarios autenticados.
Nueva regla recomendada:
```javascript
service cloud.firestore {
  match /databases/{database}/documents {
    match /{document=**} {
      allow read, write: if request.auth != null;
    }
  }
}
```

### Paso 2: Agregar SHA-1 a Firebase (Manual)
Instruir al usuario sobre cómo obtener su SHA-1 de debug y agregarlo a la configuración de la aplicación en la Consola de Firebase.

### Paso 3: Inicializar Documento de Usuario en Registro (Automático)
Actualmente, `RegisterUser.kt` no crea el documento en Firestore. Se modificará para crear un documento con un array `devs` vacío al registrarse.

### Paso 4: Robustecer Vinculación de Dispositivos (Automático)
Modificar `ConnectDevice.kt` para asegurar que el documento del usuario se cree o se actualice correctamente (usando `set` con merge) si por alguna razón no existe.

## Cambios Propuestos

### [Componente: Login/Registro]

#### [MODIFICAR] [RegisterUser.kt](file:///home/carlos/Documents/smartflu/app/src/main/java/com/unsa/smartflu/login/RegisterUser.kt)
* Agregar lógica para crear un documento en la colección `users` con un campo `devs` (lista vacía) tras un registro exitoso.

### [Componente: Componentes]

#### [MODIFICAR] [ConnectDevice.kt](file:///home/carlos/Documents/smartflu/app/src/main/java/com/unsa/smartflu/components/ConnectDevice.kt)
* Cambiar `transaction.update` por un método que cree el documento si no existe o use `transaction.set` con merge.

## Verificación
1. Solicitar al usuario que actualice las reglas en la consola.
2. Verificar que el registro de un nuevo usuario cree el documento en Firestore.
3. Verificar que el error de permisos desaparezca en `MainActivity`.
