package TweetBuilder;

import java.io.BufferedInputStream;
import java.net.URL;
import java.net.URLConnection;

import android.util.Log;

public class SendRequest {

	public static String getResponse(URL url) {
		String response = "";
		try{
			Log.i("URL", url.toString());
			URLConnection con = url.openConnection();
			BufferedInputStream bis = new BufferedInputStream(con.getInputStream());
			byte[] content = new byte[1024];
			int read = 0;
			StringBuffer buff = new StringBuffer();
			while((read = bis.read(content)) != -1) {
				response = new String(content,0,read);
				buff.append(response);
			}
			return buff.toString();
		} catch(Exception e) {
			Log.e("URL IS MESSED UP!", "getResponse");
		}
		return response;
	}
}
