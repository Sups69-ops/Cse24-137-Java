$JAVAFX_PATH = Join-Path $PSScriptRoot 'lib\javafx-sdk-21.0.1\lib'

java --module-path $JAVAFX_PATH `
     --add-modules javafx.controls,javafx.fxml `
     -cp "out/production/Bank" `
     com.Bank.LoginApp