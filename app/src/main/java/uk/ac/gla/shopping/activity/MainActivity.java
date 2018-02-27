package uk.ac.gla.shopping.activity;

import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import uk.ac.gla.shopping.R;
import uk.ac.gla.shopping.database.ShoppingListItemDatabase;
import uk.ac.gla.shopping.database.util.DatabaseInitializer;

public class MainActivity extends AppCompatActivity {

    public static final String API_KEY = "AIzaSyAE7UkFI4mjE98X-e97GtucHhNf_j5Ssu0";

    private final BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = item -> {
        Fragment selectedFragment = null;
        switch (item.getItemId()) {
            case R.id.navigation_shopping_list:
                setTitle(R.string.title_shopping_list);
                selectedFragment = ShoppingListFragment.newInstance();
                break;
            case R.id.navigation_scanner:
                setTitle(R.string.title_scanner);
                selectedFragment = ScannerFragment.newInstance();
                break;
            case R.id.navigation_translate:
                setTitle(R.string.title_translate);
                selectedFragment = TranslateFragment.newInstance();
                break;
            case R.id.navigation_phrasebook:
                setTitle(R.string.title_phrasebook);
                selectedFragment = PhrasebookFragment.newInstance();
                break;
        }
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, selectedFragment);
        transaction.commit();
        return true;
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DatabaseInitializer.populateAsync(ShoppingListItemDatabase.getInstance(getApplicationContext()));

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, ShoppingListFragment.newInstance());
        transaction.commit();
    }

}
