package nl.frankkie.ventilatorapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;

public class MainActivity extends AppCompatActivity {

    FanView fanView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fanView = (FanView) findViewById(R.id.fanview);

        ((RadioButton)findViewById(R.id.radio0)).setChecked(true);
    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radio0:
                if (checked)
                    fanView.rotateSpeed = 0;
                    break;
            case R.id.radio1:
                if (checked)
                    fanView.rotateSpeed = 10;
                    break;
            case R.id.radio2:
                if (checked)
                    fanView.rotateSpeed = 20;
                break;
            case R.id.radio3:
                if (checked)
                    fanView.rotateSpeed = 30;
                break;
        }
    }
}
