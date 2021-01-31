package com.example.meamsik;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    String currentImageUrl = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadMeme();
    }


    private void loadMeme(){
        ProgressBar progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        ImageView imageView = findViewById(R.id.memeImageView);
// for RequestQueue
 //       RequestQueue queue = Volley.newRequestQueue(this);

        // API
        String url = "https://meme-api.herokuapp.com/gimme";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            currentImageUrl = response.getString("url");
                            Glide
                                    .with(getApplicationContext())
                                    .load(currentImageUrl)
                                    .listener(new RequestListener<Drawable>() {
                                        @Override
                                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                            progressBar.setVisibility(View.GONE);
                                            return false;
                                        }

                                        @Override
                                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                            progressBar.setVisibility(View.GONE);
                                            return false;
                                        }
                                    })
                                    .into(imageView);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {


                    }
                });

        // Add the request to the RequestQueue.
        // queue.add(jsonObjectRequest);

//      OR
        // Add to MySingleton
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }

    public void shareMeme(View view) {
//      for sharing link
//
         Intent intent = new Intent(Intent.ACTION_SEND);
         intent.setType("text/plain");
         intent.putExtra(Intent.EXTRA_TEXT, "Hey checkout this Meme i found on Meamsik "+currentImageUrl);
         Intent chooser = Intent.createChooser(intent, "Share this Meme using...");
         startActivity(chooser);

//        // for sharing image ---- in progress
//        Drawable mDrawable = imageView.getDrawable();
//        Bitmap mBitmap = ((BitmapDrawable)mDrawable).getBitmap();
//        String path = MediaStore.Images.Media.insertImage(getContentResolver(), mBitmap, "R.id.memeImageView", null);
//        Uri screenshotUri = Uri.parse(path);
//
//
//     //   Uri screenshotUri = Uri.parse(MediaStore.Images.Media.EXTERNAL_CONTENT_URI + "/" + R.id.memeImageView);
//        Intent intent = new Intent(Intent.ACTION_SEND);
//        intent.setType("image/*");
//
//        intent.putExtra(Intent.EXTRA_STREAM, screenshotUri);
//        Intent chooser = Intent.createChooser(intent, "Share this Meme using...");
//        startActivity(chooser);
    }

    public void nextMeme(View view) {
        loadMeme();
    }
}