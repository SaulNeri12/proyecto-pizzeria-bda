/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.jdbcpizzeria_nerisaul;

import com.mycompany.jdbcpizzeria_nerisaul.dao.Conexion;
import com.mycompany.jdbcpizzeria_nerisaul.dao.ProductosDAO;
import com.mycompany.jdbcpizzeria_nerisaul.excepciones.DAOException;
import com.mycompany.jdbcpizzeria_nerisaul.interfaces.IConexionBD;
import com.mycompany.jdbcpizzeria_nerisaul.interfaces.IProductosDAO;
import com.mycompany.jdbcpizzeria_nerisaul.objetos.Producto;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Saul Neri
 */
public class PruebaProductosDAO {
    public static void main(String[] args) {
        IConexionBD conexion = new Conexion();
        
        IProductosDAO productos = new ProductosDAO(conexion);
        
        Producto pizzaCarneAsada = new Producto(0, "Pizza de Sajsahjhfja", "Pizza sencilla con base de tomate, queso y cubierta de carne asada", 95.f);
        Producto pizzaPeperoni = new Producto(0, "Pizza de Peperoni", "Pizza sencilla con base de tomate, queso y peperoni", 80.f);
        
        // insercion de 4 pizzas
        try {
            productos.agregar(pizzaPeperoni);
            System.out.println("Se agrego el producto: Pizza de Peperoni");
            
            productos.agregar(new Producto(0, "Pizza de Queso", "Pizza sencilla con base de tomate y queso", 65.f));
            System.out.println("Se agrego el producto: Pizza de Queso");
            
            productos.agregar(new Producto(0, "Pizza de Champinones", "Pizza sencilla con base de tomate, queso y cubierta de champinones", 85.f));
            System.out.println("Se agrego el producto: Pizza de Queso");
            
            productos.agregar(pizzaCarneAsada);
            System.out.println("Se agrego el producto: Pizza de Sajsahjhja");
            
        } catch (DAOException ex) {
            System.out.println(ex.getMessage());
        }
        
        // modificacion de una pizza
        pizzaCarneAsada.setId(4); // NOTE: ES LA 4ta PIZZA EN SER ANADIDA
        pizzaCarneAsada.setNombre("Pizza de Carne Asada");
        
        try {
            productos.actualizar(pizzaCarneAsada);
            System.out.println("Se actualizo el producto: " + pizzaCarneAsada);
        } catch (DAOException ex) {
            System.out.println(ex.getMessage());
        }
        
        List<Producto> listaPizzas;
        
        // consultar todos los productos
        try {
            listaPizzas = productos.consultarTodos();
            
            System.out.println("Productos encontrados: " + listaPizzas.size());
            
            for (Producto p: listaPizzas) {
                System.out.println(p);
            }
            
        } catch (DAOException ex) {
            System.out.println(ex.getMessage());
        }
        
        // eliminar producto
        try {
            productos.eliminar(1);
            System.out.println("Se elimino la pizza de peperoni!!!");
        } catch (DAOException ex) {
            System.out.println(ex.getMessage());
        }
        
        try {
            listaPizzas = productos.consultarTodos();
            
            System.out.println("Productos encontrados: " + listaPizzas.size());
            
            for (Producto p: listaPizzas) {
                System.out.println(p);
            }
            
        } catch (DAOException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
