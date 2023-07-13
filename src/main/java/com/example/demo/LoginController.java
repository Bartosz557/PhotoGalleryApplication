package com.example.demo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import java.sql.*;

@Controller
public class LoginController {

    private static boolean isAdmin=false;

    //login process
    @PostMapping("/login")
    @ResponseBody
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {
        isAdmin=false;
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();
        if (isValidLogin(username, password)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // Login failed
        }
    }

    private boolean isValidLogin(String username, String password) {
        String url = "jdbc:sqlserver://localhost:1433;databaseName=PhotoGallery;integratedSecurity=true;encrypt=false;";
        String dbPassword = null;
        try (Connection connection = DriverManager.getConnection(url)) {
            String querry = "SELECT password FROM Client WHERE username = ?";
            PreparedStatement statement = connection.prepareStatement(querry);
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                dbPassword = resultSet.getString("password");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if(password.equals(dbPassword)) {
            ifAdmin(url,username);
            return true;
        }
        else
            return false;
    }

    public void ifAdmin(String url,String username)
    {
        try (Connection connection = DriverManager.getConnection(url)) {
            String querry = "SELECT password, is_admin FROM Client WHERE username = ?";
            PreparedStatement statement = connection.prepareStatement(querry);
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                isAdmin = resultSet.getBoolean("is_admin");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // after successful login
    @RequestMapping("/go-to-profile")
    public RedirectView localRedirect() {
        RedirectView redirectView = new RedirectView();
        if (isAdmin) {
            redirectView.setUrl("/admin");
        } else {
            redirectView.setUrl("/profile");
        }
                   return redirectView;

    }

    private static class LoginRequest {
        private String username;
        private String password;
        public String getUsername() {
            return username;
        }
        public String getPassword() {
            return password;
        }

    }}
