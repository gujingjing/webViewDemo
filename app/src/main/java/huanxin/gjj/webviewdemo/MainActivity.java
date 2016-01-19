package huanxin.gjj.webviewdemo;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * http://blog.csdn.net/dacainiao007/article/details/21176993
 */
public class MainActivity extends AppCompatActivity {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.myButton)
    Button myButton;
    @Bind(R.id.myProgressBar)
    ProgressBar myProgressBar;
    @Bind(R.id.myWebView)
    WebView myWebView;
    @Bind(R.id.fab)
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initWebView();
    }
    public void initWebView(){
        WebSettings webSettings = myWebView.getSettings();
        //设置是否保存密码
        webSettings.setSavePassword(false);
        webSettings.setSaveFormData(false);
        // 如果访问的页面中有JavaScript，则WebView必须设置支持JavaScript ，否则显示空白页面
        webSettings.setJavaScriptEnabled(true);
        webSettings.setSupportZoom(false);
        //设置允许访问文件数据
        webSettings.setAllowFileAccess(true);
        //设置支持缩放
        webSettings.setBuiltInZoomControls(true);

        myWebView.setWebChromeClient(new MyWebChromeClient());
        //使用默认的webView加载url时---重写shouldOverrideUrlLoading方法
        myWebView.setWebViewClient(new MyWebViewClient());
        //下载文件的监听
//        myWebView.setDownloadListener(new WebViewDownloadListener());
        //使用localStorage则必须打开 这个主要是用在html5中的本地存储。
        webSettings.setDomStorageEnabled(true);
        webSettings.setSaveFormData(false);
        //Webview与js的双向交互才是android的webview强大所在(因为安全问题在API17之后以取消) ,可以暂不关心
//        myWebView.addJavascriptInterface(new DemoJavaScriptInterface(), "chen");

        //      设置WevView要显示的网页：
//      互联网用：webView.loadUrl("http://www.google.com");
//      本地文件用：webView.loadUrl("file:///android_asset/XX.html");  本地文件存放在：assets文件中
        myWebView.loadUrl("http://www.baidu.com");

        //      如果用webview点链接看了很多页以后，如果不做任何处理，点击系统“Back”键，
//      整个浏览器会调用finish()而结束自身，如果希望浏览的网页回退而不是退出浏览器，需要在当前Activity中处理并消费掉该Back事件。
//      覆盖Activity类的onKeyDown(int keyCoder,KeyEvent event)方法。

        myWebView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && myWebView != null && myWebView.canGoBack()) {
                    myWebView.goBack();
                    return true;//return true表示这个事件已经被消费，不会再向上传播
                }
                return false;
            }
        });
    }
    @OnClick({R.id.myButton})void onclick(View view){
        switch (view.getId()){
            case R.id.myButton:
                myWebView.reload();
                break;
        }
    }

//    final class DemoJavaScriptInterface {
//
//        DemoJavaScriptInterface() {
//        }
//
//        /**
//         * This is not called on the UI thread. Post a runnable to invoke
//         * loadUrl on the UI thread.
//         */
//        public void clickOnAndroid() {
//            mHandler.post(new Runnable() {
//                public void run() {
//                    mWebView.loadUrl("javascript:wave()");
//                }
//            });
//
//        }
//    }

    /**
     * Provides a hook for calling "alert" from javascript. Useful for
     * debugging your javascript.
     */
    final class MyWebChromeClient extends WebChromeClient {
        @Override
        public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
            Log.d("MyWebChromeClient===", message);
            result.confirm();
            return true;
        }
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            if(newProgress==100){
                myProgressBar.setVisibility(View.INVISIBLE);
                myProgressBar.setProgress(newProgress);
            }else{
                myProgressBar.setVisibility(View.VISIBLE);
                myProgressBar.setProgress(newProgress);
            }
        }
    }
    public class MyWebViewClient extends WebViewClient{
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url); //在当前的webview中跳转到新的url
            return true;
        }
    }
}
