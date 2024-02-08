package com.codedailywithme.springSecurityPractice.demo;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@PreAuthorize("hasRole('ADMIN')")

public class AdminController {
    @GetMapping
    @PreAuthorize("hasAuthority('admin:read')")
    public String get(){
        return "GET:: admin controller";
    }
    @PostMapping
    @PreAuthorize("hasAuthority('admin:create')")
//    @Hidden
  public String post(){
        return "POST: admin Controller";
    }
    @PutMapping
    @PreAuthorize("hasAuthority('admin::update')")
    public String updateBooks(){
        return "PUT: admin controller";
    }
    @DeleteMapping
    @PreAuthorize("hasAuthority('admin:delete')")
//    @Hidden
    public String delete() {
        return "DELETE:: admin controller";
    }

}
