package com.talentflow.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class UserRequest {
    @NotBlank 
    private String username;
    @Email 
    private String email;
    @NotBlank 
    private String password;
    @NotBlank 
    private String firstName;
    @NotBlank 
    private String lastName;
    @NotBlank 
    private String role;

      // Getters
    public String getUsername()  
    { 
        return username;
    }
    public String getEmail()
    { 
        return email;
    }
    public String getPassword() 
    { return password;

    }
    public String getFirstName()
    {
        return firstName;
    }
    public String getLastName()
    { 
        return lastName;
    }
    public String getRole() {
        return role;
   }

    // Setters
    public void setUsername(String username) { this.username = username; }
    public void setEmail(String email) { this.email = email; }
    public void setPassword(String password) { this.password = password; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public void setRole(String role) { this.role = role; }
}
