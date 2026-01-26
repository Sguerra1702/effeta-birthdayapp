#!/bin/bash

# 🚀 Script de Despliegue Automático para GCP
# Birthday App - Google Cloud Platform Deployment

echo "======================================"
echo "🚀 Birthday App - GCP Deployment"
echo "======================================"
echo ""

# Colores para output
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
NC='\033[0m' # No Color

# Variables
APP_NAME="BirthdayApp"
JAR_NAME="BirthdayApp-0.0.1-SNAPSHOT.jar"
APP_DIR="$HOME/BirthdayApp"
SERVICE_NAME="birthdayapp"

# Función para imprimir con color
print_info() {
    echo -e "${GREEN}[INFO]${NC} $1"
}

print_warn() {
    echo -e "${YELLOW}[WARN]${NC} $1"
}

print_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

# Verificar que estamos en el directorio correcto
if [ ! -f "pom.xml" ]; then
    print_error "No se encontró pom.xml. Asegúrate de estar en el directorio raíz del proyecto."
    exit 1
fi

# Paso 1: Git Pull
print_info "Paso 1/7: Actualizando código desde Git..."
git pull origin main
if [ $? -ne 0 ]; then
    print_warn "Git pull falló. Continuando de todas formas..."
fi
echo ""

# Paso 2: Compilar aplicación
print_info "Paso 2/7: Compilando aplicación con Maven..."
./mvnw clean package -DskipTests
if [ $? -ne 0 ]; then
    print_error "La compilación falló. Revisa los errores arriba."
    exit 1
fi
echo ""

# Paso 3: Verificar que el JAR existe
print_info "Paso 3/7: Verificando JAR compilado..."
if [ ! -f "target/$JAR_NAME" ]; then
    print_error "No se encontró el JAR compilado en target/$JAR_NAME"
    exit 1
fi
print_info "JAR encontrado: target/$JAR_NAME"
echo ""

# Paso 4: Detener servicio actual
print_info "Paso 4/7: Deteniendo servicio actual..."
sudo systemctl stop $SERVICE_NAME 2>/dev/null
if [ $? -eq 0 ]; then
    print_info "Servicio detenido correctamente"
else
    print_warn "El servicio no estaba corriendo o no existe aún"
fi
echo ""

# Paso 5: Crear/Actualizar servicio systemd
print_info "Paso 5/7: Configurando servicio systemd..."

# Obtener usuario actual
CURRENT_USER=$(whoami)

# Crear archivo de servicio
sudo tee /etc/systemd/system/$SERVICE_NAME.service > /dev/null << EOF
[Unit]
Description=Birthday App Spring Boot Application
After=network.target

[Service]
Type=simple
User=$CURRENT_USER
WorkingDirectory=$APP_DIR
ExecStart=/usr/bin/java -jar $APP_DIR/target/$JAR_NAME
SuccessExitStatus=143
Restart=always
RestartSec=10

[Install]
WantedBy=multi-user.target
EOF

if [ $? -eq 0 ]; then
    print_info "Archivo de servicio creado/actualizado"
else
    print_error "Error al crear archivo de servicio"
    exit 1
fi
echo ""

# Paso 6: Recargar systemd y iniciar servicio
print_info "Paso 6/7: Recargando systemd e iniciando servicio..."
sudo systemctl daemon-reload
sudo systemctl start $SERVICE_NAME
sudo systemctl enable $SERVICE_NAME

if [ $? -eq 0 ]; then
    print_info "Servicio iniciado correctamente"
else
    print_error "Error al iniciar el servicio"
    exit 1
fi
echo ""

# Paso 7: Verificar estado
print_info "Paso 7/7: Verificando estado del servicio..."
sleep 3  # Esperar 3 segundos para que arranque

sudo systemctl status $SERVICE_NAME --no-pager -l

echo ""
echo "======================================"
echo "✅ Despliegue completado"
echo "======================================"
echo ""
print_info "La aplicación debería estar corriendo en:"
echo "   http://localhost:8080/admin.html"
echo ""
print_info "Para ver logs en tiempo real:"
echo "   sudo journalctl -u $SERVICE_NAME -f"
echo ""
print_info "Para detener el servicio:"
echo "   sudo systemctl stop $SERVICE_NAME"
echo ""
print_info "Para reiniciar el servicio:"
echo "   sudo systemctl restart $SERVICE_NAME"
echo ""
