package com.nicomot.re_food.activity;

import static android.service.notification.NotificationListenerService.requestRebind;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.nicomot.re_food.R;
import com.nicomot.re_food.fragment.AccountFragment;
import com.nicomot.re_food.fragment.FragmentDapur;
import com.nicomot.re_food.fragment.HomeFragment;
import com.nicomot.re_food.fragment.OrderListFragment;
import com.nicomot.re_food.service.ServiceNotificationListener;
import com.nicomot.re_food.util.ShowMessage;

public class BaseActivity extends AppCompatActivity {


    private BottomNavigationView bottomNavigationView;
    private FrameLayout frameLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        hiddenActionBar();
        initializeID();
        navBarItemListener(bottomNavigationView);
        String[] PERMISION = new String[]{
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.MANAGE_EXTERNAL_STORAGE,
                Manifest.permission.BIND_NOTIFICATION_LISTENER_SERVICE,
                Manifest.permission.ACCESS_NOTIFICATION_POLICY,
                Manifest.permission.READ_SMS,
                Manifest.permission.RECEIVE_SMS
        };

        if(!grantPermision(BaseActivity.this,PERMISION)){
            ActivityCompat.requestPermissions(BaseActivity.this,PERMISION,1);
        }
        if(!VerifyNotificationPermission()){
            startActivity(new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"));
        } else{
        }
    }

    public void tryReconnectService() {
        toggleNotificationListenerService();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            ComponentName componentName =
                    new ComponentName(getApplicationContext(), ServiceNotificationListener.class);

            //It say to Notification Manager RE-BIND your service to listen notifications again inmediatelly!
            requestRebind(componentName);
        }
    }


    private void toggleNotificationListenerService() {
        PackageManager pm = getPackageManager();
        pm.setComponentEnabledSetting(new ComponentName(this, ServiceNotificationListener.class),
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
        pm.setComponentEnabledSetting(new ComponentName(this, ServiceNotificationListener.class),
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
    }
    SharedPreferences sharedPreferences;
    public Boolean VerifyNotificationPermission() {
        String theList = android.provider.Settings.Secure.getString(getContentResolver(), "enabled_notification_listeners");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            Intent intent=new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
            startActivity(intent);
        } else if(Build.VERSION.SDK_INT < Build.VERSION_CODES.R){
            System.out.println(theList);
            String[] theListList = theList.split(":");
            String me = (new ComponentName(this, ServiceNotificationListener.class)).flattenToString();
            for ( String next : theListList ) {
                if ( me.equals(next) ) return true;
            }
        }

        return false;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 1){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
            } else {
            }
            if(grantResults[1] == PackageManager.PERMISSION_GRANTED){

            } else{
            }
            if(grantResults[2] == PackageManager.PERMISSION_GRANTED){
            }

        }

    }

    private boolean grantPermision(Context ctx, String... permision){
        if(ctx != null && permision != null){
            for(String Permision : permision){
                if(ActivityCompat.checkSelfPermission(ctx,Permision) != PackageManager.PERMISSION_GRANTED){
                    return  false;
                }
            }
        }
        return true;
    }

    void hiddenActionBar(){
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if(getSupportActionBar() != null){
            getSupportActionBar().hide();
        }
    }
    void initializeID(){
        bottomNavigationView = this.findViewById(R.id.id_navigation_view);
        frameLayout = this.findViewById(R.id.id_base_frame);
        Intent intent = getIntent();
      /*
        String kerja = intent.getStringExtra("KERJA");

        if(kerja.equals("DAPUR")){
            FragmentDapur fragmentDapur = new FragmentDapur();
            getSupportFragmentManager().beginTransaction().replace(frameLayout.getId(),fragmentDapur).commit();
        }
      */

    }
    void navBarItemListener(BottomNavigationView bottomNavigationView){
        switchFragment(getSupportFragmentManager(),new HomeFragment());
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.id_nav_home:
                        tryReconnectService();
                        switchFragment(getSupportFragmentManager(),new HomeFragment());
                        break;
                    case R.id.id_nav_pesanan:
                        switchFragment(getSupportFragmentManager(),new OrderListFragment());
                        break;
                    case R.id.id_nav_account:
                        switchFragment(getSupportFragmentManager(),new AccountFragment());
                        break;
                }
                return true;
            }
        });
    }
    void switchFragment(FragmentManager fragmentManager, Fragment fragment){
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(frameLayout.getId(),fragment).commit();
    }
}