package entities;

/*
 * By Luka Kolic 2019
 * 2D Entity class for spawning objects into the game world
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

public class Entity {
	
	private Matrix4f model;
	private Vector3f size;
	private  Vector3f position;
	private float rotation;
	private int vao, vbo, ibo, tco;
	private float centerX, centerY; //ONLY USED TO CALCULATE THE CENTER OF THE OBJECT SO INSTEAD OF MOVING OBJECT BY ITS LEFT CORNER WE MOVE IT BY CENTER
	private Texture texture;
	public Shader shader;
	
	private float[] initPosition = {
			0.0f, 0.0f, 0.2f,
			100.0f, 0.0f , 0.2f,
			100.0f, 100.0f, 0.2f,
			0.0f, 100.0f , 0.2f
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
	
	
	public Entity(Shader shader, Texture texture, Vector3f position)
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
		
		this.texture = texture;
		this.shader = shader; //Used for block too
		Matrix4f projection_Matrix = new Matrix4f();
		projection_Matrix = Matrix4f.Orthographic(0.0f, Display.width, 0.0f, Display.height, -1.0f, 1.0f);
	
		shader.Enable();
		shader.setUnfirom1i("tex", 0);
		shader.setMatrix4f("proj", projection_Matrix);
		shader.Disable();
		
		size = new Vector3f();
		
		centerX = (50.0f * size.x);
		centerY = (50.0f * size.y);
		
		
		this.position = position;
		
	}
	
	
	
	
	public void Update(Vector3f position, float angle) {
		
		this.position = position;
		rotation = angle;
		
		model = Matrix4f.Identity();
		
		model = Matrix4f.Translate(model, new Vector3f(this.position.x - centerX, this.position.y - centerY, this.position.z)); 
		
		model = Matrix4f.Translate(model, new Vector3f(50.0f * getSize().x, 50.0f * getSize().y, 0.0f * getSize().z));
		model = Matrix4f.RotateZ(model, rotation);
		model = Matrix4f.Translate(model, new Vector3f(-50.0f * getSize().x, -50.0f * getSize().y, -0.0f * getSize().z));

		model = Matrix4f.Scale(model, getSize());
		
		shader.Enable();
		shader.setMatrix4f("model", model);
		shader.Disable();
		
	}
	
	
	
	public float getCenterX() {
		return centerX;
	}
	
	
	public float getCenterY() {
		return centerY;
	}
	
	
	public Matrix4f getModel() {
		return model;
	}
	
	
    public Vector3f getSize() {
		return size;
	}






	public void setSize(Vector3f size) {
		this.size = size;
	}



	public Vector3f getPosition() {
		return position;
	}






	public void setPosition(float x, float y, float z) {
		this.position.x = x;
		this.position.y = y;
		this.position.z = z;
	}






	public float getRotation() {
		return rotation;
	}






	public void setRotation(float rotation) {
		this.rotation = rotation;
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
		if(texture != null)
		texture.Bind();
		
		
	}
	
	public void Unbind()
	{
		glBindVertexArray(0);
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
		if(texture != null)
		texture.Unbind();
	}
	
	public void Clean()
	{
		glDeleteVertexArrays(vao);
		glDeleteBuffers(vbo);
		glDeleteBuffers(ibo);
		glDeleteTextures(tco);
		if(texture != null)
		texture.Clean();
		shader.Clean();
	}
	

}
