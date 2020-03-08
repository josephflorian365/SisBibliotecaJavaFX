package Controladores;

import Modelos.Autor;
import Modelos.Conexion;
import Modelos.Editorial;
import Modelos.Libros;
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
public class Vista_libroController implements Initializable {

    @FXML
    private ComboBox<Autor> cmbAutor;
    @FXML
    private ComboBox<Editorial> cmbEditorial;
    @FXML
    private Button btnguardar;
    @FXML
    private Button btneditar;
    @FXML
    private Button btneliminar;
    @FXML
    private Button btnactualizar;
    @FXML
    private TextField txtcodigo;
    @FXML
    private TableView<Libros> tblLibro;
    @FXML
    private TableColumn<Libros, String> clmntitulolibro;
    @FXML
    private TableColumn<Libros, Autor> clmnautorlibro;
    @FXML
    private TableColumn<Libros, Editorial> clmneditoriallibro;
    @FXML
    private TextField txttitulolibro;

    /**
     * Initializes the controller class.
     */
    private ObservableList<Libros> listaLibros;
    private ObservableList<Autor> listaAutor;
    private ObservableList<Editorial> listaEditorial;
    private ObservableList<Pais> listapais;

    private Conexion conexion;
    @FXML
    private Button btnrefrescar;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        conexion = new Conexion();
        conexion.conDB();

        //Inicializamos listas
        listaAutor = FXCollections.observableArrayList();
        listaEditorial = FXCollections.observableArrayList();
        listaLibros = FXCollections.observableArrayList();
        listapais = FXCollections.observableArrayList();

        //llenar listas
        Autor.llenarInformacionAutor(conexion.conDB(), listaAutor);
        Editorial.llenarInformacionEditorial(conexion.conDB(), listaEditorial);
        Libros.llenarInformacionLibros(conexion.conDB(), listaLibros);
        Pais.llenarInformacion(conexion.conDB(), listapais);

        //Enlazar listas con combobox y tableview
        cmbAutor.setItems(listaAutor);
        cmbEditorial.setItems(listaEditorial);
        tblLibro.setItems(listaLibros);

        //Enlazar columnas con atributos
        clmntitulolibro.setCellValueFactory(new PropertyValueFactory<Libros, String>("TITLIB"));
        clmnautorlibro.setCellValueFactory(new PropertyValueFactory<Libros, Autor>("AUTOR"));
        clmneditoriallibro.setCellValueFactory(new PropertyValueFactory<Libros, Editorial>("EDITORIAL"));
        gestionarEventos();
    }

    public void gestionarEventos() {
        tblLibro.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Libros>() {
            @Override
            public void changed(ObservableValue<? extends Libros> arg0,
                    Libros valorAnterior, Libros valorSeleccionado) {
                if (valorSeleccionado != null) {
                    txtcodigo.setText(String.valueOf(valorSeleccionado.getIDLIB()));
                    txttitulolibro.setText(valorSeleccionado.getTITLIB());
                    cmbAutor.setValue(valorSeleccionado.getAUTOR());
                    cmbEditorial.setValue(valorSeleccionado.getEDITORIAL());

                    btnguardar.setDisable(true);
                    btneliminar.setDisable(false);
                    btnactualizar.setDisable(false);
                }
            }
        });
    }

    @FXML
    public void guardarRegistro() {
        if(validarEntradadeDatos()){
        //Crear una nueva instancia del tipo Libro
        Libros l = new Libros(0,
                txttitulolibro.getText(),
                cmbEditorial.getSelectionModel().getSelectedItem(),
                cmbAutor.getSelectionModel().getSelectedItem());
        conexion.conDB();
        int resultado = l.guardarRegistro(conexion.conDB());

        if (resultado == 1) {
            listaLibros.add(l);
            refrescarDta();
            //JDK 8u40
            Alert mensaje = new Alert(AlertType.INFORMATION);
            mensaje.setTitle("Registro agregado");
            mensaje.setContentText("El registro ha sido agregado exitosamente");
            mensaje.setHeaderText("Resultado: ");
            mensaje.show();
        }
        }
    }

    @FXML
    public void actualizarRegistro() {
        Libros l = new Libros(Integer.valueOf(txtcodigo.getText()),
                txttitulolibro.getText(),
                cmbEditorial.getSelectionModel().getSelectedItem(),
                cmbAutor.getSelectionModel().getSelectedItem());
        conexion.conDB();
        int resultado = l.actualizarRegistro(conexion.conDB());

        if (resultado == 1) {
            listaLibros.set(tblLibro.getSelectionModel().getSelectedIndex(), l);
            //JDK 8u40
            Alert mensaje = new Alert(AlertType.INFORMATION);
            mensaje.setTitle("Registro agregado");
            mensaje.setContentText("El registro ha sido agregado exitosamente");
            mensaje.setHeaderText("Resultado:");
            mensaje.show();
        }
    }

    @FXML
    public void eliminarRegistro() {
        conexion.conDB();
        int resultado = tblLibro.getSelectionModel().getSelectedItem().eliminarRegistro(conexion.conDB());

        if (resultado == 1) {
            listaLibros.remove(tblLibro.getSelectionModel().getSelectedIndex());
            //JDH 8u40
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
        txttitulolibro.setText(null);
        cmbAutor.setValue(null);
        cmbEditorial.setValue(null);

        btnguardar.setDisable(false);
        btneliminar.setDisable(true);
        btnactualizar.setDisable(true);
    }

    public void refrescarDta() {
        try {
            conexion.conDB();
            listaLibros = FXCollections.observableArrayList();
            listaAutor = FXCollections.observableArrayList();
            listaEditorial = FXCollections.observableArrayList();
            listapais = FXCollections.observableArrayList();

            //Execute query
            ResultSet rs = conexion.conDB().createStatement().executeQuery("SELECT L.IDLIB, L.TITLIB, "
                    + "E.IDEDI, E.NOMEDI, "
                    + "A.IDAUT, A.NOMAUT, A.APEAUT, "
                    + "P.IDPAIS, P.NOMPAIS "
                    + "FROM LIBRO L, EDITORIAL E, AUTOR A, PAIS P "
                    + "WHERE L.IDEDI = E.IDEDI "
                    + "AND L.IDAUT = A.IDAUT "
                    + "AND A.IDPAIS = P.IDPAIS");
            while (rs.next()) {
                //get
                listaLibros.add(new Libros(rs.getInt("IDLIB"), rs.getString("TITLIB"),
                        new Editorial(rs.getInt("IDEDI"), rs.getString("NOMEDI")),
                        new Autor(rs.getInt("IDAUT"), rs.getString("NOMAUT"), rs.getString("APEAUT"),
                                new Pais(rs.getInt("IDPAIS"), rs.getString("NOMPAIS")))));
            }
        } catch (SQLException ex) {
            System.out.println("ERROR: " + ex);
        }
        //cell set value factory to tableview

        clmnautorlibro.setCellValueFactory(new PropertyValueFactory<Libros, Autor>("AUTOR"));
        clmneditoriallibro.setCellValueFactory(new PropertyValueFactory<Libros, Editorial>("EDITORIAL"));
        clmntitulolibro.setCellValueFactory(new PropertyValueFactory<Libros, String>("TITLIB"));

        tblLibro.setItems(null);
        tblLibro.setItems(listaLibros);
    }

    @FXML
    public void orderBy() {
        try {
            conexion.conDB();
            listaLibros = FXCollections.observableArrayList();
            listaAutor = FXCollections.observableArrayList();
            listaEditorial = FXCollections.observableArrayList();
            listapais = FXCollections.observableArrayList();

            //Execute query
            ResultSet rs = conexion.conDB().createStatement().executeQuery("SELECT L.IDLIB, L.TITLIB, "
                    + "E.IDEDI, E.NOMEDI, "
                    + "A.IDAUT, A.NOMAUT, A.APEAUT, "
                    + "P.IDPAIS, P.NOMPAIS "
                    + "FROM LIBRO L, EDITORIAL E, AUTOR A, PAIS P "
                    + "WHERE L.IDEDI = E.IDEDI "
                    + "AND L.IDAUT = A.IDAUT "
                    + "AND A.IDPAIS = P.IDPAIS ORDER BY L.TITLIB");
            while (rs.next()) {
                //get
                listaLibros.add(new Libros(rs.getInt("IDLIB"), rs.getString("TITLIB"),
                        new Editorial(rs.getInt("IDEDI"), rs.getString("NOMEDI")),
                        new Autor(rs.getInt("IDAUT"), rs.getString("NOMAUT"), rs.getString("APEAUT"),
                                new Pais(rs.getInt("IDPAIS"), rs.getString("NOMPAIS")))));
            }
        } catch (SQLException ex) {
            System.out.println("ERROR: " + ex);
        }
        //cell set value factory to tableview

        clmnautorlibro.setCellValueFactory(new PropertyValueFactory<Libros, Autor>("AUTOR"));
        clmneditoriallibro.setCellValueFactory(new PropertyValueFactory<Libros, Editorial>("EDITORIAL"));
        clmntitulolibro.setCellValueFactory(new PropertyValueFactory<Libros, String>("TITLIB"));

        tblLibro.setItems(null);
        tblLibro.setItems(listaLibros);
    }

    private boolean validarEntradadeDatos() {
        String errorMessage = "";
        if (txttitulolibro.getText() == null || txttitulolibro.getText().length() == 0) {
            errorMessage += "Título inválido\n";
        }
        if (cmbAutor.getSelectionModel().getSelectedItem() == null) {
            errorMessage += "Item no válido\n";
        }
        if (cmbEditorial.getSelectionModel().getSelectedItem() == null) {
            errorMessage += "Item no válido\n";
        }
        if (errorMessage.length() == 0) {
            return true;
        } else {
            //mostrando el mensaje de error
            Alert mensaje = new Alert(Alert.AlertType.ERROR);
            mensaje.setTitle("Registro no válido");
            mensaje.setHeaderText("Campos invalidos por favor corrija...");
            mensaje.setContentText(errorMessage);
            mensaje.show();
            return false;
        }
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
}
