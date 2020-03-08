/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelos;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;

/**
 *
 * @author Js
 */
public class devoluciones {
    private IntegerProperty IDDEV;
    private StringProperty fechapre;
    private StringProperty usuario;
    private StringProperty libro;
    private Date FECHDEV;
    
    public devoluciones(Integer IDDEV, String fechapre, String usuario, String libro, Date FECHDEV ){
        this.IDDEV = new SimpleIntegerProperty(IDDEV);
        this.fechapre = new SimpleStringProperty(fechapre);
        this.usuario = new SimpleStringProperty(usuario);
        this.libro = new SimpleStringProperty(libro);
        this.FECHDEV = FECHDEV;
    }
    
    public Integer getIDDEV(){
        return IDDEV.get();
    }
    
    public void setIDDEV(Integer IDDEV){
        this.IDDEV= new SimpleIntegerProperty(IDDEV);
    }
    
    public String getFechapre(){
        return fechapre.get();
    }
    
    public void setFechapre(String fechapre){
        this.fechapre = new SimpleStringProperty(fechapre);
    }
    
    public String getUsuario(){
        return usuario.get();
    }
    
    public void setUsuario(String usuario){
        this.usuario = new SimpleStringProperty(usuario);
    }
    
    public String getLibro(){
        return libro.get();
    }
    
    public  void setLibro(String libro){
        this.libro = new SimpleStringProperty(libro);
    }
    
    public Date getFECHDEV(){
        return FECHDEV;
    }
    
    public void setFECHDEV(Date FECHDEV){
        this.FECHDEV=FECHDEV;
    }
    
    public IntegerProperty IDDEVProperty(){
        return IDDEV;
    }
    
    public StringProperty fechapreProperty(){
        return fechapre;
    }
    
    public StringProperty usuarioProperty(){
        return usuario;
    }
    
    public StringProperty libroProperty(){
        return libro;
    }
    public int guardarRegistro(Connection connection){
        try{
            //Enviar inyeccion SQL
            PreparedStatement instruccion =
                    connection.prepareStatement("INSERT INTO DEVOLUCION (FECHPREDEV, FECHDEVDEV, LIBDEV, LECDEV) VALUES (?, ?, ?, ?)");
            
            instruccion.setString(1, fechapre.get());
            instruccion.setDate(2, FECHDEV);
            instruccion.setString(3, libro.get());
            instruccion.setString(4, usuario.get());
            return instruccion.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return  0;
        }
    }
    
    public static void llenarInformacion(Connection connection,
            ObservableList<devoluciones> lista){
        try{
            Statement instruccion = connection.createStatement();
            ResultSet resultado = instruccion.executeQuery("SELECT * FROM DEVOLUCION ");
            while(resultado.next()){
                lista.add( 
                             new devoluciones(resultado.getInt("IDDEV"), resultado.getString("FECHPREDEV"), resultado.getString("LECDEV"), resultado.getString("LIBDEV"), resultado.getDate("FECHDEVDEV"))
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
