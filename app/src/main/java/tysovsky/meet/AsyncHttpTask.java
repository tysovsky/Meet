package tysovsky.meet;

import android.os.AsyncTask;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by tysovsky on 4/4/16.
 */
public class AsyncHttpTask extends AsyncTask<String, Void, String> {
    private HttpHandler httpHandler;

    public AsyncHttpTask(HttpHandler httpHandler){
        this.httpHandler = httpHandler;
    }

    @Override
    protected String doInBackground(String... strings) {
        String result = "";
        try {

            HttpClient httpClient = new DefaultHttpClient();

            HttpResponse httpResponse = httpClient.execute(httpHandler.getHttpRequestMethod());

            result = getResponseBody(httpResponse);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    protected void onPostExecute(String result) {
        httpHandler.onResponse(result);
    }

    public static String getResponseBody(HttpResponse response){
        if(response.getEntity().getContentLength() != 0){
            StringBuilder sb = new StringBuilder();
            try{
                BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()), 65728 );
                String line = null;
                while((line = reader.readLine()) != null){
                    sb.append(line);
                }

                return sb.toString();
            }catch (IOException e){

            }catch (Exception e){

            }
        }

        return "failed";
    }
}
