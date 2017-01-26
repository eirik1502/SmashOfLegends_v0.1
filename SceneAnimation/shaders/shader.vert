#version 430 core

layout(location=0) in vec3 position;
layout(location=1) in vec3 color;

uniform mat4 transform;

out vec3 fragColor;

void main() {
	vec4 pos = vec4(position, 1.0);
	gl_Position = transform * pos;
	
	fragColor = color;
}