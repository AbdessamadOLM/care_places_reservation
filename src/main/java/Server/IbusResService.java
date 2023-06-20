package Server;

import Client.DataObject;
import Client.IbusClientServices;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.List;

public interface IbusResService extends Remote {
    public void reservation(String id_traj,String numplac,String name,String mobile) throws RemoteException, SQLException;
    public boolean login(String name, String pass, String table) throws RemoteException, SQLException;
    public void trajet(IbusClientServices clientServices) throws RemoteException, SQLException;
    public DataObject availablePlace(int id_trajet, IbusClientServices client) throws SQLException, RemoteException;
    public void mesResrvation(IbusClientServices client)throws SQLException, RemoteException;
    public void supprimerReservation(String id_trajet, String num_place)throws SQLException, RemoteException;
    public List<String> get_prix_mobile(String id_tra, String name) throws SQLException, RemoteException;
}
