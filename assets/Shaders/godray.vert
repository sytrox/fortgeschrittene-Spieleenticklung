varying vec2 vUv;
uniform mat4 g_WorldViewProjectionMatrix;
in vec4 inPosition;
in vec2 inTexCoord;

void main()
{
        vUv = inTexCoord;
        gl_Position = g_WorldViewProjectionMatrix * inPosition;
}