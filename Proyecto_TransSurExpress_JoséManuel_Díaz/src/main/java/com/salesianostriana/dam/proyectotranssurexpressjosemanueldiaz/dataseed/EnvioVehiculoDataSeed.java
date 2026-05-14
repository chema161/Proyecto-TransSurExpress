package com.salesianostriana.dam.proyectotranssurexpressjosemanueldiaz.dataseed;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import com.salesianostriana.dam.proyectotranssurexpressjosemanueldiaz.modelos.Envio;
import com.salesianostriana.dam.proyectotranssurexpressjosemanueldiaz.modelos.EnvioVehiculo;
import com.salesianostriana.dam.proyectotranssurexpressjosemanueldiaz.modelos.EstadoEnvio;
import com.salesianostriana.dam.proyectotranssurexpressjosemanueldiaz.modelos.Vehiculo;
import com.salesianostriana.dam.proyectotranssurexpressjosemanueldiaz.repository.EnvioRepository;
import com.salesianostriana.dam.proyectotranssurexpressjosemanueldiaz.repository.EnvioVehiculoRepository;
import com.salesianostriana.dam.proyectotranssurexpressjosemanueldiaz.repository.VehiculoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import java.time.LocalDateTime;

@Component 
public class EnvioVehiculoDataSeed implements CommandLineRunner {

    @Autowired
    private EnvioVehiculoRepository envioVehiculoRepository;

    @Autowired
    private EnvioRepository envioRepository;

    @Autowired
    private VehiculoRepository vehiculoRepository;

    @Override
    public void run(String... args) throws Exception {
        if (envioVehiculoRepository.count() == 0) {
            Envio e1 = envioRepository.findById(1L).get();
            Envio e2 = envioRepository.findById(2L).get();

            Vehiculo v1 = vehiculoRepository.findById(1L).get();
            Vehiculo v2 = vehiculoRepository.findById(2L).get();

            EnvioVehiculo r1 = new EnvioVehiculo();
            r1.setEnvio(e1);
            r1.setVehiculo(v1);
            r1.setEstado(EstadoEnvio.PREPARADO);
            r1.setFecha(LocalDateTime.now().minusDays(1));
            r1.setLugar("Centro logístico Madrid");

            EnvioVehiculo r2 = new EnvioVehiculo();
            r2.setEnvio(e1);
            r2.setVehiculo(v2);
            r2.setEstado(EstadoEnvio.EN_RUTA);
            r2.setFecha(LocalDateTime.now());
            r2.setLugar("Centro logístico Zaragoza");

            EnvioVehiculo r3 = new EnvioVehiculo();
            r3.setEnvio(e2);
            r3.setVehiculo(v2);
            r3.setEstado(EstadoEnvio.EN_REPARTO);
            r3.setFecha(LocalDateTime.now());
            r3.setLugar("Sevilla");

            envioVehiculoRepository.save(r1);
            envioVehiculoRepository.save(r2);
            envioVehiculoRepository.save(r3);
        }
    }
}