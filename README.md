Написать сценарий для утилиты **Gradle**, реализующий компиляцию, тестирование и упаковку в _jar-архив_ кода проекта из лабораторной работы №3 по дисциплине _"Веб-программирование"_.
1. Каждый этап должен быть выделен в отдельный блок сценария
2. Bсе переменные и константы, используемые в сценарии, должны быть вынесены в отдельный файл параметров
3. `MANIFEST.MF` должен содержать информацию о версии и о запускаемом классе



Cценарий должен реализовывать следующие цели (_targets_):

- `compile` -- компиляция исходных кодов проекта.
-  `build` -- компиляция исходных кодов проекта и их упаковка в исполняемый jar-архив. Компиляцию исходных кодов реализовать посредством вызова цели compile.
-  `clean` -- удаление скомпилированных классов проекта и всех временных файлов (если они есть).
-  `test` -- запуск junit-тестов проекта. Перед запуском тестов необходимо осуществить сборку проекта (цель build).
-  `music` -- воспроизведение музыки по завершению сборки (цель build).
-  `team` -- осуществляет получение из git-репозитория 4 предыдущих ревизий, их сборку (по аналогии с основной) и упаковку получившихся jar-файлов в zip-архив. Сборку реализовать посредством вызова цели build.
