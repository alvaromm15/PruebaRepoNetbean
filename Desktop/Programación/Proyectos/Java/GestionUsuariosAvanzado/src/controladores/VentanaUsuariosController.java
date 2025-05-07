/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controladores;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import modelo.Usuario;

/**
 * FXML Controller class
 *
 * @author arkano
 */
public class VentanaUsuariosController implements Initializable {
    ObservableList<Usuario> listaUsuario = FXCollections.observableArrayList();
    FilteredList<Usuario> listaFiltrada;
    StringProperty nombreUsuario = new SimpleStringProperty();
    BooleanProperty administrador = new SimpleBooleanProperty();
    
    @FXML
    private TextField tfUsuario;
    @FXML
    private PasswordField pw1;
    @FXML
    private PasswordField pw2;
    @FXML
    private CheckBox cbAdmin;
    @FXML
    private CheckBox cbVerPw;
    @FXML
    private TextField tfPassword1;
    @FXML
    private TextField tfPassword2;
    @FXML
    private TableView<Usuario> tbUsuarios;
    @FXML
    private TableColumn<Usuario, Integer> clIdUsuario;
    @FXML
    private TableColumn<Usuario, String> clUsuario;
    @FXML
    private TableColumn<Usuario, Boolean> clAdmin;
    @FXML
    private TextField tfFiltoUsuario;
    @FXML
    private CheckBox cbFiltroAdmin;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Configurar mi TableView
        clIdUsuario.setCellValueFactory(new PropertyValueFactory<>("idUsuario"));
        clUsuario.setCellValueFactory(new PropertyValueFactory<>("usuario"));
        clAdmin.setCellValueFactory(new PropertyValueFactory<>("admin"));
        listaFiltrada = new FilteredList<>(listaUsuario);
        filtraUsuarios();
                
        tbUsuarios.setItems(listaFiltrada);
        cargarUsuarios();
        
        
        // Enlazando password con tu texto
        tfPassword1.textProperty().bindBidirectional(pw1.textProperty());
        tfPassword2.textProperty().bindBidirectional(pw2.textProperty());
        
        // Controlamos al visibilidad de la password
        tfPassword1.visibleProperty().bind(cbVerPw.selectedProperty());
        pw1.visibleProperty().bind(cbVerPw.selectedProperty().not());
        tfPassword2.visibleProperty().bind(cbVerPw.selectedProperty());
        pw2.visibleProperty().bind(cbVerPw.selectedProperty().not());
        
        // Limpiar los campos password al cambiar el usuario
//        tfUsuario.textProperty().addListener(new ChangeListener<>(){
//            @Override
//            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
//                pw1.clear();
//                pw2.setText("");
//            }
//  
//        });
        // Lo mismo con listener
        tfUsuario.textProperty().addListener(e -> {
            pw1.clear();
            pw2.setText("");
        });
        
        tbUsuarios.getSelectionModel().getSelectedCells().addListener(new ListChangeListener<TablePosition>() {
            @Override
            public void onChanged(ListChangeListener.Change<? extends TablePosition> c) {
                Usuario usuario = tbUsuarios.getSelectionModel().getSelectedItem();
                if (usuario != null){
                    tfUsuario.setText(usuario.getUsuario());
                    cbAdmin.setSelected(usuario.isAdmin());
                }
            }
        });
        
        // añado listeners para filtrar usuarios
        tfFiltoUsuario.textProperty().addListener((e -> {
            filtraUsuarios();
        }));
        
        cbFiltroAdmin.selectedProperty().addListener(e -> {
            filtraUsuarios();
        });
        
        // añado listeners para mis propiedades 
        nombreUsuario.addListener(e -> {
            filtraUsuarios();
        });
        
        administrador.addListener(e -> {
            filtraUsuarios();
        });
    }    

    @FXML
    private void btnAceptaClick(ActionEvent event) {
        String nombre = tfUsuario.getText(); 
        if (nombre.equals(""))
            return;
        
        if (!administrador.getValue()){
            if (!nombre.equals(nombreUsuario.getValue())){
                return;
            }
        }
        
        Iterator<Usuario> it = listaUsuario.iterator();
        boolean encontrado = false;
        while (it.hasNext() && !encontrado) {
            Usuario u = it.next();
            if (u.getUsuario().equals(nombre)) {
                if (!pw1.getText().equals("")) {
                    if (!pw1.getText().equals(pw2.getText())) {
                        mensajeError("Las contraseñas no coinciden.");
                        return;
                    } else {
                        System.out.println("cambio la password");
                        u.setPassword(pw1.getText());
                    }
                }
                encontrado = true;
                if (administrador.getValue()){
                    u.setAdmin(cbAdmin.isSelected());
                }
            }
        }
        if (!encontrado) {
            if (!pw1.getText().equals(pw2.getText())) {
                mensajeError("Las contraseñas no coinciden.");
                return;
            } else {
                listaUsuario.add(new Usuario(tfUsuario.getText(), pw1.getText(), 
                        cbAdmin.isSelected()));
            }
        }
        tbUsuarios.refresh();
        guardarUsuarios();
    }

    @FXML
    private void btnComprobarClick(ActionEvent event) {
        boolean existe = false;
        Iterator<Usuario> it = listaUsuario.iterator();
        while (it.hasNext() && !existe){
            Usuario u = it.next();
            if (u.getUsuario().equals(tfUsuario.getText())){
                existe = true;
                if (u.compruebaPass(pw1.getText())){
                    mensajeInfor("El usuario /password es correcto.");
                } else {
                    mensajeInfor("La password no es correcta.");
                }
            } 
        }
        if (!existe){
            mensajeInfor("El usuario no existe.");
        }
        
    }

    @FXML
    private void btnEliminarClick(ActionEvent event) {
        if (!tfUsuario.getText().equals(""))
            if (!tfUsuario.getText().equals("Admin")){
                eliminarUsuario( tfUsuario.getText());
            } else {
                mensajeError("No se puede eliminar el usuario Admin");
            }
    }
    
    private void filtraUsuarios(){
        listaFiltrada.setPredicate(new Predicate<Usuario>(){
            @Override
            public boolean test(Usuario t) {
                String nombre;
                if(!administrador.getValue()){
                    nombre = nombreUsuario.getValue();
                    return t.getUsuario().equals(nombre);
                } else {
                    nombre = tfFiltoUsuario.getText();
                    System.out.println("voy por el else");
                    if (nombre.equals(""))
                        if (cbFiltroAdmin.isSelected()){
                            return t.isAdmin();
                        } else{
                            System.out.println("Salgo por aqui");
                            return true;
                        }
                    else if (cbFiltroAdmin.isSelected()){
                        return t.getUsuario().toLowerCase().contains(nombre.toLowerCase()) && t.isAdmin();
                    } else {
                        return t.getUsuario().toLowerCase().contains(nombre.toLowerCase());
                    }
                }
            }
            
        });
    }
    
    private void eliminarUsuario(String nombre){
        if (!administrador.getValue()){
            return;
        }
        Iterator<Usuario> it = listaUsuario.iterator();
        boolean encontrado = false;
        while (it.hasNext() && !encontrado){
            Usuario usuario = it.next();
            if (usuario.getUsuario().equals(nombre)){
                if(mensajeConfir("Seguro que quiere eliminar al usuario " + nombre)){
                    it.remove();
                    encontrado = true;
                    mensajeInfor("Usuario eliminado");
                }
            }
        }
        if (!encontrado){
            mensajeError("No se ha encontrado al usuario " + nombre);
        }
        guardarUsuarios();
    }
    
    List cargarUsuarios(){
        ObjectInputStream fe = null;
        ArrayList<Usuario> aux = new ArrayList();
        try{
            fe = new ObjectInputStream(new FileInputStream(new File("src/datos/usuarios.dat"))); 
        } catch (FileNotFoundException e){
        } catch (IOException e){
            mensajeError("Error accediendo al disco, no se puede cagar la lista de usuarios");
            System.exit(1);
        }
        if (fe != null){
             
            try{
                aux = (ArrayList<Usuario>) fe.readObject();
                int maxId = 0;
                for (Usuario u: aux){
                    if (u.getIdUsuario()> maxId){
                        maxId = u.getIdUsuario();
                    }
                    u.setSigUsuario(maxId+1);
                    listaUsuario.add(u);
                    
                }    
            } catch (IOException e){
                mensajeError("Error leyendo el fichero de usuarios.");
                System.exit(2);
            } catch (ClassNotFoundException e){
                mensajeError("Error leyendo el fichero de usuarios.");
                System.exit(2);
            }
        } else {
            listaUsuario.add(new Usuario("Admin", "a", true));
            aux.add(new Usuario("Admin", "a", true));
            guardarUsuarios();
        }
        return aux;
    }
    
    private void guardarUsuarios(){
        ObjectOutputStream fs = null;
        
        try {
            fs = new ObjectOutputStream(new FileOutputStream(new File("src/datos/usuarios.dat")));
        } catch (IOException e){
            mensajeError("No puedo crear el archivo de usuarios.");
            System.exit(3);
        }
        ArrayList<Usuario> aux = new ArrayList<>();
        for (Usuario u: listaUsuario)
            aux.add(u);

        try{
            fs.writeObject(aux);
        } catch (IOException e){
            mensajeError("No puedo guardar el archivo de usuarios.");
            System.exit(4);
        }
    }

    
    private void mensajeInfor(String mensaje){
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle("Ventana de Información");
        alerta.setHeaderText(mensaje);
        alerta.setContentText("Aceptar para continuar...");
        alerta.showAndWait();
    }
    
    private void mensajeError(String mensaje){
        Alert alerta = new Alert(Alert.AlertType.ERROR);
        alerta.setTitle("Mensaje de Error!!!");
        alerta.setHeaderText(mensaje);
        alerta.setContentText("Aceptar para continuar...");
        alerta.showAndWait();
    }
    
    private boolean mensajeConfir(String mensaje){
        Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
        alerta.setTitle("Mensaje de confirmaciíon");
        alerta.setHeaderText(mensaje);
        alerta.setContentText("Aceptar para continuar...");
        Optional<ButtonType> opcion = alerta.showAndWait();
        return opcion.get() == ButtonType.OK;
    }

//    private void cerrarVentana(ActionEvent event) {
//        Stage ventana = (Stage) tfUsuario.getScene().getWindow();
//                
//        usuario =  (Usuario) ventana.getUserData();
//        System.out.println("Has iniciado sesión con el usuario: " + usuario.getUsuario());
//    }
    
    void setUsuario(Usuario u){
        nombreUsuario.set(u.getUsuario());
        administrador.set(u.isAdmin());
        System.out.println("He ejecutado set Usuario, el usuario es " + u.getUsuario()
                + " y es administrador?:" + administrador);
    }
    
}
