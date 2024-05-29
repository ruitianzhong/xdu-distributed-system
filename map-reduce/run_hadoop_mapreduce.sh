echo "Run Mapreduce Job"
if [[ $# -eq 0 ]]
then
  echo "Need argument; exit;"
  exit 1
fi
mvn clean
mvn package
avg_by_class_output_dir="avg_by_class_output"
avg_by_student_output_dir="avg_by_student_output"
relation_output_dir="relation_output"
JAR_PATH="target/map-reduce-1.0-SNAPSHOT.jar"
case $1 in
avg_by_class)
  rm -rf ${avg_by_class_output_dir}
  hadoop  jar ${JAR_PATH} ink.zrt.AvgGradeByClass ./data/grades.txt ${avg_by_class_output_dir}
  ;;
avg_by_student)
  rm -rf ${avg_by_student_output_dir}
  hadoop  jar ${JAR_PATH} ink.zrt.AvgGradeByStudent ./data/grades.txt ${avg_by_student_output_dir}
  ;;
relation)
  rm -rf ${relation_output_dir}
  hadoop  jar ${JAR_PATH} ink.zrt.FindRelation ./data/child-parent.txt ${relation_output_dir}
  ;;
*)
  echo "$1 does not match any pattern"
  exit 1
  ;;
esac
