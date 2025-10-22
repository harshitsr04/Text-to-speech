package com.example.voice;

import com.google.cloud.texttospeech.v1.AudioConfig;
import com.google.cloud.texttospeech.v1.AudioEncoding;
import com.google.cloud.texttospeech.v1.SsmlVoiceGender;
import com.google.cloud.texttospeech.v1.SynthesisInput;
import com.google.cloud.texttospeech.v1.TextToSpeechClient;
import com.google.cloud.texttospeech.v1.VoiceSelectionParams;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;

public class Synthesize {
    // Synthesizes natural-sounding speech from the input string of text.
    public static Path synthesizeText(String text, String outputFile) throws IOException {
        try (TextToSpeechClient ttsClient = TextToSpeechClient.create()) {
            SynthesisInput input = SynthesisInput.newBuilder().setText(text).build();

            VoiceSelectionParams voice = VoiceSelectionParams.newBuilder()
                    .setLanguageCode("en-US")
                    .setSsmlGender(SsmlVoiceGender.NEUTRAL)
                    .build();

            AudioConfig audioConfig = AudioConfig.newBuilder()
                    .setAudioEncoding(AudioEncoding.MP3)
                    .build();

            com.google.cloud.texttospeech.v1.SynthesizeSpeechResponse response = ttsClient.synthesizeSpeech(input, voice, audioConfig);

            byte[] audioContents = response.getAudioContent().toByteArray();

            Path outPath = Path.of(outputFile);
            try (FileOutputStream out = new FileOutputStream(outPath.toFile())) {
                out.write(audioContents);
            }

            return outPath;
        }
    }
}
