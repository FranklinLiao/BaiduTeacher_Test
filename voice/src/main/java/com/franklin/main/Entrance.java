package com.franklin.main;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import com.franklin.voice.VoiceDeal;

public class Entrance {
	public static void main(String args[]) {
		VoiceDeal voiceDeal = VoiceDeal.getInstance();
		String filePath = "src/main/resources/audio/RecordAudio.wav";
		File file = new File(filePath);
		try {
			System.out.println("path:" + file.getAbsolutePath());
			System.out.println(voiceDeal.getVoice(file));
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
}
