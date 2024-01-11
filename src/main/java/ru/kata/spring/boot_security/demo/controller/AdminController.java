package ru.kata.spring.boot_security.demo.controller;


import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.services.UserService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final ModelMapper modelMapper;

    @Value("${spring.api.url}")
    private String adminApiUrl;
    @ModelAttribute("adminApiUrl")
    public String addPropertiesToModel() {
        return adminApiUrl;
    }
    @Autowired
    public AdminController(UserService userService, ModelMapper modelMapper) {
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/users")
    public ModelAndView getPage() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("users");
        return modelAndView;
    }

    @GetMapping()
    public Map<String, Object> printAllUsers() {
        Map<String, Object> resMap = new HashMap<>();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User admin = (User) authentication.getPrincipal();
        List<Role> roles = userService.getAllRoles();

        List<User> users = userService.getListOfUsers();
        users.remove(admin);
        resMap.put("admin", admin);
        resMap.put("users", users);
        resMap.put("roles", roles);

        return resMap;
    }


    @PostMapping()
    public Map<String, Object> creat(@RequestBody User user) {
        userService.save(user);
        return printAllUsers();
    }


    @PatchMapping("/{id}")
    public Map<String, Object> update(@RequestBody User user) {
        userService.update(user);
        return printAllUsers();
    }

    @DeleteMapping("/{id}")
    public Map<String, Object> delete(@PathVariable("id") long id) {
        userService.delete(id);
        return printAllUsers();
    }


}
