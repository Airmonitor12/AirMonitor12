package app.embinsys.airmonitor;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Monitor extends AppCompatActivity {
    private Handler mHandler = new Handler(Looper.getMainLooper());

    public static ServerSocket serverSocket;
    private Socket socket;
    TextView box1, box2, box3, box4, box5, box6;
    float valCO2 = 0f;
    float valPM2 = 0f;
    float valHumidity = 0f;
    float valTemp = 0f;
    float valVOC = 0f;
    float valHCHO = 0f;
    private final int portNum = 5000;
    private DataInputStream inputStream;
    private DataOutputStream outputStream;
    private TextView txtClientStatus;

    private Button btnSendData;
    private ImageView user_icon;


    private byte[] rdBuffer = new byte[64];
    private View view;


    // Class for receiving data from socket
    class SocketReceive implements Runnable {

        @Override
        public void run() {
            try {
                while (true) {
                    int ret = inputStream.read(rdBuffer);
                  //  Log.v("Ret:", String.valueOf(ret));
                    if (ret > 0) {
                        for (int i = 0; i < ret; ++i) {
                            final byte b = rdBuffer[i];

                            Log.v("ReadBuffer:", String.valueOf(b));
                            Log.v("ReadBuffer:", Integer.toHexString(b));

                            runOnUiThread(() -> {
                                valCO2 = ((int) (rdBuffer[3] & 0xff) * 256) + (int) (rdBuffer[4] & 0xff);
                                box1.setText("CO2 " + Float.toString(valCO2) + "ppm");

                                valPM2 = ((int) (rdBuffer[5] & 0xff) * 256) + (int) (rdBuffer[6] & 0xff);
                                box2.setText("PM2.5 " + Float.toString(valPM2) + "ug/m3");


                                valHumidity = ((int) ((rdBuffer[11] & 0xff) * 256) + (int) (rdBuffer[12] & 0xff)) / (100);
                                box3.setText("Humidity " + Float.toString(valHumidity) + "%");


                                valTemp = ((int) ((rdBuffer[13] & 0xff) * 256) + (int) (rdBuffer[14] & 0xff)) / (100);
                                box4.setText("Temperature " + Float.toString(valTemp) + (char) 0x00B0 + "C");

                                valVOC = (((int) (rdBuffer[7] & 0xff) * 256) + (int) (rdBuffer[8] & 0xff)) / 1000;
                                box5.setText("VOC " + Float.toString(valVOC));

                                valHCHO = (((int) (rdBuffer[9] & 0xff) * 256) + (int) (rdBuffer[10] & 0xff)) / 1000;
                                box6.setText("HCHO " + Float.toString(valHCHO));
                            });
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    class ConnectClient implements Runnable {
        @Override
        public void run() {
            try {
                serverSocket = new ServerSocket(portNum);
                runOnUiThread(() -> txtClientStatus.setText("Client Status: Waiting for client..."));
                socket = serverSocket.accept();
                runOnUiThread(() -> txtClientStatus.setText("Client Status: Client connected"));
                    inputStream = new DataInputStream(socket.getInputStream());
                    outputStream = new DataOutputStream(socket.getOutputStream());
                    new Thread(new SocketReceive()).start();
            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> txtClientStatus.setText("Client Status: Client failed to connect"));
            }
        }
    }

    // Class for sending data over socket
    class SocketSend implements Runnable {

        @Override
        public void run() {
            try {
                outputStream.write(new byte[]{0x00, 0x04, 0x00, 0x01, 0x00, 0x06, 0x20, 0x19});
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private Runnable RequestCommand = new Runnable() {
        @Override
        public void run() {
            new Thread(new SocketSend()).start();
            mHandler.postDelayed(this, 2000);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       //To remove the title bar
        getSupportActionBar().hide();

        setContentView(R.layout.activity_monitor);

        ImageView user_icon=(ImageView) findViewById(R.id.user_icon);
        user_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Monitor.this, Profile.class);
                startActivity(i);


            }
        });

        box1 = findViewById(R.id.box1);
        box2 = findViewById(R.id.box2);
        box3 = findViewById(R.id.box3);
        box4 = findViewById(R.id.box4);
        box5 = findViewById(R.id.box5);
        box6 = findViewById(R.id.box6);
        txtClientStatus = findViewById(R.id.txtClientStatus);

       // btnSendData = findViewById(R.id.btnSendData);
        new Thread(new ConnectClient()).start();

      //  btnSendData.setOnClickListener(view -> {
            mHandler.postDelayed(RequestCommand, 2000);
//            new Thread(new SocketSend()).start();
       // });


        }
}
