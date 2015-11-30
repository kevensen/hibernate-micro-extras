oc secrets new eap-app-secret keystore.jks 
oc secrets add sa/eap-service-account secret/eap-app-secret
