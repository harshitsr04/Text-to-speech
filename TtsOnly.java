package com.example.voice;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.texttospeech.v1.TextToSpeechClient;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;

public class TtsOnly {
    public static void main(String[] args) throws IOException {
        if (args.length < 2) {
            System.out.println("Usage: java -cp <jar> com.example.voice.TtsOnly <text> <output.mp3> [--creds C:\\path\\to\\key.json]");
            return;
        }

        String text = args[0];
        String out = args[1];
        String creds = null;
        for (int i = 2; i < args.length - 1; i++) {
            if ("--creds".equals(args[i])) {
                creds = args[i + 1];
            }
        }

        if (creds != null) {
            // Load credentials and set the environment variable for the client libraries
            GoogleCredentials gc = ServiceAccountCredentials.fromStream(new FileInputStream(creds));
            // Unfortunately the Java client libs read GOOGLE_APPLICATION_CREDENTIALS from env or default application creds.
            // As a lightweight approach, we set the env var for this process (PowerShell must set it too for child processes).
            System.setProperty("GOOGLE_APPLICATION_CREDENTIALS", creds);
        }

        // Reuse existing Synthesize class
        Synthesize.synthesizeText(text, out);
        System.out.println("Wrote audio to: " + Path.of(out).toAbsolutePath());
    }
}
