echo "Run Mapreduce Job"
if [[ $# -ne 1 ]]
then
  echo "Need 1 argument; exit;"
  exit 1
fi
avg_by_class_output_dir="avg_by_class_output"
avg_by_student_output_dir="avg_by_student_output"
relation_output_dir="relation_output"

if [ "$1" = "clean" ]
then
  echo "rm output file"
  rm -rf "${relation_output_dir}" "${avg_by_class_output_dir}" "${avg_by_student_output_dir}"
  exit 0
fi
mvn clean
mvn package

JAR_PATH="target/map-reduce-1.0-SNAPSHOT.jar"
DATA_PREFIX="test_data"

case $1 in
avg_by_class)
  hadoop fs -rm -f -r  ${avg_by_class_output_dir}
  hadoop  jar ${JAR_PATH} ink.zrt.AvgGradeByClass ${DATA_PREFIX}/grades.txt ${avg_by_class_output_dir}
  hadoop fs -cat ${avg_by_class_output_dir}/* | tee ${avg_by_class_output_dir}
  ;;
avg_by_student)
  hadoop fs -rm -f -r ${avg_by_student_output_dir}
  hadoop  jar ${JAR_PATH} ink.zrt.AvgGradeByStudent ${DATA_PREFIX}/grades.txt ${avg_by_student_output_dir}
  hadoop fs -cat ${avg_by_student_output_dir}/*  | tee  ${avg_by_student_output_dir}

  ;;
relation)
  hadoop fs -rm -f -r ${relation_output_dir}
  hadoop  jar ${JAR_PATH} ink.zrt.FindRelation ${DATA_PREFIX}/child-parent.txt ${relation_output_dir}
  hadoop fs -cat ${relation_output_dir}/* | tee  ${relation_output_dir}
  ;;
*)
  echo "$1 does not match any pattern"
  exit 1
  ;;
esac
