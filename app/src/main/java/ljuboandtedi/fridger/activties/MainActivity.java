package ljuboandtedi.fridger.activties;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import android.widget.Button;

import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;


import it.michelelacorte.elasticprogressbar.ElasticDownloadView;
import ljuboandtedi.fridger.R;
import ljuboandtedi.fridger.adapters.MealAdapter;
import ljuboandtedi.fridger.model.IngredientValues;
import ljuboandtedi.fridger.model.Recipe;
import ljuboandtedi.fridger.model.RecipeManager;

public class MainActivity extends DrawerActivity {

    private Button searchActivityButton;
    private ArrayList<Bitmap> bitmaps2;
    private ArrayList<String> recipesByName2;
    private SwipeFlingAdapterView flingContainer2;
    private ElasticDownloadView mElasticDownloadView;

    private String[] searches = {"soup","tomato","desert","chocolate","pizza","coke"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        super.replaceContentLayout(R.layout.activity_main, super.CONTENT_LAYOUT_ID);
        mElasticDownloadView = (ElasticDownloadView) findViewById(R.id.elastic_download_view);
        mElasticDownloadView.startIntro();
        mElasticDownloadView.setProgress(5);

        searchActivityButton = (Button) findViewById(R.id.intenting);
        searchActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,SearchingActivity.class);
                startActivity(intent);
            }
        });

        bitmaps2 = new ArrayList<>();
        flingContainer2 = (SwipeFlingAdapterView) findViewById(R.id.frame2);
        recipesByName2 = new ArrayList<>();
        mElasticDownloadView.setProgress(50);
        new RequestTaskForRelatedMeals(recipesByName2,bitmaps2,flingContainer2)
                .execute("http://api.yummly.com/v1/api/recipes?_"+getResources()
                        .getString(R.string.api)+"&q="+searchQuery()+user.getPreferences()
                        +"&maxResult=20&start=10");

    }

    private String searchQuery() {
        Random random = new Random();
        return searches[random.nextInt(searches.length-1)];
    }

    private class RequestTaskForRelatedMeals extends AsyncTask<String, String, String> {
        private ArrayList<String> recipes;
        private ArrayList<Bitmap> bitmaps;
        private SwipeFlingAdapterView flingContainer;
        private ArrayList<String> recipeIds;

        RequestTaskForRelatedMeals(ArrayList<String> recipes, ArrayList<Bitmap> bitmaps ,SwipeFlingAdapterView flingContainer){
            this.recipes = recipes;
            this.bitmaps = bitmaps;
            this.flingContainer = flingContainer;
            this.recipeIds = new ArrayList<>();
        }

        @Override
        protected String doInBackground(String... params) {
            String address = params[0];
            String json = "";
            try {
                URL url = new URL(address);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.connect();
                Scanner sc = new Scanner(connection.getInputStream());
                while (sc.hasNextLine()) {
                    json += (sc.nextLine());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return json;
        }

        @Override
        protected void onPostExecute(String jsonX) {
            try {

                JSONObject json = new JSONObject(jsonX);
                JSONArray matches = json.getJSONArray("matches");

                for (int i = 0; i < matches.length(); i++) {
                    StringBuilder mealFlavors = new StringBuilder();
                    Log.i("matches", matches.length() + "");
                    String attributes = matches.getString(i);
                    JSONObject attributesInJson = new JSONObject(attributes);
                    if (!attributesInJson.isNull("flavors")) {
                        JSONObject flavorsInJson = attributesInJson.getJSONObject("flavors");

                        mealFlavors.append("piquant: ").append(flavorsInJson.getDouble("piquant"));
                        mealFlavors.append("\nmeaty: ").append(flavorsInJson.getDouble("meaty"));
                        mealFlavors.append("\nbitter: ").append(flavorsInJson.getDouble("bitter"));
                        mealFlavors.append("\nsweet: ").append(flavorsInJson.getDouble("sweet"));
                        mealFlavors.append("\nsour: " ).append(flavorsInJson.getDouble("sour"));
                        mealFlavors.append("\nsalty: ").append(flavorsInJson.getDouble("salty"));
                    }
                    String id = "";
                    if (!attributesInJson.isNull("id")) {
                        id = attributesInJson.getString("id");
                    }
                    recipeIds.add(id);

                    new RequestTaskForRecipe(recipes,bitmaps,flingContainer,recipeIds)
                            .execute("http://api.yummly.com/v1/api/recipe/" +id+ "?_"
                                    +getResources().getString(R.string.api));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    private class RequestTaskForRecipe extends AsyncTask<String, Void, String> {
        private ArrayList<String> recipes;
        private SwipeFlingAdapterView flingContainer;
        private ArrayList<Bitmap> bitmaps;
        private ArrayList<String> recipeIds;

        RequestTaskForRecipe(ArrayList<String> recipes, ArrayList<Bitmap> bitmaps,
                             SwipeFlingAdapterView flingContainer,ArrayList<String> recipeIds){
            this.recipes = recipes;
            this.bitmaps = bitmaps;
            this.flingContainer = flingContainer;
            this.recipeIds = recipeIds;
        }

        @Override
        protected String doInBackground(String... params) {
            Log.e("params", params[0]);
            String address = params[0];
            String json = "";

            try {
                URL url = new URL(address);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.connect();
                Scanner sc = new Scanner(connection.getInputStream());
                while (sc.hasNextLine()) {
                    json += (sc.nextLine());
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            return json;
        }

        @Override
        protected void onPostExecute(String json) {
            try {
                JSONObject object = new JSONObject(json);
                JSONArray ingredientLines = object.getJSONArray("ingredientLines");
                ArrayList<String> ingredientLinesArr = new ArrayList<>();

                for (int i = 0; i < ingredientLines.length(); i++) {
                    ingredientLinesArr.add(ingredientLines.getString(i));
                }
                HashMap<String, Double> flavorsMap = new HashMap<>();
                if (!object.isNull("flavors")) {
                    JSONObject flavors = object.getJSONObject("flavors");
                    flavorsMap = new HashMap<>();
                    if (!flavors.isNull("Salty")) {
                        flavorsMap.put("Salty", flavors.getDouble("Salty"));
                    }
                    if (!flavors.isNull("Meaty")) {
                        flavorsMap.put("Meaty", flavors.getDouble("Meaty"));
                    }
                    if (!flavors.isNull("Piquant")) {
                        flavorsMap.put("Piquant", flavors.getDouble("Piquant"));
                    }
                    if (!flavors.isNull("Bitter")) {
                        flavorsMap.put("Bitter", flavors.getDouble("Bitter"));
                    }
                    if (!flavors.isNull("Sour")) {
                        flavorsMap.put("Sour", flavors.getDouble("Sour"));
                    }
                    if (!flavors.isNull("Sweet")) {
                        flavorsMap.put("Sweet", flavors.getDouble("Sweet"));
                    }
                }
                JSONArray nutritions = object.getJSONArray("nutritionEstimates");
                ArrayList<IngredientValues> nutritionsValues = new ArrayList<>();
                double fatKCAL = 0.0;
                for (int i = 0; i < nutritions.length(); i++) {
                    JSONObject nutrition = nutritions.getJSONObject(i);
                    if (nutrition.getString("attribute").equals("FAT_KCAL")) {
                        fatKCAL = nutrition.getDouble("value");
                    } else {
                        IngredientValues ingrValue = new IngredientValues(nutrition
                                .getString("description"), nutrition.getDouble("value"));
                        nutritionsValues.add(ingrValue);
                    }
                }

                String nameOfRecipe = object.getString("name");
                String servings = object.getString("yield");
                String totalTime = object.getString("totalTime");
                Double rating = object.getDouble("rating");
                JSONArray images = object.getJSONArray("images");
                String bigPicUrl = images.getJSONObject(0).getString("hostedLargeUrl");
                String smallPicUrl = images.getJSONObject(0).getString("hostedSmallUrl");
                String id = object.getString("id");
                String numberOfServings = object.getString("numberOfServings");
                ArrayList<String> coursesForTheRecipe = new ArrayList<>();
                JSONObject sources = object.getJSONObject("source");
                String source = sources.getString("sourceRecipeUrl");
                String creator = sources.getString("sourceDisplayName");
                JSONObject course = object.getJSONObject("attributes");

                if (!course.isNull("course")) {
                    JSONArray courses = course.getJSONArray("course");
                    for (int i = 0; i < courses.length(); i++) {
                        coursesForTheRecipe.add(courses.getString(i));
                    }
                }
                Recipe recipe = new Recipe(ingredientLinesArr, flavorsMap, nutritionsValues,
                        nameOfRecipe, servings, totalTime, rating, bigPicUrl, id, numberOfServings,
                        coursesForTheRecipe, source, creator, fatKCAL,smallPicUrl);
                RecipeManager.recipes.put(recipe.getName(),recipe);
                recipes.add(recipe.getName());

                new RequestTask(recipes,bitmaps,flingContainer,recipeIds).execute(recipe
                        .getBigPicUrl());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            mElasticDownloadView.setProgress(100);
        }

       private class RequestTask extends AsyncTask<String, Void, Bitmap> {
           private ArrayList<String> recipes;
           private SwipeFlingAdapterView flingContainer;
           private ArrayList<Bitmap> bitmaps;
           private ArrayList<String> recipeIds;
           private String removedRecipe = "";
           private ArrayList<Bitmap> bitmapsReplacers;
            RequestTask( ArrayList<String> recipes, ArrayList<Bitmap> bitmaps,
                         SwipeFlingAdapterView flingContainer,ArrayList<String> recipeIds){
                this.recipes = recipes;
                this.bitmaps = bitmaps;
                this.flingContainer = flingContainer;
                this.recipeIds = recipeIds;
                bitmapsReplacers = new ArrayList<>();
            }
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

                bitmaps.add(image);
                bitmapsReplacers.add(image);
                if(bitmaps.size() == 20){
                    mElasticDownloadView.success();
                    mElasticDownloadView.setVisibility(View.GONE);
                    final  MealAdapter mealAdapter = new MealAdapter(MainActivity.this, bitmaps);
                    flingContainer.setAdapter(mealAdapter);
                    mealAdapter.notifyDataSetChanged();
                    flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
                        @Override
                        public void removeFirstObjectInAdapter() {
                            mealAdapter.remove(bitmaps.get(0));
                            recipes.remove(0);
                            removedRecipe = recipeIds.get(0);
                            recipeIds.remove(0);
                         if(bitmaps.size() < 1){
                             startActivity(new Intent(MainActivity.this,MainActivity.class));
                         }
                            mealAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onLeftCardExit(Object dataObject) { }

                        @Override
                        public void onRightCardExit(Object dataObject) {
                            if(!user.getFavouriteMeals().contains(removedRecipe)){
                                user.addToFavoriteMeals(removedRecipe);
                            }
                        }

                        @Override
                        public void onAdapterAboutToEmpty(int itemsInAdapter) {  }

                        @Override
                        public void onScroll(float v) { }


                    });
                    flingContainer.setOnItemClickListener(new SwipeFlingAdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClicked(int itemPosition, Object dataObject) {
                            String recipeName = recipes.get(itemPosition);
                            Intent intent = new Intent(MainActivity.this,RecipeInfoActivity.class);
                            intent.putExtra("recipe",recipeName);
                            startActivity(intent);
                        }
                    });
                }
            }
        }
    }
}