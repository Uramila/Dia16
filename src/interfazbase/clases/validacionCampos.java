/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfazbase.clases;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

/**
 *
 * @author Alexander
 */
public class validacionCampos {

    private JTextField campo;

    public void notNumb(char caracter, JTextField campo, KeyEvent evt) {
        /*
         *Si no esta entre cero y nueve
         */

        if (Character.isDigit(caracter)) {

            JOptionPane.showMessageDialog(null, "No son validos los caracteres numéricos.", "Validando Datos",
                    JOptionPane.WARNING_MESSAGE);
            campo.setText(null);
            campo.requestFocus();
        }
    }

    public void onlyNumb(char caracter, JTextField campo, KeyEvent evt) {

        if (!(Character.isDigit(evt.getKeyChar()))) {
            if (!((int) caracter == 8)) {
                JOptionPane.showMessageDialog(null, "Sólo son validos los caracteres numéricos.", "Validando Datos",
                        JOptionPane.WARNING_MESSAGE);

                campo.setText(null);
                campo.requestFocus();
                evt.consume();

            }
        }
    }

    public void onlyAlpha(char caracter, JTextField campo, KeyEvent evt) {
        /*
         *Si no es una LETRA
         */
        if (((int) caracter >= 32) && ((int) caracter <= 43) || (((int) caracter >= 58) && ((int) caracter <= 64)) || (((int) caracter >= 91) && ((int) caracter <= 94)) || (((int) caracter >= 123) && ((int) caracter <= 164))) {
            if ((!((int) caracter == 164) || (!((int) caracter == 165)))) {
                if (!((int) caracter == 32)) {

                    JOptionPane.showMessageDialog(null, "Sólo son validos los alfanuméricos.", "Validando Datos",
                            JOptionPane.WARNING_MESSAGE);
                    evt.consume();
                    campo.setText(null);
                    campo.requestFocus();
                }
            } else {

            }
        }

    }

    public void notEmpty(String caracter, JTextField campo, boolean enviado) {
        /*
         * Valida que el campo no se encuentre vacío al realizar una accion
         */
        if ((campo.getText().equals("")) || (campo.getText() == null) || (campo.getText().equals(" "))) {
            if (enviado == false) {

                JOptionPane.showMessageDialog(null, "Asegúrese de llenar todos los campos.", "Validando Datos",
                        JOptionPane.WARNING_MESSAGE);
                campo.setText(null);
                campo.requestFocus();

            } else {
                /*
                 * DOn't do anything
                 */
            }
        }
    }

    public void notSelectedRow(int selectedRow, boolean enviado) {

        if (enviado == false) {

            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(null, "Debe seleccionar una fila de la tabla.", "Validando Datos",
                        JOptionPane.WARNING_MESSAGE);

            } else {
            }
        }
    }

}
