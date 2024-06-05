
#!/bin/bash

ZIP_FILE="builds.zip"

# Функция для обработки ошибок
check_error() {
  if [ $? -ne 0 ]; then
    echo "Ошибка: $1"
    exit 1
  fi
}

echo "Удаление старого архива, если существует."
rm -f $ZIP_FILE

echo "Получение последних 4 ревизий."
REVISIONS=$(git log -n 4 --pretty=format:"%H")
check_error "не удалось получить ревизии."
echo "Ревизии: $REVISIONS"

BUILD_DIR="builds"  # Изменено на относительный путь
echo "Создание директории $BUILD_DIR."
mkdir -p $BUILD_DIR
check_error "не удалось создать директорию $BUILD_DIR."

for REV in $REVISIONS
do
  echo "Проверка на ревизию $REV."
  git checkout $REV
  check_error "не удалось переключиться на ревизию $REV."

  echo "Удаление старой директории build/libs."
  rm -r build/libs

  echo "Сборка проекта с помощью Gradle."
  ./gradlew build
  if [ $? -ne 0 ]; then
    echo "сборка проекта завершилась неудачей."
    continue
  fi

  echo "Поиск WAR файла."
  WAR_FILE=$(find . -name "*.war" | head -n 1)
  if [ -z "$WAR_FILE" ]; then
    echo "Ошибка: не найдено WAR файл."
    continue 
  fi

  echo "Копирование $WAR_FILE в $BUILD_DIR."
  cp $WAR_FILE $BUILD_DIR/
  check_error "не удалось скопировать файл $WAR_FILE в $BUILD_DIR."
done

echo "Перемещение в директорию $BUILD_DIR."
cd $BUILD_DIR
check_error "не удалось перейти в директорию $BUILD_DIR."

echo "Создание архива $ZIP_FILE."
zip $ZIP_FILE *.war
check_error "не удалось создать архив $ZIP_FILE."

echo "Перемещение архива $ZIP_FILE в родительскую директорию."
mv $ZIP_FILE ../
check_error "не удалось переместить архив $ZIP_FILE."

echo "Возвращение в исходную директорию."
cd ..
check_error "не удалось вернуться в исходную директорию."

echo "Удаление директории $BUILD_DIR."
rm -rf $BUILD_DIR
check_error "не удалось удалить директорию $BUILD_DIR."

echo "Сборка завершена. Файл $ZIP_FILE создан."

echo "Возвращение на ветку master."
git checkout master
check_error "не удалось вернуться на ветку master."

