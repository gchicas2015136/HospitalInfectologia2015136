
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
import org.giancarlochicas.bean.Area;
import org.giancarlochicas.db.Conexion;
import org.giancarlochicas.sistema.Principal;

public class AreasController implements Initializable{
private enum operaciones{NUEVO, GUARDAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO};
    private Principal escenarioPrincipal;
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList<Area> ListaArea;
    
    @FXML private TextField txtNombreArea;
    @FXML private TableView tblAreas;
    @FXML private TableColumn colCodigoArea;
    @FXML private TableColumn colNombreArea;
    @FXML private Button btnNuevo;
    @FXML private Button btnEliminar;
    @FXML private Button btnEditar;
    @FXML private Button btnReporte;
            
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
         cargarDatos();
    }
    
    public void cargarDatos(){
        tblAreas.setItems(getAreas());
        colCodigoArea.setCellValueFactory(new PropertyValueFactory<Area, Integer>("codigoArea"));
        colNombreArea.setCellValueFactory(new PropertyValueFactory<Area, String>("nombreArea"));
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
        
        return ListaArea = FXCollections.observableList(Lista);
        
    }
    
    public void seleccionarElemento(){
        if(tblAreas.getSelectionModel().getSelectedItem() != null){
        txtNombreArea.setText(String.valueOf(((Area)tblAreas.getSelectionModel().getSelectedItem()).getNombreArea()));
        }else
        JOptionPane.showMessageDialog(null, "Selecciona un Area Porfavor.");
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
                if(tblAreas.getSelectionModel().getSelectedItem() != null){
                    int respuesta = JOptionPane.showConfirmDialog(null, "¿Estás seguro de eliminar este Registro?", "Eliminar Area", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if(respuesta == JOptionPane.YES_OPTION){
                        try{
                            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EliminarMedico(?)}");
                            procedimiento.setInt(1, ((Area)tblAreas.getSelectionModel().getSelectedItem()).getCodigoArea());
                            procedimiento.execute();
                            ListaArea.remove(tblAreas.getSelectionModel().getSelectedIndex());
                            limpiarControles();
                        }catch(Exception e){
                        e.printStackTrace();
                        }
                    }
                } else{
                JOptionPane.showMessageDialog(null, "Debe de seleccionar un Area primero.");
                }
        cancelar();
        tblAreas.getSelectionModel().select(null);
        }
    }
 
 public void buscarArea(int codigoArea){
        Area resultado = null;
        try {
        PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_BuscarArea(?)}");
        procedimiento.setInt(1, codigoArea);
        ResultSet registro = procedimiento.executeQuery();
        while(registro.next()){
            resultado = new Area(registro.getInt("codigoArea"),
                                  registro.getString("nombreArea"));
        } 
        }catch(Exception e){
            e.printStackTrace();
        }
    }
 
 public void editar(){
        switch(tipoDeOperacion){
            case NINGUNO:
                if(tblAreas.getSelectionModel().getSelectedItem() != null){
                    btnEditar.setText("Actualizar");
                    btnReporte.setText("Cancelar");
                    btnNuevo.setDisable(true);
                    btnEliminar.setDisable(true);
                    activarControles();
                    tipoDeOperacion = operaciones.ACTUALIZAR;
                    
                    
                } else{
                    JOptionPane.showMessageDialog(null, "Seleccione un Area Porfavor");
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
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EditarArea(?,?)}");
            Area registro = (Area)tblAreas.getSelectionModel().getSelectedItem();
            registro.setNombreArea(txtNombreArea.getText());
            procedimiento.setInt(1, registro.getCodigoArea());
            procedimiento.setString(2, registro.getNombreArea());
            procedimiento.execute();
        }catch(Exception e){
            e.printStackTrace();
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
    
    public void guardar(){
        if(txtNombreArea.getText().length() == 0){
            JOptionPane.showMessageDialog(null, "Se dejaron textos en blanco");
        }else{
         Area registro = new Area();
        registro.setNombreArea(txtNombreArea.getText());
        
        try{
           PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_AgregarArea(?)}");
           procedimiento.setString(1, registro.getNombreArea());
           procedimiento.execute();
           ListaArea.add(registro); 
           }catch(Exception e){
        e.printStackTrace();
        }
    }
    }
        
    public void desactivarControles(){
        txtNombreArea.setEditable(false);
    }
    
    public void activarControles(){
        txtNombreArea.setEditable(true);
    }
    
    public void limpiarControles(){
        txtNombreArea.setText("");
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
