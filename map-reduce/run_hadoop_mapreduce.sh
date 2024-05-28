echo "Run Mapreduce Job"
echo "$#"
if [[ $# -eq 0 ]]
then
  echo "Need argument; exit;"
  exit 1
fi

case $1 in
avg_by_class)
  echo byclass
  ;;
avg_by_student)
  echo stu
  ;;
relation)
  echo
  ;;
*)
  echo "$1 does not match any pattern"
  exit 1
  ;;
esac
