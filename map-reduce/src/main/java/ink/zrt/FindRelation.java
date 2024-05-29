package ink.zrt;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class FindRelation {
    public static class RelationMapper extends Mapper<Object, Text, Text, Text> {
        @Override
        public void map(Object key, Text val, Context context) throws IOException, InterruptedException {
            String[] pair = val.toString().split(",");

            if (pair.length != 2) {
                System.out.println("Warning: pair length!=2");
                return;
            }
            String parent = pair[0], child = pair[1];
            Text parentKey = new Text(parent), childKey = new Text(child);
            Text parentVal = new Text(), childValue = new Text();
            childValue.set(parent + ",parent");
            parentVal.set(childValue + ",child");
            context.write(parentKey, parentVal);
            context.write(childKey, childValue);
        }

    }

    public static class RelationReducer extends Reducer<Text, Text, Text, Text> {

        @Override
        public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
            ArrayList<String> grandparentList = new ArrayList<>(), grandchildList = new ArrayList<>();

            for (Text text : values) {
                String[] pair = text.toString().split(",");
                if (pair.length != 2) {
                    System.out.println("reducer: pair.length!=2");
                    return;
                }
                if (Objects.equals(pair[1], "child")) {
                    grandchildList.add(pair[0]);
                } else {
                    grandparentList.add(pair[0]);
                }
            }

            for (int i = 0; i < grandparentList.size(); i++) {
                for (int j = 0; j < grandparentList.size(); j++) {
                    Text outKey = new Text(grandparentList.get(i)),
                            outVal = new Text(grandchildList.get(j));
                    context.write(outKey, outVal);
                }
            }
        }
    }

    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf, "word count");
        job.setJarByClass(FindRelation.class);
        job.setMapperClass(RelationMapper.class);
//        job.setCombinerClass(IntSumReducer.class);
        job.setReducerClass(RelationReducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);
        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));
        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }

}