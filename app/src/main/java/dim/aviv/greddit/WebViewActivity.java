package dim.aviv.greddit;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * Created by אביב on 12/03/2019.
 */

public class WebViewActivity  extends AppCompatActivity{
    private static final String TAG = "WebViewActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview_layout);

        WebView webview = (WebView) findViewById(R.id.webview);
        final ProgressBar progressbar = (ProgressBar) findViewById(R.id.webvireLoadingProgressBar);
        final TextView loadingText = (TextView) findViewById(R.id.progressText);

        Log.d(TAG, "onCreate: starting...");

        progressbar.setVisibility(View.VISIBLE);

        Intent intent = getIntent();
        String url = intent.getStringExtra("url");
        webview.getSettings().setJavaScriptEnabled(true);
        webview.loadUrl(url);

        webview.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                progressbar.setVisibility(View.GONE);
                loadingText.setText("");
            }
        });
    }
}
