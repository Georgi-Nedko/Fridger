package ljuboandtedi.fridger.activties;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

import ljuboandtedi.fridger.R;
import ljuboandtedi.fridger.model.DatabaseHelper;
import ljuboandtedi.fridger.model.User;

public class ProfileActivity extends DrawerActivity {
    CheckBox vegan, vegetarian, pascetarian, ovovegetarian, lactovegetarian, paleo, dairy, egg,
            gluten, peanut, seafood, sesame, soy, sulfite, treenut, wheat, american, italian, asian,
            mexican, southernAndSoulFood, french, southwestern, barbecue, indian, chinese,
            cajunAndCreole, english, mediterranean, greek, spanish, german, thai, moroccan, irish,
            japanese, cuban, hawaiian, swedish, hungarian, portuguese;
    Button confirm;
    DatabaseHelper db;
    User user;
    ImageButton profilePicture;
    TextView nameTV;
    TextView emailTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.replaceContentLayout(R.layout.activity_profile, super.CONTENT_LAYOUT_ID);
        db = DatabaseHelper.getInstance(ProfileActivity.this);
        user = db.getCurrentUser();
        profilePicture= (ImageButton) findViewById(R.id.user_profile_photo);
        Glide.with(getApplicationContext()).load(Uri.parse(prefs.getString("pic", "")))
                .asBitmap().centerCrop().into(new BitmapImageViewTarget(profilePicture) {
            @Override
            protected void setResource(Bitmap resource) {
                RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory
                        .create(getApplicationContext().getResources(), resource);
                circularBitmapDrawable.setCircular(true);
                profilePicture.setImageDrawable(circularBitmapDrawable);
            }
        });
        nameTV = (TextView) findViewById(R.id.user_profile_name);
        nameTV.setText(prefs.getString("name", ""));
        emailTV = (TextView) findViewById(R.id.user_profile_short_bio);
        emailTV.setText(prefs.getString("email", ""));
        vegan = (CheckBox) findViewById(R.id.profile_checkBox_vegan);
        vegetarian = (CheckBox) findViewById(R.id.profile_checkBox_vegetarian);
        pascetarian = (CheckBox) findViewById(R.id.profile_checkBox_pascetarian);
        ovovegetarian = (CheckBox) findViewById(R.id.profile_checkBox_ovoVegetarian);
        lactovegetarian = (CheckBox) findViewById(R.id.profile_checkBox_lactoVegetarian);
        paleo = (CheckBox) findViewById(R.id.profile_checkBox_paleo);
        dairy = (CheckBox) findViewById(R.id.profile_checkBox_dairy);
        egg = (CheckBox) findViewById(R.id.profile_checkBox_egg);
        gluten = (CheckBox) findViewById(R.id.profile_checkBox_gluten);
        peanut = (CheckBox) findViewById(R.id.profile_checkBox_peanut);
        seafood = (CheckBox) findViewById(R.id.profile_checkBox_seafood);
        sesame = (CheckBox) findViewById(R.id.profile_checkBox_sesame);
        soy = (CheckBox) findViewById(R.id.profile_checkBox_soy);
        sulfite = (CheckBox) findViewById(R.id.profile_checkBox_sulfite);
        treenut = (CheckBox) findViewById(R.id.profile_checkBox_treeNut);
        wheat = (CheckBox) findViewById(R.id.profile_checkBox_wheat);
        american = (CheckBox) findViewById(R.id.profile_checkBox_american);
        italian = (CheckBox) findViewById(R.id.profile_checkBox_italian);
        asian = (CheckBox) findViewById(R.id.profile_checkBox_asian);
        mexican = (CheckBox) findViewById(R.id.profile_checkBox_mexican);
        southernAndSoulFood = (CheckBox) findViewById(R.id.profile_checkBox_southernAndSoulFood);
        french = (CheckBox) findViewById(R.id.profile_checkBox_french);
        southwestern = (CheckBox) findViewById(R.id.profile_checkBox_southwestern);
        barbecue = (CheckBox) findViewById(R.id.profile_checkBox_barbecue);
        indian = (CheckBox) findViewById(R.id.profile_checkBox_indian);
        chinese = (CheckBox) findViewById(R.id.profile_checkBox_chinese);
        cajunAndCreole = (CheckBox) findViewById(R.id.profile_checkBox_cajunAndCreole);
        english = (CheckBox) findViewById(R.id.profile_checkBox_english);
        mediterranean = (CheckBox) findViewById(R.id.profile_checkBox_mediterranean);
        greek = (CheckBox) findViewById(R.id.profile_checkBox_greek);
        spanish = (CheckBox) findViewById(R.id.profile_checkBox_spanish);
        german = (CheckBox) findViewById(R.id.profile_checkBox_german);
        thai = (CheckBox) findViewById(R.id.profile_checkBox_thai);
        moroccan = (CheckBox) findViewById(R.id.profile_checkBox_moroccan);
        irish = (CheckBox) findViewById(R.id.profile_checkBox_irish);
        japanese = (CheckBox) findViewById(R.id.profile_checkBox_japanese);
        cuban = (CheckBox) findViewById(R.id.profile_checkBox_cuban);
        hawaiian = (CheckBox) findViewById(R.id.profile_checkBox_hawaiian);
        swedish = (CheckBox) findViewById(R.id.profile_checkBox_swedish);
        hungarian = (CheckBox) findViewById(R.id.profile_checkBox_hungarian);
        portuguese = (CheckBox) findViewById(R.id.profile_checkBox_portugese);

        confirm = (Button) findViewById(R.id.profile_confirm_button);
        Cursor res = db.getUser(user.getFacebookID());
        res.moveToFirst();
        if (res.getString(1).equalsIgnoreCase("YES")) vegan.setChecked(true);
        if (res.getString(2).equalsIgnoreCase("YES")) vegetarian.setChecked(true);
        if (res.getString(3).equalsIgnoreCase("YES")) pascetarian.setChecked(true);
        if (res.getString(4).equalsIgnoreCase("YES")) ovovegetarian.setChecked(true);
        if (res.getString(5).equalsIgnoreCase("YES")) lactovegetarian.setChecked(true);
        if (res.getString(6).equalsIgnoreCase("YES")) paleo.setChecked(true);
        if (res.getString(7).equalsIgnoreCase("YES")) dairy.setChecked(true);
        if (res.getString(8).equalsIgnoreCase("YES")) egg.setChecked(true);
        if (res.getString(9).equalsIgnoreCase("YES")) gluten.setChecked(true);
        if (res.getString(10).equalsIgnoreCase("YES")) peanut.setChecked(true);
        if (res.getString(11).equalsIgnoreCase("YES")) seafood.setChecked(true);
        if (res.getString(12).equalsIgnoreCase("YES")) sesame.setChecked(true);
        if (res.getString(13).equalsIgnoreCase("YES")) soy.setChecked(true);
        if (res.getString(14).equalsIgnoreCase("YES")) sulfite.setChecked(true);
        if (res.getString(15).equalsIgnoreCase("YES")) treenut.setChecked(true);
        if (res.getString(16).equalsIgnoreCase("YES")) wheat.setChecked(true);
        if (res.getString(17).equalsIgnoreCase("YES")) american.setChecked(true);
        if (res.getString(18).equalsIgnoreCase("YES")) italian.setChecked(true);
        if (res.getString(19).equalsIgnoreCase("YES")) asian.setChecked(true);
        if (res.getString(20).equalsIgnoreCase("YES")) mexican.setChecked(true);
        if (res.getString(21).equalsIgnoreCase("YES")) southernAndSoulFood.setChecked(true);
        if (res.getString(22).equalsIgnoreCase("YES")) french.setChecked(true);
        if (res.getString(23).equalsIgnoreCase("YES")) southwestern.setChecked(true);
        if (res.getString(24).equalsIgnoreCase("YES")) barbecue.setChecked(true);
        if (res.getString(25).equalsIgnoreCase("YES")) indian.setChecked(true);
        if (res.getString(26).equalsIgnoreCase("YES")) chinese.setChecked(true);
        if (res.getString(27).equalsIgnoreCase("YES")) cajunAndCreole.setChecked(true);
        if (res.getString(28).equalsIgnoreCase("YES")) english.setChecked(true);
        if (res.getString(29).equalsIgnoreCase("YES")) mediterranean.setChecked(true);
        if (res.getString(30).equalsIgnoreCase("YES")) greek.setChecked(true);
        if (res.getString(31).equalsIgnoreCase("YES")) spanish.setChecked(true);
        if (res.getString(32).equalsIgnoreCase("YES")) german.setChecked(true);
        if (res.getString(33).equalsIgnoreCase("YES")) thai.setChecked(true);
        if (res.getString(34).equalsIgnoreCase("YES")) moroccan.setChecked(true);
        if (res.getString(35).equalsIgnoreCase("YES")) irish.setChecked(true);
        if (res.getString(36).equalsIgnoreCase("YES")) japanese.setChecked(true);
        if (res.getString(37).equalsIgnoreCase("YES")) cuban.setChecked(true);
        if (res.getString(38).equalsIgnoreCase("YES")) hawaiian.setChecked(true);
        if (res.getString(39).equalsIgnoreCase("YES")) swedish.setChecked(true);
        if (res.getString(40).equalsIgnoreCase("YES")) hungarian.setChecked(true);
        if (res.getString(41).equalsIgnoreCase("YES")) portuguese.setChecked(true);
        res.close();
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.editUserPrefs(user.getFacebookID(), vegan.isChecked(), vegetarian.isChecked(),
                        pascetarian.isChecked(), ovovegetarian.isChecked(),
                        lactovegetarian.isChecked(), paleo.isChecked(), dairy.isChecked(),
                        egg.isChecked(), gluten.isChecked(), peanut.isChecked(), seafood.isChecked(),
                        sesame.isChecked(), soy.isChecked(), sulfite.isChecked(), treenut.isChecked(),
                        wheat.isChecked(), american.isChecked(), italian.isChecked(),
                        asian.isChecked(), mexican.isChecked(), southernAndSoulFood.isChecked(),
                        french.isChecked(), southwestern.isChecked(), barbecue.isChecked(),
                        indian.isChecked(), chinese.isChecked(), cajunAndCreole.isChecked(),
                        english.isChecked(), mediterranean.isChecked(), greek.isChecked(),
                        spanish.isChecked(), german.isChecked(), thai.isChecked(),
                        moroccan.isChecked(), irish.isChecked(), japanese.isChecked(),
                        cuban.isChecked(), hawaiian.isChecked(), swedish.isChecked(),
                        hungarian.isChecked(), portuguese.isChecked());
                startActivity(new Intent(ProfileActivity.this, MainActivity.class));
                finish();
            }
        });
    }
}
