package TweetBuilder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import android.content.Context;
import android.util.Log;

public class FileSystemActions {
	
	public static Boolean storeFile(Context context, String filename, String content, Boolean external) {
		try{
			File file;
			FileOutputStream outputStream;
			if(external) {
				file = new File(context.getExternalFilesDir(null), filename);
				outputStream = new FileOutputStream(file);
			} else {
				outputStream = context.openFileOutput(filename, Context.MODE_PRIVATE);
			}
			outputStream.write(content.getBytes());
			outputStream.close();
		} catch(IOException e) {
			Log.e("WRITE", filename);
		}
		return true;
	}
	
	public static Boolean storeObject(Context context, String filename, String content, Boolean external) {
		try{
			File file;
			FileOutputStream fileOutput;
			ObjectOutputStream objectOutput;
			if(external) {
				file = new File(context.getExternalFilesDir(null), filename);
				fileOutput = new FileOutputStream(file);
			} else {
				fileOutput = context.openFileOutput(filename, Context.MODE_PRIVATE);
			}
			objectOutput = new ObjectOutputStream(fileOutput);
			objectOutput.writeObject(content);
			fileOutput.close();
		} catch(IOException e) {
			Log.e("WRITE", filename);
		}
		return true;
	}
}
