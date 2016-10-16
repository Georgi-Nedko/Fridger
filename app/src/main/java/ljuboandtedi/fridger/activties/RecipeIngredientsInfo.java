package ljuboandtedi.fridger.activties;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


import ljuboandtedi.fridger.R;
import ljuboandtedi.fridger.adapters.IngredientsRecyclerAdapter;
import ljuboandtedi.fridger.model.DatabaseHelper;
import ljuboandtedi.fridger.model.Recipe;
import ljuboandtedi.fridger.model.RecipeManager;

public class RecipeIngredientsInfo extends AppCompatActivity {

    private TextView recipeName;
    private ImageView recipePic;
    private RecyclerView recipeIngredients;
    private Recipe recipe;
    private IngredientsRecyclerAdapter ingredientListAdapter;
    private Button buySelected;
    private Button buyAll;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_ingredients_info);
        buySelected = (Button) findViewById(R.id.recipeIngredients_buySelectedButton);
        buyAll = (Button) findViewById(R.id.recipeIngredients_buyAlldButton);
        recipePic = (ImageView) findViewById(R.id.recipeIngredients_recipeImage);
        recipeIngredients = (RecyclerView) findViewById(R.id.recipeIngredients_recipeIngredientsRecycle);
        recipeIngredients.setLayoutManager(new LinearLayoutManager(this));
        recipeName = (TextView) findViewById(R.id.recipeIngredients_recipeName);

        recipe = RecipeManager.recipes.get(getIntent().getStringExtra("recipe"));
        recipeName.setText(recipe.getName());

        new RequestTask().execute(recipe.getBigPicUrl());

        recipeName = (TextView) findViewById(R.id.recipeIngredients_recipeName);
        ingredientListAdapter = new IngredientsRecyclerAdapter(RecipeIngredientsInfo.this,recipe.getIngredientLines());
        recipeIngredients.setAdapter(ingredientListAdapter);
        buySelected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int numberOfPurchases = ingredientListAdapter.removeSelectedProducts();
                if (numberOfPurchases > 0) {
                    Toast.makeText(RecipeIngredientsInfo.this,numberOfPurchases + "Ingr. added to your SList", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(RecipeIngredientsInfo.this, "Nothing selected", Toast.LENGTH_SHORT).show();
                }
                //Log.e("addedToFridge", DatabaseHelper.getInstance(RecipeIngredientsInfo.this).getUserShoppingList(DatabaseHelper.getInstance(RecipeIngredientsInfo.this).getCurrentUser().getFacebookID()).toString());
            }
        });
        buyAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ingredientListAdapter.removeAll();
                Toast.makeText(RecipeIngredientsInfo.this, "Everything was added", Toast.LENGTH_SHORT).show();
                buyAll.setVisibility(View.GONE);
                buySelected.setVisibility(View.GONE);
            }
        });
    }
    private class RequestTask extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected Bitmap doInBackground(String... params) {
            String address = params[0];
            Bitmap bitmap = null;
            try {
                URL url = new URL(address);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.connect();
                InputStream is = connection.getInputStream();
                bitmap = BitmapFactory.decodeStream(is);


            } catch (IOException e) {
                e.printStackTrace();
            }

            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap image) {
            recipePic.setImageBitmap(image);
        }
    }
}
