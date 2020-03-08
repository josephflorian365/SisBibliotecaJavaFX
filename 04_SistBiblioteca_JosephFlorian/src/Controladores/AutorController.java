/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controladores;

import Modelos.Autor;
import Modelos.Conexion;
import Modelos.Pais;
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
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Js
 */
public class AutorController implements Initializable {

    //Columnas
    @FXML
    private TableColumn<Autor, String> clmnnomaut;
    @FXML
    private TableColumn<Autor, String> clmnapeaut;
    @FXML
    private TableColumn<Autor, Pais> clmnpaisaut;
    //Componentes GUI
    @FXML
    private TextField txtNomAut;
    @FXML
    private TextField txtApeAut;
    @FXML
    private Button btnEditarAut;
    @FXML
    private Button btnEliminarAut;
    @FXML
    private Button btnGuardarAut;
    @FXML
    private Button btnactualizaraut;
    @FXML
    private Button btnrefrescar;
    @FXML
    private ComboBox<Pais> cmbPaisAut;
    @FXML
    private TableView<Autor> tblAutor;
    @FXML
    private TextField txtcodigo;

    //colecciones
    private ObservableList<Pais> listaPais;
    private ObservableList<Autor> listautor;

    private Conexion conexion;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // TODO
        conexion = new Conexion();
        conexion.conDB();

        //Inicializamos listas
        listaPais = FXCollections.observableArrayList();
        listautor = FXCollections.observableArrayList();

        //Llenar listas
        Pais.llenarInformacion(conexion.conDB(), listaPais);
        Autor.llenarInformacionAutor(conexion.conDB(), listautor);

        //Enlazar listas con combobox y tableview
        cmbPaisAut.setItems(listaPais);
        tblAutor.setItems(listautor);

        //Enlazar columnas con atributos
        clmnnomaut.setCellValueFactory(new PropertyValueFactory<Autor, String>("NOMAUT"));
        clmnapeaut.setCellValueFactory(new PropertyValueFactory<Autor, String>("APEAUT"));
        clmnpaisaut.setCellValueFactory(new PropertyValueFactory<Autor, Pais>("pais"));
        gestionarEventos();

    }

    public void gestionarEventos() {
        tblAutor.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Autor>() {
            @Override
            public void changed(ObservableValue<? extends Autor> arg0,
                    Autor valorAnterior, Autor valorSeleccionado) {
                if (valorSeleccionado != null) {
                    txtcodigo.setText(String.valueOf(valorSeleccionado.getIDAUT()));
                    txtNomAut.setText(valorSeleccionado.getNOMAUT());
                    txtApeAut.setText(valorSeleccionado.getAPEAUT());
                    cmbPaisAut.setValue(valorSeleccionado.getPais());

                    btnGuardarAut.setDisable(true);
                    btnEliminarAut.setDisable(false);
                    btnactualizaraut.setDisable(false);
                }
            }
        });
    }

    @FXML
    public void guardarRegistro() {
        if(validarEntradadeDatos()){
        //Crear una nueva instancia del tipo Autor
        Autor a = new Autor(0,
                txtNomAut.getText(),
                txtApeAut.getText(),
                cmbPaisAut.getSelectionModel().getSelectedItem()
        );
        conexion.conDB();
        int resultado = a.guardarRegistro(conexion.conDB());

        if (resultado == 1) {
            listautor.add(a);
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
    public void actualizarRegistro() {
        Autor a = new Autor(
                Integer.valueOf(txtcodigo.getText()),
                txtNomAut.getText(),
                txtApeAut.getText(),
                cmbPaisAut.getSelectionModel().getSelectedItem()
        );
        conexion.conDB();
        int resultado = a.actualizarRegistro(conexion.conDB());

        if (resultado == 1) {
            listautor.set(tblAutor.getSelectionModel().getSelectedIndex(), a);
            //JDK 8u40
            Alert mensaje = new Alert(AlertType.INFORMATION);
            mensaje.setTitle("Registro actualizado");
            mensaje.setContentText("El registro ha sido actualizado exitosamente");
            mensaje.setHeaderText("Resultado: ");
            mensaje.show();
        }
    }

    @FXML
    public void eliminarRegistro() {
        conexion.conDB();
        int resultado = tblAutor.getSelectionModel().getSelectedItem().eliminarRegistro(conexion.conDB());

        if (resultado == 1) {
            listautor.remove(tblAutor.getSelectionModel().getSelectedIndex());
            //JDK 8u>40
            Alert mensaje = new Alert(AlertType.INFORMATION);
            mensaje.setTitle("Registro eliminado");
            mensaje.setContentText("El registro ha sido eliminado exitosamente");
            mensaje.setHeaderText("Resultado: ");
            mensaje.show();
        }
    }

    @FXML
    public void limpiarComponentes() {
        txtcodigo.setText(null);
        txtNomAut.setText(null);
        txtApeAut.setText(null);
        cmbPaisAut.setValue(null);

        btnGuardarAut.setDisable(false);
        btnEliminarAut.setDisable(true);
        btnactualizaraut.setDisable(true);
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

    public void refrescarData() {
        try {
            conexion.conDB();
            listaPais = FXCollections.observableArrayList();
            listautor = FXCollections.observableArrayList();

            //Execute query
            ResultSet rs = conexion.conDB().createStatement().executeQuery("SELECT A.IDAUT, "
                    + "A.NOMAUT, "
                    + "A.APEAUT, "
                    + "A.IDPAIS, "
                    + "P.NOMPAIS "
                    + "FROM AUTOR A "
                    + "INNER JOIN PAIS P "
                    + "ON (A.IDPAIS = P.IDPAIS) ");
            while (rs.next()) {
                //get
                listautor.add(new Autor(rs.getInt("IDAUT"), rs.getString("NOMAUT"), rs.getString("APEAUT"),
                        new Pais(rs.getInt("IDPAIS"), rs.getString("NOMPAIS"))));
            }
        } catch (SQLException ex) {
            System.out.println("ERROR: " + ex);
        }
        //cell set value factory to tableview

        clmnnomaut.setCellValueFactory(new PropertyValueFactory<Autor, String>("NOMAUT"));
        clmnapeaut.setCellValueFactory(new PropertyValueFactory<Autor, String>("APEAUT"));
        clmnpaisaut.setCellValueFactory(new PropertyValueFactory<Autor, Pais>("pais"));

        tblAutor.setItems(null);
        tblAutor.setItems(listautor);

    }
    
    private boolean validarEntradadeDatos(){
        String errorMessage = "";
        if(txtNomAut.getText() == null || txtNomAut.getText().length() == 0){
            errorMessage += "Nombre inválido\n";
        }
        if(txtApeAut.getText() == null || txtApeAut.getText().length() == 0){
            errorMessage += "Apellido inválido\n";
        }
        if(cmbPaisAut.getSelectionModel().getSelectedItem()== null){
            errorMessage += "Item inválido\n"; 
        } 
        if(errorMessage.length()==0){
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

    @FXML
    public void orderBy() {
        try {
            conexion.conDB();
            listaPais = FXCollections.observableArrayList();
            listautor = FXCollections.observableArrayList();

            //Execute query
            ResultSet rs = conexion.conDB().createStatement().executeQuery("SELECT A.IDAUT, "
                    + "A.NOMAUT, "
                    + "A.APEAUT, "
                    + "A.IDPAIS, "
                    + "P.NOMPAIS "
                    + "FROM AUTOR A "
                    + "INNER JOIN PAIS P "
                    + "ON (A.IDPAIS = P.IDPAIS)"
                    + " ORDER BY A.NOMAUT ");
            while (rs.next()) {
                //get
                listautor.add(new Autor(rs.getInt("IDAUT"), rs.getString("NOMAUT"), rs.getString("APEAUT"),
                        new Pais(rs.getInt("IDPAIS"), rs.getString("NOMPAIS"))));
            }
        } catch (SQLException ex) {
            System.out.println("ERROR: " + ex);
        }
        //cell set value factory to tableview

        clmnnomaut.setCellValueFactory(new PropertyValueFactory<Autor, String>("NOMAUT"));
        clmnapeaut.setCellValueFactory(new PropertyValueFactory<Autor, String>("APEAUT"));
        clmnpaisaut.setCellValueFactory(new PropertyValueFactory<Autor, Pais>("pais"));

        tblAutor.setItems(null);
        tblAutor.setItems(listautor);

    }
}
