package com.example.demo.service;
import com.example.demo.model.ToDo;
import com.example.demo.repository.ToDoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ToDoService {
    @Autowired
    private ToDoRepository toDoRepository;
    
    public List<ToDo> getAllTodos() {
        return toDoRepository.findAll();
    }
    
    public void saveTodo(ToDo todo) {
        toDoRepository.save(todo);
    }
    
    public void deleteTodo(Long id) {
        toDoRepository.deleteById(id);
    }
    
    public Optional<ToDo> getTodoById(Long id) {
        return toDoRepository.findById(id);
    }
    
    public List<ToDo> getTodosByCompleted(boolean completed) {
        return toDoRepository.findByCompleted(completed);
    }
    
    // Add method to get todos by user ID
    public List<ToDo> getTodosByUserId(Long userId) {
        return toDoRepository.findByUserId(userId);
    }
    
    // Get todos by user ID and completion status
    public List<ToDo> getTodosByUserIdAndCompleted(Long userId, boolean completed) {
        return toDoRepository.findByUserIdAndCompleted(userId, completed);
    }
}