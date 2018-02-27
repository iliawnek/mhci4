package uk.ac.gla.shopping.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntro2Fragment;

import uk.ac.gla.shopping.R;

public class IntroActivity extends AppIntro {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addSlide(AppIntro2Fragment.newInstance("", "Welcome to your shopping translation assistant.", R.drawable.intro_logo, getResources().getColor(R.color.colorPrimaryDark)));
        addSlide(AppIntro2Fragment.newInstance("", "Keep track of your shopping with the auto-translated shopping list.", R.mipmap.intro_shopping_list, getResources().getColor(R.color.colorPrimaryDark)));
        addSlide(AppIntro2Fragment.newInstance("", "Translate product labels with the scanner.", R.mipmap.intro_scanner, getResources().getColor(R.color.colorPrimaryDark)));
        addSlide(AppIntro2Fragment.newInstance("", "Communicate with staff using the conversation translator.", R.mipmap.intro_conversation_translate, getResources().getColor(R.color.colorPrimaryDark)));
        addSlide(AppIntro2Fragment.newInstance("", "Get quick access to many common phrases using the phrasebook.", R.mipmap.intro_phrasebook, getResources().getColor(R.color.colorPrimaryDark)));
    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        Intent mainIntent = new Intent(this, MainActivity.class);
        startActivity(mainIntent);
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        Intent mainIntent = new Intent(this, MainActivity.class);
        startActivity(mainIntent);
    }
}