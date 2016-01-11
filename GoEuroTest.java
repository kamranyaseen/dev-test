import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;
import javax.json.*;

public class GoEuroTest
{

    public GoEuroTest()
    {
    }

    public static void main(String args[])
    {
        try
        {
            URLConnection urlconnection = (new URL((new StringBuilder()).append("http://api.goeuro.com/api/v2/position/suggest/en/").append(args[0]).toString())).openConnection();
            InputStream inputstream = urlconnection.getInputStream();
            JsonArray jsonarray = Json.createReader(inputstream).readArray();
            Iterator iterator = jsonarray.iterator();
            String s = (new SimpleDateFormat("yyyyMMdd_HHmmss")).format(Calendar.getInstance().getTime());
            File file = new File(s);
            BufferedWriter bufferedwriter = new BufferedWriter(new FileWriter((new StringBuilder()).append(file).append("-").append(args[0]).append(".csv").toString()));
            JsonObject jsonobject;
            for(; iterator.hasNext(); bufferedwriter.write((new StringBuilder()).append(jsonobject.getInt("_id")).append(",").append(jsonobject.getString("name")).append(",").append(jsonobject.getString("type")).append(",").append(jsonobject.getJsonObject("geo_position").get("latitude")).append(",").append(jsonobject.getJsonObject("geo_position").get("longitude")).append("\n").toString()))
            {
                jsonobject = (JsonObject)iterator.next();
                System.out.println((new StringBuilder()).append(jsonobject.getInt("_id")).append(" ").append(jsonobject.getString("name")).append(" ").append(jsonobject.getString("type")).append(" ").append(jsonobject.getJsonObject("geo_position").get("latitude")).append(" ").append(jsonobject.getJsonObject("geo_position").get("longitude")).toString());
            }

            bufferedwriter.close();
            inputstream.close();
            System.out.println("File generated successfully.");
        }
        catch(ArrayIndexOutOfBoundsException arrayindexoutofboundsexception)
        {
            System.out.println("Please provide the city name as argument.");
        }
        catch(Exception exception)
        {
            System.out.println(exception);
        }
    }
}