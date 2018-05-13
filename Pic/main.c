#include <main.h>

void main()
{
   setup_adc_ports(sAN0|sAN1|sAN2|sAN3);
   setup_adc(ADC_CLOCK_DIV_2);
   float volt;
   
   while(TRUE)
   {
      //TODO: User Code
      long int q;    //creamos una variable local de entero largo
      set_adc_channel(0); // seleccionamos el canal del ADC
      delay_us(1000);
      q=read_adc();    // leemos al informacion del ADC
      volt=((4.98*q)/1023.0);    // Realizamos nuestro algoritmos para sacar el voltaje
      printf("Luz = %1.2f\r\n",volt);    // Envia por el puerto serie la informacion del voltaje
      delay_ms(2000);    // Espera 2 segundos para volver a ejecutar el bucle
      
      set_adc_channel(1); // seleccionamos el canal del ADC
      delay_us(1000);
      q=read_adc();    // leemos al informacion del ADC
      volt=((4.98*q)/1023.0);    // Realizamos nuestro algoritmos para sacar el voltaje
      printf("MQ2 = %1.2f\r\n",volt);    // Envia por el puerto serie la informacion del voltaje
      delay_ms(2000);    // Espera 2 segundos para volver a ejecutar el bucle
      
      
   }
}
