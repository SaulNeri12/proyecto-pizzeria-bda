/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mycompany.jdbcpizzeria_nerisaul.interfaces;

import com.mycompany.jdbcpizzeria_nerisaul.objetos.Producto;
import java.util.List;

/**
 *
 * @author Laboratorios
 */
public interface IProductosDAO {
    public List<Producto> consultarTodos();
    
    public Producto consultar(long id);
    
    public boolean agregar(Producto producto);
    
    public boolean actualizar(Producto producto);
    
    public boolean eliminar(long id);
}
