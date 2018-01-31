package com.example.yasir.toy2searchgithublibrary;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {

    public static  EditText search_query;
    public static TextView github_link;
    public static TextView search_results;
    public static TextView error_message;
    public static ProgressBar pb;

    public void BuildURL() {
        URL url = null;
        try {
            url = new URL("https://api.github.com/search/repositories?q=" +search_query.getText().toString()+"&amp;sort=stars");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        new GithubQueryTask().execute(url);
    }
    public class GithubQueryTask extends AsyncTask<URL,Void,String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pb.setVisibility(View.VISIBLE);
            search_query.setVisibility(View.INVISIBLE);
            github_link.setVisibility(View.INVISIBLE);
            search_results.setVisibility(View.INVISIBLE);
        }

        @Override
        protected String doInBackground(URL... params) {
            URL url = params[0];

            String github_search_query = null;
            try {
                github_search_query = getResponsefromhttpURL(url);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return github_search_query;
        }

        @Override
        protected void onPostExecute(String s)

        {   pb.setVisibility(View.INVISIBLE);

            if(s!=null && s!="") {
            showJSONDataView();
            search_results.setText(s);
        }else
            showErrorView();
        }

    }
    public static String getResponsefromhttpURL(URL url) throws IOException{

        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            try {
                InputStream in = httpURLConnection.getInputStream();
                Scanner scanner = new Scanner(in);
                scanner.useDelimiter("\\A");
                boolean hasInput = scanner.hasNext();
                if (hasInput) {
                    return scanner.next();
                } else {
                    return null;
                }
            }
            finally {
                httpURLConnection.disconnect();
            }}

    public void showJSONDataView(){
        error_message.setVisibility(View.INVISIBLE);
        search_results.setVisibility(View.VISIBLE);

    }
    public void showErrorView(){
        error_message.setVisibility(View.VISIBLE);
        search_results.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        search_query=(EditText)findViewById(R.id.editText);
        github_link=(TextView)findViewById(R.id.textView);
        search_results=(TextView)findViewById(R.id.textview2);
        error_message=(TextView)findViewById(R.id.diplay_error_message);
        error_message.setText(R.string.Error_Message);
        error_message.setVisibility(View.INVISIBLE);
        pb=(ProgressBar)findViewById(R.id.loading_indicator);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int MenuItemThatWasSelected = item.getItemId();
        if(MenuItemThatWasSelected==R.id.Action_bar_1){
            BuildURL();
        }
        return super.onOptionsItemSelected(item);
    }
}