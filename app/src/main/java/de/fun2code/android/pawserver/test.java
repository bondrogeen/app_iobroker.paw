package de.fun2code.android.pawserver;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Roman on 09.05.2017.
 */

public class test {

    public static void test() throws ParseException, IOException {

        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost("http://192.168.1.31:8898");

        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

        nameValuePairs.add(new BasicNameValuePair("father_name", "Foo"));
        nameValuePairs.add(new BasicNameValuePair("mother_name", "Bar"));

        post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
        HttpResponse response = null;

        try {
            response = client.execute(post);
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String stringifiedResponse = EntityUtils.toString(response.getEntity());

        System.out.println(stringifiedResponse);


    }

}
