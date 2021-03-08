/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.clane.articleservice.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import lombok.Data;

/**
 *
 * @author austine.okoroafor
 */
@Entity
@Data
@Table(name = "AppUser")
public class User extends AuditModel{
    
     @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NotEmpty(message = "user Name is required!")
    private String username;
    @NotEmpty(message = "password is required!")
    private String password;
    @NotEmpty(message = "FirstName is required!")
    private String firstname;
    @NotEmpty(message = "LastName is required!")
    private String lastname;
    @NotEmpty(message = "Mobile is required!")
    private String mobile;
    @NotEmpty(message = "email is required!")
    private String email;
    
}
