package com.salesianostriana.dam.proyectotranssurexpressjosemanueldiaz.controllers;

import com.salesianostriana.dam.proyectotranssurexpressjosemanueldiaz.modelos.EstadoEnvio;
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
    @Autowired private EnvioVehiculoService envioVehiculoService;

    @GetMapping("/")
    public String mostrarDashboard(Model model) {

        // Totales básicos 
        model.addAttribute("totalEnvios",      envioService.findAll().size());
        model.addAttribute("totalVehiculos",   vehiculoService.findAll().size());
        model.addAttribute("totalConductores", conductorService.findAll().size());
        model.addAttribute("totalOperaciones", operacionService.findAll().size());

        // Métricas por estado de operación 
        model.addAttribute("opPreparadas",
                envioVehiculoService.findByEstado(EstadoEnvio.PREPARADO).size());
        model.addAttribute("opEnRuta",
                envioVehiculoService.findByEstado(EstadoEnvio.EN_RUTA).size());
        model.addAttribute("opEnReparto",
                envioVehiculoService.findByEstado(EstadoEnvio.EN_REPARTO).size());
        model.addAttribute("opEntregadas",
                envioVehiculoService.findByEstado(EstadoEnvio.ENTREGADO).size());

        // Coste total acumulado de todos los envíos 
        double costeTotal = envioService.findAll().stream()
                .mapToDouble(e -> e.getCoste() != null ? e.getCoste() : 0.0)
                .sum();
        model.addAttribute("costeTotal", costeTotal);

        // Peso total en tránsito (PREPARADO + EN_RUTA + EN_REPARTO) 
        double pesoEnTransito = envioVehiculoService.findByEstado(EstadoEnvio.PREPARADO).stream()
                .mapToDouble(ev -> ev.getEnvio() != null && ev.getEnvio().getPeso() != null
                        ? ev.getEnvio().getPeso() : 0.0).sum()
            + envioVehiculoService.findByEstado(EstadoEnvio.EN_RUTA).stream()
                .mapToDouble(ev -> ev.getEnvio() != null && ev.getEnvio().getPeso() != null
                        ? ev.getEnvio().getPeso() : 0.0).sum()
            + envioVehiculoService.findByEstado(EstadoEnvio.EN_REPARTO).stream()
                .mapToDouble(ev -> ev.getEnvio() != null && ev.getEnvio().getPeso() != null
                        ? ev.getEnvio().getPeso() : 0.0).sum();
        model.addAttribute("pesoEnTransito", pesoEnTransito);

        // Últimas 5 operaciones registradas 
        var todasOps = operacionService.findAll();
        int desde = Math.max(0, todasOps.size() - 5);
        model.addAttribute("ultimasOperaciones",
                todasOps.subList(desde, todasOps.size())
                        .stream()
                        .sorted((a, b) -> {
                            if (a.getFecha() == null) return 1;
                            if (b.getFecha() == null) return -1;
                            return b.getFecha().compareTo(a.getFecha());
                        })
                        .toList());

        return "index";
    }
}
