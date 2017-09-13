package cn.edu.thu.dataset;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class DesDataFile {
    public static void main(String[] args){
        String path = "src/main/resources/uber-raw-data-ALL14.csv";
        getMaxMin(path);
    }

    public static void getMaxMin(String readPath){
        long maxTime = 0l;
        long minTime = Long.MAX_VALUE;
        float minLon = 180f;
        float minLat = 90f;
        float maxLon = -180f;
        float maxLat = -90f;
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(readPath));
            String line;
            while ((line = reader.readLine()) != null) {
               String[] items = line.split(",");
               long time = Long.parseLong(items[1]);
               float lon = Float.parseFloat(items[2]);
               float lat = Float.parseFloat(items[3]);
               if(time > maxTime){
                   maxTime = time;
               }
               if(time < minTime){
                   minTime = time;
               }

               if(lon > maxLon){
                   maxLon = lon;
               }

               if(lon < minLon){
                   minLon = lon;
               }

                if(lat > maxLat){
                    maxLat = lat;
                }

                if(lat < minLat){
                    minLat = lat;
                }

            }
            System.out.println("maxTime = " + maxTime);
            System.out.println("minTime = " + minTime);
            System.out.println("maxLon = " + maxLon);
            System.out.println("minLon = " + minLon);
            System.out.println("maxLat = " + maxLat);
            System.out.println("minLat = " + minLat);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
