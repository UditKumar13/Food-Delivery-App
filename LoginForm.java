import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

class LoginForm extends Form implements ActionListener {
    JFrame register_frame;
    JLabel emailLabel = new JLabel("EMAIL");
    JLabel pwdLabel = new JLabel("PASSWORD");
    JTextField emailField = new JTextField();
    JPasswordField pwdField = new JPasswordField();
    JButton loginButton = new JButton("LOGIN");
    JButton regButton = new JButton("REGISTER");
    JCheckBox showPwd = new JCheckBox("Show Password");
    private Connection connection;


    LoginForm(Connection connection)
    {
        this.connection = connection;
        createFormWindow();
        setFieldsLocation();
        addFieldsToform();
        buttonEvents();
    }

    @Override
    public void createFormWindow()
    {
        register_frame=new JFrame();
        register_frame.setTitle("Login Form");
        register_frame.setBounds(40,40,380,400);
        register_frame.getContentPane().setLayout(null);
        register_frame.setVisible(true);
        register_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        register_frame.setResizable(false);
    }

    @Override
    public void setFieldsLocation() {
        emailLabel.setBounds(40, 90, 100, 30);
        pwdLabel.setBounds(40, 140, 100, 30);
        emailField.setBounds(140, 90, 150, 30);
        pwdField.setBounds(140, 140, 150, 30);
        showPwd.setBounds(140, 175, 150, 30);
        loginButton.setBounds(40, 240, 100, 30);
        regButton.setBounds(190, 240, 100, 30);


    }

    @Override
    public void addFieldsToform() {
        register_frame.add(emailLabel);
        register_frame.add(pwdLabel);
        register_frame.add(emailField);
        register_frame.add(pwdField); 
        register_frame.add(showPwd);
        register_frame.add(loginButton);
        register_frame.add(regButton);
    }

    @Override
    public void buttonEvents() {
        loginButton.addActionListener(this);
        regButton.addActionListener(this);
        showPwd.addActionListener(this);
    }

    
    public boolean checkUsername(String email, String pwd)
    {
        PreparedStatement ps;
        ResultSet rs;
        boolean checkUser = false;
        String query = "SELECT * FROM `Customer` WHERE `EMAIL` =? and `PASSWRD` =?";

        try {
            ps = connection.prepareStatement(query);
            ps.setString(1, email);
            ps.setString(2,pwd);

            rs = ps.executeQuery();

            if(rs.next())
            {
                checkUser = true;
            }
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        return checkUser;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == loginButton) {
            String emailText;
            String pwdText;
            emailText = emailField.getText();
            pwdText = pwdField.getText();
            boolean userExists = checkUsername(emailText,pwdText);
            if (userExists) {
                JOptionPane.showMessageDialog(null, "Login Successful");
                register_frame.dispose();
            } else {
                JOptionPane.showMessageDialog(null, "Invalid Username or Password","Login Unsuccessful",JOptionPane.ERROR_MESSAGE);
                register_frame.dispose();
                int input = JOptionPane.showConfirmDialog(null, "Do you want to register yourself first?");
                if(input ==0){
                    new RegisterForm(connection);
                }
                else{
                    new LoginForm(connection);
                }
            }

        }
        if (e.getSource() == regButton) {
            emailField.setText("");
            pwdField.setText("");
            register_frame.dispose();
            new RegisterForm(this.connection);

        }
        if (e.getSource() == showPwd) {
            if (showPwd.isSelected()) {
                pwdField.setEchoChar((char) 0);
            } else {
                pwdField.setEchoChar('*');
            }


        }
    }

}