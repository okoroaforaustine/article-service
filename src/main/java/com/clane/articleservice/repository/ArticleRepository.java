/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.clane.articleservice.repository;

import com.clane.articleservice.entities.Articles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author austine.okoroafor
 */
@Repository
public interface ArticleRepository extends JpaRepository<Articles, String>{
    
}
