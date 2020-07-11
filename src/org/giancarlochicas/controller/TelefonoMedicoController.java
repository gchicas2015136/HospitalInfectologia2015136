package org.giancarlochicas.controller;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javax.swing.JOptionPane;
import org.giancarlochicas.bean.Medico;
import org.giancarlochicas.bean.TelefonoMedico;
import org.giancarlochicas.db.Conexion;
import org.giancarlochicas.sistema.Principal;

public class TelefonoMedicoController implements Initializable{
 private enum operaciones{NUEVO, GUARDAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO};
    private Principal escenarioPrincipal;
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList<TelefonoMedico> ListaTelefonoMedico;
    private ObservableList<Medico> ListaMedico;
    
    
    @FXML TextField txtTelefonoTrabajo;
    @FXML TextField txtTelefonoPersonal;
    @FXML TableView tblTelefonosMedicos;
    @FXML ComboBox  cmbCodigoMedico;
    @FXML TableColumn colCodigoTelefonoMedico;
    @FXML TableColumn colCodigoMedico;
    @FXML TableColumn colTelefonoTrabajo;
    @FXML TableColumn colTelefonoPersonal;
    @FXML Button btnNuevo;
    @FXML Button btnEliminar;
    @FXML Button btnEditar;
    @FXML Button btnReporte;
    
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        cmbCodigoMedico.setItems(getMedicos());
    }
    
    public void cargarDatos(){
        tblTelefonosMedicos.setItems(getTelefonosMedicos());
        colCodigoTelefonoMedico.setCellValueFactory(new PropertyValueFactory<TelefonoMedico, Integer>("codigoTelefonoMedico"));
        colTelefonoPersonal.setCellValueFactory(new PropertyValueFactory<TelefonoMedico, String>("telefonoPersonal"));
        colTelefonoTrabajo.setCellValueFactory(new PropertyValueFactory<TelefonoMedico, String>("telefonoTrabajo"));
        colCodigoMedico.setCellValueFactory(new PropertyValueFactory<TelefonoMedico, Integer>("codigoMedico"));
    }
    
    public ObservableList <TelefonoMedico> getTelefonosMedicos(){
    ArrayList<TelefonoMedico> Lista = new ArrayList<TelefonoMedico>();
    try{
        PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ListarTelefonosMedicos}");
        ResultSet resultado = procedimiento.executeQuery();
        while(resultado.next()){
            Lista.add(new TelefonoMedico(resultado.getInt("codigoTelefonoMedico"),
                                  resultado.getString("telefonoPersonal"),
                                  resultado.getString("telefonoTrabajo"),
                                  resultado.getInt("codigoMedico")));
        }
    }catch(Exception e){
    e.printStackTrace();
    }    
        
        return ListaTelefonoMedico = FXCollections.observableList(Lista);
    }
    
    public ObservableList <Medico> getMedicos(){
    ArrayList<Medico> Lista = new ArrayList<Medico>();
    try{
        PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ListarMedicos}");
        ResultSet resultado = procedimiento.executeQuery();
        while(resultado.next()){
            Lista.add(new Medico(resultado.getInt("codigoMedico"),
                            resultado.getInt("licenciaMedica"),
                            resultado.getString("nombres"),
                            resultado.getString("apellidos"),
                            resultado.getString("horaEntrada"),
                            resultado.getString("horaSalida"),
                            resultado.getInt("turnoMaximo"),
                            resultado.getString("sexo")));
        }
    }catch(Exception e){
    e.printStackTrace();
    }    
        
        return ListaMedico = FXCollections.observableList(Lista);
    }
     
     public Medico buscarMedico(int codigoMedico){
        Medico resultado = null;
        try {
        PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_BuscarMedico(?)}");
        procedimiento.setInt(1, codigoMedico);
        ResultSet registro = procedimiento.executeQuery();
        while(registro.next()){
            resultado = new Medico(registro.getInt("codigOMedico"),
                                  registro.getInt("licenciaMedica"),
                                  registro.getString("nombres"),
                                  registro.getString("apellidos"),
                                  registro.getString("horaEntrada"),
                                  registro.getString("horaSalida"),
                                  registro.getInt("turnoMaximo"),
                                  registro.getString("sexo"));
        } 
        }catch(Exception e){
            e.printStackTrace();
        }
        return resultado;
    }
    
    public void seleccionarElemento(){
        if(tblTelefonosMedicos.getSelectionModel().getSelectedItem() != null){
        txtTelefonoTrabajo.setText(((TelefonoMedico)(tblTelefonosMedicos.getSelectionModel().getSelectedItem())).getTelefonoTrabajo());
        txtTelefonoPersonal.setText(((TelefonoMedico)(tblTelefonosMedicos.getSelectionModel().getSelectedItem())).getTelefonoPersonal());
        cmbCodigoMedico.getSelectionModel().select(buscarMedico(((TelefonoMedico)tblTelefonosMedicos.getSelectionModel().getSelectedItem()).getCodigoMedico()));
        
        }else{
            JOptionPane.showMessageDialog(null, "Seleccione un telefono porfavor.");        
        
        }
    }
    
    public TelefonoMedico buscarTelefonoMedico(int codigoTelefonoMedico){
        TelefonoMedico resultado = null;
      try {
        PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_BuscarTelefonoMedico(?)}");
        procedimiento.setInt(1, codigoTelefonoMedico);
        ResultSet registro = procedimiento.executeQuery();
        while(registro.next()){
            resultado = new TelefonoMedico(registro.getInt("codigoTelefonoMedico"),
                                  registro.getString("telefonoPersonal"),
                                  registro.getString("telefonoTrabajo"),
                                  registro.getInt("codigoMedico"));
        } 
        }catch(Exception e){
            e.printStackTrace();
        }
      return resultado;
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
                if(tblTelefonosMedicos.getSelectionModel().getSelectedItem() != null){
                    int respuesta = JOptionPane.showConfirmDialog(null, "¿Estás seguro de eliminar este Registro?", "Eliminar Telefono Medico", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if(respuesta == JOptionPane.YES_OPTION){
                        try{
                            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EliminarTelefonoMedico(?)}");
                            procedimiento.setInt(1, ((TelefonoMedico)tblTelefonosMedicos.getSelectionModel().getSelectedItem()).getCodigoMedico());
                            procedimiento.execute();
                            ListaTelefonoMedico.remove(tblTelefonosMedicos.getSelectionModel().getSelectedIndex());
                            limpiarControles();
                        }catch(Exception e){
                        e.printStackTrace();
                        }
                    }
                } else{
                JOptionPane.showMessageDialog(null, "Debe de seleccionar un elemento primero.");
                }
            cancelar();
            tblTelefonosMedicos.getSelectionModel().select(null);
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
    
    public void reporte(){
        cancelar();
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
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                tipoDeOperacion = operaciones.NINGUNO;
                cargarDatos();
            break;    
        }
        
    }
    
    public void editar() throws SQLException{
        switch(tipoDeOperacion){
            case NINGUNO:
                if(tblTelefonosMedicos.getSelectionModel().getSelectedItem() != null){
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
    
    public void actualizar() throws SQLException{
        try{
        PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EditarTelefonoMedico(?, ?, ?, ?)}");
        TelefonoMedico registro = (TelefonoMedico)tblTelefonosMedicos.getSelectionModel().getSelectedItem();
        registro.setTelefonoPersonal(txtTelefonoPersonal.getText());
        registro.setTelefonoTrabajo(txtTelefonoTrabajo.getText());
        registro.setCodigoMedico(((Medico)cmbCodigoMedico.getSelectionModel().getSelectedItem()).getCodigoMedico());
        procedimiento.setInt(1, registro.getCodigoTelefonoMedico());
        procedimiento.setString(2, registro.getTelefonoPersonal());
        procedimiento.setString(3, registro.getTelefonoTrabajo());
        procedimiento.setInt(4, registro.getCodigoMedico());
        procedimiento.execute();
        }catch(Exception e){
            e.printStackTrace();
        }        
    }
    
    
    
    public void guardar(){
        if(cmbCodigoMedico.getSelectionModel().getSelectedItem() == null || txtTelefonoPersonal.getText().length() == 0 || txtTelefonoTrabajo.getText().length() == 0){
            JOptionPane.showMessageDialog(null, "Primero debes de llenar todos los campos!");
        }else{
     TelefonoMedico registro = new TelefonoMedico();
        registro.setCodigoMedico(((Medico)(cmbCodigoMedico.getSelectionModel().getSelectedItem())).getCodigoMedico());
        registro.setTelefonoPersonal((txtTelefonoPersonal.getText()));
        registro.setTelefonoTrabajo(txtTelefonoTrabajo.getText());
        try{
           PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_AgregarTelefonoMedico(?,?,?)}");
           procedimiento.setString(1, registro.getTelefonoPersonal());
           procedimiento.setString(2, registro.getTelefonoTrabajo());
           procedimiento.setInt(3, registro.getCodigoMedico());
           procedimiento.execute();
           ListaTelefonoMedico.add(registro);
        }catch(Exception e){
        e.printStackTrace();
            }
        }
    }
    
    public void desactivarControles(){
        txtTelefonoTrabajo.setEditable(false);
        txtTelefonoPersonal.setEditable(false); 
        cmbCodigoMedico.setDisable(true);
    }
    
    public void activarControles(){
        txtTelefonoTrabajo.setEditable(true);
        txtTelefonoPersonal.setEditable(true);
        cmbCodigoMedico.setDisable(false);
    }
    
    public void limpiarControles(){
        txtTelefonoTrabajo.setText("");
        txtTelefonoPersonal.setText("");
        cmbCodigoMedico.getSelectionModel().clearSelection();
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
}
