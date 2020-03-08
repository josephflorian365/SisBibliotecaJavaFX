/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;

/**
 *
 * @author Js
 */
public class Libros {
    
    private IntegerProperty IDLIB;
    private StringProperty TITLIB;
    private Editorial EDITORIAL;
    private Autor AUTOR;
     
    public Libros(Integer IDLIB, String TITLIB,Editorial EDITORIAL, Autor AUTOR){
        this.IDLIB= new SimpleIntegerProperty(IDLIB);
        this.TITLIB = new SimpleStringProperty(TITLIB);
        this.EDITORIAL = EDITORIAL;
        this.AUTOR = AUTOR;
    }
    
    public Integer getIDLIB(){
        return IDLIB.get();
    }
    
    public void setIDLIB(Integer IDLIB){
        this.IDLIB = new SimpleIntegerProperty(IDLIB);
    }
    
    public String getTITLIB(){
        return TITLIB.get();
    }
    
    public void setTITLIB(String TITLIB){
    this.TITLIB = new SimpleStringProperty(TITLIB);
    }
    
    public Editorial getEDITORIAL(){
        return EDITORIAL;
    }
    
    public void setEDITORIAL(Editorial EDITORIAL){
        this.EDITORIAL = EDITORIAL;
    }
    
    public Autor getAUTOR(){
        return AUTOR;
    }
    
    public void setAUTOR(Autor AUTOR){
        this.AUTOR= AUTOR;
    }
    public IntegerProperty IDLIBProperty(){
        return IDLIB;
    }
    
    public  StringProperty TITLIBProperty(){
        return TITLIB;
    }
    
    public int guardarRegistro(Connection connection){
        try{
            //Evitar inyeccion SQL
            PreparedStatement instruccion = 
                    connection.prepareStatement("INSERT INTO LIBRO (TITLIB ,IDEDI ,IDAUT ) VALUES (?, ?, ?)");
            instruccion.setString(1, TITLIB.get());
            instruccion.setInt(2, EDITORIAL.getIDEDI());
            instruccion.setInt(3, AUTOR.getIDAUT());
            return instruccion.executeUpdate();
        } catch (SQLException e) {
           e.printStackTrace();
           return  0;
        }
    }
    
    public int actualizarRegistro(Connection connection){
        try{
            PreparedStatement instruccion =
                    connection.prepareStatement("UPDATE LIBRO SET TITLIB = ?, IDEDI = ?, IDAUT = ? WHERE IDLIB = ?");
            instruccion.setString(1, TITLIB.get());
            instruccion.setInt(2, EDITORIAL.getIDEDI());
            instruccion.setInt(3, AUTOR.getIDAUT());
            instruccion.setInt(4, IDLIB.get());
            return instruccion.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }
    
    public int eliminarRegistro(Connection connection){
        try{
            PreparedStatement instruccion = connection.prepareStatement("DELETE FROM LIBRO WHERE IDLIB = ?");
            instruccion.setInt(1, IDLIB.get());
            return instruccion.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }
    
    public static void llenarInformacionLibros(Connection connection, 
            ObservableList<Libros> lista){
            try{
                Statement instruccion = connection.createStatement();
                ResultSet resultado = instruccion.executeQuery( 
                        "SELECT L.IDLIB, L.TITLIB, "
                                + "E.IDEDI, E.NOMEDI, "
                                + "A.IDAUT, A.NOMAUT, A.APEAUT, "
                                + "P.IDPAIS, P.NOMPAIS "
                                + "FROM LIBRO L, EDITORIAL E, AUTOR A, PAIS P "
                                + "WHERE L.IDEDI = E.IDEDI "
                                + "AND L.IDAUT = A.IDAUT "
                                + "AND A.IDPAIS = P.IDPAIS"
                );
                while(resultado.next()){
                    lista.add(
                            new Libros(
                                    resultado.getInt("IDLIB"),
                                    resultado.getString("TITLIB"),
                                    new Editorial(resultado.getInt("IDEDI"),
                                            resultado.getString("NOMEDI")),
                                    
                                    new Autor(resultado.getInt("IDAUT"),
                                            resultado.getString("NOMAUT"), 
                                            resultado.getString("APEAUT"),
                                            
                                            new Pais(resultado.getInt("IDPAIS"),
                                            resultado.getString("NOMPAIS"))
                                )
                            )
                    );
                }
            } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @Override
    public String toString(){
        return TITLIB.get();
    }
    
}
