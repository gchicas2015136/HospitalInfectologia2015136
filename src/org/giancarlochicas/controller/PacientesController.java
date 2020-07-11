
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javax.swing.JOptionPane;
import org.giancarlochicas.bean.Paciente;
import org.giancarlochicas.db.Conexion;
import org.giancarlochicas.sistema.Principal;
import eu.schudt.javafx.controls.calendar.DatePicker;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import javafx.scene.layout.GridPane;
import org.giancarlochicas.report.GenerarReporte;

public class PacientesController implements Initializable{
    private enum operaciones{NUEVO, GUARDAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO};
    private Principal escenarioPrincipal;
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList<Paciente> ListaPaciente;
    private DatePicker fecha;
    
    @FXML TextField txtDPI;
    @FXML TextField txtApellidos;
    @FXML TextField txtNombres;
    @FXML GridPane grpFecha;
    @FXML TextField txtSexo;
    @FXML TextField txtDireccion;
    @FXML TextField txtOcupacion;
    @FXML TextField txtEdad;
    @FXML TableView tblPacientes;
    @FXML TableColumn colCodigoPaciente;
    @FXML TableColumn colDPI;
    @FXML TableColumn colApellidos;
    @FXML TableColumn colNombres;
    @FXML TableColumn colFechaNacimiento;
    @FXML TableColumn colSexo;
    @FXML TableColumn colDireccion;
    @FXML TableColumn colEdad;
    @FXML TableColumn colOcupacion;
    @FXML Button btnNuevo;
    @FXML Button btnEliminar;
    @FXML Button btnEditar;
    @FXML Button btnReporte;
    
   @Override
    public void initialize(URL location, ResourceBundle resources) {
       cargarDatos();
       fecha = new DatePicker(Locale.ENGLISH);
       fecha.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
       fecha.getCalendarView().todayButtonTextProperty().set("Today");
       fecha.getCalendarView().setShowWeeks(false);
       fecha.getStylesheets().add("/org/giancarlochicas/resource/DatePicker.css");
       grpFecha.add(fecha, 0, 0);
       
    }
public void cargarDatos(){
        tblPacientes.setItems(getPacientes());
        colCodigoPaciente.setCellValueFactory(new PropertyValueFactory<Paciente, Integer>("codigoPaciente"));
        colDPI.setCellValueFactory(new PropertyValueFactory<Paciente, String>("DPI"));
        colApellidos.setCellValueFactory(new PropertyValueFactory<Paciente, String>("apellidos"));
        colNombres.setCellValueFactory(new PropertyValueFactory<Paciente, String>("nombres"));
        colFechaNacimiento.setCellValueFactory(new PropertyValueFactory<Paciente, Date>("fechaNacimiento"));
        colEdad.setCellValueFactory(new PropertyValueFactory<Paciente, Integer>("edad"));
        colDireccion.setCellValueFactory(new PropertyValueFactory<Paciente, String>("direccion"));
        colOcupacion.setCellValueFactory(new PropertyValueFactory<Paciente, String>("ocupacion"));
        colSexo.setCellValueFactory(new PropertyValueFactory<Paciente, String>("sexo"));
    }

public ObservableList <Paciente> getPacientes(){
    ArrayList<Paciente> Lista = new ArrayList<Paciente>();
    try{
        PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ListarPacientes}");
        ResultSet resultado = procedimiento.executeQuery();
        while(resultado.next()){
            Lista.add(new Paciente(resultado.getInt("codigoPaciente"),
                            resultado.getString("DPI"),
                            resultado.getString("apellidos"),
                            resultado.getString("nombres"),
                            resultado.getDate("fechaNacimiento"),
                            resultado.getInt("edad"),
                            resultado.getString("direccion"),
                            resultado.getString("ocupacion"),
                            resultado.getString("sexo")));
        }
    }catch(Exception e){
    e.printStackTrace();
    }    
        
        return ListaPaciente = FXCollections.observableList(Lista);
    }

public void buscarPaciente(int codigoPaciente){
        Paciente resultado = null;
        try {
        PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_BuscarPaciente(?)}");
        procedimiento.setInt(1, codigoPaciente);
        ResultSet registro = procedimiento.executeQuery();
        while(registro.next()){
            resultado = new Paciente(registro.getInt("codigoPaciente"),
                                  registro.getString("DPI"),
                                  registro.getString("apellidos"),
                                  registro.getString("nombres"),
                                  registro.getDate("fechaNacimiento"),
                                  registro.getInt("Edad"),
                                  registro.getString("direccion"),
                                  registro.getString("ocupacion"),
                                  registro.getString("sexo"));
        } 
        }catch(Exception e){
            e.printStackTrace();
        }
    }

public void editar(){
        switch(tipoDeOperacion){
            case NINGUNO:
                if(tblPacientes.getSelectionModel().getSelectedItem() != null){
                    btnEditar.setText("Actualizar");
                    btnReporte.setText("Cancelar");
                    btnNuevo.setDisable(true);
                    btnEliminar.setDisable(true);
                    activarControles();
                    tipoDeOperacion = operaciones.ACTUALIZAR;
                    
                    
                } else{
                    JOptionPane.showMessageDialog(null, "Seleccione un Elemento Porfavor");
                }break;
                
            case ACTUALIZAR:
                actualizar();
                btnEditar.setText("Editar");
                btnReporte.setText("Reporte");
                tipoDeOperacion = operaciones.NINGUNO;
                btnNuevo.setDisable(false);
                btnEliminar.setDisable(false);
                cargarDatos();
                limpiarControles();
                desactivarControles();
                break;
        }
    }

    public void actualizar(){
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EditarPaciente(?,?,?,?,?,?,?,?)}");
            Paciente registro = (Paciente)tblPacientes.getSelectionModel().getSelectedItem();
            registro.setDPI((txtDPI.getText()));
            registro.setApellidos(txtApellidos.getText());
            registro.setNombres(txtNombres.getText());
            registro.setFechaNacimiento(fecha.getSelectedDate());
            registro.setEdad(Integer.parseInt(txtEdad.getText()));
            registro.setSexo(txtSexo.getText());
            procedimiento.setInt(1, registro.getCodigoPaciente());
            procedimiento.setString(2, registro.getDPI());
            procedimiento.setString(3, registro.getApellidos());            
            procedimiento.setString(4, registro.getNombres());
            procedimiento.setDate(5, new java.sql.Date(registro.getFechaNacimiento().getTime()));
            procedimiento.setString(6, registro.getDireccion());
            procedimiento.setString(7, registro.getOcupacion());
            procedimiento.setString(8, registro.getSexo());
            procedimiento.execute();
        }catch(Exception e){
            e.printStackTrace();
        }
        
    }

public void generarReporte(){
    switch(tipoDeOperacion){
        case NINGUNO:
            imprimirReporte();
            limpiarControles();
            tipoDeOperacion = operaciones.NINGUNO;
            break;
        case ACTUALIZAR:
            limpiarControles();
            cancelar();
    }
}

public void imprimirReporte(){
    Map parametros = new HashMap();
    parametros.put("codigoPaciente", null);
    GenerarReporte.mostrarReporte("reportePacientes.jasper", "Reporte de Pacientes", parametros);
}


public void seleccionarElemento(){
        if(tblPacientes.getSelectionModel().getSelectedItem() != null){     
        txtDPI.setText(((Paciente)(tblPacientes.getSelectionModel().getSelectedItem())).getDPI());
        txtApellidos.setText(((Paciente)(tblPacientes.getSelectionModel().getSelectedItem())).getApellidos());
        txtNombres.setText(((Paciente)(tblPacientes.getSelectionModel().getSelectedItem())).getNombres());
        fecha.selectedDateProperty().set(((Paciente)tblPacientes.getSelectionModel().getSelectedItem()).getFechaNacimiento());
        txtSexo.setText(((Paciente)(tblPacientes.getSelectionModel().getSelectedItem())).getSexo());
        txtDireccion.setText(((Paciente)(tblPacientes.getSelectionModel().getSelectedItem())).getDireccion());
        txtOcupacion.setText(((Paciente)(tblPacientes.getSelectionModel().getSelectedItem())).getOcupacion());
        txtEdad.setText(String.valueOf(((Paciente)(tblPacientes.getSelectionModel().getSelectedItem())).getEdad()));
        }else{
            JOptionPane.showMessageDialog(null, "Seleccione a un Paciente Porfavor");
            tblPacientes.getSelectionModel().clearSelection();
        }
    }

public void eliminar(){
        switch(tipoDeOperacion){
            case GUARDAR:
                desactivarControles();
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                limpiarControles();
                tipoDeOperacion = operaciones.NINGUNO;
                
                break;
                
            default:
                if(tblPacientes.getSelectionModel().getSelectedItem() != null){
                    int respuesta = JOptionPane.showConfirmDialog(null, "¿Estás seguro de eliminar este Registro?", "Eliminar Paciente", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if(respuesta == JOptionPane.YES_OPTION){
                        try{
                            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EliminarPaciente(?)}");
                            procedimiento.setInt(1, ((Paciente)tblPacientes.getSelectionModel().getSelectedItem()).getCodigoPaciente());
                            procedimiento.execute();
                            ListaPaciente.remove(tblPacientes.getSelectionModel().getSelectedIndex());
                            limpiarControles();
                        }catch(Exception e){
                        e.printStackTrace();
                        }
                    }
                } else{
                JOptionPane.showMessageDialog(null, "Debe de seleccionar un paciente primero.");
                }
        cancelar();
        tblPacientes.getSelectionModel().select(null);
        }
        
        
    }

 public void nuevo(){
        switch(tipoDeOperacion){
            case NINGUNO:
                activarControles();
                btnNuevo.setText("Guardar");
                btnEliminar.setText("Cancelar");
                btnEditar.setDisable(true);
                btnReporte.setDisable(true);
                tipoDeOperacion = operaciones.GUARDAR;
                limpiarControles();
            break;
            
            case GUARDAR:
                guardar();
                desactivarControles();
                limpiarControles();
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                tipoDeOperacion = operaciones.NINGUNO;
                cargarDatos();
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
    
    public void guardar(){
        if(txtDPI.getText().length() == 0 || txtNombres.getText().length() == 0 || fecha.getSelectedDate() == null || txtDireccion.getText().length() == 0 || txtOcupacion.getText().length() == 0 || txtSexo.getText().length() == 0){
        JOptionPane.showMessageDialog(null, "Llena todos los campos Porfavor!");
        }else{
        
        Paciente registro = new Paciente();
        registro.setDPI((txtDPI.getText()));
        registro.setNombres(txtApellidos.getText());
        registro.setApellidos(txtNombres.getText());
        registro.setFechaNacimiento(fecha.getSelectedDate());
        registro.setDireccion(txtDireccion.getText());
        registro.setOcupacion(txtOcupacion.getText());
        registro.setSexo(txtSexo.getText());
        
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call SP_AgregarPaciente(?, ?, ?, ?, ?, ?, ?)}");
            procedimiento.setString(1, registro.getDPI());
            procedimiento.setString(2, registro.getApellidos());
            procedimiento.setString(3, registro.getNombres());
            procedimiento.setDate(4, new java.sql.Date(registro.getFechaNacimiento().getTime()));
            procedimiento.setString(5, registro.getDireccion());
            procedimiento.setString(6, registro.getOcupacion());
            procedimiento.setString(7, registro.getSexo());
            procedimiento.execute();
            ListaPaciente.add(registro);   
        }catch(Exception e){
        e.printStackTrace(); 
            }
        }
    }

    
    public void desactivarControles(){
        txtDPI.setEditable(false);
        txtApellidos.setEditable(false);
        txtNombres.setEditable(false);
        fecha.setDisable(true);
        txtDireccion.setEditable(false);
        txtOcupacion.setEditable(false);
        txtSexo.setEditable(false);
        
    }
    
    public void activarControles(){
        txtDPI.setEditable(true);
        txtNombres.setEditable(true);
        txtApellidos.setEditable(true);
        fecha.setDisable(false);
        txtDireccion.setEditable(true);
        txtOcupacion.setEditable(true);
        txtSexo.setEditable(true);
        
    }
    
    public void limpiarControles(){
        txtDPI.setText("");
        txtNombres.setText("");
        txtApellidos.setText("");
        fecha.setSelectedDate(null);
        txtDireccion.setText("");
        txtOcupacion.setText("");
        txtSexo.setText("");
        txtEdad.setText("");
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
    
    public void ventanaContactoUrgencia(){
        escenarioPrincipal.ventanaContactoUrgencia();
    }
}
