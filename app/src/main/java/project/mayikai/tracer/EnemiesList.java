package project.mayikai.tracer;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

/**
 * Created by Administrator on 2016/10/15.
 */
public class EnemiesList extends Activity {
    ListView enemiesList;
    Button add;
    Button radar;
    Button friends_list;
    EditText new_name;
    EditText new_number;
    MyAdapter2 myAdapter2;
    String name;
    String number;
    static ArrayList<Item> myEnemies;
    private int RequeseCode = 1500;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.enemies_list);

        enemiesList = (ListView) this.findViewById(R.id.enemiesList);
        add = (Button) this.findViewById(R.id.add_enemy);
        radar = (Button) this.findViewById(R.id.radar);

        try{
            myEnemies = (ArrayList<Item>) getObject("enemiesList.dat");
        }catch (NullPointerException e){
            e.printStackTrace();
        }
        if(null == myEnemies) {
            myEnemies = new ArrayList<Item>();
        }
        saveObject("enemiesList.dat");
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LayoutInflater factory = LayoutInflater.from(EnemiesList.this);
                //得到自定义对话框
                final View DialogView = factory.inflate(R.layout.add_friend, null);

                new_name = (EditText) DialogView.findViewById(R.id.new_name);
                new_number = (EditText) DialogView.findViewById(R.id.new_number);

                AlertDialog dialog = new AlertDialog.Builder(EnemiesList.this)
                        .setTitle("ADD ENEMY")
                        .setView(DialogView)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                name = new_name.getText().toString();
                                number = new_number.getText().toString();
                                Item item = new Item();
                                item.setName(name);
                                item.setNumber(number);
                                myEnemies.add(item);
                                saveObject("enemiesList.dat");
                            }
                        }).setNegativeButton("取消", null)
                        .create();
                dialog.show();
            }
        });

        myAdapter2 = new MyAdapter2(EnemiesList.this, myEnemies);
        enemiesList.setAdapter(myAdapter2);

        enemiesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                Intent intent = new Intent(EnemiesList.this, showEnemiesInformation.class);
                bundle.putSerializable("name", myEnemies.get(position).getName());
                bundle.putSerializable("number", myEnemies.get(position).getNumber());
                bundle.putSerializable("location", myEnemies.get(position).getLocation());
                bundle.putSerializable("position", Integer.toString(position));
                intent.putExtras(bundle);
                startActivityForResult(intent, RequeseCode);
            }
        });

        radar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EnemiesList.this,MainActivity.class);
                startActivity(intent);
            }
        });

        friends_list = (Button) this.findViewById(R.id.friends_list);
        friends_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EnemiesList.this,FriendsList.class);
                startActivity(intent);
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        final int position;
        switch (resultCode) {
            case RESULT_OK: {
                Bundle bundle = data.getExtras();
                String NAME = bundle.getString("name");
                String number = bundle.getString("number");
                String location = bundle.getString("location");
                position = Integer.parseInt(bundle.getString("position"));
                myEnemies.get(position).setName(NAME);
                myEnemies.get(position).setNumber(number);
                myEnemies.get(position).setLocation(location);
                saveObject("enemiesList.dat");
                myAdapter2 = new MyAdapter2(EnemiesList.this, myEnemies);
                enemiesList.setAdapter(myAdapter2);
                break;
            }
            case 666: {
                Bundle bundle = data.getExtras();
                position = Integer.parseInt(bundle.getString("position"));
                myEnemies.remove(position);
                saveObject("enemiesList.dat");
                myAdapter2 = new MyAdapter2(EnemiesList.this, myEnemies);
                enemiesList.setAdapter(myAdapter2);
                break;
            }
            default:
                break;
        }
    }

    //存放list
    public void saveObject(String name) {
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        try {
            fos = this.openFileOutput(name, MODE_PRIVATE);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(myEnemies);
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

    @SuppressWarnings("unchecked")
    public Object getObject(String name) {
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        try {
            fis = this.openFileInput(name);
            ois = new ObjectInputStream(fis);
            return ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
            //这里是读取文件产生异常
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    //fis流关闭异常
                    e.printStackTrace();
                }
            }
            if (ois != null) {
                try {
                    ois.close();
                } catch (IOException e) {
                    //ois流关闭异常
                    e.printStackTrace();
                }
            }
        }
        //读取产生异常，返回null
        return null;
    }
}
