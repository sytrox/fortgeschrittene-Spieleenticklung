uniform float m_exposure;
uniform float m_decay;
uniform float m_density;
uniform float m_weight;
uniform vec2 m_lightPositionOnScreen;
uniform sampler2D m_firstPass;
varying vec2 vUv;
const int NUM_SAMPLES = 100 ;

void main()
{
    vec2 deltaTextCoord = vec2( vUv - m_lightPositionOnScreen.xy );
    vec2 textCoo = vUv;
    deltaTextCoord *= 1.0 /  float(NUM_SAMPLES) * m_density;
    float illuminationDecay = 1.0;
    gl_FragColor = vec4(0,0,0,0);
    for(int i=0; i < NUM_SAMPLES ; i++)
    {
             textCoo -= deltaTextCoord;
             vec4 sample = texture2D(m_firstPass, textCoo );

             sample *= illuminationDecay * m_weight;

             gl_FragColor += sample;

             illuminationDecay *= m_decay;
     }
     gl_FragColor *= m_exposure;
}