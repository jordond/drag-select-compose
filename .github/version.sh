#!/bin/bash

CWD="$(cd "$(dirname "${BASH_SOURCE[0]}")" >/dev/null 2>&1 && pwd)"
PARENT="$(cd "$CWD"/.. >/dev/null 2>&1 && pwd)"

set -e

SEMVER_REG="([[:digit:]]+(\.[[:digit:]]+)+)"

NEW_VERSION="$ORG_GRADLE_PROJECT_VERSION_NAME"
if [ -z "$NEW_VERSION" ]; then
  NEW_VERSION="$1"
  if [ -z "$NEW_VERSION" ]; then
    echo "ORG_GRADLE_PROJECT_VERSION_NAME is not set and no version was specified!"
    printf "Usage:\n\t./version.sh <new-version>"
    exit 1
  fi
fi

echo "Update README with version: '$NEW_VERSION'"

README_FILE="$PARENT/README.md"
VERSION_FILE="$PARENT/gradle/libs.versions.toml"

# Update Kotlin badge in README.md
LIBS_KOTLIN_VERSION=$(grep "kotlin = " "$VERSION_FILE" | cut -d= -f2 | tr -d ' "')
echo "Updating Kotlin version: '$LIBS_KOTLIN_VERSION'"
sed -i '' -E "s/kotlin-v$SEMVER_REG/kotlin-v$LIBS_KOTLIN_VERSION/" "$README_FILE"

# Update artifact versions in README.md
sed -i '' -E "s/\:$SEMVER_REG\"\)/\:$NEW_VERSION\"\)/" "$README_FILE"

# Update version catalog in README.md
sed -i '' -E "s/dragselectcompose = \"$SEMVER_REG\"/dragselectcompose = \"$NEW_VERSION\"/" "$README_FILE"
