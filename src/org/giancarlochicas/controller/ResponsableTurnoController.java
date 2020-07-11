
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
import org.giancarlochicas.bean.Area;
import org.giancarlochicas.bean.Cargo;
import org.giancarlochicas.bean.ResponsableTurno;
import org.giancarlochicas.db.Conexion;
import org.giancarlochicas.sistema.Principal;

public class ResponsableTurnoController implements Initializable{
    private Principal escenarioPrincipal;
    private enum operaciones {AGREGAR,GUARDAR, EDITAR, ELIMINAR, ACTUALIZAR, CANCELAR, REPORTAR, NINGUNO};
        private operaciones tipoDeOperacion = operaciones.NINGUNO;
        private ObservableList <ResponsableTurno> listarResponsableTurno;
        private ObservableList <Cargo> listarCargo;
        private ObservableList <Area> listarArea;
        
        

        @FXML private TextField txtNombres;
        @FXML private TextField txtApellidos;
        @FXML private TextField txtTelefono;
        @FXML private ComboBox cmbCodigoCargo;
        @FXML private ComboBox cmbCodigoArea;
        @FXML private TableView tblResponsablesTurnos;
        @FXML private TableColumn colCodigoResponsableTurno;
        @FXML private TableColumn colNombres;
        @FXML private TableColumn colApellidos;
        @FXML private TableColumn colTelefono;
        @FXML private TableColumn colCodigoArea;
        @FXML private TableColumn colCodigoCargo;
        @FXML private Button btnNuevo;
        @FXML private Button btnEditar;
        @FXML private Button btnEliminar;
        @FXML private Button btnReporte;
    

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        cmbCodigoArea.setItems(getAreas());
        cmbCodigoCargo.setItems(getCargos());
    }
    
    
    public void cargarDatos(){
        tblResponsablesTurnos.setItems(getResponsablesTurno());
        colCodigoResponsableTurno.setCellValueFactory(new PropertyValueFactory<ResponsableTurno, Integer> ("codigoResponsableTurno"));
        colNombres.setCellValueFactory(new PropertyValueFactory <ResponsableTurno, String> ("nombreResponsable"));
        colApellidos.setCellValueFactory(new PropertyValueFactory <ResponsableTurno, String> ("apellidosResponsable"));
        colTelefono.setCellValueFactory(new PropertyValueFactory <ResponsableTurno, String> ("telefonoPersonal"));
        colCodigoArea.setCellValueFactory(new PropertyValueFactory<ResponsableTurno, Integer> ("codigoArea"));
        colCodigoCargo.setCellValueFactory(new PropertyValueFactory<ResponsableTurno, Integer> ("codigoCargo"));
    }
    
    public ObservableList <ResponsableTurno> getResponsablesTurno(){
        ArrayList <ResponsableTurno> Lista = new ArrayList<ResponsableTurno>();
        try{
        PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ListarResponsableTurno}");
        ResultSet resultado = procedimiento.executeQuery();
        while(resultado.next()){
            Lista.add(new ResponsableTurno(resultado.getInt("codigoResponsableTurno")
                                    ,resultado.getString("nombreResponsable")
                                    ,resultado.getString("apellidosResponsable")
                                    ,resultado.getString("telefonoPersonal")
                                    ,resultado.getInt("codigoArea")
                                    ,resultado.getInt("codigoCargo")));
        }
    }catch(Exception e){
    e.printStackTrace();
    }    
        
        return listarResponsableTurno = FXCollections.observableList(Lista);
    }
    
    public ObservableList <Area> getAreas(){
    ArrayList<Area> Lista = new ArrayList<Area>();
    try{
        PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ListarAreas}");
        ResultSet resultado = procedimiento.executeQuery();
        while(resultado.next()){
            Lista.add(new Area(resultado.getInt("codigoArea"),
                            resultado.getString("nombreArea")));
        }
    }catch(Exception e){
    e.printStackTrace();
    }    
        
        return listarArea = FXCollections.observableList(Lista);
    }
    
    public ObservableList <Cargo> getCargos(){
    ArrayList<Cargo> Lista = new ArrayList<Cargo>();
    try{
        PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ListarCargos}");
        ResultSet resultado = procedimiento.executeQuery();
        while(resultado.next()){
            Lista.add(new Cargo(resultado.getInt("codigoCargo"),
                            resultado.getString("nombreCargo")));
        }
    }catch(Exception e){
    e.printStackTrace();
    }    
        
        return listarCargo = FXCollections.observableList(Lista);
    }
    
public void seleccionarElemento(){
    if(tblResponsablesTurnos.getSelectionModel().getSelectedItem() != null){
         cmbCodigoArea.getSelectionModel().select(buscarArea(((ResponsableTurno)tblResponsablesTurnos.getSelectionModel().getSelectedItem()).getCodigoArea()));
         cmbCodigoCargo.getSelectionModel().select(buscarCargo(((ResponsableTurno)tblResponsablesTurnos.getSelectionModel().getSelectedItem()).getCodigoCargo()));
         txtNombres.setText(((ResponsableTurno) tblResponsablesTurnos.getSelectionModel().getSelectedItem()).getNombreResponsable());
         txtApellidos.setText(((ResponsableTurno)tblResponsablesTurnos.getSelectionModel().getSelectedItem()).getApellidosResponsable());
         txtTelefono.setText(((ResponsableTurno)tblResponsablesTurnos.getSelectionModel().getSelectedItem()).getTelefonoPersonal());    
    }else{
        JOptionPane.showMessageDialog(null, "Selecciona un Responsable Porfavor!");
    }
    }

public Area buscarArea(int codArea){
    Area resultado = null;
    try{
    PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_BuscarArea(?)}");
    procedimiento.setInt(1, (codArea));
    ResultSet registro = procedimiento.executeQuery();
    while(registro.next()){
        resultado = new Area(registro.getInt("codigoArea"),
                             registro.getString("nombreArea"));
    }
    }catch (SQLException e){
    e.getMessage();
    e.printStackTrace();
    }
    
    return resultado;
}

public Cargo buscarCargo(int codCargo){
    Cargo resultado = null;
    try{
    PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_BuscarCargo(?)}");
    procedimiento.setInt(1, (codCargo));
    ResultSet registro = procedimiento.executeQuery();
    while(registro.next()){
        resultado = new Cargo(registro.getInt("codigoCargo"),
                             registro.getString("nombreCargo"));
    }
    }catch (SQLException e){
    e.getMessage();
    e.printStackTrace();
    }
    
    return resultado;
}
    
public ResponsableTurno buscarResponsableTurno(int codResponsableTurno){
            ResponsableTurno resultado = null;
            try{
                PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_BuscarResponsableTurno(?)}");
                procedimiento.setInt(1, (codResponsableTurno));
                ResultSet registro =  procedimiento.executeQuery();
                while(registro.next()){
                                resultado = new ResponsableTurno(registro.getInt("codigoResponsableTurno")
                                                       ,registro.getString("nombreResponsable")
                                                       ,registro.getString("apellidosResponsable")
                                                       ,registro.getString("telefonoPersonal")
                                                       ,registro.getInt("codigoArea")
                                                       ,registro.getInt("codigoCargo"));
            }
                
            }catch(SQLException e){
            e.getMessage();
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
                if(tblResponsablesTurnos.getSelectionModel().getSelectedItem() != null){
                    int respuesta = JOptionPane.showConfirmDialog(null, "¿Estás seguro de eliminar este Registro?", "Eliminar Telefono Responsable", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if(respuesta == JOptionPane.YES_OPTION){
                        try{
                            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EliminarResponsableTurno(?)}");
                            procedimiento.setInt(1, ((ResponsableTurno)tblResponsablesTurnos.getSelectionModel().getSelectedItem()).getCodigoResponsableTurno());
                            procedimiento.execute();
                            listarResponsableTurno.remove(tblResponsablesTurnos.getSelectionModel().getSelectedIndex());
                            limpiarControles();
                        }catch(Exception e){
                        e.printStackTrace();
                        }
                    }
                } else{
                JOptionPane.showMessageDialog(null, "Debe de seleccionar un elemento primero.");
                }
            cancelar();
            tblResponsablesTurnos.getSelectionModel().select(null);
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
                if(tblResponsablesTurnos.getSelectionModel().getSelectedItem() != null){
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
        PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EditarResponsableTurno(?, ?, ?, ?, ?, ?)}");
        ResponsableTurno registro = (ResponsableTurno)tblResponsablesTurnos.getSelectionModel().getSelectedItem();
        registro.setTelefonoPersonal(txtTelefono.getText());
        registro.setNombreResponsable(txtNombres.getText());
        registro.setApellidosResponsable(txtApellidos.getText());
        registro.setCodigoArea(((Area)cmbCodigoArea.getSelectionModel().getSelectedItem()).getCodigoArea());
        registro.setCodigoCargo(((Cargo)cmbCodigoCargo.getSelectionModel().getSelectedItem()).getCodigoCargo());
        procedimiento.setInt(1, registro.getCodigoResponsableTurno());
        procedimiento.setString(4, registro.getTelefonoPersonal());
        procedimiento.setString(2, registro.getNombreResponsable());
        procedimiento.setString(3, registro.getApellidosResponsable());
        procedimiento.setInt(5, registro.getCodigoArea());
        procedimiento.setInt(6, registro.getCodigoCargo());
        procedimiento.execute();
        }catch(Exception e){
            e.printStackTrace();
        }        
    }
    
public void guardar(){
        if(cmbCodigoArea.getSelectionModel().getSelectedItem() == null||cmbCodigoCargo.getSelectionModel().getSelectedItem() == null || txtTelefono.getText().length() == 0 || txtNombres.getText().length() == 0 || txtApellidos.getText().length() == 0){
            JOptionPane.showMessageDialog(null, "Primero debes de llenar todos los campos!");
        }else{
     ResponsableTurno registro = new ResponsableTurno();
        registro.setCodigoArea(((Area)(cmbCodigoArea.getSelectionModel().getSelectedItem())).getCodigoArea());
        registro.setCodigoCargo(((Cargo)(cmbCodigoCargo.getSelectionModel().getSelectedItem())).getCodigoCargo());
        registro.setTelefonoPersonal((txtTelefono.getText()));
        registro.setNombreResponsable(txtNombres.getText());
        registro.setApellidosResponsable(txtApellidos.getText());
        
        try{
           PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_AgregarResponsableTurno(?,?,?,?,?)}");
           procedimiento.setString(3, registro.getTelefonoPersonal());
           procedimiento.setString(1, registro.getNombreResponsable());
           procedimiento.setString(2, registro.getApellidosResponsable());
           procedimiento.setInt(4, registro.getCodigoArea());
           procedimiento.setInt(5, registro.getCodigoCargo());
           procedimiento.execute();
           listarResponsableTurno.add(registro);
        }catch(Exception e){
        e.printStackTrace();
            }
        }
    }
    
public void desactivarControles(){
        txtNombres.setEditable(false);
        txtApellidos.setEditable(false);
        txtTelefono.setEditable(false); 
        cmbCodigoArea.setDisable(true);
        cmbCodigoCargo.setDisable(true);
    }
    
    public void activarControles(){
        txtNombres.setEditable(true);
        txtApellidos.setEditable(true);
        txtTelefono.setEditable(true); 
        cmbCodigoArea.setDisable(false);
        cmbCodigoCargo.setDisable(false);
    }
    
    public void limpiarControles(){
        txtNombres.setText("");
        txtApellidos.setText("");
        txtTelefono.setText(""); 
        cmbCodigoArea.getSelectionModel().clearSelection();
        cmbCodigoCargo.getSelectionModel().clearSelection();
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
