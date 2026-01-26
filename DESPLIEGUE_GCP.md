# 🚀 Guía de Despliegue en Google Cloud Platform (GCP)

## 📋 Requisitos Previos

1. **Cuenta de Google** (Gmail)
2. **MongoDB Atlas** (base de datos gratuita en la nube)
3. **Tarjeta de crédito/débito** (para verificación, NO se cobra con Free Tier)

---

## 💰 VENTAJAS DE GCP

### Créditos Gratis:
- **$300 USD en créditos** para usar en 90 días ✅
- Después: **Always Free Tier** (similar a AWS)

### Always Free Tier (Después de los $300):
- **1 VM e2-micro** (0.25-0.5 vCPU, 1GB RAM) 24/7
- **30 GB HDD** de almacenamiento
- **1 GB de tráfico** de salida al mes (a Norteamérica)
- **Snapshots:** 5 GB

### Comparación con AWS:
| Característica | AWS Free Tier | GCP Free Tier |
|----------------|---------------|---------------|
| Duración inicial | 12 meses | **$300 por 90 días** ✅ |
| VM gratis permanente | ❌ | ✅ e2-micro (forever) |
| RAM | 1 GB | 1 GB |
| Almacenamiento | 30 GB | 30 GB |
| IP estática | ❌ (~$3/mes) | ✅ Gratis si está en uso |

---

## 🗄️ PASO 1: Configurar MongoDB Atlas (10 minutos)

> **Nota:** Este paso es igual que AWS. Si ya lo hiciste, salta al Paso 2.

### 1.1 Crear Cuenta en MongoDB Atlas

1. Ve a: https://www.mongodb.com/cloud/atlas/register
2. Regístrate gratis
3. Crea un nuevo proyecto llamado "BirthdayApp"

### 1.2 Crear Cluster Gratuito

1. Click en **"Build a Database"**
2. Selecciona **"M0 FREE"** (512MB de almacenamiento)
3. Proveedor: **Google Cloud** (para mejor integración)
4. Región: **Iowa (us-central1)** o la más cercana
5. Nombre del cluster: `BirthdayAppCluster`
6. Click en **"Create"**

### 1.3 Configurar Acceso

**Usuario de Base de Datos:**
1. En "Security" → "Database Access"
2. Click **"Add New Database User"**
3. Usuario: `birthdayapp_user`
4. Password: **Genera una contraseña segura** (guárdala)
5. Privilegios: **"Read and write to any database"**
6. Click **"Add User"**

**IP Whitelist:**
1. En "Security" → "Network Access"
2. Click **"Add IP Address"**
3. Selecciona **"Allow Access from Anywhere"** (0.0.0.0/0)
4. Click **"Confirm"**

### 1.4 Obtener Connection String

1. Ve a "Database" → Click **"Connect"**
2. Selecciona **"Connect your application"**
3. Driver: **Java**, Version: **4.3 or later**
4. Copia el **Connection String**:
   ```
   mongodb+srv://birthdayapp_user:<password>@birthdayappcluster.xxxxx.mongodb.net/?retryWrites=true&w=majority
   ```
5. **Reemplaza `<password>`** con tu contraseña
6. **Agrega `/birthdayapp`** antes de `?`:
   ```
   mongodb+srv://birthdayapp_user:TU_PASSWORD@birthdayappcluster.xxxxx.mongodb.net/birthdayapp?retryWrites=true&w=majority
   ```

✅ **Guarda este Connection String**

---

## ☁️ PASO 2: Crear Cuenta en Google Cloud Platform

### 2.1 Registro

1. Ve a: https://console.cloud.google.com
2. Inicia sesión con tu cuenta de Google
3. Acepta los términos de servicio
4. **Activa la prueba gratuita** ($300 en créditos)
5. Completa información:
   - País
   - Tipo de cuenta: Individual
   - Nombre y dirección
   - **Tarjeta de crédito** (solo verificación, NO se cobra)
6. Click **"Iniciar prueba gratuita"**

### 2.2 Crear Proyecto

1. En la barra superior, click en el selector de proyectos
2. Click **"Nuevo proyecto"**
3. Nombre: `birthdayapp`
4. Click **"Crear"**
5. Espera 30 segundos
6. Selecciona el proyecto creado

---

## 💻 PASO 3: Crear Instancia de VM (Compute Engine)

### 3.1 Habilitar Compute Engine API

1. En el menú (☰), ve a **"Compute Engine"** → **"Instancias de VM"**
2. Click **"Habilitar"** (primera vez, tarda 2-3 minutos)
3. Espera a que se active la API

### 3.2 Crear VM

1. Click **"Crear instancia"**

**Configuración:**

**Nombre y región:**
- **Nombre:** `birthdayapp-server`
- **Región:** `us-central1 (Iowa)` ⭐ Capa gratuita
- **Zona:** `us-central1-a` (cualquiera funciona)

**Configuración de máquina:**
- **Serie:** `E2`
- **Tipo de máquina:** **`e2-micro`** ⭐ (2 vCPU compartidas, 1 GB RAM)
  - ✅ Elegible para capa gratuita siempre
  - Costo si excedes: ~$7/mes

**Disco de arranque:**
- Click en **"Cambiar"**
- **Sistema operativo:** `Debian` o `Ubuntu`
- **Versión:** `Debian GNU/Linux 12 (bookworm)` o `Ubuntu 22.04 LTS`
- **Tipo de disco de arranque:** `Disco persistente estándar`
- **Tamaño:** `30 GB` (máximo gratis)
- Click **"Seleccionar"**

**Identity and API access:**
- Dejar por defecto

**Firewall:**
- ✅ **Permitir tráfico HTTP**
- ✅ **Permitir tráfico HTTPS**

2. Click **"Crear"**
3. Espera 30-60 segundos hasta que aparezca ✅ verde

### 3.3 Configurar Reglas de Firewall

1. En el menú, ve a **"Red de VPC"** → **"Firewall"**
2. Click **"Crear regla de firewall"**

**Configuración:**
- **Nombre:** `allow-birthdayapp`
- **Registros:** Desactivado
- **Red:** `default`
- **Prioridad:** `1000`
- **Dirección del tráfico:** `Entrada`
- **Acción en caso de coincidencia:** `Permitir`
- **Destinos:** `Todas las instancias de la red`
- **Filtro de origen:** `Rangos de IPv4`
- **Rangos de IPv4 de origen:** `0.0.0.0/0`
- **Protocolos y puertos:**
  - ✅ **TCP:** `8080`

3. Click **"Crear"**

### 3.4 Reservar IP Estática (Opcional pero Recomendado)

1. En el menú, ve a **"Red de VPC"** → **"Direcciones IP"**
2. Click **"Reservar dirección externa estática"**
3. **Nombre:** `birthdayapp-ip`
4. **Región:** `us-central1` (la misma de tu VM)
5. **Adjuntar a:** Selecciona `birthdayapp-server`
6. Click **"Reservar"**

✅ **Esta IP es GRATIS mientras esté en uso**

### 3.5 Obtener IP Pública

1. Ve a **"Compute Engine"** → **"Instancias de VM"**
2. Anota la **IP externa** de tu VM
3. Ejemplo: `34.123.45.67`

---

## 🔐 PASO 4: Conectarse a la VM

### 4.1 Desde el Navegador (Más Fácil) ✅

1. En **"Instancias de VM"**, encuentra tu VM
2. Click en **"SSH"** (botón al lado derecho)
3. Se abre una terminal en el navegador ✅
4. Ya estás conectado!

### 4.2 Desde PowerShell (Windows)

#### Primera Opción: gcloud CLI (Recomendado)

1. **Instalar Google Cloud SDK:**
   - Descarga: https://cloud.google.com/sdk/docs/install
   - Ejecuta el instalador
   - Sigue las instrucciones

2. **Inicializar y conectar:**
```powershell
# Autenticarse
gcloud auth login

# Configurar proyecto
gcloud config set project birthdayapp

# Conectar a la VM
gcloud compute ssh birthdayapp-server --zone=us-central1-a
```

#### Segunda Opción: SSH con Clave

1. **Generar clave SSH:**
```powershell
ssh-keygen -t rsa -b 2048 -f $env:USERPROFILE\.ssh\gcp-key
```

2. **Agregar clave pública a GCP:**
   - Ve a **"Compute Engine"** → **"Metadatos"**
   - Tab **"Claves SSH"**
   - Click **"Editar"**
   - Click **"Agregar elemento"**
   - Copia el contenido de `$env:USERPROFILE\.ssh\gcp-key.pub`
   - Pega en el campo
   - Click **"Guardar"**

3. **Conectar:**
```powershell
ssh -i $env:USERPROFILE\.ssh\gcp-key TU_USUARIO@34.123.45.67
```

---

## ⚙️ PASO 5: Configurar el Servidor

### 5.1 Actualizar Sistema

```bash
sudo apt update && sudo apt upgrade -y
```

### 5.2 Instalar Java 17

**Para Debian/Ubuntu:**
```bash
sudo apt install openjdk-17-jdk -y

# Verificar
java -version
```

**Debe mostrar:** `openjdk version "17.x.x"`

### 5.3 Instalar Maven

```bash
sudo apt install maven -y

# Verificar
mvn -version
```

### 5.4 Instalar Git

```bash
sudo apt install git -y

# Verificar
git --version
```

---

## 📦 PASO 6: Desplegar la Aplicación

### 6.1 Subir Código a GitHub (Desde tu PC)

```powershell
cd "C:\Users\santi\OneDrive\Documentos\Personal Projects\BirthdayApp"

# Si no lo has hecho:
git init
git add .
git commit -m "GCP deployment ready"

# Crear repo en: https://github.com/new
git remote add origin https://github.com/TU_USUARIO/BirthdayApp.git
git branch -M main
git push -u origin main
```

### 6.2 Clonar en la VM

```bash
cd ~
git clone https://github.com/TU_USUARIO/BirthdayApp.git
cd BirthdayApp
```

### 6.3 Configurar MongoDB

```bash
# Copiar template
cp application-prod.properties.example application-prod.properties

# Editar (con nano o vim)
nano application-prod.properties
```

**Agregar tu MongoDB Connection String:**
```properties
spring.data.mongodb.uri=mongodb+srv://birthdayapp_user:TU_PASSWORD@cluster.xxxxx.mongodb.net/birthdayapp?retryWrites=true&w=majority
```

**Guardar:** `Ctrl + O` → `Enter` → `Ctrl + X`

### 6.4 Compilar

```bash
./mvnw clean package -DskipTests
```

**Debe mostrar:** `BUILD SUCCESS`

### 6.5 Ejecutar (Modo Simple para Pruebas)

```bash
nohup java -jar target/BirthdayApp-0.0.1-SNAPSHOT.jar --spring.config.location=application-prod.properties > app.log 2>&1 &

# Ver logs
tail -f app.log
```

**Para detener:** `pkill -f BirthdayApp`

---

## 🔧 PASO 7: Configurar como Servicio Systemd (Producción)

### 7.1 Crear Servicio

```bash
sudo nano /etc/systemd/system/birthdayapp.service
```

**Contenido:**
```ini
[Unit]
Description=Birthday App Spring Boot Application
After=network.target

[Service]
Type=simple
User=TU_USUARIO
WorkingDirectory=/home/TU_USUARIO/BirthdayApp
ExecStart=/usr/bin/java -jar /home/TU_USUARIO/BirthdayApp/target/BirthdayApp-0.0.1-SNAPSHOT.jar --spring.config.location=/home/TU_USUARIO/BirthdayApp/application-prod.properties
SuccessExitStatus=143
Restart=always
RestartSec=10

[Install]
WantedBy=multi-user.target
```

**Reemplaza `TU_USUARIO`** con tu usuario (ver con `whoami`)

### 7.2 Activar Servicio

```bash
# Recargar systemd
sudo systemctl daemon-reload

# Iniciar servicio
sudo systemctl start birthdayapp

# Verificar estado
sudo systemctl status birthdayapp

# Habilitar inicio automático
sudo systemctl enable birthdayapp

# Ver logs
sudo journalctl -u birthdayapp -f
```

### 7.3 Comandos Útiles

```bash
# Detener
sudo systemctl stop birthdayapp

# Reiniciar
sudo systemctl restart birthdayapp

# Ver logs
sudo journalctl -u birthdayapp -n 100
```

---

## 🌐 PASO 8: Acceder a la Aplicación

### En tu navegador:

**Con IP Pública:**
```
http://34.123.45.67:8080/admin.html
```

**Login:**
- Usuario: `admin`
- Password: `Effeta.Admin2771`

---

## 🔒 PASO 9: Configurar Nginx (Proxy Inverso - Opcional)

### 9.1 Instalar Nginx

```bash
sudo apt install nginx -y
```

### 9.2 Configurar Nginx

```bash
sudo nano /etc/nginx/sites-available/birthdayapp
```

**Contenido:**
```nginx
server {
    listen 80;
    server_name TU_IP_PUBLICA;

    location / {
        proxy_pass http://localhost:8080;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
    }
}
```

### 9.3 Activar Configuración

```bash
# Crear enlace simbólico
sudo ln -s /etc/nginx/sites-available/birthdayapp /etc/nginx/sites-enabled/

# Probar configuración
sudo nginx -t

# Reiniciar Nginx
sudo systemctl restart nginx
sudo systemctl enable nginx
```

**Ahora accede sin puerto:**
```
http://34.123.45.67/admin.html
```

---

## 🔐 PASO 10: SSL/HTTPS con Let's Encrypt (Opcional)

### Requisitos:
- Dominio propio apuntando a tu IP de GCP

### 10.1 Instalar Certbot

```bash
sudo apt install certbot python3-certbot-nginx -y
```

### 10.2 Obtener Certificado

```bash
sudo certbot --nginx -d tudominio.com -d www.tudominio.com
```

### 10.3 Renovación Automática

```bash
# Probar renovación
sudo certbot renew --dry-run

# Certbot crea un cron job automáticamente
```

---

## 📊 PASO 11: Monitoreo

### Ver Logs en Tiempo Real

```bash
# Logs de la aplicación
sudo journalctl -u birthdayapp -f

# Logs de Nginx
sudo tail -f /var/log/nginx/access.log
sudo tail -f /var/log/nginx/error.log
```

### Ver Recursos del Sistema

```bash
# Instalar htop
sudo apt install htop -y

# Ver recursos
htop
```

### Ver Estado del Servicio

```bash
sudo systemctl status birthdayapp
```

---

## 🔄 PASO 12: Actualizar Aplicación

```bash
# Conectar a la VM
gcloud compute ssh birthdayapp-server --zone=us-central1-a

# Actualizar código
cd ~/BirthdayApp
git pull origin main

# Recompilar
./mvnw clean package -DskipTests

# Reiniciar
sudo systemctl restart birthdayapp

# Verificar logs
sudo journalctl -u birthdayapp -n 50
```

---

## 💰 PASO 13: Monitorear Costos

### 13.1 Ver Créditos Gratuitos

1. En GCP Console, ve a **"Facturación"**
2. Selecciona tu cuenta de facturación
3. Ve a **"Informes"**
4. Filtra por proyecto: `birthdayapp`

### 13.2 Configurar Alertas de Presupuesto

1. En **"Facturación"** → **"Presupuestos y alertas"**
2. Click **"Crear presupuesto"**
3. **Nombre:** `Alerta BirthdayApp`
4. **Presupuesto:** `$5 USD/mes`
5. **Alertas:** 50%, 90%, 100%
6. **Email:** Tu correo
7. Click **"Finalizar"**

### 13.3 Costos Estimados (Después de $300)

| Servicio | Costo |
|----------|-------|
| VM e2-micro (us-central1) | **GRATIS** ✅ (Always Free) |
| 30 GB disco estándar | **GRATIS** ✅ (Always Free) |
| IP estática (en uso) | **GRATIS** ✅ |
| Tráfico salida (hasta 1GB) | **GRATIS** ✅ |
| **TOTAL** | **$0/mes** ✅ |

**Excesos posibles:**
- Tráfico > 1GB: ~$0.12/GB
- Snapshots > 5GB: ~$0.026/GB/mes

---

## 🛡️ PASO 14: Seguridad

### 14.1 Firewall de GCP

Ya configurado en Paso 3.3, pero verifica:
- Puerto 22 (SSH): Solo para administración
- Puerto 80 (HTTP): Público
- Puerto 443 (HTTPS): Público
- Puerto 8080: Solo si NO usas Nginx

### 14.2 Actualizar Contraseña Admin

Edita `SecurityConfig.java` y recompila.

### 14.3 Restringir SSH

```bash
# Solo permitir SSH desde tu IP
# En GCP Console → Red de VPC → Firewall
# Edita regla "default-allow-ssh"
# Cambiar "0.0.0.0/0" a "TU_IP/32"
```

### 14.4 Habilitar Firewall en el SO

```bash
sudo apt install ufw -y
sudo ufw allow 22/tcp
sudo ufw allow 80/tcp
sudo ufw allow 443/tcp
sudo ufw allow 8080/tcp
sudo ufw enable
sudo ufw status
```

---

## 🐛 SOLUCIÓN DE PROBLEMAS

### Error: "Cannot connect to MongoDB"
```bash
# Verificar connection string
cat application-prod.properties | grep mongodb

# Probar conexión
mongosh "TU_CONNECTION_STRING"
```

### Error: "Port 8080 already in use"
```bash
# Encontrar proceso
sudo lsof -i :8080

# Matar proceso
sudo kill -9 PID

# O reiniciar servicio
sudo systemctl restart birthdayapp
```

### Error: "Permission denied"
```bash
# Verificar permisos
ls -la ~/BirthdayApp

# Cambiar dueño si es necesario
sudo chown -R $USER:$USER ~/BirthdayApp
```

### La VM es lenta
```bash
# Ver recursos
htop

# Si RAM está al 100%, considera:
# - Aumentar a e2-small (2GB RAM) ~$13/mes
# - Optimizar aplicación
```

### No puedo acceder desde el navegador
```bash
# Verificar que el servicio está corriendo
sudo systemctl status birthdayapp

# Verificar reglas de firewall en GCP Console
# Verificar Security Group permite puerto 8080

# Probar localmente en la VM
curl http://localhost:8080/admin.html
```

---

## ✅ CHECKLIST COMPLETO

### MongoDB Atlas:
- [ ] Cuenta creada
- [ ] Cluster M0 FREE creado
- [ ] Usuario de BD creado
- [ ] IP whitelist configurado (0.0.0.0/0)
- [ ] Connection String obtenido

### Google Cloud Platform:
- [ ] Cuenta GCP creada
- [ ] $300 en créditos activados
- [ ] Proyecto "birthdayapp" creado
- [ ] Compute Engine habilitado

### VM Configuration:
- [ ] VM e2-micro creada en us-central1
- [ ] Firewall configurado (puerto 8080)
- [ ] IP estática reservada (opcional)
- [ ] SSH funcionando

### Servidor:
- [ ] Java 17 instalado
- [ ] Maven instalado
- [ ] Git instalado
- [ ] Código clonado
- [ ] application-prod.properties configurado
- [ ] Aplicación compilada
- [ ] Servicio systemd configurado
- [ ] Aplicación iniciando automáticamente

### Verificación:
- [ ] Aplicación accesible desde navegador
- [ ] Login funcionando
- [ ] Datos guardándose en MongoDB
- [ ] Invitados creándose correctamente

---

## 📚 RECURSOS ADICIONALES

- **GCP Free Tier:** https://cloud.google.com/free
- **GCP Docs:** https://cloud.google.com/docs
- **Compute Engine:** https://cloud.google.com/compute/docs
- **MongoDB Atlas:** https://www.mongodb.com/docs/atlas/
- **Spring Boot Docs:** https://docs.spring.io/spring-boot/docs/current/reference/html/

---

## 🎉 ¡FELICIDADES!

Tu aplicación BirthdayApp está ahora desplegada en Google Cloud Platform con:

- ✅ **$300 USD en créditos** para 90 días
- ✅ **Always Free Tier** después (VM e2-micro gratis para siempre)
- ✅ MongoDB Atlas (gratis para siempre)
- ✅ IP estática gratis
- ✅ Costo total: **$0/mes** indefinidamente

**Tu aplicación está en:**
```
http://TU_IP:8080/admin.html
```

**Usuario:** admin  
**Password:** Effeta.Admin2771

---

**¿Preguntas?** Revisa la sección de Troubleshooting o consulta la documentación de GCP.

**¡Disfruta tu app en la nube! 🚀**
