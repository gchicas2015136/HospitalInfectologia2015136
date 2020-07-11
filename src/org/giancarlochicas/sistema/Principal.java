
package org.giancarlochicas.sistema;

import java.io.InputStream;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.giancarlochicas.controller.AreasController;
import org.giancarlochicas.controller.CargosController;
import org.giancarlochicas.controller.ContactoUrgenciaController;
import org.giancarlochicas.controller.EspecialidadesController;
import org.giancarlochicas.controller.HorariosController;
import org.giancarlochicas.controller.MedicoController;
import org.giancarlochicas.controller.MedicoEspecialidadController;
import org.giancarlochicas.controller.MenuPrincipalController;
import org.giancarlochicas.controller.PacientesController;
import org.giancarlochicas.controller.ProgramadorController;
import org.giancarlochicas.controller.ResponsableTurnoController;
import org.giancarlochicas.controller.TelefonoMedicoController;
import org.giancarlochicas.controller.TurnosController;


public class Principal extends Application {
    private final String PAQUETE_VISTA = "/org/giancarlochicas/view/";
    private Stage escenarioPrincipal;
    private Scene escena;
    
    
    @Override
    public void start(Stage escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
        escenarioPrincipal.setTitle("Hospital de Infectologia");
        escenarioPrincipal.getIcons().add(new Image("/org/giancarlochicas/images/IconoHospital.png"));
        menuPrincipal();
        escenarioPrincipal.show();
    }
 
    
    public Initializable cambiarEscena(String fxml, int ancho, int alto) throws Exception{
        Initializable resultado = null;
        FXMLLoader cargadorFXML = new FXMLLoader();
        InputStream archivo = Principal.class.getResourceAsStream(PAQUETE_VISTA + fxml);
        cargadorFXML.setBuilderFactory(new JavaFXBuilderFactory());
        cargadorFXML.setLocation(Principal.class.getResource(PAQUETE_VISTA+fxml));
        escena = new Scene((AnchorPane)cargadorFXML.load(archivo), ancho, alto);
        escenarioPrincipal.setScene(escena);
        escenarioPrincipal.sizeToScene();
        resultado = (Initializable)cargadorFXML.getController();
        return resultado;
    }

    public static void main(String[] args) {
        launch(args);
    }
    
    public void menuPrincipal(){
        try{
        MenuPrincipalController menuPrincipal = (MenuPrincipalController)cambiarEscena("MenuPrincipalView.fxml",499,499);
        menuPrincipal.setEscenarioPrincipal(this);
        
        }catch(Exception e){
            e.printStackTrace();
        }
    
    }
    
     public void ventanaMedicos(){
       try{
           MedicoController medicoController = (MedicoController)cambiarEscena("MedicoView.fxml", 840, 661);
           medicoController.setEscenarioPrincipal(this);
       }catch(Exception e){
           e.printStackTrace();
       }
    
     }
     
        public void ventanaProgramador(){
       try{
           ProgramadorController programadorController = (ProgramadorController)cambiarEscena("ProgramadorView.fxml", 582, 367);
           programadorController.setEscenarioPrincipal(this);
       }catch(Exception e){
           e.printStackTrace();
       }
    
     }

    public void ventanaPacientes() {
           try{
           PacientesController pacientesController = (PacientesController)cambiarEscena("PacientesView.fxml", 967, 713);
           pacientesController.setEscenarioPrincipal(this);
       }catch(Exception e){
           e.printStackTrace();  
        }
    }
    
    public void ventanaTelefonoMedico(){
               try{
           TelefonoMedicoController telefonoMedicoController = (TelefonoMedicoController)cambiarEscena("TelefonoMedicoView.fxml", 576, 569);
           telefonoMedicoController.setEscenarioPrincipal(this);
       }catch(Exception e){
           e.printStackTrace();  
        }
    }
    
    
     public void ventanaCargos(){
               try{
           CargosController cargosController = (CargosController)cambiarEscena("CargosView.fxml", 600, 431);
           cargosController.setEscenarioPrincipal(this);
       }catch(Exception e){
           e.printStackTrace();  
        }
    }
  
      public void ventanaAreas(){
               try{
           AreasController areasController = (AreasController)cambiarEscena("AreasView.fxml", 600, 431);
           areasController.setEscenarioPrincipal(this);
       }catch(Exception e){
           e.printStackTrace();  
        }
    }
      
      public void ventanaEspecialidades(){
               try{
           EspecialidadesController especialidadesController = (EspecialidadesController)cambiarEscena("EspecialidadesView.fxml", 600, 504);
           especialidadesController.setEscenarioPrincipal(this);
       }catch(Exception e){
           e.printStackTrace();  
        }
    }
    
          public void ventanaContactoUrgencia(){
               try{
           ContactoUrgenciaController contactoUrgenciaController = (ContactoUrgenciaController)cambiarEscena("ContactoUrgenciaView.fxml", 691, 603);
           contactoUrgenciaController.setEscenarioPrincipal(this);
       }catch(Exception e){
           e.printStackTrace();  
        }
    }
          
    public void ventanaResponsableTurno(){
        try{
            ResponsableTurnoController responsableTurnoController = (ResponsableTurnoController) cambiarEscena("ResponsableTurnoView.fxml", 840,661);
            responsableTurnoController.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void ventanaHorarios(){
        try{
            HorariosController horarios = (HorariosController) cambiarEscena("HorariosView.fxml", 840, 661);
            horarios.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
        public void ventanaTurnos(){
        try{
            TurnosController turnos = (TurnosController) cambiarEscena("TurnosView.fxml", 840, 661);
            turnos.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
        
        public void ventanaMedicoEspecialidad(){
        try{
            MedicoEspecialidadController medicoEspecialidadController = (MedicoEspecialidadController) cambiarEscena("MedicoEspecialidadView.fxml", 840, 661);
            medicoEspecialidadController.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
