package it.deepnet.joshua.job;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Mcamangi
 */
public class Note extends JFrame {

    private static final Logger logger = Logger.getLogger(Note.class.getSimpleName());

    static final long serialVersionUID = 0L;

    //private Connection c      = null;
    private final String user_id;
    private final String project_id;
    private final int user_event_id;

    Container cp;
    //JPanel title = new JPanel();
    JPanel row1 = new JPanel();

    JPanel row2 = new JPanel();
    JButton
            jbupdate = new JButton("Update"),
            jbcancel = new JButton("Clear"),
            jbreload = new JButton("Reload");

    JTextArea
            note = new JTextArea(5, 25);

    Engine engine = new Engine();

    ActionListener
            aclear = e -> note.setText(""),
            aupdate = e -> {
                saveNote(note.getText());
                //setVisible(false);
                dispose();
            },
            areload = e -> refreshNote();

    private void saveNote(final String data) {
        if (data != null) {
            try {
                engine.updateNote(this.user_id, this.project_id, this.user_event_id, data);
            } catch (Exception e) {
                logger.log(Level.SEVERE, "{Note}", e);
            }
        }
    }

    private void refreshNote() {

        try {
            note.setText(engine.getNote(user_event_id));
        } catch (Exception e3) {
            logger.log(Level.SEVERE, "{Note}", e3);
        }

    }

    public Note(String user_id, String project_id, int id) {

        this.user_id = user_id;
        this.project_id = project_id;
        this.user_event_id = id;
        cp = getContentPane();

        cp.setLayout(new GridLayout(2, 1));

        final JPanel buttonsEntry = new JPanel();
        buttonsEntry.setLayout(new FlowLayout(FlowLayout.CENTER));
        buttonsEntry.add(jbupdate);
        buttonsEntry.add(jbcancel);
        buttonsEntry.add(jbreload);
        row1.add(buttonsEntry);
        cp.add(row1);

        row2.setLayout(new FlowLayout(FlowLayout.CENTER));
        row2.add(new JScrollPane(note));
        cp.add(row2);

        note.setEditable(true);
        note.setWrapStyleWord(true);
        note.setLineWrap(true);

        jbcancel.addActionListener(aclear);
        jbupdate.addActionListener(aupdate);
        jbreload.addActionListener(areload);


        logger.log(Level.FINE, "Loading notes for user event id " + user_event_id);
        note.setText(engine.getNote(user_event_id));
    }
}
