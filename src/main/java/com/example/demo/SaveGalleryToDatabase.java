package com.example.demo;

import java.sql.*;
import java.util.List;

public class SaveGalleryToDatabase {
    private String galleryName;
    private String username;
    private String password;
    private List<String> photos;


    public SaveGalleryToDatabase(String galleryName, String username, String password, List<String> photos) {
        this.galleryName = galleryName;
        this.username = username;
        this.password = password;
        this.photos = photos;
    }
    public void saveToDatabase() {
        String url = "jdbc:sqlserver://localhost:1433;databaseName=PhotoGallery;integratedSecurity=true;encrypt=false;";

        try (Connection connection = DriverManager.getConnection(url)) {

            // adding new client
            String clientQuerry = "INSERT INTO Client (username, password) VALUES (?, ?)";
            PreparedStatement clientStatement = connection.prepareStatement(clientQuerry, Statement.RETURN_GENERATED_KEYS);
            clientStatement.setString(1, username);
            clientStatement.setString(2, password);
            clientStatement.executeUpdate();

            // getting client's id
            ResultSet clientKeys = clientStatement.getGeneratedKeys();
            int clientId = -1;
            if (clientKeys.next()) {
                clientId = clientKeys.getInt(1);
            }

            // adding new gallery
            String galleryQuerry = "INSERT INTO Gallery (name, client_id) VALUES (?, ?)";
            PreparedStatement galleryStatement = connection.prepareStatement(galleryQuerry, Statement.RETURN_GENERATED_KEYS);
            galleryStatement.setString(1, galleryName);
            galleryStatement.setInt(2, clientId);
            galleryStatement.executeUpdate();

            // getting gallery's id
            ResultSet galleryKeys = galleryStatement.getGeneratedKeys();
            int galleryId = -1;
            if (galleryKeys.next()) {
                galleryId = galleryKeys.getInt(1);
            }

            // adding new photos
            for (String photo : photos) {
                String photoQuerry = "INSERT INTO Photo (name, gallery_id, photo_path) VALUES (?, ?, ?)";
                PreparedStatement photoStatement = connection.prepareStatement(photoQuerry);
                photoStatement.setString(1, photo);
                photoStatement.setInt(2, galleryId);
                photoStatement.setString(3, "/static/images/" + photo); // Set the correct photo path
                photoStatement.executeUpdate();
            }
            System.out.println("Saved to database");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
