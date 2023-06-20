package Client;

import java.io.Serializable;
import java.util.Vector;

public class DataObject implements Serializable {
    Vector<String> columnIdentifiers;
    Vector<Vector<Object>> dataVector;

    public DataObject(Vector<String> columnIdentifiers,Vector<Vector<Object>> dataVector){
        this.columnIdentifiers = columnIdentifiers;
        this.dataVector = dataVector;
    }
    public Vector<String> getColumnIdentifiers(){
        return this.columnIdentifiers;
    }
    public Vector<Vector<Object>>  getDataVector(){
        return this.dataVector;
    }
}
