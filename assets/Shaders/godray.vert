varying vec2 vUv;
uniform mat4 g_WorldViewProjectionMatrix;
attribute vec4 inPosition;
attribute vec2 inTexCoord;

void main()
{
        vUv = inTexCoord;
        gl_Position = g_WorldViewProjectionMatrix * inPosition;
}