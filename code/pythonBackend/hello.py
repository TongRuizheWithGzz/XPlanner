from flask import Flask,request
from MyQR import myqr
import os
app = Flask(__name__)

@app.route('/getQrcode',methods=["GET"])
def getQrcode():
    openId=request.args.get('openId')
    version, level, qr_name = myqr.run(
	"https://jaccount.sjtu.edu.cn/oauth2/authorize?scope=basic+essential+lessons+classes+exams&response_type=code&redirect_uri=http%3A%2F%2Flocalhost%3A8082%2Fapi%2FloginByJAccount%2Fauthorize&state="+openId+"&client_id=ajebOnLZZi7Uk7y3Jbze",
    version=1,
    level='H',
    picture=None,
    colorized=True,
    contrast=1.0,
    brightness=1.0,
    save_name=None,
    save_dir=os.getcwd()
	)
   
    return "helloworld"
