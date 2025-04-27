/*

// 1. Make dialog at center, printing welcome user, your daily journal !
// 2. Implement closing window in dialog, as soon as you close that window, main window event opens !
3. Set up the layouts of main window event.
4. Add create new account stuff.
5. Configure database connection.

*/

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class JournalApp extends Frame implements ActionListener {
    TextArea BigTextArea;
    Button BSave, PPlus, PDelete;
    Panel PSidebar, JournalEntryPanel;
    GridBagConstraints GridConstraints;
    Font JournalFont, ButtonFont;

    int totalEntries;
    int entryCounter, currentCounter;
    DatabaseManager db;

    JournalApp(String username) throws SQLException {
        db = new DatabaseManager(username);
        SetDefaults(username);
        InitializeComponents();
        ActionListeners();
        entryCounter = 1;

        totalEntries = db.CountTotal();
        checkJournalEntry();

        for (int i = 0; i < totalEntries; i++) {
            addJournalEntry(entryCounter);
            entryCounter++;
        }
    }

    private void checkJournalEntry(){
        if(totalEntries == 0){
            BigTextArea.setText("Create a New Note. . .");
            BigTextArea.setEditable(false);
            BigTextArea.setFocusable(false);
            BSave.setEnabled(false);
            PDelete.setEnabled(false);
        } else if(currentCounter == 0){
            BigTextArea.setText("Select a note");
            BigTextArea.setEditable(false);
            BigTextArea.setFocusable(false);
            BSave.setEnabled(false);
            PDelete.setEnabled(false);
        } else{
            BigTextArea.setEditable(true);
            BigTextArea.setFocusable(true);
            BSave.setEnabled(true);
            PDelete.setEnabled(true);
        }

    }

    private void setFonts() {
        JournalFont = new Font(Font.SERIF, Font.ITALIC, 26);
        ButtonFont = new Font(Font.SANS_SERIF, Font.ITALIC, 18);
        addFonts();
    }

    private void addFonts() {
        BigTextArea.setFont(JournalFont);
        BSave.setFont(ButtonFont);
    }

    private void InitializeComponents() {
        BigTextArea = new TextArea("",0,0, TextArea.SCROLLBARS_VERTICAL_ONLY);
        BSave = new Button("Save");

        PSidebar = new Panel(new BorderLayout());
        PPlus = new Button(" + ");
        PDelete = new Button(" Delete ");

        JournalEntryPanel = new Panel(new GridBagLayout());
        GridConstraints = new GridBagConstraints();

        addComponents();
        setFonts();
    }

    private void addComponents() {
        add(BigTextArea, BorderLayout.CENTER);
        add(BSave, BorderLayout.SOUTH);
        add(PSidebar, BorderLayout.EAST);

        PSidebar.add(PPlus, BorderLayout.NORTH);
        PSidebar.add(JournalEntryPanel, BorderLayout.CENTER);
        PSidebar.add(PDelete, BorderLayout.SOUTH);
    }

    private void addJournalEntry(int e) {
        Button TempButton = new Button(Integer.toString(e));
        TempButton.setActionCommand(Integer.toString(e));

        TempButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                try {
                    String StoredContent = db.SelectData(e);
                    BigTextArea.setText(StoredContent);
                    currentCounter = e;
                    checkJournalEntry();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        GridConstraints.fill = GridBagConstraints.HORIZONTAL;
        GridConstraints.gridx = 0;
        GridConstraints.gridy = e;
        JournalEntryPanel.add(TempButton, GridConstraints);
        JournalEntryPanel.validate();
        JournalEntryPanel.repaint();
    }

    private void SetDefaults(String username) {
        setLayout(new BorderLayout(10,25));
        setBounds(200, 25, 1200,1000);
        setTitle(username);
        setVisible(true);
    }

    public void login(String username){
        new DialogManager("Welcome " + username + " to your daily journal !", this);
    }

    private void ActionListeners() {
        BSave.addActionListener(this);
        BSave.setActionCommand("SAVE");
        PPlus.addActionListener(this);
        PPlus.setActionCommand("ADD");
        PDelete.addActionListener(this);
        PDelete.setActionCommand("DELETE");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String action = e.getActionCommand();
        int error = validateText();

        try {
            if (error == 0) {
                if(BigTextArea.getText().equals("Create a New Note. . .")){
                    BigTextArea.setText("Type something...");
                }

                switch (action) {
                    case "SAVE":
                        db.UpdateData(currentCounter, BigTextArea.getText());
                        break;
                    case "ADD":
                        db.InsertValue(entryCounter, BigTextArea.getText());
                        addJournalEntry(entryCounter++);
                        break;
                    case "DELETE":
                        db.DeleteData(currentCounter);
                        deleteJournalEntry(--entryCounter);
                        break;
                    default:
                        break;
                }
                totalEntries = db.CountTotal();
                checkJournalEntry();
            }
        } catch (SQLException ex){
            throw new RuntimeException(ex);
        }

    }

    private void deleteJournalEntry(int e) {
        BigTextArea.setText("");
        currentCounter = 0;

        JournalEntryPanel.remove(e - 1);
        JournalEntryPanel.validate();
        JournalEntryPanel.repaint();
    }

    private int validateText() {
        if(BigTextArea.getText().trim().isEmpty()){
            new DialogManager("Your note is empty ! Please write something... ", this);
            return 1;
        }
        return 0;
    }
}
