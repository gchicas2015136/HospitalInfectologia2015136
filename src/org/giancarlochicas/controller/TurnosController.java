
package org.giancarlochicas.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import org.giancarlochicas.sistema.Principal;

public class TurnosController implements Initializable{
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
    
    public void menuPrincipal(){
    escenarioPrincipal.menuPrincipal();
    }
    
}
