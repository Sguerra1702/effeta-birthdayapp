# ⚡ Guía Rápida de Despliegue en GCP (35 minutos)

## 🎯 Objetivo
Desplegar BirthdayApp en Google Cloud Platform accesible desde:
```
http://TU_IP_PUBLICA/admin.html
```
**Sin necesidad de especificar puerto :8080**

---

## 📋 PASO 1: MongoDB Atlas (Ya Completado ✅)

Tu Connection String:
```
mongodb+srv://birthdayapp_user:EffetaEffeta.admin2026@effeta.bg7eptg.mongodb.net/birthdayapp?retryWrites=true&w=majority&appName=Effeta
```

---

## ☁️ PASO 2: Crear VM en GCP (10 minutos)

### 2.1 Ir a Google Cloud Console
1. https://console.cloud.google.com
2. Crear proyecto: **"birthdayapp"**
3. Ir a: **Compute Engine** → **Instancias de VM**

### 2.2 Crear Instancia
- **Nombre:** `birthdayapp-server`
- **Región:** `us-central1 (Iowa)` ⭐
- **Tipo de máquina:** `e2-micro` ⭐ (Gratis siempre)
- **Disco:** `Debian 12`, `30 GB`
- **Firewall:** ✅ Permitir HTTP y HTTPS
- Click **Crear**

### 2.3 Configurar Firewall
1. Menú → **Red de VPC** → **Firewall**
2. Click **Crear regla de firewall**
   - **Nombre:** `allow-birthdayapp`
   - **Destinos:** Todas las instancias
   - **Rangos de IPv4:** `0.0.0.0/0`
   - **Protocolos:** TCP puerto `8080`
3. Click **Crear**

### 2.4 Anotar IP
En **Instancias de VM**, anota tu **IP Externa**: `34.123.45.67`

---

## 🔐 PASO 3: Conectar a la VM (1 minuto)

### Opción Fácil: SSH desde Navegador ✅
1. En **Instancias de VM**, click botón **SSH**
2. Se abre terminal en el navegador
3. ¡Listo! Ya estás conectado

### Opción Avanzada: Desde PowerShell
```powershell
# Instalar gcloud CLI
# Descarga: https://cloud.google.com/sdk/docs/install

# Conectar
gcloud compute ssh birthdayapp-server --zone=us-central1-a
```

---

## ⚙️ PASO 4: Instalar Dependencias (5 minutos)

```bash
# Actualizar sistema
sudo apt update && sudo apt upgrade -y

# Instalar Java 17
sudo apt install openjdk-17-jdk -y

# Instalar Maven
sudo apt install maven -y

# Instalar Git
sudo apt install git -y

# Instalar Nginx (para quitar el puerto)
sudo apt install nginx -y

# Verificar
java -version
mvn -version
git --version
nginx -v
```

---

## 📦 PASO 5: Subir Código a GitHub (Desde tu PC)

```powershell
# En tu PC (PowerShell)
cd "C:\Users\santi\OneDrive\Documentos\Personal Projects\BirthdayApp"

# Inicializar Git (si no lo has hecho)
git init
git add .
git commit -m "GCP deployment ready"

# Crear repo en GitHub: https://github.com/new
# Nombre: BirthdayApp

# Conectar y subir
git remote add origin https://github.com/TU_USUARIO/BirthdayApp.git
git branch -M main
git push -u origin main
```

---

## 🚀 PASO 6: Desplegar en GCP (10 minutos)

### 6.1 Clonar Repositorio
```bash
cd ~
git clone https://github.com/TU_USUARIO/BirthdayApp.git
cd BirthdayApp
```

### 6.2 Compilar
```bash
./mvnw clean package -DskipTests
```

**Debe mostrar:** `BUILD SUCCESS`

### 6.3 Configurar Servicio Systemd
```bash
# Obtener usuario actual
whoami
# Ejemplo: tu_usuario

# Crear servicio
sudo nano /etc/systemd/system/birthdayapp.service
```

**Pega este contenido (reemplaza `TU_USUARIO` con el resultado de `whoami`):**
```ini
[Unit]
Description=Birthday App Spring Boot Application
After=network.target

[Service]
Type=simple
User=TU_USUARIO
WorkingDirectory=/home/TU_USUARIO/BirthdayApp
ExecStart=/usr/bin/java -jar /home/TU_USUARIO/BirthdayApp/target/BirthdayApp-0.0.1-SNAPSHOT.jar
SuccessExitStatus=143
Restart=always
RestartSec=10

[Install]
WantedBy=multi-user.target
```

**Guardar:** `Ctrl + O` → `Enter` → `Ctrl + X`

### 6.4 Iniciar Servicio
```bash
# Recargar systemd
sudo systemctl daemon-reload

# Iniciar aplicación
sudo systemctl start birthdayapp

# Verificar estado
sudo systemctl status birthdayapp

# Si está corriendo (verde), habilitar inicio automático
sudo systemctl enable birthdayapp
```

### 6.5 Probar Aplicación
```bash
# Desde la VM
curl http://localhost:8080/admin.html

# Debe mostrar HTML
```

**Desde tu navegador:**
```
http://TU_IP:8080/admin.html
```

---

## 🌐 PASO 7: Configurar Nginx (Quitar Puerto) ⭐

### 7.1 Configurar Nginx
```bash
sudo nano /etc/nginx/sites-available/birthdayapp
```

**Pega este contenido (reemplaza `TU_IP` con tu IP pública de GCP):**
```nginx
server {
    listen 80;
    server_name TU_IP_PUBLICA;

    # Redirigir todo el tráfico a la app
    location / {
        proxy_pass http://localhost:8080;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
        
        # WebSocket support (si lo necesitas)
        proxy_http_version 1.1;
        proxy_set_header Upgrade $http_header_upgrade;
        proxy_set_header Connection "upgrade";
    }
}
```

**Guardar:** `Ctrl + O` → `Enter` → `Ctrl + X`

### 7.2 Activar Configuración
```bash
# Crear enlace simbólico
sudo ln -s /etc/nginx/sites-available/birthdayapp /etc/nginx/sites-enabled/

# Eliminar configuración por defecto (opcional)
sudo rm /etc/nginx/sites-enabled/default

# Probar configuración
sudo nginx -t

# Debe mostrar: "syntax is ok" y "test is successful"

# Reiniciar Nginx
sudo systemctl restart nginx

# Habilitar inicio automático
sudo systemctl enable nginx
```

### 7.3 Verificar
```bash
# Ver estado de Nginx
sudo systemctl status nginx

# Ver estado de la app
sudo systemctl status birthdayapp
```

---

## 🎉 PASO 8: ¡Acceder sin Puerto!

**Abre tu navegador:**
```
http://TU_IP_PUBLICA/admin.html
```

**Ejemplo:**
```
http://34.123.45.67/admin.html
```

**Login:**
- Usuario: `admin`
- Password: `Admin2024!`

✅ **¡Ya no necesitas especificar :8080!**

---

## 🔍 Comandos Útiles

### Ver Logs
```bash
# Logs de la aplicación
sudo journalctl -u birthdayapp -f

# Logs de Nginx
sudo tail -f /var/log/nginx/access.log
sudo tail -f /var/log/nginx/error.log
```

### Reiniciar Servicios
```bash
# Reiniciar aplicación
sudo systemctl restart birthdayapp

# Reiniciar Nginx
sudo systemctl restart nginx
```

### Ver Recursos
```bash
# Instalar htop
sudo apt install htop -y

# Ver recursos
htop
```

---

## 🔄 Actualizar Aplicación

### Desde tu PC:
```powershell
# Hacer cambios en el código
git add .
git commit -m "Nuevos cambios"
git push origin main
```

### En la VM:
```bash
cd ~/BirthdayApp

# Actualizar código
git pull origin main

# Recompilar
./mvnw clean package -DskipTests

# Reiniciar
sudo systemctl restart birthdayapp

# Ver logs
sudo journalctl -u birthdayapp -f
```

**O usa el script automático:**
```bash
cd ~/BirthdayApp
chmod +x deploy-gcp.sh
./deploy-gcp.sh
```

---

## 🐛 Solución de Problemas

### No puedo acceder desde el navegador
```bash
# 1. Verificar que la app está corriendo
sudo systemctl status birthdayapp

# 2. Verificar que Nginx está corriendo
sudo systemctl status nginx

# 3. Probar localmente
curl http://localhost:8080/admin.html
curl http://localhost/admin.html

# 4. Verificar firewall de GCP
# En GCP Console → Red de VPC → Firewall
# Debe existir regla que permita puerto 80 (HTTP)
```

### Error "502 Bad Gateway"
```bash
# La app no está corriendo
sudo systemctl start birthdayapp
sudo systemctl status birthdayapp

# Ver logs
sudo journalctl -u birthdayapp -n 50
```

### Error "Connection refused"
```bash
# Nginx no está corriendo
sudo systemctl start nginx
sudo systemctl status nginx

# Ver errores de Nginx
sudo nginx -t
```

### La app no arranca
```bash
# Ver logs detallados
sudo journalctl -u birthdayapp -n 100

# Problemas comunes:
# - MongoDB connection string incorrecto
# - Puerto 8080 ocupado
# - Permisos incorrectos
```

---

## 🔒 Agregar HTTPS (Opcional)

### Requisitos:
- Dominio propio apuntando a tu IP de GCP

### Pasos:
```bash
# Instalar Certbot
sudo apt install certbot python3-certbot-nginx -y

# Obtener certificado SSL gratis
sudo certbot --nginx -d tudominio.com

# Seguir instrucciones
# Certbot configura Nginx automáticamente

# Renovación automática ya está configurada
```

**Luego accede con:**
```
https://tudominio.com/admin.html
```

---

## 💰 Costos

| Servicio | Costo |
|----------|-------|
| VM e2-micro (us-central1) | **GRATIS** ✅ (Always Free) |
| 30 GB disco | **GRATIS** ✅ |
| MongoDB Atlas M0 | **GRATIS** ✅ |
| Tráfico (hasta 1GB/mes) | **GRATIS** ✅ |
| **TOTAL** | **$0/mes** ✅ |

---

## ✅ Checklist

- [ ] VM e2-micro creada en GCP
- [ ] Firewall configurado (puerto 80 y 8080)
- [ ] SSH conectado
- [ ] Java 17, Maven, Git, Nginx instalados
- [ ] Código subido a GitHub
- [ ] Código clonado en VM
- [ ] Aplicación compilada
- [ ] Servicio systemd configurado
- [ ] Aplicación corriendo (puerto 8080)
- [ ] Nginx configurado (puerto 80)
- [ ] Acceso sin puerto funcionando ✅

---

## 🎯 Resultado Final

**Antes:**
```
http://34.123.45.67:8080/admin.html  ❌ (con puerto)
```

**Después:**
```
http://34.123.45.67/admin.html  ✅ (sin puerto)
```

**¿Cómo funciona?**
```
Internet (puerto 80) → Nginx → Redirige → Tu App (puerto 8080)
```

---

## 📚 Recursos

- **GCP Free Tier:** https://cloud.google.com/free
- **Nginx Docs:** https://nginx.org/en/docs/
- **Spring Boot Docs:** https://docs.spring.io/spring-boot/

---

## 🎉 ¡Listo!

Tu aplicación está desplegada en GCP:
- ✅ Sin especificar puerto
- ✅ Always Free Tier (gratis para siempre)
- ✅ MongoDB Atlas (gratis para siempre)
- ✅ Costo total: **$0/mes**

**¡Disfruta tu app en la nube! 🚀**
