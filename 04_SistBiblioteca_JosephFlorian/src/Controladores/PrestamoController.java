/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controladores;

import Modelos.Autor;
import Modelos.Conexion;
import Modelos.Editorial;
import Modelos.Libros;
import Modelos.Pais;
import Modelos.Prestamo;
import Modelos.usuarios;
import java.io.IOException;
import java.net.URL;
import java.sql.Date;
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
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
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
public class PrestamoController implements Initializable {

    @FXML
    private DatePicker cmbfecha;
    @FXML
    private ComboBox<Libros> cmblibro;
    @FXML
    private ComboBox<usuarios> cmbusuario;
    @FXML
    private Button btnguardar;
    @FXML
    private Button btnnuevo;
    @FXML
    private Button btneliminar;
    @FXML
    private Button btnactualizar;
    @FXML
    private Button btnrefrescar;
    @FXML
    private TextField txtcodigo;
    @FXML
    private TableView<Prestamo> tblprestamo;
    @FXML
    private TableColumn<Prestamo, usuarios> clmnlectorlibro;
    @FXML
    private TableColumn<Prestamo, Date> clmnfecha;
    
    //Colecciones
    private ObservableList<usuarios> listausuario;
    private ObservableList<Prestamo> listaprestamo;
    private ObservableList<Libros> listalibro;

    /**
     * Initializes the controller class.
     */
    private Conexion conexion;
    @FXML
    private TableColumn<Prestamo, Libros> clmnLibro;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        conexion = new Conexion();
        conexion.conDB();
        
        //Inicializamos listas 
        listalibro=FXCollections.observableArrayList();
        listaprestamo=FXCollections.observableArrayList();
        listausuario=FXCollections.observableArrayList();
        
        //llenar listas
        usuarios.llenarInformacionUsuarios(conexion.conDB(), listausuario);
        Prestamo.llenarInformacion(conexion.conDB(), listaprestamo);
        Libros.llenarInformacionLibros(conexion.conDB(), listalibro);
        
        //Enlazar listas con combobox y tableview
        cmblibro.setItems(listalibro);
        cmbusuario.setItems(listausuario);
        tblprestamo.setItems(listaprestamo);
        
        //Enlazar columnas con atributos
        clmnlectorlibro.setCellValueFactory(new PropertyValueFactory<Prestamo, usuarios>("Usuario"));
        clmnfecha.setCellValueFactory(new PropertyValueFactory<Prestamo, Date>("FECHPRE"));
        clmnLibro.setCellValueFactory(new PropertyValueFactory<Prestamo, Libros>("libro"));
        gestionarEventos();
        
    }

    public void gestionarEventos(){
        tblprestamo.getSelectionModel().selectedItemProperty().addListener(
                new ChangeListener<Prestamo>() {
                            @Override
                            public void changed(ObservableValue<? extends Prestamo> arg0,
                                    Prestamo valorAnterior, Prestamo valorSeleccionado){
                                    if(valorSeleccionado != null){
                                        txtcodigo.setText(String.valueOf(valorSeleccionado.getIDPRE()));
                                        cmbfecha.setValue(valorSeleccionado.getFECHPRE().toLocalDate());
                                        cmbusuario.setValue(valorSeleccionado.getUsuario());
                                        cmblibro.setValue(valorSeleccionado.getLibro());
                                        
                                        btnguardar.setDisable(true);
                                        btneliminar.setDisable(false);
                                        btnactualizar.setDisable(false);
                                    }
                            }
                        }
        );
    }
    
    @FXML
    public void guardarRegistro(){
        if(validarEntradadeDatos()){
        //crear una nueva instancia del tipo Prestamo
        Prestamo f = new Prestamo(0,
                                    Date.valueOf(cmbfecha.getValue()),
                                    cmbusuario.getSelectionModel().getSelectedItem(),
                                    cmblibro.getSelectionModel().getSelectedItem());
        
        //llama al metodo guardaRRegistro de la clase Prestamo
        conexion.conDB();
        int resultado = f.guardarRegistro(conexion.conDB());
        
        if(resultado == 1){
            listaprestamo.add(f);
            refrescarData();
            //JDK8u40
            Alert mensaje = new Alert(Alert.AlertType.INFORMATION);
            mensaje.setTitle("Registro agregado");
            mensaje.setContentText("El registro ha sio agregado exitosamente");
            mensaje.setHeaderText("Resultado: ");
            mensaje.show();
        }
      }
    }
    
    @FXML
    public void actualizarRegistro(){
        Prestamo f = new Prestamo(Integer.valueOf(txtcodigo.getText()), 
                        Date.valueOf(cmbfecha.getValue()),
                        cmbusuario.getSelectionModel().getSelectedItem(),
                        cmblibro.getSelectionModel().getSelectedItem());
        conexion.conDB();
        int resultaado = f.actualizarRegistro(conexion.conDB());
        
        if(resultaado == 1){
            listaprestamo.set(tblprestamo.getSelectionModel().getSelectedIndex(), f);
            //JDK 8u40
            Alert mensaje = new Alert(Alert.AlertType.INFORMATION);
            mensaje.setTitle("Registro actualizado");
            mensaje.setContentText("El registro ha sido actualizado exitosamente");
            mensaje.setHeaderText("Resultado: ");
            mensaje.show();
        }
    }
    
    @FXML
    public void eliminarRegistro(){
        conexion.conDB();
        int resultado = tblprestamo.getSelectionModel().getSelectedItem().eliminarRegistro(conexion.conDB());
        
        if(resultado == 1)  {
            listaprestamo.remove(tblprestamo.getSelectionModel().getSelectedIndex());
            //JDK 8u40
            Alert mensaje = new Alert(Alert.AlertType.INFORMATION);
            mensaje.setTitle("Registro eliminado");
            mensaje.setContentText("El registro ha sido eliminado exitosamente");
            mensaje.setHeaderText("Resultado: ");
            mensaje.show();
        }
    }
    
    @FXML
    public void limpiarComponentes(){
        txtcodigo.setText(null);
        cmbfecha.setValue(null);
        cmbusuario.setValue(null);
        cmblibro.setValue(null);
        
        btnguardar.setDisable(false);
        btneliminar.setDisable(true);
        btnactualizar.setDisable(true);
    }
    public void refrescarData(){
        try{
            conexion.conDB();
            listalibro = FXCollections.observableArrayList();
            listaprestamo = FXCollections.observableArrayList();
            listausuario = FXCollections.observableArrayList();
            
            //Execute query
            ResultSet rs = conexion.conDB().createStatement().executeQuery("SELECT F.IDPRE, F.FECHPRE, "
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
            
            while(rs.next()){
                //get
                listaprestamo.add(new Prestamo(rs.getInt("IDPRE"), rs.getDate("FECHPRE"), 
                        new usuarios(rs.getInt("IDLEC"), rs.getString("NOMLEC"), rs.getString("APELEC"), rs.getString("TELLEC"), rs.getString("NOMUSULEC"), rs.getString("PASLEC")),
                        new Libros(rs.getInt("IDLIB"), rs.getString("TITLIB"), 
                                new Editorial(rs.getInt("IDEDI"), rs.getString("NOMEDI")),
                                new Autor(rs.getInt("IDAUT"), rs.getString("NOMAUT"), rs.getString("APEAUT"), 
                                new Pais(rs.getInt("IDPAIS"), rs.getString("NOMPAIS"))))));
            }
            
        } catch (SQLException ex) {
            System.out.println("ERROR: "+ ex);
        }
        //cell set value factory to tableview
        
        clmnLibro.setCellValueFactory(new PropertyValueFactory<Prestamo, Libros>("libro"));
        clmnfecha.setCellValueFactory(new PropertyValueFactory<Prestamo, Date>("FECHPRE"));
        clmnlectorlibro.setCellValueFactory(new PropertyValueFactory<Prestamo, usuarios>("Usuario"));
        
        tblprestamo.setItems(null);
        tblprestamo.setItems(listaprestamo);
    }
    
    @FXML
    public void orderBy(){
        try{
            conexion.conDB();
            listalibro = FXCollections.observableArrayList();
            listaprestamo = FXCollections.observableArrayList();
            listausuario = FXCollections.observableArrayList();
            
            //Execute query
            ResultSet rs = conexion.conDB().createStatement().executeQuery("SELECT F.IDPRE, F.FECHPRE, "
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
                    + "ON(A.IDPAIS = P.IDPAIS) "
                    + "ORDER BY O.TITLIB");
            
            while(rs.next()){
                //get
                listaprestamo.add(new Prestamo(rs.getInt("IDPRE"), rs.getDate("FECHPRE"), 
                        new usuarios(rs.getInt("IDLEC"), rs.getString("NOMLEC"), rs.getString("APELEC"), rs.getString("TELLEC"), rs.getString("NOMUSULEC"), rs.getString("PASLEC")),
                        new Libros(rs.getInt("IDLIB"), rs.getString("TITLIB"), 
                                new Editorial(rs.getInt("IDEDI"), rs.getString("NOMEDI")),
                                new Autor(rs.getInt("IDAUT"), rs.getString("NOMAUT"), rs.getString("APEAUT"), 
                                new Pais(rs.getInt("IDPAIS"), rs.getString("NOMPAIS"))))));
            }
            
        } catch (SQLException ex) {
            System.out.println("ERROR: "+ ex);
        }
        //cell set value factory to tableview
        
        clmnLibro.setCellValueFactory(new PropertyValueFactory<Prestamo, Libros>("libro"));
        clmnfecha.setCellValueFactory(new PropertyValueFactory<Prestamo, Date>("FECHPRE"));
        clmnlectorlibro.setCellValueFactory(new PropertyValueFactory<Prestamo, usuarios>("Usuario"));
        
        tblprestamo.setItems(null);
        tblprestamo.setItems(listaprestamo);
    }
    
    private boolean validarEntradadeDatos(){
        String errorMessage = "";
        if(cmblibro.getSelectionModel().getSelectedItem() == null){
            errorMessage += "Item inválido\n";
        }
        if(cmbusuario.getSelectionModel().getSelectedItem() == null){
            errorMessage += "Item no válido\n";
        }
        if(cmbfecha.getValue() ==null){
            errorMessage += "Fecha no válida\n";
        }
        if(errorMessage.length() == 0){
            return  true;
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
            
            Parent root = loader.load();
            
           PrincipalController controlador = loader.getController();
            
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            
            stage.setScene(scene);
            new animatefx.animation.ZoomIn(root).play();
            stage.show();
            
           } catch (IOException ex) {
            Logger.getLogger(Vista_usuarioController.class.getName()).log(Level.SEVERE, null, ex);
        } 
      }
    }

