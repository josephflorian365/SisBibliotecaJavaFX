/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelos;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 *
 * @author oXCToo
 */
public class Conexion {
    Connection conn = null;
    public static Connection conDB()
    {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost/04_QueryBD_JosephFlorian", "root", "");
            return con;
        } catch (Exception ex) {
           return null;
        }
    }
    
    //make sure you add the lib
}