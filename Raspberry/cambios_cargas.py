import RPi.GPIO as GPIO
import pyrebase
import config_pyrebase
from time import sleep
import time

GPIO.setwarnings(False)
GPIO.setmode(GPIO.BCM)

GPIO.setup(20, GPIO.OUT)                   #PWM VENTILADOR
p = GPIO.PWM(20,207)
p.start(0)

GPIO.setup(21, GPIO.OUT)                  #PWM PUERTA GARAJE
a = GPIO.PWM(21,50)                       #Ponemos el pin 21 en modo PWM y enviamos 50 pulsos por segundo
a.start(21.5)                             #Enviamos un pulso del 7.5% para centrar el servo


def principal():
#>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> STREAM LUCES

    def stream_handlerluz(message):
        auto_mode_casa = config_pyrebase.db.child("casa/auto-mode/luces/habilitado").get()
        if auto_mode_casa.val() == "apagado": #actualizar lucez normal
            #Get value of luzBano
            luz_Bano = config_pyrebase.db.child("casa/status/luces/bano").get() #-------------------------------// Get Values //--------------------------------*
            #Get value of luzCocina
            luz_Cocina = config_pyrebase.db.child("casa/status/luces/cocina").get()
            #Get value of luzCuarto
            luz_Cuarto = config_pyrebase.db.child("casa/status/luces/cuarto").get()
            #Get value of luzGaraje
            luz_Garaje = config_pyrebase.db.child("casa/status/luces/garaje").get()
            #Get value of luzSala
            luz_Sala = config_pyrebase.db.child("casa/status/luces/sala").get()

            print ("")
            print ("----------- EL ESTADO DE LAS LUCES ES -----------")         #-------------------------------// Set Values //--------------------------------*
            print ("")


            #Set value of luzBano
            if luz_Bano.val() == "encendido":
                GPIO.output(22, True) ## Enciendo el 17
            else:
                GPIO.output(22, False) ## Apago el 17

            #Set value of luzCocina
            if luz_Cocina.val() == "encendido":
                GPIO.output(5, True) ## Enciendo el 27
            else:
                GPIO.output(5, False) ## Apago el 27

            #Set value of luzCuarto
            if luz_Cuarto.val() == "encendido":
                GPIO.output(6, True) ## Enciendo el 22
            else:
                GPIO.output(6, False) ## Apago el 22

            #Set value of luzGaraje
            if luz_Garaje.val() == "encendido":
                GPIO.output(13, True) ## Enciendo el 5
            else:
                GPIO.output(13, False) ## Apago el 5

            #Set value of luzSala
            if luz_Sala.val() == "encendido":
                GPIO.output(27, True) ## Enciendo el 13
            else:
                GPIO.output(27, False) ## Apago el 13

            #Imprimo el valor de Luz Bano
            print ("Salida luz_Bano: ")
            print (luz_Bano.val())

            #Imprimo el valor de Luz Cocina
            print ("Salida luz_Cocina: ")
            print(luz_Cocina.val())

            #Imprimo el valor de Luz Cuarto
            print ("Salida luz_Cuarto")
            print (luz_Cuarto.val())

            #Imprimo el valor de Luz Garaje
            print ("Salida luz_Garaje")
            print (luz_Garaje.val())

            #Imprimo el valor de Luz Sala
            print ("Salida luz_Sala")
            print (luz_Sala.val())

            print ("")
            print ("----------- ESTADO DE LAS LUCES FIN -----------")
        auto_mode_casa_patio = config_pyrebase.db.child("casa/auto-mode/patio/habilitado").get()
        if auto_mode_casa_patio.val() == "apagado": #actualizar lucez normal
            luz_Patio = config_pyrebase.db.child("casa/status/luces/patio").get() #Get value of luzPatio
            #Set value of luzPatio
            if luz_Patio.val() == "encendido":
                GPIO.output(17, True) ## Enciendo el 6
            else:
                GPIO.output(17, False) ## Apago el 6
            #Imprimo el valor de Luz Patio
            print ("Salida luz_Patio")
            print (luz_Patio.val())
    global my_streamLuz
    my_streamLuz = config_pyrebase.db.child ("casa/status/luces").stream (stream_handlerluz)
    print ("inicio el streaming Luz")

    def stream_handlerAutoLuz(message):
        auto_mode_casa = config_pyrebase.db.child("casa/auto-mode/luces/habilitado").get()
        if auto_mode_casa.val() == "encendido": #actualizar lucez normal
            hora_encendido_luz = config_pyrebase.db.child("casa/auto-mode/luces/encendido").get()
            hora_apagado_luz = config_pyrebase.db.child("casa/auto-mode/luces/apagado").get()
            print ("")
            print ("----------- AUTO - LUCES - ON -----------")         #-------------------------------// Set Auto Luces //--------------------------------*
            print ("")
            while auto_mode_casa.val() == "encendido":
                if ( hora_encendido_luz.val() <= time.strftime('%H:%M')) and (hora_apagado_luz.val() >= time.strftime('%H:%M')) :
                        #Set value of luzBano
                        GPIO.output(22, True) ## Enciendo el 17
                        #Set value of luzCocina
                        GPIO.output(5, True) ## Enciendo el 27
                        #Set value of luzCuarto
                        GPIO.output(6, True) ## Enciendo el 22
                        #Set value of luzGaraje
                        GPIO.output(13, True) ## Enciendo el 5
                        #Set value of luzSala
                        GPIO.output(27, True) ## Enciendo el 13
                        print ("Autolucez habilitado")
                else:
                        #Set value of luzBano
                        GPIO.output(22, False) ## Enciendo el 17
                        #Set value of luzCocina
                        GPIO.output(5, False) ## Enciendo el 27
                        #Set value of luzCuarto
                        GPIO.output(6, False) ## Enciendo el 22
                        #Set value of luzGaraje
                        GPIO.output(13, False) ## Enciendo el 5
                        #Set value of luzSala
                        GPIO.output(27, False) ## Enciendo el 13
                        print ("Autolucez Inahabilitado")
                time.sleep(15)
                hora_encendido_luz = config_pyrebase.db.child("casa/auto-mode/luces/encendido").get()
                hora_apagado_luz = config_pyrebase.db.child("casa/auto-mode/luces/apagado").get()
                auto_mode_casa = config_pyrebase.db.child("casa/auto-mode/luces/habilitado").get()
    global my_streamAutoLuz
    my_streamAutoLuz = config_pyrebase.db.child ("casa/auto-mode/luces/habilitado").stream (stream_handlerAutoLuz)
    print ("inicio el streaming AutoLuz")

    def stream_handlerAutoLuzPatio(message):
        auto_mode_casa_patio = config_pyrebase.db.child("casa/auto-mode/patio/habilitado").get()
        if auto_mode_casa_patio.val() == "encendido": #actualizar lucez normal
            hora_encendido_luz_patio = config_pyrebase.db.child("casa/auto-mode/patio/encendido").get()
            hora_apagado_luz_patio = config_pyrebase.db.child("casa/auto-mode/patio/apagado").get()
            print ("")
            print ("----------- AUTO - LUCES PATIO - ON -----------")         #-------------------------------// Set Auto Luces //--------------------------------*
            print ("")
            while auto_mode_casa_patio.val() == "encendido":
                if ( hora_encendido_luz_patio.val() <= time.strftime('%H:%M')) and (hora_apagado_luz_patio.val() >= time.strftime('%H:%M')) :
                    GPIO.output(17, True) ## Enciendo el 6
                    print ("Autoluzpatio habilitado")
                else:
                    GPIO.output(17, False) ## Enciendo el 6
                    print ("Autoluzpatio Inahabilitado")
                time.sleep(15)
                hora_encendido_luz_patio = config_pyrebase.db.child("casa/auto-mode/patio/encendido").get()
                hora_apagado_luz_patio = config_pyrebase.db.child("casa/auto-mode/patio/apagado").get()
                auto_mode_casa_patio = config_pyrebase.db.child("casa/auto-mode/patio/habilitado").get()
    global my_streamAutoLuzPatio
    my_streamAutoLuzPatio = config_pyrebase.db.child ("casa/auto-mode/patio/habilitado").stream (stream_handlerAutoLuzPatio)
    print ("inicio el streaming AutoLuzPatio")


#>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> STREAM ASPERSORES
    def stream_handlerAspersores(message):

        auto_mode_casa_aspersores = config_pyrebase.db.child("casa/auto-mode/aspersores/habilitado").get()
        if auto_mode_casa_aspersores.val() == "apagado": #actualizar lucez normal
            aspersores_Estado = config_pyrebase.db.child("casa/status/aspersores").get() #-------------------------------// Get Values //--------------------------------*
            print ("")
            print ("----------- EL ESTADO DE LOS ASPERSORES INICIO -----------") #-------------------------------// Set Values //--------------------------------*
            print ("")
            #Set value of Aspersores
            if aspersores_Estado.val() == "activados":
                GPIO.output(4, True) ## Enciendo el 17
            else:
                GPIO.output(4, False) ## Apago el 17
            print ("Salida aspersores: ")
            print (aspersores_Estado.val())
            print ("")
            print ("----------- ESTADO DE ASPERSORES FIN -----------")
    global my_streamAspersores
    my_streamAspersores = config_pyrebase.db.child ("casa/status/aspersores").stream (stream_handlerAspersores)
    print ("inicio el streaming Aspersores")

    def stream_handlerAutoAspersores(message):
        auto_mode_casa_aspersores = config_pyrebase.db.child("casa/auto-mode/aspersores/habilitado").get()
        if auto_mode_casa_aspersores.val() == "encendido": #actualizar lucez normal
            hora_encendido_aspersores = config_pyrebase.db.child("casa/auto-mode/aspersores/encendido").get()
            hora_apagado_aspersores = config_pyrebase.db.child("casa/auto-mode/aspersores/apagado").get()
            print ("")
            print ("----------- AUTO - ASPERSORES - ON -----------")         #-------------------------------// Set Auto Luces //--------------------------------*
            print ("")
            while auto_mode_casa_aspersores.val() == "encendido":
                if ( hora_encendido_aspersores.val() <= time.strftime('%H:%M')) and (hora_apagado_aspersores.val() >= time.strftime('%H:%M')) :
                    GPIO.output(4, True) ## Enciendo el 17
                    print ("AutoAspersores habilitado")
                else:
                    GPIO.output(4, False) ## Enciendo el 17
                    print ("AutoAspersores Inahabilitado")
                time.sleep(15)
                hora_encendido_aspersores = config_pyrebase.db.child("casa/auto-mode/aspersores/encendido").get()
                hora_apagado_aspersores = config_pyrebase.db.child("casa/auto-mode/aspersores/apagado").get()
                auto_mode_casa_aspersores = config_pyrebase.db.child("casa/auto-mode/aspersores/habilitado").get()
    global my_streamAutoAspersores
    my_streamAutoAspersores = config_pyrebase.db.child ("casa/auto-mode/aspersores/habilitado").stream (stream_handlerAutoAspersores)
    print ("inicio el streaming Auto-Aspersores")
#>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> STREAM PUERTA GARAJE
    def stream_handlerPuertaGaraje(message):

        puertagaraje_Estado = config_pyrebase.db.child("casa/status/pgaraje").get() #Get value of Pgaraje

        print ("")
        print ("----------- EL ESTADO DE PUERTA GARAJE INICIO -----------")
        print ("")

        if puertagaraje_Estado.val() == "abierta":
            a.ChangeDutyCycle(2.5)
            time.sleep(1)
            print("Abierta la puerta del garaje")## Abrir puerta!
        else:
            a.ChangeDutyCycle(12.5)
            time.sleep(1)
            print("Cerrada la puerta del garaje")## Cerrar puerta!
        print ("Salida Puerta Garaje: ")
        print (puertagaraje_Estado.val())
        print ("")
        print ("----------- ESTADO DE PUERTA GARAJE FIN -----------")
    global my_streamPuertaGaraje
    my_streamPuertaGaraje = config_pyrebase.db.child ("casa/status/pgaraje").stream (stream_handlerPuertaGaraje)
    print ("inicio el streaming Puerta Garaje")
#>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> STREAM VENTILACION
    def stream_handlerVentilacion(message):
        auto_mode_casa_ventilacion = config_pyrebase.db.child("casa/auto-mode/ventilacion/habilitado").get()
        if auto_mode_casa_ventilacion.val() == "apagado": #actualizar lucez normal
            ventilacion_Estado = config_pyrebase.db.child("casa/status/ventilacion").get() #Get value of Ventilacion

            print ("")
            print ("----------- EL ESTADO DE VENTILACION INICIO -----------")
            print ("")

            #Set value of int(ventilacion_Estado)
            p.ChangeDutyCycle(int(ventilacion_Estado.val()))

            print ("Salida Ventilacion: ")
            print (ventilacion_Estado.val())
            print ("")
            print ("----------- ESTADO DE VENTILACION FIN -----------")
    global my_streamVentilacion
    my_streamVentilacion = config_pyrebase.db.child ("casa/status/ventilacion").stream (stream_handlerVentilacion)
    print ("inicio el streaming Ventilacion")
    def stream_handlerAutoVentilacion(message):
        auto_mode_casa_ventilacion = config_pyrebase.db.child("casa/auto-mode/ventilacion/habilitado").get()
        temp_hogar = config_pyrebase.db.child("casa/status/temperatura").get()
        set_point_ventilacion = config_pyrebase.db.child("casa/auto-mode/ventilacion/set_point").get()

        if auto_mode_casa_ventilacion.val() == "encendido": #actualizar lucez normal

            print ("")
            print ("----------- AUTO - VENTILACION - ON -----------")         #-------------------------------// Set Auto Luces //--------------------------------*
            print ("")
            while auto_mode_casa_aspersores.val() == "encendido":
                p.ChangeDutyCycle(int(set_point_ventilacion.val())*100/30)
                time.sleep(15)
                temp_hogar = config_pyrebase.db.child("casa/auto-mode/aspersores/encendido").get()
                set_point_ventilacion = config_pyrebase.db.child("casa/auto-mode/aspersores/apagado").get()
                auto_mode_casa_ventilacion = config_pyrebase.db.child("casa/auto-mode/ventilacion/habilitado").get()
    global my_streamAutoVentilacion
    my_streamAutoVentilacion = config_pyrebase.db.child ("casa/auto-mode/ventilacion/habilitado").stream (stream_handlerAutoVentilacion)
    print ("inicio el streaming Auto-Ventilacion")

#>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> STREAM ALARMA PIR
    def stream_handlerAlarma(message):
        alarma_Estado = config_pyrebase.db.child("casa/status/alarma").get()#Get value of Alarma
        print ("")
        print ("----------- EL ESTADO DE ALARMA INICIO -----------")
        print ("")
        print ("Salida Alarma: ")
        print (alarma_Estado.val())
        print ("")
        print ("----------- ESTADO DE ALARMA FIN -----------")
    global my_streamAlarma
    my_streamAlarma = config_pyrebase.db.child ("casa/status/alarma").stream (stream_handlerAlarma)
    print ("inicio el streaming Alarma")
