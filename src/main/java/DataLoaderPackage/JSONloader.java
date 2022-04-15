package DataLoaderPackage;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import SetupPackage.Constants;

public class JSONloader {
    public static JSONArray getJson(String filename, String path) {
        JSONParser parser = new JSONParser();

        try {
            Object obj = parser.parse(new FileReader(path + filename + ".json"));
            JSONArray jsonArray = (JSONArray) obj;
            return jsonArray;
        } catch(FileNotFoundException e) {
            //e.getMessage();
            JSONloader.mergeJson(Constants.COST_FILENAME,Constants.FLOW_FILENAME,Constants.PATH);
            return getJson(filename,path);
        } catch(IOException e) {
            e.printStackTrace();
        } catch(ParseException e) {
            e.printStackTrace();
        } catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void readJson(JSONArray jsonArray) {
        if (jsonArray == null) {
            System.out.println("jsonArray is null");
            return;
        }
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jsonObject = (JSONObject) jsonArray.get(i);
            Object[] keys = jsonObject.keySet().toArray();
            for (int j = 0; j < keys.length; j++) {
                String value = String.valueOf(jsonObject.get(keys[j]));
                System.out.println(keys[j] + " " + value);
            }
            System.out.println();
        }
    }

    public static void mergeJson(String cost_filename, String flow_filename, String path) {
        JSONArray json1 = getJson(cost_filename, path);
        JSONArray json2 = getJson(flow_filename, path);
        JSONArray output = new JSONArray();

        for (int i = 0; i < json1.size(); i++) {
            JSONObject jsonObject = (JSONObject) json1.get(i);
            for (int j = 0; j < json2.size(); j++) {
                JSONObject jsonObject2 = (JSONObject) json2.get(j);

                if (jsonObject.get(Constants.SOURCE).equals(jsonObject2.get(Constants.SOURCE))
                    && jsonObject.get(Constants.DEST).equals(jsonObject2.get(Constants.DEST))) {

                    //System.out.println(jsonObject.get(Constants.source).getClass());
                    JSONObject obj = new JSONObject();
                    obj.put(Constants.SOURCE, jsonObject.get(Constants.SOURCE));
                    obj.put(Constants.DEST, jsonObject.get(Constants.DEST));
                    obj.put(Constants.COST, jsonObject.get(Constants.COST));
                    obj.put(Constants.AMOUNT, jsonObject2.get(Constants.AMOUNT));
                    output.add(obj);
                }
            }
        }

        try (FileWriter file = new FileWriter(path + cost_filename + flow_filename + ".json")) {
            file.write(output.toJSONString());
            file.flush();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}
