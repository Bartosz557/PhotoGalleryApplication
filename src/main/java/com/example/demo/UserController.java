package com.example.demo;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;
import java.sql.*;

@Controller
public class UserController {
    @RequestMapping("/profile")
    public RedirectView printPhotos(Model model) {
        String url = "jdbc:sqlserver://localhost:1433;databaseName=PhotoGallery;integratedSecurity=true;encrypt=false;";
        String formattedPhotoUrl="";
        try (Connection connection = DriverManager.getConnection(url)) {
            String querry = "SELECT photo_path FROM Photo";

            PreparedStatement statement = connection.prepareStatement(querry);
            ResultSet resultSet = statement.executeQuery();

            StringBuilder htmlBuilder = new StringBuilder();
            htmlBuilder.append("<html><body>");

            while (resultSet.next()) {
                String photoUrl = resultSet.getString("photo_path");
                formattedPhotoUrl = "/static/images/" + photoUrl;
                htmlBuilder.append("<img src=\"" + formattedPhotoUrl + "\">");
            }
            htmlBuilder.append("</body></html>");
            model.addAttribute("photoUrl", formattedPhotoUrl);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        RedirectView redirectView = new RedirectView();
        redirectView.setUrl("/gallery");
        return redirectView;
    }
}
