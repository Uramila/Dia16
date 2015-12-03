package interfazbase.administrador;

import interfazbase.Ingresar;
import interfazbase.administrador.MenuAdmin;
import interfazbase.empleado.MenuEmpleado;
import interfazbase.clases.conexion;
import interfazbase.clases.numeracionFactura;
import java.awt.event.KeyEvent;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ButtonGroup;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import static javax.swing.JOptionPane.WARNING_MESSAGE;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author Alexander
 */
public class principalFacturacion extends javax.swing.JFrame {

    public int selectedRow;
    public boolean estaticaEnviado;
    public boolean mensajeEnviado;
    public JTextField campo_enviar;
    public KeyEvent evento_enviar;
    boolean mensajeTabla;
    double totalRegistros = 0;
    int totalPaginas = 0;
    double maxReg = 8;
    public int paginaActual = 1;
    public int paginaSiguienteCli;
    public int paginaSiguienteProd;
    public Object cantidadVendida;
    public Object codigoProducto;
    public Object stockActual;
    public Object stockNuevo;
    public Object ultima = "";
    int proxima;

    public void numFact() {

        /*
         Obtener la numeracion de la factura
         */
        String getUltimaFactura = ("select * from tblfactxdatos order by id_fact");
        try {
            Statement stNum = cn.createStatement();
            ResultSet reTotal = stNum.executeQuery(getUltimaFactura);

            while (reTotal.next()) {

                ultima = reTotal.getInt(1);

            }

            proxima = (Integer.parseInt(ultima.toString()) + 1);
            //System.out.println("Ultima= " + ultima + "Proxima = " + proxima);
            txtNumeroFactura.setText(String.valueOf(proxima));

        } catch (SQLException ex) {
            Logger.getLogger(PrincipalClientes.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void desactBotonesPaginas() {
        /*
         Desactivar el botón si no hay páginas anteriores
         */
        String actualCli = txtPagAct.getText();
        String totalCli = txtPagTotal.getText();
        String actualProd = txtPagAct1.getText();
        String totalProd = txtPagTotal1.getText();

        if ("1".equals(actualCli)) {

            btnAnterior.setEnabled(false);
        }
        if (!("1".equals(actualCli))) {

            btnAnterior.setEnabled(true);
        }

        if ("1".equals(actualProd)) {

            btnAnterior1.setEnabled(false);
        }
        if (!("1".equals(actualProd))) {

            btnAnterior1.setEnabled(true);
        }
        /*
         Desactivar el botón si no hay páginas siguientes
         */
        if (actualProd.equals(totalProd)) {
            btnSiguiente1.setEnabled(false);

        }
        if ((!actualProd.equals(totalProd))) {
            btnSiguiente1.setEnabled(true);

        }

        if (actualCli.equals(totalCli)) {
            btnSiguiente.setEnabled(false);

        }
        if ((!actualCli.equals(totalCli))) {
            btnSiguiente.setEnabled(true);

        }
    }

    public void paginaSiguienteCli() {
        /*
         Método para calcular qué pagina sigue en la tabla
         */
        try {

            int paginaActual = (Integer.parseInt(txtPagAct.getText()));

            paginaSiguienteCli = (paginaActual + 1);

            int offsetEnviar = (paginaSiguienteCli - 1) * 8;

            mostrardatoscli("", offsetEnviar);
        } catch (Exception ex) {
            Logger.getLogger(PrincipalClientes.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void paginaSiguienteProd() {
        /*
         Método para calcular qué pagina sigue en la tabla
         */
        try {

            int paginaActual = (Integer.parseInt(txtPagAct1.getText()));

            paginaSiguienteProd = (paginaActual + 1);

            int offsetEnviar = (paginaSiguienteProd - 1) * 8;

            mostrardatosprod("", offsetEnviar);
        } catch (Exception ex) {
            Logger.getLogger(PrincipalClientes.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void paginaAnteriorCli() {
        /*
         Método para calcular qué pagina sigue en la tabla
         */
        try {

            int paginaActual = (Integer.parseInt(txtPagAct.getText()));

            paginaSiguienteCli = (paginaActual - 1);

            int offsetEnviar = (paginaSiguienteCli - 1) * 8;

            mostrardatoscli("", offsetEnviar);
        } catch (Exception ex) {
            Logger.getLogger(PrincipalClientes.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void paginaAnteriorProd() {
        /*
         Método para calcular qué pagina sigue en la tabla
         */
        try {

            int paginaActual = (Integer.parseInt(txtPagAct1.getText()));

            paginaSiguienteProd = (paginaActual - 1);

            int offsetEnviar = (paginaSiguienteProd - 1) * 8;

            mostrardatosprod("", offsetEnviar);
        } catch (Exception ex) {
            Logger.getLogger(PrincipalClientes.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    void agregarATablaFactura() {

        int fila = jTable3.getSelectedRow();
        Object cantidad = "";
        cantidad = jTable3.getValueAt(fila, 4);
        int cantidadVenta = Integer.parseInt(txt_cantidad.getText());

        if (cantidadVenta > (int) cantidad) {
            JOptionPane.showMessageDialog(null, "La cantidad seleccionada supera el stock disponible", "Información", WARNING_MESSAGE);
        } else {
            try {
                String codigo, nombre, marca, descripcion, precio, cant, stock, iva, sub;
                int calcula = 0, subtotal = 0, ivas = 0;
                int canti = 0;

                if (fila < 0) {
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
                    Object rows[] = {codigo, nombre, cant, precio, stock, subtotal};
                    modelo_factura.addRow(rows);

                    calcula = (Integer.parseInt(precio));

                    total = total + calcula;

                }
            } catch (Exception e) {
            }

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

    void mostrardatosprod(String valor, int offset) {
        /*
         Obtener los registros de la tabla de productos en la BD para mostrarlos
         en en table de jdialog de los productos
         */
        DefaultTableModel modelo = new DefaultTableModel();
        modelo.addColumn("Codigo");
        modelo.addColumn("Nombre");
        modelo.addColumn("Marca");
        modelo.addColumn("Precio");
        modelo.addColumn("Cantidad");
        modelo.addColumn("Descripcion");
        jTable3.setModel(modelo);
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

                txtPagAct1.setText(paginaActual + "");
                txtPagTotal1.setText("" + totalPaginas);
                jTable3.setModel(modelo);

            } catch (SQLException ex) {
                Logger.getLogger(PrincipalClientes.class
                        .getName()).log(Level.SEVERE, null, ex);
            }

            sql = "select * from tblproductos order by id_producto  OFFSET " + offset + " ROWS FETCH NEXT 8 ROWS ONLY";
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
            Logger.getLogger(PrincipalProductos.class
                    .getName()).log(Level.SEVERE, null, ex);
        }

    }

    void mostrardatoscli(String valor, int offset) {
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

                txtPagAct.setText(paginaActual + "");
                txtPagTotal.setText("" + totalPaginas);
                jTable2.setModel(modelo);

            } catch (SQLException ex) {
                Logger.getLogger(PrincipalClientes.class
                        .getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(PrincipalClientes.class
                    .getName()).log(Level.SEVERE, null, ex);
        }

    }

    void actualizarStock() {

        /*
         Método que actualizará el inventario despues de realizada una venta
        
         */
        int totalFilas = table_factura.getRowCount();

        for (int i = 0; i < (totalFilas); i++) {

            try {

                codigoProducto = table_factura.getValueAt(i, 0);
                cantidadVendida = table_factura.getValueAt(i, 2);

                stockActual = table_factura.getValueAt(i, 4);

                int actual = (Integer.parseInt(stockActual.toString()));
                int codigoPST = (Integer.parseInt(codigoProducto.toString()));
                int vendido = (Integer.parseInt(cantidadVendida.toString()));

                int nuevo = actual - vendido;

                PreparedStatement statementStock = cn.prepareStatement("update tblproductos set cantidad_producto = " + nuevo + "where id_producto = " + codigoPST + "");

                statementStock.executeUpdate();

            } catch (SQLException ex) {
                Logger.getLogger(principalFacturacion.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

    }

    void guardarFactura() {

        /* 
         Metodo que guardará los datos de la factura en la base de datos
         */
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
                subtotal = Integer.parseInt(String.valueOf(table_factura.getValueAt(x, 5)));

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
            Logger.getLogger(PrincipalClientes.class
                    .getName()).log(Level.SEVERE, null, ex);

            System.out.print(ex.getMessage());

        }
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
        modelo.addColumn("Stock");
        modelo.addColumn("Subtotal");

        table_factura.setModel(modelo);
        table_factura.getColumnModel().getColumn(4).setMaxWidth(0);
        table_factura.getColumnModel().getColumn(4).setMinWidth(0);
        table_factura.getColumnModel().getColumn(4).setPreferredWidth(0);
    }

    DefaultTableModel modelo_factura;

    static int total;
    int sub_total;
    int iva;
    int sumatoria1;
    String numeroFactura;

    public principalFacturacion() {

        Locale locale = new Locale("es", "CO");
        total = 0;
        sub_total = 0;
        iva = 0;

        initComponents();
        mostrardatoscli("", 0);
        llamarCliente();
        mostrardatosprod("", 0);
        iniciarTablaFactura();
        desactBotonesPaginas();
        numFact();

        //Agrupar los RadioButtons.
        ButtonGroup group = new ButtonGroup();
        group.add(radioCedula);
        group.add(radioNombre);

        radioCedula.setSelected(true);

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
        btnAnterior = new javax.swing.JButton();
        jTextField2 = new javax.swing.JTextField();
        txtPagAct = new javax.swing.JTextField();
        jTextField1 = new javax.swing.JTextField();
        txtPagTotal = new javax.swing.JTextField();
        btnSiguiente = new javax.swing.JButton();
        radioNombre = new javax.swing.JRadioButton();
        radioCedula = new javax.swing.JRadioButton();
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
        btnAnterior1 = new javax.swing.JButton();
        jTextField3 = new javax.swing.JTextField();
        txtPagAct1 = new javax.swing.JTextField();
        jTextField4 = new javax.swing.JTextField();
        txtPagTotal1 = new javax.swing.JTextField();
        btnSiguiente1 = new javax.swing.JButton();
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
        jButton6 = new javax.swing.JButton();

        jDialog1.setTitle("Buscar cliente - PRECAR");
        jDialog1.setBounds(new java.awt.Rectangle(0, 0, 600, 400));
        jDialog1.setResizable(false);
        jDialog1.getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane3.setViewportView(jTable2);

        jDialog1.getContentPane().add(jScrollPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 80, -1, 183));

        txt_buscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_buscarActionPerformed(evt);
            }
        });
        jDialog1.getContentPane().add(txt_buscar, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 30, 205, -1));

        jButton8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/search.png"))); // NOI18N
        jButton8.setText("Buscar cliente.");
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });
        jDialog1.getContentPane().add(jButton8, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 20, 220, 40));

        btn_buscar.setText("Seleccionar cliente");
        btn_buscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_buscarActionPerformed(evt);
            }
        });
        jDialog1.getContentPane().add(btn_buscar, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 80, -1, 79));

        jButton9.setText("Aceptar");
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });
        jDialog1.getContentPane().add(jButton9, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 170, 120, 90));

        btnAnterior.setText("Anterior");
        btnAnterior.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAnteriorActionPerformed(evt);
            }
        });
        jDialog1.getContentPane().add(btnAnterior, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 280, 130, 30));

        jTextField2.setText(" Página");
        jTextField2.setBorder(null);
        jTextField2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField2ActionPerformed(evt);
            }
        });
        jDialog1.getContentPane().add(jTextField2, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 280, 40, 30));

        txtPagAct.setBorder(null);
        jDialog1.getContentPane().add(txtPagAct, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 280, 30, 30));

        jTextField1.setText("de");
        jTextField1.setBorder(null);
        jDialog1.getContentPane().add(jTextField1, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 280, 20, 30));

        txtPagTotal.setBorder(null);
        txtPagTotal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPagTotalActionPerformed(evt);
            }
        });
        jDialog1.getContentPane().add(txtPagTotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 280, 40, 30));

        btnSiguiente.setText("Siguiente");
        btnSiguiente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSiguienteActionPerformed(evt);
            }
        });
        jDialog1.getContentPane().add(btnSiguiente, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 280, 130, 30));

        radioNombre.setText("Nombre");
        radioNombre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                radioNombreActionPerformed(evt);
            }
        });
        jDialog1.getContentPane().add(radioNombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 40, -1, -1));

        radioCedula.setText("Cedula");
        jDialog1.getContentPane().add(radioCedula, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 20, -1, -1));

        jDialog2.setBounds(new java.awt.Rectangle(0, 0, 750, 350));
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
        jDialog2.getContentPane().add(jButton11, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 230, 120, 40));

        jTable3.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane4.setViewportView(jTable3);

        jDialog2.getContentPane().add(jScrollPane4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 100, 560, 170));
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

        btnAnterior1.setText("Anterior");
        btnAnterior1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAnterior1ActionPerformed(evt);
            }
        });
        jDialog2.getContentPane().add(btnAnterior1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 280, 130, 30));

        jTextField3.setText(" Página");
        jTextField3.setBorder(null);
        jTextField3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField3ActionPerformed(evt);
            }
        });
        jDialog2.getContentPane().add(jTextField3, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 280, 40, 30));

        txtPagAct1.setBorder(null);
        jDialog2.getContentPane().add(txtPagAct1, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 280, 30, 30));

        jTextField4.setText("de");
        jTextField4.setBorder(null);
        jDialog2.getContentPane().add(jTextField4, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 280, 20, 30));

        txtPagTotal1.setBorder(null);
        txtPagTotal1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPagTotal1ActionPerformed(evt);
            }
        });
        jDialog2.getContentPane().add(txtPagTotal1, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 280, 40, 30));

        btnSiguiente1.setText("Siguiente");
        btnSiguiente1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSiguiente1ActionPerformed(evt);
            }
        });
        jDialog2.getContentPane().add(btnSiguiente1, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 280, 130, 30));

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel1.setName("");
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel3.setText("Direccion: ");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 100, 50, 20));

        jLabel5.setText("Nombre: ");
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, 50, 20));

        jLabel7.setText("Telefono:");
        jPanel1.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 70, 50, 20));

        txt_dir_cli.setFocusable(false);
        txt_dir_cli.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_dir_cliActionPerformed(evt);
            }
        });
        jPanel1.add(txt_dir_cli, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 100, 160, -1));

        txt_nom_cli.setFocusable(false);
        jPanel1.add(txt_nom_cli, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 40, 160, -1));

        txt_ape_cli.setFocusable(false);
        jPanel1.add(txt_ape_cli, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 40, 160, 20));

        txt_tel_cli.setFocusable(false);
        jPanel1.add(txt_tel_cli, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 70, 160, 20));

        txt_ced_cli.setText("0");
        txt_ced_cli.setFocusable(false);
        jPanel1.add(txt_ced_cli, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 10, 160, 20));

        jLabel9.setText("Cedula:");
        jPanel1.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 50, 20));

        jButton1.setText("Buscar cliente...");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 70, 230, 50));
        jButton1.getAccessibleContext().setAccessibleDescription("");

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 80, 480, 140));

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/search.png"))); // NOI18N
        jButton2.setText("Añadir");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(730, 240, 140, 100));
        getContentPane().add(jSeparator3, new org.netbeans.lib.awtextra.AbsoluteConstraints(930, 150, -1, -1));
        getContentPane().add(jSeparator4, new org.netbeans.lib.awtextra.AbsoluteConstraints(920, 140, -1, -1));
        getContentPane().add(jSeparator7, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 620, 850, 30));

        jButton5.setText("Cerrar factura");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton5, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 560, 130, 40));

        table_factura.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane2.setViewportView(table_factura);

        getContentPane().add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 240, 700, 300));

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

        getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 80, 360, 140));

        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/delete.png"))); // NOI18N
        jButton3.setText("Quitar");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(730, 350, 140, 100));
        getContentPane().add(jSeparator10, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 230, 850, 30));

        jLabel10.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel10.setText("Total: ");
        getContentPane().add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 560, 50, 40));

        txttotal.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        txttotal.setFocusable(false);
        getContentPane().add(txttotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 560, 170, 40));
        getContentPane().add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(750, 512, -1, 90));

        jSeparator2.setOrientation(javax.swing.SwingConstants.VERTICAL);
        getContentPane().add(jSeparator2, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 560, 10, 40));

        jButton4.setText("Calcular total");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton4, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 560, 210, 40));

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel1.setText("Numero de factura: ");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 20, 130, 40));

        txtNumeroFactura.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        txtNumeroFactura.setEnabled(false);
        txtNumeroFactura.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNumeroFacturaActionPerformed(evt);
            }
        });
        getContentPane().add(txtNumeroFactura, new org.netbeans.lib.awtextra.AbsoluteConstraints(750, 20, 110, 40));

        jSeparator5.setOrientation(javax.swing.SwingConstants.VERTICAL);
        getContentPane().add(jSeparator5, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 560, 10, 40));

        jButton7.setText("Imprimir factura");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton7, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 560, 240, 40));

        jButton13.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/back.png"))); // NOI18N
        jButton13.setText("Regresar");
        jButton13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton13ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton13, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 640, 170, 60));

        jButton6.setText("jButton6");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton6, new org.netbeans.lib.awtextra.AbsoluteConstraints(740, 480, -1, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        /*
         Abir el jdialog para buscar clientes
         */

        jDialog1.setVisible(true);

    }//GEN-LAST:event_jButton1ActionPerformed

    private void txt_dir_cliActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_dir_cliActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_dir_cliActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        /*
         Abrir el jdialog de los productos
         */

        jDialog2.setVisible(true);

        // TODO add your handling code here:
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        /*
         Boton generar factura que envía los datos a la base de datos
         */

        guardarFactura();

        actualizarStock();

    }//GEN-LAST:event_jButton5ActionPerformed

    private void txt_buscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_buscarActionPerformed

    }//GEN-LAST:event_txt_buscarActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        /*
         Buscar cliente por cedula
         */
        String ced_buscar = txt_buscar.getText();

        mostrardatoscli(ced_buscar, 0);

    }//GEN-LAST:event_jButton8ActionPerformed

    private void btn_buscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_buscarActionPerformed
        /*
         Enviar cliente a la pantalla de factura 
         */
        buscar_cliente();
        llamarCliente();
    }//GEN-LAST:event_btn_buscarActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        jDialog1.dispose();

    }//GEN-LAST:event_jButton9ActionPerformed

    private void txt_buscar1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_buscar1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_buscar1ActionPerformed

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
        /*
         Buscar producto por codigo
         */
        String cod_buscar = txt_buscar1.getText();

        mostrardatosprod(cod_buscar, 0);
    }//GEN-LAST:event_jButton10ActionPerformed

    private void btn_buscar1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_buscar1ActionPerformed
        /*
         ENviar al jtable de la pantalla de facturas
         */

        agregarATablaFactura();

    }//GEN-LAST:event_btn_buscar1ActionPerformed

    private void jButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton11ActionPerformed
        jDialog2.dispose();
    }//GEN-LAST:event_jButton11ActionPerformed

    private void jButton12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton12ActionPerformed
        /*
         Mostrar la tabla de productos completa
         */
        mostrardatosprod("", 0);
    }//GEN-LAST:event_jButton12ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        /*
         Eliminar del jTable de productos en la pantalla factura
         */
        DefaultTableModel modelo1 = (DefaultTableModel) table_factura.getModel();
        modelo1.removeRow(table_factura.getSelectedRow());
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed

        /*
         Calcular el total de la venta
         */
        int sumatoria1 = 0;
        int sumatoria = 0;
        int totalRows = table_factura.getRowCount();
        for (int i = 0; i < (totalRows); i++) {
            sumatoria = Integer.parseInt(String.valueOf(table_factura.getValueAt(i, 5)));
            sumatoria1 += sumatoria;
        }
        txttotal.setText(String.valueOf(sumatoria1));

    }//GEN-LAST:event_jButton4ActionPerformed

    private void txtNumeroFacturaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNumeroFacturaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNumeroFacturaActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
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


    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButton13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton13ActionPerformed
        /*
         Compara la variable que almacena el tipo de usuario que ingresó al 
         sistema
         */
        MenuAdmin ventAdmin = new MenuAdmin();
        ventAdmin.setVisible(true);
        dispose();
    }//GEN-LAST:event_jButton13ActionPerformed

    private void btnAnteriorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAnteriorActionPerformed
        /*
         Boton para ir a la anterior pagina de la tabla
         */
        paginaAnteriorCli();
        txtPagAct.setText(String.valueOf(paginaSiguienteCli));
        desactBotonesPaginas();
    }//GEN-LAST:event_btnAnteriorActionPerformed

    private void jTextField2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField2ActionPerformed

    private void txtPagTotalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPagTotalActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPagTotalActionPerformed

    private void btnSiguienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSiguienteActionPerformed
        /*
         Boton para ir a la siguiente pagina de la tabla
         */

        paginaSiguienteCli();
        txtPagAct.setText(String.valueOf(paginaSiguienteCli));
        desactBotonesPaginas();
    }//GEN-LAST:event_btnSiguienteActionPerformed

    private void btnAnterior1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAnterior1ActionPerformed
        /*
         Boton para ir a la anterior pagina de la tabla
         */
        paginaAnteriorProd();
        txtPagAct1.setText(String.valueOf(paginaSiguienteProd));
        desactBotonesPaginas();
    }//GEN-LAST:event_btnAnterior1ActionPerformed

    private void jTextField3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField3ActionPerformed

    private void txtPagTotal1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPagTotal1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPagTotal1ActionPerformed

    private void btnSiguiente1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSiguiente1ActionPerformed
        /*
         Boton para ir a la siguiente pagina de la tabla
         */

        paginaSiguienteProd();
        txtPagAct1.setText(String.valueOf(paginaSiguienteProd));

        desactBotonesPaginas();
    }//GEN-LAST:event_btnSiguiente1ActionPerformed

    private void radioNombreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radioNombreActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_radioNombreActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed

        try {
            Connection conectar = DriverManager.getConnection("jdbc:sqlserver://"
                    + "PrecarDB.mssql.somee.com;databasename=PrecarDB",
                    "PrecarDBServer", "precarSena1");

            Map parametro = new HashMap();
            parametro.put("numFact", (Integer.parseInt(txtNumeroFactura.getText())));

            JasperReport report = JasperCompileManager.compileReport("src/Reportes/factura.jrxml");
            JasperPrint print = JasperFillManager.fillReport(report, parametro, conectar);
            JasperViewer viewer = new JasperViewer(print, false);
            viewer.setVisible(true);

        } catch (SQLException ex) {
            Logger.getLogger(principalFacturacion.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JRException ex) {
            Logger.getLogger(principalFacturacion.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_jButton6ActionPerformed

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
            java.util.logging.Logger.getLogger(principalFacturacion.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(principalFacturacion.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(principalFacturacion.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(principalFacturacion.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new principalFacturacion().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAnterior;
    private javax.swing.JButton btnAnterior1;
    private javax.swing.JButton btnSiguiente;
    private javax.swing.JButton btnSiguiente1;
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
    private javax.swing.JButton jButton6;
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
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JRadioButton radioCedula;
    private javax.swing.JRadioButton radioNombre;
    private javax.swing.JTable table_factura;
    private javax.swing.JTextField txtNumeroFactura;
    private javax.swing.JTextField txtPagAct;
    private javax.swing.JTextField txtPagAct1;
    private javax.swing.JTextField txtPagTotal;
    private javax.swing.JTextField txtPagTotal1;
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
    Ingresar ingresar = new Ingresar();

}
