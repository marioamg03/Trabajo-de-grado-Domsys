import RPi.GPIO as GPIO
import time
import config_pyrebase
from pyfcm import FCMNotification
push_service = FCMNotification(api_key="AIzaSyAsdTiJ-kRj5J0QMqPK5igDZYMIRO556sI")
registration_id = "e5SWXFOeKIA:APA91bH3RGguRdm7tKtzWKvLwGDDRU5jE3ETOmBP_cCjIonULShb46wwJNOffnfYcgBxpGp_DvfkQYbMQQbXC98vCQTkQCrO4MakXa09eANfdZApKA8sR3yU6Ny5AsuXDnQi6om2CFEV"

def actualizar_Seguridad():

    def stream_handlerseguridad(message):

#-------------------------------// Set Values //--------------------------------*
        print ("")
        print ("----------- EL ESTADO DE LA SEGURIDAD INICIO-----------")
        print ("")
        #Set value of luzBano

        seguridad = config_pyrebase.db.child("casa/status/seguridad").get()

        while seguridad.val() == "activa":
            if GPIO.input(18):                 #When output from motion sensor is LOW
                print "INTRUSO ACTIVO",GPIO.input(18)
                message_body = "INTRUSO DETECTADO!!!"
                result = push_service.notify_single_device(registration_id=registration_id, message_body=message_body, sound="warning_siren")
                time.sleep(30)
#                config_pyrebase.db.child("casa/status").update({"alarma": "encendida"})  #actualizo alarma
            else:
                print "NO INTRUSO",GPIO.input(18)
#-------------------------------// Get Values //--------------------------------*
            #Get value of Seguridad
            seguridad = config_pyrebase.db.child("casa/status/seguridad").get()


        print("")
        print ("----------- EL ESTADO DE LA SEGURIDAD FIN-----------")


    global my_streamSeguridad
    my_streamSeguridad = config_pyrebase.db.child ("casa/status/seguridad").stream (stream_handlerseguridad)
    print ("inicio el streaming Seguridad")
