package com.mycompany.tienda_tecnologica;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lucas
 */


public class DatabaeConn {
    
    private static final String URL = "jdbc.sqlite:tech.db";
    
    // Connect to the database
    public static Connection connect() {
       try{
           return DriverManager.getConnection(URL);
       }catch (SQLException e) {
           System.err.println("Error al conectar con la base de datos");
           e.printStackTrace();
           return null;
       }
    }
    
    // Create table
    public static void createTable() {
        try (Connection conn = connect(); Statement stmt = conn.createStatement()){
            
            String createUsersTable = """
                                        CREATE TABLE IF NOT EXISTS user (
                                            idUser INTEGER PRIMARY KEY AUTOINCREMENT,
                                            name VARCHAR(60) NOT NULL,
                                            email VARCHAR(60) NOT NULL UNIQUE,
                                            address VARCHAR(100) NOT NULL
                                        );
                                      """;
            
            String createCategoriesTable = """
                                        CREATE TABLE IF NOT EXISTS category (
                                            idCategory INTEGUER PRIMARY KEY AUTOINCREMENT,
                                            name TEXT NOT NULL
                                        );
                                        """;
            
            String createProductsTable = """
                                        CREATE TABLE IF NOT EXITS product (
                                           idProduct INTEGUER PRIMARY KEY AUTINCREMENT,
                                           idCategory INTEGUER NOT NULL,
                                           name VARCHAR(60) NOT NULL,
                                           prize REAL NOT NULL,
                                           description VARCHAR(1000),
                                           stock INTEGER NOT NULL,
                                           FOREIGN KEY (idCategory) REFERENCES category(id)
                                        );
                                        """;
            
            String createRecordTable = """
                                        CREATE TABLE IF NOT EXISTS record(
                                            idRecord INTEGER PRIMARY KEY AUTOINCREMENT,
                                            idUser INTEGER NOT NULL,
                                            idProduct INTEGER NOT NULL,
                                            quantity INTEGER NOT NULL,
                                            date TEXT NOT NULL,
                                            FOREIGN KEY (idUser) REFERENCES user(id),
                                            FOREIGN KEY (idProduct) REFERENCES product(id)
                                       );
                                       """;
            
            stmt.execute(createUsersTable);
            stmt.execute(createCategoriesTable);
            stmt.execute(createProductsTable);
            stmt.execute(createRecordTable);
            
            System.out.println("Tablas creadas correctamente.");            
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void inserUser(int idUser, String name, String email, String address) {
        
        String query = "INSERT OR IGNORE INTO user(idUser, name, email, address) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = connect(); PreparedStatement psmt = conn.prepareStatement(query)){
            psmt.setInt(1, idUser);
            psmt.setString(2, name);
            psmt.setString(3, email);
            psmt.setString(4, address);
            psmt.executeUpdate();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public static void insertRecord (int idUser, int idProduct, int quantity, String date) {
        
        String query = "INSERT INTO record (idRecord, idUser, idProduct, quantity, date) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = connect(); PreparedStatement psmt = conn.prepareStatement(query)){
            psmt.setInt(1, idUser);
            psmt.setInt(2, idProduct);
            psmt.setInt(3, quantity);
            psmt.setString(4, date);
            psmt.executeUpdate();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    // Retrieve all users
    public static List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        
        String query = "SELECT * FROM user";
        
        try (Connection conn = connect(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                users.add(new User(rs.getInt("idUser"), rs.getString("name"), rs.getString("email"), rs.getString("address")));
            } 
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return users;
        
    }
}
