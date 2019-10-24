package utilities;

/*
 * By Luka Kolic 2019
 * Shader class for loading shaders for dispplaying graphics and other effects
 * 
 */



import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.*;

import java.util.HashMap;
import java.util.Map;

import maths.Matrix4f;
import maths.Vector2f;
import maths.Vector4f;


public class Shader{
	
	public int programID = 0;
	private String vertexSource, fragmentSource;
	private static Map<String, Integer> locationCache = new HashMap<String, Integer>();
	
	
	public Shader(String vertexFile, String fragmentFile)
	{
		//Getting text read from file
		vertexSource = FileUtility.loadFromFile(vertexFile);
		fragmentSource = FileUtility.loadFromFile(fragmentFile);
		/////////////////////////////
		
		//Initializing shaders
	
		final int vertexID = glCreateShader(GL_VERTEX_SHADER);
		final int fragmentID = glCreateShader(GL_FRAGMENT_SHADER);
		programID = glCreateProgram();
		
		glShaderSource(vertexID, vertexSource);
		glShaderSource(fragmentID, fragmentSource);
		
		glCompileShader(vertexID);
		if(glGetShaderi(vertexID, GL_COMPILE_STATUS) == GL_FALSE)
		{
			System.err.println("Failed to compile vertex shader!");
			System.err.println(glGetShaderInfoLog(vertexID));
		}
		
		glCompileShader(fragmentID);
		if(glGetShaderi(vertexID, GL_COMPILE_STATUS) == GL_FALSE)
		{
			System.err.println("Failed to compile fragment shader!");
			System.err.println(glGetShaderInfoLog(vertexID));
		}
		
		
		glAttachShader(programID, vertexID);
		glAttachShader(programID, fragmentID);
		glBindAttribLocation(programID, 0, "pos");
		glBindAttribLocation(programID, 1, "tc");

		
		glLinkProgram(programID);
		if(glGetProgrami(programID, GL_LINK_STATUS) == GL_FALSE)
		{
			System.err.println("Failed to link program!");
			System.err.println(glGetProgramInfoLog(programID));
		}
		
		glDeleteShader(vertexID);
		glDeleteShader(fragmentID);
	}
	
	
	public void setUnfirom1i(String uniformName, int data) {
		int result = 0;
		if(locationCache.containsKey(uniformName))
		{
			result = locationCache.get(uniformName);
		}
		else {
			result = glGetUniformLocation(programID, uniformName);
		}
		glUniform1i(result, data);
		
	}
	
	public void setUnfirom2f(String uniformName, Vector2f vector) {
		int result = 0;
		if(locationCache.containsKey(uniformName))
		{
			result = locationCache.get(uniformName);
		}
		else {
			result = glGetUniformLocation(programID, uniformName);
		}
		glUniform2f(result, vector.x, vector.y);
		
	}
	
	public void setUnfirom4f(String uniformName, Vector4f vector) {
		int result = 0;
		if(locationCache.containsKey(uniformName))
		{
			result = locationCache.get(uniformName);
		}
		else {
			result = glGetUniformLocation(programID, uniformName);
		}
		glUniform4f(result, vector.x, vector.y, vector.z, vector.w);
		
	}
	
	public void setMatrix4f(String uniformName, Matrix4f matrix) {
		int result = 0;
		if(locationCache.containsKey(uniformName))
		{
			result = locationCache.get(uniformName);
		}
		else {
			result = glGetUniformLocation(programID, uniformName);
		}
		glUniformMatrix4fv(result, false, matrix.toFloatBuffer());
		
	}
	
	
	
	public void Enable()
	{
		glUseProgram(programID);
	}
	
	public void Disable()
	{
		glUseProgram(0);
	}
	
	public void Clean()
	{
		glDeleteProgram(programID);
	}

	

}
