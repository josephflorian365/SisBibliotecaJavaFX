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
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;

/**
 *
 * @author oXCToo
 */
public class usuarios {

    private IntegerProperty IDUSU;
    private StringProperty NOMUSU;
    private StringProperty APEUSU;
    private StringProperty TELUSU;
    private StringProperty NOMUSUSU;
    private StringProperty PASUSU;

    public usuarios(Integer IDUSU, String NOMUSU, String APEUSU , String TELUSU,String NOMUSUSU,String PASUSU){
        this.IDUSU= new SimpleIntegerProperty(IDUSU);
        this.NOMUSU=new SimpleStringProperty(NOMUSU);
        this.APEUSU=new SimpleStringProperty(APEUSU);
        this.TELUSU=new SimpleStringProperty(TELUSU);
        this.NOMUSUSU=new SimpleStringProperty(NOMUSUSU);
        this.PASUSU=new SimpleStringProperty(PASUSU);
    }
    
    public Integer getIDUSU(){
        return IDUSU.get();
    }
    
    public void setIDUSU(Integer IDUSU){
        this.IDUSU = new SimpleIntegerProperty(IDUSU);
        
    }
    
    public String getNOMUSU(){
        return NOMUSU.get();
    }
    
    public void setNOMUSU(String NOMUSU){
        this.NOMUSU = new SimpleStringProperty(NOMUSU);
    }
    
    public String getAPEUSU(){
        return APEUSU.get();
    }
    
    public void setAPEUSU(String APEUSU){
        this.APEUSU= new SimpleStringProperty(APEUSU);
    }
    
    public String getTELUSU(){
        return TELUSU.get();
    }
    
    public void setTELUSU(String TELUSU){
        this.TELUSU = new SimpleStringProperty(TELUSU);
    }
    
    public String getNOMUSUSU(){
        return NOMUSUSU.get();
    }
    
    public void setNOMUSUSU(String NOMUSUSU){
        this.NOMUSUSU = new SimpleStringProperty(NOMUSUSU);
    }
    
    public String getPASUSU(){
        return PASUSU.get();
    }
    
    public void setPASUSU(String PASUSU){
        this.PASUSU = new SimpleStringProperty(PASUSU);
    }
    
    public IntegerProperty IDUSUProperty(){
        return IDUSU;
    }
    
    public StringProperty NOMUSUProperty(){
        return NOMUSU;
    }
    
    public StringProperty APEUSUProperty(){
        return APEUSU;
    }
    
    public StringProperty TELUSUProperty(){
        return TELUSU;
    }
    
    public StringProperty NOMUSUSU(){
        return NOMUSUSU;
    }
    
    public StringProperty PASUSU(){
        return PASUSU;
    }
        
    public int guardarRegistro(Connection connection){
        try{
            //Enviar inyeccion SQL.
            PreparedStatement instruccion =
                    connection.prepareStatement("INSERT INTO LECTOR (NOMLEC, APELEC, TELLEC, NOMUSULEC, PASLEC )"
                    +" VALUES(?, ?, ?, ?, ?)");
           
            instruccion.setString(1, NOMUSU.get());
            instruccion.setString(2, APEUSU.get());
            instruccion.setString(3, TELUSU.get());
            instruccion.setString(4, NOMUSUSU.get());
            instruccion.setString(5, PASUSU.get());
            return instruccion.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }
    
    public int actualizarRegistro(Connection connection){
        try{
            PreparedStatement instruccion = 
                    connection.prepareStatement (
                                        "UPDATE LECTOR SET NOMLEC = ?, APELEC = ?, TELLEC = ?, NOMUSULEC = ?, PASLEC = ? WHERE IDLEC = ?");
            
            instruccion.setString(1, NOMUSU.get());
            instruccion.setString(2, APEUSU.get());
            instruccion.setString(3, TELUSU.get());
            instruccion.setString(4, NOMUSUSU.get());
            instruccion.setString(5, PASUSU.get());
            instruccion.setInt(6, IDUSU.get());
            return instruccion.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return  0;
        }
    }
    
    public int eliminarRegistro(Connection connection){
        try{
            PreparedStatement instruccion =
                    connection.prepareStatement("DELETE from LECTOR WHERE IDLEC = ?"
                    );
            instruccion.setInt(1, IDUSU.get());
            return  instruccion.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return  0;
        }
    }
    



    public static void llenarInformacionUsuarios(Connection connection,
                            ObservableList<usuarios> lista){
        try{
            Statement instruccion = connection.createStatement();
            ResultSet resultado = instruccion.executeQuery("SELECT IDLEC, NOMLEC, APELEC, TELLEC, NOMUSULEC, PASLEC FROM LECTOR");
            while(resultado.next()){
                lista.add(
                            new usuarios(
                                            resultado.getInt("IDLEC"),
                                            resultado.getString("NOMLEC"),
                                            resultado.getString("APELEC"),
                                            resultado.getString("TELLEC"),
                                            resultado.getString("NOMUSULEC"),
                                            resultado.getString("PASLEC")            
                            )
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @Override
    public String toString(){
        return NOMUSU.get() + " " + APEUSU.get();
    }
}