/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfazbase.empleado;

import interfazbase.*;
import interfazbase.administrador.MenuAdmin;
import interfazbase.empleado.MenuEmpleado;
import interfazbase.clases.conexion;
import interfazbase.clases.validacionCampos;
import java.awt.Component;
import java.awt.TextComponent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.export.JRPdfExporter;

/**
 *
 * @author SENA
 */
public class PrincipalClientesEmp extends javax.swing.JFrame {

    public int selectedRow;
    public boolean estaticaEnviado;
    public boolean mensajeEnviado;
    public JTextField campo_enviar;
    public KeyEvent evento_enviar;
    boolean mensajeTabla;
     int totalRegistros=0;
        int totalPaginas=0;
        int  maxReg=8;
        int paginaActual=1;


    /**
     * Creates new form PrincipalClientes
     */
    public PrincipalClientesEmp() {

        boolean mensajeTabla = false;
        estaticaEnviado = false;
        mensajeEnviado = false;
        initComponents();
        setLocationRelativeTo(null);
        setResizable(false);
        setTitle("PRECAR - Clientes");
        mostrardatos("");
        
    }

    void mostrardatos(String valor) {
        
        DefaultTableModel modelo = new DefaultTableModel();
        modelo.addColumn("Cedula");
        modelo.addColumn("Nombre");
        modelo.addColumn("Apellido");
        modelo.addColumn("Telefono");
        modelo.addColumn("Direccion");
        jTable1.setModel(modelo);
        String sql = "";

        if (valor.equals("")) {
            sql = "select * from tblclientes order by cedula_cliente /n" +
                   "OFFSET 1 ROWS FETCH NEXT 8 ROWS ONLY";
        } else {
            sql = "select * from tblclientes where cedula_cliente=" + valor + "";
        }
        Object[] datos = new Object[5];
        //query
        try {
            Statement st = cn.createStatement();
            ResultSet re = st.executeQuery(sql);

            while (re.next()) {
                
                 totalRegistros = totalRegistros + 1;
                datos[0] = re.getInt(1);
                datos[1] = re.getString(2);
                datos[2] = re.getString(3);
                datos[3] = re.getInt(4);
                datos[4] = re.getString(5);

                modelo.addRow(datos);
                
                 //System.out.println(totalRegistros);
                totalPaginas= Math.round(totalRegistros/maxReg)+1;
                //System.out.println("TotalPaginas  = " + totalRegistros + "/ " + maxReg + " = " + totalPaginas);
                
                
            }
            System.out.print(totalPaginas);
            jTable1.setModel(modelo);
        } catch (SQLException ex) {
            Logger.getLogger(PrincipalClientesEmp.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

//mostrardatos("");
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
        jButton3 = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        jPanel1 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        txt_dir_cli = new javax.swing.JTextField();
        txt_ced_cli = new javax.swing.JTextField();
        txt_nom_cli = new javax.swing.JTextField();
        txt_ape_cli = new javax.swing.JTextField();
        txt_tel_cli = new javax.swing.JTextField();
        jButton8 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jSeparator2 = new javax.swing.JSeparator();
        txt_buscar = new javax.swing.JTextField();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();
        jSeparator3 = new javax.swing.JSeparator();
        jSeparator4 = new javax.swing.JSeparator();
        jSeparator5 = new javax.swing.JSeparator();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setText("Clientes");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 0, -1, 44));

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(jTable1);

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 430, 860, 160));

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/eraser.png"))); // NOI18N
        jButton1.setText("Eliminar cliente");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 240, 210, 80));

        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/dbdown.png"))); // NOI18N
        jButton3.setText("Guardar infotmacion del cliente");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 140, 430, 80));
        getContentPane().add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 230, 430, 10));

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel3.setText("Direccion cliente: ");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 180, 110, 30));

        jLabel4.setText("Cedula cliente: ");
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 20, 90, 30));

        jLabel5.setText("Nombre cliente: ");
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 60, 90, 30));

        jLabel6.setText("Apellido cliente:");
        jPanel1.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 100, 80, 30));

        jLabel7.setText("Telefono cliente:");
        jPanel1.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 140, 110, 30));

        txt_dir_cli.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt_dir_cliKeyPressed(evt);
            }
        });
        jPanel1.add(txt_dir_cli, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 180, 190, 30));

        txt_ced_cli.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_ced_cliActionPerformed(evt);
            }
        });
        txt_ced_cli.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_ced_cliKeyTyped(evt);
            }
        });
        jPanel1.add(txt_ced_cli, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 20, 190, 30));

        txt_nom_cli.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt_nom_cliKeyPressed(evt);
            }
        });
        jPanel1.add(txt_nom_cli, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 60, 190, 30));

        txt_ape_cli.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt_ape_cliKeyPressed(evt);
            }
        });
        jPanel1.add(txt_ape_cli, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 100, 190, 30));

        txt_tel_cli.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt_tel_cliKeyPressed(evt);
            }
        });
        jPanel1.add(txt_tel_cli, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 140, 190, 30));

        jButton8.setText("Limpiar");
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton8, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 230, 290, -1));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 50, 400, 270));

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/save.png"))); // NOI18N
        jButton2.setText("Ingresar nuevo cliente");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 50, 430, 80));
        getContentPane().add(jSeparator2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 610, 860, 10));

        txt_buscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_buscarActionPerformed(evt);
            }
        });
        txt_buscar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_buscarKeyReleased(evt);
            }
        });
        getContentPane().add(txt_buscar, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 370, 290, 30));

        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/search.png"))); // NOI18N
        jButton4.setText("Buscar");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton4, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 350, 150, 70));

        jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/refresh.png"))); // NOI18N
        jButton5.setText("Actualizar tabla");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton5, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 350, 230, 70));

        jButton6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/print.png"))); // NOI18N
        jButton6.setText("Reporte");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton6, new org.netbeans.lib.awtextra.AbsoluteConstraints(730, 350, 150, 70));

        jButton7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/write.png"))); // NOI18N
        jButton7.setText("Actualizar cliente ");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton7, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 240, 210, 80));

        jButton9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/back.png"))); // NOI18N
        jButton9.setText("Regresar");
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton9, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 640, 160, 50));
        getContentPane().add(jSeparator3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));
        getContentPane().add(jSeparator4, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));
        getContentPane().add(jSeparator5, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 340, 860, 10));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed

        /*
         * Validar campos con el metodo de la clase validacionCampos
         */
        

        try {

            campo_enviar = txt_ced_cli;
            String captura = campo_enviar.getText();
            valida.notEmpty(captura, campo_enviar, mensajeEnviado);
            
        } catch (Exception e) {
        }

        try {

            campo_enviar = txt_nom_cli;
            String captura = campo_enviar.getText();
            valida.notEmpty(captura, campo_enviar, mensajeEnviado);
           

        } catch (Exception e) {
        }

        try {

            campo_enviar = txt_ape_cli;
            String captura = campo_enviar.getText();
            valida.notEmpty(captura, campo_enviar, mensajeEnviado);
            
        } catch (Exception e) {
        }

        try {

            campo_enviar = txt_dir_cli;
            String captura = campo_enviar.getText();
            valida.notEmpty(captura, campo_enviar, mensajeEnviado);
           
        } catch (Exception e) {
        }

        try {

            campo_enviar = txt_tel_cli;
            String captura = campo_enviar.getText();
            valida.notEmpty(captura, campo_enviar, mensajeEnviado);
           
        } catch (Exception e) {
        }

        /*
         * Actualizar tabla
         */
        try {

            PreparedStatement pst = cn.prepareStatement("INSERT INTO tblclientes (cedula_cliente, nombre_cliente, apellido_cliente, telefono_cliente, dirreccion_cliente) VALUES (?,?,?,?,?)");
            int cedula;
            cedula = Integer.parseInt(txt_ced_cli.getText());

            pst.setInt(1, cedula);
            pst.setString(2, txt_nom_cli.getText());
            pst.setString(3, txt_ape_cli.getText());
            pst.setString(4, txt_tel_cli.getText());
            pst.setString(5, txt_dir_cli.getText());

            pst.executeUpdate();

            //
        } catch (Exception e) {

        }

    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed

        mensajeTabla = false;
        int fila = jTable1.getSelectedRow();
        Object cedula_cliente = "";

        try {

            if (fila >= 0) {
                cedula_cliente = jTable1.getValueAt(fila, 0).toString();
                PreparedStatement pst = cn.prepareStatement("delete from tblclientes where cedula_cliente=" + cedula_cliente);
                pst.executeUpdate();
            } else {
                valida.notSelectedRow(fila, mensajeTabla);
                mensajeTabla = true;
            }

        } catch (SQLException ex) {
            Logger.getLogger(PrincipalClientesEmp.class.getName()).log(Level.SEVERE, null, ex);
            System.out.print(ex.getMessage());
        }

    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        mostrardatos(txt_buscar.getText());
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        mostrardatos("");
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        /*
         * Este evento obtiene los valores de la tabla de clientes y los
         envía al formulario para que el usuario los pueda editar
         */
        mensajeTabla = false;

        try {
            int fila = jTable1.getSelectedRow();
            if (fila >= 0) {

                txt_ced_cli.setText(jTable1.getValueAt(fila, 0).toString());
                txt_nom_cli.setText(jTable1.getValueAt(fila, 1).toString());
                txt_ape_cli.setText(jTable1.getValueAt(fila, 2).toString());
                txt_tel_cli.setText(jTable1.getValueAt(fila, 3).toString());
                txt_dir_cli.setText(jTable1.getValueAt(fila, 4).toString());

            } else {
                valida.notSelectedRow(fila, mensajeTabla);
                mensajeTabla = true;
            }
        } catch (Exception e) {

        }

    }//GEN-LAST:event_jButton7ActionPerformed

    private void txt_ced_cliActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_ced_cliActionPerformed
        // TODO add your handling code here:


    }//GEN-LAST:event_txt_ced_cliActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed

        txt_ced_cli.setText(null);
        txt_nom_cli.setText(null);
        txt_ape_cli.setText(null);
        txt_tel_cli.setText(null);
        txt_dir_cli.setText(null);

    }//GEN-LAST:event_jButton8ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed

        /*
         * Validar campos con el metodo de la clase validacionCampos
         */
        

        try {

            campo_enviar = txt_ced_cli;
            String captura = campo_enviar.getText();
            valida.notEmpty(captura, campo_enviar, mensajeEnviado);
            
        } catch (Exception e) {
        }

        try {

            campo_enviar = txt_nom_cli;
            String captura = campo_enviar.getText();
            valida.notEmpty(captura, campo_enviar, mensajeEnviado);
            

        } catch (Exception e) {
        }

        try {

            campo_enviar = txt_ape_cli;
            String captura = campo_enviar.getText();
            valida.notEmpty(captura, campo_enviar, mensajeEnviado);
           
        } catch (Exception e) {
        }

        try {

            campo_enviar = txt_dir_cli;
            String captura = campo_enviar.getText();
            valida.notEmpty(captura, campo_enviar, mensajeEnviado);
           
        } catch (Exception e) {
        }

        try {

            campo_enviar = txt_tel_cli;
            String captura = campo_enviar.getText();
            valida.notEmpty(captura, campo_enviar, mensajeEnviado);
            
        } catch (Exception e) {
        }

        /*
         * Actualizar tabla
         */
        
//        if(valida.mensaje==false){
//        try {
//
//            PreparedStatement pst = cn.prepareStatement("update tblclientes set nombre_cliente='" + txt_nom_cli.getText() + "',apellido_cliente='" + txt_ape_cli.getText() + "',telefono_cliente=" + txt_tel_cli.getText() + ",dirreccion_cliente='" + txt_dir_cli.getText() + "'where cedula_cliente=" + txt_ced_cli.getText() + "");
//            if (mensajeEnviado == false) {
//                pst.executeUpdate();
//            } else {
//                /*
//                 * Do absolutely nothing
//                 */
//            }
//        } catch (SQLException ex) {
//            Logger.getLogger(PrincipalClientesEmp.class.getName()).log(Level.SEVERE, null, ex);
//            System.out.print(ex.getMessage());
//        }
//        }else{
//            /*
//            Nothing
//            */
//        }
        
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed

        /*
        
        */
        
            MenuEmpleado ventEmp = new MenuEmpleado();
            ventEmp.setVisible(true);
            dispose();
       
    }//GEN-LAST:event_jButton9ActionPerformed

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

                    JasperReport report = JasperCompileManager.compileReport("src/Reportes/reporte_clientes.jrxml");
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

    private void txt_buscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_buscarActionPerformed

    }//GEN-LAST:event_txt_buscarActionPerformed

    private void txt_buscarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_buscarKeyReleased
        campo_enviar = txt_buscar;
        char captura = evt.getKeyChar();
        evento_enviar = evt;
        valida.onlyNumb(captura, campo_enviar, evt);
    }//GEN-LAST:event_txt_buscarKeyReleased

    private void txt_ced_cliKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_ced_cliKeyTyped
        char captura = evt.getKeyChar();
        valida.onlyNumb(captura, txt_ced_cli, evt);
    }//GEN-LAST:event_txt_ced_cliKeyTyped

    private void txt_nom_cliKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_nom_cliKeyPressed
        char captura = evt.getKeyChar();
        valida.onlyAlpha(captura, txt_nom_cli, evt);
    }//GEN-LAST:event_txt_nom_cliKeyPressed

    private void txt_ape_cliKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_ape_cliKeyPressed
        char captura = evt.getKeyChar();
        valida.onlyAlpha(captura, txt_ape_cli, evt);
    }//GEN-LAST:event_txt_ape_cliKeyPressed

    private void txt_tel_cliKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_tel_cliKeyPressed
        char captura = evt.getKeyChar();
        valida.onlyNumb(captura, txt_tel_cli, evt);
    }//GEN-LAST:event_txt_tel_cliKeyPressed

    private void txt_dir_cliKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_dir_cliKeyPressed
        char captura = evt.getKeyChar();
        valida.onlyAlpha(captura, txt_dir_cli, evt);
    }//GEN-LAST:event_txt_dir_cliKeyPressed

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
            java.util.logging.Logger.getLogger(PrincipalClientesEmp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(PrincipalClientesEmp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(PrincipalClientesEmp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(PrincipalClientesEmp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new PrincipalClientesEmp().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField txt_ape_cli;
    private javax.swing.JTextField txt_buscar;
    private javax.swing.JTextField txt_ced_cli;
    private javax.swing.JTextField txt_dir_cli;
    private javax.swing.JTextField txt_nom_cli;
    private javax.swing.JTextField txt_tel_cli;
    // End of variables declaration//GEN-END:variables

    conexion cc = new conexion();
    Connection cn = cc.conexion();

    validacionCampos valida = new validacionCampos();
    
    Ingresar ingresar  = new Ingresar();

    private void JFileChooser() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
