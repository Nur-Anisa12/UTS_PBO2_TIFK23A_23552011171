package com.example.demo.repository;
import com.example.demo.model.ToDo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ToDoRepository extends JpaRepository<ToDo, Long> {
    List<ToDo> findByCompleted(boolean completed);
    
    // Add method to find todos by user ID
    List<ToDo> findByUserId(Long userId);
    
    // Find todos by user ID and completion status
    List<ToDo> findByUserIdAndCompleted(Long userId, boolean completed);
}