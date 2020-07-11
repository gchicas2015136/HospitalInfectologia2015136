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
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javax.swing.JOptionPane;
import org.giancarlochicas.bean.ContactoUrgencia;
import org.giancarlochicas.bean.Paciente;
import org.giancarlochicas.db.Conexion;
import org.giancarlochicas.sistema.Principal;

public class ContactoUrgenciaController implements Initializable{
private enum operaciones{NUEVO, GUARDAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO};
    private Principal escenarioPrincipal;
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList<ContactoUrgencia> ListaContactoUrgencia;
    private ObservableList<Paciente> ListaPaciente;   
    
    @FXML TextField txtNumeroContacto;
    @FXML TextField txtNombres;
    @FXML TextField txtApellidos;
    @FXML TableView tblContactosUrgencia;
    @FXML ComboBox  cmbCodigoPaciente;
    @FXML TableColumn colCodigoContacto;
    @FXML TableColumn colNumeroContacto;
    @FXML TableColumn colApellidos;
    @FXML TableColumn colNombres;
    @FXML TableColumn colCodigoPaciente;
    @FXML Button btnNuevo;
    @FXML Button btnEliminar;
    @FXML Button btnEditar;
    @FXML Button btnReporte;    
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
               cargarDatos();
        cmbCodigoPaciente.setItems(getPacientes());
    }
    public void cargarDatos(){
        tblContactosUrgencia.setItems(getContactosUrgencia());
        colCodigoContacto.setCellValueFactory(new PropertyValueFactory<ContactoUrgencia, Integer>("codigoContactoUrgencia"));
        colNombres.setCellValueFactory(new PropertyValueFactory<ContactoUrgencia, String>("nombres"));
        colApellidos.setCellValueFactory(new PropertyValueFactory<ContactoUrgencia, String>("apellidos"));
        colNumeroContacto.setCellValueFactory(new PropertyValueFactory<ContactoUrgencia, String>("numeroContacto"));
        colCodigoPaciente.setCellValueFactory(new PropertyValueFactory<ContactoUrgencia, Integer>("codigoPaciente"));
    }
    
     public ObservableList <ContactoUrgencia> getContactosUrgencia(){
    ArrayList<ContactoUrgencia> Lista = new ArrayList<ContactoUrgencia>();
    try{
        PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ListarContactosUrgencia}");
        ResultSet resultado = procedimiento.executeQuery();
        while(resultado.next()){
            Lista.add(new ContactoUrgencia(resultado.getInt("codigoContactoUrgencia"),
                                  resultado.getString("nombres"),
                                  resultado.getString("apellidos"),
                                  resultado.getString("numeroContacto"),
                                  resultado.getInt("codigoPaciente")));
        }
    }catch(Exception e){
    e.printStackTrace();
    }    
        
        return ListaContactoUrgencia = FXCollections.observableList(Lista);
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

     
public Paciente buscarPaciente(int codigoPaciente){
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
        
        return resultado;
    }

public void seleccionarElemento(){
        if(tblContactosUrgencia.getSelectionModel().getSelectedItem() != null){
        txtNumeroContacto.setText(((ContactoUrgencia)(tblContactosUrgencia.getSelectionModel().getSelectedItem())).getNumeroContacto());
        txtApellidos.setText(((ContactoUrgencia)(tblContactosUrgencia.getSelectionModel().getSelectedItem())).getApellidos());
        txtNombres.setText(((ContactoUrgencia)(tblContactosUrgencia.getSelectionModel().getSelectedItem())).getNombres());
        cmbCodigoPaciente.getSelectionModel().select(buscarPaciente(((ContactoUrgencia)tblContactosUrgencia.getSelectionModel().getSelectedItem()).getCodigoPaciente()));
        
        }else{
            JOptionPane.showMessageDialog(null, "Seleccione un Contacto porfavor.");        
        
        }
    }

public ContactoUrgencia buscarContactoUrgencia(int codigoContactoUrgencia){
        ContactoUrgencia resultado = null;
      try {
        PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_BuscarContactoUrgencia(?)}");
        procedimiento.setInt(1, codigoContactoUrgencia);
        ResultSet registro = procedimiento.executeQuery();
        while(registro.next()){
            resultado = new ContactoUrgencia(registro.getInt("codigoContactoUrgencia"),
                                  registro.getString("nombres"),  
                                  registro.getString("apellidos"),
                                  registro.getString("numeroContacto"),
                                  registro.getInt("codigoPaciente"));
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
                if(tblContactosUrgencia.getSelectionModel().getSelectedItem() != null){
                    int respuesta = JOptionPane.showConfirmDialog(null, "¿Estás seguro de eliminar este Registro?", "Eliminar Telefono Contacto Urgencia ", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if(respuesta == JOptionPane.YES_OPTION){
                        try{
                            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EliminarContactoUrgencia(?)}");
                            procedimiento.setInt(1, ((ContactoUrgencia)tblContactosUrgencia.getSelectionModel().getSelectedItem()).getCodigoContactoUrgencia());
                            procedimiento.execute();
                            ListaContactoUrgencia.remove(tblContactosUrgencia.getSelectionModel().getSelectedIndex());
                            limpiarControles();
                        }catch(Exception e){
                        e.printStackTrace();
                        }
                    }
                } else{
                JOptionPane.showMessageDialog(null, "Debe de seleccionar un Contacto primero.");
                }
            cancelar();
            tblContactosUrgencia.getSelectionModel().select(null);
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
 
 public void editar(){
        switch(tipoDeOperacion){
            case NINGUNO:
                if(tblContactosUrgencia.getSelectionModel().getSelectedItem() != null){
                    btnEditar.setText("Actualizar");
                    btnReporte.setText("Cancelar");
                    btnNuevo.setDisable(true);
                    btnEliminar.setDisable(true);
                    activarControles();
                    tipoDeOperacion = operaciones.ACTUALIZAR;
                    
                    
                } else{
                    JOptionPane.showMessageDialog(null, "Seleccione un Contacto Porfavor");
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
        PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EditarContactoUrgencia(?, ?, ?, ?)}");
        ContactoUrgencia registro = (ContactoUrgencia)tblContactosUrgencia.getSelectionModel().getSelectedItem();
        registro.setNumeroContacto(txtNumeroContacto.getText());
        registro.setApellidos(txtApellidos.getText());
        registro.setNombres(txtNombres.getText());
        registro.setCodigoPaciente(((Paciente)cmbCodigoPaciente.getSelectionModel().getSelectedItem()).getCodigoPaciente());
        procedimiento.setInt(1, registro.getCodigoContactoUrgencia());
        procedimiento.setString(2, registro.getApellidos());
        procedimiento.setString(3, registro.getNombres());
        procedimiento.setString(4, registro.getNumeroContacto());
        procedimiento.execute();
        }catch(Exception e){
            e.printStackTrace();
        }        
    }
    
    public void guardar(){
       if(cmbCodigoPaciente.getSelectionModel().getSelectedItem() == null || txtNombres.getText().length() == 0 || txtApellidos.getText().length() == 0 || txtNumeroContacto.getText().length() == 0){
           JOptionPane.showMessageDialog(null, "Debes de llenar primero todos los campos!");
       }else{
        
        
     ContactoUrgencia registro = new ContactoUrgencia();
        registro.setCodigoPaciente(((Paciente)(cmbCodigoPaciente.getSelectionModel().getSelectedItem())).getCodigoPaciente());
        registro.setNombres((txtNombres.getText()));
        registro.setApellidos(txtApellidos.getText());
        registro.setNumeroContacto(txtNumeroContacto.getText());
        try{
           PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_AgregarContactoUrgencia(?,?,?,?)}");
           procedimiento.setString(1, registro.getNombres());
           procedimiento.setString(2, registro.getApellidos());
           procedimiento.setString(3, registro.getNumeroContacto());
           procedimiento.setInt(4, registro.getCodigoPaciente());
           procedimiento.execute();
           ListaContactoUrgencia.add(registro);
        }catch(Exception e){
        e.printStackTrace();
           }
       }
    }
    
     public void desactivarControles(){
        txtNumeroContacto.setEditable(false);
        txtApellidos.setEditable(false);
        txtNombres.setEditable(false); 
        cmbCodigoPaciente.setDisable(true);
    }
    
    public void activarControles(){
        txtNumeroContacto.setEditable(true);
        txtApellidos.setEditable(true);
        txtNombres.setEditable(true); 
        cmbCodigoPaciente.setDisable(false);
    }
    
    public void limpiarControles(){
        txtNumeroContacto.setText("");
        txtApellidos.setText("");
        txtNombres.setText(""); 
        cmbCodigoPaciente.getSelectionModel().clearSelection();
    }
    
    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
    public void ventanaPacientes(){
        escenarioPrincipal.ventanaPacientes();
    }
}
