for file in ./*/*
do
  java -jar ../target/expert-system-1.0-SNAPSHOT-jar-with-dependencies.jar -p "$file"
done