#!/usr/bin/env bash
# Clone gdx-teavm for TeaVM/WASM browser builds (tag 1.5.6) and apply GiraffeRun patches.
set -euo pipefail
ROOT="$(cd "$(dirname "$0")/.." && pwd)"
TARGET="$ROOT/tools/gdx-teavm"
PATCH="$ROOT/scripts/gdx-teavm-patches/girafferun.patch"

if [[ ! -d "$TARGET" ]]; then
  mkdir -p "$(dirname "$TARGET")"
  git clone --depth 1 --branch 1.5.6 https://github.com/xpenatan/gdx-teavm.git "$TARGET"
  echo "Cloned gdx-teavm to $TARGET"
else
  echo "gdx-teavm already present at $TARGET"
fi

if git -C "$TARGET" apply --check "$PATCH" 2>/dev/null; then
  git -C "$TARGET" apply "$PATCH"
  echo "Applied GiraffeRun gdx-teavm patches."
else
  echo "GiraffeRun patches already applied (or gdx-teavm version mismatch)."
fi

echo "TeaVM setup complete. Run: ./gradlew :teavm:buildWasm"
