package cn.edu.thu.dataset;

import cn.edu.tsinghua.tsfile.timeseries.basis.TsFile;
import cn.edu.tsinghua.tsfile.timeseries.read.LocalFileInput;
import cn.edu.tsinghua.tsfile.timeseries.read.qp.Path;
import cn.edu.tsinghua.tsfile.timeseries.read.query.QueryDataSet;

import java.io.IOException;
import java.util.ArrayList;

public class ReadTsFile {
    public static void main(String[] args) throws IOException {
        if(args.length<1) {
            System.out.println("filepath selectpath1 selectpath2 ...");
        }
        String file = args[0];
        ArrayList<Path> paths = new ArrayList<>();
        for(int i = 1; i < args.length; i++) {
            paths.add(new Path(args[i]));
        }
        // read example : no filter
        LocalFileInput input = new LocalFileInput(file);
        TsFile readTsFile = new TsFile(input);

        QueryDataSet queryDataSet = readTsFile.query(paths, null, null);
        int i = 0;
        while (queryDataSet.hasNextRecord()) {
            i++;
            if(i > 10) {
                return;
            }
            System.out.println(queryDataSet.getNextRecord());
        }
    }
}
