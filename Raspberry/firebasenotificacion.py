from pyfcm import FCMNotification

push_service = FCMNotification(api_key="AIzaSyAsdTiJ-kRj5J0QMqPK5igDZYMIRO556sI")

registration_id = "e5SWXFOeKIA:APA91bH3RGguRdm7tKtzWKvLwGDDRU5jE3ETOmBP_cCjIonULShb46wwJNOffnfYcgBxpGp_DvfkQYbMQQbXC98vCQTkQCrO4MakXa09eANfdZApKA8sR3yU6Ny5AsuXDnQi6om2CFEV"

message_body = "MENSAJE DE EMERGENCIA"

result = push_service.notify_single_device(registration_id=registration_id, message_body=message_body, data_message=data_message, sound="warning_siren")

print result
