package entities;

/*
 * By Luka Kolic 2019
 * Code for setting background
 * 
 */

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

import maths.Matrix4f;
import maths.Vector3f;
import utilities.BufferUtilities;
import utilities.Shader;
import utilities.Texture;
import window.Display;

public class Background {
	
	
	private Matrix4f model;
	private  Vector3f position;
	private float rotation;
	private int vao, vbo, ibo, tco;
	private Texture texture;
	public Shader shader;
	
	private float[] initPosition = {
			0.0f, 0.0f, 0.0f,
			Display.width + 420f, 0.0f , 0.0f,
			Display.width + 420f, Display.height + 420f, 0.0f,
			0.0f, Display.height + 900f, 0.0f
	};
	
	private byte[] index = {
			0, 1, 2,
			2, 3, 0
	};
	
	private float[] tcs = {
			0f, 1f,
			0f, 0f,
			1f, 0f,
			1f, 1f	
	};
	
	
	public Background()
	{
		vao = glGenVertexArrays();
		vbo = glGenBuffers();
		ibo = glGenBuffers();
		tco = glGenBuffers();
		
		
		glBindVertexArray(vao);
		
			glBindBuffer(GL_ARRAY_BUFFER, vbo);
			glBufferData(GL_ARRAY_BUFFER, BufferUtilities.createFloatBuffer(initPosition), GL_STATIC_DRAW);
			glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
			glEnableVertexAttribArray(0);
			
			glBindBuffer(GL_ARRAY_BUFFER, tco);
			glBufferData(GL_ARRAY_BUFFER, BufferUtilities.createFloatBuffer(tcs), GL_STATIC_DRAW);
			glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);
			glEnableVertexAttribArray(1);
			
			glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
			glBufferData(GL_ELEMENT_ARRAY_BUFFER, BufferUtilities.createByteBuffer(index), GL_STATIC_DRAW);
			glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);

		
		
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		glBindVertexArray(0);
		
		
		shader = new Shader("/Shaders/BackgroundVertex.glsl", "/Shaders/BackgroundFragment.glsl");

		texture = new Texture("/res/background.jpg");
		
		 Matrix4f projection_Background = new Matrix4f();
		 projection_Background = Matrix4f.Orthographic(0.0f, Display.width, 0.0f, Display.height, -1.0f, 1.0f);
		 shader.Enable();
		 shader.setUnfirom1i("tex", 0);
		 shader.setMatrix4f("proj", projection_Background);
		 shader.Disable();
		 

	
		
	}
	
	public void Update(Vector3f position, float angle)
	{
		this.position = position;
		rotation = angle;
		
		model = Matrix4f.Identity();
		
		model = Matrix4f.Translate(model, new Vector3f(this.position.x, this.position.y, this.position.z)); 
		
		model = Matrix4f.Translate(model, new Vector3f((Display.width + 420f) / 2f, (Display.height + 420f) / 2f, 0.0f));
		model = Matrix4f.RotateZ(model, rotation);
		model = Matrix4f.Translate(model, new Vector3f(-(Display.width + 420f) / 2f, -(Display.height + 420f) / 2f, 0.0f));

		model = Matrix4f.Scale(model, new Vector3f(1f, 1f, 1f));
		
		shader.Enable();
		shader.setMatrix4f("model", model);
		shader.Disable();
		
	}
	


	public void Render()
	{
		shader.Enable();
		this.Bind();
		glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_BYTE, 0);
		this.Unbind();
		shader.Disable();
	}
	
	public void Bind()
	{
		glBindVertexArray(vao);
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
		texture.Bind();
		
	}
	
	public void Unbind()
	{
		glBindVertexArray(0);
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
		texture.Unbind();
	}
	
	public void Clean()
	{
		glDeleteVertexArrays(vao);
		glDeleteBuffers(vbo);
		glDeleteBuffers(ibo);
		glDeleteTextures(tco);
		texture.Clean();
		shader.Clean();
	}
	

	
	
	
	

}
