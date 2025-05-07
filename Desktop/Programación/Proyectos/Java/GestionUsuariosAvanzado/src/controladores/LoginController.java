/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controladores;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import modelo.Usuario;

/**
 * FXML Controller class
 *
 * @author arkano
 */
public class LoginController implements Initializable {
    ArrayList<Usuario> listaUsuarios;
    @FXML
    private TextField tfUsuario;
    @FXML
    private PasswordField pfPassword;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        listaUsuarios = (ArrayList) new VentanaUsuariosController().cargarUsuarios();
        
        listaUsuarios.forEach(System.out::println);
    }    

    @FXML
    private void entrar(ActionEvent event) {
        Iterator<Usuario> it = listaUsuarios.iterator();
        
        while (it.hasNext()) {
            Usuario u = it.next();
            if (tfUsuario.getText().equals(u.getUsuario())) {
                if (u.compruebaPass(pfPassword.getText())) {
                    
                     
//                    // Método 1. No funciona si quiero que la info este disponible desde
//                    // el initialice de la nueva ventana.
//                    Parent root = FXMLLoader.load(getClass().getResource("/vistas/ventanUsuarios.fxml"));
//                    Stage ventaUsuarios = new Stage();
//                    ventaUsuarios.setUserData(u);
//                    Scene escena = new Scene(root);
//                    ventaUsuarios.setScene(escena);
//                    ventaUsuarios.setTitle("Gestión de Uuarios.");
//                    ventaUsuarios.show();

                    // Método 2. Creo previamente el contolador de la nueva ventana.
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/vistas/ventanaUsuarios.fxml"));
                    Parent root = null;
                    try {
                        root = loader.load();
                    } catch (IOException ex) {
                        Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    Scene escena = new Scene(root);
                    Stage ventaUsuarios = new Stage();
                    ventaUsuarios.setScene(escena);
                    ventaUsuarios.setTitle("Gestión de Uuarios.");
                    VentanaUsuariosController controlador = loader.getController();
                    ventaUsuarios.show();
                    controlador.setUsuario(u);
                    ((Stage) (tfUsuario.getScene().getWindow())).close();
                } else {
                    // mostrar mensaje de error;
                }
            } else {
                // mostrar mensaje de error; 
            }
        }
    }

}
