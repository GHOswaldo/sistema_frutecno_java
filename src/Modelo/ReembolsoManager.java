/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

/**
 *
 * @author OMCG1
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ReembolsoManager {

    private static final String URL = "jdbc:mysql://localhost:3306/sistemafrutecno?serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public void realizarReembolso(int idVenta) {
        try {
            Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
            connection.setAutoCommit(false);

            try {
                String obtenerVentaSQL = "SELECT * FROM ventas WHERE id = ?";
                PreparedStatement obtenerVentaStmt = connection.prepareStatement(obtenerVentaSQL);
                obtenerVentaStmt.setInt(1, idVenta);
                String incrementarStockSQL = "UPDATE productos SET stock = stock + (SELECT cantidad FROM detalle WHERE id_venta = ? AND id_pro = productos.id)";
                PreparedStatement incrementarStockStmt = connection.prepareStatement(incrementarStockSQL);
                incrementarStockStmt.setInt(1, idVenta);
                incrementarStockStmt.executeUpdate();
                // Eliminar la venta
                String eliminarVentaSQL = "DELETE FROM ventas WHERE id = ?";
                PreparedStatement eliminarVentaStmt = connection.prepareStatement(eliminarVentaSQL);
                eliminarVentaStmt.setInt(1, idVenta);
                eliminarVentaStmt.executeUpdate();
                // Confirmar la transacción
                connection.commit();
            } catch (SQLException e) {
                // En caso de error, realizar un rollback para deshacer los cambios
                connection.rollback();
                e.printStackTrace();
            } finally {
                // Restaurar la configuración de la conexión
                connection.setAutoCommit(true);
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
