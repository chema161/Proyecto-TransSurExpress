package com.salesianostriana.dam.proyectotranssurexpressjosemanueldiaz.dataseed;

import com.salesianostriana.dam.proyectotranssurexpressjosemanueldiaz.modelos.Vehiculo;
import com.salesianostriana.dam.proyectotranssurexpressjosemanueldiaz.repository.VehiculoRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
 
@Component
public class VehiculoDataSeed implements CommandLineRunner {

   @Autowired
   private VehiculoRepository vehiculoRepository;

   @Override
   public void run(String... args) throws Exception {
       if (vehiculoRepository.count() == 0) {
           Vehiculo v1 = new Vehiculo();
           v1.setMatricula("ABC-123");
           v1.setCapacidad(1000.0);

           Vehiculo v2 = new Vehiculo();
           v2.setMatricula("XYZ-789");
           v2.setCapacidad(2000.0);

           Vehiculo v3 = new Vehiculo();
           v3.setMatricula("LMN-456");
           v3.setCapacidad(1500.0);

           vehiculoRepository.save(v1);
           vehiculoRepository.save(v2);
           vehiculoRepository.save(v3);
       }
   }
}