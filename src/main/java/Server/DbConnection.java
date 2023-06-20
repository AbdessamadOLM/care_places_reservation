package Server;

import Client.DataObject;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class DbConnection {
    String url = "jdbc:mysql://localhost:3306/busreservation";
    String username = "root";
    String password = "";
    Connection connection;
    Statement statement;
    ResultSet resultSet;

    public boolean login(String name,String pass,String table) throws SQLException {
        connection = DriverManager.getConnection(url, username, password);
        String query = "SELECT * FROM "+table;
        statement = connection.createStatement();
        resultSet = statement.executeQuery(query);
        while (resultSet.next()){
            if((resultSet.getString("nom")).equals(name) && (resultSet.getString("passwrd")).equals(pass)){
                connection.close();
                return true;
            }
        }
        connection.close();
        return false;
    }
    public DataObject trajet() throws SQLException {
        connection = DriverManager.getConnection(url, username, password);
        String query = "SELECT * FROM trajet";
        statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        Vector<Vector<Object>> dataVector = new Vector<>();
        Vector<String> columnIdentifiers = new Vector<>();
        ResultSetMetaData metaData = resultSet.getMetaData();
//        int columnCount = metaData.getColumnCount();
//
//        // Get the column names from the ResultSetMetaData and add them to columnIdentifiers
//        for (int i = 1; i <= columnCount; i++) {
//            columnIdentifiers.add(metaData.getColumnLabel(i));
//        }
        String[] aux =new String[] {"Numero trajet","Depart","Arrivé","Bus","Date","Prix"};

//         Get the column names from the ResultSetMetaData and add them to columnIdentifiers
        for (int t = 0; t < aux.length; t++) {
            columnIdentifiers.add(aux[t]);
        }

        try {
            while (resultSet.next()) {
                Vector<Object> row = new Vector<>();
                row.add(resultSet.getObject("id_traj"));
                row.add(resultSet.getObject("ville_dep"));
                row.add(resultSet.getObject("ville_arr"));
                row.add(resultSet.getObject("bus_num"));
                row.add(resultSet.getObject("date"));
                row.add(resultSet.getObject("prix"));
                dataVector.add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        connection.close();
        // Create a custom data object to hold the column identifiers and data vector
        DataObject dataObject = new DataObject(columnIdentifiers, dataVector);

        // Transfer the dataObject to the client using RMI
        return dataObject;

    }

    public DataObject places(int id_trajet) throws SQLException {
        connection = DriverManager.getConnection(url, username, password);
        String query = "SELECT * FROM reservation where id_traj = "+id_trajet;
        statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        Vector<Vector<Object>> dataVector = new Vector<>();
        Vector<String> columnIdentifiers = new Vector<>();
        List<Integer> list = new ArrayList<Integer>();
        String[] aux =new String[] {"Numero trajet","Numero place","etat"};

//         Get the column names from the ResultSetMetaData and add them to columnIdentifiers
        for (int t = 0; t < aux.length; t++) {
            columnIdentifiers.add(aux[t]);
        }

        try {
            while (resultSet.next()) {
                list.add(resultSet.getInt("numero_place"));
            }
            for (int i=1 ; i<30 ; i++){
                boolean flag = false;
              for (int seat : list){
                  if(i == seat){
                      flag = true;
                  }
              }
              if(!flag){
                  Vector<Object> row = new Vector<>();
                  row.add(id_trajet);
                  row.add(i);
                  row.add("non reservé");
                  dataVector.add(row);
              }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        connection.close();
        // Create a custom data object to hold the column identifiers and data vector
        DataObject dataObject = new DataObject(columnIdentifiers, dataVector);

        // Transfer the dataObject to the client using RMI
        return dataObject;

    }
    public DataObject allReservation(String name) throws SQLException {
        connection = DriverManager.getConnection(url, username, password);
        String query = "SELECT t.id_traj , t.numero_place, t.nom , t.mobile, t2.ville_dep,t2.prix FROM reservation t INNER JOIN trajet t2 ON t.id_traj = t2.id_traj where t.nom=?";
        PreparedStatement stmt = connection.prepareStatement(query);
        stmt.setString(1, name);
        ResultSet resultSet = stmt.executeQuery();
        Vector<Vector<Object>> dataVector = new Vector<>();
        Vector<String> columnIdentifiers = new Vector<>();
        List<Integer> list = new ArrayList<Integer>();
        String[] aux =new String[] {"Numero trajet","Numero place","nom","mobile","Depart","prix"};
//         Get the column names from the ResultSetMetaData and add them to columnIdentifiers
        for (int t = 0; t < aux.length; t++) {
            columnIdentifiers.add(aux[t]);
        }

        try {
            while (resultSet.next()) {
                Vector<Object> row = new Vector<>();
                row.add(resultSet.getObject("id_traj"));
                row.add(resultSet.getObject("numero_place"));
                row.add(resultSet.getObject("nom"));
                row.add(resultSet.getObject("mobile"));
                row.add(resultSet.getObject("ville_dep"));
                row.add(resultSet.getObject("prix"));
                System.out.println(resultSet.getString("ville_dep"));
                dataVector.add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        connection.close();
        // Create a custom data object to hold the column identifiers and data vector
        DataObject dataObject = new DataObject(columnIdentifiers, dataVector);

        // Transfer the dataObject to the client using RMI
        return dataObject;

    }
    public void suprimer(String id_trajet,String num_place) throws SQLException {
        connection = DriverManager.getConnection(url, username, password);
        String query = "DELETE  from reservation where id_traj =? and numero_place=?";
        PreparedStatement stmt = connection.prepareStatement(query);
        stmt.setString(1, id_trajet);
        stmt.setString(2, num_place);
        int lignesSupprimees = stmt.executeUpdate();
        System.out.println(lignesSupprimees);
    }
    public List<String> get_ifo(String id_trajet,String name) throws SQLException {
        List<String> aux = new ArrayList<>();
        connection = DriverManager.getConnection(url, username, password);
        String query = "SELECT * FROM reservation where nom = ?";
        PreparedStatement stmt = connection.prepareStatement(query);
        stmt.setString(1, name);
        ResultSet resultSet = stmt.executeQuery();
        while (resultSet.next()){
            aux.add(resultSet.getString("mobile"));
            break;
        }
        query = "SELECT * FROM trajet where id_traj = ?";
        stmt = connection.prepareStatement(query);
        stmt.setString(1, id_trajet);
        resultSet = stmt.executeQuery();
        while (resultSet.next()){
            aux.add(resultSet.getString("prix"));
            break;
        }
        return aux;
    }
    public void reserver(String id_traj, String numplac, String name, String mobile) throws SQLException {
        connection = DriverManager.getConnection(url, username, password);
        String query = "insert into reservation(id_traj,numero_place,nom,mobile)values (?,?,?,?)";
        PreparedStatement stmt = connection.prepareStatement(query);
        stmt.setString(1, String.valueOf(id_traj));
        stmt.setString(2, String.valueOf(id_traj));
        stmt.setString(3, String.valueOf(name));
        stmt.setString(4, String.valueOf(mobile));
        int lignes_ajouter = stmt.executeUpdate();
        System.out.println(lignes_ajouter);
    }


    public static void main(String[] args) throws SQLException {
        DbConnection db = new DbConnection();
       db.reserver("1","20","admin","07757175");
    }
}
