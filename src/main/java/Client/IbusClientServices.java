package Client;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.SQLException;

public interface IbusClientServices extends Remote {
    public void allTrajet(DataObject dataObject) throws RemoteException, SQLException;
    public void trajetPlaces(DataObject datao) throws RemoteException;
    public String getClientName() throws RemoteException;
    public void getClientReservation(DataObject object) throws RemoteException;

}
