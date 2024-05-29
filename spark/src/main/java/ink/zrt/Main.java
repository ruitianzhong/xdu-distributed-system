package ink.zrt;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.SparkSession;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");
        String logFile = "./README.md";
        SparkSession spark = SparkSession.builder().appName("grades").getOrCreate();
        Dataset<String> logData = spark.read().textFile(logFile).cache();

    }
}