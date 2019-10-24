package main;

/*
 * By Luka Kolic 2019
 * Simple Pong with no end game
 * 
 */

import input.Input;
import maths.Vector3f;
import maths.Vector4f;
import utilities.Shader;
import utilities.Texture;
import window.Display;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import org.lwjgl.opengl.GL15;
import entities.Background;
import entities.Entity;
import entities.ParticleGenerator;



public class Main implements Runnable {
	
	//GAME LOOP DATA
	private static boolean  running = false;
	private Thread thread = null;
	////////////////////////////////////
	//GRAPHICS DATA
	ParticleGenerator particleGenerator = null;
	Shader particleShader = null;
	////////////////////////////////////
	//GAMEPLAY DATA
	private Entity player = null;
	private Entity block = null;
	private Entity ball = null;
	private Background background = null;
	float delta = 0.0f; //used for calculating fps
	float dt = 0.0f; //Time difference so particles can respawn after some time
	private float rotation = 0.0f;
	private float background_rotation = 0.0f;
	private float xPos = 0.0f, yPos = 0.0f;
	private float bXpos = 0.0f, bYpos = 0.0f;
	private float ballXpos = 0.0f, ballYpos = 0.0f;
	private float directionX = 1;
	private float directionY = 1;
	private float ballSpeed = 4.2f;
	////////////////////////////////////
	
	private void Start()
	{
		running = true;
		thread = new Thread(this, "PongEffected");
		thread.start();
	}
	
    //////////////////////////////MAIN METHOD///////////////////////////////////////////
	/*
	 * 
	 * 
	 * 
	 * 
	 */	
	public static void main(String[] args) {
		new Main().Start();
	}
	/*
	 * 
	 * 
	 * 
	 * 
	 */	
	/////////////////////////////////////////////////////////////////////////////////
	
	
	//Set up the window and shaders, matrices, etc...
	private void Initialize() {
		
		 new Display();
		 GL15.glActiveTexture(GL15.GL_TEXTURE0);
		 
		 //particle system
		 particleShader = new Shader("/Shaders/ParticleVertex.glsl", "/Shaders/ParticleFragment.glsl");
		 particleGenerator = new ParticleGenerator(particleShader, 50, new Vector4f(1.0f, 0.0f, 0.0f, 1.0f));
		 /////////////////////////////
		 
		 
		 //Setting up player data		 
		 player = new Entity(new Shader("/Shaders/PlayerVertex.glsl", "/Shaders/PlayerFragment.glsl"), new Texture("/res/pad.png"), new Vector3f((Display.width / 2), 10.0f,0.0f));
		 player.setSize(new Vector3f(1.0f, 0.25f, 1.0f));
		 xPos = player.getPosition().x - 50.0f * player.getSize().x;
		 yPos = player.getPosition().y * player.getSize().y;
		 /////////////////////////////////////
		 
		 //Setting up Blocks data
		 block = new Entity(new Shader("/Shaders/PlayerVertex.glsl", "/Shaders/PlayerFragment.glsl"), new Texture("/res/pad.png") ,new Vector3f((Display.width / 2), (Display.height - 25f),0.0f));
		 block.setSize(new Vector3f(1.0f, 0.25f, 1.0f));
		 bXpos = block.getPosition().x - 50.0f * block.getSize().x;
		 bYpos = block.getPosition().y - 25.0f * block.getSize().y; 
		 
		
		 
		 
		 //background
		 background = new Background();
	
		 //Setting up Ball data 
		
		 ball = new Entity(new Shader("/Shaders/BallVertex.glsl", "/Shaders/BallFragment.glsl"), null, new Vector3f((Display.width / 2), (Display.height / 2),0.0f));
		 ball.setSize(new Vector3f(0.15f, 0.15f, 0.0f));
		 ballXpos = ball.getPosition().x -  50.0f * ball.getSize().x;
		 ballYpos = ball.getPosition().y;
		 //////////////////////////////////////////////////
		 
 
		 
	}
	
	
	private void CPUupdate() {
		glfwPollEvents();
		
		//Player Input
		if(Input.isKeyPressed(GLFW_KEY_ESCAPE))
		{
			glfwSetWindowShouldClose(Display.monitor, true);
		}
		
		if(Input.isKeyPressed(GLFW_KEY_LEFT))
		{
			xPos -= 3.5f;
			if(xPos <= 0.0f)
				xPos = 0.0f;
		}
		if(Input.isKeyPressed(GLFW_KEY_RIGHT))
		{
			xPos += 3.5f;
			if(xPos + 100.0f >= Display.width)
				xPos = Display.width - 100.0f;
		}
	
		/////////////////////////////////////////////////////////////
		//Ball Behavior
		
		ballXpos += directionX * ballSpeed;
		if(ballXpos + (100.0f * 0.15f)  >= Display.width )
		{
			directionX = -1;
		}
		
		if(ballXpos <= 0.0f )
		{
			directionX = 1;
		}	
		
		ballYpos += directionY * ballSpeed;
		if(ballYpos + (100.0f * 0.15f)  >= Display.height )
		{
			directionY = -1;
		}
		
		if(ballYpos <= 0.0f )
		{
			directionY = 1;
		}	
		
		///////////////////////////
		
		
		//Ball rotation
		rotation += 10f;
		if(rotation >= 360.0f)
			rotation = 0.0f;
		////
		
		
		//Collision detection player
		if(ballXpos + (100.0f * ball.getSize().x)  >= xPos && ballXpos + (100.0f * ball.getSize().x)  <= xPos + (100.0f * player.getSize().x) )
		{
			
			if(ballYpos  <= yPos + (100.0f * 0.25))
			{
				
				directionY = 1;
				if(ballXpos > xPos + 20f && ballXpos  < xPos + 60.0f)
				{
					
					if(directionX < 0)
					{
						directionX = -0.5f;
						directionY = 1.5f;
					}
					else 
					{
					   directionX =  0.5f;
					   directionY = 1.5f;
					}
					
				}
				
			}
			
		}
		
		//Collision detection NPC
		
		if(ballXpos + (100.0f * ball.getSize().x)  >= bXpos && ballXpos + (100.0f * ball.getSize().x)  <= bXpos + (100.0f * block.getSize().x) )
		{
			
			if(ballYpos + (100.0f * ball.getSize().y)  >= bYpos)
			{
				
				directionY = -1;
				if(ballXpos > bXpos + 20f && ballXpos  < bXpos + 60.0f)
				{
					
					if(directionX < 0)
					{
						directionX = -0.5f;
						directionY = -1.5f;
					}
					else 
					{
					   directionX =  0.5f;
					   directionY = -1.5f;
					}
			}
			}
			
		}
		
		
	
		if(directionX < 0 && directionY < 0)
			particleGenerator.setColor(new Vector4f(1.0f, 0.0f, 0.0f, 1.0f));
		else if(directionX < 0 && directionY > 0)
			particleGenerator.setColor(new Vector4f(0.2f, 0.5f, 0.0f, 1.0f));
		else if(directionX > 0 && directionY > 0)
			particleGenerator.setColor(new Vector4f(0.8f, 0.8f, 0.0f, 1.0f));
		else if(directionX > 0 && directionY < 0)
			particleGenerator.setColor(new Vector4f(0.0f, 0.50f, 1.0f, 1.0f));

		

		
		
		
		//////////////////////////////////////////////////
		

		//NPC movement
		
		//Window boundary check
		if(xPos >= bXpos + 100.0f * block.getSize().x)
		{
			
			bXpos++;
			
		}
		if(bXpos + 100.0f >= Display.width)
			bXpos = Display.width - 100.0f;
		
		if(xPos + 100.0f <= bXpos)
		{
			bXpos--;
			
		}
		if(bXpos <= 0.0f)
			bXpos = 0.0f;
		/////
		
		
		//Following the ball
		
		if(ballXpos + (100f * ball.getSize().x) > bXpos + (100f * ball.getSize().x))
		{
			bXpos += 3.5f;
		}
		
		if(ballXpos  < bXpos)
		{
			bXpos -= 3.5f;
		}
		
	
		background_rotation += 0.05f;
		if(background_rotation >= 360.0f)
		{
			background_rotation = 0.0f;
		}
		
		
		particleGenerator.Update((float)dt, ball, 2, ball.getPosition());
		ball.Update(new Vector3f(ballXpos, ballYpos, 0.0f), rotation);	
		player.Update(new Vector3f(xPos, yPos, 0.0f), 0.0f);
		block.Update(new Vector3f(bXpos,  bYpos, 0.0f), 0.0f);
		background.Update(new Vector3f(-130f, -207f, 0.0f), background_rotation);
	}
	
	
	private void GraphicsUpdate() {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		
		
		
		background.Render();
		player.Render();
		block.Render();
		ball.Render();	
		particleGenerator.Render();
		
		
		glfwSwapBuffers(Display.monitor);

	}
	
	/*////////////////////////////////////////////////////////////////////////////////////////////////////
	 * 
	 * 
	 * GAME LOOP
	 * 
	 * 
	 */

	@Override
	public void run() {
		Initialize();
		
		//FPS counter and CPU counter
		float lastTime = System.nanoTime();
		double ns = 1000000000.0 / 60.0; //Convert to 60 FPS
		long timer = System.currentTimeMillis();
		int cpuTime = 0;
		int frameTime = 0;
	    /////////////////////////////////////////////
		
		
		while(running)
		{
			
			float nowTime = System.nanoTime();
			delta += (nowTime - lastTime) / ns;
			dt = (nowTime - lastTime) / 1000000000f;
			lastTime = nowTime;
			
			if(delta >= 1.0) //We only get updates in the cpu frame rate
			{
				CPUupdate();
				cpuTime++;
				delta--;
			}
			
			
			GraphicsUpdate();
			frameTime++;
			if(System.currentTimeMillis() - timer > 1000) //FPS COUNTER right now its locked to your monitor Hz . Can unlock it by using glfw
			{
				
				timer += 1000;
				System.out.println("Ups: " + cpuTime + ", Frames: " + frameTime);
				cpuTime = 0;
				frameTime = 0;

			}
			
			if(glfwWindowShouldClose(Display.monitor))
			{
				running = false;
			}
		}
		
		
		
		
		
		//FREE UP THE MEMORY AFTER THE APPLICATION CLOSES
		player.Clean();
		block.Clean();
		background.Clean();
		particleGenerator.Clean();
		particleShader.Clean();
		glfwDestroyWindow(Display.monitor);
		glfwTerminate();
		///////////////////////////////////////	
	}
	
	/*
	 * 
	 * 
	 *///////////////////////////////////////////////////////////
	 

}
