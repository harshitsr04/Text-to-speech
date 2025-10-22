package com.example.voice;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

public class VoiceGUI extends JFrame {
    private JTextField synthTextField;
    private JTextField synthOutField;
    private JTextField transcribeInField;
    private JTextArea transcribeResultArea;

    public VoiceGUI() {
        setTitle("Voice (Google Cloud) - GUI");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 400);
        setLocationRelativeTo(null);

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Synthesize", buildSynthesizePanel());
        tabs.addTab("Transcribe", buildTranscribePanel());

        add(tabs);
    }

    private JPanel buildSynthesizePanel() {
        JPanel panel = new JPanel(new BorderLayout(8, 8));

        JPanel form = new JPanel(new GridLayout(3, 1, 6, 6));
        synthTextField = new JTextField("Hello from GUI");
        synthOutField = new JTextField(System.getProperty("user.home") + File.separator + "Desktop" + File.separator + "tts_out.mp3");

        form.add(labeled("Text to speak:", synthTextField));
        form.add(labeled("Output file:", synthOutField));

        JButton synthBtn = new JButton("Synthesize");
        synthBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                synthBtn.setEnabled(false);
                try {
                    Synthesize.synthesizeText(synthTextField.getText(), synthOutField.getText());
                    JOptionPane.showMessageDialog(VoiceGUI.this, "Wrote audio to: " + synthOutField.getText());
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(VoiceGUI.this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                } finally {
                    synthBtn.setEnabled(true);
                }
            }
        });

        panel.add(form, BorderLayout.CENTER);
        panel.add(synthBtn, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel buildTranscribePanel() {
        JPanel panel = new JPanel(new BorderLayout(8, 8));

        transcribeInField = new JTextField(System.getProperty("user.home") + File.separator + "Desktop" + File.separator + "input.wav");
        JButton pickBtn = new JButton("Browse...");
        pickBtn.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            int res = chooser.showOpenDialog(VoiceGUI.this);
            if (res == JFileChooser.APPROVE_OPTION) {
                transcribeInField.setText(chooser.getSelectedFile().getAbsolutePath());
            }
        });

        JPanel top = new JPanel(new BorderLayout(6,6));
        top.add(transcribeInField, BorderLayout.CENTER);
        top.add(pickBtn, BorderLayout.EAST);

        JButton transcribeBtn = new JButton("Transcribe");
        transcribeResultArea = new JTextArea();
        transcribeResultArea.setLineWrap(true);
        transcribeResultArea.setWrapStyleWord(true);

        transcribeBtn.addActionListener(e -> {
            transcribeBtn.setEnabled(false);
            transcribeResultArea.setText("Working...");
            try {
                String text = Transcribe.transcribeFile(transcribeInField.getText());
                transcribeResultArea.setText(text);
            } catch (IOException ex) {
                transcribeResultArea.setText("Error: " + ex.getMessage());
            } finally {
                transcribeBtn.setEnabled(true);
            }
        });

        panel.add(top, BorderLayout.NORTH);
        panel.add(new JScrollPane(transcribeResultArea), BorderLayout.CENTER);
        panel.add(transcribeBtn, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel labeled(String label, JComponent comp) {
        JPanel p = new JPanel(new BorderLayout(6,6));
        p.add(new JLabel(label), BorderLayout.WEST);
        p.add(comp, BorderLayout.CENTER);
        return p;
    }

    public static void showGui() {
        SwingUtilities.invokeLater(() -> {
            VoiceGUI gui = new VoiceGUI();
            gui.setVisible(true);
        });
    }
}
