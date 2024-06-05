#!/bin/bash

ZIP_FILE="builds.zip"

rm -f $ZIP_FILE

REVISIONS=$(git log -n 4 --pretty=format:"%H")

BUILD_DIR="/builds"
mkdir -p $BUILD_DIR

for REV in $REVISIONS
do
  git checkout $REV
  
  ./gradlew build
  
  JAR_FILE=$(find . -name "*.jar" | head -n 1)
  cp $JAR_FILE $BUILD_DIR/$(basename $JAR_FILE .jar)-$REV.jar
done

cd $BUILD_DIR

zip $ZIP_FILE *.jar

mv $ZIP_FILE ../

cd ..
rm -rf $CLONE_DIR
rm -rf $BUILD_DIR

echo "Сборка завершена. Файл $ZIP_FILE создан."
