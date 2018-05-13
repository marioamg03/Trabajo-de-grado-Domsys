import RPi.GPIO as GPIO
import time
import config_pyrebase
import Adafruit_DHT

GPIO.setmode(GPIO.BCM)     #Ponemos la placa en modo BCM
GPIO.setwarnings(False)
GPIO_TRIGGER = 8          #Usamos el pin GPIO 8 como TRIGGER
GPIO_ECHO    = 7           #Usamos el pin GPIO 7 como ECHO
GPIO_ACTBOMBA = 12         #usamos el pin GPIO 12 para activar y desctivar la bomba

GPIO.setup(GPIO_ACTBOMBA,GPIO.OUT) # Configuro la activacion de la bomba

GPIO.setup(GPIO_TRIGGER,GPIO.OUT)  #Configuramos Trigger como salida
GPIO.setup(GPIO_ECHO,GPIO.IN)      #Configuramos Echo como entrada

GPIO.output(GPIO_TRIGGER,False)    #Ponemos el pin 8 como LOW

#Configuracion Pines de DHT11 sensor temperatura y humedad
sensor = Adafruit_DHT.DHT11
# Configuracion del puerto GPIO al cual esta conectado  (GPIO 23)
pinDHT= 25

def actualizar_Temp_Humd():
    while True:

        print ("La Temp y Humedad es:")
        humedad, temperatura = Adafruit_DHT.read_retry(sensor,pinDHT)

        if humedad is not None and temperatura is not None:
            print('Temperatura={0:0.1f}*  Humedad={1:0.1f}%'.format(temperatura, humedad))
            config_pyrebase.db.child("casa/status").update({"temperatura": str(temperatura)})  #actualizo temperatura
            config_pyrebase.db.child("casa/status").update({"humedad": str(humedad)})          #actualizo humedad
        else:
            print('Lectura de temperatura y humedad fallida')

        GPIO.output(GPIO_TRIGGER,True)   #Enviamos un pulso de ultrasonidos
        time.sleep(0.00001)
        GPIO.output(GPIO_TRIGGER,False)  #Apagamos el pulso
        start = time.time()              #Guarda el tiempo actual mediante time.time()
        while GPIO.input(GPIO_ECHO)==0:
            start = time.time()          #Mantenemos el tiempo actual mediante time.time()
        while GPIO.input(GPIO_ECHO)==1:
            stop = time.time()           #Guarda el tiempo actual mediante time.time() en otra variable
        elapsed = stop-start
        distance = (elapsed * 34300)/2   #Distancia es igual a tiempo por velocidad partido por 2  343.2 m/s  D = (T x V)/2
        distance = round(distance, 2)
        porcentaje = round(((distance - 2) * 10), 2)
        porcentaje = 100 - porcentaje

        if porcentaje > 100:
            porcentaje = 100
        elif porcentaje < 0:
            porcentaje = 0

        auto_mode_bomba = config_pyrebase.db.child("casa/auto-mode/regulacion bomba/habilitado").get()
        auto_mode_bomba_porcentaje = config_pyrebase.db.child("casa/auto-mode/regulacion bomba/set_point").get()

        if auto_mode_bomba.val() == "apagado": #actualizar lucez normal
            if porcentaje >= 60:
                GPIO.output(GPIO_ACTBOMBA,False)
                print ("BOMBA DESACTIVA")
            else:
                GPIO.output(GPIO_ACTBOMBA,True)
                time.sleep(1)
                GPIO.output(GPIO_ACTBOMBA,False)
                time.sleep(2)
                print("BOMBA ACTIVADA")
        else:
            if porcentaje > int(auto_mode_bomba_porcentaje.val()) or porcentaje >= 60:
                GPIO.output(GPIO_ACTBOMBA,False)
                print ("BOMBA DESACTIVA")
            else:
                GPIO.output(GPIO_ACTBOMBA,True)
                time.sleep(1)
                GPIO.output(GPIO_ACTBOMBA,False)
                time.sleep(2)
                print("BOMBA ACTIVADA")

        config_pyrebase.db.child("casa/status").update({"tanque": str(porcentaje)})  #actualizo porcentaje del tanque
        print "El porcentaje de agua en el tanque es de: ", porcentaje,"%"

        time.sleep(10)
