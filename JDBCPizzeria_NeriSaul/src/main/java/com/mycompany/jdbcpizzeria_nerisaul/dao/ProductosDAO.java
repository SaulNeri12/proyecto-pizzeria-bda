/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.jdbcpizzeria_nerisaul.dao;

import com.mycompany.jdbcpizzeria_nerisaul.interfaces.IConexionBD;
import com.mycompany.jdbcpizzeria_nerisaul.interfaces.IProductosDAO;
import com.mycompany.jdbcpizzeria_nerisaul.objetos.Producto;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Laboratorios
 */
public class ProductosDAO implements IProductosDAO {

    public IConexionBD conexion;

    private static final String QUERY_CONSULTAR_TODOS = "SELECT * FROM producto;";
    private static final String INSERT_AGREGAR_PRODUCTO = "INSERT INTO producto(nombre, descripcion, precio) values (?,?,?);";
    
    public ProductosDAO(IConexionBD conexion) {
        this.conexion = conexion;
    }
    
    @Override
    public List<Producto> consultarTodos() {
       
        Connection c = conexion.crearConexion();
        
        try (PreparedStatement select = c.prepareStatement(this.QUERY_CONSULTAR_TODOS, Statement.RETURN_GENERATED_KEYS)) {
            ResultSet resultados = select.executeQuery();
            
            List<Producto> productos = new ArrayList<>();
            
            while (resultados.next()) {
                Producto producto = new Producto(
                        resultados.getLong("id"),
                        resultados.getString("nombre"),
                        resultados.getString("descripcion"),
                        resultados.getFloat("precio")
                );
                
                productos.add(producto);
            }
            
            return productos;
            
        } catch (SQLException ex) {
            return null;
        }
    }

    @Override
    public Producto consultar(long id) {
        Connection c = conexion.crearConexion();
        
        try (PreparedStatement select = c.prepareStatement(this.QUERY_CONSULTAR_TODOS, Statement.RETURN_GENERATED_KEYS)) {
            ResultSet resultado = select.executeQuery();
            
            Producto producto = null;
            
            if (resultado.first()) {
                producto = new Producto(
                        resultado.getLong("id"),
                        resultado.getString("nombre"),
                        resultado.getString("descripcion"),
                        resultado.getFloat("precio")
                );
            }
            
            return producto;
            
        } catch (SQLException ex) {
            return null;
        }
    }

    @Override
    public boolean agregar(Producto producto) {
        Connection c = conexion.crearConexion();
        
        try (PreparedStatement insert = c.prepareStatement(this.INSERT_AGREGAR_PRODUCTO, Statement.RETURN_GENERATED_KEYS)) {
           
            insert.setLong(1, producto.getId());
            insert.setString(2, producto.getNombre());
            insert.setFloat(3, producto.getPrecio());
            
            ResultSet resultado = insert.executeQuery();
            
            if (resultado.isFirst()) {
                return true;
            }
            
        } catch (SQLException ex) {}
        
        return false;
    }

    @Override
    public boolean actualizar(Producto producto) {
    }

    @Override
    public boolean eliminar(long id) {
    }
    
}
