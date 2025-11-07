$JAVAFX_PATH = Join-Path $PSScriptRoot 'lib\javafx-sdk-21.0.1\lib'

# Ensure output directory exists
New-Item -ItemType Directory -Force -Path (Join-Path $PSScriptRoot 'out\production\Bank') | Out-Null

Push-Location $PSScriptRoot
# Compile all Java sources in the project so newly added DAO/DB files are included.
javac --module-path $JAVAFX_PATH `
      --add-modules javafx.controls,javafx.fxml `
      -d out/production/Bank `
      *.java
Pop-Location

# Copy FXML files to output directory
Copy-Item -Force (Join-Path $PSScriptRoot '*.fxml') (Join-Path $PSScriptRoot 'out\production\Bank')
Copy-Item -Force (Join-Path $PSScriptRoot '*.xml') (Join-Path $PSScriptRoot 'out\production\Bank')

# Also copy resources into the package folder so calls like getClass().getResource("LogInView.fxml")
# (which look relative to package `com.Bank`) can find them at runtime.
$pkgPath = Join-Path $PSScriptRoot 'out\production\Bank\com\Bank'
New-Item -ItemType Directory -Force -Path $pkgPath | Out-Null
Copy-Item -Force (Join-Path $PSScriptRoot '*.fxml') $pkgPath
Copy-Item -Force (Join-Path $PSScriptRoot '*.xml') $pkgPath