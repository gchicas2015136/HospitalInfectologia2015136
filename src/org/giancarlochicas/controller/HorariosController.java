
package org.giancarlochicas.controller;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javax.swing.JOptionPane;
import org.giancarlochicas.bean.Horario;
import org.giancarlochicas.db.Conexion;
import org.giancarlochicas.sistema.Principal;

public class HorariosController implements Initializable{
    private Principal escenarioPrincipal;
    private enum operaciones {NUEVO, GUARDAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO};
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList <Horario> ListaHorario;
    
    @FXML private TextField txtHorarioInicio;
    @FXML private TextField txtHorarioSalida;
    @FXML private CheckBox chkLunes;
    @FXML private CheckBox chkMartes;
    @FXML private CheckBox chkMiercoles;
    @FXML private CheckBox chkJueves;
    @FXML private CheckBox chkViernes;
    @FXML private TableView tblHorarios;
    @FXML private TableColumn colCodigoHorario;
    @FXML private TableColumn colHorarioSalida;
    @FXML private TableColumn colHorarioInicio;
    @FXML private TableColumn colLunes;
    @FXML private TableColumn colMartes;
    @FXML private TableColumn colMiercoles;
    @FXML private TableColumn colJueves;
    @FXML private TableColumn colViernes;
    @FXML private Button btnNuevo;
    @FXML private Button btnEliminar;
    @FXML private Button btnEditar;
    @FXML private Button btnReporte;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
    }
    
    public void cargarDatos(){
        tblHorarios.setItems(getHorarios());
        colCodigoHorario.setCellValueFactory(new PropertyValueFactory<Horario, Integer> ("codigoHorario"));
        colHorarioInicio.setCellValueFactory(new PropertyValueFactory<Horario, String> ("horarioInicio"));
        colHorarioSalida.setCellValueFactory(new PropertyValueFactory<Horario, String>("horarioSalida"));
        colLunes.setCellValueFactory(new PropertyValueFactory<Horario, Boolean> ("lunes"));
        colMartes.setCellValueFactory(new PropertyValueFactory <Horario, Boolean> ("martes"));
        colMiercoles.setCellValueFactory(new PropertyValueFactory <Horario, Boolean> ("miercoles"));
        colJueves.setCellValueFactory(new PropertyValueFactory <Horario, Boolean> ("jueves"));
        colViernes.setCellValueFactory(new PropertyValueFactory<Horario, Boolean> ("viernes"));
    }
    
    public ObservableList <Horario> getHorarios(){
        ArrayList<Horario> Lista = new ArrayList <Horario>();
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ListarHorarios()}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                Lista.add(new Horario(resultado.getInt("codigoHorario"),
                                      resultado.getString("horarioInicio"),
                                      resultado.getString("horarioSalida"),
                                      resultado.getBoolean("lunes"),
                                      resultado.getBoolean("martes"),
                                      resultado.getBoolean("miercoles"),
                                      resultado.getBoolean("jueves"),
                                      resultado.getBoolean("viernes")));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
            return ListaHorario = FXCollections.observableList(Lista);    
    }
    
    public void nuevo(){
        switch(tipoDeOperacion){
            case NINGUNO:
                limpiarControles();
                activarControles();
                btnNuevo.setText("Guardar");
                btnEliminar.setText("Cancelar");
                btnEditar.setDisable(true);
                btnReporte.setDisable(true);
                tipoDeOperacion = operaciones.GUARDAR;
                break;
            
            case GUARDAR:
                guardar();
                desactivarControles();
                limpiarControles();
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Cancelar");
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                tipoDeOperacion = operaciones.NINGUNO;
        }
    }
    
public void guardar(){
    if(txtHorarioInicio.getText().length() == 0 || txtHorarioSalida.getText().length() == 0){
        JOptionPane.showMessageDialog(null, "!Llena todos los horarios porfavor!");
    }else{
        Horario registro = new Horario();
            registro.setCodigoHorario(Integer.parseInt(txtHorarioInicio.getText()));
            registro.setHorarioInicio(txtHorarioInicio.getText());
            registro.setHorarioSalida(txtHorarioSalida.getText());
            registro.setLunes(chkLunes.isSelected());
            registro.setMartes(chkMartes.isSelected());
            registro.setMiercoles(chkMiercoles.isSelected());
            registro.setJueves(chkJueves.isSelected());
            registro.setViernes(chkViernes.isSelected());
            
            try{
                PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_AgregarHorario(?, ?, ?, ?, ?, ?, ?)}");
                procedimiento.setString(1, registro.getHorarioInicio());
                procedimiento.setString(2, registro.getHorarioSalida());
                procedimiento.setBoolean(3, registro.isLunes());
                procedimiento.setBoolean(4, registro.isMartes());
                procedimiento.setBoolean(5, registro.isMiercoles());
                procedimiento.setBoolean(6, registro.isJueves());
                procedimiento.setBoolean(7, registro.isViernes());
            }catch(Exception e){
                e.printStackTrace();
            }
    }
}
    
public void cancelar(){
   desactivarControles();
   limpiarControles();
   btnNuevo.setText("Nuevo");
   btnEliminar.setText("Eliminar");
   btnEditar.setText("Editar");
   btnReporte.setText("Reporte");
   btnEditar.setDisable(false);
   btnReporte.setDisable(false);
   btnNuevo.setDisable(false);
   btnEliminar.setDisable(false);
   tipoDeOperacion = operaciones.NINGUNO;      
            
    }
public void activarControles(){
    txtHorarioInicio.setEditable(true);
    txtHorarioSalida.setEditable(true);
    chkLunes.setDisable(false);
    chkMartes.setDisable(false);
    chkMiercoles.setDisable(false);
    chkJueves.setDisable(false);
    chkViernes.setDisable(false);
}

public void desactivarControles(){
    txtHorarioInicio.setEditable(false);
    txtHorarioSalida.setEditable(false);
    chkLunes.setDisable(true);
    chkMartes.setDisable(true);
    chkMiercoles.setDisable(true);
    chkJueves.setDisable(true);
    chkViernes.setDisable(true);
}

public void limpiarControles(){
    txtHorarioInicio.setText("");
    txtHorarioSalida.setText("");
    chkLunes.setSelected(false);
    chkMartes.setSelected(false);
    chkMiercoles.setSelected(false);
    chkJueves.setSelected(false);
    chkViernes.setSelected(false);
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
