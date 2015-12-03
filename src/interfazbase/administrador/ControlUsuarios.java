/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfazbase.administrador;

import interfazbase.administrador.MenuAdmin;
import interfazbase.clases.validacionCampos;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Alexander
 */
public class ControlUsuarios extends javax.swing.JFrame {

    /**
     * Creates new form ControlUsuarios
     */
    public Object[] datos;
    public JTextField campo_enviar;
    public boolean mensajeEnviado;

    public ControlUsuarios() {
        initComponents();
        setResizable(false);
        setTitle("PRECAR - Control de usuarios");
        setLocationRelativeTo(null);
        setBounds(780, 370, 780, 370);
        mostrardatos("");

    }

    public void mostrardatos(String valor) {
        DefaultTableModel modelo = new DefaultTableModel();

        modelo.addColumn("Id");
        modelo.addColumn("Nombre de usuario");
        modelo.addColumn("Contraseña");
        modelo.addColumn("Tipo de usuario");

        jTable1.setModel(modelo);

      
        jTable1.getColumnModel().getColumn(2).setMaxWidth(0);
        jTable1.getColumnModel().getColumn(2).setMinWidth(0);
        jTable1.getColumnModel().getColumn(0).setPreferredWidth(6);
        jTable1.getColumnModel().getColumn(2).setPreferredWidth(0);

        String sql = "";

        if (valor.equals("")) {
            sql = "select * from usuarios";
        } else {
            sql = "select * from usuarios where usr_nick=" + valor + "";
        }
        datos = new Object[5];

        try {
            Statement st = cn.createStatement();
            ResultSet re = st.executeQuery(sql);

            while (re.next()) {
                datos[0] = re.getInt(1);
                datos[1] = re.getString(2);
                datos[2] = re.getString(3);
                datos[3] = re.getString(4);

                modelo.addRow(datos);
            }
            jTable1.setModel(modelo);
        } catch (SQLException ex) {
            Logger.getLogger(PrincipalClientes.class.getName()).log(Level.SEVERE, null, ex);
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

        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jSeparator3 = new javax.swing.JSeparator();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox();
        jLabel5 = new javax.swing.JLabel();
        txtNick = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        txtPass = new javax.swing.JPasswordField();
        txtConf = new javax.swing.JPasswordField();
        jButton8 = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        txt_id = new javax.swing.JTextField();
        jButton2 = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBounds(new java.awt.Rectangle(680, 280, 280, 280));
        getContentPane().setLayout(null);

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(jTable1);

        getContentPane().add(jScrollPane1);
        jScrollPane1.setBounds(20, 40, 280, 280);

        jSeparator3.setOrientation(javax.swing.SwingConstants.VERTICAL);
        getContentPane().add(jSeparator3);
        jSeparator3.setBounds(440, 30, 20, 300);

        jButton6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/delete.png"))); // NOI18N
        jButton6.setText("Eliminar");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton6);
        jButton6.setBounds(305, 200, 130, 60);

        jButton7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/write.png"))); // NOI18N
        jButton7.setText("Editar");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton7);
        jButton7.setBounds(305, 100, 130, 60);

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel3.setText("Codigo de usuario: ");
        getContentPane().add(jLabel3);
        jLabel3.setBounds(480, 50, 130, 28);

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel4.setText("Confirmar contraseña:");
        getContentPane().add(jLabel4);
        jLabel4.setBounds(460, 170, 150, 30);

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Administrador", "Empleado" }));
        getContentPane().add(jComboBox1);
        jComboBox1.setBounds(620, 210, 130, 30);

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel5.setText("Tipo de usuario: ");
        getContentPane().add(jLabel5);
        jLabel5.setBounds(500, 210, 110, 30);
        getContentPane().add(txtNick);
        txtNick.setBounds(620, 90, 130, 30);

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel6.setText("Contraseña:");
        getContentPane().add(jLabel6);
        jLabel6.setBounds(520, 130, 80, 30);
        getContentPane().add(txtPass);
        txtPass.setBounds(620, 130, 130, 30);
        getContentPane().add(txtConf);
        txtConf.setBounds(620, 170, 130, 30);

        jButton8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/save16.png"))); // NOI18N
        jButton8.setText("Registrar nuevo usuario");
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton8);
        jButton8.setBounds(460, 290, 170, 40);

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/save16.png"))); // NOI18N
        jButton1.setText("Guardar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton1);
        jButton1.setBounds(640, 290, 120, 40);

        jLabel7.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel7.setText("Nombre de usuario: ");
        getContentPane().add(jLabel7);
        jLabel7.setBounds(480, 90, 130, 28);

        txt_id.setEnabled(false);
        getContentPane().add(txt_id);
        txt_id.setBounds(620, 50, 130, 30);

        jButton2.setText("Limpiar");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton2);
        jButton2.setBounds(463, 250, 290, 23);

        jButton9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/back.png"))); // NOI18N
        jButton9.setText("Regresar");
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton9);
        jButton9.setBounds(20, 370, 129, 57);
        getContentPane().add(jSeparator1);
        jSeparator1.setBounds(20, 350, 740, 30);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        /*
         *Captura la informacion del usuario para ser editada
         */

        int fila = jTable1.getSelectedRow();
        if (fila >= 0) {

            String id   = jTable1.getValueAt(fila, 0).toString();
            String nick = jTable1.getValueAt(fila, 1).toString();
            String pass = jTable1.getValueAt(fila, 2).toString();
            String tipo = jTable1.getValueAt(fila, 3).toString();

            txt_id.setText(id);
            txtNick.setText(nick);
            txtPass.setText(pass);

            if ("Administrador".equals(tipo)) {
                jComboBox1.setSelectedIndex(0);
            } else if ("Empleado".equals(tipo)) {
                jComboBox1.setSelectedIndex(1);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Seleccione una fila");
        }
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        
       
        
        /*
         * Evento que envía el nuevo usuario a la base de datos
         */
        mensajeEnviado = false;

        if (txtPass.getText().equals(txtConf.getText())) {
            try {

                campo_enviar = txtNick;
                String captura = campo_enviar.getText();
                valida.notEmpty(captura, campo_enviar, mensajeEnviado);
                mensajeEnviado = true;
            } catch (Exception e) {
            }

            try {

                campo_enviar = txtPass;
                String captura = campo_enviar.getText();
                valida.notEmpty(captura, campo_enviar, mensajeEnviado);
                mensajeEnviado = true;
            } catch (Exception e) {
            }

            /*
             * Actualizar tabla
             */
            try {

                PreparedStatement pst = cn.prepareStatement("INSERT INTO usuarios"
                        + " (usr_nick, usr_pass, usr_tipo) VALUES (?,?,?)");

                pst.setString(1, txtNick.getText());
                pst.setString(2, txtPass.getText());
                pst.setString(3, jComboBox1.getSelectedItem().toString());
                pst.executeUpdate();

                //
            } catch (SQLException e) {
                System.out.print(e);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Las contraseñas no coinciden", "Validando datos", JOptionPane.WARNING_MESSAGE);
            txtPass.setText(null);
            txtConf.setText(null);
            txtPass.requestFocus();
        }

         mostrardatos("");

    }//GEN-LAST:event_jButton8ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed

       
        mensajeEnviado = false;

        if (txtPass.getText().equals(txtConf.getText())) {
            try {

                campo_enviar = txtNick;
                String captura = campo_enviar.getText();
                valida.notEmpty(captura, campo_enviar, mensajeEnviado);
                mensajeEnviado = true;
            } catch (Exception e) {
            }

            try {

                campo_enviar = txtPass;
                String captura = campo_enviar.getText();
                valida.notEmpty(captura, campo_enviar, mensajeEnviado);
                mensajeEnviado = true;
            } catch (Exception e) {
            }

            /*
             * Actualizar tabla
             */
            try {

                int fila = jTable1.getSelectedRow();
                if (fila >= 0) {
               
                PreparedStatement pst = cn.prepareStatement("update usuarios set"
                        + " usr_nick='" + txtNick.getText() + "',usr_pass='" + txtPass.getText() + "',usr_tipo='" + jComboBox1.getSelectedItem().toString() +  "'where usr_id=" + Integer.parseInt(txt_id.getText()) + "");
               
                pst.executeUpdate();

        }  //
            } catch (SQLException e) {
                System.out.print(e);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Las contraseñas no coinciden", "Validando datos", JOptionPane.WARNING_MESSAGE);
            txtPass.setText(null);
            txtConf.setText(null);
            txtPass.requestFocus();
        }
         mostrardatos("");
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        /*
        *Vacía los campos de texto del formulario
        */
        txt_id.setText(null);
        txtNick.setText(null);
        txtPass.setText(null);
        txtConf.setText(null);
        
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        /*
        *Eliminar usuario de la tabla y la BD
        */
        try {

            int fila = jTable1.getSelectedRow();
            if (fila >= 0) {

                PreparedStatement pst = cn.prepareStatement("delete from usuarios where usr_id=" + fila);

                pst.executeUpdate();

            } else {
                JOptionPane.showMessageDialog(null, "Seleccione una fila");
            }
        } catch (SQLException e) {
            System.out.print(e);
        }

       
        
         mostrardatos("");
        
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed

        MenuAdmin ventHome = new MenuAdmin();
        ventHome.setVisible(true);
        dispose();
    }//GEN-LAST:event_jButton9ActionPerformed

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
            java.util.logging.Logger.getLogger(ControlUsuarios.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ControlUsuarios.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ControlUsuarios.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ControlUsuarios.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ControlUsuarios().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JTable jTable1;
    private javax.swing.JPasswordField txtConf;
    private javax.swing.JTextField txtNick;
    private javax.swing.JPasswordField txtPass;
    private javax.swing.JTextField txt_id;
    // End of variables declaration//GEN-END:variables
 interfazbase.clases.conexion cc = new interfazbase.clases.conexion();
    Connection cn = cc.conexion();

    validacionCampos valida = new validacionCampos();

}
