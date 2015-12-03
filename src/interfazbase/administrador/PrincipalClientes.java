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
import interfazbase.clases.validacionCampos;
import java.awt.Component;
import java.awt.Container;
import java.awt.TextComponent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.print.PrinterException;
import java.io.File;
import java.io.FileNotFoundException;
import static java.lang.Thread.sleep;
import java.sql.*;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ButtonGroup;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import static javax.swing.JOptionPane.INFORMATION_MESSAGE;
import static javax.swing.JOptionPane.WARNING_MESSAGE;
import javax.swing.JTable;
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
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.engine.export.JRPdfExporter;

/**
 *
 * @author SENA
 */
public class PrincipalClientes extends javax.swing.JFrame {

    public int selectedRow;
    public boolean estaticaEnviado;
    boolean mensajeEnviado;
    public KeyEvent evento_enviar;
    boolean mensajeTabla;
    double totalRegistros = 0;
    int totalPaginas = 0;
    double maxReg = 8;
    public int paginaActual = 1;
    public int paginaSiguiente;

    /**
     * Creates new form PrincipalClientes
     */
    public PrincipalClientes() throws InterruptedException {

        boolean mensajeTabla = false;
        estaticaEnviado = false;
        mensajeEnviado = false;
        initComponents();
        setLocationRelativeTo(null);
        setResizable(false);
        setTitle("PRECAR - Clientes");
        mostrardatos("", 0);
        desactBotonesPaginas();

        //Agrupar los RadioButtons.
        ButtonGroup group = new ButtonGroup();
        group.add(radioCedula);
        group.add(radioNombre);

        radioCedula.setSelected(true);

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

            int offsetEnviar = (paginaSiguiente - 1) * 8;

            mostrardatos("", offsetEnviar);
        } catch (InterruptedException ex) {
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

            int offsetEnviar = (paginaSiguiente - 1) * 8;

            mostrardatos("", offsetEnviar);
        } catch (InterruptedException ex) {
            Logger.getLogger(PrincipalClientes.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    void mostrardatos(String valor, int offset) throws InterruptedException {

        /*
         Método para cargar los datos de la tabla en el jtable
         */
        DefaultTableModel modelo = new DefaultTableModel();
        modelo.addColumn("Cedula");
        modelo.addColumn("Nombre");
        modelo.addColumn("Apellido");
        modelo.addColumn("Telefono");
        modelo.addColumn("Direccion");
        jTable1.setModel(modelo);
        String sql = "";

        totalRegistros = 0;
        if (valor.equals("")) {

            String getTotalRows = ("select * from tblclientes");
            try {
                Statement stTotal = cn.createStatement();
                ResultSet reTotal = stTotal.executeQuery(getTotalRows);

                while (reTotal.next()) {
                    totalRegistros = totalRegistros + 1;
                }
                totalPaginas = (int) Math.ceil(totalRegistros / maxReg);
                //System.out.println("TotalPaginas  = " + totalRegistros + "/ " + maxReg + " = " + totalRegistros/maxReg);

                txtPagAct.setText(paginaActual + "");
                txtPagTotal.setText("" + totalPaginas);
                jTable1.setModel(modelo);

            } catch (SQLException ex) {
                Logger.getLogger(PrincipalClientes.class.getName()).log(Level.SEVERE, null, ex);
            }

            sql = "select * from tblclientes order by cedula_cliente  OFFSET " + offset + " ROWS FETCH NEXT 8 ROWS ONLY";
        } else if (radioCedula.isSelected() == true) {
            sql = "select * from tblclientes where cedula_cliente = " + valor + "";
        } else if (radioNombre.isSelected() == true) {
            sql = "select * from tblclientes where nombre_cliente = '" + valor + "'";
        }

        Object[] datos = new Object[5];

        try {

            Statement st = cn.createStatement();
            ResultSet re = st.executeQuery(sql);

            while (re.next()) {

                datos[0] = re.getInt(1);
                datos[1] = re.getString(2);
                datos[2] = re.getString(3);
                datos[3] = re.getInt(4);
                datos[4] = re.getString(5);

                modelo.addRow(datos);
            }

        } catch (SQLException ex) {
            Logger.getLogger(PrincipalClientes.class.getName()).log(Level.SEVERE, null, ex);
        }

        desactBotonesPaginas();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

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
        jButton10 = new javax.swing.JButton();
        btnAnterior = new javax.swing.JButton();
        btnSiguiente = new javax.swing.JButton();
        txtPagAct = new javax.swing.JTextField();
        txtPagTotal = new javax.swing.JTextField();
        jTextField1 = new javax.swing.JTextField();
        jTextField2 = new javax.swing.JTextField();
        radioCedula = new javax.swing.JRadioButton();
        radioNombre = new javax.swing.JRadioButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(jTable1);

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 360, 860, 160));

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/eraser.png"))); // NOI18N
        jButton1.setText("Eliminar cliente");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 210, 210, 80));

        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/dbdown.png"))); // NOI18N
        jButton3.setText("Guardar infotmacion del cliente");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 110, 430, 80));
        getContentPane().add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 200, 430, 10));

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel1.setEnabled(false);
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel3.setText("Direccion cliente: ");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 180, 110, 30));
        jLabel3.getAccessibleContext().setAccessibleName("");

        jLabel4.setText("Cedula cliente: ");
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 20, 90, 30));
        jLabel4.getAccessibleContext().setAccessibleName("");

        jLabel5.setText("Nombre cliente: ");
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 60, 90, 30));
        jLabel5.getAccessibleContext().setAccessibleName("");

        jLabel6.setText("Apellido cliente:");
        jPanel1.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 100, 80, 30));
        jLabel6.getAccessibleContext().setAccessibleName("");

        jLabel7.setText("Telefono cliente:");
        jPanel1.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 140, 110, 30));
        jLabel7.getAccessibleContext().setAccessibleName("");

        txt_dir_cli.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt_dir_cliKeyPressed(evt);
            }
        });
        jPanel1.add(txt_dir_cli, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 180, 190, 30));
        txt_dir_cli.getAccessibleContext().setAccessibleName("");

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
        txt_ced_cli.getAccessibleContext().setAccessibleName("");

        txt_nom_cli.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt_nom_cliKeyPressed(evt);
            }
        });
        jPanel1.add(txt_nom_cli, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 60, 190, 30));
        txt_nom_cli.getAccessibleContext().setAccessibleName("");

        txt_ape_cli.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt_ape_cliKeyPressed(evt);
            }
        });
        jPanel1.add(txt_ape_cli, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 100, 190, 30));
        txt_ape_cli.getAccessibleContext().setAccessibleName("");

        txt_tel_cli.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt_tel_cliKeyPressed(evt);
            }
        });
        jPanel1.add(txt_tel_cli, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 140, 190, 30));
        txt_tel_cli.getAccessibleContext().setAccessibleName("");

        jButton8.setText("Limpiar");
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton8, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 230, 290, -1));
        jButton8.getAccessibleContext().setAccessibleName("");

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 20, 400, 270));

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/save.png"))); // NOI18N
        jButton2.setText("Ingresar nuevo cliente");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 20, 430, 80));
        getContentPane().add(jSeparator2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 580, 860, 10));

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
        getContentPane().add(txt_buscar, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 320, 190, 20));

        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/search.png"))); // NOI18N
        jButton4.setText("Buscar");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton4, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 310, 140, 40));

        jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/refresh.png"))); // NOI18N
        jButton5.setText("Actualizar");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton5, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 310, 130, 40));

        jButton6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/print.png"))); // NOI18N
        jButton6.setText("Reporte");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton6, new org.netbeans.lib.awtextra.AbsoluteConstraints(740, 310, 140, 40));

        jButton7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/write.png"))); // NOI18N
        jButton7.setText("Actualizar cliente ");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton7, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 210, 210, 80));

        jButton9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/back.png"))); // NOI18N
        jButton9.setText("Regresar");
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton9, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 590, 160, 40));
        getContentPane().add(jSeparator3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));
        getContentPane().add(jSeparator4, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));
        getContentPane().add(jSeparator5, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 300, 860, 10));

        jButton10.setText("Imprimir cliente");
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton10, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 310, 120, 40));

        btnAnterior.setText("Anterior");
        btnAnterior.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAnteriorActionPerformed(evt);
            }
        });
        getContentPane().add(btnAnterior, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 530, 130, 30));

        btnSiguiente.setText("Siguiente");
        btnSiguiente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSiguienteActionPerformed(evt);
            }
        });
        getContentPane().add(btnSiguiente, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 530, 130, 30));

        txtPagAct.setBorder(null);
        getContentPane().add(txtPagAct, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 530, 30, 30));

        txtPagTotal.setBorder(null);
        txtPagTotal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPagTotalActionPerformed(evt);
            }
        });
        getContentPane().add(txtPagTotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 530, 40, 30));

        jTextField1.setText("de");
        jTextField1.setBorder(null);
        getContentPane().add(jTextField1, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 530, 20, 30));

        jTextField2.setText(" Página");
        jTextField2.setBorder(null);
        jTextField2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField2ActionPerformed(evt);
            }
        });
        getContentPane().add(jTextField2, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 530, 40, 30));

        radioCedula.setText("Cedula");
        getContentPane().add(radioCedula, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 310, -1, -1));

        radioNombre.setText("Nombre");
        radioNombre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                radioNombreActionPerformed(evt);
            }
        });
        getContentPane().add(radioNombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 330, -1, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed

        /*
         * Validar campos con el metodo de la clase validacionCampos
         */
        mensajeEnviado = false;

        try {

            valida.notEmpty(txt_ced_cli.getText(), txt_ced_cli, mensajeEnviado);
            mensajeEnviado = true;
        } catch (Exception e) {
        }

        try {

            valida.notEmpty(txt_nom_cli.getText(), txt_nom_cli, mensajeEnviado);
            mensajeEnviado = true;

        } catch (Exception e) {
        }

        try {

            valida.notEmpty(txt_ape_cli.getText(), txt_ape_cli, mensajeEnviado);
            mensajeEnviado = true;
        } catch (Exception e) {
        }

        try {

            valida.notEmpty(txt_dir_cli.getText(), txt_dir_cli, mensajeEnviado);
            mensajeEnviado = true;
        } catch (Exception e) {
        }

        try {

            valida.notEmpty(txt_tel_cli.getText(), txt_tel_cli, mensajeEnviado);
            mensajeEnviado = true;
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

            JOptionPane.showMessageDialog(rootPane, "Registro exitoso!", "Información", INFORMATION_MESSAGE);

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
            Logger.getLogger(PrincipalClientes.class.getName()).log(Level.SEVERE, null, ex);
            System.out.print(ex.getMessage());
        }

    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed

        try {
            mostrardatos(txt_buscar.getText(), 1);
        } catch (InterruptedException ex) {
            Logger.getLogger(PrincipalClientes.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        try {
            mostrardatos("", 0);
        } catch (InterruptedException ex) {
            Logger.getLogger(PrincipalClientes.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        /*
         * Este evento obtiene los valores de la tabla de clientes y los
         envía al formulario para que el usuario los pueda editar
         */
        mensajeTabla = false;
        txt_ced_cli.setEnabled(false);

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
         *Validar campos con el metodo de la clase validacionCampos
         */
        
        
            /*
             Ejecutar la consulta
             */
            try {

                System.out.print("LLegó");
                PreparedStatement pst = cn.prepareStatement("update tblclientes set nombre_cliente='" + txt_nom_cli.getText() + "',apellido_cliente='" + txt_ape_cli.getText() + "',telefono_cliente=" + txt_tel_cli.getText() + ",dirreccion_cliente='" + txt_dir_cli.getText() + "'where cedula_cliente=" + txt_ced_cli.getText() + "");

                pst.executeUpdate();

                JOptionPane.showMessageDialog(rootPane, "Actualizacion exitosa!", "Información", INFORMATION_MESSAGE);

            } catch (SQLException ex) {
                Logger.getLogger(PrincipalClientes.class.getName()).log(Level.SEVERE, null, ex);
                System.out.print(ex.getMessage());
            }

            txt_ced_cli.setText(null);
            txt_nom_cli.setText(null);
            txt_ape_cli.setText(null);
            txt_tel_cli.setText(null);
            txt_dir_cli.setText(null);
        
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed

        /*
         Compara la variable que almacena el tipo de usuario que ingresó al 
         sistema
         */
        MenuAdmin ventAdmin = new MenuAdmin();
        ventAdmin.setVisible(true);
        dispose();
    }//GEN-LAST:event_jButton9ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed

        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            Connection conectar = DriverManager.getConnection("jdbc:sqlserver://"
                    + "PrecarDB.mssql.somee.com;databasename=PrecarDB",
                    "PrecarDBServer", "precarSena1");

            int imprimirReport = JOptionPane.showConfirmDialog(null, "¿Desea "
                    + "guardar el reporte antes de imprimirlo?", "Confirmación", JOptionPane.YES_NO_OPTION);

            if (imprimirReport == JOptionPane.YES_OPTION) {

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

                        JasperReport report = JasperCompileManager.compileReport("src/Reportes/reporteGeneralClientes.jrxml");
                        JasperPrint print = JasperFillManager.fillReport(report, new HashMap(), conectar);

                        JRExporter exporter = new JRPdfExporter();
                        exporter.setParameter(JRExporterParameter.JASPER_PRINT, print);
                        exporter.setParameter(JRExporterParameter.OUTPUT_FILE, new java.io.File(Path));
                        exporter.exportReport();

                        /*
                         Llenar reporte
                         */
                        JOptionPane.showMessageDialog(null, "Guardando reporte", "Información", JOptionPane.INFORMATION_MESSAGE);
                        JOptionPane.showMessageDialog(null, "Reporte guardado exitosamente.", "Información", JOptionPane.INFORMATION_MESSAGE);

                    }
                } catch (JRException ex) {
                    System.out.print(ex.getMessage());
                }
            }

            if (imprimirReport == JOptionPane.NO_OPTION) {
                /*
                 Mostrar dialogo de impresion de reporte SIN GUARDAR
                 */

                JasperReport report = JasperCompileManager.compileReport("src/Reportes/reporteGeneralClientes.jrxml");
                JasperPrint print = JasperFillManager.fillReport(report, new HashMap(), conectar);
                JasperPrintManager.printReport(print, true);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(PrincipalClientes.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JRException ex) {
            Logger.getLogger(PrincipalClientes.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(PrincipalClientes.class.getName()).log(Level.SEVERE, null, ex);
        }


    }//GEN-LAST:event_jButton6ActionPerformed

    private void txt_buscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_buscarActionPerformed

    }//GEN-LAST:event_txt_buscarActionPerformed

    private void txt_buscarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_buscarKeyReleased

        char captura = evt.getKeyChar();
        evento_enviar = evt;
        valida.onlyAlpha(captura, txt_buscar, evt);
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

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
        try {
            jTable1.print(JTable.PrintMode.FIT_WIDTH);
        } catch (PrinterException ex) {
            Logger.getLogger(PrincipalClientes.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton10ActionPerformed

    private void btnSiguienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSiguienteActionPerformed

        /*
         Boton para ir a la siguiente pagina de la tabla
         */
        paginaSiguiente();
        txtPagAct.setText(String.valueOf(paginaSiguiente));

        desactBotonesPaginas();

    }//GEN-LAST:event_btnSiguienteActionPerformed

    private void txtPagTotalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPagTotalActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPagTotalActionPerformed

    private void jTextField2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField2ActionPerformed

    private void btnAnteriorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAnteriorActionPerformed

        /*
         Boton para ir a la anterior pagina de la tabla
         */
        paginaAnterior();
        txtPagAct.setText(String.valueOf(paginaSiguiente));

        desactBotonesPaginas();

    }//GEN-LAST:event_btnAnteriorActionPerformed

    private void radioNombreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radioNombreActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_radioNombreActionPerformed

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
            java.util.logging.Logger.getLogger(PrincipalClientes.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(PrincipalClientes.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(PrincipalClientes.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(PrincipalClientes.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    new PrincipalClientes().setVisible(true);
                } catch (InterruptedException ex) {
                    Logger.getLogger(PrincipalClientes.class.getName()).log(Level.SEVERE, null, ex);
                }
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
    private javax.swing.JButton jButton9;
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
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JRadioButton radioCedula;
    private javax.swing.JRadioButton radioNombre;
    private javax.swing.JTextField txtPagAct;
    private javax.swing.JTextField txtPagTotal;
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

    Ingresar ingresar = new Ingresar();

    private void JFileChooser() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
