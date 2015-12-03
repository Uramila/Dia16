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
import java.text.DateFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
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
public class buscarFacturas extends javax.swing.JFrame {

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
    public Object numFact;
 
   

    /**
     * Creates new form PrincipalClientes
     */
    public buscarFacturas() throws InterruptedException {

        
        boolean mensajeTabla = false;
        estaticaEnviado = false;
        mensajeEnviado = false;
        initComponents();
        setLocationRelativeTo(null);
        setResizable(false);
        setTitle("PRECAR - Buscar facturas");
        mostrardatos("", 0);
        
        desactBotonesPaginas();
        iniciarTablaFactura();
        
      
        
        //Agrupar los RadioButtons.
        
        ButtonGroup group = new ButtonGroup();
        group.add(radioFact);
        group.add(radioCedula);
        group.add(radioNombre);
        group.add(radioApellido);
        group.add(radioFechaExp);
        group.add(radioFechaVenc);
        

        radioFact.setSelected(true);

    }

     void iniciarTablaFactura() {
        /*
         Modelo de la tabla factura
         */
        DefaultTableModel modelo = new DefaultTableModel();
        modelo.addColumn("Codigo");
        modelo.addColumn("Nombre");
        modelo.addColumn("Cantidad vendida");
        modelo.addColumn("Precio unitario");
        modelo.addColumn("Subtotal");

        table_factura.setModel(modelo);


    }

    public void desactBotonesPaginas() {
        /*
         Desactivar el botón si no hay páginas anteriores
         */
        String actual = txtPagAct.getText();
        String total = txtPagTotal.getText();
        
       
        
        if ((radioFechaExp.isSelected() == true || radioFechaVenc.isSelected()== true)){
            jDateChooser1.setEnabled(true);
        }
        
        if (!((radioFechaExp.isSelected() == true || radioFechaVenc.isSelected()== true))){
            jDateChooser1.setEnabled(false);
        }
        
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
            Logger.getLogger(buscarFacturas.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(buscarFacturas.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
   

    void mostrardatos(String valor, int offset) throws InterruptedException {

        /*
         Método para cargar los datos de la tabla en el jtable
         */
        DefaultTableModel modelo = new DefaultTableModel();
        modelo.addColumn("N. Factura");
        modelo.addColumn("C.C cliente");
        modelo.addColumn("Nombre cliente");
        modelo.addColumn("Apellido cliente");
        modelo.addColumn("Telefono cliente");
        modelo.addColumn("Direccion cliente");
        modelo.addColumn("Fecha expedición");
        modelo.addColumn("Fecha vencimiento");
        modelo.addColumn("Total");
        jTable1.setModel(modelo);
        String sql = "";

        totalRegistros = 0;
        if (valor.equals("")) {

            String getTotalRows = ("select * from tblfactxdatos");
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
                Logger.getLogger(buscarFacturas.class.getName()).log(Level.SEVERE, null, ex);
            }

            sql = "select * from tblfactxdatos order by id_fact  OFFSET " + offset + " ROWS FETCH NEXT 8 ROWS ONLY";
        
        
        } else if (radioFact.isSelected() == true) {
            sql = "select * from tblfactxdatos where id_fact = " + valor + "";
        } else if (radioCedula.isSelected() == true) {
            sql = "select * from tblfactxdatos where cedula_cliente = " + valor + "";
        } else if (radioNombre.isSelected() == true) {
            sql = "select * from tblfactxdatos where nombre_cliente = '" + valor + "'";
        } else if (radioApellido.isSelected() == true) {
            sql = "select * from tblfactxdatos where apellido_cliente = '" + valor + "'";
        } else if (radioFechaExp.isSelected() == true) {
            sql = "select * from tblfactxdatos where fecha_exp = '" + valor + "'";
        }else if (radioFechaVenc.isSelected() == true) {
            sql = "select * from tblfactxdatos where fecha_venc = '" + valor + "'";
        }
        Object[] datos = new Object[9];

        try {

            Statement st = cn.createStatement();
            ResultSet re = st.executeQuery(sql);

            while (re.next()) {

                datos[0] = re.getInt(1);
                datos[1] = re.getInt(2);
                datos[2] = re.getString(3);
                datos[3] = re.getString(4);
                datos[4] = re.getInt(5);
                datos[5] = re.getString(6);
                datos[6] = re.getString(7);
                datos[7] = re.getString(8);
                datos[8] = re.getInt(9);

                modelo.addRow(datos);
            }

        } catch (SQLException ex) {
            Logger.getLogger(buscarFacturas.class.getName()).log(Level.SEVERE, null, ex);
        }

        desactBotonesPaginas();
    }
    
     void mostrardatosProd(Object valor) throws InterruptedException {

        /*
         Método para cargar los datos de la tabla en el jtable
         */
         
        
         
        DefaultTableModel modelo = new DefaultTableModel();
        modelo.addColumn("Codigo");
        modelo.addColumn("Nombre");
        modelo.addColumn("Cantidad vendida");
        modelo.addColumn("Precio unitario");
        modelo.addColumn("Subtotal");
        table_factura.setModel(modelo);
        
        
        String sql = "";

        int factNumb = Integer.parseInt(numFact.toString());
        sql = "select * from tblfactxproductos  WHERE id_fact = " + factNumb + "";
        
        Object[] datos = new Object[5];

        try {

            Statement st = cn.createStatement();
            ResultSet re = st.executeQuery(sql);

            while (re.next()) {

                
                datos[0] = re.getInt(2);
                datos[1] = re.getString(3);
                datos[2] = re.getInt(4);
                datos[3] = re.getInt(5);
                datos[4] = re.getInt(6);
               
                modelo.addRow(datos);
            }

        } catch (SQLException ex) {
            Logger.getLogger(buscarFacturas.class.getName()).log(Level.SEVERE, null, ex);
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
        jSeparator2 = new javax.swing.JSeparator();
        txt_buscar = new javax.swing.JTextField();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();
        jSeparator3 = new javax.swing.JSeparator();
        jSeparator4 = new javax.swing.JSeparator();
        btnAnterior = new javax.swing.JButton();
        btnSiguiente = new javax.swing.JButton();
        txtPagAct = new javax.swing.JTextField();
        txtPagTotal = new javax.swing.JTextField();
        jTextField1 = new javax.swing.JTextField();
        jTextField2 = new javax.swing.JTextField();
        radioCedula = new javax.swing.JRadioButton();
        radioNombre = new javax.swing.JRadioButton();
        txtNumeroFactura = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        radioFact = new javax.swing.JRadioButton();
        radioApellido = new javax.swing.JRadioButton();
        radioFechaExp = new javax.swing.JRadioButton();
        radioFechaVenc = new javax.swing.JRadioButton();
        jDateChooser1 = new com.toedter.calendar.JDateChooser();
        jScrollPane2 = new javax.swing.JScrollPane();
        table_factura = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        jSeparator5 = new javax.swing.JSeparator();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jTable1MousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jTable1MouseReleased(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 80, 860, 160));
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
        getContentPane().add(txt_buscar, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 40, 190, 20));

        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/search.png"))); // NOI18N
        jButton4.setText("Buscar");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton4, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 30, 210, 40));

        jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/refresh.png"))); // NOI18N
        jButton5.setText("Actualizar");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton5, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 250, 130, 30));

        jButton6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/print.png"))); // NOI18N
        jButton6.setText("Imprimir factura");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton6, new org.netbeans.lib.awtextra.AbsoluteConstraints(660, 550, 220, 40));

        jButton9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/back.png"))); // NOI18N
        jButton9.setText("Regresar");
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton9, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 630, 160, 40));
        getContentPane().add(jSeparator3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));
        getContentPane().add(jSeparator4, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        btnAnterior.setText("Anterior");
        btnAnterior.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAnteriorActionPerformed(evt);
            }
        });
        getContentPane().add(btnAnterior, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 250, 90, 30));

        btnSiguiente.setText("Siguiente");
        btnSiguiente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSiguienteActionPerformed(evt);
            }
        });
        getContentPane().add(btnSiguiente, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 250, 100, 30));

        txtPagAct.setBorder(null);
        getContentPane().add(txtPagAct, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 250, 30, 30));

        txtPagTotal.setBorder(null);
        txtPagTotal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPagTotalActionPerformed(evt);
            }
        });
        getContentPane().add(txtPagTotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 250, 40, 30));

        jTextField1.setText("de");
        jTextField1.setBorder(null);
        getContentPane().add(jTextField1, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 250, 20, 30));

        jTextField2.setText(" Página");
        jTextField2.setBorder(null);
        jTextField2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField2ActionPerformed(evt);
            }
        });
        getContentPane().add(jTextField2, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 250, 40, 30));

        radioCedula.setText("Cedula");
        radioCedula.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                radioCedulaActionPerformed(evt);
            }
        });
        getContentPane().add(radioCedula, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 50, -1, -1));

        radioNombre.setText("Nombre");
        radioNombre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                radioNombreActionPerformed(evt);
            }
        });
        getContentPane().add(radioNombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 30, -1, -1));

        txtNumeroFactura.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        txtNumeroFactura.setEnabled(false);
        txtNumeroFactura.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNumeroFacturaActionPerformed(evt);
            }
        });
        getContentPane().add(txtNumeroFactura, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 250, 110, 30));

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel1.setText("N. de factura: ");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 250, -1, 30));

        radioFact.setText("N. factura");
        radioFact.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                radioFactActionPerformed(evt);
            }
        });
        getContentPane().add(radioFact, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 30, 80, 20));

        radioApellido.setText("Apellido");
        radioApellido.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                radioApellidoActionPerformed(evt);
            }
        });
        getContentPane().add(radioApellido, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 50, -1, -1));

        radioFechaExp.setText("Fecha exp.");
        radioFechaExp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                radioFechaExpActionPerformed(evt);
            }
        });
        getContentPane().add(radioFechaExp, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 30, -1, 20));

        radioFechaVenc.setText("Fecha venc.");
        radioFechaVenc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                radioFechaVencActionPerformed(evt);
            }
        });
        getContentPane().add(radioFechaVenc, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 50, -1, -1));
        getContentPane().add(jDateChooser1, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 40, 150, -1));

        table_factura.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane2.setViewportView(table_factura);

        getContentPane().add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 290, 860, 250));

        jButton1.setText("Ver detalle");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(740, 250, 130, 30));

        jSeparator1.setOrientation(javax.swing.SwingConstants.VERTICAL);
        getContentPane().add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(880, 250, 10, 30));

        jSeparator5.setOrientation(javax.swing.SwingConstants.VERTICAL);
        getContentPane().add(jSeparator5, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 250, 10, 30));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed

    DateFormat dateformat = DateFormat.getDateInstance(DateFormat.LONG, new Locale("es", "CO"));
     
    
    if ((radioFechaExp.isSelected() == true || radioFechaVenc.isSelected()== true)){
        try {
            java.util.Date Fecha = jDateChooser1.getDate();
            String fecha = dateformat.format(Fecha);
            mostrardatos(fecha, 1);
          
        } catch (InterruptedException ex) {
            Logger.getLogger(buscarFacturas.class.getName()).log(Level.SEVERE, null, ex);
        }
        }    
        
    if ((radioFechaExp.isSelected() == false && radioFechaVenc.isSelected()== false)){    
        try {
            mostrardatos(txt_buscar.getText(), 1);
        } catch (InterruptedException ex) {
            Logger.getLogger(buscarFacturas.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        try {
            mostrardatos("", 0);
        } catch (InterruptedException ex) {
            Logger.getLogger(buscarFacturas.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton5ActionPerformed

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

         /*
         Compilar el reporte y generar el cuadro de dialogo para, guardarlo como
         PDF, o sólo imprimirlo
         */

        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            Connection conectar = DriverManager.getConnection("jdbc:sqlserver://"
                    + "PrecarDB.mssql.somee.com;databasename=PrecarDB",
                    "PrecarDBServer", "precarSena1");

            int imprimirReport = JOptionPane.showConfirmDialog(null, "¿Desea "
                    + "guardar la factura antes de imprimirla?", "Confirmación", JOptionPane.YES_NO_OPTION);

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
 
                        Map parametro = new HashMap();
                        parametro.put("numFact", (Integer.parseInt(txtNumeroFactura.getText())));

                        JasperReport report = JasperCompileManager.compileReport("src/Reportes/factura.jrxml");
                        JasperPrint print = JasperFillManager.fillReport(report, parametro, conectar);

                        JRExporter exporter = new JRPdfExporter();
                        exporter.setParameter(JRExporterParameter.JASPER_PRINT, print);
                        exporter.setParameter(JRExporterParameter.OUTPUT_FILE, new java.io.File(Path));
                        exporter.exportReport();

                        /*
                         Llenar reporte
                         */
                        JOptionPane.showMessageDialog(null, "Guardando factura", "Información", JOptionPane.INFORMATION_MESSAGE);
                        JOptionPane.showMessageDialog(null, "Factura guardada exitosamente.", "Información", JOptionPane.INFORMATION_MESSAGE);

                    }
                } catch (JRException ex) {
                    System.out.print(ex.getMessage());
                }

                try {

                    Map parametro = new HashMap();
                    parametro.put("numFact", (Integer.parseInt(txtNumeroFactura.getText())));
                    JasperReport report = JasperCompileManager.compileReport("src/Reportes/factura.jrxml");
                    JasperPrint print = JasperFillManager.fillReport(report, parametro, conectar);
                    JasperPrintManager.printReport(print, true);
                } catch (JRException ex) {
                    System.out.print(ex.getMessage());
                }
            }

            if (imprimirReport == JOptionPane.NO_OPTION) {
                /*
                 Mostrar dialogo de impresion de reporte SIN GUARDAR
                 */

                Map parametro = new HashMap();
                parametro.put("numFact", (Integer.parseInt(txtNumeroFactura.getText())));
                JasperReport report = JasperCompileManager.compileReport("src/Reportes/factura.jrxml");
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
        
         desactBotonesPaginas();
        
    }//GEN-LAST:event_radioNombreActionPerformed

    private void txtNumeroFacturaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNumeroFacturaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNumeroFacturaActionPerformed

    private void radioFactActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radioFactActionPerformed
       
        desactBotonesPaginas();
    }//GEN-LAST:event_radioFactActionPerformed

    private void radioCedulaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radioCedulaActionPerformed
       
         desactBotonesPaginas();
        
    }//GEN-LAST:event_radioCedulaActionPerformed

    private void radioApellidoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radioApellidoActionPerformed
       
         desactBotonesPaginas();
        
    }//GEN-LAST:event_radioApellidoActionPerformed

    private void radioFechaExpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radioFechaExpActionPerformed
       
        desactBotonesPaginas();
        
    }//GEN-LAST:event_radioFechaExpActionPerformed

    private void radioFechaVencActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radioFechaVencActionPerformed
       
         desactBotonesPaginas();
        
    }//GEN-LAST:event_radioFechaVencActionPerformed

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        
      
    }//GEN-LAST:event_jTable1MouseClicked

    private void jTable1MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseReleased
      
     
    }//GEN-LAST:event_jTable1MouseReleased

    private void jTable1MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MousePressed
          try {
            int fila = jTable1.getSelectedRow();
            if (fila >= 0) {

                numFact = Integer.parseInt(jTable1.getValueAt(fila, 0).toString());
                txtNumeroFactura.setText(numFact.toString());
                
            } else {
               
            }
      
         
        } catch (NumberFormatException e) {

        }
    }//GEN-LAST:event_jTable1MousePressed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
      
        numFact = txtNumeroFactura.getText();
        
        
        try {
            mostrardatosProd(numFact) ;
        } catch (InterruptedException ex) {
            Logger.getLogger(buscarFacturas.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }//GEN-LAST:event_jButton1ActionPerformed

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
            java.util.logging.Logger.getLogger(buscarFacturas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(buscarFacturas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(buscarFacturas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(buscarFacturas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    new buscarFacturas().setVisible(true);
                } catch (InterruptedException ex) {
                    Logger.getLogger(buscarFacturas.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAnterior;
    private javax.swing.JButton btnSiguiente;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton9;
    private com.toedter.calendar.JDateChooser jDateChooser1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JRadioButton radioApellido;
    private javax.swing.JRadioButton radioCedula;
    private javax.swing.JRadioButton radioFact;
    private javax.swing.JRadioButton radioFechaExp;
    private javax.swing.JRadioButton radioFechaVenc;
    private javax.swing.JRadioButton radioNombre;
    private javax.swing.JTable table_factura;
    private javax.swing.JTextField txtNumeroFactura;
    private javax.swing.JTextField txtPagAct;
    private javax.swing.JTextField txtPagTotal;
    private javax.swing.JTextField txt_buscar;
    // End of variables declaration//GEN-END:variables

    conexion cc = new conexion();
    Connection cn = cc.conexion();

    validacionCampos valida = new validacionCampos();

    Ingresar ingresar = new Ingresar();

    private void JFileChooser() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
