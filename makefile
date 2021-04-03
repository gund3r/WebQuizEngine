run:
	./gradlew bootRun

build: clear
	./gradlew build

clear:
	./gradlew clean

.DEFAULT_GOAL := build-run
build-run: build run