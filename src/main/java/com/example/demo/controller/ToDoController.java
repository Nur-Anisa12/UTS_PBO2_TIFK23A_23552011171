package com.example.demo.controller;

import com.example.demo.model.ToDo;
import com.example.demo.model.User;
import com.example.demo.service.ToDoService;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Controller
public class ToDoController {
    @Autowired
    private ToDoService toDoService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/todos")
    public String todos(
            @RequestParam(required = false, defaultValue = "") String filter,
            Model model,
            Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
        Long userId = user.getId();

        List<ToDo> todos;
        if ("completed".equals(filter)) {
            todos = toDoService.getTodosByUserIdAndCompleted(userId, true);
        } else if ("incomplete".equals(filter)) {
            todos = toDoService.getTodosByUserIdAndCompleted(userId, false);
        } else {
            todos = toDoService.getTodosByUserId(userId);
        }

        System.out.println("Filter value: " + filter); // atau gunakan logger

        model.addAttribute("todos", todos);
        model.addAttribute("filter", filter);
        model.addAttribute("newTodo", new ToDo());
        model.addAttribute("user", user);
        return "todos";
    }

    @GetMapping("/todos/create") // tampilkan form
    public String showCreateForm(Model model, Authentication authentication) {
        // Tambahkan objek ToDo kosong untuk form
        model.addAttribute("todo", new ToDo());

        // Tambahkan data user ke model
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
        model.addAttribute("user", user);

        return "create";
    }

    @PostMapping("/todos/create")
    public String createTodo(@ModelAttribute("todo") ToDo todo,
            Authentication authentication,
            RedirectAttributes redirectAttributes) {
        try {
            if (todo.getTask() == null || todo.getTask().trim().isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Task tidak boleh kosong");
                return "redirect:/";
            }

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            User user = userRepository.findByUsername(userDetails.getUsername())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            todo.setUser(user);
            todo.setCompleted(false);

            if (todo.getDeadline() != null && todo.getDeadline().isBefore(LocalDate.now())) {
                redirectAttributes.addFlashAttribute("error", "Deadline tidak boleh di masa lalu");
                return "redirect:/";
            }

            toDoService.saveTodo(todo);

            redirectAttributes.addFlashAttribute("success", "Task berhasil ditambahkan");
            return "redirect:/todos";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Gagal menambahkan task: " + e.getMessage());
            return "redirect:/";
        }
    }

    // Hapus method showDeleteConfirmation karena tidak diperlukan lagi

    // Proses penghapusan langsung dari halaman todos
    // Proses penghapusan langsung dari halaman todos dengan redirect kembali ke
    // todos
    @GetMapping("/todos/delete/{id}")
    public String deleteTodo(@PathVariable Long id, Authentication authentication,
            RedirectAttributes redirectAttributes) {
        // Verify the current user is authorized to delete this todo
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Optional<ToDo> todoOptional = toDoService.getTodoById(id);
        if (todoOptional.isPresent() && todoOptional.get().getUser().getId().equals(user.getId())) {
            toDoService.deleteTodo(id);
            redirectAttributes.addFlashAttribute("message", "Tugas berhasil dihapus!");
            redirectAttributes.addFlashAttribute("type", "success");
        } else {
            redirectAttributes.addFlashAttribute("message", "Tugas tidak ditemukan atau Anda tidak memiliki izin.");
            redirectAttributes.addFlashAttribute("type", "danger");
        }

        // Redirect kembali ke halaman todos tanpa parameter tambahan
        return "redirect:/todos";
    }

    /**
     * Menampilkan halaman edit untuk tugas tertentu
     */
    @GetMapping("/todos/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model, Authentication authentication,
            RedirectAttributes redirectAttributes) {
        // Verify the current user is authorized to edit this todo
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Get todo by ID
        Optional<ToDo> todoOptional = toDoService.getTodoById(id);

        // Check if todo exists and belongs to current user
        if (todoOptional.isPresent() && todoOptional.get().getUser().getId().equals(user.getId())) {
            // Add todo to model
            model.addAttribute("todo", todoOptional.get());
            model.addAttribute("user", user);
            return "edit"; // Return edit.html view
        } else {
            // Todo not found or not owned by current user
            redirectAttributes.addAttribute("message",
                    "Tidak dapat menemukan tugas atau Anda tidak memiliki izin untuk mengeditnya.");
            redirectAttributes.addAttribute("type", "danger");
            return "redirect:/";
        }
    }

    @PostMapping("/edit")
    public String editTodo(
            @RequestParam Long id,
            @RequestParam String task,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate deadline,
            Authentication authentication,
            RedirectAttributes redirectAttributes) {

        // Verify the current user is authorized to edit this todo
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        try {
            // Get todo by ID
            Optional<ToDo> todoOptional = toDoService.getTodoById(id);

            // Check if todo exists and belongs to current user
            if (todoOptional.isPresent() && todoOptional.get().getUser().getId().equals(user.getId())) {
                ToDo todo = todoOptional.get();
                todo.setTask(task);
                todo.setDeadline(deadline);
                toDoService.saveTodo(todo);

                redirectAttributes.addAttribute("message", "Tugas berhasil diubah!");
                redirectAttributes.addAttribute("type", "success");
            } else {
                // Todo not found or not owned by current user
                redirectAttributes.addAttribute("message",
                        "Tidak dapat menemukan tugas atau Anda tidak memiliki izin untuk mengeditnya.");
                redirectAttributes.addAttribute("type", "danger");
            }
        } catch (Exception e) {
            redirectAttributes.addAttribute("message", "Gagal mengubah tugas: " + e.getMessage());
            redirectAttributes.addAttribute("type", "danger");
        }

        return "redirect:/todos";
    }

    @PostMapping("/update/{id}")
    public String toggleStatus(@PathVariable Long id, Authentication authentication,
            RedirectAttributes redirectAttributes) {
        // Verify the todo belongs to the current user
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Optional<ToDo> todoOptional = toDoService.getTodoById(id);
        if (todoOptional.isPresent() && todoOptional.get().getUser().getId().equals(user.getId())) {
            ToDo todo = todoOptional.get();
            todo.setCompleted(!todo.isCompleted());
            toDoService.saveTodo(todo);

            redirectAttributes.addAttribute("message",
                    todo.isCompleted() ? "Tugas ditandai selesai!" : "Tugas ditandai belum selesai!");
            redirectAttributes.addAttribute("type", "success");
        } else {
            redirectAttributes.addAttribute("message", "Tugas tidak ditemukan atau Anda tidak memiliki akses.");
            redirectAttributes.addAttribute("type", "danger");
        }

        return "redirect:/todos";
    }
}