/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main_1.java to edit this template
 */

package gestionusuarios;

import controladores.LoginController;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/** Incluir esta línea en property/Run:
 *  --module-path /usr/lib/jvm/javafx-sdk-11/lib --add-modules javafx.controls,javafx.fxml
 *
 * @author arkano
 */
public class GestionUsuario extends Application {

    
   public static void main(String[] args) {
       System.out.println("cachalote");
       launch(args);
    }
    
    @Override
    public void start(Stage ventana) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/vistas/login.fxml"));

        Scene escena = new Scene(root,300,350);
        ventana.setTitle("Gestión de Usuarios");
        ventana.setScene(escena);
        ventana.show();   
    }
}
