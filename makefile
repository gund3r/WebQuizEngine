run:
	./gradlew bootRun

build: clean
	./gradlew build

clean:
	./gradlew clean

lint:
	./gradlew checkstyleMain

.DEFAULT_GOAL := build-run
build-run: build run