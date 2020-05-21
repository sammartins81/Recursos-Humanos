/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Clases.Evaluacion;

import BD.BD;
import Clases.empleados.ListaMaestro;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 *
 * @author jluis
 */
public class BDEvaluacion {
    
    public static void insertarEvaluacion(ClassEvaluacion c) throws SQLException {
        
        Connection cnn = BD.getConnection();
        PreparedStatement p = null;
        p = cnn.prepareStatement("insert into BEVALUACION_DESEMPENO values(id_evaluacion.nextval,?,?,1,?,?)");
        p.setInt(1, c.getId_listaempleados());
        p.setDate(2, new java.sql.Date(c.getFecha().getTime()));
        p.setInt(3, c.getFace());
        p.setInt(4, c.getNoEvaluacion());
        p.executeUpdate();
        cnn.close();
        p.close();
    }
 
    
    
public static ClassEvaluacion buscarEmpleado(int id) throws SQLException {
        return buscarEmple(id, null);
    }    
 public static ClassEvaluacion buscarEmple(int id,ClassEvaluacion p) throws SQLException {
        Connection cnn = BD.getConnection();
        
        try {
            PreparedStatement ps = null;
            ps = cnn.prepareStatement("select ID_LISTAEMPLEADOS,nombres,apellidos,puesto,decode(departamento,1,'INSPECCION',2,'TESTING',3,'CHIPS',4,'STRIP Y POTTING',5,'TRANSFORMADORES',6,'TALLER',7,'BODEGA',8,'ADMINISTRACION','GERENCIA',9) as DEPTO from alistaempleados where codigo = ?");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                if (p == null) {
                    p = new ClassEvaluacion(){
                    };
                }
                p.setId_listaempleados(rs.getInt("ID_LISTAEMPLEADOS"));
                p.setNombres(rs.getString("NOMBRES"));
                p.setApellidos(rs.getString("APELLIDOS"));
                p.setDepto(rs.getString("DEPTO"));
                p.setPuesto(rs.getString("PUESTO"));
                cnn.close();
                ps.close();
                return p;
            }

        } catch (Exception e) {
               System.err.println("errroooooorrr"+e);    
        }
        
        return null;

    }   
 
 public static ClassEvaluacion buscarEmpleadoID(int id) throws SQLException {
        return buscarEmpleID(id, null);
    }    
 public static ClassEvaluacion buscarEmpleID(int id,ClassEvaluacion p) throws SQLException {
        Connection cnn = BD.getConnection();
        try {
            PreparedStatement ps = null;
            ps = cnn.prepareStatement("select ID_LISTAEMPLEADOS,codigo,nombres,apellidos,puesto,decode(departamento,1,'INSPECCION',2,'TESTING',3,'CHIPS',4,'STRIP Y POTTING',5,'TRANSFORMADORES',6,'TALLER',7,'BODEGA',8,'ADMINISTRACION','GERENCIA',9) as DEPTO from alistaempleados where id_listaempleados = ?");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                if (p == null) {
                    p = new ClassEvaluacion(){
                    };
                }
                p.setCodigo(rs.getInt("CODIGO"));
                p.setId_listaempleados(rs.getInt("ID_LISTAEMPLEADOS"));
                p.setNombres(rs.getString("NOMBRES"));
                p.setApellidos(rs.getString("APELLIDOS"));
                p.setDepto(rs.getString("DEPTO"));
                p.setPuesto(rs.getString("PUESTO"));
                cnn.close();
                ps.close();
                return p;
            }
        } catch (Exception e) {
               System.err.println("errroooooorrr"+e);    
        }
        return null;
    }   
 
 public static ArrayList<ClassEvaluacion> ListarEvaluacionesPendientes(String a ,int depto) {
                   
        return SQL1("SELECT v.id_evaluacion,e.codigo,E.NOMBRES,E.APELLIDOS,decode(e.departamento,1,'INSPECCION',2,'TESTING',3,'CHIPS',4,'STRIP Y POTTING',5,'TRANSFORMADORES',6,'TALLER',7,'BODEGA',8,'ADMINISTRACION',9,'GERENCIA') as DEPTO,\n" +
"E.PUESTO,TO_CHAR(V.FECHA,'dd/mm/yy') as FECHA,v.evaluacion,decode(v.face,1,'FASE 1',2,'FASE 2',3,'FASE 3') as FASE \n" +
"FROM alistaempleados E INNER JOIN bevaluacion_desempeno V ON e.id_listaempleados = v.id_listaempleados where v.estado = 1 and e.departamento = "+depto+" and upper(e.codigo) like upper('"+a+"%')");
    }
 
 public static ArrayList<ClassEvaluacion> ListarEvaluacionesTerminadas(String B,int depto) {
                   
        return SQL1("SELECT v.id_evaluacion,e.codigo,E.NOMBRES,E.APELLIDOS,decode(e.departamento,1,'INSPECCION',2,'TESTING',3,'CHIPS',4,'STRIP Y POTTING',5,'TRANSFORMADORES',6,'TALLER',7,'BODEGA',8,'ADMINISTRACION','GERENCIA',9) as DEPTO,\n" +
"E.PUESTO,TO_CHAR(V.FECHA,'dd/mm/yy') as FECHA,decode(v.face,1,'FASE 1',2,'FASE 2',3,'FASE 3') as FASE,v.evaluacion \n" +
"FROM alistaempleados E INNER JOIN bevaluacion_desempeno V ON e.id_listaempleados = v.id_listaempleados where v.estado = 2 and e.departamento = "+depto+" and upper(e.codigo) like upper('"+B+"%') order by v.id_evaluacion");
    }
    private static ArrayList<ClassEvaluacion> SQL1(String sql){
    ArrayList<ClassEvaluacion> list = new ArrayList<ClassEvaluacion>();
    Connection cn = BD.getConnection();
        try {
            ClassEvaluacion b;
            Statement stmt = cn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()){
                 b = new ClassEvaluacion();
                 b.setId_evaluacion(rs.getInt("id_evaluacion"));
                 b.setCodigo(rs.getInt("codigo"));
                 b.setNombres(rs.getString("nombres"));
                 b.setApellidos(rs.getString("apellidos"));
                 b.setDepto(rs.getString("depto"));
                 b.setPuesto(rs.getString("puesto"));
                 b.setFechaS(rs.getString("FECHA"));
                 b.setFaceS(rs.getString("FASE"));
                 b.setNoEvaluacion(rs.getInt("evaluacion"));
                 list.add(b);
            }
            cn.close();
        } catch (Exception e) {
            System.out.println(e);
            return null;
        } 
        return list;
}
  
public static ClassEvaluacion buscarEmpleadoIDEvaluacion(int id) throws SQLException {
        return buscarEmpleIDeva(id, null);
    }    
 public static ClassEvaluacion buscarEmpleIDeva(int id,ClassEvaluacion p) throws SQLException {
        Connection cnn = BD.getConnection();
        try {
            PreparedStatement ps = null;
            ps = cnn.prepareStatement("SELECT v.id_listaempleados,v.id_evaluacion,e.codigo,E.NOMBRES,E.APELLIDOS,decode(e.departamento,1,'INSPECCION',2,'TESTING',3,'CHIPS',4,'STRIP Y POTTING',5,'TRANSFORMADORES',6,'TALLER',7,'BODEGA',8,'ADMINISTRACION','GERENCIA',9) as DEPTO,E.PUESTO,to_char(V.FECHA,'dd/mm/yy') as fecha,v.face,decode(v.face,1,'FASE 1',2,'FASE 2',3,'FASE 3') as FASE, v.evaluacion FROM alistaempleados E INNER JOIN bevaluacion_desempeno V ON e.id_listaempleados = v.id_listaempleados\n" +
            "where v.id_evaluacion = ?");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                if (p == null) {
                    p = new ClassEvaluacion(){
                    };
                }
                p.setCodigo(rs.getInt("CODIGO"));
                p.setId_evaluacion(rs.getInt("ID_EVALUACION"));
                p.setNombres(rs.getString("NOMBRES"));
                p.setApellidos(rs.getString("APELLIDOS"));
                p.setDepto(rs.getString("DEPTO"));
                p.setPuesto(rs.getString("PUESTO"));
                p.setFechaS(rs.getString("FECHA"));
                p.setFaceS(rs.getString("FASE"));
                p.setFace(rs.getInt("face"));
                p.setNoEvaluacion(rs.getInt("evaluacion"));
                p.setId_listaempleados(rs.getInt("id_listaempleados"));
                cnn.close();
                ps.close();
                return p;
            }
        } catch (Exception e) {
               System.err.println("errroooooorrr"+e);    
        }
        return null;
    }       
    
 
 public static void insertarTrabajoEnEquipo(ClassEvaluacion c) throws SQLException {
        
        Connection cnn = BD.getConnection();
        PreparedStatement p = null;
        p = cnn.prepareStatement("insert into DTRABAJO_EQUIPO values(IDTRABAJO_EQUIPO.nextval,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,1,1,?)");
        p.setInt(1, c.getId_evaluacion());
        p.setInt(2, c.getUNO());
        p.setInt(3, c.getDOS());
        p.setInt(4, c.getTRES());
        p.setInt(5, c.getCUATRO());
        p.setInt(6, c.getUNO1());
        p.setInt(7, c.getDOS1());
        p.setInt(8, c.getTRES1());
        p.setInt(9, c.getCUATRO1());
        p.setInt(10, c.getUNO2());
        p.setInt(11, c.getDOS2());
        p.setInt(12, c.getTRES2());
        p.setInt(13, c.getCUATRO2());
        p.setInt(14, c.getUNO3());
        p.setInt(15, c.getDOS3());
        p.setInt(16, c.getTRES3());
        p.setInt(17, c.getCUATRO3());
        p.setInt(18, c.getUNO4());
        p.setInt(19, c.getDOS4());
        p.setInt(20, c.getTRES4());
        p.setInt(21, c.getCUATRO4());
        p.setString(22,c.getNOTA());
        p.executeUpdate();
        cnn.close();
        p.close();
    }
 
 public static void insertarOrientacionResultados(ClassEvaluacion c) throws SQLException {
        
        Connection cnn = BD.getConnection();
        PreparedStatement p = null;
        p = cnn.prepareStatement("insert into BORIENTACION values(IDORIENTACION.nextval,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,1,1,?)");
        p.setInt(1, c.getId_evaluacion());
        p.setInt(2, c.getUNO());
        p.setInt(3, c.getDOS());
        p.setInt(4, c.getTRES());
        p.setInt(5, c.getCUATRO());
        p.setInt(6, c.getUNO1());
        p.setInt(7, c.getDOS1());
        p.setInt(8, c.getTRES1());
        p.setInt(9, c.getCUATRO1());
        p.setInt(10, c.getUNO2());
        p.setInt(11, c.getDOS2());
        p.setInt(12, c.getTRES2());
        p.setInt(13, c.getCUATRO2());
        p.setInt(14, c.getUNO3());
        p.setInt(15, c.getDOS3());
        p.setInt(16, c.getTRES3());
        p.setInt(17, c.getCUATRO3());
        p.setInt(18, c.getUNO4());
        p.setInt(19, c.getDOS4());
        p.setInt(20, c.getTRES4());
        p.setInt(21, c.getCUATRO4());
        p.setString(22, c.getNOTA());
        p.executeUpdate();
        cnn.close();
        p.close();
    }
 
 
 public static void insertarOrganizacion(ClassEvaluacion c) throws SQLException {
        
        Connection cnn = BD.getConnection();
        PreparedStatement p = null;
        p = cnn.prepareStatement("insert into EORGANIZACION values(IDORGANIZACION.nextval,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,1,1,?)");
        p.setInt(1, c.getId_evaluacion());
        p.setInt(2, c.getUNO());
        p.setInt(3, c.getDOS());
        p.setInt(4, c.getTRES());
        p.setInt(5, c.getCUATRO());
        p.setInt(6, c.getUNO1());
        p.setInt(7, c.getDOS1());
        p.setInt(8, c.getTRES1());
        p.setInt(9, c.getCUATRO1());
        p.setInt(10, c.getUNO2());
        p.setInt(11, c.getDOS2());
        p.setInt(12, c.getTRES2());
        p.setInt(13, c.getCUATRO2());
        p.setInt(14, c.getUNO3());
        p.setInt(15, c.getDOS3());
        p.setInt(16, c.getTRES3());
        p.setInt(17, c.getCUATRO3());
        p.setInt(18, c.getUNO4());
        p.setInt(19, c.getDOS4());
        p.setInt(20, c.getTRES4());
        p.setInt(21, c.getCUATRO4());
        p.setString(22, c.getNOTA());
        p.executeUpdate();
        cnn.close();
        p.close();
    }
 
 public static void insertarResponsabilidad(ClassEvaluacion c) throws SQLException {
        
        Connection cnn = BD.getConnection();
        PreparedStatement p = null;
        p = cnn.prepareStatement("insert into GRESPONSABILIDAD values(IDRESPONSABILIDAD.nextval,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,1,1,?)");
        p.setInt(1, c.getId_evaluacion());
        p.setInt(2, c.getUNO());
        p.setInt(3, c.getDOS());
        p.setInt(4, c.getTRES());
        p.setInt(5, c.getCUATRO());
        p.setInt(6, c.getUNO1());
        p.setInt(7, c.getDOS1());
        p.setInt(8, c.getTRES1());
        p.setInt(9, c.getCUATRO1());
        p.setInt(10, c.getUNO2());
        p.setInt(11, c.getDOS2());
        p.setInt(12, c.getTRES2());
        p.setInt(13, c.getCUATRO2());
        p.setInt(14, c.getUNO3());
        p.setInt(15, c.getDOS3());
        p.setInt(16, c.getTRES3());
        p.setInt(17, c.getCUATRO3());
        p.setInt(18, c.getUNO4());
        p.setInt(19, c.getDOS4());
        p.setInt(20, c.getTRES4());
        p.setInt(21, c.getCUATRO4());
        p.setString(22, c.getNOTA());
        p.executeUpdate();
        cnn.close();
        p.close();
    }
 
 
 
 
 
 
 
 
 
}
