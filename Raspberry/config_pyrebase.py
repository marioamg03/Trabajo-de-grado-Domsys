import pyrebase

#Firebase Configuration
config = {
  "apiKey": "AIzaSyC-3FMsYm31PvrmPkhvHwV785fLceqx8Sc",
  "authDomain": "tesis-domsys.firebaseapp.com",
  "databaseURL": "https://tesis-domsys.firebaseio.com",
  "storageBucket": "tesis-domsys.appspot.com",
  "serviceAccount": "/home/pi/Tesis/tesis-domsys-firebase-adminsdk-bkvxa-846d0cd938.json"
}

firebase = pyrebase.initialize_app(config)
print ("Conexion Pyrebase Lista")

#Firebase Database Intialization
db = firebase.database ()
