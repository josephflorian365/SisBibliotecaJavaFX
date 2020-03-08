/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controladores;

import Modelos.Conexion;
import Modelos.Editorial;
import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Observable;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Js
 */
public class EditorialController implements Initializable {

    @FXML
    private Button btnGuardarEdi;
    @FXML
    private Button btnEditarEdi;
    @FXML
    private Button btnEliminarEdi;
    @FXML
    private JFXTextField txttituloedi;
    @FXML
    private TextField txtcodigo;
    @FXML
    private TableView<Editorial> tbledi;
    @FXML
    private TableColumn<Editorial, String> clmnntituloedi;

    private ObservableList<Editorial> listaeditorial;
    
    private Conexion conexion;
    @FXML
    private Button btnactualizar;
    @FXML
    private Button btnrefrescar;

    
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        conexion = new Conexion();
        conexion.conDB();
        
        //inicializamos listas
        listaeditorial = FXCollections.observableArrayList();
        
        //llenar listas
        Editorial.llenarInformacionEditorial(conexion.conDB(), listaeditorial);
        
        //Enlazar lista con tableview
        tbledi.setItems(listaeditorial);
        
        //Enlazar columnas con atributos
        clmnntituloedi.setCellValueFactory(new PropertyValueFactory<Editorial, String>("NOMEDI") );
        gestionarEventos();
        
    }

    public void gestionarEventos(){
        tbledi.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Editorial>(){
            @Override
            public void changed(ObservableValue<? extends Editorial> arg0,
                    Editorial valorAnterior, Editorial valorSeleccionado){
                if (valorSeleccionado != null){
                    txtcodigo.setText(String.valueOf(valorSeleccionado.getIDEDI()));
                    txttituloedi.setText(valorSeleccionado.getNOMEDI());
                    
                    btnGuardarEdi.setDisable(true);
                    btnEliminarEdi.setDisable(false);
                    btnactualizar.setDisable(false);
                }
            }
        });
    }
    
    @FXML
    public void guardarRegistro(){
        if(validarEntradadeDatos()){
        //Crear una nueva instancia del tipo Editorial
        Editorial e = new Editorial(0, txttituloedi.getText());
        conexion.conDB();
        int resultado = e.guardarRegistro(conexion.conDB());
        
        if(resultado == 1){
            listaeditorial.add(e);
            refrescarData();
            //JDK 8u40
            Alert mensaje = new Alert(Alert.AlertType.INFORMATION);
            mensaje.setTitle("Registro agregado");
            mensaje.setContentText("El registro ha sido agregado exitosamente");
            mensaje.setHeaderText("Resultado: ");
            mensaje.show();
        }
        }
    }
    
    @FXML
    public void actualizarRegistro(){
        Editorial e = new Editorial(Integer.valueOf(txtcodigo.getText()), 
                                                 txttituloedi.getText());
        conexion.conDB();
        int resultado = e.actualizarRegistro(conexion.conDB());
        
        if(resultado == 1){
            listaeditorial.set(tbledi.getSelectionModel().getSelectedIndex(), e);
            //JDK 8u40
            Alert mensaje = new Alert(AlertType.INFORMATION);
            mensaje.setTitle("Registro actualizado");
            mensaje.setContentText("El registro ha sido actualizado exitosamente");
            mensaje.setHeaderText("Resultado: ");
            mensaje.show();
        }
    }
    
    @FXML
    public void eliminarRegistro(){
        conexion.conDB();
        int resultado = tbledi.getSelectionModel().getSelectedItem().eliminarRegistro(conexion.conDB());
        
        if(resultado == 1){
            listaeditorial.remove(tbledi.getSelectionModel().getSelectedIndex());
            //JDK 8u40
            Alert mensaje = new Alert(AlertType.INFORMATION);
            mensaje.setTitle("Registro eliminado");
            mensaje.setContentText("El registro ha sido eliminado exitosamente");
            mensaje.setHeaderText("Resultado: ");
            mensaje.show();
            
        }
    }
    
    @FXML
    public void limpiarcomponentes(){
        txtcodigo.setText(null);
        txttituloedi.setText(null);
        
        btnGuardarEdi.setDisable(false);
        btnEliminarEdi.setDisable(true);
        btnactualizar.setDisable(true);
    }
    
    public void refrescarData(){
        try{
            conexion.conDB();
            listaeditorial = FXCollections.observableArrayList();

            //Excute Query
            ResultSet rs = conexion.conDB().createStatement().executeQuery("SELECT * FROM EDITORIAL");
            while(rs.next()){
            //get
            listaeditorial.add(new Editorial(rs.getInt(1), rs.getString(2)));
        }
        } catch (SQLException ex) {
            System.out.println("ERROR: "+ex);
        }
        
        clmnntituloedi.setCellValueFactory(new PropertyValueFactory<Editorial, String>("NOMEDI"));
        
        tbledi.setItems(null);
        tbledi.setItems(listaeditorial);
    }
    @FXML
    public void orderBy(){
        try{
            conexion.conDB();
            listaeditorial = FXCollections.observableArrayList();

            //Excute Query
            ResultSet rs = conexion.conDB().createStatement().executeQuery("SELECT * FROM EDITORIAL ORDER BY NOMEDI");
            while(rs.next()){
            //get
            listaeditorial.add(new Editorial(rs.getInt(1), rs.getString(2)));
        }
        } catch (SQLException ex) {
            System.out.println("ERROR: "+ex);
        }
        
        clmnntituloedi.setCellValueFactory(new PropertyValueFactory<Editorial, String>("NOMEDI"));
        
        tbledi.setItems(null);
        tbledi.setItems(listaeditorial);
    }
    
    private boolean validarEntradadeDatos(){
        String errorMessage = "";
        if(txttituloedi.getText() == null || txttituloedi.getText().length() == 0){
            errorMessage += "Titutlo inválido";
        }
        if(errorMessage.length() == 0){
            return true;
        }else{
            //mostrando el mensaje de error
            Alert mensaje = new Alert(Alert.AlertType.ERROR);
            mensaje.setTitle("Registro no válido");
            mensaje.setHeaderText("Campos invalidos por favor corrija...");
            mensaje.setContentText(errorMessage);
            mensaje.show();
            return false;
        }
    }
    
    
    
    public void closeWindows(){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Vistas/Principal.fxml"));
            
            Parent root= loader.load();
            
            PrincipalController controlador = loader.getController();
            
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            
            stage.setScene(scene);
            new animatefx.animation.ZoomIn(root).play();
            stage.show();
            
            
            
        } catch (IOException ex) {
            Logger.getLogger(EditorialController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
