# 🎂 BirthdayApp - Sistema de Invitaciones Personalizadas

Sistema de invitaciones digitales para cumpleaños con enlaces únicos por invitado.

## 🚀 Características

- ✅ Invitaciones personalizadas con enlace único
- ✅ Confirmación de asistencia
- ✅ Panel de administración
- ✅ Sin necesidad de que el invitado ingrese datos
- ✅ Enlaces compartibles por WhatsApp

## 📋 Cómo funciona

1. Cada invitado recibe un **enlace único** por WhatsApp
2. Al abrir el enlace, ve su tarjeta personalizada con su nombre
3. Puede confirmar su asistencia con un clic
4. El administrador ve en tiempo real quién confirmó

## 🛠️ Construcción del proyecto

### Opción 1: Con Maven Wrapper (Recomendado)
```powershell
.\mvnw.cmd clean package
```

### Opción 2: Con Maven instalado
```powershell
mvn clean package
```

El archivo JAR se generará en: `target/BirthdayApp-0.0.1-SNAPSHOT.jar`

## ▶️ Ejecución

### Opción 1: Con Maven Wrapper
```powershell
.\mvnw.cmd spring-boot:run
```

### Opción 2: Con el JAR compilado
```powershell
java -jar target/BirthdayApp-0.0.1-SNAPSHOT.jar
```

### Opción 3: Con Maven instalado
```powershell
mvn spring-boot:run
```

La aplicación estará disponible en: **http://localhost:8080**

## 📱 Uso

### Para Administradores

1. Abre el panel de administración: **http://localhost:8080/admin.html**
2. Agrega invitados con sus nombres
3. Copia los enlaces generados
4. Envía cada enlace al invitado correspondiente por WhatsApp

### Para Invitados

1. Reciben un enlace como: `http://localhost:8080/invitacion.html?id=abc123`
2. Al abrirlo, ven su invitación personalizada
3. Pueden confirmar su asistencia con un clic

## 🔗 Endpoints de la API

- `GET /api/invitacion?id={id}` - Obtener información de invitado
- `POST /api/confirmar?id={id}` - Confirmar asistencia
- `POST /api/invitado` - Crear nuevo invitado (body: `{"nombre": "Juan"}`)
- `GET /api/invitados` - Obtener todos los invitados

## 📊 Datos de Prueba

Al iniciar la aplicación, se crean automáticamente 4 invitados de prueba:
- Ana García
- Carlos Pérez
- María López
- Juan Rodríguez

Los enlaces se muestran en la consola al iniciar la aplicación.

## 🎨 Personalización

Puedes personalizar:
- Fecha del evento en `invitacion.html` (línea con 📅)
- Hora en `invitacion.html` (línea con 🕐)
- Lugar en `invitacion.html` (línea con 📍)
- Colores en los estilos CSS

## 📦 Estructura del Proyecto

```
src/main/java/com/effeta/BirthdayApp/
├── model/
│   └── Invitado.java          # Modelo de datos
├── service/
│   └── InvitacionService.java # Lógica de negocio
├── controller/
│   └── InvitacionController.java # API REST
└── BirthdayAppApplication.java

src/main/resources/
├── static/
│   ├── invitacion.html        # Tarjeta de invitación
│   └── admin.html             # Panel de administración
└── application.properties
```

## 🚀 Despliegue en producción

Para desplegar en un servidor real (Railway, Heroku, etc.):

1. El enlace cambiaría a tu dominio: `https://tudominio.com/invitacion.html?id=abc123`
2. Asegúrate de usar HTTPS en producción
3. Considera usar una base de datos real (PostgreSQL, MySQL) en lugar del Map en memoria

## 💡 Mejoras futuras

- [ ] Base de datos persistente
- [ ] Autenticación para el panel admin
- [ ] Envío automático de WhatsApp con API
- [ ] Recordatorios automáticos
- [ ] Mapa de ubicación del evento
- [ ] Galería de fotos
- [ ] Lista de regalos

## 📄 Licencia

Este proyecto es de código abierto y puede ser usado libremente.
