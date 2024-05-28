package ink.zrt;

import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

public class FloatAvgReducer extends Reducer<Text, FloatWritable, Text, FloatWritable> {
    @Override
    public void reduce(Text key, Iterable<FloatWritable> values, Context context) throws IOException, InterruptedException {
        FloatWritable result = new FloatWritable();
        float sum = 0;
        float count = 0;
        for (FloatWritable val : values) {
            sum += val.get();
            count++;
        }
        if (count == 0) {
            System.out.println("abnormal data " + key.toString());
            return;
        }
        result.set(sum / count);
        context.write(key, result);
    }
}