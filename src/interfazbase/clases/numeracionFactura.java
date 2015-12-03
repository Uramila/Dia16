/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfazbase.clases;

import interfazbase.administrador.PrincipalClientes;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Alexander
 */
public class numeracionFactura {
    
      
   
    public Object ultima = ""; 
  
    
    public Object  numFact(){
    
    String getUltimaFactura=("select * from tblfactxdatos order by id_fact");
            try {
                Statement stNum = cn.createStatement();
                ResultSet reTotal = stNum.executeQuery(getUltimaFactura);
                
                while (reTotal.last()){
                    
                    ultima = reTotal.getInt(0);
                    
                }
                
                
             

                }  catch (SQLException ex) {
                    Logger.getLogger(PrincipalClientes.class.getName()).log(Level.SEVERE, null, ex);
                }
   return ultima;
           
    
  }
    
  public static void main (String args[]){
      
  }  
    
conexion cc= new conexion();
Connection cn= cc.conexion();

   
}
