/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package clock.controller;

import clock.controller.ScheduleDate.DayOfWeek;
import java.awt.Dialog;
import java.awt.FileDialog;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 *
 * @author joshualange
 */
public class ScheduleWindow extends javax.swing.JFrame implements
        CopyPasteButtonListener, ScheduleEventListener {

    public static int NUM_ENTRIES = 40;
    JPanel[] weeks;
    String[] daysOfWeek;
    TimeControl[][] scheduleTimes;
    CopyPasteButtons[] copyButtons;
    int toCopy = -1;//stores which column's copy button is pressed
    private Integer currentOutput = 1;
    private final int defaultRingTime = 5;//seconds
    MainWindow mw;

    boolean waitingForEvents = false;

    private ClockProtocolCoder theProtocolCoder;

    /**
     * Get the value of theProtocolCoder
     *
     * @return the value of theProtocolCoder
     */
    public ClockProtocolCoder getTheProtocolCoder() {
        return theProtocolCoder;
    }

    /**
     * Set the value of theProtocolCoder
     *
     * @param theProtocolCoder new value of theProtocolCoder
     */
    public void setTheProtocolCoder(ClockProtocolCoder theProtocolCoder) {
        this.theProtocolCoder = theProtocolCoder;
        this.theProtocolCoder.setTheScheduleListener(this);
    }

    public void setMw(MainWindow mw) {
        this.mw = mw;
    }

    /**
     * Creates new form ScheduleWindow
     */
    public ScheduleWindow() {
        //list of the days of the week
        this.daysOfWeek = new String[]{"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};

        //init the array of schedule columns for the days of the week
        weeks = new JPanel[7];
        copyButtons = new CopyPasteButtons[7];
        //init the array of time controls
        scheduleTimes = new TimeControl[7][NUM_ENTRIES];

        //netbeans provided method
        initComponents();
        doneLabel.setVisible(false);
        //add column layout manager for main view
        weekView.setLayout(new BoxLayout(weekView, BoxLayout.X_AXIS));
        //build the columns of controls
        for (int i = 0; i < weeks.length; i++) {
            //init and configure the JPanels that form columns
            weeks[i] = new JPanel();
            //init and configure copy paste buttons
            copyButtons[i] = new CopyPasteButtons();
            copyButtons[i].setListener(this);

            weeks[i].setBorder(BorderFactory.createTitledBorder(daysOfWeek[i]));
            weeks[i].setLayout(new BoxLayout(weeks[i], BoxLayout.Y_AXIS));
            //add copy paste buttons to top of list
            weeks[i].add(copyButtons[i]);

            //add the rows of time controls to the columns
            for (int j = 0; j < NUM_ENTRIES; j++) {
                scheduleTimes[i][j] = new TimeControl();
                weeks[i].add(scheduleTimes[i][j]);

            }
            //after the column is built, add it to the main view
            weekView.add(weeks[i]);
        }

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        loadScheduleFileButton = new javax.swing.JButton();
        saveScheduleFileButton = new javax.swing.JButton();
        downloadScheduleClockButton = new javax.swing.JButton();
        uploadScheduleClockButton = new javax.swing.JButton();
        weekScrollView = new javax.swing.JScrollPane();
        weekView = new javax.swing.JPanel();
        advancedButton = new javax.swing.JButton();
        downloadProgressBar = new javax.swing.JProgressBar();
        jLabel1 = new javax.swing.JLabel();
        bellTimeSpinner = new javax.swing.JSpinner();
        jLabel2 = new javax.swing.JLabel();
        clearScheduleButton = new javax.swing.JButton();
        doneLabel = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Schedule Editor");
        setMinimumSize(new java.awt.Dimension(1024, 400));
        setSize(new java.awt.Dimension(1200, 400));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        loadScheduleFileButton.setText("Load Schedule From File");
        loadScheduleFileButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loadScheduleFileButtonActionPerformed(evt);
            }
        });

        saveScheduleFileButton.setText("Save Schedule To File");
        saveScheduleFileButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveScheduleFileButtonActionPerformed(evt);
            }
        });

        downloadScheduleClockButton.setText("Download Schedule From Clock");
        downloadScheduleClockButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                downloadScheduleClockButtonActionPerformed(evt);
            }
        });

        uploadScheduleClockButton.setText("Upload Schedule To Clock");
        uploadScheduleClockButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                uploadScheduleClockButtonActionPerformed(evt);
            }
        });

        weekScrollView.setBackground(new java.awt.Color(204, 204, 204));

        org.jdesktop.layout.GroupLayout weekViewLayout = new org.jdesktop.layout.GroupLayout(weekView);
        weekView.setLayout(weekViewLayout);
        weekViewLayout.setHorizontalGroup(
            weekViewLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 1019, Short.MAX_VALUE)
        );
        weekViewLayout.setVerticalGroup(
            weekViewLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 414, Short.MAX_VALUE)
        );

        weekScrollView.setViewportView(weekView);

        advancedButton.setText("Advanced...");
        advancedButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                advancedButtonActionPerformed(evt);
            }
        });

        jLabel1.setText("Bell Length:");

        bellTimeSpinner.setModel(new javax.swing.SpinnerNumberModel(5, 1, 99, 1));

        jLabel2.setText("Seconds");

        clearScheduleButton.setText("Clear Schedule");
        clearScheduleButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearScheduleButtonActionPerformed(evt);
            }
        });

        doneLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        doneLabel.setText("Done");

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(weekScrollView)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(layout.createSequentialGroup()
                                .add(loadScheduleFileButton)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(clearScheduleButton))
                            .add(layout.createSequentialGroup()
                                .add(saveScheduleFileButton)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(advancedButton)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(jLabel1)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(bellTimeSpinner, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(jLabel2)))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .add(doneLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 100, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                                .add(6, 6, 6)
                                .add(uploadScheduleClockButton))
                            .add(layout.createSequentialGroup()
                                .add(downloadProgressBar, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(downloadScheduleClockButton)))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(weekScrollView, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 417, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                        .add(loadScheduleFileButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 29, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(downloadScheduleClockButton)
                        .add(clearScheduleButton))
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                        .add(org.jdesktop.layout.GroupLayout.TRAILING, doneLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .add(org.jdesktop.layout.GroupLayout.TRAILING, downloadProgressBar, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(saveScheduleFileButton)
                    .add(uploadScheduleClockButton)
                    .add(advancedButton)
                    .add(jLabel1)
                    .add(bellTimeSpinner, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel2))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void loadScheduleFileButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loadScheduleFileButtonActionPerformed
        if (this.confifirmAndClearSchedule()) {
            File openFile = this.selectFile("Untitled Schedule.csv", "Load Schedule From File", FileDialog.LOAD);
            if (openFile != null) {
                try {
                    BufferedReader bReader = new BufferedReader(new FileReader(openFile));
                    bReader.readLine();//Skip column header line
                    while (true) {
                        String line = bReader.readLine();
                        if (line != null) {
                            String[] subStrings = line.split(",");
                            int dayOfWeek = Integer.parseInt(subStrings[0]);
                            int hour = Integer.parseInt(subStrings[1]);
                            int minute = Integer.parseInt(subStrings[2]);
                            this.addNewScheduleToGrid(new ScheduleDate(currentOutput, defaultRingTime,
                                    columnToDayOfWeek(dayOfWeek), hour, minute));
                        } else {
                            break;
                        }
                    }
                    doneLabel.setVisible(false);
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(ScheduleWindow.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(ScheduleWindow.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }//GEN-LAST:event_loadScheduleFileButtonActionPerformed

    private void saveScheduleFileButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveScheduleFileButtonActionPerformed
        File saveFile = this.selectFile("Untitled Schedule.csv", "Save Schedule To File", FileDialog.SAVE);
        if (saveFile != null) {
            try {
                BufferedWriter bWriter = new BufferedWriter(new FileWriter(saveFile));
                bWriter.append("DOW,H,M,Len\n");
                Date theDate;
                SimpleDateFormat sdf = new SimpleDateFormat(",H,m,");
                for (int i = 0; i < weeks.length; i++) {
                    for (int j = 0; j < NUM_ENTRIES; j++) {
                        if (scheduleTimes[i][j].getEnabled()) {
                            theDate = scheduleTimes[i][j].getTime();
                            bWriter.append(Integer.toString(i));//column
                            bWriter.append(sdf.format(theDate));
                            //bWriter.append(scheduleTimes[i][j].getLength)
                            bWriter.append("\n");
                        }
                    }
                }
                bWriter.close();
            } catch (IOException ex) {
                Logger.getLogger(ScheduleWindow.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
            }
        }
    }//GEN-LAST:event_saveScheduleFileButtonActionPerformed

    private void advancedButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_advancedButtonActionPerformed
//SpinnerNumberModel sModel = new SpinnerNumberModel(1, 1, 8, 1);
//JSpinner spinner = new JSpinner(sModel);
//JOptionPane.showMessageDialog(null, spinner);
        Integer[] values = {1, 2, 3, 4, 5, 6, 7, 8};
        Integer option = (Integer) JOptionPane.showInputDialog(null, "Specify the Bell Output Number. \n"
                + "Only change this if you know what you're doing!", "Advanced Options", JOptionPane.QUESTION_MESSAGE, null, values, currentOutput);
        if (option == null) {
            // user hit cancel
        } else {
            this.currentOutput = option;
        }

    }//GEN-LAST:event_advancedButtonActionPerformed

    private void downloadScheduleClockButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_downloadScheduleClockButtonActionPerformed
        if (this.theProtocolCoder != null) {
            if (this.confifirmAndClearSchedule()) {
                downloadProgressBar.setIndeterminate(true);
                this.theProtocolCoder.requestFirstScheduleProgramFromClock();
            }
        }
    }//GEN-LAST:event_downloadScheduleClockButtonActionPerformed

    private void uploadScheduleClockButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_uploadScheduleClockButtonActionPerformed
        int confirm = JOptionPane.showConfirmDialog(this, "This action"
                + " will replace the current schedule in the clock system.\n "
                + "The new schedule will take effect immediately.", "Are You Sure?",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);
        if (confirm == JOptionPane.OK_OPTION) {
            //do some quick calculations to set up the progress bar
            int total = 0;
            for (int i = 0; i < weeks.length; i++) {
                total += getUsedRowCountForColumn(i);
            }
            downloadProgressBar.setMaximum(total);
            downloadProgressBar.setMinimum(0);
            doneLabel.setVisible(true);
            doneLabel.setText("Uploading...");
            uploadScheduleClockButton.setEnabled(false);
            downloadScheduleClockButton.setEnabled(false);
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    int count = 0;
                    theProtocolCoder.deleteAllSchedulePrograms();//first, clear out all the old schedules
                    for (int i = 0; i < weeks.length; i++) {
                        for (int j = 0; j < NUM_ENTRIES; j++) {
                            downloadProgressBar.setValue(count);
                            downloadProgressBar.getParent().repaint();
                            count++;
                            if (scheduleTimes[i][j].getEnabled()) {
                                GregorianCalendar cc = new GregorianCalendar();
                                cc.setTime(scheduleTimes[i][j].getTime());
                                theProtocolCoder.sendScheduleProgram((i + j), cc.get(GregorianCalendar.HOUR_OF_DAY),
                                        cc.get(GregorianCalendar.MINUTE), columnToDayOfWeek(i), currentOutput,
                                        (Integer) bellTimeSpinner.getValue());
                                try {
                                    Thread.sleep(75);
                                } catch (InterruptedException ex) {
                                    Logger.getLogger(ScheduleWindow.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }
                        }
                    }
                    downloadProgressBar.setValue(downloadProgressBar.getMinimum());
                    doneLabel.setText("Done.");
                    uploadScheduleClockButton.setEnabled(true);
                    downloadScheduleClockButton.setEnabled(true);
                }
            });

        }
    }//GEN-LAST:event_uploadScheduleClockButtonActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        if (mw != null) {
            mw.setVisible(true);
        }
    }//GEN-LAST:event_formWindowClosing

    private void clearScheduleButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearScheduleButtonActionPerformed
        confifirmAndClearSchedule();
    }//GEN-LAST:event_clearScheduleButtonActionPerformed

    private boolean confifirmAndClearSchedule() {

        int confirm = JOptionPane.showConfirmDialog(this, "This action"
                + " will clear the currently entered schedule.", "Are You Sure?",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);
        if (confirm == JOptionPane.OK_OPTION) {
            doneLabel.setVisible(false);
            for (int i = 0; i < weeks.length; i++) {
                for (int j = 0; j < NUM_ENTRIES; j++) {
                    scheduleTimes[i][j].setEnabled(false);
                    scheduleTimes[i][j].setTime(new Date());
                }
            }
        }
        return confirm == JOptionPane.OK_OPTION;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /*
         * Set the Nimbus look and feel
         
         //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
         /*
         * If Nimbus (introduced in Java SE 6) is not available, stay with the
         * default look and feel. For details see
         * http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ScheduleWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ScheduleWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ScheduleWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ScheduleWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /*
         * Create and display the form
         */
        java.awt.EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                new ScheduleWindow().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton advancedButton;
    private javax.swing.JSpinner bellTimeSpinner;
    private javax.swing.JButton clearScheduleButton;
    private javax.swing.JLabel doneLabel;
    private javax.swing.JProgressBar downloadProgressBar;
    private javax.swing.JButton downloadScheduleClockButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JButton loadScheduleFileButton;
    private javax.swing.JButton saveScheduleFileButton;
    private javax.swing.JButton uploadScheduleClockButton;
    private javax.swing.JScrollPane weekScrollView;
    private javax.swing.JPanel weekView;
    // End of variables declaration//GEN-END:variables

    @Override
    public void copyClicked(CopyPasteButtons source) {
        //find out which column was clicked
        for (int i = 0; i < copyButtons.length; i++) {
            copyButtons[i].setPasteButtonEnabled(true);
            if (source == copyButtons[i]) {
                toCopy = i;
                //break;
            }

        }
    }

    @Override
    public void pasteClicked(CopyPasteButtons source, boolean clear) {

        int dest = -1;
        //find out which column was clicked
        for (int i = 0; i < copyButtons.length; i++) {
            if (source == copyButtons[i]) {
                dest = i;
                break;
            }
        }
        if (dest >= 0 && dest < scheduleTimes.length) {
            if (clear) {
                Date d = new Date();
                for (int j = 0; j < scheduleTimes[dest].length; j++) {
                    scheduleTimes[dest][j].setTime(d);
                    scheduleTimes[dest][j].setEnabled(false);
                }
            } else {
                if (toCopy >= 0) {
                    for (int j = 0; j < scheduleTimes[dest].length; j++) {
                        scheduleTimes[dest][j].setTime(scheduleTimes[toCopy][j].getTime());
                        scheduleTimes[dest][j].setEnabled(scheduleTimes[toCopy][j].getEnabled());
                    }
                }
            }
        }
    }

    @Override
    public void eventReceived(int id, ScheduleDate date) {
        if (date.getOutputNum() == this.currentOutput) {
            this.addNewScheduleToGrid(date);
        }
        theProtocolCoder.requestNextScheduleProgramFromClock();
    }

    @Override
    public void lastEventReceived() {

        downloadProgressBar.setIndeterminate(false);
    }

    private File selectFile(String name, String dlgTitle, int type) {
        FileDialog fd = new FileDialog(this, dlgTitle, type);
        fd.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        if (type == FileDialog.LOAD) {
            fd.setFilenameFilter(new FilenameFilter() {

                @Override
                public boolean accept(File dir, String name) {
                    return name.endsWith(".csv");
                }
            });
        }
        fd.setUndecorated(true);
        fd.setFile(name);
        fd.setVisible(true);
        return (fd.getDirectory() == null) ? null : new File(fd.getDirectory(), fd.getFile());
    }

    private int getNextFreeRowInColumn(int column) {
        for (int i = 0; i < NUM_ENTRIES; i++) {
            if (scheduleTimes[column][i].getEnabled() == false) {
                return i;
            }
        }
        return NUM_ENTRIES - 1;//not a great solution
    }

    private int getUsedRowCountForColumn(int column) {
        int n = 0;
        for (int i = 0; i < NUM_ENTRIES; i++) {
            if (scheduleTimes[column][i].getEnabled() == false) {
                n++;
            }
        }
        return n;
    }

    private void addNewScheduleToGrid(ScheduleDate d) {
        switch (d.getDayOfWeek()) {
            case MONDAY:
            case TUESDAY:
            case WEDNESDAY:
            case THURSDAY:
            case FRIDAY:
            case SATURDAY:
            case SUNDAY:
                int col = dayOfWeekToColumn(d.getDayOfWeek());
                int row = this.getNextFreeRowInColumn(col);
                scheduleTimes[col][row].setEnabled(true);
                GregorianCalendar calDate = new GregorianCalendar();
                calDate.set(GregorianCalendar.HOUR_OF_DAY, d.getHour());
                calDate.set(GregorianCalendar.MINUTE, d.getMinute());
                scheduleTimes[col][row].setTime(calDate.getTime());
                break;
            case SATURDAY_AND_SUNDAY:
                addNewScheduleToGrid(d.modDayOfWeek(DayOfWeek.SATURDAY));
                addNewScheduleToGrid(d.modDayOfWeek(DayOfWeek.SUNDAY));
                break;
            case MONDAY_FRIDAY:
                addNewScheduleToGrid(d.modDayOfWeek(DayOfWeek.MONDAY));
                addNewScheduleToGrid(d.modDayOfWeek(DayOfWeek.TUESDAY));
                addNewScheduleToGrid(d.modDayOfWeek(DayOfWeek.WEDNESDAY));
                addNewScheduleToGrid(d.modDayOfWeek(DayOfWeek.THURSDAY));
                addNewScheduleToGrid(d.modDayOfWeek(DayOfWeek.FRIDAY));
                break;
            case ALL_THE_DAYS:
                addNewScheduleToGrid(d.modDayOfWeek(DayOfWeek.MONDAY));
                addNewScheduleToGrid(d.modDayOfWeek(DayOfWeek.TUESDAY));
                addNewScheduleToGrid(d.modDayOfWeek(DayOfWeek.WEDNESDAY));
                addNewScheduleToGrid(d.modDayOfWeek(DayOfWeek.THURSDAY));
                addNewScheduleToGrid(d.modDayOfWeek(DayOfWeek.FRIDAY));
                addNewScheduleToGrid(d.modDayOfWeek(DayOfWeek.SATURDAY));
                addNewScheduleToGrid(d.modDayOfWeek(DayOfWeek.SUNDAY));
                break;
            default:
                throw new AssertionError();
        }

    }

    private int dayOfWeekToColumn(DayOfWeek dow) {
        switch (dow) {
            case MONDAY:
                return 0;
            case TUESDAY:
                return 1;
            case WEDNESDAY:
                return 2;
            case THURSDAY:
                return 3;
            case FRIDAY:
                return 4;
            case SATURDAY:
                return 5;
            case SUNDAY:
                return 6;
            case SATURDAY_AND_SUNDAY:
                return -1;
            case MONDAY_FRIDAY:
                return -2;
            case ALL_THE_DAYS:
                return -3;
            default:
                throw new AssertionError();
        }
    }

    private DayOfWeek columnToDayOfWeek(int column) {
        switch (column) {
            case 0:
                return DayOfWeek.MONDAY;
            case 1:
                return DayOfWeek.TUESDAY;
            case 2:
                return DayOfWeek.WEDNESDAY;
            case 3:
                return DayOfWeek.THURSDAY;
            case 4:
                return DayOfWeek.FRIDAY;
            case 5:
                return DayOfWeek.SATURDAY;
            case 6:
                return DayOfWeek.SUNDAY;
            case -1:
                return DayOfWeek.SATURDAY_AND_SUNDAY;
            case -2:
                return DayOfWeek.MONDAY_FRIDAY;
            case -3:
                return DayOfWeek.ALL_THE_DAYS;
            default:
                throw new AssertionError();
        }

    }
}