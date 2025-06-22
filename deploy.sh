#!/bin/bash
echo "🚀 Deploying orders app to WSL fake cloud..."

docker-compose down -v
docker-compose build
docker-compose up -d

echo "✅ Deployment complete!"
docker ps
