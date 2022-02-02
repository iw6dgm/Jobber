package it.deepnet.joshua.job;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;

/**
 * @author Mcamangi
 */
public class Note extends JFrame {

    static final long serialVersionUID = 0L;

    //private Connection c      = null;
    private int user_event_id = -1;
    private String notes = "<Empty>";

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
            aclear = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            note.setText("");
        }
    },
            aupdate = new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    saveNote(user_event_id, note.getText());
                    //setVisible(false);
                    dispose();
                }
            },
            areload = new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    refreshNote();
                }
            };

    private void loadNote(int id) {
        notes = engine.getNote(id);
    }

    private void saveNote(int id, String data) {
        if (data != null) {
            try {
                engine.updateNote(id, data);

            } catch (Exception e) {
                Job.logger.log(Level.SEVERE, "{Note}", e);
            }
        }
    }

    private void refreshNote() {

        try {
            loadNote(user_event_id);
            note.setText(notes);
        } catch (Exception e3) {
            Job.logger.log(Level.SEVERE, "{Note}", e3);
        }

    }

    public Note(int id) {

        user_event_id = id;
        cp = getContentPane();

        cp.setLayout(new GridLayout(2, 1));

//		title.setLayout(new FlowLayout(FlowLayout.CENTER));
//		title.add(new JLabel("Update notes"));
//		row1.add(title);

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


        Job.logger.log(Level.FINE, "Loading notes for user event id " + user_event_id);
        loadNote(user_event_id);
        note.setText(notes);

    }
}
