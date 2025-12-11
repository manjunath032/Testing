Param()

Write-Host "1/3 - Running Maven build (Windows PowerShell)"
& mvn -B -DskipTests package

Write-Host "2/3 - Building Docker image 'springwsdl:latest'"
docker build -t springwsdl:latest .

Write-Host "Build complete. To run locally: docker run -p 8080:8080 springwsdl:latest"
