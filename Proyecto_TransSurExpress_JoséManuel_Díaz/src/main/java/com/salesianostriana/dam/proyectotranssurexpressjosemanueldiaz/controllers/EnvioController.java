package com.salesianostriana.dam.proyectotranssurexpressjosemanueldiaz.controllers;

import com.salesianostriana.dam.proyectotranssurexpressjosemanueldiaz.exceptions.CodigoEnvioDuplicadoException;
import com.salesianostriana.dam.proyectotranssurexpressjosemanueldiaz.exceptions.EnvioInvalidoException;
import com.salesianostriana.dam.proyectotranssurexpressjosemanueldiaz.modelos.Envio;
import com.salesianostriana.dam.proyectotranssurexpressjosemanueldiaz.services.EnvioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/envios")
@RequiredArgsConstructor
public class EnvioController {

    private final EnvioService service;

    @GetMapping
    public String listarEnvios(Model model) {
        model.addAttribute("envios", service.findAll());
        return "envios/lista";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        model.addAttribute("envio", new Envio());
        return "envios/formulario";
    }

    @PostMapping("/guardar")
    public String guardarEnvio(@Valid @ModelAttribute("envio") Envio envio,
                               BindingResult bindingResult,
                               Authentication auth,
                               RedirectAttributes redirectAttrs,
                               Model model) {
        if (bindingResult.hasErrors()) {
            return "envios/formulario";
        }

        boolean esNuevo = (envio.getId() == null);

        try {
            service.guardarConValidacion(envio);
        } catch (EnvioInvalidoException | CodigoEnvioDuplicadoException ex) {
            model.addAttribute("error", ex.getMessage());
            return "envios/formulario";
        }

        if (esNuevo) {
            // Crear: puede ser admin u operador
            redirectAttrs.addFlashAttribute("mensaje",
                getActor(auth) + " ha registrado el envío " + envio.getCodigo() + ".");
            redirectAttrs.addFlashAttribute("tipoMensaje", "exito");
        } else {
            // Editar: solo admin puede llegar aquí
            redirectAttrs.addFlashAttribute("mensaje",
                "El administrador ha actualizado el envío " + envio.getCodigo() + ".");
            redirectAttrs.addFlashAttribute("tipoMensaje", "edicion");
        }

        return "redirect:/envios";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model) {
        service.findById(id).ifPresent(envio -> model.addAttribute("envio", envio));
        return "envios/formulario";
    }

    @GetMapping("/borrar/{id}")
    public String borrarEnvio(@PathVariable Long id, RedirectAttributes redirectAttrs) {
        // Solo admin puede llegar aquí
        service.findById(id).ifPresent(envio -> {
            service.deleteById(id);
            redirectAttrs.addFlashAttribute("mensaje",
                "El administrador ha eliminado el envío " + envio.getCodigo() + ".");
            redirectAttrs.addFlashAttribute("tipoMensaje", "borrado");
        });
        return "redirect:/envios";
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
