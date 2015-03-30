uniform vec2 m_uvScale;
varying vec2 vUv;

uniform mat4 g_WorldViewProjectionMatrix;
uniform mat4 g_ProjectionMatrix;
attribute vec4 inPosition;
attribute vec2 inTexCoord;

void main()
{

    vUv = m_uvScale * inTexCoord;
    gl_Position = g_WorldViewProjectionMatrix * inPosition;

}