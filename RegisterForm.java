import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

class RegisterForm extends Form implements ActionListener {
    JFrame register_frame;
    String[] gender={"Male","Female"};
    JLabel nameLabel=new JLabel("NAME");
    JLabel genderLabel=new JLabel("GENDER");
    JLabel pwdLabel=new JLabel("PASSWORD");
    JLabel confirmPwdLabel=new JLabel("CONFIRM PASSWORD");
    JLabel addrLabel=new JLabel("ADDRESS");
    JLabel emailLabel=new JLabel("EMAIL");
    JTextField nameField=new JTextField();
    JComboBox genderDropDown=new JComboBox(gender);
    JPasswordField pwdField=new JPasswordField();
    JPasswordField confirmPwdField=new JPasswordField();
    JTextField addrField=new JTextField();
    JTextField emailField=new JTextField();
    JButton registerButton=new JButton("REGISTER");
    JButton resButton=new JButton("RESET");
    private Connection connection;


    RegisterForm(Connection connection)
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
        register_frame.setTitle("Registeration Form");
        register_frame.setBounds(50,50,400,660);
        register_frame.getContentPane().setLayout(null);
        register_frame.setVisible(true);
        register_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        register_frame.setResizable(false);
    }

    @Override
    public void setFieldsLocation()
    {
        nameLabel.setBounds(18,82,40,70);
        genderLabel.setBounds(18,122,80,70);
        pwdLabel.setBounds(18,172,100,70);
        confirmPwdLabel.setBounds(18,222,140,70);
        addrLabel.setBounds(18,272,100,70);
        emailLabel.setBounds(18,322,100,70);
        nameField.setBounds(178,105,165,23);
        genderDropDown.setBounds(180,144,165,23);
        pwdField.setBounds(178,195,165,23);
        confirmPwdField.setBounds(178,243,165,23);
        addrField.setBounds(178,295,165,23);
        emailField.setBounds(178,345,165,23);
        registerButton.setBounds(68,402,100,35);
        resButton.setBounds(218,402,100,35);
    }

    @Override
    public void addFieldsToform()
    {
        register_frame.add(nameLabel);
        register_frame.add(genderLabel);
        register_frame.add(pwdLabel);
        register_frame.add(confirmPwdLabel);
        register_frame.add(addrLabel);
        register_frame.add(emailLabel);
        register_frame.add(nameField);
        register_frame.add(genderDropDown);
        register_frame.add(pwdField);
        register_frame.add(confirmPwdField);
        register_frame.add(addrField);
        register_frame.add(emailField);
        register_frame.add(registerButton);
        register_frame.add(resButton);
    }

    @Override
    public void buttonEvents()
    {
        registerButton.addActionListener(this);
        resButton.addActionListener(this);
    }

    public boolean checkUsername(String email)
    {
        PreparedStatement ps;
        ResultSet rs;
        boolean checkUser = false;
        String query = "SELECT * FROM `Customer` WHERE `EMAIL` =?";

        try {
            ps = connection.prepareStatement(query);
            ps.setString(1, email);

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
        if(e.getSource()==registerButton)
        {
            try {
                PreparedStatement pstmt=connection.prepareStatement("insert into Customer values(?,?,?,?,?)");
                pstmt.setString(1,nameField.getText());
                pstmt.setString(2,genderDropDown.getSelectedItem().toString());
                pstmt.setString(3,pwdField.getText());
                pstmt.setString(4,addrField.getText());
                pstmt.setString(5,emailField.getText());
                if(pwdField.getText().equalsIgnoreCase(confirmPwdField.getText()))
                {
                    String emailText;
                    emailText = emailField.getText();
                    boolean userExists = checkUsername(emailText);
                    if (!userExists) {
                        pstmt.executeUpdate();
                        register_frame.dispose();
                        JOptionPane.showMessageDialog(null, "Registered Successfully");
                    }
                    else{
                        JOptionPane.showMessageDialog(null, "User already exists with email ID!","Registeration Unsuccessful",JOptionPane.ERROR_MESSAGE);
                    }
                }
                else
                {
                    JOptionPane.showMessageDialog(null,"Make sure passwords match!");
                }

            } catch (SQLException e1) {
                e1.printStackTrace();
            }


        }
        if(e.getSource()==resButton)
        {
            nameField.setText("");
            genderDropDown.setSelectedItem("Female");
            pwdField.setText("");
            confirmPwdField.setText("");
            addrField.setText("");
            emailField.setText("");
        }

    }
}