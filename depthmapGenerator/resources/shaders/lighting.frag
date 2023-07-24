#version 330 core

struct Light {
    vec3  position;
    vec3  direction;
    float cutOff;
    float outerCutOff;

    vec3 ambient;
    vec3 diffuse;
    vec3 specular;

    float constant;
    float linear;
    float quadratic;
};

out vec4 FragColor;

uniform Light light;

in vec3 Normal;
in vec3 FragPos;
in vec2 TexCoords;


void main()
{
    vec3 ambient = light.ambient;
    
    vec3 norm = normalize(light.position);
    vec3 lightDir = normalize(light.position - FragPos);
    float diff = max(dot(norm, lightDir), 0.0);
    vec3 diffuse = light.diffuse * diff;


    //attenuation
    float distance    = length(light.position - FragPos);
    float attenuation = 1.0 / (light.constant + light.linear * distance +
    light.quadratic * (distance * distance));

    //spotlight intensity
    //This is how we calculate the spotlight, for a more in depth explanation of how this works. Check out the web tutorials.
    float theta     = dot(lightDir, normalize(-light.direction));
    float epsilon   = light.cutOff - light.outerCutOff;
    float intensity = clamp((theta - light.outerCutOff) / epsilon, 0.0, 1.0); //The intensity, is the lights intensity on a given fragment,
    
    diffuse  *= attenuation * intensity;
    FragColor = vec4(diffuse, 1.0);
}