import RPi.GPIO as GPIO
import threading
import time
import config_cargas
import cambios_cargas
import subir_datostemphumd
import subir_datossegu
import serial_comunicacion
import subir_datosco2

#GPIO Setup
GPIO.setmode(GPIO.BCM)
GPIO.setwarnings(False)

#luzPatio
GPIO.setup(17, GPIO.OUT)
#luzCocina
GPIO.setup(5, GPIO.OUT)
#luzBano
GPIO.setup(22, GPIO.OUT)
#luzSala
GPIO.setup(27, GPIO.OUT)
#luzCuarto
GPIO.setup(6, GPIO.OUT)
#luzGaraje
GPIO.setup(13, GPIO.OUT)
#Aspersores
GPIO.setup(4, GPIO.OUT)
#Seguridad
GPIO.setup(18, GPIO.IN) #ENTRADA DE SEGURIDAD
GPIO.setup(19, GPIO.IN) #ENTRADA DE CO2

#GPIO 25 APARTADO SENSOR DE TEMPE Y HUMEDAD
#GPIO 20 APARTADO VENTILADOR
#GPIO 21 APARTADO PUERTA
#GPIO 12 ACTIVACION BOMBA AGUA

#Usamos el pin GPIO 8 como TRIGGER
#Usamos el pin GPIO 7 como ECHO

config_cargas.verificar_Luces()
config_cargas.verificar_Puerta_Garaje()
config_cargas.verificar_Aspersores()
config_cargas.verificar_Ventilador()

hilo_cambiocargas = threading.Thread(target= cambios_cargas.principal())
hilo_cambiocargas.start()

hilo_actdatosseg = threading.Thread(target= subir_datossegu.actualizar_Seguridad())
hilo_actdatosseg.start()

hilo_actdatostemphumd = threading.Thread(target= subir_datostemphumd.actualizar_Temp_Humd)
hilo_actdatostemphumd.setDaemon(True)
hilo_actdatostemphumd.start()

hilo_serial = threading.Thread(target= serial_comunicacion.actualizar)
hilo_serial.start()
