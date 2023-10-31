package com.example.do_it_api.task;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@CrossOrigin(origins = "${app.url}", allowCredentials = "true")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/task")
public class TaskController {


    private final TaskService taskService;

    @GetMapping("/{id}")
    public ResponseEntity<TaskDTO> getTask(@PathVariable long id) {
        return taskService.getTask(id);
    }

    @PostMapping("/")
    public ResponseEntity<TaskDTO> addTask(@Valid @RequestBody TaskDTO taskDTO) {
        return taskService.addTask(taskDTO);
    }

    @GetMapping("/")
    public ResponseEntity<List<TaskDTO>> getAllTasks(){
        return taskService.getAllTasks();
    }


    @PutMapping("/")
    public ResponseEntity<TaskDTO> modifyTask(@Valid @RequestBody TaskDTO taskDTO){
        return  taskService.modifyTask(taskDTO);
    }

    @GetMapping("/day/{day}/{month}/{year}")
    public ResponseEntity<List<TaskDTO>> getTaskByDay(@PathVariable int day, @PathVariable int month,
                                                      @PathVariable int year){
        return taskService.getTaskByDay(LocalDate.of(year, month, day));
    }

    @GetMapping("/week/{day}/{month}/{year}")
    public ResponseEntity<List<TaskDTO>> getTaskInWeek(@PathVariable int day, @PathVariable int month, @PathVariable int year){
        return taskService.getTaskInWeek(LocalDate.of(year, month, day));
    }

    @GetMapping("/month/{month}/{year}")
    public ResponseEntity<List<TaskDTO>> getTasksInMonth(@PathVariable int month, @PathVariable int year){
        return taskService.getTasksInMonth(LocalDate.of(year, month, 1));
    }

    @PutMapping("/state/{id}")
    public ResponseEntity<TaskDTO> toggleFinished(@PathVariable long id){
        return taskService.toggleFinished(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTask(@PathVariable long id){
        return taskService.deleteTask(id);
    }
}
