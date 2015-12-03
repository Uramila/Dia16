package interfazbase.empleado;

import interfazbase.*;
import interfazbase.administrador.MenuAdmin;
import interfazbase.empleado.MenuEmpleado;
import interfazbase.clases.conexion;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
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
 * @author Alexander
 */
public class PrincipalFacturacionEmp extends javax.swing.JFrame {
    
    
    void agregarATablaFactura() {
        int fila = jTable3.getSelectedRow();
        try {
            String codigo, nombre, marca, descripcion, precio, cant, stock, iva, sub;
            int calcula = 0, subtotal = 0, ivas = 0;
            int canti = 0;

            if (fila == -1) {
                JOptionPane.showMessageDialog(null, "Por favor seleccione un "
                        + "producto.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            } else {
                modelo_factura = (DefaultTableModel) jTable3.getModel();
                codigo = jTable3.getValueAt(fila, 0).toString();
                nombre = jTable3.getValueAt(fila, 1).toString();
                marca = jTable3.getValueAt(fila, 2).toString();
                precio = jTable3.getValueAt(fila, 3).toString();
                stock = jTable3.getValueAt(fila, 4).toString();
                descripcion = jTable3.getValueAt(fila, 5).toString();

                cant = txt_cantidad.getText();

                //Calcular total
                subtotal = (Integer.parseInt(precio) * Integer.parseInt(cant));

                sub = String.valueOf(subtotal);

                modelo_factura = (DefaultTableModel) table_factura.getModel();
                Object rows[] = {codigo, nombre, cant, precio, subtotal};
                modelo_factura.addRow(rows);

                calcula = (Integer.parseInt(precio));

                total = total + calcula;

            }
        } catch (Exception e) {

        }

    }

    void buscar_producto() {
        /*
         LLamar los datos de un producto a JTextfield
         */
        int fila = jTable3.getSelectedRow();
        if (fila >= 0) {

            String cliente_encontrado = jTable3.getValueAt(fila, 0).toString();

        } else {
            JOptionPane.showMessageDialog(null, "Seleccione una fila");
        }
    }

    void buscar_cliente() {
        /*
         LLamar la cedula de un cliente y pasarla a un jtextfield
         */
        int fila = jTable2.getSelectedRow();
        if (fila >= 0) {

            String cliente_encontrado = jTable2.getValueAt(fila, 0).toString();

            txt_ced_cli.setText(cliente_encontrado);
        } else {
            JOptionPane.showMessageDialog(null, "Seleccione una fila");
        }
    }

    void mostrardatosprod(String valor) {
        /*
         Obtener los registros de la tabla de productos en la BD para mostrarlos
         en en table de jdialog de los productos
         */
        DefaultTableModel modelo = new DefaultTableModel();
        modelo.addColumn("Codigo");
        modelo.addColumn("Nombre");
        modelo.addColumn("Marca");
        modelo.addColumn("Precio");
        modelo.addColumn("Stock");
        modelo.addColumn("Descripcion");

        String sql = "";

        if (valor.equals("")) {
            sql = "select * from tblproductos";
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
            jTable3.setModel(modelo);
        } catch (SQLException ex) {
            Logger.getLogger(PrincipalClientesEmp.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    void mostrardatoscli(String valor) {
        /*
         Consultar la tabla de clientes y pasarla al jtable del dialogo
         */
        DefaultTableModel modelo = new DefaultTableModel();
        modelo.addColumn("Cedula");
        modelo.addColumn("Nombre");
        modelo.addColumn("Apellido");
        modelo.addColumn("Telefono");
        modelo.addColumn("Direccion");
        jTable2.setModel(modelo);
        String sql = "";

        if (valor.equals("")) {
            sql = "select * from tblclientes";
        } else {
            sql = "select * from tblclientes where cedula_cliente=" + valor + "";
        }
        Object[] datos = new Object[5];
        //query
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
            jTable2.setModel(modelo);
        } catch (SQLException ex) {
            Logger.getLogger(PrincipalClientesEmp.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    void guardarFactura() {

        /* 
         Metodo que guardará los datos de la factura en la base de datos
         */
        Object valoresTabla[];
        DateFormat dateformat = DateFormat.getDateInstance(DateFormat.LONG, new Locale("es", "CO"));

        try {

            PreparedStatement statementDatos = cn.prepareStatement("INSERT INTO tblfactxdatos(id_fact, cedula_cliente, nombre_cliente, "
                    + "apellido_cliente, telefono_cliente, direccion_cliente,"
                    + " fecha_exp, fecha_venc, total)"
                    + " VALUES (?,?,?,?,?,?,?,?,?)");

            int num_fact = Integer.parseInt(txtNumeroFactura.getText());
            int ced_cli = Integer.parseInt(txt_ced_cli.getText());
            String nom_cli = txt_nom_cli.getText();
            String ape_cli = txt_ape_cli.getText();
            int tel_cli = Integer.parseInt(txt_tel_cli.getText());
            String dir_cli = txt_dir_cli.getText();
            Date fechaExp = jDateChooser1.getDate();
            Date fechaVenc = jDateChooser2.getDate();
            int total = Integer.parseInt(txttotal.getText());

            String fechaE = dateformat.format(fechaExp);
            String fechaV = dateformat.format(fechaVenc);

            statementDatos.setInt(1, num_fact);
            statementDatos.setInt(2, ced_cli);
            statementDatos.setString(3, nom_cli);
            statementDatos.setString(4, ape_cli);
            statementDatos.setInt(5, tel_cli);
            statementDatos.setString(6, dir_cli);
            statementDatos.setString(7, fechaE);
            statementDatos.setString(8, fechaV);
            statementDatos.setInt(9, total);

            statementDatos.executeUpdate();

        } catch (SQLException | NumberFormatException e) {
            System.out.print(e.getMessage());
        }

        try {

            int filas = table_factura.getRowCount();
            for (int x = 0; x < filas; x++) {

                String nombre_producto;
                int id_fact, cod_producto, cantidad_prod, precio_unitario, subtotal;

                id_fact = Integer.parseInt(txtNumeroFactura.getText());

                cod_producto = Integer.parseInt(String.valueOf(table_factura.getValueAt(x, 0)));
                nombre_producto = String.valueOf(table_factura.getValueAt(x, 1));
                cantidad_prod = Integer.parseInt(String.valueOf(table_factura.getValueAt(x, 2)));
                precio_unitario = Integer.parseInt(String.valueOf(table_factura.getValueAt(x, 3)));
                subtotal = Integer.parseInt(String.valueOf(table_factura.getValueAt(x, 4)));

                PreparedStatement statementProductos = cn.prepareStatement("INSERT"
                        + " INTO tblfactxproductos  (id_fact, cod_producto, "
                        + "nombre_producto, cantidad_prod, precio_unitario,"
                        + " subtotal) VALUES (?,?,?,?,?,?)");

                statementProductos.setInt(1, id_fact);
                statementProductos.setInt(2, cod_producto);
                statementProductos.setString(3, nombre_producto);
                statementProductos.setInt(4, cantidad_prod);
                statementProductos.setInt(5, precio_unitario);
                statementProductos.setInt(6, subtotal);

                statementProductos.executeUpdate();

            }

        } catch (SQLException | NumberFormatException e) {
            System.out.print(e.getMessage());

        }

    }

    void llamarCliente() {

        /*
         Llenar todos los txt de cliente a partir de la cedula
         */
        int valor = Integer.parseInt(txt_ced_cli.getText());

        String sql = "";
        sql = "select * from tblclientes where cedula_cliente=" + valor + "";
        //vector tipo object to handle int also 
        Object[] datos = new Object[5];
        //query
        try {
            Statement st = cn.createStatement();
            ResultSet re = st.executeQuery(sql);

            while (re.next()) {
                datos[0] = re.getInt(1);
                datos[1] = re.getString(2);
                datos[2] = re.getString(3);
                datos[3] = re.getInt(4);
                datos[4] = re.getString(5);

                txt_ced_cli.setText(String.valueOf(datos[0]));
                txt_nom_cli.setText(String.valueOf(datos[1]));
                txt_ape_cli.setText(String.valueOf(datos[2]));
                txt_tel_cli.setText(String.valueOf(datos[3]));
                txt_dir_cli.setText(String.valueOf(datos[4]));
            }

        } catch (SQLException ex) {
            Logger.getLogger(PrincipalClientesEmp.class.getName()).log(Level.SEVERE, null, ex);

            System.out.print(ex.getMessage());

        }
    }
    /*
     void llamarProducto() {

     int valor = Integer.parseInt(txt_cod_prod.getText());
     String sql = "";
     sql = "select * from tblproductos where id_producto=" + valor + "";
     //vector tipo object to handle int also 
     Object[] datos = new Object[5];
     //query
     try {
     Statement st = cn.createStatement();
     ResultSet re = st.executeQuery(sql);

     while (re.next()) {
     datos[0] = re.getInt(1);
     datos[1] = re.getString(2);
     datos[2] = re.getString(3);
     datos[3] = re.getInt(4);
     datos[4] = re.getString(6);

     txt_cod_prod.setText(String.valueOf(datos[0]));
     txt_nom_prod.setText(String.valueOf(datos[1]));
     txt_marca_prod.setText(String.valueOf(datos[2]));
     txt_prec_prod.setText(String.valueOf(datos[3]));
     txt_desc_prod.setText(String.valueOf(datos[4]));

               
     }

     } catch (SQLException ex) {
     Logger.getLogger(PrincipalClientesEmp.class.getName()).log(Level.SEVERE, null, ex);

     System.out.print(ex.getMessage());

     }
     } 
     */

    void iniciarTablaFactura() {
        /*
         Modelo de la tabla factura
         */
        DefaultTableModel modelo = new DefaultTableModel();
        modelo.addColumn("Codigo");
        modelo.addColumn("Nombre");
        modelo.addColumn("Cantidad");
        modelo.addColumn("Precio unitario");
        modelo.addColumn("Subtotal");

        table_factura.setModel(modelo);
    }

    DefaultTableModel modelo_factura;

    static int total;
    int sub_total;
    int iva;
    int sumatoria1;

    public PrincipalFacturacionEmp() {

        Locale locale = new Locale("es", "CO");
        total = 0;
        sub_total = 0;
        iva = 0;
        initComponents();
        mostrardatoscli("");
        llamarCliente();
        mostrardatosprod("");
        iniciarTablaFactura();

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jDialog1 = new javax.swing.JDialog();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        txt_buscar = new javax.swing.JTextField();
        jButton8 = new javax.swing.JButton();
        btn_buscar = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();
        jDialog2 = new javax.swing.JDialog();
        txt_buscar1 = new javax.swing.JTextField();
        jButton10 = new javax.swing.JButton();
        btn_buscar1 = new javax.swing.JButton();
        jButton11 = new javax.swing.JButton();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTable3 = new javax.swing.JTable();
        txt_cantidad = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jSeparator8 = new javax.swing.JSeparator();
        jLabel8 = new javax.swing.JLabel();
        jSeparator9 = new javax.swing.JSeparator();
        jButton12 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        txt_dir_cli = new javax.swing.JTextField();
        txt_nom_cli = new javax.swing.JTextField();
        txt_ape_cli = new javax.swing.JTextField();
        txt_tel_cli = new javax.swing.JTextField();
        txt_ced_cli = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jSeparator3 = new javax.swing.JSeparator();
        jSeparator4 = new javax.swing.JSeparator();
        jSeparator7 = new javax.swing.JSeparator();
        jButton5 = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        table_factura = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jDateChooser1 = new com.toedter.calendar.JDateChooser();
        jLabel6 = new javax.swing.JLabel();
        jDateChooser2 = new com.toedter.calendar.JDateChooser();
        jButton3 = new javax.swing.JButton();
        jSeparator10 = new javax.swing.JSeparator();
        jLabel10 = new javax.swing.JLabel();
        txttotal = new javax.swing.JTextField();
        jSeparator1 = new javax.swing.JSeparator();
        jSeparator2 = new javax.swing.JSeparator();
        jButton4 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        txtNumeroFactura = new javax.swing.JTextField();
        jSeparator5 = new javax.swing.JSeparator();
        jButton7 = new javax.swing.JButton();
        jButton13 = new javax.swing.JButton();

        jDialog1.setTitle("Buscar cliente - PRECAR");
        jDialog1.setBounds(new java.awt.Rectangle(0, 0, 600, 300));
        jDialog1.setResizable(false);

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane3.setViewportView(jTable2);

        txt_buscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_buscarActionPerformed(evt);
            }
        });

        jButton8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/search.png"))); // NOI18N
        jButton8.setText("Buscar cliente por cedula.");
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        btn_buscar.setText("Seleccionar cliente");
        btn_buscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_buscarActionPerformed(evt);
            }
        });

        jButton9.setText("Aceptar");
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jDialog1Layout = new javax.swing.GroupLayout(jDialog1.getContentPane());
        jDialog1.getContentPane().setLayout(jDialog1Layout);
        jDialog1Layout.setHorizontalGroup(
            jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialog1Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jDialog1Layout.createSequentialGroup()
                        .addComponent(txt_buscar, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jDialog1Layout.createSequentialGroup()
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(8, 8, 8)
                        .addGroup(jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btn_buscar)
                            .addComponent(jButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jDialog1Layout.setVerticalGroup(
            jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialog1Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txt_buscar, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton8))
                .addGap(10, 10, 10)
                .addGroup(jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jDialog1Layout.createSequentialGroup()
                        .addComponent(btn_buscar, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(jButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(27, Short.MAX_VALUE))
        );

        jDialog2.setBounds(new java.awt.Rectangle(0, 0, 734, 311));
        jDialog2.getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txt_buscar1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_buscar1ActionPerformed(evt);
            }
        });
        jDialog2.getContentPane().add(txt_buscar1, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 20, 170, 60));

        jButton10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/search.png"))); // NOI18N
        jButton10.setText("Buscar producto...");
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });
        jDialog2.getContentPane().add(jButton10, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 20, 220, -1));

        btn_buscar1.setText("Agregar");
        btn_buscar1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_buscar1ActionPerformed(evt);
            }
        });
        jDialog2.getContentPane().add(btn_buscar1, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 180, 120, 40));

        jButton11.setText("Aceptar");
        jButton11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton11ActionPerformed(evt);
            }
        });
        jDialog2.getContentPane().add(jButton11, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 230, 120, 50));

        jTable3.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane4.setViewportView(jTable3);

        jDialog2.getContentPane().add(jScrollPane4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 100, 560, 183));
        jDialog2.getContentPane().add(txt_cantidad, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 130, 123, 31));

        jLabel4.setText("Cantidad");
        jDialog2.getContentPane().add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 110, -1, -1));
        jDialog2.getContentPane().add(jSeparator8, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 170, 120, -1));

        jLabel8.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel8.setText("Codigo del producto: ");
        jDialog2.getContentPane().add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 29, -1, 40));
        jDialog2.getContentPane().add(jSeparator9, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 100, 120, 30));

        jButton12.setText("Mostrar todos");
        jButton12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton12ActionPerformed(evt);
            }
        });
        jDialog2.getContentPane().add(jButton12, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 20, 120, 60));

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel1.setName("");
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel3.setText("Direccion: ");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 130, 50, 30));

        jLabel5.setText("Nombre: ");
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 50, 50, 30));

        jLabel7.setText("Telefono:");
        jPanel1.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 90, 50, 30));

        txt_dir_cli.setFocusable(false);
        txt_dir_cli.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_dir_cliActionPerformed(evt);
            }
        });
        jPanel1.add(txt_dir_cli, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 130, 160, 30));

        txt_nom_cli.setFocusable(false);
        jPanel1.add(txt_nom_cli, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 50, 160, 30));

        txt_ape_cli.setFocusable(false);
        jPanel1.add(txt_ape_cli, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 50, 160, 30));

        txt_tel_cli.setFocusable(false);
        jPanel1.add(txt_tel_cli, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 90, 160, 30));

        txt_ced_cli.setText("0");
        txt_ced_cli.setFocusable(false);
        jPanel1.add(txt_ced_cli, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 10, 160, 30));

        jLabel9.setText("Cedula:");
        jPanel1.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 50, 30));

        jButton1.setText("Buscar cliente...");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 90, 230, 70));
        jButton1.getAccessibleContext().setAccessibleDescription("");

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 80, 480, 180));

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/search.png"))); // NOI18N
        jButton2.setText("Añadir");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(730, 280, 140, 100));
        getContentPane().add(jSeparator3, new org.netbeans.lib.awtextra.AbsoluteConstraints(930, 150, -1, -1));
        getContentPane().add(jSeparator4, new org.netbeans.lib.awtextra.AbsoluteConstraints(920, 140, -1, -1));
        getContentPane().add(jSeparator7, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 660, 850, 30));

        jButton5.setText("Cerrar factura");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton5, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 600, 130, 40));

        table_factura.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane2.setViewportView(table_factura);

        getContentPane().add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 280, 700, 300));

        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel15.setText("Fecha: ");
        jPanel2.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 90, 50, 30));

        jLabel2.setText("Fecha de vencimiento");
        jPanel2.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 60, -1, 30));

        jDateChooser1.setDateFormatString("dd/MM/yyyy");
        jPanel2.add(jDateChooser1, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 20, 190, 30));

        jLabel6.setText("Fecha de expedicion");
        jPanel2.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, -1, 30));

        jDateChooser2.setDateFormatString("dd/MM/yyyy");
        jPanel2.add(jDateChooser2, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 60, 190, 30));

        getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 80, 360, 180));

        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/delete.png"))); // NOI18N
        jButton3.setText("Quitar");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(730, 390, 140, 100));
        getContentPane().add(jSeparator10, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 270, 850, 30));

        jLabel10.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel10.setText("Total: ");
        getContentPane().add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 600, 50, 40));

        txttotal.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        txttotal.setFocusable(false);
        getContentPane().add(txttotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 600, 170, 40));
        getContentPane().add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(750, 512, -1, 90));

        jSeparator2.setOrientation(javax.swing.SwingConstants.VERTICAL);
        getContentPane().add(jSeparator2, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 600, 10, 40));

        jButton4.setText("Calcular total");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton4, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 600, 210, 40));

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel1.setText("Numero de factura: ");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 20, 130, 40));

        txtNumeroFactura.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNumeroFacturaActionPerformed(evt);
            }
        });
        getContentPane().add(txtNumeroFactura, new org.netbeans.lib.awtextra.AbsoluteConstraints(750, 20, 110, 40));

        jSeparator5.setOrientation(javax.swing.SwingConstants.VERTICAL);
        getContentPane().add(jSeparator5, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 600, 10, 40));

        jButton7.setText("Imprimir factura");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton7, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 600, 240, 40));

        jButton13.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/back.png"))); // NOI18N
        jButton13.setText("Regresar");
        jButton13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton13ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton13, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 680, 170, 60));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        /*
         Abiri el jdialog para buscar clientes
         */

        jDialog1.setVisible(true);
        jDialog1.setModal(true);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void txt_dir_cliActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_dir_cliActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_dir_cliActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        /*
         Abrir el jdialog de los productos
         */

        jDialog2.setVisible(true);
        jDialog2.setModal(true);

        // TODO add your handling code here:
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        /*
         Boton generar factura que envía los datos a la base de datos
         */

        guardarFactura();


    }//GEN-LAST:event_jButton5ActionPerformed

    private void txt_buscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_buscarActionPerformed

    }//GEN-LAST:event_txt_buscarActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        /*
         Buscar cliente por cedula
         */
        String ced_buscar = txt_buscar.getText();

        mostrardatoscli(ced_buscar);

    }//GEN-LAST:event_jButton8ActionPerformed

    private void btn_buscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_buscarActionPerformed
        /*
         Enviar cliente a la pantalla de factura 
         */
        buscar_cliente();
        llamarCliente();
    }//GEN-LAST:event_btn_buscarActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed

    }//GEN-LAST:event_jButton9ActionPerformed

    private void txt_buscar1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_buscar1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_buscar1ActionPerformed

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
        /*
         Buscar producto por codigo
         */
        String cod_buscar = txt_buscar1.getText();

        mostrardatosprod(cod_buscar);
    }//GEN-LAST:event_jButton10ActionPerformed

    private void btn_buscar1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_buscar1ActionPerformed
        /*
         ENviar al jtable de la pantalla de facturas
         */
        agregarATablaFactura();

    }//GEN-LAST:event_btn_buscar1ActionPerformed

    private void jButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton11ActionPerformed

    }//GEN-LAST:event_jButton11ActionPerformed

    private void jButton12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton12ActionPerformed
        /*
         Mostrar la tabla de productos completa
         */
        mostrardatosprod("");
    }//GEN-LAST:event_jButton12ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        /*
         Eliminar del jTable de productos en la pantalla factura
         */
        DefaultTableModel modelo1 = (DefaultTableModel) table_factura.getModel();
        modelo1.removeRow(table_factura.getSelectedRow());
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        
        int sumatoria1=0;
        int sumatoria = 0;
        int totalRows = table_factura.getRowCount();
        //totalRows =-1;
        for (int i = 0; i < (totalRows); i++) {
            sumatoria = Integer.parseInt(String.valueOf(table_factura.getValueAt(i, 4)));
        sumatoria1 += sumatoria;
        }
        txttotal.setText(String.valueOf(sumatoria1));

    }//GEN-LAST:event_jButton4ActionPerformed

    private void txtNumeroFacturaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNumeroFacturaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNumeroFacturaActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        /*
         Compilar el reporte y generar el cuadro de dialogo para guardarlo como
         PDF
         */
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection conectar = DriverManager.getConnection("jdbc:mysql:"
                    + "//localhost/baseinterfaz", "root", "");

            JFileChooser fileChooser = new JFileChooser();
            fileChooser.addChoosableFileFilter(new FileNameExtensionFilter(""
                    + "Todos los archivos *.PDF", "pdf", "PDF"));
            int seleccion = fileChooser.showSaveDialog(null);
            try {
                if (seleccion == JFileChooser.APPROVE_OPTION) {
                    File JFC = fileChooser.getSelectedFile();
                    String Path = JFC.getAbsolutePath();
                    if (!(Path.endsWith(".pdf"))) {
                        File temp = new File(Path + ".pdf");
                        JFC.renameTo(temp);
                    }

                    JOptionPane.showMessageDialog(null, "Generando reporte",
                            "", JOptionPane.WARNING_MESSAGE);
                    JOptionPane.showMessageDialog(null, "Reporte generado"
                            + " exitosamente.",
                            "", JOptionPane.INFORMATION_MESSAGE);

                    JasperReport report = JasperCompileManager.compileReport("src/Reportes/factura.jrxml");
                    JasperPrint print = JasperFillManager.fillReport(report, null, conectar);
                    JRExporter exporter = new JRPdfExporter();
                    exporter.setParameter(JRExporterParameter.JASPER_PRINT, print);
                    exporter.setParameter(JRExporterParameter.OUTPUT_FILE,
                            new java.io.File(Path));
                    exporter.exportReport();
                }

            } catch (JRException ex  ) {
                System.out.print(ex.getMessage());
            }
        } catch (Exception e) {
        }
    
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButton13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton13ActionPerformed
   /*
        Compara la variable que almacena el tipo de usuario que ingresó al 
        sistema
        */
    MenuEmpleado ventEmp = new MenuEmpleado();
            ventEmp.setVisible(true);
            dispose();
           
    }//GEN-LAST:event_jButton13ActionPerformed

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
            java.util.logging.Logger.getLogger(PrincipalFacturacionEmp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(PrincipalFacturacionEmp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(PrincipalFacturacionEmp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(PrincipalFacturacionEmp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new PrincipalFacturacionEmp().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_buscar;
    private javax.swing.JButton btn_buscar1;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton13;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private com.toedter.calendar.JDateChooser jDateChooser1;
    private com.toedter.calendar.JDateChooser jDateChooser2;
    public javax.swing.JDialog jDialog1;
    private javax.swing.JDialog jDialog2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator10;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JSeparator jSeparator7;
    private javax.swing.JSeparator jSeparator8;
    private javax.swing.JSeparator jSeparator9;
    private javax.swing.JTable jTable2;
    private javax.swing.JTable jTable3;
    private javax.swing.JTable table_factura;
    private javax.swing.JTextField txtNumeroFactura;
    private javax.swing.JTextField txt_ape_cli;
    private javax.swing.JTextField txt_buscar;
    private javax.swing.JTextField txt_buscar1;
    private javax.swing.JTextField txt_cantidad;
    public static javax.swing.JTextField txt_ced_cli;
    private javax.swing.JTextField txt_dir_cli;
    private javax.swing.JTextField txt_nom_cli;
    private javax.swing.JTextField txt_tel_cli;
    private javax.swing.JTextField txttotal;
    // End of variables declaration//GEN-END:variables

    /*
     Instanciar la clase conexion y crear una nueva conexion
     */
    conexion cc = new conexion();
    Connection cn = cc.conexion();
    Ingresar ingresar  = new Ingresar();

}
