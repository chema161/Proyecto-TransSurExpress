package com.salesianostriana.dam.proyectotranssurexpressjosemanueldiaz.controllers;

import com.salesianostriana.dam.proyectotranssurexpressjosemanueldiaz.modelos.EnvioVehiculo;
import com.salesianostriana.dam.proyectotranssurexpressjosemanueldiaz.services.EnvioService;
import com.salesianostriana.dam.proyectotranssurexpressjosemanueldiaz.services.EnvioVehiculoService;
import com.salesianostriana.dam.proyectotranssurexpressjosemanueldiaz.services.VehiculoService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/rutas") 
@RequiredArgsConstructor
public class EnvioVehiculoController {

    private final EnvioVehiculoService service;
    private final EnvioService envioService;
    private final VehiculoService vehiculoService;

    @GetMapping
    public String listarRutas(Model model) {
        model.addAttribute("rutas", service.findAll());
        return "rutas/lista";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        model.addAttribute("ruta", new EnvioVehiculo());
        model.addAttribute("envios", envioService.findAll());
        model.addAttribute("vehiculos", vehiculoService.findAll());
        return "rutas/formulario";
    }

    @PostMapping("/guardar")
    public String guardarRuta(@ModelAttribute("ruta") EnvioVehiculo envioVehiculo) {
        service.save(envioVehiculo);
        return "redirect:/rutas";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model) {
        service.findById(id).ifPresent(ruta -> model.addAttribute("ruta", ruta));
        model.addAttribute("envios", envioService.findAll());
        model.addAttribute("vehiculos", vehiculoService.findAll());
        return "rutas/formulario";
    }

    @GetMapping("/borrar/{id}")
    public String borrarRuta(@PathVariable Long id) {
        service.deleteById(id);
        return "redirect:/rutas";
    }
    
}