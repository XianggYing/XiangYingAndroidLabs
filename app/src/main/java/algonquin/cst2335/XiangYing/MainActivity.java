package algonquin.cst2335.XiangYing;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView topView= findViewById(R.id.textview);
        String oldText = topView.getText().toString();
        topView.setText("Java put this here");

        EditText bottom = findViewById(R.id.bottomtext);

        Button btn = findViewById(R.id.button);
        btn.setText("The view was previously "+ oldText);
        btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                topView.setText("Edit text has " + bottom.getText())
            }

                               }

        )
                }
        );
       ;
    }
}