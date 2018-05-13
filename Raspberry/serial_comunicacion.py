import RPi.GPIO as GPIO
import time
import config_pyrebase
from pyfcm import FCMNotification
import serial

push_service = FCMNotification(api_key="AIzaSyAsdTiJ-kRj5J0QMqPK5igDZYMIRO556sI")
registration_id = "e5SWXFOeKIA:APA91bH3RGguRdm7tKtzWKvLwGDDRU5jE3ETOmBP_cCjIonULShb46wwJNOffnfYcgBxpGp_DvfkQYbMQQbXC98vCQTkQCrO4MakXa09eANfdZApKA8sR3yU6Ny5AsuXDnQi6om2CFEV"
ser = serial.Serial('/dev/ttyAMA0', baudrate=9600,
                    parity=serial.PARITY_NONE,
                    stopbits=serial.STOPBITS_ONE,
                    bytesize=serial.EIGHTBITS
                    )
time.sleep(1)

def actualizar():
    try:
        print '------------------------- INICIO COMUNICACION SERIAL PIC'
        while True:
            if ser.inWaiting() > 0:
                data = ser.readline()
                print data
                if data[0:6] == "Luz = ":
                    Luz = round(float(data[6:-2]), 2)
                    print Luz
                    if Luz > 4.5:                 #When output from motion sensor is LOW
                        config_pyrebase.db.child("casa/status/validacionluz").update({"cuarto": "apagado"})
                        print ("Luz apagada")
                    else:
                        config_pyrebase.db.child("casa/status/validacionluz").update({"cuarto": "encendido"})
                        print ("Luz encendida")


                if data[0:6] == "MQ2 = ":
                    Mq2 = round(float(data[6:-2]), 2)
                    print Mq2
                    if Mq2 > 1:                 #When output from motion sensor is LOW
                        print "PROPANO DETECTADO",GPIO.input(19)
                        message_body = "PROPANO DETECTADO!!!"
                        result = push_service.notify_single_device(registration_id=registration_id, message_body=message_body, sound="warning_siren")
                        config_pyrebase.db.child("casa/status").update({"co2": str(round(Mq2*100/5,2))})  #actualizo alarma
                        time.sleep(3)
                    else:
                        print "NO PROPANO",GPIO.input(19)
                        config_pyrebase.db.child("casa/status").update({"co2": str(round(Mq2*100/5,2))})  #actualizo alarma
                        time.sleep(3)

    except KeyboardInterrupt:
        print "Exiting Program"

    except:
        print "Error Occurs, Exiting Program"

    finally:
        ser.close()
        pass
