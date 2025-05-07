/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package modelo;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author arkano
 */
public class Usuario implements Serializable{
    private int idUsuario;
    private String usuario;
    private ArrayList<Byte> password = new ArrayList();
    private boolean admin;
    static private int sigUsuario = 0;

    public Usuario(String usuario, String password, boolean admin) {
        this.usuario = usuario;
        setPassword(password);
        this.admin = admin;
        idUsuario = sigUsuario++;
    }

    public void setSigUsuario(int num){
        sigUsuario = num;
    }
    
    public int getSigUsuario(){
        return sigUsuario;
    }
    
    public void setPassword(String pass){
        password.clear();
        for (int i = 0; i < pass.length(); i++){
            int c = pass.charAt(i);
            password.add((byte)encriptar(c));
        }
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }
    
    
    
    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public boolean compruebaPass(String prueba){
        ArrayList<Byte> pass = new ArrayList<>();
        for (int i = 0; i < prueba.length(); i++){
            int c = prueba.charAt(i);
            pass.add((byte)encriptar(c));
        }
        System.out.println(pass);
        System.out.println(password);
        if (pass.size() != password.size())
            return false;
        
        for (int i = 0; i < pass.size(); i++){
            if (!pass.get(i).equals(password.get(i)))
                return false;
        }
        return true;
    }
    
    
    private int encriptar(int b){
        return 255 - b;
    }
}
