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
 * Created by Administrator on 2016/10/16.
 */
public class showEnemiesInformation extends Activity {
    String enemy_name;
    String enemy_number;
    String enemy_location;
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
    Button friends_list;
    Button back_to_list;
    Button edit;
    ImageView delete;


    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.enemy_information);


        name = (EditText) this.findViewById(R.id.enemy_name);
        number = (EditText) this.findViewById(R.id.enemy_number);
        location = (EditText) this.findViewById(R.id.enemy_location);
        altitude = (EditText) this.findViewById(R.id.enemy_altitude);
        address = (EditText) this.findViewById(R.id.enemy_address);
        accuracy = (EditText) this.findViewById(R.id.enemy_accuracy);
        last_update = (EditText) this.findViewById(R.id.enemy_last_update);
        next_update = (EditText) this.findViewById(R.id.enemy_next_update);
        radar = (Button) this.findViewById(R.id.radar);
        back_to_list = (Button) this.findViewById(R.id.back_to_list);
        edit = (Button) this.findViewById(R.id.edit);
        delete = (ImageView) this.findViewById(R.id.enemy_delete);

        final Intent intent = getIntent();
        final Bundle bundle = intent.getExtras();
        enemy_name = bundle.getString("name");
        enemy_number = bundle.getString("number");
        enemy_location = bundle.getString("location");
        position = Integer.parseInt(bundle.getString("position"));

        name.setText(enemy_name);
        number.setText(enemy_number);
        location.setText(enemy_location);

        radar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(showEnemiesInformation.this, MainActivity.class);
                startActivity(intent1);
            }
        });

        friends_list = (Button)this.findViewById(R.id.friends_list);
        friends_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(showEnemiesInformation.this,FriendsList.class);
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
                String LOCATION = location.getText().toString();
                bundle1.putSerializable("name",NAME);
                bundle1.putSerializable("number",NUMBER);
                bundle1.putSerializable("location",LOCATION);
                bundle1.putSerializable("position",Integer.toString(position));
                intent.putExtras(bundle1);
                showEnemiesInformation.this.setResult(RESULT_OK, intent);
                showEnemiesInformation.this.finish();
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog dialog = new AlertDialog.Builder(showEnemiesInformation.this)
                        .setTitle("DELETE ENEMY")
                        .setMessage("确定移除敌人？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Bundle bundle1 = new Bundle();
                                bundle1.putSerializable("position",Integer.toString(position));
                                intent.putExtras(bundle1);
                                showEnemiesInformation.this.setResult(666, intent);
                                showEnemiesInformation.this.finish();
                            }
                        }).setNegativeButton("取消",null)
                        .create();
                dialog.show();
            }
        });
    }
}
