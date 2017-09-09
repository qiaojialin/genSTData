package cn.edu.thu.dataset;

/**
 * Created by lina on 2017/1/5.
 */

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ReadAndWriteCSV {

    public static void main(String[] args) {
        String sourcePath = "E:/data/gw20150527.csv";
        String regex = ",";
        String dataFormattedPath = "C:/Users/lina/Desktop/data/dataFormatted.csv";
        //formattedSourceFile(sourcePath, regex, dataFormattedPath);

        int num = 100;
        String savePath = "C:/Users/lina/Desktop/data/test";
        readDiffNumLinesFromFormattedFile(num, dataFormattedPath, savePath);
    }

    public static void formattedSourceFile(String sourcePath , String regex, String dataFormattedPath) {
        BufferedReader reader = null;
        BufferedWriter writer= null;

        try {
            reader = new BufferedReader(new FileReader(sourcePath));
            writer = new BufferedWriter(new FileWriter(dataFormattedPath));

            reader.readLine();// the first line
            String line = null;
            int i = 0;
            while ((line = reader.readLine()) != null) {
                i++;
                if(i % 1000000 == 0){
                    System.out.println("the num of data lines have read is : " + i);
                }
                String item[] = line.split(regex);//CSV格式文件为逗号分隔符文件，这里根据逗号切分
                //System.out.println(line);
                //System.out.println(item[0] + "," + item[2]+ "," + item[4] + item[3] + "," );
                String device = item[0].replace("\"","");

                String time = item[2];
                time = time.replace('T', ' ');
                time = time.replace('Z', '.');
                time = time.replaceAll(time, time + "000");
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");//设置日期格式
                Date date;
                long timestamp = 0;
                try {
                    date = df.parse(time);
                    timestamp = date.getTime();
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                String latStr = item[3];
                latStr = latStr.substring(2,10);
                float latitude = Float.valueOf(latStr)/1000000;
                String lonStr = item[4];
                lonStr = lonStr.substring(2,10);
                float longitude = Float.valueOf(lonStr)/100000;
                //System.out.println(device + "," + timestamp + "," +  longitude + "," + latitude);

                // 写入处理之后的数据
                String wirteString = device + "," + timestamp + "," +  longitude + "," + latitude;
                writer.write(wirteString + "\n");

                
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                reader.close();

                writer.flush();
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void readDiffNumLinesFromFormattedFile(int num, String readPath, String savePath){
        BufferedReader reader = null;
        BufferedWriter writer= null;

        try {
            reader = new BufferedReader(new FileReader(readPath));
            writer = new BufferedWriter(new FileWriter(savePath + num + ".csv"));

            String line = null;
            while ((line = reader.readLine()) != null && num > 0) {
                num--;
                writer.write(line+ "\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                reader.close();

                writer.flush();
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    
    
}
