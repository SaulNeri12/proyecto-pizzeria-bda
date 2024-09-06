/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.jdbcpizzeria_nerisaul.dao;

import com.mycompany.jdbcpizzeria_nerisaul.excepciones.DAOException;
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
/**
 *
 * @author Saul Neri
 */
public class ProductosDAO implements IProductosDAO {

    public IConexionBD conexion;

    private static final String QUERY_CONSULTAR_TODOS       = "SELECT * FROM producto;";
    private static final String QUERY_CONSULTAR_PRODUCTO    = "SELECT * FROM producto WHERE id = ?;";
    private static final String INSERT_AGREGAR_PRODUCTO     = "INSERT INTO producto(nombre, descripcion, precio) values (?,?,?);";
    private static final String UPDATE_ACTUALIZAR_PRODUCTO  = "UPDATE producto SET nombre = ?, descripcion = ?, precio = ? WHERE id = ?;";
    private static final String DELETE_ELIMINAR_PRODUCTO    = "DELETE FROM producto WHERE id = ?;";
    
    public ProductosDAO(IConexionBD conexion) {
        this.conexion = conexion;
    }
    
    @Override
    public List<Producto> consultarTodos() throws DAOException {
       
        
        
        try (Connection c = conexion.crearConexion();
                PreparedStatement select = c.prepareStatement(ProductosDAO.QUERY_CONSULTAR_TODOS, Statement.RETURN_GENERATED_KEYS)) {
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
            throw new DAOException("Ocurrio un error al traer la lista de productos, intente mas tarde...");
        }
    }

    @Override
    public Producto consultar(long id) throws DAOException {
        
        
        try (Connection c = conexion.crearConexion();
                PreparedStatement select = c.prepareStatement(ProductosDAO.QUERY_CONSULTAR_PRODUCTO, Statement.RETURN_GENERATED_KEYS)) {
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
            throw new DAOException("Ocurrio un error en la consulta del producto, intente mas tarde...");
        }
    }

    @Override
    public void agregar(Producto producto) throws DAOException {
        
        
        try (Connection c = conexion.crearConexion();
                PreparedStatement insert = c.prepareStatement(ProductosDAO.INSERT_AGREGAR_PRODUCTO, Statement.RETURN_GENERATED_KEYS)) {
           
            insert.setString(1, producto.getNombre());
            insert.setString(2, producto.getDescripcion());
            insert.setFloat(3, producto.getPrecio());
            
            int insertados = insert.executeUpdate();
            
            if (insertados < 1) {
                throw new DAOException("No se pudo registrar el producto");
            }
            
        } catch (SQLException ex) {
            //throw new DAOException(ex.getMessage());
            throw new DAOException("Ocurrio un error al intentar registrar el producto, intente mas tarde...");
        }
    }

    @Override
    public void actualizar(Producto producto) throws DAOException {
        
        try (Connection c = conexion.crearConexion();
                PreparedStatement update = c.prepareStatement(ProductosDAO.UPDATE_ACTUALIZAR_PRODUCTO)) {
            
            update.setString(1, producto.getNombre());
            update.setString(2, producto.getDescripcion());
            update.setFloat(3, producto.getPrecio());
            update.setLong(4, producto.getId());
            
            int modificados = update.executeUpdate();
            
            if (modificados < 1) {
                throw new DAOException("No se encontro el producto a actualizar");
            }
            
        } catch (SQLException ex) {
            //throw new DAOException(ex.getMessage());
            throw new DAOException("Ocurrio un error al intentar actualizar la informacion del producto, intente mas tarde...");

        }
    }

    @Override
    public void eliminar(long id) throws DAOException {
        
        
        try (Connection c = conexion.crearConexion();
                PreparedStatement delete = c.prepareStatement(ProductosDAO.DELETE_ELIMINAR_PRODUCTO, Statement.RETURN_GENERATED_KEYS)) {
           
            delete.setLong(1, id);
            
            int eliminados = delete.executeUpdate();
            
            if (eliminados < 1) {
                throw new DAOException("No se pudo eliminar el producto");
            }
            
        } catch (SQLException ex) {
            throw new DAOException("Ocurrio un error al intentar eliminar el producto, intente mas tarde...");
        }
    }
}
