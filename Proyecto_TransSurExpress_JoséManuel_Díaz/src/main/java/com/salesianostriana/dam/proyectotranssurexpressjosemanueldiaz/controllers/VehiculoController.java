package com.salesianostriana.dam.proyectotranssurexpressjosemanueldiaz.controllers;

import com.salesianostriana.dam.proyectotranssurexpressjosemanueldiaz.modelos.Vehiculo;
import com.salesianostriana.dam.proyectotranssurexpressjosemanueldiaz.services.VehiculoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/vehiculos")
@RequiredArgsConstructor
public class VehiculoController {

    private final VehiculoService service;

    @GetMapping
    public String listarVehiculos(Model model) {
        model.addAttribute("vehiculos", service.findAll());
        return "vehiculos/lista";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        model.addAttribute("vehiculo", new Vehiculo());
        return "vehiculos/formulario";
    }

    @PostMapping("/guardar")
    public String guardarVehiculo(@Valid @ModelAttribute("vehiculo") Vehiculo vehiculo,
                                  BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            // Volvemos al formulario mostrando los errores de validación
            return "vehiculos/formulario";
        }
        service.save(vehiculo);
        return "redirect:/vehiculos";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model) {
        service.findById(id).ifPresent(vehiculo -> model.addAttribute("vehiculo", vehiculo));
        return "vehiculos/formulario";
    }

    @GetMapping("/borrar/{id}")
    public String borrarVehiculo(@PathVariable Long id) {
        service.deleteById(id);
        return "redirect:/vehiculos";
    }
}
