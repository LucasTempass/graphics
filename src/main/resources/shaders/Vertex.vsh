#version 460

layout (location = 0) in vec3 position;
layout (location = 1) in vec3 vertex_color;

out vec4 finalColor;

uniform mat4 transform;

void main() {
    gl_Position = transform * vec4(position, 1.0);
    finalColor = vec4(vertex_color, 1.0);
}