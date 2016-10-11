package project.mayikai.tracer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by Administrator on 2016/10/11.
 */
public class showInformation extends Activity {

    String friend_name;
    String friend_number;
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

        final Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        friend_name = bundle.getString("name");
        friend_number = bundle.getString("number");

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


    }
}
