package algonquin.cst2335.XiangYing;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView topView= findViewById(R.id.bottom);
        String oldText = topView.getText().toString();
        topView.setText("Java put this here");

        EditText bottom = findViewById(R.id.bottomtext);

        Button btn = findViewById(R.id.button);
        btn.setText("The view was previously "+ oldText);
        btn.setOnClickListener( ( v ) -> {topView.setText("Edit text has " +  bottom.getText());} );
        CheckBox cb = findViewById(R.id.checkbox);
        RadioButton radio = findViewById(R.id.radio);
        Switch sw = findViewById(R.id.sw);

        sw.setOnCheckedChangeListener((button, onOrOff) -> {
            radio.setChecked(onOrOff);
            Toast.makeText(MainActivity.this, "You clicked on switch", Toast.LENGTH_LONG).show();

        });
        cb.setOnCheckedChangeListener(( b, c) -> {
            Toast.makeText(MainActivity.this, "You clicked on checkbox", Toast.LENGTH_SHORT).show();
            if(c)
                radio.setChecked(true);
            else
                radio.setChecked(false);
        });
        ImageButton ib = findViewById(R.id.button4);
        ib.setOnClickListener((vw) ->{
            int width = ib.getWidth();
            int height = vw.getHeight();
        });
    }


}

