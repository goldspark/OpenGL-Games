#version 330 core
in vec2 pos;

out vec4 ParticleColor;

uniform mat4 proj;
uniform vec2 offset;
uniform vec4 color;

void main()
{
    float scale = 5.0f;
    ParticleColor = color;
    gl_Position = proj * vec4((pos * scale) + offset, 0.2f, 1.0f);
}
