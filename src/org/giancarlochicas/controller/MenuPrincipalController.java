
package org.giancarlochicas.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import org.giancarlochicas.sistema.Principal;

public class MenuPrincipalController implements Initializable{
    private Principal escenarioPrincipal;
   
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
    }

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }

    public void ventanaMedicos(){
        escenarioPrincipal.ventanaMedicos();
    }
    
    public void ventanaProgramador(){
        escenarioPrincipal.ventanaProgramador();
    }
    
    public void ventanaPacientes(){
        escenarioPrincipal.ventanaPacientes();
    }
    
    public void ventanaCargos(){
        escenarioPrincipal.ventanaCargos();
    }
    
    public void ventanaAreas(){
        escenarioPrincipal.ventanaAreas();
    }
   
   public void ventanaEspecialidades(){
       escenarioPrincipal.ventanaEspecialidades();
   }
    
   public void ventanaResponsableTurno(){
       escenarioPrincipal.ventanaResponsableTurno();
   } 
    
   public void ventanaHorarios(){
       escenarioPrincipal.ventanaHorarios();
   }
   public void ventanaTurnos(){
       escenarioPrincipal.ventanaTurnos();
   }
   
   public void ventanaMedicoEspecialidad(){
       escenarioPrincipal.ventanaMedicoEspecialidad();
   }
}
    
    
    
    

