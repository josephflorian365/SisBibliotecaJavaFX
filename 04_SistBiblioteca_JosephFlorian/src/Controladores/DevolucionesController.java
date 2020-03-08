/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controladores;

import Modelos.Conexion;
import Modelos.Libros;
import Modelos.Prestamo;
import Modelos.devoluciones;
import Modelos.usuarios;
import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
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
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
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
public class DevolucionesController implements Initializable {
    //PRESTAMO GUIS
    @FXML
    private TableView<Prestamo> tblPre;
    @FXML
    private TableColumn<Prestamo, Integer> clmnIdPre;
    @FXML
    private TableColumn<Prestamo, Date> clmnfechaPre;
    @FXML
    private TableColumn<Prestamo, Libros> clmnLibro;
    @FXML
    private TableColumn<Prestamo, usuarios> clmnLector;
    @FXML
    private TextField tctLibro;
    @FXML
    private TextField txtLector;
    @FXML
    private TextField txtfechaPre;
    @FXML
    private DatePicker cmbDev;
    
    //DEVOLUCIONES GUIS
    @FXML
    private Button btnDevolver;
    @FXML
    private TableView<devoluciones> tblDev;
    @FXML
    private TableColumn<devoluciones,Integer> clmnIdDev;
    @FXML
    private TableColumn<devoluciones, String> clmnfechaPrestado;
    @FXML
    private TableColumn<devoluciones,Date> clmnfechaDev;
    @FXML
    private TableColumn<devoluciones, String> clmnLibroDev;
    @FXML
    private TableColumn<devoluciones, String> clmnLecDev;
    
    //PRESTAMO
    private ObservableList<Prestamo> listaprestamo;
    private ObservableList<usuarios> listausuarios;
    private ObservableList<Libros> listalibros;
    
    //devoluciones
    private ObservableList<devoluciones> listadevolucion;

    
    private Conexion conexion;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        conexion= new Conexion();
        conexion.conDB();
        
        //inicializamos listas PRESTAMO
        listaprestamo= FXCollections.observableArrayList();
        listalibros= FXCollections.observableArrayList();
        listausuarios= FXCollections.observableArrayList();
        
        //inicializamos listas devoluciones
        listadevolucion=FXCollections.observableArrayList();
        
        //llenar listas PRESTAMO
        Prestamo.llenarInformacion(conexion.conDB(), listaprestamo);
        usuarios.llenarInformacionUsuarios(conexion.conDB(), listausuarios);
        Libros.llenarInformacionLibros(conexion.conDB(), listalibros);
        
        //llenar listas devoluciones
        devoluciones.llenarInformacion(conexion.conDB(), listadevolucion);

        
        // Enlazar listas con tableview PRESTAMO
        tblPre.setItems(listaprestamo);
        
        // Enlazar listas con tableview devoluciones
        tblDev.setItems(listadevolucion);

        
        //enlazar columnas con atributos PRESTAMO
        clmnIdPre.setCellValueFactory(new PropertyValueFactory<Prestamo, Integer>("IDPRE"));
        clmnfechaPre.setCellValueFactory(new PropertyValueFactory<Prestamo, Date>("FECHPRE"));
        clmnLibro.setCellValueFactory(new PropertyValueFactory<Prestamo, Libros>("libro"));
        clmnLector.setCellValueFactory(new PropertyValueFactory<Prestamo, usuarios>("Usuario"));
        
        //enlazar columnas con atributos devoluciones
        clmnIdDev.setCellValueFactory(new PropertyValueFactory<devoluciones, Integer>("IDDEV"));
        clmnfechaPrestado.setCellValueFactory(new PropertyValueFactory<devoluciones, String>("fechapre"));
        clmnfechaDev.setCellValueFactory(new PropertyValueFactory<devoluciones, Date>("FECHDEV"));
        clmnLibroDev.setCellValueFactory(new PropertyValueFactory<devoluciones, String>("libro"));
        clmnLecDev.setCellValueFactory(new PropertyValueFactory<devoluciones, String>("usuario"));
        
      
        gestionarEventos();
    }    
    public void gestionarEventos(){
        tblPre.getSelectionModel().selectedItemProperty().addListener(
                new ChangeListener<Prestamo>() {
                    @Override
                    public void changed(ObservableValue<? extends Prestamo> arg0,
                            Prestamo valorAnterior, Prestamo valorSeleccionado){
                        if(valorSeleccionado != null){
                            txtfechaPre.setText(String.valueOf(valorSeleccionado.getFECHPRE()));
                            txtLector.setText(String.valueOf(valorSeleccionado.getUsuario()));
                            tctLibro.setText(String.valueOf(valorSeleccionado.getLibro()));
                        }
                    }
            
        }
        );
    }
    public void guardarRegistro(){
        //crear una nueva instancia del tipo devoluciones
        devoluciones d = new devoluciones(0, 
                txtfechaPre.getText(), 
                txtLector.getText(), 
                tctLibro.getText(), 
                Date.valueOf(cmbDev.getValue()));
        //llamar al metodo de la clase devoluciones
        conexion.conDB();
        int resultado = d.guardarRegistro(conexion.conDB());
        
        if(resultado == 1){
            listadevolucion.add(d);
            
            //JDK 8u40
            Alert mensaje = new Alert(Alert.AlertType.INFORMATION);
            mensaje.setTitle("Libro devuelto");
            mensaje.setContentText("El libro fue entregado exitosamente");
            mensaje.setHeaderText("Resultado: ");
            mensaje.show();
        }
    }
    
    public void eliminarRegistro(){
        
        conexion.conDB();
        int resultado = tblPre.getSelectionModel().getSelectedItem().eliminarRegistro(conexion.conDB());
        
        if(resultado == 1){
            listaprestamo.remove(tblPre.getSelectionModel().getSelectedIndex());
            
        }
    }
    
    public void refrescartabla(){
        try{
            conexion.conDB();
            listadevolucion = FXCollections.observableArrayList();

            //execute Query
            ResultSet rs = conexion.conDB().createStatement().executeQuery("SELECT * FROM DEVOLUCION");
                  while(rs.next()){
                      listadevolucion.add(new devoluciones(rs.getInt("IDDEV"), rs.getString("FECHPREDEV"), rs.getString("LECDEV"), rs.getString("LIBDEV"), rs.getDate("FECHDEVDEV")));
                  }  
        } catch (SQLException ex) {
            System.out.println("ERROR: " +ex);
        }
        
        //cell set value factory to tableview
        
        clmnIdDev.setCellValueFactory(new PropertyValueFactory<devoluciones, Integer>("IDDEV"));
        clmnfechaPrestado.setCellValueFactory(new PropertyValueFactory<devoluciones, String>("fechapre"));
        clmnfechaDev.setCellValueFactory(new PropertyValueFactory<devoluciones, Date>("FECHDEV"));
        clmnLibroDev.setCellValueFactory(new PropertyValueFactory<devoluciones, String>("libro"));
        clmnLecDev.setCellValueFactory(new PropertyValueFactory<devoluciones, String>("usuario"));
        
        tblDev.setItems(null);
        tblDev.setItems(listadevolucion);
        
    }
    
    @FXML
    public void devolverLibro(){
        eliminarRegistro();
        guardarRegistro();
        refrescartabla();
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
