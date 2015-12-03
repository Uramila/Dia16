/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfazbase.clases;

import java.sql.Connection;
import java.sql.DriverManager;

public class conexion {
   Connection conectar = null;
   public Connection conexion(){
        try{      
             //Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
                conectar = DriverManager.getConnection("jdbc:sqlserver://PrecarDB.mssql.somee.com;databasename=PrecarDB","PrecarDBServer","precarSena1");
      
            }
            catch (Exception e){
            System.out.print(e.getMessage());
}
                
      return conectar;           
   
   
      
    
    
}
}
