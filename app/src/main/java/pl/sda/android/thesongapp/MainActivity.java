package pl.sda.android.thesongapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;

/**************
Hi there!
Please, PLEASE let me pass this test, join Tooploox and learn more! :)
As you can see I managed to do every assigned task AND one extra sorting!
I'd love to work with you guys!
Sincerelly, yours
A.B.
 *************/

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.research) EditText research;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);
        floatButtonFunctionCombo();
        floatButtonFunctionOffline();
        floatButtonFunctionOnline();
}
    private void floatButtonFunctionOnline() {
        FloatingActionButton functionOnline = (FloatingActionButton) findViewById(R.id.fab1);
        functionOnline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(research.getText().toString().equals(null)||research.getText().toString().equals("")){
                    Snackbar.make(view, "Empty search!", Snackbar.LENGTH_LONG).show();
                }else {
                    Intent intent = new Intent(MainActivity.this, MyListViewing.class);
                    intent.putExtra("search", research.getText().toString());
                    intent.putExtra("engine", 1);
                    startActivity(intent);
                }
            }
        });
    }

    private void floatButtonFunctionOffline() {
        FloatingActionButton functionOffline = (FloatingActionButton) findViewById(R.id.fab2);
        functionOffline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(research.getText().toString().equals(null)||research.getText().toString().equals("")){
                    Snackbar.make(view, "Empty search!", Snackbar.LENGTH_LONG).show();
                }else {
                    Intent intent = new Intent(MainActivity.this, MyListViewing.class);
                    intent.putExtra("search", research.getText().toString());
                    intent.putExtra("engine", 2);
                    startActivity(intent);
                }
            }
        });
    }

    private void floatButtonFunctionCombo() {
        FloatingActionButton functionCombo = (FloatingActionButton) findViewById(R.id.fab3);
        functionCombo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(research.getText().toString().equals(null)||research.getText().toString().equals("")){
                    Snackbar.make(view, "Empty search!", Snackbar.LENGTH_LONG).show();
                }else {
                    Intent intent = new Intent(MainActivity.this, MyListViewing.class);
                    intent.putExtra("search", research.getText().toString());
                    intent.putExtra("engine", 3);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_task) {
            startActivity(new Intent(this, Instruction.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
