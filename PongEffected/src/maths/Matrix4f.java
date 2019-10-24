package maths;

/*
 * By Luka Kolic 2019
 * Math classs for shaders
 * 
 */

import java.nio.FloatBuffer;

import utilities.BufferUtilities;

public class Matrix4f {
	
	private  float elements[] = new float[16];
	
	public Matrix4f()
	{
		
	}
	
	
	public static Matrix4f Identity()
	{
		Matrix4f result = new Matrix4f();
		
		for(int i = 0; i < 16; i++)
		{
			result.elements[i] = 0.0f;
		}
		
		result.elements[0 + 0 * 4] = 1.0f;
		result.elements[1 + 1 * 4] = 1.0f;
		result.elements[2 + 2 * 4] = 1.0f;
		result.elements[3 + 3 * 4] = 1.0f;
		
		return result;
		
	}
	
	public Matrix4f Multiply(Matrix4f matrix)
	{
		Matrix4f result = new Matrix4f();
		for(int y = 0; y < 4; y++) {
			for(int x = 0; x < 4; x++) {
				float sum = 0.0f;
				for(int e = 0; e < 4; e++) {
					sum += this.elements[x + e * 4] * matrix.elements[e + y * 4]; 
				}
				result.elements[x + y * 4] = sum;
			}
		}
		
		return result;
	}
	
	public static Matrix4f Orthographic(float left, float right, float bottom, float top, float near, float far) {
		Matrix4f result = Identity();
		
		result.elements[0 + 0 * 4] = 2.0f / (right - left);

		result.elements[1 + 1 * 4] = 2.0f / (top - bottom);

		result.elements[2 + 2 * 4] = 2.0f / (near - far);
		
		result.elements[0 + 3 * 4] = (left + right) / (left - right);
		result.elements[1 + 3 * 4] = (bottom + top) / (bottom - top);
		result.elements[2 + 3 * 4] = (far + near) / (far - near);
		
		return result;
	}
	
	public static Matrix4f Translate(Matrix4f matrix, Vector3f vector)
	{
		Matrix4f result = Identity();
		result.elements[0 + 3 * 4] = vector.x;
		result.elements[1 + 3 * 4] = vector.y;
		result.elements[2 + 3 * 4] = vector.z;
		
		
		
		return matrix.Multiply(result);
	}
	
	public static Matrix4f RotateZ(Matrix4f matrix, float angle) {
		Matrix4f result = Identity();
		float r = (float) Math.toRadians(angle);
		float cos = (float) Math.cos(r);
		float sin = (float) Math.sin(r);
		
		result.elements[0 + 0 * 4] = cos;
		result.elements[1 + 0 * 4] = sin;
		
		result.elements[0 + 1 * 4] = -sin;
		result.elements[1 + 1 * 4] = cos;
		
		return matrix.Multiply(result);
	}
	
	
	public static  Matrix4f Scale(Matrix4f matrix, Vector3f vector) {
		Matrix4f result = Identity();
		
		
		result.elements[0 + 0 * 4] = vector.x;
		result.elements[1 + 1 * 4] = vector.y;
		result.elements[2 + 2 * 4] = vector.z;

		
		return matrix.Multiply(result);
	}
	
	public FloatBuffer toFloatBuffer() {
		return BufferUtilities.createFloatBuffer(elements);
	}

	
}
