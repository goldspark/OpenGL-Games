#version 330 core


out vec4 color;

in vec2 textureCoordinate;

uniform sampler2D tex;

void main()
{


	color = texture(tex, textureCoordinate);

}
