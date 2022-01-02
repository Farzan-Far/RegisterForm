package com.example.registerformlogin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.List;

@Controller
public class AppController
{
    @Autowired
    private UserServices service;

    @Autowired
    private UserRepository userRepo;

    @GetMapping("")
    public String viewHomePage()
    {
        return "index";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());

        return "signup_form";
    }

    @PostMapping("/process_register")
    public String processRegister(User user, HttpServletRequest request)
            throws UnsupportedEncodingException, MessagingException
    {
        /*BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        userRepo.save(user);*/
        service.register(user,getSiteURL(request));

        return "register_success";
    }

    @GetMapping("/users")
    public String listUsers(Model model)
    {
        List<User> listUsers = userRepo.findAll();
        model.addAttribute("listUsers", listUsers);

        return "users";
    }

    private String getSiteURL(HttpServletRequest request)
    {
        String siteURL = request.getRequestURL().toString();
        return siteURL.replace(request.getServletPath(),"");
    }

    public String verifyUSer(@Param("code") String code)
    {
        if (service.verify(code))
        {
            return "verify_success";
        }
        else
        {
            return "verify_fail";
        }
    }
}