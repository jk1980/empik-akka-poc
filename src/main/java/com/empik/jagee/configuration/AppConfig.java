package com.empik.jagee.configuration;

import akka.actor.ActorSystem;
import lombok.val;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import static com.empik.jagee.configuration.SpringExtension.SPRING_EXTENSION_PROVIDER;

@Configuration
@ComponentScan
public class AppConfig {

    @Autowired
    private ApplicationContext applicationContext;

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public ActorSystem actorSystem() {

        val system = ActorSystem.create("akka-jagee");

        SPRING_EXTENSION_PROVIDER.get(system)
                .initialize(applicationContext);

        return system;
    }
}
