package com.salesianostriana.dam.proyectotranssurexpressjosemanueldiaz.config;

import com.salesianostriana.dam.proyectotranssurexpressjosemanueldiaz.modelos.Envio;
import com.salesianostriana.dam.proyectotranssurexpressjosemanueldiaz.modelos.Vehiculo;
import com.salesianostriana.dam.proyectotranssurexpressjosemanueldiaz.repository.EnvioRepository;
import com.salesianostriana.dam.proyectotranssurexpressjosemanueldiaz.repository.VehiculoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.core.convert.converter.Converter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final EnvioRepository envioRepository;
    private final VehiculoRepository vehiculoRepository;

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new Converter<String, Envio>() {
            @Override
            public Envio convert(String id) {
                if (id == null || id.isBlank()) return null;
                return envioRepository.findById(Long.parseLong(id)).orElse(null);
            }
        });
        registry.addConverter(new Converter<String, Vehiculo>() {
            @Override
            public Vehiculo convert(String id) {
                if (id == null || id.isBlank()) return null;
                return vehiculoRepository.findById(Long.parseLong(id)).orElse(null);
            }
        });
    }
}
