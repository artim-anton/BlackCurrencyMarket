package com.artimanton.blackcurrencymarket.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.TabActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.preference.PreferenceManager;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.TransactionDetails;
import com.artimanton.blackcurrencymarket.R;
import com.artimanton.blackcurrencymarket.model.RecordModel;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class NavigationActivity extends  TabActivity implements BillingProcessor.IBillingHandler, NavigationView.OnNavigationItemSelectedListener {
    public Spinner spinner_city,spinner_country;
    // это будет именем файла настроек
    public static final String APP_PREFERENCES = "mysettings";
    public static final String APP_PREFERENCES_COUNTER = "counter";
    public static final String APP_PREFERENCES_COUNTER_COUNTRY = "counter_country";
    public static final String APP_PREFERENCES_CITY = "city";
    public static final String APP_PREFERENCES_COUNTRY = "country";
    public static final String APP_PREFERENCES_CURRENCY = "currency";
    public static final String APP_PREFERENCES_PATH = "path";
    private SharedPreferences mSettings;
    TabHost tabHost;
    TabHost.TabSpec tabSpec;
    DollarActivity dollarActivity;

    private BillingProcessor bp;
    private static final String LICENSE_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAjwSzeEgAXyQBBKCp724PVqTDbIzY9CGhkTdXFrPNmxpwmLjUyHB9oR3rlkOQIbCPRgU1nWaWqD27qhijbnx1dm3sNZ136EnB2tzbMZjTU88p9Meizz8YfkAhD0csKiEzYk7tzbhO9EWfIprDylbD6UpJsa8OJS//8xQHykcgt2r/DKICxoRoR3hxNczgQY9dtJOPcdrMwKKB402lkqqdEAEyN1t5E0vxBQpqU6Ouufjx3aUrI12GovTnn1kOyT4UUYt20UQz9E9M9GRaBoHgxPstZB8rY6ffkkqaKmmmqjFM5g8hY9OxNF8jkApcjgAtvq03t4j6YOiFUetI+4yc5wIDAQAB"; // PUT YOUR MERCHANT KEY HERE;
    private static final String PRODUCT_ID = "com.artimanton.blackcurrencymarket";
    private static final String SUBSCRIPTION_ID = "com.artimanton.blackcurrencymarket.subs1";
    //авторизацыя
    private static final int MY_REQUEST_CODE = 7117;
    List<AuthUI.IdpConfig> providers;
    Button btn_sign_out;
    Button btn_sign_out_drawer;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        Toolbar toolbar = findViewById(R.id.toolbar);
        //setActionBar(toolbar);
        bp = new BillingProcessor(this, LICENSE_KEY, this);
        //loadLocate();
        initializationDrawer();
        initializationTabHost();
        floatingActionButton();
        ///АВТОРИЗАЦИЯ
        authorization();


        /*NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerview = navigationView.getHeaderView(0);
        LinearLayout header = (LinearLayout) headerview.findViewById(R.id.header);
        header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(NavigationActivity.this, "clicked", Toast.LENGTH_SHORT).show();
               // drawer.closeDrawer(GravityCompat.START);
            }
        });*/


    }


    private void floatingActionButton() {
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bp.isSubscribed(SUBSCRIPTION_ID)) {
                    Intent intent = new Intent(NavigationActivity.this, AddActivity.class);
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(NavigationActivity.this, AddActivity.class);
                    startActivity(intent);
                }


                //Snackbar.make(view, "Here's a Snackbar", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });
    }

    private void initializationTabHost() {
        // получаем TabHost
        tabHost = getTabHost();

        // инициализация была выполнена в getTabHost
        // метод setup вызывать не нужно



        tabSpec = tabHost.newTabSpec("tag1");
        tabSpec.setIndicator(getString(R.string.dollar));
        tabSpec.setContent(new Intent(this, DollarActivity.class));
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("tag2");
        tabSpec.setIndicator(getString(R.string.euro));
        tabSpec.setContent(new Intent(this, EuroActivity.class));
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("tag3");
        tabSpec.setIndicator(getString(R.string.ruble));
        tabSpec.setContent(new Intent(this, RubleActivity.class));
        tabHost.addTab(tabSpec);

        mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = mSettings.edit();
        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                switch (tabId){
                    case "tag1":
                        //Toast.makeText(getBaseContext(), "tag1", Toast.LENGTH_SHORT).show();
                        editor.putInt(APP_PREFERENCES_CURRENCY, 0);
                        editor.apply();
                    break;
                    case "tag2":
                        //Toast.makeText(getBaseContext(), "tag2", Toast.LENGTH_SHORT).show();
                        editor.putInt(APP_PREFERENCES_CURRENCY, 1);
                        editor.apply();
                    break;
                    case "tag3":
                        //Toast.makeText(getBaseContext(), "tag2", Toast.LENGTH_SHORT).show();
                        editor.putInt(APP_PREFERENCES_CURRENCY, 2);
                        editor.apply();
                        break;
                }
            }
        });

    }

    private void authorization() {
        btn_sign_out =(Button) findViewById(R.id.btn_sign_out);
        btn_sign_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Logout
                AuthUI.getInstance()
                        .signOut(NavigationActivity.this)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                btn_sign_out.setEnabled(false);
                                //Init providers
                                providers = Arrays.asList(
                                        new AuthUI.IdpConfig.EmailBuilder().build(),
                                        new AuthUI.IdpConfig.PhoneBuilder().build(),
                                        //new AuthUI.IdpConfig.FacebookBuilder().build(),
                                        new AuthUI.IdpConfig.GoogleBuilder().build()
                                );
                                showSingInOptions();
                            }
                        }) .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(NavigationActivity.this, ""+e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // User is signed in
            //Toast.makeText(NavigationActivity.this, "onAuthStateChanged:signed_in:" + user,Toast.LENGTH_SHORT).show();
            btn_sign_out.setEnabled(true);
        } else {
            //Init providers
            providers = Arrays.asList(
                    new AuthUI.IdpConfig.EmailBuilder().build(),
                    new AuthUI.IdpConfig.PhoneBuilder().build(),
                    //new AuthUI.IdpConfig.FacebookBuilder().build(),
                    new AuthUI.IdpConfig.GoogleBuilder().build()
            );
            showSingInOptions();
            // User is signed out
            //Toast.makeText(NavigationActivity.this, "onAuthStateChanged:signed_out",Toast.LENGTH_SHORT).show();
        }
    }

    private void initializationDrawer() {
        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawerLayout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, R.string.nav_setting, R.string.nav_logout);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }

    @Override
    public void onProductPurchased(String productId, TransactionDetails details) {

    }

    @Override
    public void onPurchaseHistoryRestored() {
        bp.loadOwnedPurchasesFromGoogle();
    }

    @Override
    public void onBillingError(int errorCode, Throwable error) {

    }

    @Override
    public void onBillingInitialized() {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    private void showSingInOptions() {
        startActivityForResult(
                AuthUI.getInstance().createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .setTheme(R.style.MyTheme)
                        .build(), MY_REQUEST_CODE
        );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == MY_REQUEST_CODE){
            IdpResponse response = IdpResponse.fromResultIntent(data);
            if(resultCode == RESULT_OK){
                //Get User
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                //Show email in Toast
                Toast.makeText(this, ""+user.getEmail(), Toast.LENGTH_LONG).show();
                //Set button sign_out
                btn_sign_out.setEnabled(true);
            }
            else{
                Toast.makeText(this, ""+response.getError().getMessage(), Toast.LENGTH_LONG).show();

            }
        }
    }

    @Override
    public void recreate() {
        super.recreate();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        /*if (id == R.id.nav_setting) {showChangeLanguageDialog();

        } else*/ if (id == R.id.new_record){

            if (bp.isSubscribed(SUBSCRIPTION_ID)) {
                Intent intent = new Intent(NavigationActivity.this, AddActivity.class);
                startActivity(intent);
            }else{
                Intent intent = new Intent(NavigationActivity.this, BillingActivity.class);
                startActivity(intent);
            }
        }
        else if (id == R.id.delete_record){
            showDeleteCurrencyDialog();

            Toast.makeText(this, ""+mSettings.getString(APP_PREFERENCES_PATH,"DEFAULT"), Toast.LENGTH_LONG).show();
        }

        else if (id == R.id.nav_logout){

            this.finish();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawerLayout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void showDeleteCurrencyDialog() {
        final String[] listItem = {"доллары", "евро", "рубли"};
        AlertDialog.Builder mBilder = new AlertDialog.Builder(NavigationActivity.this);
        mBilder.setTitle(getString(R.string.choose_currency));
        mBilder.setSingleChoiceItems(listItem, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                if (i == 0){
                    mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
                    FirebaseDatabase database = FirebaseDatabase.getInstance();;
                    DatabaseReference reference;
                    String s = mSettings.getString(APP_PREFERENCES_PATH,"DEFAULT") + "dollar";
                    reference = database.getReference(s);
                    String mUserId = FirebaseAuth.getInstance().getUid();
                    reference.child(mUserId+"buy").removeValue();
                    reference.child(mUserId+"sell").removeValue();
                    Toast.makeText(NavigationActivity.this, s, Toast.LENGTH_LONG).show();
                }
                else if (i == 1){
                    mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
                    FirebaseDatabase database = FirebaseDatabase.getInstance();;
                    DatabaseReference reference;
                    String s = mSettings.getString(APP_PREFERENCES_PATH,"DEFAULT") + "evro";
                    reference = database.getReference(s);
                    String mUserId = FirebaseAuth.getInstance().getUid();
                    reference.child(mUserId+"buy").removeValue();
                    reference.child(mUserId+"sell").removeValue();
                    Toast.makeText(NavigationActivity.this, s, Toast.LENGTH_LONG).show();
                }
                else if (i == 2){
                    mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
                    FirebaseDatabase database = FirebaseDatabase.getInstance();;
                    DatabaseReference reference;
                    String s = mSettings.getString(APP_PREFERENCES_PATH,"DEFAULT") + "ruble";
                    reference = database.getReference(s);
                    String mUserId = FirebaseAuth.getInstance().getUid();
                    reference.child(mUserId+"buy").removeValue();
                    reference.child(mUserId+"sell").removeValue();
                    Toast.makeText(NavigationActivity.this, s, Toast.LENGTH_LONG).show();
                }
                dialog.dismiss();
            }
        });
        AlertDialog mDialog = mBilder.create();
        mDialog.show();

    }

    /*private void showChangeLanguageDialog() {
        final String[] listItem = {"Русский", "Українська", "Polski", "English"};
        AlertDialog.Builder mBilder = new AlertDialog.Builder(NavigationActivity.this);
        mBilder.setTitle(getString(R.string.choose_language));
        mBilder.setSingleChoiceItems(listItem, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                if (i == 0){
                    setLocate("ru");
                    recreate();
                }
                else if (i == 1){
                    setLocate("uk");
                    recreate();
                }
                else if (i == 2){
                    setLocate("pl");
                    recreate();
                }
                else if (i == 3){
                    setLocate("en");
                    recreate();
                }
                dialog.dismiss();
            }
        });
        AlertDialog mDialog = mBilder.create();
        mDialog.show();
    }

    private void setLocate(String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());

        SharedPreferences.Editor editor = getSharedPreferences("blackCurrencyMarketSetting", MODE_PRIVATE).edit();
        editor.putString("My_lang", lang);
        editor.apply();
    }
    public void loadLocate(){
        SharedPreferences prefs = getSharedPreferences("blackCurrencyMarketSetting", Activity.MODE_PRIVATE);
        String language = prefs.getString("My_lang", "");
        setLocate(language);
    }*/

    public void drawer_menu(View view) {
        final Animation animAlpha = AnimationUtils.loadAnimation(this, R.anim.alpha);
        view.startAnimation(animAlpha);
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawerLayout);
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {
                drawer.openDrawer(GravityCompat.START);
            }
    }

    public void bntSignOut(View view) {
        AuthUI.getInstance()
                .signOut(NavigationActivity.this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        //btn_sign_out.setEnabled(false);
                        //Init providers
                        providers = Arrays.asList(
                                new AuthUI.IdpConfig.EmailBuilder().build(),
                                new AuthUI.IdpConfig.PhoneBuilder().build(),
                                //new AuthUI.IdpConfig.FacebookBuilder().build(),
                                new AuthUI.IdpConfig.GoogleBuilder().build()
                        );
                        showSingInOptions();
                    }
                }) .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(NavigationActivity.this, ""+e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawerLayout);
        drawer.closeDrawer(GravityCompat.START);

    }

    public void setting_menu(View view) {
        final Animation animAlpha = AnimationUtils.loadAnimation(this, R.anim.alpha);
        view.startAnimation(animAlpha);
        Intent intent = new Intent(NavigationActivity.this, SettingActivity.class);
        startActivity(intent);
    }

    public void btnReload(View view) {
        Intent i = new Intent( this , this.getClass() );
        finish();
        this.startActivity(i);
    }
}
