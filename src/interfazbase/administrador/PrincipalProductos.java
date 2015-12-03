/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfazbase.administrador;

import interfazbase.Ingresar;
import interfazbase.administrador.MenuAdmin;
import interfazbase.empleado.MenuEmpleado;
import interfazbase.clases.conexion;
import java.awt.HeadlessException;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRPdfExporter;

/**
 *
 * @author SENA
 */
public class PrincipalProductos extends javax.swing.JFrame {

    public int selectedRow;
    public boolean estaticaEnviado;
    public boolean mensajeEnviado;
    public JTextField campo_enviar;
    public KeyEvent evento_enviar;
    boolean mensajeTabla;
    double totalRegistros = 0;
    int totalPaginas = 0;
    double maxReg = 32;
    public int paginaActual = 1;
    public int paginaSiguiente;

    /**
     * Creates new form PrincipalClientes
     */
    public PrincipalProductos() {

        boolean mensajeTabla = false;
        estaticaEnviado = false;
        mensajeEnviado = false;
        initComponents();
        setLocationRelativeTo(null);
        setResizable(false);
        setTitle("PRECAR - Inventario");
        mostrardatos("", 0);
        desactBotonesPaginas();

    }

    public void desactBotonesPaginas() {
        /*
         Desactivar el botón si no hay páginas anteriores
         */
        String actual = txtPagAct.getText();
        String total = txtPagTotal.getText();

        if ("1".equals(actual)) {

            btnAnterior.setEnabled(false);
        }
        if (!("1".equals(actual))) {

            btnAnterior.setEnabled(true);
        }

        /*
         Desactivar el botón si no hay páginas siguientes
         */
        if (actual.equals(total)) {
            btnSiguiente.setEnabled(false);

        }
        if ((!actual.equals(total))) {
            btnSiguiente.setEnabled(true);

        }
    }

    public void paginaSiguiente() {
        /*
         Método para calcular qué pagina sigue en la tabla
         */
        try {

            int paginaActual = (Integer.parseInt(txtPagAct.getText()));

            paginaSiguiente = (paginaActual + 1);

            int offsetEnviar = (paginaSiguiente - 1) * 32;

            mostrardatos("", offsetEnviar);
        } catch (Exception ex) {
            Logger.getLogger(PrincipalClientes.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void paginaAnterior() {
        /*
         Método para calcular qué pagina sigue en la tabla
         */
        try {

            int paginaActual = (Integer.parseInt(txtPagAct.getText()));

            paginaSiguiente = (paginaActual - 1);

            int offsetEnviar = (paginaSiguiente - 1) * 32;

            mostrardatos("", offsetEnviar);
        } catch (Exception ex) {
            Logger.getLogger(PrincipalClientes.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    void mostrardatos(String valor, int offset) {
        /*
         Método para cargar los datos de la BD al jtable
        
         */

        DefaultTableModel modelo = new DefaultTableModel();
        modelo.addColumn("Codigo");
        modelo.addColumn("Nombre");
        modelo.addColumn("Marca");
        modelo.addColumn("Precio");
        modelo.addColumn("Cantidad");
        modelo.addColumn("Descripcion");
        jTable1.setModel(modelo);
        String sql = "";

        totalRegistros = 0;
        if (valor.equals("")) {

            String getTotalRows = "select * from tblproductos";
            try {
                Statement stTotal = cn.createStatement();
                ResultSet reTotal = stTotal.executeQuery(getTotalRows);

                while (reTotal.next()) {
                    totalRegistros = totalRegistros + 1;
                }
                totalPaginas = (int) Math.ceil(totalRegistros / maxReg);

                txtPagAct.setText(paginaActual + "");
                txtPagTotal.setText("" + totalPaginas);
                jTable1.setModel(modelo);

            } catch (SQLException ex) {
                Logger.getLogger(PrincipalClientes.class.getName()).log(Level.SEVERE, null, ex);
            }

            sql = "select * from tblproductos order by id_producto  OFFSET " + offset + " ROWS FETCH NEXT 32 ROWS ONLY";
        } else {
            sql = "select * from tblproductos where id_producto=" + valor + "";
        }

        Object[] datos = new Object[6];

        try {

            Statement st = cn.createStatement();
            ResultSet re = st.executeQuery(sql);

            while (re.next()) {
                datos[0] = re.getInt(1);
                datos[1] = re.getString(2);
                datos[2] = re.getString(3);
                datos[3] = re.getInt(4);
                datos[4] = re.getInt(5);
                datos[5] = re.getString(6);

                modelo.addRow(datos);
            }

            jTable1.setModel(modelo);
        } catch (SQLException ex) {
            Logger.getLogger(PrincipalProductos.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        txt_desc_pro = new javax.swing.JTextField();
        txt_cod_pro = new javax.swing.JTextField();
        txt_nom_pro = new javax.swing.JTextField();
        txt_marca_pro = new javax.swing.JTextField();
        txt_prec_pro = new javax.swing.JTextField();
        jButton8 = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        txt_cant_pro = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jSeparator2 = new javax.swing.JSeparator();
        txt_buscar = new javax.swing.JTextField();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jButton10 = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        jSeparator3 = new javax.swing.JSeparator();
        btnAnterior = new javax.swing.JButton();
        jTextField2 = new javax.swing.JTextField();
        txtPagAct = new javax.swing.JTextField();
        jTextField1 = new javax.swing.JTextField();
        btnSiguiente = new javax.swing.JButton();
        txtPagTotal = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setText("Productos");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 0, -1, 44));

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(jTable1);

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 30, 850, 500));

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/eraser.png"))); // NOI18N
        jButton1.setText("Eliminar producto seleccionado");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(930, 400, 330, 60));

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel3.setText("Descripcion: ");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 210, 140, 30));

        jLabel4.setText("Codigo producto:");
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, 140, 30));

        jLabel6.setText("Marca: ");
        jPanel1.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 90, 140, 30));

        jLabel7.setText("Precio:");
        jPanel1.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 130, 140, 30));
        jPanel1.add(txt_desc_pro, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 210, 190, 30));

        txt_cod_pro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_cod_proActionPerformed(evt);
            }
        });
        jPanel1.add(txt_cod_pro, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 10, 190, 30));
        jPanel1.add(txt_nom_pro, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 50, 190, 30));
        jPanel1.add(txt_marca_pro, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 90, 190, 30));
        jPanel1.add(txt_prec_pro, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 130, 190, 30));

        jButton8.setText("Limpiar");
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton8, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 250, 70, 30));

        jLabel8.setText("Cantidad: ");
        jPanel1.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 170, 140, 30));
        jPanel1.add(txt_cant_pro, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 170, 190, 30));

        jLabel5.setText("Nombre producto: ");
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 50, 140, 30));

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/save16.png"))); // NOI18N
        jButton2.setText("Ingresar nuevo producto");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 250, 210, 30));

        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/dbdown.png"))); // NOI18N
        jButton3.setText("Guardar cambios");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 290, 290, 60));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(930, 30, 330, 360));
        getContentPane().add(jSeparator2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 660, 1240, 20));

        txt_buscar.setText("Buscar producto por codigo...");
        getContentPane().add(txt_buscar, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 600, 270, 30));

        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/search.png"))); // NOI18N
        jButton4.setText("Buscar");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton4, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 590, 210, 40));

        jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/refresh.png"))); // NOI18N
        jButton5.setText("Actualizar tabla");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton5, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 590, 330, 40));

        jButton6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/print.png"))); // NOI18N
        jButton6.setText("Reporte");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton6, new org.netbeans.lib.awtextra.AbsoluteConstraints(930, 590, 320, 40));

        jButton7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/write.png"))); // NOI18N
        jButton7.setText("Actualizar producto seleccionado");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton7, new org.netbeans.lib.awtextra.AbsoluteConstraints(930, 470, 330, 60));

        jButton10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/back.png"))); // NOI18N
        jButton10.setText("Regresar");
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton10, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 680, 170, 60));
        getContentPane().add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 90, -1, -1));
        getContentPane().add(jSeparator3, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 580, 1230, 10));

        btnAnterior.setText("Anterior");
        btnAnterior.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAnteriorActionPerformed(evt);
            }
        });
        getContentPane().add(btnAnterior, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 540, 130, 30));

        jTextField2.setText(" Página");
        jTextField2.setBorder(null);
        jTextField2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField2ActionPerformed(evt);
            }
        });
        getContentPane().add(jTextField2, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 540, 40, 30));

        txtPagAct.setBorder(null);
        getContentPane().add(txtPagAct, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 540, 30, 30));

        jTextField1.setText("de");
        jTextField1.setBorder(null);
        getContentPane().add(jTextField1, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 540, 20, 30));

        btnSiguiente.setText("Siguiente");
        btnSiguiente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSiguienteActionPerformed(evt);
            }
        });
        getContentPane().add(btnSiguiente, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 540, 130, 30));

        txtPagTotal.setBorder(null);
        txtPagTotal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPagTotalActionPerformed(evt);
            }
        });
        getContentPane().add(txtPagTotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 540, 40, 30));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        try {
            //query insertar
            PreparedStatement pst = cn.prepareStatement("INSERT INTO tblproductos (id_producto, nombre_producto, marca_producto, precio_producto, cantidad_producto, descripcion_producto) VALUES (?,?,?,?,?,?)");

            int id = Integer.parseInt(txt_cod_pro.getText());
            int precio = Integer.parseInt(txt_prec_pro.getText());
            int cant = Integer.parseInt(txt_cant_pro.getText());

            pst.setInt(1, id);
            pst.setString(2, txt_nom_pro.getText());
            pst.setString(3, txt_marca_pro.getText());
            pst.setInt(4, precio);
            pst.setInt(5, cant);
            pst.setString(6, txt_desc_pro.getText());

            pst.executeUpdate();
            mostrardatos("", 0);

        } catch (Exception e) {
            System.out.print(e.getMessage());

        }

    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        int fila = jTable1.getSelectedRow();
        Object id_producto = "";
        id_producto = jTable1.getValueAt(fila, 0).toString();

        mostrardatos("", 0);

        try {
            PreparedStatement pst = cn.prepareStatement("delete from tblproductos where id_producto=" + id_producto);
            pst.executeUpdate();
        } catch (Exception e) {

        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        mostrardatos(txt_buscar.getText(), 0);

    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        mostrardatos("", 0);


    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        //this fills the text fields with the table informatiion
        int fila = jTable1.getSelectedRow();
        if (fila >= 0) {
            txt_cod_pro.setText(jTable1.getValueAt(fila, 0).toString());
            txt_nom_pro.setText(jTable1.getValueAt(fila, 1).toString());
            txt_marca_pro.setText(jTable1.getValueAt(fila, 2).toString());
            txt_prec_pro.setText(jTable1.getValueAt(fila, 3).toString());
            txt_cant_pro.setText(jTable1.getValueAt(fila, 4).toString());
            txt_desc_pro.setText(jTable1.getValueAt(fila, 5).toString());

        } else {
            JOptionPane.showMessageDialog(null, "Seleccione una fila");
        }

    }//GEN-LAST:event_jButton7ActionPerformed

    private void txt_cod_proActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_cod_proActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_cod_proActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed

        txt_cod_pro.setText(null);
        txt_nom_pro.setText(null);
        txt_marca_pro.setText(null);
        txt_prec_pro.setText(null);
        txt_cant_pro.setText(null);
        txt_desc_pro.setText(null);

    }//GEN-LAST:event_jButton8ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        try {
            PreparedStatement pst = cn.prepareStatement("update tblproductos set nombre_producto='" + txt_nom_pro.getText() + "',marca_producto='" + txt_marca_pro.getText() + "',precio_producto=" + txt_prec_pro.getText() + ",cantidad_producto=" + txt_cant_pro.getText() + ",descripcion_producto='" + txt_desc_pro.getText() + "'where id_producto=" + txt_cod_pro.getText() + "");
            pst.executeUpdate();

            //actualizar
            mostrardatos("", 0);

        } catch (SQLException ex) {
            Logger.getLogger(PrincipalProductos.class.getName()).log(Level.SEVERE, null, ex);
            System.out.print(ex.getMessage());
        }

    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
        /*
         Compara la variable que almacena el tipo de usuario que ingresó al 
         sistema
         */

        MenuAdmin ventAdmin = new MenuAdmin();
        ventAdmin.setVisible(true);
        dispose();
    }//GEN-LAST:event_jButton10ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection conectar = DriverManager.getConnection("jdbc:mysql://localhost/baseinterfaz", "root", "");

            JFileChooser fileChooser = new JFileChooser();
            fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("Todos los archivos *.PDF", "pdf", "PDF"));
            int seleccion = fileChooser.showSaveDialog(null);
            try {
                if (seleccion == JFileChooser.APPROVE_OPTION) {
                    File JFC = fileChooser.getSelectedFile();
                    String Path = JFC.getAbsolutePath();
                    if (!(Path.endsWith(".pdf"))) {
                        File temp = new File(Path + ".pdf");
                        JFC.renameTo(temp);
                    }

                    JOptionPane.showMessageDialog(null, "Generando reporte", "", JOptionPane.WARNING_MESSAGE);
                    JOptionPane.showMessageDialog(null, "Reporte generado exitosamente.", "", JOptionPane.INFORMATION_MESSAGE);

                    JasperReport report = JasperCompileManager.compileReport("src/Reportes/reporte_productos.jrxml");
                    JasperPrint print = JasperFillManager.fillReport(report, null, conectar);
                    JRExporter exporter = new JRPdfExporter();
                    exporter.setParameter(JRExporterParameter.JASPER_PRINT, print);
                    exporter.setParameter(JRExporterParameter.OUTPUT_FILE, new java.io.File(Path));
                    exporter.exportReport();
                }

            } catch (JRException ex) {
                System.out.print(ex.getMessage());
            }
        } catch (Exception e) {
        }
    }//GEN-LAST:event_jButton6ActionPerformed

    private void btnAnteriorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAnteriorActionPerformed
        /*
         Boton para ir a la anterior pagina de la tabla
         */
        paginaAnterior();
        txtPagAct.setText(String.valueOf(paginaSiguiente));
    }//GEN-LAST:event_btnAnteriorActionPerformed

    private void jTextField2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField2ActionPerformed

    private void btnSiguienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSiguienteActionPerformed
        /*
         Boton para ir a la siguiente pagina de la tabla
         */

        paginaSiguiente();
        txtPagAct.setText(String.valueOf(paginaSiguiente));
    }//GEN-LAST:event_btnSiguienteActionPerformed

    private void txtPagTotalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPagTotalActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPagTotalActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Windows look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Windows (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(PrincipalProductos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(PrincipalProductos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(PrincipalProductos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(PrincipalProductos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new PrincipalProductos().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAnterior;
    private javax.swing.JButton btnSiguiente;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField txtPagAct;
    private javax.swing.JTextField txtPagTotal;
    private javax.swing.JTextField txt_buscar;
    private javax.swing.JTextField txt_cant_pro;
    private javax.swing.JTextField txt_cod_pro;
    private javax.swing.JTextField txt_desc_pro;
    private javax.swing.JTextField txt_marca_pro;
    private javax.swing.JTextField txt_nom_pro;
    private javax.swing.JTextField txt_prec_pro;
    // End of variables declaration//GEN-END:variables

    conexion cc = new conexion();
    Connection cn = cc.conexion();
    Ingresar ingresar = new Ingresar();

}
