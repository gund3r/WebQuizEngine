run:
	./gradlew bootRun

build: clean
	./gradlew build

clean:
	./gradlew clean

lint:
	./gradlew checkstyleMain

test:
	./gradlew test

.DEFAULT_GOAL := build-run
build-run: build run