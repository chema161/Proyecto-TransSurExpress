package com.salesianostriana.dam.proyectotranssurexpressjosemanueldiaz.controllers;

import com.salesianostriana.dam.proyectotranssurexpressjosemanueldiaz.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

    @Autowired private EnvioService envioService;
    @Autowired private VehiculoService vehiculoService;
    @Autowired private ConductorService conductorService;
    @Autowired private OperacionService operacionService;

    @GetMapping("/")
    public String mostrarDashboard(Model model) {
        model.addAttribute("totalEnvios", envioService.findAll().size());
        model.addAttribute("totalVehiculos", vehiculoService.findAll().size());
        model.addAttribute("totalConductores", conductorService.findAll().size());
        model.addAttribute("rutasActivas", operacionService.findAll().size());
        
        return "index";
    }
}