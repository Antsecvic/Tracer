package project.mayikai.tracer;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

/**
 * Created by Administrator on 2016/10/11.
 */
public class showInformation extends Activity {

    String friend_name;
    String friend_number;
    int position;
    EditText number;
    EditText location;
    EditText altitude;
    EditText accuracy;
    EditText address;
    EditText last_update;
    EditText next_update;
    EditText name;
    Button radar;
    Button back_to_list;
    Button edit;
    ImageView delete;


    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friend_information);


        name = (EditText) this.findViewById(R.id.friend_name);
        number = (EditText) this.findViewById(R.id.friend_number);
        location = (EditText) this.findViewById(R.id.friend_location);
        altitude = (EditText) this.findViewById(R.id.friend_altitude);
        address = (EditText) this.findViewById(R.id.friend_address);
        accuracy = (EditText) this.findViewById(R.id.friend_accuracy);
        last_update = (EditText) this.findViewById(R.id.friend_last_update);
        next_update = (EditText) this.findViewById(R.id.friend_next_update);
        radar = (Button) this.findViewById(R.id.radar);
        back_to_list = (Button) this.findViewById(R.id.back_to_list);
        edit = (Button) this.findViewById(R.id.edit);
        delete = (ImageView) this.findViewById(R.id.friend_delete);

        final Intent intent = getIntent();
        final Bundle bundle = intent.getExtras();
        friend_name = bundle.getString("name");
        friend_number = bundle.getString("number");
        position = Integer.parseInt(bundle.getString("position"));

        name.setText(friend_name);
        number.setText(friend_number);

        radar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(showInformation.this, MainActivity.class);
                startActivity(intent1);
            }
        });

        back_to_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle1 = new Bundle();
                String NAME = name.getText().toString();
                String NUMBER = number.getText().toString();
                bundle1.putSerializable("name",NAME);
                bundle1.putSerializable("number",NUMBER);
                bundle1.putSerializable("position",Integer.toString(position));
                intent.putExtras(bundle1);
                showInformation.this.setResult(RESULT_OK, intent);
                showInformation.this.finish();
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog dialog = new AlertDialog.Builder(showInformation.this)
                        .setTitle("DELETE FRIEND")
                        .setMessage("确定移除好友？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Bundle bundle1 = new Bundle();
                                bundle1.putSerializable("position",Integer.toString(position));
                                intent.putExtras(bundle1);
                                showInformation.this.setResult(666, intent);
                                showInformation.this.finish();
                            }
                        }).setNegativeButton("取消",null)
                        .create();
                dialog.show();
            }
        });
    }
}
