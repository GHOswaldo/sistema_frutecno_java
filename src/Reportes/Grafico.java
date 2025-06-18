/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package Reportes;

/**
 *
 * @author OMCG1
 */

import Modelo.Conexion;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Grafico {
    public static void GraficarClientesMasCompraron(String fecha) {
        Connection con;
        Conexion cn = new Conexion();
        PreparedStatement ps;
        ResultSet rs;
        try {
            String sql = "SELECT c.id, c.nombre, SUM(v.total) as total_compras "
                    + "FROM clientes c "
                    + "JOIN ventas v ON c.id = v.cliente "
                    + "WHERE v.fecha = ? "
                    + "GROUP BY c.id, c.nombre";
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, fecha);
            rs = ps.executeQuery();
            DefaultPieDataset dataset = new DefaultPieDataset();
            double totalv = 0.0;
            while (rs.next()) {
                totalv += rs.getDouble("total_compras");
            }
            rs.beforeFirst(); 
            while (rs.next()) {
                int idCliente = rs.getInt("id");
                String nombreCliente = rs.getString("nombre");
                double totalc = rs.getDouble("total_compras");
                double porcentaje = (totalc / totalv) * 100.0;
                dataset.setValue(idCliente + " - " + nombreCliente + " - Total: $" + String.format("%.2f", totalc) + " (" + String.format("%.2f", porcentaje) + "%)", totalc);
            }
            JFreeChart jf = ChartFactory.createPieChart("Gráfico de compras en el día", dataset);
            ChartFrame f = new ChartFrame("Gráfico de ventas", jf);
            f.setSize(1000, 500);
            f.setLocationRelativeTo(null);
            f.setVisible(true);
        } catch (SQLException e) {
            System.out.println(e.toString());
        }
    }
}