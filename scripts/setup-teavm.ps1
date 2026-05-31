# Clone gdx-teavm for TeaVM/WASM browser builds (tag 1.5.6)
$target = Join-Path $PSScriptRoot "..\tools\gdx-teavm"
if (Test-Path $target) {
    Write-Host "gdx-teavm already present at $target"
    exit 0
}
New-Item -ItemType Directory -Force -Path (Split-Path $target) | Out-Null
git clone --depth 1 --branch 1.5.6 https://github.com/xpenatan/gdx-teavm.git $target
Write-Host "Cloned gdx-teavm to $target"
