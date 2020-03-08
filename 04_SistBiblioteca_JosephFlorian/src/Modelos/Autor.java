
package Modelos;

import java.sql.Connection;
import java.sql.PreparedStatement;
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
import javafx.beans.property.StringPropertyBase;
import javafx.collections.ObservableList;


public class Autor {
     private IntegerProperty IDAUT;
     private StringProperty NOMAUT;
     private StringProperty APEAUT;
     private Pais pais;
     
     public Autor(Integer IDAUT ,String NOMAUT, String APEAUT, Pais pais){
         this.IDAUT=new SimpleIntegerProperty(IDAUT);
         this.NOMAUT=new SimpleStringProperty(NOMAUT);
         this.APEAUT=new SimpleStringProperty(APEAUT);
         this.pais= pais;
     }
     
     public Integer getIDAUT(){
         return IDAUT.get();
     }
     
     public void setIDAUT(Integer IDAUT){
         this.IDAUT = new SimpleIntegerProperty(IDAUT);
     }
     
     public String getNOMAUT(){
         return NOMAUT.get();
     }
     
     public void setNOMAUT(String NOMAUT){
         this.NOMAUT = new SimpleStringProperty(NOMAUT);
     }
     
     public String getAPEAUT(){
         return APEAUT.get();
     }
     
     public void setAPEAUT(String APEAUT){
         this.APEAUT= new SimpleStringProperty(APEAUT);
     }
     
     public Pais getPais(){
         return pais;
     }
     
     public void setPais(Pais pais){
         this.pais= pais;
     }
     
     public IntegerProperty IDAUTProperty(){
         return IDAUT;
     }
     
     public StringProperty NOMAUTProperty(){
         return NOMAUT;
     }
     public StringProperty APEAUT(){
         return APEAUT;
     }
     
     public  int guardarRegistro(Connection connection){
         try{
             //Evitar inyeccion SQL
             PreparedStatement instruccion = 
                     connection.prepareStatement("INSERT INTO AUTOR (NOMAUT, APEAUT, IDPAIS) VALUES (?, ?, ?)");
             
             instruccion.setString(1, NOMAUT.get());
             instruccion.setString(2, APEAUT.get());
             instruccion.setInt(3, pais.getIDPAIS());
             return instruccion.executeUpdate();
         } catch (SQLException e) {
             e.printStackTrace();
             return 0;
         }
     }
     
     public int actualizarRegistro(Connection connection){
         try{
             PreparedStatement instruccion = 
                                            connection.prepareStatement("UPDATE AUTOR SET NOMAUT = ?, APEAUT = ?, IDPAIS = ? WHERE IDAUT = ?");
             
             instruccion.setString(1, NOMAUT.get());
             instruccion.setString(2, APEAUT.get());
             instruccion.setInt(3, pais.getIDPAIS());
             instruccion.setInt(4, IDAUT.get());
             return instruccion.executeUpdate();
             
         } catch (SQLException e) {
             e.printStackTrace();
             return 0;
         }
     }
     
     public int eliminarRegistro(Connection connection){
         try{
             PreparedStatement instruccion =
                                        connection.prepareStatement(
                                                "DELETE FROM AUTOR "+
                                                "WHERE IDAUT = ?"
                                        );
             instruccion.setInt(1, IDAUT.get());
             return instruccion.executeUpdate();
         } catch (SQLException e) {
             e.printStackTrace();
             return 0;
     }
     }
     public static  void llenarInformacionAutor(Connection connection,
                                ObservableList<Autor> lista){
                try{
         Statement instruccion = connection.createStatement();
         ResultSet resultado = instruccion.executeQuery(
                                                    "SELECT A.IDAUT, "
                                                    + "A.NOMAUT, "
                                                    + "A.APEAUT, "
                                                    + "A.IDPAIS, "
                                                    + "P.NOMPAIS "
                                                    + "FROM AUTOR A "
                                                    + "INNER JOIN PAIS P "
                                                    + "ON (A.IDPAIS = P.IDPAIS) "
         );
         while(resultado.next()){
             lista.add(
                     new Autor(
                             resultado.getInt("IDAUT"),
                             resultado.getString("NOMAUT"),
                             resultado.getString("APEAUT"),
                             new Pais(resultado.getInt("IDPAIS"),
                             resultado.getString("NOMPAIS")
                             )                    
                     )
             );
         }
         
     }   catch (SQLException e) {
             e.printStackTrace();
         }
     }
     @Override
     public String toString(){
         return NOMAUT.get() + " " + APEAUT.get() ;
     }
}
