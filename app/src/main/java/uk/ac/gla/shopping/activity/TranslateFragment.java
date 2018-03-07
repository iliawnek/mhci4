package uk.ac.gla.shopping.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.Layout;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;

import uk.ac.gla.shopping.R;

public class TranslateFragment extends Fragment {
    public static TranslateFragment newInstance() {
        TranslateFragment fragment = new TranslateFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_translate, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        View toSection = getView().findViewById(R.id.section_to_language);
        TextView translateFromText = getView().findViewById(R.id.translateFromText);
        TextView translateToText = getView().findViewById(R.id.translateToText);
        FloatingActionButton fab = getView().findViewById(R.id.fab_translate);

        class TranslationTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {
                translateToText.setText("");
                translateToText.setHint("翻译...");
                Translate translate = TranslateOptions.newBuilder().setApiKey(MainActivity.API_KEY).build().getService();
                Translation translation = translate.translate(
                        params[0],
                        Translate.TranslateOption.sourceLanguage(params[1]),
                        Translate.TranslateOption.targetLanguage(params[2])
                );
                return translation.getTranslatedText();
            }

            @Override
            protected void onPostExecute(String s) {
                translateToText.setText(s);
                fab.setVisibility(View.VISIBLE);
            }
        }

        translateFromText.setSingleLine(true);
        translateFromText.setMaxLines(4);
        translateFromText.setHorizontallyScrolling(false);
        translateFromText.setImeOptions(EditorInfo.IME_ACTION_DONE);

        translateFromText.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                toSection.setVisibility(View.GONE);
            } else {
                toSection.setVisibility(View.VISIBLE);
            }
        });

        fab.setOnClickListener(v -> {
            String text = translateFromText.getText().toString();
            new TranslationTask().execute(text, "en", "zh");
            fab.setVisibility(View.GONE);
            translateFromText.clearFocus();
        });

        // Insert phrase if triggered from phrasebook.
        Bundle arguments = getArguments();
        if (arguments != null) {
            String selectedPhrase = getArguments().getString("selectedPhrase");
            translateFromText.setText(selectedPhrase);
            new TranslationTask().execute(selectedPhrase, "en", "zh");
        }
        super.onViewCreated(view, savedInstanceState);
    }
}
