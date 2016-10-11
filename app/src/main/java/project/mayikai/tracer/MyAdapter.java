package project.mayikai.tracer;


import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/10/9.
 */
public class MyAdapter extends BaseAdapter {

    private List<Item> data = new ArrayList<>();
    private Context context;


    public MyAdapter(Context context,ArrayList<Item> list) {
        this.context = context;
        this.data = list;
    }

    @Override
    public int getViewTypeCount(){
        return 1;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    public View getView(final int position, View convertView,
                        final ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView == null){
            //实例化
            holder = new ViewHolder();
            convertView = View.inflate(context,R.layout.list_item,null);
            holder.delete = (ImageView) convertView.findViewById(R.id.delete);
            holder.friend = (ImageView) convertView.findViewById(R.id.friend);
            holder.name = (TextView) convertView.findViewById(R.id.name);
            convertView.setTag(R.id.tag,holder);
        }else{
            holder = (ViewHolder) convertView.getTag(R.id.tag);
        }

        holder.delete.setTag(position);
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog dialog = new AlertDialog.Builder(MyAdapter.this.context)
                        .setTitle("DELETE FRIEND")
                        .setMessage("确定移除好友？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                data.remove(position);
                                saveObject("friendsList.dat");
                                notifyDataSetChanged();
                            }
                        }).setNegativeButton("取消",null)
                        .create();
                dialog.show();
            }
        });

        Item o = data.get(position);



        holder.friend.setImageResource(R.drawable.icon_friend);
        holder.delete.setImageResource(R.drawable.icon_delete);
        holder.name.setText(o.getName());
        holder.name.setTextColor(Color.GREEN);

        return convertView;
    }

    private static class ViewHolder{
        ImageView friend;
        ImageView delete;
        TextView name;
    }

    //存放list
    public void saveObject(String name) {
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        try {
            fos = MyAdapter.this.context.openFileOutput(name, MyAdapter.this.context.MODE_PRIVATE);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(data);
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


