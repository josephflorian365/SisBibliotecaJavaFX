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
import javafx.collections.ObservableList;

/**
 *
 * @author Js
 */
public class Prestamo {
    private IntegerProperty IDPRE;
    private Date FECHPRE;
    private usuarios Usuario;
    private Libros libro;
    
    public Prestamo(Integer IDPRE, Date FECHPRE, usuarios Usuario, Libros libro){
        this.IDPRE = new SimpleIntegerProperty(IDPRE);
        this.FECHPRE = FECHPRE;
        this.Usuario = Usuario;
        this.libro = libro;
    }
    
    public Integer getIDPRE(){
        return IDPRE.get();
    }
    
    public void setIDPRE(Integer IDPRE){
        this.IDPRE = new SimpleIntegerProperty(IDPRE);
    }
    
    public Date getFECHPRE(){
        return FECHPRE;
    }
    
    public void setFECHPRE(Date FECHPRE){
        this.FECHPRE = FECHPRE;
    }
    
    public usuarios getUsuario(){
        return Usuario;
    }
    
    public void setUsuario(usuarios Usuario){
        this.Usuario = Usuario;
    }
    
    public Libros getLibro(){
        return libro;
    }
    
    public void setLibro(Libros libro){
        this.libro = libro;
    }
    
    public IntegerProperty IDPREProperty(){
        return IDPRE;
    }
    
    public int guardarRegistro(Connection connection){
        try{
            //Enviar inyeccion SQL
            PreparedStatement instruccion = 
                    connection.prepareStatement("INSERT INTO PRESTAMO (FECHPRE, IDLEC, IDLIB) VALUES (?, ?, ?)");
            
            instruccion.setDate(1, FECHPRE);
            instruccion.setInt(2, Usuario.getIDUSU());
            instruccion.setInt(3, libro.getIDLIB());
            return instruccion.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }
    
    public int actualizarRegistro(Connection connection){
        try{
            PreparedStatement instruccion = 
                    connection.prepareStatement("UPDATE PRESTAMO SET FECHPRE = ?, IDLEC = ?, IDLIB = ? WHERE IDPRE = ?");
            instruccion.setDate(1, FECHPRE);
            instruccion.setInt(2, Usuario.getIDUSU());
            instruccion.setInt(3, libro.getIDLIB());
            instruccion.setInt(4, IDPRE.get());
            return instruccion.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }
    
    public int eliminarRegistro(Connection connection){
        try{
            PreparedStatement instruccion =
                    connection.prepareStatement("DELETE FROM PRESTAMO WHERE IDPRE = ?");
            instruccion.setInt(1, IDPRE.get());
            return instruccion.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }
    
    public static void llenarInformacion(Connection connection,
            ObservableList<Prestamo> lista){
        try{
            Statement instruccion = connection.createStatement();
            ResultSet resultado = instruccion.executeQuery("SELECT F.IDPRE, F.FECHPRE, "
                    + "L.IDLEC, "
                    + "L.NOMLEC, L.APELEC, "
                    + "L.TELLEC, L.NOMUSULEC, L.PASLEC, "
                    + "O.IDLIB,"
                    + "O.TITLIB, "
                    + "E.IDEDI,"
                    + "E.NOMEDI,"
                    + "A.IDAUT,"
                    + "A.NOMAUT,"
                    + "A.APEAUT,"
                    + "P.IDPAIS,"
                    + "P.NOMPAIS "
                    + "FROM PRESTAMO F "
                    + "INNER JOIN LECTOR L "
                    + "ON (F.IDLEC = L.IDLEC) "
                    + "INNER JOIN LIBRO O "
                    + "ON (F.IDLIB = O.IDLIB)"
                    + "INNER JOIN EDITORIAL E "
                    + "ON (O.IDEDI = E.IDEDI) "
                    + "INNER JOIN AUTOR A "
                    + "ON (O.IDAUT = A.IDAUT) "
                    + "INNER JOIN PAIS P "
                    + "ON(A.IDPAIS = P.IDPAIS)");
            while(resultado.next()){
                lista.add(
                             new Prestamo(resultado.getInt("IDPRE"), 
                                            resultado.getDate("FECHPRE"), 
                                            new usuarios(resultado.getInt("IDLEC"), 
                                                         resultado.getString("NOMLEC"), 
                                                         resultado.getString("APELEC"), 
                                                         resultado.getString("TELLEC"), 
                                                         resultado.getString("NOMUSULEC"), 
                                                         resultado.getString("PASLEC")),
                                            new Libros(resultado.getInt("IDLIB"), 
                                                       resultado.getString("TITLIB"), 
                                                                    new Editorial(resultado.getInt("IDEDI"), resultado.getString("NOMEDI")),
                                                                    new Autor(resultado.getInt("IDAUT"), resultado.getString("NOMAUT"), resultado.getString("APEAUT"), 
                                                                                       new Pais(resultado.getInt("IDPAIS"), resultado.getString("NOMPAIS")))
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
         return FECHPRE.toString();
    }
}
