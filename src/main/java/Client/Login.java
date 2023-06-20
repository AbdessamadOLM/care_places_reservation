package Client;

import Server.IbusResService;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.Naming;

public class Login extends JFrame {

    private JPanel login;
    private JTextField username;
    private JPasswordField password;
    private JButton loginButton;
    static IbusResService ibusResService;

    public Login(){
        setContentPane(login);
        this.setLocationRelativeTo(null);
        setTitle("Welcome");
        setSize(300,150);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        getRootPane().setDefaultButton(loginButton);
        setVisible(true);
        int port = 52000;
        String url = "rmi://localhost:"+port+"/echo";
        try {
            ibusResService = (IbusResService) Naming.lookup(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if(!username.equals(" ") || !password.equals(" ")){
                        if(ibusResService.login(username.getText(), password.getText(),"client")){
                            new ClientInterface(username.getText(),ibusResService);
                            dispose();
                        }else{
                            JOptionPane.showMessageDialog(new JFrame(),"username or password is wrong");
                            username.setText("");
                            password.setText("");
                        }
                    }else{
                        JOptionPane.showMessageDialog(new JFrame(),"username or password is missing");
                    }

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    public static void main(String[] args) {
        Login login = new Login();
    }
}
