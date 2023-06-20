package Server;

import Client.DataObject;
import Client.IbusClientServices;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
import java.util.List;

public class BusResServiceImp extends UnicastRemoteObject implements IbusResService {

    private DbConnection db = new DbConnection();


    public BusResServiceImp() throws RemoteException {
        super();
    }
    @Override
    public void reservation(String id_traj, String numplac, String name, String mobile) throws RemoteException, SQLException {
        db.reserver(id_traj,numplac,name,mobile);
    }

    @Override
    public boolean login(String name, String pass, String table) throws RemoteException, SQLException {
       return db.login(name,pass,table);
    }

    @Override
    public void trajet(IbusClientServices clientServices) throws RemoteException, SQLException {
        DataObject a = db.trajet();
        clientServices.allTrajet(a);
    }

    @Override
    public DataObject availablePlace(int id, IbusClientServices client) throws SQLException, RemoteException {
       DataObject dataObject = db.places(id);
       //client.trajetPlaces(dataObject);
       return dataObject;
    }

    @Override
    public void mesResrvation(IbusClientServices client) throws SQLException, RemoteException {
        DataObject dataObject= db.allReservation(client.getClientName());
        client.getClientReservation(dataObject);
    }

    @Override
    public void supprimerReservation(String id_trajet, String num_place) throws SQLException, RemoteException {
        db.suprimer(id_trajet,num_place);
    }

    @Override
    public List<String> get_prix_mobile(String id_tra, String name) throws SQLException, RemoteException {
        List<String>  list = db.get_ifo(id_tra,name);
        return list;
    }
}
