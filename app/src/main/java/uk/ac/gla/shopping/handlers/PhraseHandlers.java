package uk.ac.gla.shopping.handlers;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import uk.ac.gla.shopping.R;
import uk.ac.gla.shopping.activity.MainActivity;

public class PhraseHandlers {
    private Context context;

    public PhraseHandlers(Context context) {
        this.context = context;
    }

    public void onClickPhrase(View view) {
        // Get phrase from phrasebook recycler item.
        ViewGroup childViewGroup = (ViewGroup) ((ViewGroup) view).getChildAt(0);
        TextView textView = (TextView) childViewGroup.getChildAt(0);
        String phrase = textView.getText().toString();

        // Select phrasebook fragment.
        MainActivity mainActivity = (MainActivity) context;
        mainActivity.selectNavigationItem(R.id.navigation_translate, phrase);
    }
}
