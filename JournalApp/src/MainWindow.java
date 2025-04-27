import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;

public class MainWindow extends Frame implements ActionListener {
    Label LGreetings, LUsername, LPassword;
    TextField TUsername, TPassword;
    Button BLogin, BCreateNewAccount;
    Font TitleFont, TextFont, InputFont, ButtonFont;
    LoginDatabase logindb;

    MainWindow() throws SQLException {
        logindb = new LoginDatabase();
        SetDefaultWindow();
        InitializeWindow();
    }

    private void InitializeWindow() {
        LGreetings = new Label("Welcome To Journal App !");
        LUsername = new Label("USERNAME ");
        LPassword = new Label("PASSWORD ");

        TUsername = new TextField();
        TPassword = new TextField();

        BLogin = new Button("Login ");
        BCreateNewAccount = new Button("Create a Database ");

        HandleFont();
        AddComponents();
        SetBoundsWindow();
        ActionListeners();
    }

    private void HandleFont() {
        TitleFont = new Font(Font.SANS_SERIF, Font.ITALIC, 42);
        TextFont = new Font(Font.SANS_SERIF, Font.BOLD, 18);
        InputFont = new Font(Font.SERIF, Font.ITALIC, 22);
        ButtonFont = new Font(Font.SERIF, Font.ITALIC, 16);
        setFonts();
    }

    private void setFonts() {
        LGreetings.setFont(TitleFont);
        LUsername.setFont(TextFont);
        LPassword.setFont(TextFont);
        TUsername.setFont(InputFont);
        TPassword.setFont(InputFont);
        BLogin.setFont(ButtonFont);
        BCreateNewAccount.setFont(ButtonFont);
    }

    private void SetBoundsWindow() {
        LGreetings.setBounds(130, 50, 600, 50);
        LUsername.setBounds(150, 250, 150, 25);
        LPassword.setBounds(150, 350, 150, 25);
        TUsername.setBounds(300, 250, 400, 25);
        TPassword.setBounds(300, 350, 400, 25);
        BLogin.setBounds(180, 500, 150, 35);
        BCreateNewAccount.setBounds(380, 500, 250, 35);
    }

    private void AddComponents() {
        add(LGreetings);
        add(LUsername);
        add(LPassword);
        add(TUsername);
        add(TPassword);
        add(BLogin);
        add(BCreateNewAccount);
    }

    private void SetDefaultWindow() {
        setLayout(null);
        setVisible(true);
        setTitle("Journal App");
        setBounds(400,150, 800,800);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        String action = e.getActionCommand();

        if(action.equals("LOGIN")){
            int error = verifyInputs();
            if(error == 0) {
                try {
                    if(logindb.authenticate(TUsername.getText(), TPassword.getText())){
                        new JournalApp(TUsername.getText()).login(TUsername.getText());
                        autoWindowClose();
                    } else {
                        new DialogManager("Wrong Login Credentials !", this);
                    }
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }

        if(action.equals("CREATE")){
            try {
                new CreateNewDatabaseWindow(logindb);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    private void autoWindowClose() {
        setVisible(false);
        dispose();
    }

    private int verifyInputs() {
        if(TUsername.getText().trim().isEmpty()){
            new DialogManager("Username cannot be empty", this);
            return 1;
        } else if(TPassword.getText().trim().isEmpty()){
            new DialogManager("Password cannot be empty", this);
            return 1;
        }
        return 0;
    }

    private void ActionListeners() {
        addWindowListener(new WindowHandler());
        BLogin.addActionListener(this);
        BLogin.setActionCommand("LOGIN");
        BCreateNewAccount.addActionListener(this);
        BCreateNewAccount.setActionCommand("CREATE");
    }

}

class WindowHandler extends WindowAdapter{
    @Override
    public void windowClosing(WindowEvent e) {
        e.getWindow().dispose();
        e.getWindow().setVisible(false);
        System.out.println("Closing window...");
    }
}
