package cn.edu.thu.dataset;

import cn.edu.tsinghua.tsfile.common.utils.RandomAccessOutputStream;
import cn.edu.tsinghua.tsfile.common.utils.TSRandomAccessFileWriter;
import cn.edu.tsinghua.tsfile.file.metadata.enums.TSDataType;
import cn.edu.tsinghua.tsfile.timeseries.basis.TsFile;
import cn.edu.tsinghua.tsfile.timeseries.write.exception.WriteProcessException;
import cn.edu.tsinghua.tsfile.timeseries.write.schema.FileSchema;
import cn.edu.tsinghua.tsfile.timeseries.write.schema.SchemaBuilder;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class GenSkewData {

    public static void main(String[] args) throws IOException, WriteProcessException {
        if(args.length<1) {
            System.out.println("genDataFolder");
        }
        String folder = "src/main/resources";
        int deviceNumber = 5;
        // one year
        long dataSize = 365 * 24 * 60;

        System.out.println(transferDataToLong("01/01/2010 00:00:00"));
        System.out.println(transferDataToLong("01/03/2011 00:00:00"));
        System.out.println(transferDataToLong("01/05/2012 00:00:00"));
        System.out.println(transferDataToLong("01/07/2013 00:00:00"));
        System.out.println(transferDataToLong("01/09/2014 00:00:00"));
        System.out.println(transferDataToLong("01/11/2015 00:00:00"));
        System.out.println(transferDataToLong("01/13/2016 00:00:00"));

        gen7Years(folder + "/2010-2017.tsfile", deviceNumber, dataSize);
//
//        gen1Year(folder + "/2010.tsfile", "01/01/2010 00:00:00", deviceNumber, dataSize, -180f, 180f, -90f, 90f);
//        gen1Year(folder + "/2011.tsfile", "01/03/2011 00:00:00", deviceNumber, dataSize, 0f, 180f, -90f, 90f);
//        gen1Year(folder + "/2012.tsfile", "01/05/2012 00:00:00", deviceNumber, dataSize, 0f, 180f, 0f, 90f);
//        gen1Year(folder + "/2013.tsfile", "01/07/2013 00:00:00", deviceNumber, dataSize, 0f, 90f, 0f, 90f);
//        gen1Year(folder + "/2014.tsfile", "01/09/2014 00:00:00", deviceNumber, dataSize, 0f, 40f, 0f, 20f);
//        gen1Year(folder + "/2015.tsfile", "01/11/2015 00:00:00", deviceNumber, dataSize, 0f, 20f, 0f, 10f);
//        gen1Year(folder + "/2016.tsfile", "01/13/2016 00:00:00", deviceNumber, dataSize, 0f, 10f, 0f, 5f);

    }

    private static void gen1Year(String filepath, String time, int deviceNumber, long dataSize, float min_lon, float max_lon, float min_lat, float max_lat) throws IOException, WriteProcessException {
        TSRandomAccessFileWriter output = new RandomAccessOutputStream(new File(filepath));
        SchemaBuilder builder = new SchemaBuilder();
        builder.addSeries("lon", TSDataType.FLOAT, "RLE", null);
        builder.addSeries("lat", TSDataType.FLOAT, "RLE", null);
        FileSchema fileSchema = builder.build();

        TsFile tsFile = new TsFile(output, fileSchema);
        generateRandomData(tsFile, time, deviceNumber, dataSize, min_lon, max_lon, min_lat, max_lat);
        tsFile.close();
    }

    private static void gen7Years(String filepath, int deviceNumber, long dataSize) throws IOException, WriteProcessException {
        TSRandomAccessFileWriter output = new RandomAccessOutputStream(new File(filepath));
        SchemaBuilder builder = new SchemaBuilder();
        builder.addSeries("lon", TSDataType.FLOAT, "RLE", null);
        builder.addSeries("lat", TSDataType.FLOAT, "RLE", null);
        FileSchema fileSchema = builder.build();

        TsFile tsFile = new TsFile(output, fileSchema);
        generateRandomData(tsFile, "01/01/2010 00:00:00", deviceNumber, dataSize, -180f, 180f, -90f, 90f);
        generateRandomData(tsFile, "01/03/2011 00:00:00", deviceNumber, dataSize, 0f, 180f, -90f, 90f);
        generateRandomData(tsFile, "01/05/2012 00:00:00", deviceNumber, dataSize, 0f, 180f, 0f, 90f);
        generateRandomData(tsFile, "01/07/2013 00:00:00", deviceNumber, dataSize, 0f, 90f, 0f, 90f);
        generateRandomData(tsFile, "01/09/2014 00:00:00", deviceNumber, dataSize, 0f, 40f, 0f, 20f);
        generateRandomData(tsFile, "01/11/2015 00:00:00", deviceNumber, dataSize, 0f, 20f, 0f, 10f);
        generateRandomData(tsFile, "01/13/2016 00:00:00", deviceNumber, dataSize, 0f, 10f, 0f, 5f);
        tsFile.close();
    }

    //total = deviceNumber * dataSize;
    private static void generateRandomData(TsFile tsFile, String sDateTime, int deviceNumber, long dataSize, float minLon, float maxLon, float minLat, float maxLat) throws IOException, WriteProcessException {
            long time = transferDataToLong(sDateTime);
            //0.008983056 0.004491528
            float lonInterval = 1.0f;
            float latInterval =  1.0f;

            for (int i = 1; i <= deviceNumber; i++) {
                String device = "d" + i;
                System.out.println(device);
                long startTime = time;
                float[] start_lon_lat = generateLonLat(minLon, maxLon, minLat, maxLat);
                float lon = start_lon_lat[0];
                float lat = start_lon_lat[1];
                for (long j = 0; j < dataSize; j++) {
                    startTime += 60000;// frequency is 1 minute,60 * 1000 ms
                    int direction = generateDirection();
                    if (direction == 1) {
                        if (lat + latInterval > maxLat) {
                            lat = lat - latInterval;
                        } else {
                            lat = lat + latInterval;
                        }
                    } else if (direction == 2) {
                        if (lat - latInterval < minLat) {
                            lat = lat + latInterval;
                        } else {
                            lat = lat - latInterval;
                        }
                    } else if (direction == 3) {
                        if (lon - lonInterval < minLon) {
                            lon = lon + lonInterval;
                        } else {
                            lon = lon - lonInterval;
                        }
                    } else {
                        if (lon + lonInterval > maxLon) {
                            lon = lon - lonInterval;
                        } else {
                            lon = lon + lonInterval;
                        }
                    }
                    String writeString = device + "," + startTime + ",lon," + lon + ",lat," + lat;
                    tsFile.writeLine(writeString);
                }
            }

    }

    private static long transferDataToLong(String sDateTime) {
        //String sDateTime = "08/31/2006 21:08:00";
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        Date dt2 = null;
        try {
            dt2 = sdf.parse(sDateTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //继续转换得到毫秒数的long型
        return dt2.getTime();
    }


    private static float[] generateLonLat(float minLon, float maxLon, float minLat, float maxLat) {
        float[] result = new float[2];
        result[0] = minLon + (float) (Math.random() * (maxLon - minLon));
        result[1] = minLat + (float) (Math.random() * (maxLat - minLat));
        return result;
    }

    private static int generateDirection(){

        /**
         * 1 up
         * 2 down
         * 3 left
         * 4 right
         */
        return 1 + (int) (Math.random() * 4);
    }

}
