/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.jdbcpizzeria_nerisaul.dao;

import com.mycompany.jdbcpizzeria_nerisaul.interfaces.IConexionBD;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Laboratorios
 */
public class Conexion implements IConexionBD {

    String cadenaConexion = "jdbc:mysql://localhost:3306/pizzeria_bda";
    String user = "root";
    String pwd = "itson";
    
    @Override
    public Connection crearConexion() {
       Connection c;
       
        try {
            c = DriverManager.getConnection(cadenaConexion, user, pwd);
        } catch (SQLException ex) {
            return null;
        }
        
        return c;
    }
    
}
