/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.mycompany.jdbcpizzeria_nerisaul.gui;

import com.mycompany.jdbcpizzeria_nerisaul.dao.Conexion;
import com.mycompany.jdbcpizzeria_nerisaul.dao.ProductosDAO;
import com.mycompany.jdbcpizzeria_nerisaul.excepciones.DAOException;
import com.mycompany.jdbcpizzeria_nerisaul.interfaces.IConexionBD;
import com.mycompany.jdbcpizzeria_nerisaul.interfaces.IProductosDAO;
import com.mycompany.jdbcpizzeria_nerisaul.objetos.Producto;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Saul Neri
 */
public class FrmProductos extends javax.swing.JFrame {

    private DefaultTableModel modeloTabla;
    private IProductosDAO productos;
    private Producto productoSeleccionado;
    private int idProductoSeleccionado;
    
    /**
     * Creates new form FrmProductos
     */
    public FrmProductos() {
        initComponents();
    
        this.setTitle("Formulario Productos");
        
        IConexionBD conexion = new Conexion();
        productos = new ProductosDAO(conexion);
        
        this.btnActualizarProducto.setEnabled(false);
        this.btnEliminarProducto.setEnabled(false);
        
        inicializaTablaProductos(); 
        mostrarProductosTabla();
    }
      
    
    private void inicializaTablaProductos() {
        modeloTabla = new DefaultTableModel();
        
        modeloTabla.addColumn("ID");
        modeloTabla.addColumn("Nombre");
        modeloTabla.addColumn("Precio");
        modeloTabla.addColumn("Descripcion");
        
        this.tablaProductos.setModel(modeloTabla);
        
        tablaProductos.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                int selectedRow = tablaProductos.getSelectedRow();
                if (selectedRow != -1) {
                    long id = (Long) modeloTabla.getValueAt(selectedRow, 0);
                    String nombre = (String) modeloTabla.getValueAt(selectedRow, 1);
                    String descripcion = (String) modeloTabla.getValueAt(selectedRow, 2);
                    Float precio = (Float) modeloTabla.getValueAt(selectedRow, 3);
                    
                    productoSeleccionado = new Producto(id, nombre, descripcion, precio);
                    
                    idProductoSeleccionado = selectedRow;
                    
                    // se cambia el contenido de los campos...
                    campoTextoNombre.setText(nombre);
                    campoTextoDescripcion.setText(descripcion);
                    campoTextoPrecio.setText(precio.toString());
                    
                    btnActualizarProducto.setEnabled(true);
                    btnEliminarProducto.setEnabled(true);
                } else {
                    // ninguno seleccionado
                    btnActualizarProducto.setEnabled(false);
                    btnEliminarProducto.setEnabled(false);
                }
            }
        });
        
        this.tablaProductos.repaint();
    }
    
    private void mostrarProductosTabla() {
        List<Producto> lista;
        
        int filas = this.modeloTabla.getRowCount() - 1;
        for (int i=filas - 1; i >= 0; i--) {
            this.modeloTabla.removeRow(i);
        }
        
        this.tablaProductos.repaint();
        
        try {
            lista = this.productos.consultarTodos();
            
            for (Producto p: lista) {
                Object[] objeto = new Object[4];
                objeto[0] = p.getId();
                objeto[1] = p.getNombre();
                objeto[2] = p.getDescripcion();
                objeto[3] = p.getPrecio();
                
                this.modeloTabla.addRow(objeto);
            }
         
            this.tablaProductos.repaint();
        } catch (DAOException ex) {
            JOptionPane.showConfirmDialog(
                    this, 
                    "Tabla Erronea", 
                    ex.getMessage(), 
                    JOptionPane.WARNING_MESSAGE
            );
        }
    }
    
    private void limpiarCampos() {
        this.campoTextoDescripcion.setText("");
        this.campoTextoNombre.setText("");
        this.campoTextoPrecio.setText("");
    }
    
    private void registrarProducto() {
        String nombreProducto = this.campoTextoNombre.getText();
        String descripcionProducto = this.campoTextoDescripcion.getText();
        String precioProducto = this.campoTextoPrecio.getText();
        
        
        if (nombreProducto.isBlank() || nombreProducto.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nombre Erroneo", "Ingresa un nombre para el producto", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (descripcionProducto.isBlank() || descripcionProducto.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Descripcion Erronea", "Ingresa la descripcion del producto", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        Float precioProductoFloat = Float.valueOf(precioProducto);
        
        if (precioProducto.isBlank() || precioProducto.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Precio Erroneo", "Ingresa el precio del producto en el formato correcto", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (precioProductoFloat.isNaN() || precioProductoFloat < 0) {
            JOptionPane.showMessageDialog(this, "Precio Erroneo", "El precio dado es incorrecto", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        Producto producto = new Producto();
        
        producto.setNombre(nombreProducto);
        producto.setDescripcion(descripcionProducto);
        producto.setPrecio(precioProductoFloat);
        
        try {
            productos.agregar(producto);
            
            JOptionPane.showMessageDialog(
                    this, 
                    "Agregar Producto", 
                    "El producto ha sido anadido con exito", 
                    JOptionPane.INFORMATION_MESSAGE
            );
            
            limpiarCampos();
        } catch (DAOException ex) {
            JOptionPane.showMessageDialog(
                    this, 
                    "Agregar Producto", 
                    ex.getMessage(), 
                    JOptionPane.ERROR_MESSAGE
            );
        }
        
        mostrarProductosTabla();
    }
    
    private void actualizarProducto() {
        if (productoSeleccionado != null) {
            int opcion = JOptionPane.showConfirmDialog(
                    this, 
                    String.format(
                            "Desea actualizar el producto[id: %d]?", 
                            productoSeleccionado.getId()
                    )
            );
            
            String nombreProducto = this.campoTextoNombre.getText();
            String descripcionProducto = this.campoTextoDescripcion.getText();
            String precioProducto = this.campoTextoPrecio.getText();

            if (nombreProducto.isBlank() || nombreProducto.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nombre Erroneo", "Ingresa un nombre para el producto", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (descripcionProducto.isBlank() || descripcionProducto.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Descripcion Erronea", "Ingresa la descripcion del producto", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Float precioProductoFloat = Float.valueOf(precioProducto);

            if (precioProducto.isBlank() || precioProducto.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Precio Erroneo", "Ingresa el precio del producto en el formato correcto", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (precioProductoFloat.isNaN() || precioProductoFloat < 0) {
                JOptionPane.showMessageDialog(this, "Precio Erroneo", "El precio dado es incorrecto", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            productoSeleccionado.setNombre(nombreProducto);
            productoSeleccionado.setDescripcion(descripcionProducto);
            productoSeleccionado.setPrecio(precioProductoFloat);
            
            if (opcion == JOptionPane.OK_OPTION) {
                try {
                    this.productos.actualizar(productoSeleccionado);
                    JOptionPane.showMessageDialog(this, "El producto ha sido actualizado con exito");
                    mostrarProductosTabla();
                } catch (DAOException ex) {
                    JOptionPane.showMessageDialog(this, "Actualizar Producto", ex.getMessage(), JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }
    
    private void eliminarProducto() {
        if (productoSeleccionado != null) {
            int opcion = JOptionPane.showConfirmDialog(
                    this, 
                    String.format(
                            "Desea eliminar el producto[id: %d]'%s'?", 
                            productoSeleccionado.getId(),
                            productoSeleccionado.getNombre()
                    )
            );
            
            if (opcion == JOptionPane.OK_OPTION) {
                try {
                    this.productos.eliminar(productoSeleccionado.getId());
                    JOptionPane.showMessageDialog(this, "El producto ha sido eliminado con exito");
                    
                    modeloTabla.removeRow(idProductoSeleccionado);
                    
                    mostrarProductosTabla();
                } catch (DAOException ex) {
                    JOptionPane.showMessageDialog(this, "Eliminar Producto", ex.getMessage(), JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }
    
    
    //private void actualizar

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        campoTextoPrecio = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        campoTextoNombre = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        campoTextoDescripcion = new javax.swing.JTextArea();
        btnActualizarProducto = new javax.swing.JButton();
        btnAgregarProducto = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tablaProductos = new javax.swing.JTable();
        btnEliminarProducto = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Noto Sans Lisu", 1, 18)); // NOI18N
        jLabel1.setText("Productos");

        campoTextoPrecio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                campoTextoPrecioActionPerformed(evt);
            }
        });

        jLabel2.setText("Nombre:");

        jLabel3.setText("Descripcion:");

        campoTextoNombre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                campoTextoNombreActionPerformed(evt);
            }
        });

        jLabel4.setText("Precio:");

        campoTextoDescripcion.setColumns(20);
        campoTextoDescripcion.setRows(5);
        jScrollPane1.setViewportView(campoTextoDescripcion);

        btnActualizarProducto.setText("Actualizar");
        btnActualizarProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnActualizarProductoActionPerformed(evt);
            }
        });

        btnAgregarProducto.setText("Agregar");
        btnAgregarProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarProductoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 232, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnActualizarProducto)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnAgregarProducto))
                    .addComponent(campoTextoNombre)
                    .addComponent(campoTextoPrecio)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addGap(8, 8, 8))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(22, 22, 22)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(campoTextoNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(campoTextoPrecio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnActualizarProducto)
                    .addComponent(btnAgregarProducto))
                .addContainerGap())
        );

        jSeparator1.setOrientation(javax.swing.SwingConstants.VERTICAL);

        tablaProductos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane2.setViewportView(tablaProductos);

        btnEliminarProducto.setText("Eliminar");
        btnEliminarProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarProductoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnEliminarProducto)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 547, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(17, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(72, 72, 72)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 384, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnEliminarProducto)
                .addContainerGap(28, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void campoTextoPrecioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_campoTextoPrecioActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_campoTextoPrecioActionPerformed

    private void campoTextoNombreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_campoTextoNombreActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_campoTextoNombreActionPerformed

    private void btnActualizarProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnActualizarProductoActionPerformed
        actualizarProducto();
    }//GEN-LAST:event_btnActualizarProductoActionPerformed

    private void btnAgregarProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarProductoActionPerformed
        registrarProducto();
    }//GEN-LAST:event_btnAgregarProductoActionPerformed

    private void btnEliminarProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarProductoActionPerformed
        eliminarProducto();
    }//GEN-LAST:event_btnEliminarProductoActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(FrmProductos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FrmProductos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FrmProductos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FrmProductos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FrmProductos().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnActualizarProducto;
    private javax.swing.JButton btnAgregarProducto;
    private javax.swing.JButton btnEliminarProducto;
    private javax.swing.JTextArea campoTextoDescripcion;
    private javax.swing.JTextField campoTextoNombre;
    private javax.swing.JTextField campoTextoPrecio;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTable tablaProductos;
    // End of variables declaration//GEN-END:variables
}
