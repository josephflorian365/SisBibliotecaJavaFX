/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controladores;

import Modelos.usuarios;
import Modelos.Conexion;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javax.swing.JOptionPane;

/**
 * FXML Controller class
 *
 * @author Js
 */
public class Vista_usuarioController implements Initializable {
    //columnas

    @FXML
    private TableColumn<usuarios, String> clmnNombre;
    @FXML
    private TableColumn<usuarios, String> clmnApellido;
    @FXML
    private TableColumn<usuarios, String> clmnTelefono;
    @FXML
    private TableColumn<usuarios, String> clmnNombreUsuario;
    @FXML
    private TableColumn<usuarios, String> clmnPassword;

    //Componentes GUI
    @FXML
    private TextField txtCodigo;
    @FXML
    private JFXTextField txtNombresUsu;
    @FXML
    private JFXTextField txtApeUsu;
    @FXML
    private JFXTextField txtTelUsu;
    @FXML
    private JFXTextField txtuserUsu;
    @FXML
    private JFXTextField txtcontraUsu;
    @FXML
    private Button btnrefrescar;
    @FXML
    private Button btnGuUsu;
    @FXML
    private Button btnActUsu;
    @FXML
    private Button btnElUsu;
    @FXML
    private TableView<usuarios> tblUs;

    //Colecciones
    private ObservableList<usuarios> listausuarios;

    private Conexion conexion;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // TODO
        conexion = new Conexion();
        conexion.conDB();

        //Inicializar listas
        listausuarios = FXCollections.observableArrayList();

        //llenar listas
        usuarios.llenarInformacionUsuarios(conexion.conDB(), listausuarios);

        //Enlazar tableview
        tblUs.setItems(listausuarios);

        //Enlazar columnas con atributos      
        clmnNombre.setCellValueFactory(new PropertyValueFactory<usuarios, String>("NOMUSU"));
        clmnApellido.setCellValueFactory(new PropertyValueFactory<usuarios, String>("APEUSU"));
        clmnTelefono.setCellValueFactory(new PropertyValueFactory<usuarios, String>("TELUSU"));
        clmnNombreUsuario.setCellValueFactory(new PropertyValueFactory<usuarios, String>("NOMUSUSU"));
        clmnPassword.setCellValueFactory(new PropertyValueFactory<usuarios, String>("PASUSU"));

        gestionarEventos();

    }

    public void gestionarEventos() {
        tblUs.getSelectionModel().selectedItemProperty().addListener(
                new ChangeListener<usuarios>() {
            @Override
            public void changed(ObservableValue<? extends usuarios> arg0,
                    usuarios valorAnterior, usuarios valorSeleccionado) {
                if (valorSeleccionado != null) {
                    txtCodigo.setText(String.valueOf(valorSeleccionado.getIDUSU()));
                    txtNombresUsu.setText(valorSeleccionado.getNOMUSU());
                    txtApeUsu.setText(valorSeleccionado.getAPEUSU());
                    txtTelUsu.setText(valorSeleccionado.getTELUSU());
                    txtuserUsu.setText(valorSeleccionado.getNOMUSUSU());
                    txtcontraUsu.setText(valorSeleccionado.getPASUSU());

                    btnGuUsu.setDisable(true);
                    btnElUsu.setDisable(false);
                    btnActUsu.setDisable(false);
                }
            }
        }
        );
    }

    @FXML
    public void guardarRegistro() {
        if(validarEntradadeDatos()){
             //Crear una nueva instancia del usuario
           usuarios u = new usuarios(0,
                txtNombresUsu.getText(),
                txtApeUsu.getText(),
                txtTelUsu.getText(),
                txtuserUsu.getText(),
                txtcontraUsu.getText());
        
      
       //Llamar el metodo guardarRegistro de la clase usuarios
        int resultado = u.guardarRegistro(conexion.conDB());
        
            if(resultado == 1) {
            listausuarios.add(u);
            idmostrar();
            //JDK 8u40
            Alert mensaje = new Alert(AlertType.INFORMATION);
            mensaje.setTitle("Registro agregado");
            mensaje.setContentText("El registro se ha egregado correctamente");
            mensaje.setHeaderText("Resultado:");
            mensaje.show();
        
        } 
        }
        
    }

    @FXML
    public void actualizarRegistro() {
        usuarios u = new usuarios(
                Integer.valueOf(txtCodigo.getText()),
                txtNombresUsu.getText(),
                txtApeUsu.getText(),
                txtTelUsu.getText(),
                txtuserUsu.getText(),
                txtcontraUsu.getText()
        );
        conexion.conDB();
        int resultado = u.actualizarRegistro(conexion.conDB());

        if (resultado == 1) {
            listausuarios.set(tblUs.getSelectionModel().getSelectedIndex(), u);

            Alert mensaje = new Alert(AlertType.INFORMATION);
            mensaje.setTitle("Registro actualizado");
            mensaje.setContentText("El registro ha sido actualizado exitosamente");
            mensaje.setHeaderText("Resultado:");
            mensaje.show();
        }

    }

    @FXML
    public void eliminarRegistro() {
        conexion.conDB();
        int resultado = tblUs.getSelectionModel().getSelectedItem().eliminarRegistro(conexion.conDB());

        if (resultado == 1) {
            listausuarios.remove(tblUs.getSelectionModel().getSelectedIndex());
            Alert mensaje = new Alert(AlertType.INFORMATION);
            mensaje.setTitle("Registro eliminado");
            mensaje.setContentText("El registro ha sido eliminado exitosamente");
            mensaje.setHeaderText("Resultado:");
            mensaje.show();
        }
    }

    @FXML
    public void limpiarComponentes() {
        txtCodigo.setText("");
        txtNombresUsu.setText("");
        txtApeUsu.setText("");
        txtTelUsu.setText("");
        txtuserUsu.setText("");
        txtcontraUsu.setText("");

        btnGuUsu.setDisable(false);
        btnElUsu.setDisable(true);
        btnActUsu.setDisable(true);
    }

    public void closeWindows() {
        try {
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

    @FXML
    private void orderby(ActionEvent event) {
        try {
            conexion.conDB();
            listausuarios = FXCollections.observableArrayList();
            //Execute query
            ResultSet rs = conexion.conDB().createStatement().executeQuery("SELECT * FROM LECTOR  ORDER BY NOMLEC");
            while (rs.next()) {
                //get
                listausuarios.add(new usuarios(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6)));
            }
        } catch (SQLException ex) {
            System.out.println("ERROR: " + ex);
        }
        //cell set value factory to tableview

        clmnNombre.setCellValueFactory(new PropertyValueFactory<>("NOMUSU"));
        clmnApellido.setCellValueFactory(new PropertyValueFactory<>("APEUSU"));
        clmnTelefono.setCellValueFactory(new PropertyValueFactory<>("TELUSU"));
        clmnNombreUsuario.setCellValueFactory(new PropertyValueFactory<>("NOMUSUSU"));
        clmnPassword.setCellValueFactory(new PropertyValueFactory<>("PASUSU"));

        tblUs.setItems(null);
        tblUs.setItems(listausuarios);
    }

    private void idmostrar() {
        try {
            conexion.conDB();
            listausuarios = FXCollections.observableArrayList();
            //Execute query
            ResultSet rs = conexion.conDB().createStatement().executeQuery("SELECT * FROM LECTOR");
            while (rs.next()) {
                //get
                listausuarios.add(new usuarios(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6)));
            }
        } catch (SQLException ex) {
            System.out.println("ERROR: " + ex);
        }
        //cell set value factory to tableview

        clmnNombre.setCellValueFactory(new PropertyValueFactory<>("NOMUSU"));
        clmnApellido.setCellValueFactory(new PropertyValueFactory<>("APEUSU"));
        clmnTelefono.setCellValueFactory(new PropertyValueFactory<>("TELUSU"));
        clmnNombreUsuario.setCellValueFactory(new PropertyValueFactory<>("NOMUSUSU"));
        clmnPassword.setCellValueFactory(new PropertyValueFactory<>("PASUSU"));

        tblUs.setItems(null);
        tblUs.setItems(listausuarios);
    }
    
    private boolean validarEntradadeDatos(){
        String errorMessage = "";
        if(txtNombresUsu.getText() == null || txtNombresUsu.getText().length() == 0){
            errorMessage += "Nombre invalido\n";
        }
        if(txtApeUsu.getText() == null || txtApeUsu.getText().length() == 0 ){
            errorMessage += "Apellido inavlido\n";
        }
        if(txtTelUsu.getText() == null || txtTelUsu.getText().length() == 0 ){
            errorMessage += "Teléfono invalido\n";
        }
        if(txtuserUsu.getText() == null || txtuserUsu.getText().length() == 0 ){
            errorMessage += "Nombre de usuario inválido\n";
        }
        if(txtcontraUsu.getText() == null || txtcontraUsu.getText().length() == 0 ){
            errorMessage += "Contraseña inválida\n";
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
    

}
