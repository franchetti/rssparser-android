package gq.emiliodallatorre.trial;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.ListView;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    private ProgressDialog progressDialog;
    private List<Model> modelList;
    private ActionBar actionBar;
    private Context context;
    private WebView webView;

    private boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.items, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        int i1 = item.getItemId();
        if (i1 == R.id.who) {
            String link = ("http://istitutobrunofranchetti.gov.it/giornalino/chi-siamo");
            Intent intent = new Intent(context, view.class);
            intent.putExtra("link", link);
            context.startActivity(intent);

        } else if (i1 == R.id.mail) {
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("message/rfc822");
            i.putExtra(Intent.EXTRA_EMAIL, new String[]{"emiliodallatorre12@live.com"});
            try {
                startActivity(Intent.createChooser(i, getResources().getString(R.string.intent_send)));
            } catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(MainActivity.this, getResources().getString(R.string.intent_error), Toast.LENGTH_LONG).show();
            }

        } else if (i1 == R.id.rate) {
            try {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(getResources().getString(R.string.intent_store))));
            } catch (android.content.ActivityNotFoundException anfe) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(getResources().getString(R.string.intent_raw_url))));
            }

        }
        return true;
    }

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.listView);

        new HaberServisiAsynTask().execute(getResources().getString(R.string.rss_link));

        actionBar = getSupportActionBar();


        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);


        actionBar.setCustomView(R.layout.action_bar_title);

/*        MobileAds.initialize(getApplicationContext(), "ca-app-pub-4586118376037791~8745478356");
        AdView mAdView = (AdView) this.findViewById(R.id.adViewINMAIN);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
*/
    }

    class HaberServisiAsynTask extends AsyncTask<String, String, List<Model>> {


        @Override
        protected List<Model> doInBackground(String... params) {
            modelList = new ArrayList<Model>();
            HttpURLConnection connessione = null;
            try {
                URL url = new URL(params[0]);
                connessione = (HttpURLConnection) url.openConnection();
                int baglantiDurumu = connessione.getResponseCode();
                System.out.println(connessione);

                if (baglantiDurumu == HttpURLConnection.HTTP_OK) {

                    BufferedInputStream bufferedInputStream = new BufferedInputStream(connessione.getInputStream());
                    publishProgress(getResources().getString(R.string.loading));
                    DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
                    DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
                    Document document = documentBuilder.parse(bufferedInputStream);

                    NodeList haberNodeList = document.getElementsByTagName("item");

                    for (int i = 0; i < haberNodeList.getLength(); i++) {
                        Element element = (Element) haberNodeList.item(i);

                        NodeList nodeListTitle = element.getElementsByTagName("title");
                        NodeList nodeListLink = element.getElementsByTagName("link");
                        NodeList nodeListDate = element.getElementsByTagName("pubDate");
                        NodeList nodeListCreator = element.getElementsByTagName("dc:creator");
//                        NodeList nodeListDescription = element.getElementsByTagName("description");

                        String title = nodeListTitle.item(0).getFirstChild().getNodeValue();
                        String link = nodeListLink.item(0).getFirstChild().getNodeValue();
                        String date = nodeListDate.item(0).getFirstChild().getNodeValue();
                        String creator = nodeListCreator.item(0).getFirstChild().getNodeValue();
//                        String description = nodeListDescription.item(0).getFirstChild().getNodeValue();

/*                        String imgurl = null;
                        org.jsoup.nodes.Document documentA = null;
                        try {
                            documentA = Jsoup.connect(link).userAgent("Mozilla").get();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        try {
                            org.jsoup.nodes.Element image = documentA.select("img.alignright, img.alignleft, img.aligncenter, .size-full").first();
                            imgurl = image.absUrl("src");
                        } catch (java.lang.NullPointerException e) {
                            e.printStackTrace();
                        }
                        Bitmap bitmap = null;
                        Bitmap resizedbitmap = null;
                        try {
                            URL urlResim = new URL(imgurl);
                            bitmap = BitmapFactory.decodeStream(urlResim.openConnection().getInputStream());
                            resizedbitmap = Bitmap.createScaledBitmap(bitmap, 250, 150, true);
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
*/
                        Model model = new Model();
                        model.setTitle(title);
                        model.setLink(link);
                        model.setDate(date);
                        model.setCreator(creator);
/*                        model.setImage(resizedbitmap);
                        model.setDescription(description);
*/
                        modelList.add(model);
                        publishProgress("Caricamento...");
                    }


                } else {
                    Toast.makeText(MainActivity.this,
                            "Per cortesia, controlla di essere connesso a internet.",
                            Toast.LENGTH_SHORT).show();

                }
            } catch (IOException | ParserConfigurationException | SAXException e) {
                e.printStackTrace();
                Toast.makeText(MainActivity.this,
                        "Errore del server internet: il sito www.istitutobrunofranchetti.gov.it/giornalino non è momentaneamente raggiungibile.",
                        Toast.LENGTH_LONG).show();
            } finally {
                if (connessione != null)
                    connessione.disconnect();
            }

            return modelList;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(MainActivity.this,
                    "Caricamento...", "Caricamento...", true);
        }

        @Override
        protected void onPostExecute(List<Model> modelList) {
            super.onPostExecute(modelList);
            CustomAdaptor adapter = new CustomAdaptor(MainActivity.this, modelList);
            listView.setAdapter(adapter);
            progressDialog.cancel();
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            progressDialog.setMessage(values[0]);
        }
    }
}