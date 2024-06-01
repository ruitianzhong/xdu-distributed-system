from pyspark.sql import SparkSession
from pyspark.sql import functions as sf
from pyspark.sql.dataframe import DataFrame
from pyspark.sql.types import FloatType

spark = SparkSession.builder.appName("grade").getOrCreate()

output_name_avg = "avg_grade_by_student_output"
output_range = "range_by_grade_output"

df = spark.read.text("../map-reduce/data/grades.txt")
split_cols = sf.split(df["value"], ",")

table = df.withColumn("class", split_cols.getItem(0)).withColumn("name", split_cols.getItem(1)) \
    .withColumn("curriculum", split_cols.getItem(2)).withColumn("type", split_cols.getItem(3)) \
    .withColumn("grade", split_cols.getItem(4).cast(FloatType())).drop("value")
# It seems that the empty line is ignored
print("[INFO] total record number is " + str(table.count()))

avg_by_student = table.filter(table.type == "必修") \
    .groupBy("name") \
    .avg("grade") \
    .withColumnRenamed("avg(grade)", "average")

print("[INFO] Total student number is: " + str(avg_by_student.count()))

# calculate the number of students in respective grade range
count90_100 = avg_by_student.where(90 <= avg_by_student.average).where(avg_by_student.average <= 100).count()
count80_89 = avg_by_student.where(80 <= avg_by_student.average).where(avg_by_student.average < 90).count()
count70_79 = avg_by_student.where(70 <= avg_by_student.average).where(avg_by_student.average < 80).count()
count60_69 = avg_by_student.where(60 <= avg_by_student.average).where(avg_by_student.average < 70).count()
count_under_60 = avg_by_student.where(avg_by_student.average < 60).count()

# create range result
rangeData = spark.createDataFrame(
    [("90-100", count90_100), ("80-89", count80_89), ("70-79", count70_79), ("60-69", count60_69),
     ("<60", count_under_60), ], schema=["range", "count"])


# store function
def store_result(dir_name: str, dataframe: DataFrame):
    dataframe.show()
    (dataframe
     .write.
     format("csv").
     option("header", "true")
     .option("delimiter", "|")
     .mode("overwrite")
     .save(dir_name))


store_result(output_name_avg, avg_by_student)
store_result(output_range, rangeData)
