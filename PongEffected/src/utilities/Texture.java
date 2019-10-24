package utilities;

/*
 * By Luka Kolic 2019
 * Image loading from file and Texture creating class
 * 
 */

import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.lwjgl.opengl.GL11.*;
import javax.imageio.ImageIO;

public class Texture {
	private int width, height;
	private int textureID;
	
	
	public Texture(String path)
	{
		textureID = load(path);
	}
	
	//Load from File
	private int load(String path) {
		int[] pixels = null;
		InputStream fs = getClass().getResourceAsStream(path);
		try {
			BufferedImage image = ImageIO.read(fs);
			width = image.getWidth();
			height = image.getHeight();
			pixels = new int[width * height];
			image.getRGB(0, 0, width, height, pixels, 0, width);
			
		}catch (IOException e) {
			e.printStackTrace();
		}
		int data[] = new int [width * height];
		for(int i = 0; i < data.length; i++) {
			
			int a = (pixels[i] & 0xff000000) >> 24;
			int r = (pixels[i] & 0xff0000) >> 16;
			int g = (pixels[i] & 0xff00) >> 8;
			int b = (pixels[i] & 0xff);
			
			
			
			data[i] = a << 24 | b << 16 | g << 8 | r;
		}
		
		int result = glGenTextures();
		glBindTexture(GL_TEXTURE_2D, result);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, BufferUtilities.createIntBuffer(data));

		return result;
	}
	
	public void Bind() {
		glBindTexture(GL_TEXTURE_2D, textureID);
	}
	
	public void Unbind() {
		glBindTexture(GL_TEXTURE_2D, 0);
	}
	
	public void Clean() {
		glDeleteTextures(textureID);
	}
}
