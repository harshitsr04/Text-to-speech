package com.example.voice;

import com.google.cloud.speech.v1.RecognitionAudio;
import com.google.cloud.speech.v1.RecognitionConfig;
import com.google.cloud.speech.v1.RecognizeResponse;
import com.google.cloud.speech.v1.SpeechClient;
import com.google.cloud.speech.v1.SpeechRecognitionAlternative;
import com.google.cloud.speech.v1.SpeechRecognitionResult;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Transcribe {
    // Transcribes a local audio file (LINEAR16 / WAV) to text.
    public static String transcribeFile(String filePath) throws IOException {
        byte[] data = Files.readAllBytes(Path.of(filePath));

        RecognitionAudio audio = RecognitionAudio.newBuilder()
                .setContent(com.google.protobuf.ByteString.copyFrom(data))
                .build();

        RecognitionConfig config = RecognitionConfig.newBuilder()
                .setEncoding(RecognitionConfig.AudioEncoding.LINEAR16)
                .setSampleRateHertz(16000)
                .setLanguageCode("en-US")
                .build();

        try (SpeechClient speechClient = SpeechClient.create()) {
            RecognizeResponse response = speechClient.recognize(config, audio);
            StringBuilder result = new StringBuilder();
            for (SpeechRecognitionResult res : response.getResultsList()) {
                SpeechRecognitionAlternative alt = res.getAlternativesList().get(0);
                result.append(alt.getTranscript()).append("\n");
            }
            return result.toString().trim();
        }
    }
}
