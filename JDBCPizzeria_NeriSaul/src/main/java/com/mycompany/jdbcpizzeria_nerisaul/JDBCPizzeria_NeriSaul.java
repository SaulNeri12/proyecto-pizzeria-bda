/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.jdbcpizzeria_nerisaul;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Laboratorios
 */
public class JDBCPizzeria_NeriSaul {

    
    public static void main(String[] args) {
        String cadenaConexion = "jdbc:mysql://localhost:3306/pizzeria_bda";
        String user = "root";
        String pwd = "itson";
        String query = "INSERT INTO producto(nombre, precio, descripcion) VALUES (?,?,?);";
        
        try {
            Connection c = DriverManager.getConnection(cadenaConexion, user, pwd);
            PreparedStatement insert = c.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            
            /*
            insert.setString(1, "Pizza de Peperoni");
            insert.setFloat(2, 89.0f);
            insert.setString(3, "Pizza sencilla con base de tomate y queso, con topping de pepperonis.");
            insert.execute();
            
            
            insert.setString(1, "Pizza de Champinones");
            insert.setFloat(2, 85.0f);
            insert.setString(3, "Pizza sencilla con base de tomate y queso, con topping de champinones.");
            insert.execute();

            insert.setString(1, "Pizza de Queso");
            insert.setFloat(2, 75.0f);
            insert.setString(3, "Pizza sencilla con base de tomate y queso.");
            insert.execute();

            */

            query = "SELECT * FROM producto WHERE precio > ?";
            
            PreparedStatement busqueda = c.prepareStatement(query);
            busqueda.setInt(1, 50);
            
            ResultSet resultados = busqueda.executeQuery();
            
            while (resultados.next()) {
                System.out.println(String.format(
                        "Producto(id=%d, nombre=%s, precio=%.2f, descripcion=%s)",
                        resultados.getInt("id"),
                        resultados.getString("nombre"),
                        resultados.getFloat("precio"),
                        resultados.getString("descripcion")
                ));
            }
            
            c.close();
            
        } catch (SQLException ex) {
            Logger.getLogger(JDBCPizzeria_NeriSaul.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        
        
        
        
    }
}
