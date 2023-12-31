package com.example.do_it_api.config;

import com.example.do_it_api.event.dto.EventCreateDTO;
import com.example.do_it_api.event.Event;
import com.example.do_it_api.task.Task;
import com.example.do_it_api.task.TaskDTO;
import com.example.do_it_api.task.TaskRepo;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
