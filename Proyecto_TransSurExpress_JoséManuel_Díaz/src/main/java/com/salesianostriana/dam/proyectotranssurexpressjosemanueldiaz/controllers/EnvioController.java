package com.salesianostriana.dam.proyectotranssurexpressjosemanueldiaz.controllers;

import com.salesianostriana.dam.proyectotranssurexpressjosemanueldiaz.modelos.Envio;
import com.salesianostriana.dam.proyectotranssurexpressjosemanueldiaz.services.EnvioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
    public String guardarEnvio(@ModelAttribute("envio") Envio envio) {
        service.save(envio);
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