package com.example.do_it_api.task;

import com.example.do_it_api.exception.custom_exception.InvalidRequestBodyException;

import com.example.do_it_api.security.EntityAccessHelper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepo taskRepo;
    private final ModelMapper modelMapper;
    private final EntityAccessHelper<Task> entityAccessHelper;

    public ResponseEntity<TaskDTO> getTask(long id) {
        Task task = taskRepo.findById(id).orElseThrow(NoSuchElementException::new);
        if (!entityAccessHelper.hasUserAccessTo(task)) {
            throw new AccessDeniedException("Access denied");
        }
        TaskDTO taskDTO = modelMapper.map(task, TaskDTO.class);
        return new ResponseEntity<>(taskDTO, HttpStatus.OK);
    }

    public ResponseEntity<List<TaskDTO>> getTaskByDay(LocalDate localDate) {
        List<Task> tasks = taskRepo.findAllByDateAndUser_Id(localDate, entityAccessHelper.getLoggedUserId());
        List<TaskDTO> taskDTOS = tasks.stream().map(task -> modelMapper.map(task, TaskDTO.class)).toList();
        return new ResponseEntity<>(taskDTOS, HttpStatus.OK);
    }

    public ResponseEntity<List<TaskDTO>> getTaskInWeek(LocalDate localDate) {
        List<Task> tasks = taskRepo.findAllByDateBetweenAndUser_Id(localDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)), localDate.plusDays(6), entityAccessHelper.getLoggedUserId());
        List<TaskDTO> taskDTOS = tasks.stream().map(task -> modelMapper.map(task, TaskDTO.class)).toList();
        return new ResponseEntity<>(taskDTOS, HttpStatus.OK);
    }

    public ResponseEntity<List<TaskDTO>> getTasksInMonth(LocalDate localDate) {
        List<Task> tasks = taskRepo.findAllByDateBetweenAndUser_Id(localDate.with(TemporalAdjusters.firstDayOfMonth()), localDate.with(TemporalAdjusters.lastDayOfMonth()), entityAccessHelper.getLoggedUserId());
        List<TaskDTO> taskDTOS = tasks.stream().map(task -> modelMapper.map(task, TaskDTO.class)).toList();
        return new ResponseEntity<>(taskDTOS, HttpStatus.OK);
    }


    public ResponseEntity<TaskDTO> addTask(TaskDTO taskDTO) {
        if (!validateTaskRequest(taskDTO))
           throw new InvalidRequestBodyException();
        Task task = modelMapper.map(taskDTO, Task.class);
        task.setUser(entityAccessHelper.getLoggedUser());
        task = taskRepo.save(task);
        taskDTO = modelMapper.map(task, TaskDTO.class);
        return new ResponseEntity<>(taskDTO, HttpStatus.OK);
    }

    public ResponseEntity<List<TaskDTO>> getAllTasks() {

        List<Task> tasks = taskRepo.findAllByUser_Id(entityAccessHelper.getLoggedUserId());
        List<TaskDTO> taskDTOS = new ArrayList<>();
        tasks.forEach(task -> taskDTOS.add(modelMapper.map(task, TaskDTO.class)));
        return new ResponseEntity<>(taskDTOS, HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<TaskDTO> modifyTask(TaskDTO taskDTO) {

        if (!validateTaskRequest(taskDTO))
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);

        Task task = taskRepo.findById(taskDTO.getId()).orElseThrow(NoSuchElementException::new);
        if (!entityAccessHelper.hasUserAccessTo(task)) {
            throw new AccessDeniedException("Access denied");
        }
        task.setName(taskDTO.getName());
        task.setDescription(taskDTO.getDescription());
        task.setDate(taskDTO.getDate());
        task.setImportant(taskDTO.isImportant());
        task.setFinished(taskDTO.isFinished());
        taskDTO = modelMapper.map(task, TaskDTO.class);
        return new ResponseEntity<>(taskDTO, HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<TaskDTO> toggleFinished(long id) {
        Task task = taskRepo.findById(id).orElseThrow(NoSuchElementException::new);
        if (!entityAccessHelper.hasUserAccessTo(task)) {
            throw new AccessDeniedException("Access denied");
        }
        task.setFinished(!task.isFinished());
        TaskDTO taskDTO = modelMapper.map(task, TaskDTO.class);
        return new ResponseEntity<>(taskDTO, HttpStatus.OK);
    }

    public ResponseEntity<String> deleteTask(Long id) {
        if (!entityAccessHelper.hasUserAccessTo(taskRepo.findById(id).orElseThrow(NoSuchElementException::new)))
            throw new AccessDeniedException("Access denied");
        taskRepo.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private boolean validateTaskRequest(TaskDTO taskDTO) {
        String name = taskDTO.getName().replaceAll("\\s+", "");
        return name.length() >= 1;
    }
}