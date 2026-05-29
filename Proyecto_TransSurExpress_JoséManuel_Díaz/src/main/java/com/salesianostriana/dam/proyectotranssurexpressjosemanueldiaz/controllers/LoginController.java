package com.salesianostriana.dam.proyectotranssurexpressjosemanueldiaz.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String mostrarLogin() {
        System.out.println(">>> LOGIN CONTROLLER EJECUTADO <<<");
        return "login";
    }
}