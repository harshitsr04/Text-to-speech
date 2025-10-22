package com.example.voice;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        if (args.length == 0) {
            System.out.println("Usage:");
            System.out.println("  java -jar voice.jar transcribe <input.wav>");
            System.out.println("  java -jar voice.jar synthesize \"text to speak\" <output.mp3>");
            System.out.println("  java -jar voice.jar gui    # open Swing GUI");
            return;
        }

        String cmd = args[0];
        if ("transcribe".equalsIgnoreCase(cmd)) {
            if (args.length < 2) {
                System.err.println("transcribe requires an input file");
                return;
            }
            String input = args[1];
            String text = Transcribe.transcribeFile(input);
            System.out.println("Transcription:\n" + text);
        } else if ("synthesize".equalsIgnoreCase(cmd)) {
            if (args.length < 3) {
                System.err.println("synthesize requires text and output file");
                return;
            }
            String text = args[1];
            String out = args[2];
            Synthesize.synthesizeText(text, out);
            System.out.println("Wrote audio to: " + out);
        } else if ("gui".equalsIgnoreCase(cmd)) {
            VoiceGUI.showGui();
        } else {
            System.err.println("Unknown command: " + cmd);
        }
    }
}
