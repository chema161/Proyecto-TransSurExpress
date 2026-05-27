package com.salesianostriana.dam.proyectotranssurexpressjosemanueldiaz.controllers;

import com.salesianostriana.dam.proyectotranssurexpressjosemanueldiaz.exceptions.*;
import com.salesianostriana.dam.proyectotranssurexpressjosemanueldiaz.modelos.EnvioVehiculo;
import com.salesianostriana.dam.proyectotranssurexpressjosemanueldiaz.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/operaciones")
public class OperacionController {

    @Autowired private OperacionService operacionService;
    @Autowired private EnvioService envioService;
    @Autowired private VehiculoService vehiculoService;

    @GetMapping
    public String listarOperaciones(Model model) {
        model.addAttribute("operaciones", operacionService.findAll());
        return "operaciones/lista";
    }

    @GetMapping("/nueva")
    public String nuevaOperacion(Model model) {
        model.addAttribute("operacion", new EnvioVehiculo());
        model.addAttribute("envios", envioService.findAll());
        model.addAttribute("vehiculos", vehiculoService.findAll());
        return "operaciones/formulario";
    }

    @PostMapping("/guardar")
    public String guardarOperacion(@ModelAttribute("operacion") EnvioVehiculo op, Model model) {
        try {
            operacionService.planificarOperacion(op);
            return "redirect:/operaciones";
        } catch (PesoExcedidoException | AsignacionInvalidaException ex) {
            model.addAttribute("error", ex.getMessage());
            model.addAttribute("operacion", op);
            model.addAttribute("envios", envioService.findAll());
            model.addAttribute("vehiculos", vehiculoService.findAll());
            return "operaciones/formulario";
        }
    }

    @GetMapping("/editar/{id}")
    public String editarOperacion(@PathVariable Long id, Model model) {
        operacionService.findById(id).ifPresentOrElse(
            op -> model.addAttribute("operacion", op),
            () -> model.addAttribute("operacion", new EnvioVehiculo())
        );
        model.addAttribute("envios", envioService.findAll());
        model.addAttribute("vehiculos", vehiculoService.findAll());
        return "operaciones/formulario";
    }

    @GetMapping("/borrar/{id}")
    public String borrarOperacion(@PathVariable Long id) {
        operacionService.deleteById(id);
        return "redirect:/operaciones";
    }

    @GetMapping("/historial/{envioId}")
    public String verHistorialEnvio(@PathVariable Long envioId, Model model) {
        model.addAttribute("historial", operacionService.obtenerHistorialPorEnvio(envioId));
        model.addAttribute("envioId", envioId);
        return "operaciones/historial_envio";
    }
}