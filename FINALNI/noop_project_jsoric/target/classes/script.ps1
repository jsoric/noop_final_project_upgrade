# Use the folder where the script is located
$sourcePath = $PSScriptRoot

# Output file in the same folder
$outputFile = Join-Path $PSScriptRoot "merged_java_files.txt"

# Remove output file if it exists
Remove-Item $outputFile -ErrorAction SilentlyContinue

# Recursively merge all .java files
Get-ChildItem -Path $sourcePath -Recurse -Filter *.java |
    Sort-Object FullName |
    ForEach-Object {

        Add-Content $outputFile "========================================="
        Add-Content $outputFile "FILE: $($_.FullName)"
        Add-Content $outputFile "========================================="
        Add-Content $outputFile ""

        Get-Content $_.FullName | Add-Content $outputFile
        Add-Content $outputFile "`n`n"
    }

Write-Host "Done. Script folder and all child folders processed."