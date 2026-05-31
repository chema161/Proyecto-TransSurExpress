package com.salesianostriana.dam.proyectotranssurexpressjosemanueldiaz.controllers;

import com.salesianostriana.dam.proyectotranssurexpressjosemanueldiaz.modelos.Conductor;
import com.salesianostriana.dam.proyectotranssurexpressjosemanueldiaz.services.ConductorService;
import com.salesianostriana.dam.proyectotranssurexpressjosemanueldiaz.services.VehiculoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/conductores")
@RequiredArgsConstructor
public class ConductorController {

    private final ConductorService service;
    private final VehiculoService vehiculoService;

    @GetMapping
    public String listarConductores(Model model) {
        model.addAttribute("conductores", service.findAll());
        return "conductores/lista";
    }

    @GetMapping("/nuevo")
    public String nuevoConductor(Model model) {
        model.addAttribute("conductor", new Conductor());
        model.addAttribute("vehiculos", vehiculoService.findAll());
        return "conductores/formulario";
    }

    @PostMapping("/guardar")
    public String guardarConductor(@Valid @ModelAttribute("conductor") Conductor conductor,
                                   BindingResult bindingResult,
                                   Authentication auth,
                                   RedirectAttributes redirectAttrs,
                                   Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("vehiculos", vehiculoService.findAll());
            return "conductores/formulario";
        }

        boolean esNuevo = (conductor.getId() == null);
        service.save(conductor);

        if (esNuevo) {
            // Crear: puede ser admin u operador
            redirectAttrs.addFlashAttribute("mensaje",
                getActor(auth) + " ha dado de alta al conductor " + conductor.getNombre() + ".");
            redirectAttrs.addFlashAttribute("tipoMensaje", "exito");
        } else {
            // Editar: solo admin puede llegar aquí
            redirectAttrs.addFlashAttribute("mensaje",
                "El administrador ha actualizado la ficha de " + conductor.getNombre() + ".");
            redirectAttrs.addFlashAttribute("tipoMensaje", "edicion");
        }

        return "redirect:/conductores";
    }

    @GetMapping("/editar/{id}")
    public String editarConductor(@PathVariable Long id, Model model) {
        Optional<Conductor> opt = service.findById(id);
        if (opt.isPresent()) {
            model.addAttribute("conductor", opt.get());
        } else {
            return "redirect:/conductores";
        }
        model.addAttribute("vehiculos", vehiculoService.findAll());
        return "conductores/formulario";
    }

    @GetMapping("/borrar/{id}")
    public String borrarConductor(@PathVariable Long id, RedirectAttributes redirectAttrs) {
        // Solo el admin puede llegar aquí
        service.findById(id).ifPresent(c -> {
            service.deleteById(id);
            redirectAttrs.addFlashAttribute("mensaje",
                "El administrador ha dado de baja al conductor " + c.getNombre() + ".");
            redirectAttrs.addFlashAttribute("tipoMensaje", "borrado");
        });
        return "redirect:/conductores";
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
