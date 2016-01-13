import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;
import javax.json.*;
import java.net.HttpURLConnection;
import java.nio.charset.Charset;
public class GoEuroTest
{
	static String urlsite = "http://api.goeuro.com/api/v2/position/suggest/en/";
	static FileWriter fileWriter = null;
	private static final String COMMA_DELIMITER = ",";
    private static final String NEW_LINE_SEPARATOR = "\n";

	public static String retrieveResult(String myURL) {
			System.out.println("Requested URL:" + myURL);
			StringBuilder sb = new StringBuilder();
			URLConnection urlConn = null;
			InputStreamReader in = null;
			try {
				URL url = new URL(myURL);
				urlConn = url.openConnection();
				if (urlConn != null)
					urlConn.setReadTimeout(60 * 1000);
				if (urlConn != null && urlConn.getInputStream() != null) {
					in = new InputStreamReader(urlConn.getInputStream(),
							Charset.defaultCharset());
					BufferedReader bufferedReader = new BufferedReader(in);
					if (bufferedReader != null) {
						int cp;
						while ((cp = bufferedReader.read()) != -1) {
							sb.append((char) cp);
						}
						bufferedReader.close();
					}
				}
			in.close();
			} catch (Exception e) {
				throw new RuntimeException("Exception while calling URL:"+ myURL, e);
			}

			return sb.toString();
	}

    public static void main(String args[]) throws JsonException
    {
        try
        {
			boolean isOK = args[0] != null ? args[0].matches("^[a-zA-Z]*$"):false;
			if (isOK)
			{
			String jsondata = retrieveResult(urlsite + args[0]);
			if (jsondata != null && !jsondata.equals("[]"))
			{
			JsonArray arrObj = Json.createReader(new StringReader(jsondata)).readArray();
			Iterator iterator = arrObj.iterator();
            String s = (new SimpleDateFormat("yyyyMMdd_HHmmss")).format(Calendar.getInstance().getTime()) + "-" + args[0] + ".csv";
            fileWriter = new FileWriter(s,true);
            JsonObject jsonobject;
            for(JsonValue value : arrObj){
				jsonobject = (JsonObject)iterator.next();
			    fileWriter.append(!jsonobject.isNull("_id") ? (new StringBuilder()).append(jsonobject.getInt("_id")) : null);
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(!jsonobject.isNull("name") ? jsonobject.getString("name") : null);
			    fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(!jsonobject.isNull("type") ? jsonobject.getString("type"): null);
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(Double.valueOf(jsonobject.getJsonObject("geo_position").get("latitude").toString())!=null ? (new StringBuilder()).append(jsonobject.getJsonObject("geo_position").get("latitude")) : "");
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(Double.valueOf(jsonobject.getJsonObject("geo_position").get("longitude").toString())!=null ? (new StringBuilder()).append(jsonobject.getJsonObject("geo_position").get("longitude")) : "");
				fileWriter.append(NEW_LINE_SEPARATOR);
			    System.out.println((new StringBuilder()).append(jsonobject.getInt("_id")).append(" ").append(jsonobject.getString("name")).append(" ").append(jsonobject.getString("type")).append(" ").append(jsonobject.getJsonObject("geo_position").get("latitude")).append(" ").append(jsonobject.getJsonObject("geo_position").get("longitude")).toString());
			  }
			  System.out.println("CSV file was created successfully !!!");
		  }
		  else
		  {
			  System.out.println("Information not found.");
			  }
			}
			else
			{
				System.out.println("Invalid city name.");
				}
			}
        catch(ArrayIndexOutOfBoundsException arrayindexoutofboundsexception)
        {
            System.out.println("Please provide the city name as argument.");
        }
        catch(NullPointerException exn)
        {
		    System.out.println(exn);
			}
        catch(ClassCastException exc)
        {
			System.out.println(exc);
			}
        catch(Exception exception)
        {
            System.out.println(exception);
        }
        finally {
			try {
				if (fileWriter != null)
				{
					fileWriter.flush();
					fileWriter.close();
				}
				} catch (IOException e) {
					System.out.println("Error while flushing/closing fileWriter !!!");
		               e.printStackTrace();
				}
		}
    }
}