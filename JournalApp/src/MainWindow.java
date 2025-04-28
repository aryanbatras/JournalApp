import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.sql.SQLException;

public class MainWindow extends Frame implements ActionListener {
    Label LGreetings, LUsername, LPassword;
    TextField TUsername, TPassword;
    Button BLogin, BCreateNewAccount;
    Font TitleFont, TextFont, InputFont, ButtonFont;
    LoginDatabase logindb;

    MainWindow() throws SQLException, IOException {
        logindb = new LoginDatabase();
        SetDefaultWindow();
        InitializeWindow();
//        checkLoginSession();
    }

  /*  private boolean checkLoginSession() throws IOException, SQLException {
        boolean logged = false;

        File r = new File("loginSession.txt");

        if(!r.exists()){
            r.createNewFile();
        }

        BufferedReader f = new BufferedReader(new FileReader(r));
        String sessionFlag = f.readLine();
        String username = "";
        String password = "";

        if(sessionFlag != null && sessionFlag.trim().equals("true")){
            username = f.readLine();
            password = f.readLine();

            if(username != null) username = username.trim();
            if(password != null) password = password.trim();

            System.out.println("[DEBUG] Loaded username: '" + username + "'");
            System.out.println("[DEBUG] Loaded password: '" + password + "'");

            TUsername.setText(username);
            TPassword.setText(password);
            loginWork();
            logged = true;
            System.out.println("Successfully loaded session...");
        }
        f.close();

        return logged;
    }

    private void successfullyLoggedIn(String s, String password) throws IOException {
        File r = new File("loginSession.txt");
        BufferedWriter f = new BufferedWriter(new FileWriter(r));
        f.write("true\n");
        f.write(s + "\n");
        f.write(password);
        f.close();
        System.out.println("File successfully updated...");
    }
*/
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
                    loginWork();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                } catch (IOException ex) {
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

    private void loginWork() throws SQLException, IOException {
        if(logindb.authenticate(TUsername.getText(), TPassword.getText())){
            new JournalApp(TUsername.getText()).login(TUsername.getText());
//            successfullyLoggedIn(TUsername.getText(), TPassword.getText());
            autoWindowClose();
        } else {
            new DialogManager("Wrong Login Credentials !", this);
        }
    }

    public void autoWindowClose() {
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
