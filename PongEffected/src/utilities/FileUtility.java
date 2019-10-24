package utilities;

/*
 * By Luka Kolic 2019
 * File reading class
 * It reads whole text from file
 * 
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


public class FileUtility {

	
	public static String loadFromFile(String fileName)
	{
		StringBuilder builder = new StringBuilder();
		InputStream is = FileUtility.class.getResourceAsStream(fileName);
		try {
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(is));
			String line = "";
			while((line = reader.readLine()) != null)
			{
				builder.append(line + '\n');			
			}
			
			reader.close();
		
		}catch (IOException e) {
			e.printStackTrace();
			System.err.println("Could not find file: " + fileName);
			return null;
		}
		
		return builder.toString();
	}
	
	
}
