package com.salesianostriana.dam.proyectotranssurexpressjosemanueldiaz.controllers;

import com.salesianostriana.dam.proyectotranssurexpressjosemanueldiaz.exceptions.CodigoEnvioDuplicadoException;
import com.salesianostriana.dam.proyectotranssurexpressjosemanueldiaz.modelos.Envio;
import com.salesianostriana.dam.proyectotranssurexpressjosemanueldiaz.services.EnvioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

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
                               BindingResult bindingResult, Model model) {
        // 1. Errores de validación de campos (@Valid)
        if (bindingResult.hasErrors()) {
            return "envios/formulario";
        }

        try {
            service.guardarConValidacion(envio);
        } catch (CodigoEnvioDuplicadoException ex) {
            model.addAttribute("error", ex.getMessage());
            return "envios/formulario";
        }

        return "redirect:/envios";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model) {
        service.findById(id).ifPresent(envio -> model.addAttribute("envio", envio));
        return "envios/formulario";
    }

    @GetMapping("/borrar/{id}")
    public String borrarEnvio(@PathVariable Long id) {
        service.deleteById(id);
        return "redirect:/envios";
    }
}
