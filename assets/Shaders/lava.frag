uniform float m_time;
uniform vec2 m_resolution;

uniform float m_fogDensity;
uniform vec3 m_fogColor;

uniform sampler2D m_texture1;
uniform sampler2D m_texture2;

uniform vec4 m_test;

varying vec2 vUv;

void main( void ) {

    vec2 position = -1.0 + 2.0 * vUv;

    vec4 noise = texture2D( m_texture1, vUv );
    vec2 T1 = vUv + vec2( 1.5, -1.5 ) * m_time  *0.2;
    vec2 T2 = vUv + vec2( -0.5, 2.0 ) * m_time * 0.1;

    T1.x += noise.x * 2.0;
    T1.y += noise.y * 2.0;
    T2.x -= noise.y * 0.2;
    T2.y += noise.z * 0.2;

    float p = texture2D( m_texture1, T1 * 2.0 ).a;

    vec4 color = texture2D( m_texture2, T2 * 2.0 );
    vec4 temp = color * ( vec4( p, p, p, p ) * 2.0 ) + ( color * color - 0.1 );

    if( temp.r > 1.0 ){ temp.bg += clamp( temp.r - 2.0, 0.0, 100.0 ); }
    if( temp.g > 1.0 ){ temp.rb += temp.g - 1.0; }
    if( temp.b > 1.0 ){ temp.rg += temp.b - 1.0; }

    gl_FragColor = temp;

    float depth = gl_FragCoord.z / gl_FragCoord.w;
    float LOG2 = 1.442695;
    float fogFactor = exp2( - m_fogDensity * m_fogDensity * depth * depth * LOG2 );
    fogFactor = 1.0 - clamp( fogFactor, 0.0, 1.0 );

    gl_FragColor = mix( gl_FragColor, vec4( m_fogColor, gl_FragColor.w ), fogFactor );

}