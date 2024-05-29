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


public class AvgGradeByStudent {
    // key in, value in, key out, value out
    public static class StudentMapper extends Mapper<Object, Text, Text, FloatWritable> {
        @Override
        public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
            String line = value.toString();
            String[] info = line.split(",");
            if (info.length != 5) {
                System.out.println("Warning info.length: " + info.length);
                return;
            }
            if (!info[3].equals("必修")) {
                return;
            }
            Text outKey = new Text();
            FloatWritable f = new FloatWritable();
            outKey.set(info[1]);
            float grade = Float.parseFloat(info[4]);
            f.set(grade);
            context.write(outKey, f);
        }
    }


    public static void main(String[] args) throws IOException, InterruptedException, ClassNotFoundException {
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf, "average_by_student");
        job.setJarByClass(AvgGradeByStudent.class);
        job.setMapperClass(StudentMapper.class);
        job.setReducerClass(FloatAvgReducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(FloatWritable.class);

        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));
        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}
