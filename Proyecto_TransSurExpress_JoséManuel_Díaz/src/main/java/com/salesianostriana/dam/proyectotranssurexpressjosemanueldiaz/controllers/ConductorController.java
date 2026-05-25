package com.salesianostriana.dam.proyectotranssurexpressjosemanueldiaz.controllers;

import com.salesianostriana.dam.proyectotranssurexpressjosemanueldiaz.modelos.Conductor;
import com.salesianostriana.dam.proyectotranssurexpressjosemanueldiaz.services.ConductorService;
import com.salesianostriana.dam.proyectotranssurexpressjosemanueldiaz.services.VehiculoService;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

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

    @GetMapping("/test")
    @ResponseBody
    public String test() {
        return "<h1>¡EL CONTROLADOR FUNCIONA!</h1> <p>Si ves esto, el problema es el HTML.</p>";
    }
    
    @GetMapping("/nuevo")
    public String nuevoConductor(Model model) {
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
    public String editarConductor(@PathVariable Long id, Model model) {     
        Optional<Conductor> conductorOpt = service.findById(id);
        
        if (conductorOpt.isPresent()) {
            model.addAttribute("conductor", conductorOpt.get());
        } else {
            return "redirect:/conductores"; 
        } 
        model.addAttribute("vehiculos", vehiculoService.findAll());
        return "conductores/formulario";
    }  
    
    @GetMapping("/borrar/{id}")
    public String borrarConductor(@PathVariable Long id) {
        service.deleteById(id);
        return "redirect:/conductores";
    }
    
}