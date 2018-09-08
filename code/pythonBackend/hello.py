from flask import Flask,request
from MyQR import myqr
import urllib
import json
import os

#Localhost is with port

localhost="http://10.162.118.2:8082"
app = Flask(__name__)

@app.route('/getQrcode',methods=["GET"])
def getQrcode():

    #query parameters
    authorizeUrl="https://jaccount.sjtu.edu.cn/oauth2/authorize?"
    openId=request.args.get('openId')
    scope="basic essential lessons classes exams"
    response_type="code"
    client_id="ajebOnLZZi7Uk7y3Jbze"
    redirect_uri=localhost+"/api/auth/authorize"

    #build url
    params={"scope":scope,"response_type":response_type,"redirect_uri":redirect_uri,"state":openId,"client_id":client_id}

    url=authorizeUrl+urllib.parse.urlencode(params)
    
    print("Build url successfully: "+url)


    #Generate the qrCode
    version, level, qr_name = myqr.run(
    url,
    level='H',
    picture=None,
    colorized=True,
    contrast=1.0,
    brightness=1.0,
    save_name=openId+".png",
    save_dir=os.getcwd()+'/static'
	)
    return json.dumps({"errMsg":"success","errno":0})

if __name__ == '__main__':
    app.run(host='0.0.0.0',debug=True)
