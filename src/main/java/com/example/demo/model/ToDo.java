package com.example.demo.model;

import java.time.LocalDate;
import jakarta.persistence.*;

@Entity
@Table(name = "todos")
public class ToDo {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 255)
    private String task;
    
    @Column(nullable = false)
    private boolean completed = false;
    
    @Column(nullable = false)
    private LocalDate deadline;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Constructors
    public ToDo() {
    }

    public ToDo(String task, LocalDate deadline, User user) {
        this.task = task;
        this.deadline = deadline;
        this.user = user;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public LocalDate getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDate deadline) {
        this.deadline = deadline;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    // toString() untuk debugging
    @Override
    public String toString() {
        return "ToDo{" +
                "id=" + id +
                ", task='" + task + '\'' +
                ", completed=" + completed +
                ", deadline=" + deadline +
                ", user=" + (user != null ? user.getId() : "null") +
                '}';
    }
}