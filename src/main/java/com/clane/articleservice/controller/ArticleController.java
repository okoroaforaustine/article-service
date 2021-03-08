/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.clane.articleservice.controller;

import com.clane.articleservice.dto.Response;
import com.clane.articleservice.entities.Articles;
import com.clane.articleservice.entities.User;
import com.clane.articleservice.repository.ArticleRepository;
import com.clane.articleservice.repository.UserRepository;
import com.clane.articleservice.util.AppUtils;
import com.clane.articleservice.util.ClientUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import java.util.Optional;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

/**
 *
 * @author austine.okoroafor
 */
@RestController
@RequestMapping("/Article")
@Api(value = "Article Controller", description = "All API operations on Article...")
@Slf4j
@CrossOrigin
public class ArticleController {
    
     @Autowired
    AppUtils appUtils;

    @Autowired
    ClientUtil clientUtils;
    @Autowired
    UserRepository userRepo;
    @Autowired
    ArticleRepository articleRepo;

    @PostMapping("/save")
    @ApiOperation(value = "Add new article",
            notes = "Add new article in the system, provided all information are correct",
            response = Response.class)
    public ResponseEntity<?> saveArticle(@Valid @RequestBody Articles article, @ApiIgnore Errors errors) {

        if (errors.hasErrors()) {
            return appUtils.returnPostValidationErrors(errors);
        }

        String getcurrentUsername = clientUtils.getCurrentUserByUsername();
        User getUser = userRepo.findByUsername(getcurrentUsername);

        String firstName = getUser.getFirstname();
        String lastName = getUser.getLastname();
        String names = firstName ;
        String reqwrittenBy=article.getWritenby();
        
        if (!names.equalsIgnoreCase(reqwrittenBy)) {

            return appUtils.returnErrorResponse("Writenby name must match the loggin users firstname", HttpStatus.BAD_REQUEST);

        }
        article.setUser(getUser);
        Articles newArticle = articleRepo.save(article);
        newArticle.setUser(getUser);

        return appUtils.returnSuccessResponse(newArticle, "Article created Succesfully");

    }

    @PutMapping("/update/{id}")
    @ApiOperation(value = "Update article",
            notes = "Update existing article. The Article ID is required",
            response = Response.class)
    public ResponseEntity<?> updateArticle(@PathVariable("id") String id, @Valid @RequestBody Articles article, @ApiIgnore Errors errors) {
        log.debug("Received request to update existing article with id: " + article.getId());

        if (errors.hasErrors()) {
            return appUtils.returnPostValidationErrors(errors);
        }

        if (null == article.getId() || appUtils.isEmptyString(article.getId().toString())) {
            return appUtils.returnErrorResponse("article ID number is required.", HttpStatus.BAD_REQUEST);
        }
        String getcurrentUsername = clientUtils.getCurrentUserByUsername();
        User getUser = userRepo.findByUsername(getcurrentUsername);

       
        String firstName = getUser.getFirstname();
        String lastName = getUser.getLastname();
        String names = firstName ;
        String reqwrittenBy=article.getWritenby();
        
        if (!names.equalsIgnoreCase(reqwrittenBy)) {
            return appUtils.returnErrorResponse("Writenby name must match the loggin users firstname", HttpStatus.BAD_REQUEST);
        }

        Optional ArticleData = articleRepo.findById(id);
        if (ArticleData.isPresent()) {

            article.setUser(getUser);
            Articles newArtilce = articleRepo.save(article);

            log.debug("article successfully updated...");

            return appUtils.returnSuccessResponse(article, "Article updated Succesfully");
        }
        return appUtils.returnErrorResponse("article ID not found.", HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/delete/{id}")
    @ApiOperation(value = "Delete article",
            notes = "Delete existing article. The Article ID is required",
            response = Response.class)
    public ResponseEntity<?> deleteArticle(@PathVariable("id") String id) {
        String getcurrentUsername = clientUtils.getCurrentUserByUsername();
        User getUser = userRepo.findByUsername(getcurrentUsername);
        Optional<Articles> ArticleData = articleRepo.findById(id);

        if (ArticleData.isPresent()) {

            articleRepo.deleteById(id);

            return appUtils.returnStatusResponse("Article has been deleted Succesfully");
        }

        return appUtils.returnErrorResponse("article ID not found.", HttpStatus.NOT_FOUND);

    }

    @GetMapping(value = "/getAll", produces = "Application/json", consumes = "Application/json")
    public ResponseEntity<?> findAll(@RequestParam("start") int start, @RequestParam("limit") int limit) {
        List<Articles> list = clientUtils.getAllArticle(start, limit, "id");

        return appUtils.returnSuccessResponse(list,null);

    }
    
}
