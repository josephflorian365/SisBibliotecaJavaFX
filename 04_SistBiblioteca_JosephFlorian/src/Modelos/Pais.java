/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelos;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Observable;
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
public class Pais {
    private IntegerProperty IDPAIS;
    private StringProperty NOMPAIS;
    
    public Pais(Integer IDPAIS,String NOMPAIS){
        this.IDPAIS=new SimpleIntegerProperty(IDPAIS);
        this.NOMPAIS=new SimpleStringProperty(NOMPAIS);
    }
    
    public Integer getIDPAIS(){
    return IDPAIS.get();
    }
    
    public void setIDPAIS(Integer IDPAIS){
        this.IDPAIS= new SimpleIntegerProperty();
    }
    
    public String getNOMPAIS(){
        return NOMPAIS.get();
    }
    
    public void setNOMPAIS(String NOMPAIS){
        this.NOMPAIS = new SimpleStringProperty(NOMPAIS);
    }
    
    public IntegerProperty IDPAISProperty(){
        return IDPAIS;
    }
    
    public StringProperty NOMPAIS(){
        return NOMPAIS;
    }
    
    public static void llenarInformacion(Connection connection, ObservableList<Pais>lista){
        try{
            Statement statement = connection.createStatement();
            ResultSet resultado = statement.executeQuery("SELECT IDPAIS, NOMPAIS FROM PAIS");
            while(resultado.next()){
                lista.add(
                        new Pais(
                                    resultado.getInt("IDPAIS"),
                                    resultado.getString("NOMPAIS")
                        )
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @Override
    public String toString(){
        return NOMPAIS.get();
    }
    
}
