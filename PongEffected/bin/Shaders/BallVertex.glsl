#version 330 core


in vec3 pos;


uniform mat4 proj = mat4(1.0f);
uniform mat4 model = mat4(1.0f);


void main()
{

	gl_Position = proj * model * vec4(pos, 1.0f);

}
