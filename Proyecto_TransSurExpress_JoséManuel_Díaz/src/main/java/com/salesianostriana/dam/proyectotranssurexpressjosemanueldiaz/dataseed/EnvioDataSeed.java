package com.salesianostriana.dam.proyectotranssurexpressjosemanueldiaz.dataseed;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import com.salesianostriana.dam.proyectotranssurexpressjosemanueldiaz.modelos.Envio;
import com.salesianostriana.dam.proyectotranssurexpressjosemanueldiaz.repository.EnvioRepository;
import org.springframework.beans.factory.annotation.Autowired;

@Component
public class EnvioDataSeed implements CommandLineRunner {

   @Autowired
   private EnvioRepository envioRepository;

   @Override
   public void run(String... args) throws Exception {
       if (envioRepository.count() == 0) {
           Envio e1 = new Envio();
           e1.setCodigo("ENV-001");
           e1.setOrigen("Madrid");
           e1.setDestino("Barcelona");
           e1.setPeso(500.0);
           e1.setCoste(150.0);

           Envio e2 = new Envio();
           e2.setCodigo("ENV-002");
           e2.setOrigen("Valencia");
           e2.setDestino("Sevilla");
           e2.setPeso(300.0);
           e2.setCoste(120.0);

           Envio e3 = new Envio();
           e3.setCodigo("ENV-003");
           e3.setOrigen("Bilbao");
           e3.setDestino("Zaragoza");
           e3.setPeso(700.0);
           e3.setCoste(200.0);

           envioRepository.save(e1);
           envioRepository.save(e2);
           envioRepository.save(e3);
       }
   }
}
