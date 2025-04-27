import java.awt.*;

public class DialogManager extends Dialog {
    private Label LDialog;
    private Font FDialog;

    public DialogManager(String username, CreateNewDatabaseWindow DatabaseDialog, int timer) {
        super(DatabaseDialog, username, true);
        timeOut(timer);
        addDialog(username);
        setDefaults();
    }

    public DialogManager(String username, CreateNewDatabaseWindow DatabaseDialog) {
        super(DatabaseDialog, username, true);
        timeOut();
        addDialog(username);
        setDefaults();
    }

    public DialogManager(String username, MainWindow mainWindow) {
        super(mainWindow, username, true);
        timeOut();
        addDialog(username);
        setDefaults();
    }

    public DialogManager(String username, JournalApp JournalWindow) {
        super(JournalWindow, username, true);
        timeOut();
        addDialog(username);
        setDefaults();
    }

    private void timeOut() {
        new Thread(() -> {
            try {
                System.out.println("Thread working...");
                Thread.sleep(2000);
                setVisible(false);
                dispose();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }

    private void timeOut(int timer) {
        new Thread(() -> {
            try {
                System.out.println("Thread working...");
                Thread.sleep(timer);
                setVisible(false);
                dispose();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }

    private void addDialog(String username) {
        FDialog = new Font(Font.SERIF, Font.ITALIC, 25);
        setLayout(new FlowLayout(FlowLayout.CENTER, 10, 60));
        LDialog = new Label(username);
        LDialog.setFont(FDialog);
        add(LDialog);
    }

    private void setDefaults() {
        setSize(600, 200);
        setLocation(525, 375);
        setVisible(true);
    }
}
