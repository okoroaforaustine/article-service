/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.clane.articleservice.util;

import com.clane.articleservice.entities.Articles;
import com.clane.articleservice.entities.User;
import com.clane.articleservice.repository.ArticleRepository;
import com.clane.articleservice.repository.UserRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

/**
 *
 * @author austine.okoroafor
 */
@Component
@Slf4j
public class ClientUtil {
     @Autowired
    AppUtils appUtils;
    @Autowired
    UserRepository userRepository;
    @Autowired
    
    ArticleRepository articleRepo;
    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    public String getCurrentUserByUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!(auth instanceof AnonymousAuthenticationToken)) {
            String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            return username;
        }

        return null;
    }
    
    public List<Articles> getAllArticle(Integer pageNo, Integer pageSize, String sortBy) {
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));

        Page<Articles> pagedResult = articleRepo.findAll(paging);

        if (pagedResult.hasContent()) {
            return pagedResult.getContent();
        } else {
            return new ArrayList<Articles>();
        }
    }
     public List<User> getAllUser(Integer pageNo, Integer pageSize, String sortBy) {
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));

        Page<User> pagedResult = userRepository.findAll(paging);

        if (pagedResult.hasContent()) {
            return pagedResult.getContent();
        } else {
            return new ArrayList<User>();
        }
    }
    
}
