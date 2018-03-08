package uk.ac.gla.shopping.activity;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;

import uk.ac.gla.shopping.R;

public class TranslateFragment extends Fragment {
    final String TAG = "TranslateFragment";
    final int RESULT_SPEECH_TARGET = 1;
    final int RESULT_SPEECH_SOURCE = 2;

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

    class TranslationTask extends AsyncTask<String, Void, String> {
        String sourceLanguage;
        String targetLanguage;
        TextView textView;
        FloatingActionButton fab;

        public TranslationTask() {
            super();
            fab = getView().findViewById(R.id.fab_translate);
        }

        public TranslationTask(String sourceLanguage, String targetLanguage) {
            this();
            this.sourceLanguage = sourceLanguage;
            this.targetLanguage = targetLanguage;
            if (targetLanguage.equals("zh")) {
                textView = getView().findViewById(R.id.target_text);
            } else {
                textView = getView().findViewById(R.id.source_text);
            }
        }

        @Override
        protected String doInBackground(String... params) {
            textView.setText("");
            if (targetLanguage.equals("zh")) {
                textView.setHint("翻译...");
            } else {
                textView.setHint("Translating...");
            }
            Translate translate = TranslateOptions.newBuilder().setApiKey(MainActivity.API_KEY).build().getService();
            Translation translation = translate.translate(
                    params[0],
                    Translate.TranslateOption.sourceLanguage(sourceLanguage),
                    Translate.TranslateOption.targetLanguage(targetLanguage)
            );
            return translation.getTranslatedText();
        }

        @Override
        protected void onPostExecute(String s) {
            textView.setText(s);
            fab.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        View toSection = getView().findViewById(R.id.section_to_language);
        TextView translateFromText = getView().findViewById(R.id.source_text);
        FloatingActionButton fab = getView().findViewById(R.id.fab_translate);
        ImageButton sourceVoiceButton = getView().findViewById(R.id.source_voice_button);
        ImageButton targetVoiceButton = getView().findViewById(R.id.target_voice_button);

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
            new TranslationTask("en", "zh").execute(text);
            fab.setVisibility(View.GONE);
            translateFromText.clearFocus();
        });

        sourceVoiceButton.setOnClickListener(v -> {
            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            try {
                startActivityForResult(intent, RESULT_SPEECH_SOURCE);
            } catch (ActivityNotFoundException a) {
                Toast.makeText(getContext(), "Your device does not support speech-to-text.", Toast.LENGTH_SHORT).show();
            }
        });

        targetVoiceButton.setOnClickListener(v -> {
            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "zh");
            try {
                startActivityForResult(intent, RESULT_SPEECH_TARGET);
            } catch (ActivityNotFoundException a) {
                Toast.makeText(getContext(), "Your device does not support speech-to-text.", Toast.LENGTH_SHORT).show();
            }
        });

        // Insert phrase if triggered from phrasebook.
        Bundle arguments = getArguments();
        if (arguments != null) {
            String selectedPhrase = getArguments().getString("selectedPhrase");
            translateFromText.setText(selectedPhrase);
            new TranslationTask("en", "zh").execute(selectedPhrase);
        }
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        TextView sourceTextView = getView().findViewById(R.id.source_text);
        TextView targetTextView = getView().findViewById(R.id.target_text);

        // Receive speech-to-text result.
        if (requestCode == RESULT_SPEECH_TARGET || requestCode == RESULT_SPEECH_SOURCE) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                String recognizedText = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS).get(0);
                if (requestCode == RESULT_SPEECH_TARGET) {
                    targetTextView.setText(recognizedText);
                    new TranslationTask("zh", "en").execute(recognizedText);
                } else {
                    sourceTextView.setText(recognizedText);
                    new TranslationTask("en", "zh").execute(recognizedText);
                }
            }
        }
    }
}
