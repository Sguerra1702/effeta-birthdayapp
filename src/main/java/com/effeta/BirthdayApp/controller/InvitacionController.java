package com.effeta.BirthdayApp.controller;

import com.effeta.BirthdayApp.model.Fiesta;
import com.effeta.BirthdayApp.model.Invitado;
import com.effeta.BirthdayApp.service.FiestaService;
import com.effeta.BirthdayApp.service.InvitacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class InvitacionController {

    @Autowired
    private InvitacionService invitacionService;

    @Autowired
    private FiestaService fiestaService;

    /**
     * GET /api/invitacion?id=abc123
     * Obtiene la información de un invitado por su ID
     */
    @GetMapping("/invitacion")
    public ResponseEntity<?> obtenerInvitacion(@RequestParam String id) {
        Invitado invitado = invitacionService.obtenerInvitado(id);
        
        if (invitado == null) {
            return ResponseEntity.status(404)
                    .body(Map.of("error", "Invitación no válida"));
        }
        
        return ResponseEntity.ok(invitado);
    }

    /**
     * POST /api/confirmar?id=abc123
     * Confirma la asistencia de un invitado
     */
    @PostMapping("/confirmar")
    public ResponseEntity<?> confirmarAsistencia(@RequestParam String id) {
        boolean confirmado = invitacionService.confirmarAsistencia(id);
        
        if (!confirmado) {
            return ResponseEntity.status(404)
                    .body(Map.of("error", "Invitado no encontrado"));
        }
        
        return ResponseEntity.ok(Map.of("ok", true, "mensaje", "¡Asistencia confirmada! 🎉"));
    }

    /**
     * GET /api/invitacion/descargar?id=abc123
     * Descarga la invitación personalizada del invitado
     */
    @GetMapping("/invitacion/descargar")
    public ResponseEntity<Resource> descargarInvitacion(@RequestParam String id) {
        try {
            Invitado invitado = invitacionService.obtenerInvitado(id);
            
            if (invitado == null) {
                return ResponseEntity.status(404).build();
            }
            
            // Generar el nombre del archivo según el nombre del invitado
            String nombreArchivo = invitacionService.generarNombreArchivoInvitacion(invitado.getNombre());
            
            // Cargar el archivo desde static/invitaciones/
            Resource resource = new ClassPathResource("static/invitaciones/" + nombreArchivo);
            
            if (!resource.exists()) {
                return ResponseEntity.status(404).build();
            }
            
            // Configurar la respuesta para descarga
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + nombreArchivo + "\"")
                    .body(resource);
                    
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    /**
     * POST /api/invitado
     * Crea un nuevo invitado (para administración)
     */
    @PostMapping("/invitado")
    public ResponseEntity<Invitado> crearInvitado(@RequestBody Map<String, String> body) {
        String nombre = body.get("nombre");
        String telefono = body.get("telefono");
        String fiestaId = body.get("fiestaId");
        
        if (nombre == null || nombre.trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        
        if (telefono == null || telefono.trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        // Si no se especifica fiesta, usar la primera disponible
        if (fiestaId == null || fiestaId.trim().isEmpty()) {
            fiestaId = fiestaService.obtenerFiestaDefault().getId();
        }
        
        Invitado invitado = new Invitado(null, nombre, telefono, fiestaId, false);
        invitado = invitacionService.crearInvitado(invitado);
        return ResponseEntity.ok(invitado);
    }

    /**
     * GET /api/invitados
     * Obtiene todos los invitados (para administración)
     */
    @GetMapping("/invitados")
    public ResponseEntity<List<Invitado>> obtenerTodosInvitados() {
        return ResponseEntity.ok(invitacionService.obtenerTodosInvitados());
    }

    /**
     * GET /api/invitados/fiesta/{fiestaId}
     * Obtiene invitados por fiesta
     */
    @GetMapping("/invitados/fiesta/{fiestaId}")
    public ResponseEntity<List<Invitado>> obtenerInvitadosPorFiesta(@PathVariable String fiestaId) {
        return ResponseEntity.ok(invitacionService.obtenerInvitadosPorFiesta(fiestaId));
    }

    // === ENDPOINTS DE FIESTAS ===

    /**
     * POST /api/fiesta
     * Crea una nueva fiesta
     */
    @PostMapping("/fiesta")
    public ResponseEntity<Fiesta> crearFiesta(@RequestBody Map<String, String> body) {
        String nombre = body.get("nombre");
        String fecha = body.get("fecha");
        String hora = body.get("hora");
        String lugar = body.get("lugar");
        String codigoVestimenta = body.get("codigoVestimenta");
        String anfitrion = body.get("anfitrion");

        if (nombre == null || nombre.trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        Fiesta fiesta = new Fiesta(null, nombre, fecha, hora, lugar, codigoVestimenta, anfitrion);
        fiesta = fiestaService.crearFiesta(fiesta);
        return ResponseEntity.ok(fiesta);
    }

    /**
     * GET /api/fiestas
     * Obtiene todas las fiestas
     */
    @GetMapping("/fiestas")
    public ResponseEntity<List<Fiesta>> obtenerTodasFiestas() {
        return ResponseEntity.ok(fiestaService.obtenerTodasFiestas());
    }

    /**
     * GET /api/fiesta/{id}
     * Obtiene una fiesta por ID
     */
    @GetMapping("/fiesta/{id}")
    public ResponseEntity<?> obtenerFiesta(@PathVariable String id) {
        Fiesta fiesta = fiestaService.obtenerFiesta(id);
        if (fiesta == null) {
            return ResponseEntity.status(404)
                    .body(Map.of("error", "Fiesta no encontrada"));
        }
        return ResponseEntity.ok(fiesta);
    }

    /**
     * DELETE /api/fiesta/{id}
     * Elimina una fiesta
     */
    @DeleteMapping("/fiesta/{id}")
    public ResponseEntity<?> eliminarFiesta(@PathVariable String id) {
        boolean eliminado = fiestaService.eliminarFiesta(id);
        if (!eliminado) {
            return ResponseEntity.status(404)
                    .body(Map.of("error", "Fiesta no encontrada"));
        }
        return ResponseEntity.ok(Map.of("ok", true, "mensaje", "Fiesta eliminada"));
    }
}
