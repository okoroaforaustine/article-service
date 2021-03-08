/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.clane.articleservice.controller;

import com.clane.articleservice.dto.Response;
import com.clane.articleservice.entities.Articles;
import com.clane.articleservice.entities.User;
import com.clane.articleservice.repository.UserRepository;
import com.clane.articleservice.util.AppUtils;
import com.clane.articleservice.util.ClientUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;
import com.clane.articleservice.dto.Error;
import com.clane.articleservice.security.JWTAuthenticationFilter;
import javax.validation.Valid;

/**
 *
 * @author austine.okoroafor
 */
@RestController
@RequestMapping("/writer")
@Api(value = "User Controller", description = "All API operations on User...")
@Slf4j
@CrossOrigin
public class UserController {
    @Autowired
    AppUtils appUtils;

    @Autowired
    UserRepository userRepo;
    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    ClientUtil clientUtils;
    
    
    

    @PostMapping("/register")
    @ApiOperation(value = "Register a new writer",
            notes = "Register a new writer in the system, provided all information are correct and user and email does not exist already..",
            response = Response.class)
    public ResponseEntity<?> saveUser(@Valid @RequestBody User user, @ApiIgnore Errors errors) {
        log.debug("Received request to register new merchant.");

        if (errors.hasErrors()) {
            return appUtils.returnPostValidationErrors(errors);
        }

        List<Error> validationErrors = validateUser(user);
        if (!validationErrors.isEmpty()) {
            return appUtils.returnErrorResponse(validationErrors, HttpStatus.CONFLICT);
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        userRepo.save(user);

        log.debug("New writer registered successfully. Returning response...");

        return appUtils.returnSuccessResponse(user, "User created Succesfully");

    }

    private List<Error> validateUser(User user) {
        List<Error> errors = new ArrayList<Error>();

        log.debug("Validating User request...");

        User existingName = userRepo.findByUsername(user.getUsername());
        if (null != existingName && !existingName.getId().equals(user.getId())) {
            errors.add(new Error("user with this name '"
                    + user.getUsername() + "' already exists.", 4, "username"));
        }
        
        User existEmail=userRepo.findByEmail(user.getEmail());
         if (null != existEmail) {
            errors.add(new Error("user with this email '"
                    + user.getEmail() + "' already exists.", 4, "email"));
        }
         log.debug("User registration request validation completed...");
		
		return errors;
    }
    
    @GetMapping(value = "/getAll", produces = "Application/json", consumes = "Application/json")
    public ResponseEntity<?> findAll(@RequestParam("start") int start, @RequestParam("limit") int limit) {
        List<User> list = clientUtils.getAllUser(start, limit, "wrritenBy");

        return appUtils.returnSuccessResponse(list, null);

    }
    
}
