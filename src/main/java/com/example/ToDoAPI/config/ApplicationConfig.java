package com.example.ToDoAPI.config;

import com.example.ToDoAPI.event.DTOs.EventCreateDTO;
import com.example.ToDoAPI.event.Event;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.web.client.RestTemplate;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {


    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        TypeMap<EventCreateDTO, Event> map = modelMapper.createTypeMap(EventCreateDTO.class, Event.class);
        map.addMappings(mapper -> mapper.skip(Event::setId));
        return modelMapper;
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }


}
