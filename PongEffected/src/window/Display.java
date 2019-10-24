package window;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryUtil.*;
import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;

import input.Input;

public class Display {

	//Window attributes
	public static int width = 480;
	public static int height = 640;
	
	public static long monitor;
	//------------------
	
	public Display() {
		
		if(!glfwInit())
		{
			throw new IllegalStateException("Unable to initialize GLFW");		
		}
		
		glfwWindowHint(GLFW_VERSION_MAJOR, 3);
		glfwWindowHint(GLFW_VERSION_MINOR, 3);
		//glfwWindowHint(GLFW, GLFW_OPENGL_CORE_PROFILE);
		glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);
		
		monitor = glfwCreateWindow(width, height, "PongEffected", NULL, NULL);
		if (monitor == NULL) {
			System.err.println("Could not create GLFW window!");
		}
		GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
		glfwSetWindowPos(monitor, (vidmode.width() - width) / 2, (vidmode.height() - height) / 2);
		glfwSetKeyCallback(monitor, new Input());
		glfwMakeContextCurrent(monitor);
		GL.createCapabilities();
		
		glViewport(0, 0, width, height);
		//glEnable(GL_DEPTH_TEST);
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glClearColor(1.0f, 0.0f, 1.0f, 1.0f);
		System.out.println("OpenGL: " +  glGetString(GL_VERSION));
		
	}
	
}
