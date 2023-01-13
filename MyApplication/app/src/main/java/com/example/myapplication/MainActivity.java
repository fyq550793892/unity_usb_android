package com.example.myapplication;

import android.content.Context;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.cece.VR.R;
import com.hoho.android.usbserial.driver.UsbSerialDriver;
import com.hoho.android.usbserial.driver.UsbSerialPort;
import com.hoho.android.usbserial.driver.UsbSerialProber;
import com.unity3d.player.UnityPlayer;

import java.nio.charset.StandardCharsets;
import java.util.List;

public class MainActivity extends UnityPlayerActivity {
    private static final String TAG = "MainActivity";
    Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        button = findViewById(R.id.button);
////        button.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////                UnityCallAndroid();
////            }
////        });
    }

    //unity调用Android
    public void UnityCallAndroid () {

        Toast.makeText(this,"unity调用android成功", Toast.LENGTH_LONG).show();

        UsbManager manager = (UsbManager) getSystemService(Context.USB_SERVICE);
        List<UsbSerialDriver> availableDrivers = UsbSerialProber.getDefaultProber().findAllDrivers(manager);
        if (availableDrivers.isEmpty()) {
            Toast.makeText(this,"usb列表为空", Toast.LENGTH_LONG).show();
            return;
        } else {
            Toast.makeText(this,"usb列表不为空", Toast.LENGTH_LONG).show();
        }

        // Open a connection to the first available driver.
        UsbSerialDriver driver = availableDrivers.get(0);
        UsbDeviceConnection connection = manager.openDevice(driver.getDevice());
        if (connection == null) {
            // add UsbManager.requestPermission(driver.getDevice(), ..) handling here
            return;
        }

        UsbSerialPort port = driver.getPorts().get(0); // Most devices have just one port (port 0)
        try {
            port.open(connection);
            port.setParameters(115200, 8, UsbSerialPort.STOPBITS_1, UsbSerialPort.PARITY_NONE);
            port.write("aaa".getBytes(StandardCharsets.UTF_8), 1000);
//                    len = port.read(response, READ_WAIT_MILLIS);
        } catch (Exception e) {
            Log.d(TAG, "eeee" + e);
        }

        AndroidCallUnity();
    }

    //android调用unity
    public void AndroidCallUnity () {

        //第1个参数为Unity场景中用于接收android消息的对象名称
        //第2个参数为对象上的脚本的一个成员方法名称（脚本名称不限制）
        //第3个参数为unity方法的参数
        UnityPlayer.UnitySendMessage("receiveObj", "UnityMethod", "This is args.");
    }

}
