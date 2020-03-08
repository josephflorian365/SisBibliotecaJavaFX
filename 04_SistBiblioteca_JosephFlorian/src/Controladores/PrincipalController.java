/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controladores;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Js
 */
public class PrincipalController implements Initializable {

    @FXML
    private Button btnusuarios;
    @FXML
    private Button btnlibros;
    @FXML
    private Button btnAutor;
    @FXML
    private Button btnprestamo;
    @FXML
    private Button btnEditor;
    @FXML
    private Button btndev;
    @FXML
    private AnchorPane pnlPrincipal;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    private void addusu(ActionEvent event) {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Vistas/Vista_usuario.fxml"));

            Parent root = loader.load();

            Vista_usuarioController controlador = loader.getController();

            Scene scene = new Scene(root);
            Stage stage = new Stage();

            stage.setScene(scene);
            new animatefx.animation.ZoomIn(root).play();
            stage.show();

            stage.setOnCloseRequest(e -> controlador.closeWindows());

            Stage myStage = (Stage) this.btnusuarios.getScene().getWindow();
            myStage.close();

        } catch (IOException ex) {
            Logger.getLogger(PrincipalController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @FXML
    private void addlibro(ActionEvent event) {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Vistas/Vista_libro.fxml"));

            Parent root = loader.load();

            Vista_libroController controlador = loader.getController();

            Scene scene = new Scene(root);
            Stage stage = new Stage();

            stage.setScene(scene);
            new animatefx.animation.ZoomIn(root).play();
            stage.show();

            stage.setOnCloseRequest(e -> controlador.closeWindows());

            Stage myStage = (Stage) this.btnlibros.getScene().getWindow();
            myStage.close();

        } catch (IOException ex) {
            Logger.getLogger(PrincipalController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void addAutor(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Vistas/Autor.fxml"));

            Parent root = loader.load();

            AutorController controlador = loader.getController();

            Scene scene = new Scene(root);
            Stage stage = new Stage();

            stage.setScene(scene);
            new animatefx.animation.ZoomIn(root).play();
            stage.show();
            stage.setOnCloseRequest(e -> controlador.closeWindows());

            Stage myStage = (Stage) this.btnAutor.getScene().getWindow();
            myStage.close();

        } catch (IOException ex) {
            Logger.getLogger(PrincipalController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void addprestamo(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Vistas/Prestamo.fxml"));

            Parent root = loader.load();

            PrestamoController controlador = loader.getController();

            Scene scene = new Scene(root);
            Stage stage = new Stage();

            stage.setScene(scene);
            new animatefx.animation.ZoomIn(root).play();
            stage.show();
            stage.setOnCloseRequest(e -> controlador.closeWindows());

            Stage myStage = (Stage) this.btnprestamo.getScene().getWindow();
            myStage.close();

        } catch (IOException ex) {
            Logger.getLogger(PrincipalController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void addEditor(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Vistas/Editorial.fxml"));

            Parent root = loader.load();

            EditorialController controlador = loader.getController();

            Scene scene = new Scene(root);
            Stage stage = new Stage();

            stage.setScene(scene);
            new animatefx.animation.ZoomIn(root).play();
            stage.show();
            stage.setOnCloseRequest(e -> controlador.closeWindows());

            Stage myStage = (Stage) this.btnprestamo.getScene().getWindow();
            myStage.close();

        } catch (IOException ex) {
            Logger.getLogger(PrincipalController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void addDev(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Vistas/Devoluciones.fxml"));

            Parent root = loader.load();

            DevolucionesController controlador = loader.getController();

            Scene scene = new Scene(root);
            Stage stage = new Stage();

            stage.setScene(scene);
            new animatefx.animation.ZoomIn(root).play();
            stage.show();
            stage.setOnCloseRequest(e -> controlador.closeWindows());

            Stage mysStage = (Stage) this.btndev.getScene().getWindow();
            mysStage.close();
        } catch (IOException ex) {
            Logger.getLogger(PrincipalController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
