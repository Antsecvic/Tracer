package project.mayikai.tracer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.map.TextOptions;
import com.baidu.mapapi.model.LatLng;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 此demo用来展示如何结合定位SDK实现定位，并使用MyLocationOverlay绘制定位位置 同时展示如何使用自定义图标绘制并点击时弹出泡泡
 */
public class MainActivity extends Activity {

    // 定位相关
    LocationClient mLocClient;
    public MyLocationListenner myListener = new MyLocationListenner();
    private MyLocationConfiguration.LocationMode mCurrentMode;
    BitmapDescriptor mCurrentMarker;

    MapView mMapView;
    BaiduMap mBaiduMap;

    // UI相关
    RadioGroup.OnCheckedChangeListener radioButtonListener;
    Button findFriends;
    Button findEnemies;
    Button refresh;
    Button show_friends;
    Button show_enemies;
    Button locate_myself;
    ImageView scanline;
    boolean isFirstLoc = true; // 是否首次定位
    ArrayList<Item> myFriends;
    ArrayList<Item> myEnemies;
    static double myLatitude;
    static double myLongitude;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_main);

        // 地图初始化
        mMapView = (MapView) findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();

        mCurrentMode = MyLocationConfiguration.LocationMode.NORMAL;
        mCurrentMarker = null;
        mBaiduMap
                .setMyLocationConfigeration(new MyLocationConfiguration(
                        mCurrentMode, true, null));

        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        // 定位初始化
        mLocClient = new LocationClient(this);
        mLocClient.registerLocationListener(myListener);
        final LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(1000);
        mLocClient.setLocOption(option);
        mLocClient.start();

        locate_myself = (Button) findViewById(R.id.locate_myself);
        locate_myself.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isFirstLoc = true;
                mLocClient.registerLocationListener(myListener);
                final LocationClientOption option = new LocationClientOption();
                option.setOpenGps(true); // 打开gps
                option.setCoorType("bd09ll"); // 设置坐标类型
                option.setScanSpan(1000);
                mLocClient.setLocOption(option);
                mLocClient.start();
            }
        });


        findFriends = (Button) findViewById(R.id.friends);
        findFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, FriendsList.class);
                startActivity(intent);
            }
        });

        findEnemies = (Button) findViewById(R.id.enemies_list);
        findEnemies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, EnemiesList.class);
                startActivity(intent);
            }
        });


        //点击刷新按钮发送短信
        refresh = (Button) this.findViewById(R.id.refresh);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanline = (ImageView) findViewById(R.id.scanline);
                final Animation anim = AnimationUtils.loadAnimation(MainActivity.this,R.anim.rotateanim);
                anim.setFillAfter(true);
                scanline.startAnimation(anim);
                try {
                    myFriends = (ArrayList<Item>) getObject("friendsList.dat");
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
                try {
                    myEnemies = (ArrayList<Item>) getObject("enemiesList.dat");
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }

                SmsManager manager = SmsManager.getDefault();
                if(null != myFriends) {
                    for (int i = 0; i < myFriends.size(); i++) {
                        ArrayList<String> list = manager.divideMessage("where are you");
                        for (String text : list)
                            manager.sendTextMessage(myFriends.get(i).getNumber(), null, text, null, null);
                    }
                }
                if(null != myEnemies) {
                    for (int i = 0; i < myEnemies.size(); i++) {
                        ArrayList<String> list = manager.divideMessage("where are you");
                        for (String text : list)
                            manager.sendTextMessage(myEnemies.get(i).getNumber(), null, text, null, null);
                    }
                }
                if (myFriends == null && myEnemies == null)
                    Toast.makeText(getApplicationContext(), "发送失败", Toast.LENGTH_SHORT).show();

                else
                    Toast.makeText(getApplicationContext(), "发送完毕", Toast.LENGTH_SHORT).show();
                mBaiduMap.clear();
            }
        });

        show_friends = (Button) findViewById(R.id.show_friends);
        show_friends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    myFriends = (ArrayList<Item>) getObject("friendsList.dat");
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
                if (null != myFriends) {
                    for (int i = 0; i < myFriends.size(); i++) {
                        if (myFriends.get(i).getLocation() != null && (myFriends.get(i).getLocation()).matches("\\d+[.]\\d+/\\d+[.]\\d+")) {
                            String[] ll = myFriends.get(i).getLocation().split("/");
                            LatLng point = new LatLng(Double.parseDouble(ll[0]), Double.parseDouble(ll[1]));
                            BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.drawable.friend_icon);
                            OverlayOptions option = new MarkerOptions()
                                    .position(point)
                                    .icon(bitmap)
                                    .title(myFriends.get(i).getName());
                            //构建文字Option对象，用于在地图上添加文字
                            OverlayOptions textOption = new TextOptions()
                                    .bgColor(0xff00ff00)
                                    .fontSize(30)
                                    .fontColor(0xffff0000)
                                    .text(myFriends.get(i).getName() + "\n" + myFriends.get(i).getNumber())
                                    .rotate(0)
                                    .position(point);

                            double distance = DistanceOfTwoPoints(myLatitude,myLongitude,Double.parseDouble(ll[0]),
                                    Double.parseDouble(ll[1]));
                            myFriends.get(i).setDistance(distance);

                            LatLng p1 = new LatLng(myLatitude,myLongitude);
                            LatLng p2 = new LatLng(Double.parseDouble(ll[0]), Double.parseDouble(ll[1]));
                            LatLng p3 = new LatLng((myLatitude + Double.parseDouble(ll[0]))/2,
                                    (myLongitude+Double.parseDouble(ll[1]))/2);
                            List<LatLng> points = new ArrayList<LatLng>();
                            points.add(p1);
                            points.add(p2);
                            OverlayOptions ooPolyline = new PolylineOptions().width(10).color(0xff00ff00).points(points);

                            OverlayOptions textOption2 = new TextOptions()
                                    .bgColor(0xff00ff00)
                                    .fontSize(30)
                                    .fontColor(0xffff0000)
                                    .text(Double.toString(myFriends.get(i).getDistance()) + "m")
                                    .rotate(0)
                                    .position(p3);
                            //在地图上添加该文字对象并显示
                            mBaiduMap.addOverlay(textOption);
                            mBaiduMap.addOverlay(option);
                            mBaiduMap.addOverlay(ooPolyline);
                            mBaiduMap.addOverlay(textOption2);
                        }
                    }
                }
            }
        });

        show_enemies = (Button) this.findViewById(R.id.show_enemies);
        show_enemies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    myEnemies = (ArrayList<Item>) getObject("enemiesList.dat");
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
                if (null != myEnemies) {
                    for (int i = 0; i < myEnemies.size(); i++) {
                        if (myEnemies.get(i).getLocation() != null && (myEnemies.get(i).getLocation()).matches("\\d+[.]\\d+/\\d+[.]\\d+")) {
                            String[] ll = myEnemies.get(i).getLocation().split("/");
                            LatLng point = new LatLng(Double.parseDouble(ll[0]), Double.parseDouble(ll[1]));
                            BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.drawable.enemy_icon);
                            OverlayOptions option = new MarkerOptions()
                                    .position(point)
                                    .icon(bitmap)
                                    .title(myEnemies.get(i).getName());
                            //构建文字Option对象，用于在地图上添加文字
                            OverlayOptions textOption = new TextOptions()
                                    .bgColor(0xffff0000)
                                    .fontSize(30)
                                    .fontColor(0xff00ff00)
                                    .text(myEnemies.get(i).getName() + "\n" + myEnemies.get(i).getNumber())
                                    .rotate(0)
                                    .position(point);


                            double distance = DistanceOfTwoPoints(myLatitude,myLongitude,Double.parseDouble(ll[0]),
                                    Double.parseDouble(ll[1]));
                            myEnemies.get(i).setDistance(distance);
                            LatLng p1 = new LatLng(myLatitude,myLongitude);
                            LatLng p2 = new LatLng(Double.parseDouble(ll[0]), Double.parseDouble(ll[1]));
                            LatLng p3 = new LatLng((myLatitude + Double.parseDouble(ll[0]))/2,
                                    (myLongitude+Double.parseDouble(ll[1]))/2);
                            List<LatLng> points = new ArrayList<LatLng>();
                            points.add(p1);
                            points.add(p2);
                            OverlayOptions ooPolyline = new PolylineOptions().width(10).color(0xffff0000).points(points);

                            OverlayOptions textOption2 = new TextOptions()
                                    .bgColor(0xffff0000)
                                    .fontSize(30)
                                    .fontColor(0xff00ff00)
                                    .text(Double.toString(myEnemies.get(i).getDistance()) + "m")
                                    .rotate(0)
                                    .position(p3);
                            //在地图上添加该文字对象并显示
                            mBaiduMap.addOverlay(textOption);
                            mBaiduMap.addOverlay(option);
                            mBaiduMap.addOverlay(ooPolyline);
                            mBaiduMap.addOverlay(textOption2);
                        }
                    }
                }
            }
        });
    }


    /**
     * 定位SDK监听函数
     */
    public class MyLocationListenner implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            // map view 销毁后不在处理新接收的位置
            if (location == null || mMapView == null) {
                return;
            }
            myLatitude = location.getLatitude();
            myLongitude = location.getLongitude();
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                            // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(0).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            mBaiduMap.setMyLocationData(locData);
            if (isFirstLoc) {
                isFirstLoc = false;
                LatLng ll = new LatLng(location.getLatitude(),
                        location.getLongitude());
                MapStatus.Builder builder = new MapStatus.Builder();
                builder.target(ll).zoom(18.0f);
                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
            }
        }

        public void onReceivePoi(BDLocation poiLocation) {
        }
    }

    @Override
    protected void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        mMapView.onResume();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        // 退出时销毁定位
        mLocClient.stop();
        // 关闭定位图层
        mBaiduMap.setMyLocationEnabled(false);
        mMapView.onDestroy();
        mMapView = null;
        super.onDestroy();
    }

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

    private static final double EARTH_RADIUS = 6378137;

    private static double rad(double d) {
        return d * Math.PI / 180.0;
    }

    /**
     * 根据两点间经纬度坐标（double值），计算两点间距离，
     *
     * @param lat1
     * @param lng1
     * @param lat2
     * @param lng2
     * @return 距离：单位为米
     */
    public static double DistanceOfTwoPoints(double lat1,double lng1,
                                             double lat2,double lng2) {
        double radLat1 = rad(lat1);
        double radLat2 = rad(lat2);
        double a = radLat1 - radLat2;
        double b = rad(lng1) - rad(lng2);
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
                + Math.cos(radLat1) * Math.cos(radLat2)
                * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        s = Math.round(s * 10000) / 10000;
        return s;
    }
}
