package include;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.joel.proyectogrado.R;

public class MyToolbar {

    public static void show(AppCompatActivity activity, String titulo, boolean upButton){
       Toolbar mToolbar=activity.findViewById(R.id.toolbar);
        activity.setSupportActionBar(mToolbar);
        activity.getSupportActionBar().setTitle(titulo);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(upButton);
    }
}
