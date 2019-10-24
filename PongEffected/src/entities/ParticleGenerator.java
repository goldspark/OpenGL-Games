package entities;


/*
 * By Luka Kolic 2019
 * Simple particle generator
 * 
 */

import maths.Matrix4f;
import maths.Vector2f;
import maths.Vector3f;
import maths.Vector4f;
import utilities.BufferUtilities;
import utilities.Shader;
import window.Display;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL30.*;

import java.util.ArrayList;
import java.util.Random;







public class ParticleGenerator {
	
	public static class Particle{
		public Vector2f position;
		public Vector2f velocity;
		public Vector4f color;
		public float life;
		
		public Particle()
		{
			 position = new Vector2f();
			 velocity = new Vector2f();
			 color = new Vector4f(0f, 0f, 0f, 0f);
			 life = 0.0f;
		}
		
	}
	
	float lifetest = 0.0f;
	private int vao;
	private int lastUsedParticle = 0;
	private int count;
	private ArrayList<Particle> particles = new ArrayList<ParticleGenerator.Particle>();
	private Shader shader;
	public Vector4f color_setter = null;
	
	
	
	public ParticleGenerator(Shader shader, int count, Vector4f color_setter) {
		this.shader = shader;
		this.count = count;
		this.color_setter = color_setter;
		this.Init();
		
		
		 Matrix4f projection_Particle = new Matrix4f();
		 projection_Particle = Matrix4f.Orthographic(0.0f, Display.width, 0.0f, Display.height, -1.0f, 1.0f);
	 
		 shader.Enable();
		 shader.setMatrix4f("proj", projection_Particle);
		 shader.Disable();
		 lifetest = 1.0f;
	}
	
	
	public void Update(float dt, Entity ball, int newParticles, Vector3f offset)
	{
		
		
		for (int i = 0; i < newParticles; ++i)
	    {
	        int unusedParticle = this.FirstUnusedParticle();
	        this.RespawnParticle(this.particles.get(unusedParticle), ball, offset);
	    }
		
		for(int i = 0; i < this.count; i++)
		{
			Particle p = this.particles.get(i);
			p.life -= dt;
			if(p.life >= 0.0f)
			{
				p.color.w -= dt * 1.5f;
			}
		}		
	}
	
	
	public void Render()
	{
		
		//glBlendFunc(GL_SRC_ALPHA,  GL_ONE);
		this.shader.Enable();
		for(Particle p : particles)
		{
			
			if(p.life > -0.1f)
			{

				this.shader.setUnfirom2f("offset", p.position);
				this.shader.setUnfirom4f("color", p.color);
				glBindVertexArray(vao);
				glDrawArrays(GL_TRIANGLES, 0, 6);
				glBindVertexArray(0);
			}
		}
		this.shader.Disable();
		//glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
	}
	
	
	
	private void Init() {
		
		int vbo = glGenBuffers();
		float particle_quad[] = {
			0.0f, 0.0f,
			0.5f, 0.0f,
			0.5f, 0.5f,
			
			0.5f, 0.5f,
			0.0f, 0.5f,
			0.0f, 0.0f
		};
		
		vao = glGenVertexArrays();
		glBindVertexArray(vao);
		
		glBindBuffer(GL_ARRAY_BUFFER, vbo);
		glBufferData(GL_ARRAY_BUFFER, BufferUtilities.createFloatBuffer(particle_quad), GL_STATIC_DRAW);
		glEnableVertexAttribArray(0);
		glVertexAttribPointer(0, 2, GL_FLOAT, false, 0, 0);
		glBindVertexArray(0);
		
		
		for(int i = 0; i < this.count; i++)
		{
			this.particles.add(new Particle());
		}	
	}
	
	private int FirstUnusedParticle() {
		
		for(int i = lastUsedParticle; i < this.count; i++)
		{
			if(this.particles.get(i).life < 0.0f) {
				lastUsedParticle = i;
				return i;
			}
		}
		
		for(int i = 0; i < lastUsedParticle; i++) {
			if(this.particles.get(i).life < 0.0f) {
				lastUsedParticle = i;
				return i;
			}
		}
		
		lastUsedParticle = 0;
		return 0;
		
	}
	
	private void RespawnParticle(Particle p, Entity ball, Vector3f offset)
	{
		Random random = new Random();
		p.position.x = ball.getPosition().x + random.nextInt(15);
		p.position.y = ball.getPosition().y + random.nextInt(15);

		p.color = getColor();
		p.life = 0.1f;

	}
	
	
	public void setColor(Vector4f color)
	{
		this.color_setter = color;
		
	}
	
	public Vector4f getColor()
	{
		return this.color_setter;
	}
	
	
	public void Clean() {
		
		glDeleteVertexArrays(vao);
		shader.Clean();
	}
	

	

}
