import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class CreateNewDatabaseWindow extends Frame implements ActionListener {
    Label DGreetings, DUsername, DPassword, DManagePassword;
    TextField TUsername, TPassword, TManagePassword;
    Button BDatabase, BManageDB, BManageDelete, BManageDeleteAll, BackBtn;
    Font TitleFont, TextFont, InputFont, ButtonFont;
    TextArea DManageArea;
    LoginDatabase logindb;


    CreateNewDatabaseWindow(LoginDatabase logindb) throws SQLException {
        this.logindb = logindb;

        setDefaults();
        initializeDefaults();
    }

    private void initializeDefaults() throws SQLException {
        DGreetings = new Label("Journal database creation");
        DUsername = new Label("Name your Database ");
        DPassword = new Label("Password for Database ");
        BDatabase = new Button("Create your personal database ");
        BManageDB = new Button("Manage all databases ");
        TUsername = new TextField();
        TPassword = new TextField();

        DManageArea = new TextArea(logindb.selectAll(), 5, 2, TextArea.SCROLLBARS_VERTICAL_ONLY);
        BManageDelete = new Button("Delete database");
        BManageDeleteAll = new Button("Delete all databases");
        DManagePassword = new Label("Password ");
        TManagePassword = new TextField();

        BackBtn = new Button("Back");

        HandleFont();
        addComponents();
        setComponentBounds();
        ActionListeners();
        hideManageContainer();
    }

    private void hideManageContainer() {
        DManageArea.setVisible(false);
        BManageDelete.setVisible(false);
        DManagePassword.setVisible(false);
        TManagePassword.setVisible(false);
        BManageDeleteAll.setVisible(false);
    }

    private void ActionListeners() {
        BDatabase.addActionListener(this);
        BDatabase.setActionCommand("CREATE");
        BManageDB.addActionListener(this);
        BManageDB.setActionCommand("MANAGE");
        BManageDelete.addActionListener(this);
        BManageDelete.setActionCommand("DELETE");
        BackBtn.addActionListener(this);
        BackBtn.setActionCommand("BACK");
        BManageDeleteAll.addActionListener(this);
        BManageDeleteAll.setActionCommand("DELETEALL");
    }

    private void HandleFont() {
        TitleFont = new Font(Font.SANS_SERIF, Font.ITALIC, 42);
        TextFont = new Font(Font.SANS_SERIF, Font.ITALIC, 14);
        InputFont = new Font(Font.SERIF, Font.ITALIC, 22);
        ButtonFont = new Font(Font.SERIF, Font.ITALIC, 16);
        setFonts();
    }

    private void setFonts() {
        DGreetings.setFont(TitleFont);
        DUsername.setFont(TextFont);
        DPassword.setFont(TextFont);
        TUsername.setFont(InputFont);
        TPassword.setFont(InputFont);
        BDatabase.setFont(ButtonFont);
        BManageDB.setFont(ButtonFont);
        DManageArea.setFont(TextFont);
        BManageDelete.setFont(ButtonFont);
        DManagePassword.setFont(TextFont);
        TManagePassword.setFont(InputFont);
        BackBtn.setFont(TextFont);
        BManageDeleteAll.setFont(ButtonFont);
    }


    private void setComponentBounds() {
        DGreetings.setBounds(130, 50, 600, 50);
        DUsername.setBounds(120, 250, 150, 25);
        DPassword.setBounds(120, 350, 200, 25);
        TUsername.setBounds(355, 250, 250, 30);
        TPassword.setBounds(355, 350, 250, 30);
        BDatabase.setBounds(100, 500, 300, 42);
        BManageDB.setBounds(450, 500, 200, 42);
        DManageArea.setBounds(100, 600, 400, 150);
        BManageDelete.setBounds(550, 650, 200, 42);
        DManagePassword.setBounds(550, 560, 200, 32);
        TManagePassword.setBounds(550,600, 200, 30);
        BManageDeleteAll.setBounds(550,700,200,42);
        BackBtn.setBounds(5, 35, 80, 25);
    }

    private void addComponents() {
        add(DGreetings);
        add(DUsername);
        add(DPassword);
        add(BDatabase);
        add(BManageDB);
        add(TUsername);
        add(TPassword);
        add(DManageArea);
        add(BManageDelete);
        add(DManagePassword);
        add(TManagePassword);
        add(BackBtn);
        add(BManageDeleteAll);
    }

    private void setDefaults() {
        setLayout(null);
        setVisible(true);
        setTitle("Personal Database Creation");
        setBounds(400,150, 800,800);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String action = e.getActionCommand();

        if(action.equals("CREATE")){
            int error = verifyInputs();
            if(error == 0){
                try {
                        if(logindb.checkDuplicates(TUsername.getText(), TPassword.getText())){
                            databaseCreation(TUsername.getText(), TPassword.getText());
                            DManageArea.setText(logindb.selectAll());
                         } else {
                            new DialogManager("duplicate database found with username or password ", this, 5000);
                        }
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }

        if(action.equals("MANAGE")){
            new DialogManager("Password that matches username will be affected...", this);
            manageDatabases();
        }

        if(action.equals("DELETE")){
            try {
                if(verifyPasswordField() == 0){
                    deleteDatabase(TManagePassword.getText());
                    DManageArea.setText(logindb.selectAll());
                    TManagePassword.setText("");
                }
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
         }

        if(action.equals("DELETEALL")){
            if(!(TManagePassword.getText().trim().equals("1970-01-01"))){
                new DialogManager("WRITE PASSWORD \"`1970-01-01`\" TO PROCEED ", this, 10000);
            }
            try {
                    deleteAllDatabase(TManagePassword.getText());
                    DManageArea.setText(logindb.selectAll());
                    TManagePassword.setText("");
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }

        if(action.equals("BACK")){
            setVisible(false);
            dispose();
        }
    }

    private void deleteAllDatabase(String password) throws SQLException {
        int e = logindb.deleteAllData(password);
        System.out.println("Successfully deleted " + e + " tables");
    }

    private void deleteDatabase(String password) throws SQLException {
        boolean deleted = logindb.deleteData(password);
        if(deleted){
            new DialogManager( " database of password " + password + " has been deleted successfully", this);
        } else{
            new DialogManager("pasword did not matched with any username ", this);
        }

    }

    private int verifyPasswordField() {
        if(TManagePassword.getText().trim().isEmpty()){
            new DialogManager("Password is required to delete ...", this);
            return 1;
        }
        return 0;
    }

    private void manageDatabases() {
            showManage();
    }

    private void showManage() {
        DManageArea.setVisible(true);
        DManageArea.setEditable(false);
        BManageDelete.setVisible(true);
        DManagePassword.setVisible(true);
        TManagePassword.setVisible(true);
        BManageDeleteAll.setVisible(true);
    }

    private void databaseCreation(String username, String password) throws SQLException {
        logindb.InsertValues(username, password);
        new DialogManager("database of " + username + " has been created successfully", this);
        new DialogManager("remember " + password + " it will never be shown again", this, 5000);
    }

    private int verifyInputs() {
        if(TUsername.getText().trim().isEmpty() || TPassword.getText().trim().isEmpty()){
            new DialogManager("Name and Password cannot be empty !", this);
            return 1;
        }
        return 0;
    }

}
