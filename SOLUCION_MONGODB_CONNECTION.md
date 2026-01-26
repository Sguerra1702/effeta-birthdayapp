# 🔧 Solución: Error de Conexión MongoDB

## ❌ Problema Detectado

Tu aplicación está intentando conectarse a:
```
localhost:27017  ❌ (MongoDB local que no existe)
```

En lugar de:
```
mongodb+srv://effeta.bg7eptg.mongodb.net  ✅ (MongoDB Atlas en la nube)
```

---

## ✅ Solución: Configurar Variables de Entorno

### Opción 1: Crear archivo application-prod.properties ⭐ (RECOMENDADO)

```bash
# En la VM de GCP
cd ~/effeta-birthdayapp

# Crear archivo de configuración de producción
nano application-prod.properties
```

**Pega este contenido:**
```properties
# MongoDB Atlas Connection
spring.data.mongodb.uri=mongodb+srv://birthdayapp_user:EffetaEffeta.admin2026@effeta.bg7eptg.mongodb.net/birthdayapp?retryWrites=true&w=majority&appName=Effeta
spring.data.mongodb.database=birthdayapp

# Server Configuration
server.port=8080

# Security
spring.security.user.name=admin
spring.security.user.password=Admin2024!

# Logging
logging.level.org.springframework.data.mongodb=DEBUG
logging.level.com.effeta.BirthdayApp=INFO
```

**Guardar:** `Ctrl + O` → `Enter` → `Ctrl + X`

**Ejecutar con este archivo:**
```bash
java -jar target/BirthdayApp-0.0.1-SNAPSHOT.jar --spring.config.location=application-prod.properties
```

---

### Opción 2: Variable de Entorno en el Servicio Systemd

```bash
# Editar el servicio
sudo nano /etc/systemd/system/birthdayapp.service
```

**Reemplaza todo el contenido con esto:**
```ini
[Unit]
Description=Birthday App Spring Boot Application
After=network.target

[Service]
Type=simple
User=santiago_guerra_775
WorkingDirectory=/home/santiago_guerra_775/effeta-birthdayapp

# Variables de entorno
Environment="MONGODB_URI=mongodb+srv://birthdayapp_user:EffetaEffeta.admin2026@effeta.bg7eptg.mongodb.net/birthdayapp?retryWrites=true&w=majority&appName=Effeta"
Environment="MONGODB_DATABASE=birthdayapp"

# Comando de ejecución
ExecStart=/usr/bin/java -jar /home/santiago_guerra_775/effeta-birthdayapp/target/BirthdayApp-0.0.1-SNAPSHOT.jar

SuccessExitStatus=143
Restart=always
RestartSec=10

[Install]
WantedBy=multi-user.target
```

**Guardar:** `Ctrl + O` → `Enter` → `Ctrl + X`

**Recargar y reiniciar:**
```bash
sudo systemctl daemon-reload
sudo systemctl restart birthdayapp
sudo systemctl status birthdayapp
```

---

### Opción 3: Variable de Entorno Temporal (Para Pruebas)

```bash
# Detener si está corriendo
pkill -f BirthdayApp

# Exportar variable
export MONGODB_URI="mongodb+srv://birthdayapp_user:EffetaEffeta.admin2026@effeta.bg7eptg.mongodb.net/birthdayapp?retryWrites=true&w=majority&appName=Effeta"

# Ejecutar
java -jar target/BirthdayApp-0.0.1-SNAPSHOT.jar
```

---

## 🎯 Solución Recomendada (Paso a Paso)

### 1️⃣ Detener la aplicación actual
```bash
# Si está corriendo como servicio
sudo systemctl stop birthdayapp

# O si está corriendo en terminal
pkill -f BirthdayApp
```

### 2️⃣ Crear archivo de configuración de producción
```bash
cd ~/effeta-birthdayapp

# Crear archivo
cat > application-prod.properties << 'EOF'
spring.data.mongodb.uri=mongodb+srv://birthdayapp_user:EffetaEffeta.admin2026@effeta.bg7eptg.mongodb.net/birthdayapp?retryWrites=true&w=majority&appName=Effeta
spring.data.mongodb.database=birthdayapp
server.port=8080
spring.security.user.name=admin
spring.security.user.password=Admin2024!
logging.level.org.springframework.data.mongodb=DEBUG
EOF

# Verificar que se creó
cat application-prod.properties
```

### 3️⃣ Actualizar el servicio systemd
```bash
sudo nano /etc/systemd/system/birthdayapp.service
```

**Contenido completo del servicio:**
```ini
[Unit]
Description=Birthday App Spring Boot Application
After=network.target

[Service]
Type=simple
User=santiago_guerra_775
WorkingDirectory=/home/santiago_guerra_775/effeta-birthdayapp
ExecStart=/usr/bin/java -jar /home/santiago_guerra_775/effeta-birthdayapp/target/BirthdayApp-0.0.1-SNAPSHOT.jar --spring.config.location=/home/santiago_guerra_775/effeta-birthdayapp/application-prod.properties
SuccessExitStatus=143
Restart=always
RestartSec=10

[Install]
WantedBy=multi-user.target
```

**Guardar:** `Ctrl + O` → `Enter` → `Ctrl + X`

### 4️⃣ Reiniciar servicio
```bash
# Recargar configuración
sudo systemctl daemon-reload

# Reiniciar servicio
sudo systemctl restart birthdayapp

# Ver estado
sudo systemctl status birthdayapp

# Ver logs en tiempo real
sudo journalctl -u birthdayapp -f
```

### 5️⃣ Verificar logs
**Deberías ver:**
```
✅ MongoClient with metadata ... created with settings
✅ Cluster created with settings ... hosts=[effeta.bg7eptg.mongodb.net]
✅ Started BirthdayAppApplication in X seconds
```

**En lugar de:**
```
❌ Exception opening socket
❌ Connection refused
❌ localhost:27017
```

---

## 🔍 Verificar Conexión a MongoDB Atlas

```bash
# Ver logs completos
sudo journalctl -u birthdayapp -n 100

# Si ves este mensaje, ¡está conectado! ✅
# "Cluster created with settings ... hosts=[effeta.bg7eptg.mongodb.net]"
```

---

## 🌐 Probar la Aplicación

**Desde la VM:**
```bash
curl http://localhost:8080/admin.html
```

**Desde tu navegador:**
```
http://TU_IP:8080/admin.html
```

**O sin puerto (si configuraste Nginx):**
```
http://TU_IP/admin.html
```

---

## 🐛 Si Sigue sin Funcionar

### Verificar Connection String
```bash
# Ver qué está usando
cat application-prod.properties | grep mongodb

# Debe mostrar:
# spring.data.mongodb.uri=mongodb+srv://birthdayapp_user:EffetaEffeta.admin2026@effeta.bg7eptg.mongodb.net/...
```

### Probar conexión manualmente
```bash
# Instalar mongosh (MongoDB Shell)
curl -fsSL https://www.mongodb.org/static/pgp/server-7.0.asc | sudo gpg --dearmor -o /usr/share/keyrings/mongodb-server-7.0.gpg
echo "deb [ signed-by=/usr/share/keyrings/mongodb-server-7.0.gpg ] https://repo.mongodb.org/apt/ubuntu jammy/mongodb-org/7.0 multiverse" | sudo tee /etc/apt/sources.list.d/mongodb-org-7.0.list
sudo apt update
sudo apt install -y mongodb-mongosh

# Probar conexión
mongosh "mongodb+srv://birthdayapp_user:EffetaEffeta.admin2026@effeta.bg7eptg.mongodb.net/birthdayapp?retryWrites=true&w=majority&appName=Effeta"

# Si conecta, verás:
# Current Mongosh Log ID: ...
# Connecting to: mongodb+srv://...
# Using MongoDB: ...
```

### Verificar IP Whitelist en MongoDB Atlas
1. Ve a: https://cloud.mongodb.com
2. Security → Network Access
3. Verifica que esté **0.0.0.0/0** (permite todas las IPs)
4. O agrega la IP de tu VM de GCP

---

## ✅ Resumen

**El problema:**
- La app usa el valor por defecto `localhost:27017`
- No encuentra MongoDB local
- No puede conectarse a Atlas

**La solución:**
- Crear `application-prod.properties` con el Connection String correcto
- Configurar el servicio systemd para usar este archivo
- Reiniciar la aplicación

**Archivo clave:**
```bash
# Este es el archivo más importante
~/effeta-birthdayapp/application-prod.properties
```

**Con la línea crítica:**
```properties
spring.data.mongodb.uri=mongodb+srv://birthdayapp_user:EffetaEffeta.admin2026@effeta.bg7eptg.mongodb.net/birthdayapp?retryWrites=true&w=majority&appName=Effeta
```

---

## 🎉 Próximos Pasos

Una vez que veas en los logs:
```
✅ Started BirthdayAppApplication
```

Entonces puedes:
1. Acceder desde el navegador
2. Configurar Nginx (si quieres quitar el puerto)
3. Agregar más funcionalidades

**¡Tu app estará conectada a MongoDB Atlas en la nube! 🚀**
