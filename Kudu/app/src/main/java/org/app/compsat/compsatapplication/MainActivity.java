package org.app.compsat.compsatapplication;

import android.app.TabActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends TabActivity {

    private final String PROJECT_NUMBER = "1058631919049";
    private SharedPreferences prefs;
    private Typeface tf_opensans_regular, tf_opensans_bold;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tabhost);

        Resources resources = getResources();
        TabHost tabHost = getTabHost();

        ImageView menu = (ImageView)findViewById(R.id.menu_button);
        menu.setImageDrawable(resources.getDrawable(R.drawable.ic_menu));



        // Android tab
        Intent intentCalendar = new Intent().setClass(this, CalendarActivity.class);
        TabSpec tabSpecCalendar = tabHost
                .newTabSpec("Calendar")
                .setIndicator("", resources.getDrawable(R.drawable.ic_event))
                .setContent(intentCalendar);


        Intent intentNews = new Intent().setClass(this, NewsfeedActivity.class);
        TabSpec tabSpecNews = tabHost
                .newTabSpec("Discover")
                .setIndicator("", resources.getDrawable(R.drawable.search))
                .setContent(intentNews);

        Intent intentSetting = new Intent().setClass(this, SettingsActivity.class);
        TabSpec tabSpecSettings = tabHost
                .newTabSpec("Settings")
                .setIndicator("", resources.getDrawable(R.drawable.settings))
                .setContent(intentSetting);



        tabHost.addTab(tabSpecCalendar);
        tabHost.addTab(tabSpecNews);
        tabHost.addTab(tabSpecSettings);



        final TextView toolbar;
        toolbar = (TextView) findViewById(R.id.toolbar_title);
        tf_opensans_regular = Typeface.createFromAsset(getAssets(), "fonts/OpenSans-Regular.ttf");
        tf_opensans_bold = Typeface.createFromAsset(getAssets(), "fonts/OpenSans-Bold.ttf");
        toolbar.setTypeface(tf_opensans_bold);


        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
            if ("Discover".equals(tabId)) {
                toolbar.setText("Discover");
            }
            else if ("Calendar".equals(tabId)) {
                toolbar.setText("Calendar");
            }
            else if ("Settings".equals(tabId)) {
                toolbar.setText("Settings");
            }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        Toast.makeText(this, "Test", Toast.LENGTH_LONG).show();
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo contextMenuInfo = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        switch(item.getItemId()) {
            case R.id.logout:
                logout();
                break;
            case R.id.about:
                break;
        }
        return super.onContextItemSelected(item);
    }

    public void logout(){
        Intent loginScreen=new Intent(this, LoginActivity.class);
        loginScreen.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        prefs = this.getSharedPreferences("org.app.compsat.compsatapplication", MODE_PRIVATE);
        prefs.edit().clear().apply();
        startActivity(loginScreen);
        this.finish();
    }

    public void inflateMenu(View view){
        openOptionsMenu();
    }

}
