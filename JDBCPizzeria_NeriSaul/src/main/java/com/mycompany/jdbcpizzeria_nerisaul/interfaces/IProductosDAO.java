/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mycompany.jdbcpizzeria_nerisaul.interfaces;

import com.mycompany.jdbcpizzeria_nerisaul.excepciones.DAOException;
import com.mycompany.jdbcpizzeria_nerisaul.objetos.Producto;
import java.util.List;

/**
 *
 * @author Saul Neri
 */
public interface IProductosDAO {
    public List<Producto> consultarTodos() throws DAOException;
    
    public Producto consultar(long id) throws DAOException;
    
    public void agregar(Producto producto) throws DAOException;
    
    public void actualizar(Producto producto) throws DAOException;
    
    public void eliminar(long id) throws DAOException;
}
