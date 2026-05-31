package com.salesianostriana.dam.proyectotranssurexpressjosemanueldiaz.controllers;

import com.salesianostriana.dam.proyectotranssurexpressjosemanueldiaz.exceptions.*;
import com.salesianostriana.dam.proyectotranssurexpressjosemanueldiaz.modelos.EnvioVehiculo;
import com.salesianostriana.dam.proyectotranssurexpressjosemanueldiaz.services.EnvioService;
import com.salesianostriana.dam.proyectotranssurexpressjosemanueldiaz.services.OperacionService;
import com.salesianostriana.dam.proyectotranssurexpressjosemanueldiaz.services.VehiculoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/operaciones")
public class OperacionController {

    @Autowired private OperacionService operacionService;
    @Autowired private EnvioService envioService;
    @Autowired private VehiculoService vehiculoService;

    @GetMapping
    public String listarOperaciones(Model model) {
        model.addAttribute("operaciones", operacionService.findAll());
        return "operaciones/lista";
    }

    @GetMapping("/nueva")
    public String nuevaOperacion(Model model) {
        model.addAttribute("operacion", new EnvioVehiculo());
        model.addAttribute("envios", envioService.findAll());
        model.addAttribute("vehiculos", vehiculoService.findAll());
        return "operaciones/formulario";
    }

    @PostMapping("/guardar")
    public String guardarOperacion(@Valid @ModelAttribute("operacion") EnvioVehiculo op,
                                   BindingResult bindingResult,
                                   Authentication auth,
                                   RedirectAttributes redirectAttrs,
                                   Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("envios", envioService.findAll());
            model.addAttribute("vehiculos", vehiculoService.findAll());
            return "operaciones/formulario";
        }

        boolean esNueva = (op.getId() == null);

        try {
            // Pasamos el nombre de usuario para que quede registrado en el historial
            operacionService.planificarOperacion(op, auth.getName());
        } catch (EnvioInvalidoException | PesoExcedidoException
                 | AsignacionInvalidaException | ConductorOcupadoException ex) {
            model.addAttribute("error", ex.getMessage());
            model.addAttribute("operacion", op);
            model.addAttribute("envios", envioService.findAll());
            model.addAttribute("vehiculos", vehiculoService.findAll());
            return "operaciones/formulario";
        }

        String codigoEnvio = op.getEnvio() != null ? op.getEnvio().getCodigo() : "desconocido";

        if (esNueva) {
            redirectAttrs.addFlashAttribute("mensaje",
                getActor(auth) + " ha planificado una nueva operación para el envío " + codigoEnvio + ".");
            redirectAttrs.addFlashAttribute("tipoMensaje", "exito");
        } else {
            redirectAttrs.addFlashAttribute("mensaje",
                "El administrador ha modificado la operación del envío " + codigoEnvio + ".");
            redirectAttrs.addFlashAttribute("tipoMensaje", "edicion");
        }

        return "redirect:/operaciones";
    }

    @GetMapping("/editar/{id}")
    public String editarOperacion(@PathVariable Long id, Model model) {
        operacionService.findById(id).ifPresentOrElse(
            op -> model.addAttribute("operacion", op),
            () -> model.addAttribute("operacion", new EnvioVehiculo())
        );
        model.addAttribute("envios", envioService.findAll());
        model.addAttribute("vehiculos", vehiculoService.findAll());
        return "operaciones/formulario";
    }

    @GetMapping("/borrar/{id}")
    public String borrarOperacion(@PathVariable Long id, RedirectAttributes redirectAttrs) {
        operacionService.findById(id).ifPresent(op -> {
            String codigoEnvio = op.getEnvio() != null ? op.getEnvio().getCodigo() : "desconocido";
            operacionService.deleteById(id);
            redirectAttrs.addFlashAttribute("mensaje",
                "El administrador ha eliminado la operación del envío " + codigoEnvio + ".");
            redirectAttrs.addFlashAttribute("tipoMensaje", "borrado");
        });
        return "redirect:/operaciones";
    }

    @GetMapping("/historial/{envioId}")
    public String verHistorialEnvio(@PathVariable Long envioId, Model model) {
        model.addAttribute("historial", operacionService.obtenerHistorialPorEnvio(envioId));
        model.addAttribute("envioId", envioId);
        return "operaciones/historial_envio";
    }

    /** Nuevo endpoint: muestra el historial de cambios de estado de una operación concreta */
    @GetMapping("/{id}/estados")
    public String verHistorialEstados(@PathVariable Long id, Model model) {
        operacionService.findById(id).ifPresent(op -> model.addAttribute("operacion", op));
        model.addAttribute("historialEstados", operacionService.obtenerHistorialEstadosPorOperacion(id));
        return "operaciones/historial_estados";
    }

    private String getActor(Authentication auth) {
        return auth.getAuthorities().stream()
            .findFirst()
            .map(a -> {
                String rol = a.getAuthority().replace("ROLE_", "").toLowerCase();
                return rol.equals("administrador") ? "El administrador" : "El operador";
            })
            .orElse("El usuario");
    }
}
