# Clone gdx-teavm for TeaVM/WASM browser builds (tag 1.5.6) and apply GiraffeRun patches.
$ErrorActionPreference = "Stop"
$root = Split-Path $PSScriptRoot -Parent
$target = Join-Path $root "tools\gdx-teavm"
$patch = Join-Path $PSScriptRoot "gdx-teavm-patches\girafferun.patch"

if (-not (Test-Path $target)) {
    New-Item -ItemType Directory -Force -Path (Split-Path $target) | Out-Null
    git clone --depth 1 --branch 1.5.6 https://github.com/xpenatan/gdx-teavm.git $target
    Write-Host "Cloned gdx-teavm to $target"
} else {
    Write-Host "gdx-teavm already present at $target"
}

Push-Location $target
try {
    git apply --check $patch 2>$null
    if ($LASTEXITCODE -eq 0) {
        git apply $patch
        Write-Host "Applied GiraffeRun gdx-teavm patches."
    } else {
        Write-Host "GiraffeRun patches already applied (or gdx-teavm version mismatch)."
    }
} finally {
    Pop-Location
}

Write-Host "TeaVM setup complete. Run: ./gradlew :teavm:buildWasm"
