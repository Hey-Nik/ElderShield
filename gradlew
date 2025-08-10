#!/usr/bin/env sh
# Minimal gradlew that uses preinstalled Gradle if wrapper JAR is absent.
DIR="$(cd "$(dirname "$0")" && pwd)"
WRAPPER_JAR="$DIR/gradle/wrapper/gradle-wrapper.jar"
if [ ! -f "$WRAPPER_JAR" ]; then
  echo "Wrapper JAR missing; invoking system gradle to generate wrapper..."
  gradle wrapper --gradle-version 8.7 || exit 1
fi
exec ./gradlew "$@"
