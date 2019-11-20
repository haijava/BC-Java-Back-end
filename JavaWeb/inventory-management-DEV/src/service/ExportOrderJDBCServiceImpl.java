package service;

import model.ExportOrder;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static jdk.nashorn.internal.objects.NativeError.printStackTrace;

public class ExportOrderJDBCServiceImpl implements ExportService {
    private String jdbcURL = "jdbc:mysql://localhost:3306/inventory_management";
    private String jdbcUsername = "codegym";
    private String jdbcPassword = "codegym.123";

        private static final String INSERT_EX_SQL = "INSERT INTO export_order (name, idStaff, idStock, idStore, createBy) VALUES (?,?,?,?,?);";
        private static final String SELECT_USER_BY_IDEXPORTORDER = "SELECT * FROM export_order WHERE (idExportOrder = ?);";
        private static final String SELECT_ALL_EXPORTORDER = "select * from export_order inner join store on store.idStore = export_order.idStore\n" +
                                                             " inner join stock on stock.idStock = export_order.idStock\n" +
                                                             " inner join staff on staff.idStaff = export_order.idStaff\n" +
                                                             "where staff.isDelete = 0 and  stock.isDelete = 0 and store.isDelete = 0 and export_order.isDelete = 0";
        private static final String DELETE_USER_SQL = "UPDATE export_order SET isDelete = 1, deleteBy = ? WHERE (idExportOrder = ?);";
        private static final String UPDATE_USER_SQL = "UPDATE export_order SET name = ?, modifyBy = ? WHERE (idExportOrder = ?);";
    protected Connection getConnection() {
    Connection connection = null;
        try {
        Class.forName("com.mysql.jdbc.Driver");
        connection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
    } catch (SQLException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    } catch (ClassNotFoundException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }
        return connection;
}

    private void printSQLException(SQLException ex) {
        for (Throwable e : ex) {
            if (e instanceof SQLException) {
                e.printStackTrace(System.err);
                System.err.println("SQLState: " + ((SQLException) e).getSQLState());
                System.err.println("Error Code: " + ((SQLException) e).getErrorCode());
                System.err.println("Message: " + e.getMessage());
                Throwable t = ex.getCause();
                while (t != null) {
                    System.out.println("Cause: " + t);
                    t = t.getCause();
                }
            }
        }
    }

    @Override
    public List<ExportOrder> findAll() {
        List<ExportOrder> exportOrder = new ArrayList<>();
       try( Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_EXPORTORDER);) {
            System.out.println(preparedStatement);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                int idExportOrder = rs.getInt("export_order.idExportOrder");
                String name = rs.getString("export_order.name");
                String createBy = rs.getString("export_order.createBy");
                String nameStaff = rs.getString("staff.name");
                String nameStore = rs.getString("store.name");
                String nameStock = rs.getString("stock.name");
                String createDate = rs.getString("export_order.createDate");
                String modifyBy = rs.getString("export_order.modifyBy");
                String modifyDate = rs.getString("export_order.modifyDate");

                exportOrder.add(new ExportOrder(idExportOrder,nameStore, nameStock,nameStaff,name, createBy,createDate,modifyBy,modifyDate));
            }
        } catch (SQLException e) {
            printSQLException(e);
        }
        return exportOrder;
    }

    @Override
    public void save(ExportOrder exportOrder){
        System.out.println("INSERT_USER_SQL");
        try(Connection connection = getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(INSERT_EX_SQL)) {

            preparedStatement.setString(1,exportOrder.getName());
            preparedStatement.setInt(2,exportOrder.getIdStaff());
            preparedStatement.setInt(3,exportOrder.getIdStock());
            preparedStatement.setInt(4,exportOrder.getIdStore());
            preparedStatement.setString(5,exportOrder.getCreateBy());
            System.out.println(preparedStatement);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            printSQLException(e);
        }
    }
    @Override
//    idExportOrder,name,exportDate,createBy
    public ExportOrder findById(int id) {
        ExportOrder exportOrder = null;
            try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_USER_BY_IDEXPORTORDER);) {
            preparedStatement.setInt(1, id);
            System.out.println(preparedStatement);
            ResultSet rs = preparedStatement.executeQuery();

            // Step 4: Process the ResultSet object.
            while (rs.next()) {
                int idExportOrder = rs.getInt("idExportOrder");
                String name = rs.getString("name");
//                String exportDate = rs.getString("exportDate");
//                String deleteBy = rs.getString("deleteBy");
//                String deleteDate = rs.getString("deleteDate");
                String modifyBy = rs.getString("modifyBy");
                String modifyDate = rs.getString("modifyDate");
                String createBy = rs.getString("createBy");
                String createDate = rs.getString("createDate");
                exportOrder = new ExportOrder(idExportOrder, name,null,null, createBy,createDate, modifyBy, modifyDate);
            }
        } catch (SQLException e) {
            printSQLException(e);
        }
        return exportOrder;
    }


    @Override
    public void update(int idExportOrder, ExportOrder exportOrder){
        boolean rowUpdate;
        try(Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement(UPDATE_USER_SQL);) {

            statement.setString(1,exportOrder.getName());
            statement.setString(2,exportOrder.getModifyBy());
            statement.setInt(3,exportOrder.getIdExportOrder());

            rowUpdate = statement.executeUpdate()>0;
        } catch (SQLException e) {
            printSQLException(e);
        }
    }

    @Override
    public void remove(int idExportOrder, ExportOrder exportOrder){
        boolean rowUpdated;
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_USER_SQL);){
//            statement.setInt(1,idExportOrder);
            statement.setString(1,exportOrder.getDeleteBy());
            statement.setInt(2,exportOrder.getIdExportOrder());
            rowUpdated = statement.executeUpdate()>0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



}


