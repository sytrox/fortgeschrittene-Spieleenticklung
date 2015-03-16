uniform vec2 m_uvScale;
varying vec2 vUv;

uniform mat4 g_WorldViewProjectionMatrix;
uniform mat4 g_ProjectionMatrix;
in vec4 inPosition;
in vec2 inTexCoord;

void main()
{

    vUv = m_uvScale * inTexCoord;
    gl_Position = g_WorldViewProjectionMatrix * inPosition;

}