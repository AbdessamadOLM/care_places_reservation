package Client;

import Server.IbusResService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.List;
import java.util.Vector;

public class ClientInterface extends JFrame {
    private JPanel global;
    private JTable trajet_table;
    private JTextField in_num_trajet;
    private JTextField in_name;
    private JButton button1;
    private JButton reserverButton;
    private JTextField in_num_place;
    private JPanel reservation_pan;
    private JTable mes_reservation;
    private JLabel Prix;
    private JPanel trajet_pan;
    private JPanel place_pan;
    private JTable places_table;
    private JButton button2;
    private JTextField mobile_in;
    String name;
    IbusClientServices client;
    IbusResService iBusResService;
    DataObject dataObject;

    public ClientInterface(String name, IbusResService ibusResService){
        initComponent();
        this.name = name;
       this.iBusResService = Login.ibusResService;
        try {
           client = new ClientServiceImp(name,trajet_table,places_table,mes_reservation,reservation_pan,trajet_pan,place_pan,global);
                ibusResService.trajet(client);
                ibusResService.mesResrvation(client);
        } catch (RemoteException | SQLException e) {
            e.printStackTrace();
        }
        trajet_table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseReleased(e);
                DefaultTableModel model1 = (DefaultTableModel) trajet_table.getModel();
                int selectedRow = trajet_table.getSelectedRow();
                if (selectedRow != -1) {
                    String id_trajet = model1.getValueAt(selectedRow, 0).toString();
                    System.out.println("ID Trajet : " + id_trajet);

                    try {
                        dataObject =ibusResService.availablePlace(Integer.parseInt(id_trajet), client);
                        Vector<Vector<Object>> datavector = dataObject.getDataVector();
                        Vector<String> column = dataObject.getColumnIdentifiers();
                        DefaultTableModel model = new DefaultTableModel();
                        model.setDataVector(datavector, column);
                        places_table.setModel(model);
                        JScrollPane jScrollPane = new JScrollPane(places_table);
                        jScrollPane.setPreferredSize(new Dimension(500, 100));
                        place_pan.setLayout(new FlowLayout());
                        place_pan.add(jScrollPane);
                    } catch (SQLException | RemoteException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
        button2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DefaultTableModel model1 = (DefaultTableModel) mes_reservation.getModel();
                int selectedRow = mes_reservation.getSelectedRow();
                if (selectedRow != -1) {
                    String id_tr = model1.getValueAt(selectedRow, 0).toString();
                    String num_plac = model1.getValueAt(selectedRow, 1).toString();
                    System.out.println("ID Trajet : " + id_tr+" num place "+num_plac);
                    try {
                        ibusResService.supprimerReservation(id_tr,num_plac);
                        ibusResService.mesResrvation(client);
                        JOptionPane.showMessageDialog(new JFrame(),"Réservation annulé");
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    } catch (RemoteException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
        places_table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                DefaultTableModel model1 = (DefaultTableModel) places_table.getModel();
                int selectedRow = places_table.getSelectedRow();
                if (selectedRow != -1) {
                    String id_trajet = model1.getValueAt(selectedRow, 0).toString();
                    String num_lap = model1.getValueAt(selectedRow, 1).toString();
                    in_num_trajet.setText(id_trajet);
                    in_num_place.setText(num_lap);
                    try {
                        in_name.setText(client.getClientName());
                        System.out.println("ID Trajet : " + id_trajet+" name "+client.getClientName()+" numero place "+num_lap);
                        List<String> list = ibusResService.get_prix_mobile(id_trajet,client.getClientName());
                        Prix.setText(list.get(1)+" DH");
                        mobile_in.setText(list.get(0));
                    } catch (RemoteException | SQLException ex) {
                        ex.printStackTrace();
                    }

                }
            }
        });

        reserverButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                try {
                    if(!in_num_place.equals("")){
                        ibusResService.reservation(in_num_trajet.getText(), in_num_place.getText(), client.getClientName(), mobile_in.getText());
                        ibusResService.mesResrvation(client);
                        JOptionPane.showMessageDialog(new JFrame(),"Réservation Réussie");
                    }else
                    JOptionPane.showMessageDialog(new JFrame(),"Sélectionnez une place");
                } catch (RemoteException ex) {
                    ex.printStackTrace();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }



    public void initComponent(){
        setContentPane(global);
        this.setLocationRelativeTo(null);
        setSize(1000,500);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }



}
