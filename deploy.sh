# 🚀 Script de Despliegue Rápido para AWS EC2

# Este archivo debe ejecutarse en el servidor EC2 después de clonar el repositorio

echo "🎉 Iniciando despliegue de BirthdayApp..."

# Variables (editar según tu configuración)
APP_NAME="birthdayapp"
APP_DIR="/home/ec2-user/BirthdayApp"
JAR_FILE="target/BirthdayApp-0.0.1-SNAPSHOT.jar"

# Colores para output
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
NC='\033[0m' # No Color

# Función para imprimir mensajes
print_message() {
    echo -e "${GREEN}[✓]${NC} $1"
}

print_warning() {
    echo -e "${YELLOW}[!]${NC} $1"
}

print_error() {
    echo -e "${RED}[✗]${NC} $1"
}

# Verificar que estamos en el directorio correcto
if [ ! -f "pom.xml" ]; then
    print_error "No se encuentra pom.xml. ¿Estás en el directorio correcto?"
    exit 1
fi

print_message "Directorio de trabajo: $(pwd)"

# Actualizar código desde Git
print_message "Actualizando código desde Git..."
git pull origin main

# Compilar aplicación
print_message "Compilando aplicación..."
./mvnw clean package -DskipTests

if [ $? -ne 0 ]; then
    print_error "Error al compilar la aplicación"
    exit 1
fi

print_message "Compilación exitosa"

# Detener servicio si existe
print_message "Deteniendo servicio anterior..."
sudo systemctl stop $APP_NAME 2>/dev/null || print_warning "Servicio no estaba corriendo"

# Crear servicio systemd si no existe
if [ ! -f "/etc/systemd/system/$APP_NAME.service" ]; then
    print_message "Creando servicio systemd..."
    
    sudo tee /etc/systemd/system/$APP_NAME.service > /dev/null <<EOF
[Unit]
Description=Birthday App Spring Boot Application
After=network.target

[Service]
User=ec2-user
WorkingDirectory=$APP_DIR
ExecStart=/usr/bin/java -jar $APP_DIR/$JAR_FILE
SuccessExitStatus=143
Restart=always
RestartSec=10

[Install]
WantedBy=multi-user.target
EOF

    sudo systemctl daemon-reload
    print_message "Servicio creado"
fi

# Iniciar servicio
print_message "Iniciando servicio..."
sudo systemctl start $APP_NAME
sudo systemctl enable $APP_NAME

# Esperar 5 segundos
sleep 5

# Verificar estado
if sudo systemctl is-active --quiet $APP_NAME; then
    print_message "✅ Aplicación desplegada exitosamente!"
    print_message "Estado del servicio:"
    sudo systemctl status $APP_NAME --no-pager -l
    echo ""
    print_message "Puedes ver los logs con: sudo journalctl -u $APP_NAME -f"
else
    print_error "❌ Error al iniciar la aplicación"
    print_warning "Revisa los logs con: sudo journalctl -u $APP_NAME -n 50"
    exit 1
fi

echo ""
print_message "🎉 Despliegue completado!"
print_message "Accede a tu aplicación en: http://$(curl -s ifconfig.me):8080/admin.html"
