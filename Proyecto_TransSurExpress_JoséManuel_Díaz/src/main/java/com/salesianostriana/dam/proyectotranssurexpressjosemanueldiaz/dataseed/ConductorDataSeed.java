package com.salesianostriana.dam.proyectotranssurexpressjosemanueldiaz.dataseed;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import com.salesianostriana.dam.proyectotranssurexpressjosemanueldiaz.modelos.Conductor;
import com.salesianostriana.dam.proyectotranssurexpressjosemanueldiaz.repository.ConductorRepository;
import com.salesianostriana.dam.proyectotranssurexpressjosemanueldiaz.repository.VehiculoRepository;
import org.springframework.beans.factory.annotation.Autowired;

@Component
public class ConductorDataSeed implements CommandLineRunner {

   @Autowired
   private ConductorRepository conductorRepository;

   @Autowired
   private VehiculoRepository vehiculoRepository;

   @Override
   public void run(String... args) throws Exception {
       if (conductorRepository.count() == 0) {
           Conductor c1 = new Conductor();
           c1.setNombre("Juan Pérez");
           c1.setExperiencia(5);
           c1.setVehiculo(vehiculoRepository.findById(1L).get());

           Conductor c2 = new Conductor();
           c2.setNombre("María Gómez");
           c2.setExperiencia(3);
           c2.setVehiculo(vehiculoRepository.findById(2L).get());

           Conductor c3 = new Conductor();
           c3.setNombre("Luis Torres");
           c3.setExperiencia(7);
           c3.setVehiculo(vehiculoRepository.findById(1L).get());

           conductorRepository.save(c1);
           conductorRepository.save(c2);
           conductorRepository.save(c3);
       }
   }
}