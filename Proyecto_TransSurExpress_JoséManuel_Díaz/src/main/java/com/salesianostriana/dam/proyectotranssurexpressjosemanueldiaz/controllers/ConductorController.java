package com.salesianostriana.dam.proyectotranssurexpressjosemanueldiaz.controllers;

import com.salesianostriana.dam.proyectotranssurexpressjosemanueldiaz.modelos.Conductor;
import com.salesianostriana.dam.proyectotranssurexpressjosemanueldiaz.services.ConductorService;
import com.salesianostriana.dam.proyectotranssurexpressjosemanueldiaz.services.VehiculoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
    public String mostrarFormularioNuevo(Model model) {
        model.addAttribute("conductor", new Conductor());
        model.addAttribute("vehiculos", vehiculoService.findAll());
        return "conductores/formulario";
    }

    @PostMapping("/guardar")
    public String guardarConductor(@ModelAttribute("conductor") Conductor conductor) {
        service.save(conductor);
        return "redirect:/conductores";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model) {
        service.findById(id).ifPresent(conductor -> model.addAttribute("conductor", conductor));
        model.addAttribute("vehiculos", vehiculoService.findAll());
        return "conductores/formulario";
    }

    @GetMapping("/borrar/{id}")
    public String borrarConductor(@PathVariable Long id) {
        service.deleteById(id);
        return "redirect:/conductores";
    }
    
}