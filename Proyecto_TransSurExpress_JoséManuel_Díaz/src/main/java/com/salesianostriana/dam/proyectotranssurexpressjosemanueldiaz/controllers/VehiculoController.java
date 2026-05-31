package com.salesianostriana.dam.proyectotranssurexpressjosemanueldiaz.controllers;

import com.salesianostriana.dam.proyectotranssurexpressjosemanueldiaz.modelos.Vehiculo;
import com.salesianostriana.dam.proyectotranssurexpressjosemanueldiaz.services.VehiculoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/vehiculos")
@RequiredArgsConstructor
public class VehiculoController {

    private final VehiculoService service;

    @GetMapping
    public String listarVehiculos(Model model, Authentication auth) {
        var vehiculos = esAdmin(auth) ? service.findAllIncluyendoInactivos() : service.findAll();
        model.addAttribute("vehiculos", vehiculos);
        model.addAttribute("capacidadesDisponibles", service.calcularCapacidadesDisponibles(vehiculos));
        return "vehiculos/lista";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        model.addAttribute("vehiculo", new Vehiculo());
        return "vehiculos/formulario";
    }

    @PostMapping("/guardar")
    public String guardarVehiculo(@Valid @ModelAttribute("vehiculo") Vehiculo vehiculo,
                                  BindingResult bindingResult,
                                  Authentication auth,
                                  RedirectAttributes redirectAttrs) {
        if (bindingResult.hasErrors()) {
            return "vehiculos/formulario";
        }

        boolean esNuevo = (vehiculo.getId() == null);
        service.save(vehiculo);

        if (esNuevo) {
            // Crear: puede ser admin u operador
            redirectAttrs.addFlashAttribute("mensaje",
                getActor(auth) + " ha registrado el vehículo " + vehiculo.getMatricula() + " correctamente.");
            redirectAttrs.addFlashAttribute("tipoMensaje", "exito");
        } else {
            // Editar: solo admin puede llegar aquí (bloqueado por SecurityConfig)
            redirectAttrs.addFlashAttribute("mensaje",
                "El administrador ha actualizado los datos del vehículo " + vehiculo.getMatricula() + ".");
            redirectAttrs.addFlashAttribute("tipoMensaje", "edicion");
        }

        return "redirect:/vehiculos";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model) {
        service.findById(id).ifPresent(v -> model.addAttribute("vehiculo", v));
        return "vehiculos/formulario";
    }

    @GetMapping("/borrar/{id}")
    public String borrarVehiculo(@PathVariable Long id, RedirectAttributes redirectAttrs) {
        // Solo admin puede llegar aquí (bloqueado por SecurityConfig)
        service.findById(id).ifPresent(v -> {
            service.deleteById(id);
            redirectAttrs.addFlashAttribute("mensaje",
                "El administrador ha eliminado el vehículo " + v.getMatricula() + ".");
            redirectAttrs.addFlashAttribute("tipoMensaje", "borrado");
        });
        return "redirect:/vehiculos";
    }

    // Solo necesario para el crear, ya que ambos roles pueden hacerlo
    private String getActor(Authentication auth) {
        return auth.getAuthorities().stream()
            .findFirst()
            .map(a -> {
                String rol = a.getAuthority().replace("ROLE_", "").toLowerCase();
                return rol.equals("administrador") ? "El administrador" : "El operador";
            })
            .orElse("El usuario");
    }

    private boolean esAdmin(Authentication auth) {
        return auth != null && auth.getAuthorities().stream()
            .anyMatch(a -> a.getAuthority().equals("ROLE_ADMINISTRADOR"));
    }
}
