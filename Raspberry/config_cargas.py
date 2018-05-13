import RPi.GPIO as GPIO
import time
import config_pyrebase

GPIO.setwarnings(False)
GPIO.setmode(GPIO.BCM)

GPIO.setup(21, GPIO.OUT)               #PWM PUERTA GARAJE
a = GPIO.PWM(21,50)                    #Ponemos el pin 21 en modo PWM y enviamos 50 pulsos por segundo
a.ChangeDutyCycle(2.5)
a.start(21.5)                          #Enviamos un pulso del 7.5% para centrar el servo

GPIO.setup(20, GPIO.OUT)               #PWM VENTILADOR
p = GPIO.PWM(20,207)
p.start(1)


def verificar_Luces():
    print ("Salida de los BOMBILLOS:")

    #Get value of luzBano
    luz_Bano = config_pyrebase.db.child("casa/status/luces/bano").get()

    if luz_Bano.val() == "encendido":
        GPIO.output(22, True) ## Enciendo el 17
    else:
        GPIO.output(22, False) ## Apago el 17

    print ("Salida luz_Bano: ")
    print (luz_Bano.val())

    #Get value of luzCocina
    luz_Cocina = config_pyrebase.db.child("casa/status/luces/cocina").get()

    if luz_Cocina.val() == "encendido":
        GPIO.output(5, True) ## Enciendo el 27
    else:
        GPIO.output(5, False) ## Apago el 27

    print ("Salida luz_Cocina: ")
    print(luz_Cocina.val())

    #Get value of luzCuarto
    luz_Cuarto = config_pyrebase.db.child("casa/status/luces/cuarto").get()

    if luz_Cuarto.val() == "encendido":
        GPIO.output(6, True) ## Enciendo el 22
    else:
        GPIO.output(6, False) ## Apago el 22

    print ("Salida luz_Cuarto")
    print (luz_Cuarto.val())

    #Get value of luzGaraje
    luz_Garaje = config_pyrebase.db.child("casa/status/luces/garaje").get()

    if luz_Garaje.val() == "encendido":
        GPIO.output(13, True) ## Enciendo el 5
    else:
        GPIO.output(13, False) ## Apago el 5

    print ("Salida luz_Garaje")
    print (luz_Garaje.val())

    #Get value of luzPatio
    luz_Patio = config_pyrebase.db.child("casa/status/luces/patio").get()

    if luz_Patio.val() == "encendido":
        GPIO.output(17, True) ## Enciendo el 6
    else:
        GPIO.output(17, False) ## Apago el 6

    print ("Salida luz_Patio")
    print (luz_Patio.val())

    #Get value of luzSala
    luz_Sala = config_pyrebase.db.child("casa/status/luces/sala").get()

    if luz_Sala.val() == "encendido":
        GPIO.output(27, True) ## Enciendo el 13
    else:
        GPIO.output(27, False) ## Apago el 13

    print ("Salida luz_Sala")
    print (luz_Sala.val())



def verificar_Puerta_Garaje():
    #Get value of Puerta_Garaje
    puerta_Garaje = config_pyrebase.db.child("casa/status/pgaraje").get()

    if puerta_Garaje.val() == "abierta":
        a.ChangeDutyCycle(2.5)
        time.sleep(1)
        print("Abierta la puerta del garaje")## Abrir puerta!
    else:
        a.ChangeDutyCycle(12.5)
        time.sleep(1)
        print("Cerrada la puerta del garaje")## Cerrar puerta!

    print ("Salida Puerta_Garaje: ")
    print(puerta_Garaje.val())



def verificar_Aspersores():
        #Get value of Aspersores
    aspersores = config_pyrebase.db.child("casa/status/aspersores").get()

    if aspersores.val() == "activados":
        print("Los aspersores estan en funcionamiento")## Aspersores Activados!
    else:
        print("Los aspersores NO estan en funcionamiento")## Aspersores Desactivados!

    print ("Salida ASPERSORES: ")
    print(aspersores.val())



def verificar_Ventilador():
    #Get value of Ventilacion
    ventilacion_Estado = config_pyrebase.db.child("casa/status/ventilacion").get()

    p.ChangeDutyCycle(int(ventilacion_Estado.val()))
    time.sleep(1)

    print ("Salida VENTILADORES: ")
    print(ventilacion_Estado.val())
