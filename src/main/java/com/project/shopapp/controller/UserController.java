package com.project.shopapp.controller;

import com.project.shopapp.dtos.UserDTO;
import com.project.shopapp.dtos.UserloginDTO;
import com.project.shopapp.services.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> createUSer(@Valid @RequestBody UserDTO userDTO,
                                        BindingResult result) {
        try {
            if (result.hasErrors()){
                List<String> errorsMessage = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(errorsMessage);
            }
            userService.createUser(userDTO);
            return ResponseEntity.ok("Registration sucessfully");
        }catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody UserloginDTO userloginDTO) {
        //kiem tra dang nhap va sinh token
        String token = userService.login(userloginDTO.getPhoneNumber(), userloginDTO.getPassword());

        // Tra ve token va response.

        return ResponseEntity.ok(token);
    }
}
