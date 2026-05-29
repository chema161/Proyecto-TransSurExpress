package com.salesianostriana.dam.proyectotranssurexpressjosemanueldiaz.controllers;

import com.salesianostriana.dam.proyectotranssurexpressjosemanueldiaz.modelos.Conductor;
import com.salesianostriana.dam.proyectotranssurexpressjosemanueldiaz.services.ConductorService;
import com.salesianostriana.dam.proyectotranssurexpressjosemanueldiaz.services.VehiculoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

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
                                   BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("vehiculos", vehiculoService.findAll());
            return "conductores/formulario";
        }
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
