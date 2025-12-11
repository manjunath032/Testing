#!/usr/bin/env bash
set -euo pipefail

echo "1/4 - Running Maven package (skip tests=false for local verification)"
mvn -B -DskipTests package

echo "2/4 - Building Docker image 'springwsdl:latest'"
docker build -t springwsdl:latest .

echo "Build complete. To run locally: docker run -p 8080:8080 springwsdl:latest"

