package project.mayikai.tracer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

/**
 * Created by Administrator on 2016/10/13.
 */
public class SMSBroadcastReceiver extends BroadcastReceiver {
    static String BODY;
    private Context context;
    @Override
    public void onReceive(Context context,Intent intent){
        this.context = context;
        Object[] pdus = (Object[]) intent.getExtras().get("pdus"); //接受数据
        for(Object p:pdus){
            byte[] pdu = (byte[])p;
            SmsMessage message = SmsMessage.createFromPdu(pdu); //根据获得的byte[]封装成SmsMessage
            String body = message.getMessageBody();
            String sender = message.getOriginatingAddress();
            if(body.equals("where are you")){
                SmsManager manager = SmsManager.getDefault();
                String myLocation = String.valueOf(MainActivity.myLatitude) + "/" +
                        String.valueOf(MainActivity.myLongitude);
                ArrayList<String> list = manager.divideMessage(myLocation);
                for (String text : list)
                    manager.sendTextMessage(sender,null, text, null, null);
            }else{
                for(int i = 0;i < FriendsList.myFriends.size();i++){
                    if ((FriendsList.myFriends.get(i).getNumber()).equals(sender)){
                        FriendsList.myFriends.get(i).setLocation(body);
                        saveObject("friendsList.dat");
                    }
                }
            }
        }
    }

    //存放list
    public void saveObject(String name) {
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        try {
            fos = SMSBroadcastReceiver.this.context.openFileOutput(name, SMSBroadcastReceiver.this.context.MODE_PRIVATE);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(FriendsList.myFriends);
        } catch (Exception e) {
            e.printStackTrace();
            //这里是保存文件产生异常
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    //fos流关闭异常
                    e.printStackTrace();
                }
            }
            if (oos != null) {
                try {
                    oos.close();
                } catch (IOException e) {
                    //oos流关闭异常
                    e.printStackTrace();
                }
            }
        }
    }
}
