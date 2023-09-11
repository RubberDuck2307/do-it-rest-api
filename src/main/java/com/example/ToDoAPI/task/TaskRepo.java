package com.example.ToDoAPI.task;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TaskRepo extends JpaRepository<Task, Long> {

    List<Task> findAllByDateAndUserId(LocalDate date, Long userId);
    List<Task> findAllByDateBetweenAndUserId(LocalDate start, LocalDate end, Long userId);

    List<Task> findAllByUserId(Long userId);



}
