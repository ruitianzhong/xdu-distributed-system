package ink.zrt;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

public class AvgGradeByClass {
    public static class ClassSubjectMapper extends Mapper<Object, Text, Text, FloatWritable> {

        @Override
        public void map(Object inKey, Text inValue, Context context) throws IOException, InterruptedException {
            String line = inValue.toString();
            String[] info = line.split(",");
            if (info.length != 5) {
                System.out.println("warning : info length!=5");
                return;
            }
            String newKey = info[0] + "," + info[2];
            float grade = Float.parseFloat(info[4]);

            Text outKey = new Text();
            FloatWritable outValue = new FloatWritable();
            outKey.set(newKey);
            outValue.set(grade);
            context.write(outKey, outValue);
        }
    }


    public static void main(String[] args) throws IOException, InterruptedException, ClassNotFoundException {
        Configuration conf = new Configuration();

        Job job = Job.getInstance(conf, "average_by_class_and_subject");
        job.setJarByClass(AvgGradeByClass.class);
        job.setMapperClass(ClassSubjectMapper.class);
        job.setReducerClass(FloatAvgReducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(FloatWritable.class);

        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));
        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}
