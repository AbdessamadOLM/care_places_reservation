package Client;

import Server.IbusResService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
import java.util.Vector;


public class ClientServiceImp extends UnicastRemoteObject implements IbusClientServices {

    private  String name;
    private  JTable trajet;
    private  JTable places;
    private  JPanel place_pan;
    private  JPanel trajet_pan;
    private  JPanel global;
    private  IbusResService server;
    private JTable resrv_table;
    private JPanel resrv_pan;

    protected ClientServiceImp(String name, JTable Trajet, JTable places, JTable resrv_table,JPanel resrv_pan,JPanel trajet_pan,JPanel place_pan,JPanel global) throws RemoteException {
        this.name = name;
        this.trajet = Trajet;
        this.places = places;
        this.server = Login.ibusResService;
        this.place_pan = place_pan;
        this.trajet_pan = trajet_pan;
        this.resrv_table = resrv_table;
        this.resrv_pan = resrv_pan;
        this.global = global;
    }

    @Override
    public void allTrajet(DataObject dataObject) throws RemoteException, SQLException {
        Vector<Vector<Object>> datavector = dataObject.getDataVector();
        Vector<String> column  = dataObject.getColumnIdentifiers();
        DefaultTableModel model = new DefaultTableModel();
        model.setDataVector(datavector,column);
        this.trajet.setModel(model);
        JScrollPane jScrollPane = new JScrollPane(this.trajet);
        jScrollPane.setPreferredSize(new Dimension(500, 100));
        trajet_pan.setLayout(new FlowLayout());
        trajet_pan.add(jScrollPane);
    }


    @Override
    public void trajetPlaces(DataObject datao) throws RemoteException {
        Vector<Vector<Object>> datavector = datao.getDataVector();
        Vector<String> column = datao.getColumnIdentifiers();
        DefaultTableModel model = new DefaultTableModel();
        model.setDataVector(datavector, column);
        places.setModel(model);
        JScrollPane jScrollPane = new JScrollPane(places);
        jScrollPane.setPreferredSize(new Dimension(250, 200));
        place_pan.setLayout(new FlowLayout());
        place_pan.add(jScrollPane);
    }

    @Override
    public String getClientName() throws RemoteException {
        return this.name;
    }

    @Override
    public void getClientReservation(DataObject object) throws RemoteException {
        Vector<Vector<Object>> datavector = object.getDataVector();
        Vector<String> column  = object.getColumnIdentifiers();
        DefaultTableModel model = new DefaultTableModel();
        model.setDataVector(datavector,column);
        this.resrv_table.setModel(model);
        JScrollPane jScrollPane = new JScrollPane(this.resrv_table);
        jScrollPane.setPreferredSize(new Dimension(300, 100));
        resrv_pan.setLayout(new FlowLayout());
        resrv_pan.add(jScrollPane);
    }
}
