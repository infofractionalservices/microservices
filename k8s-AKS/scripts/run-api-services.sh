#!/bin/bash

# Configmap
kubectl create configmap otel-collector-configmap  --from-file monitoring/otel-config.yml

# Monitoring Files
kubectl apply -f monitoring/jaeger-service-deployment.yml
kubectl apply -f monitoring/otel-collector-service-deployment.yml
kubectl apply -f monitoring/microservice-service-monitor.yml

# Applications
kubectl apply -f apps/backend-service-deployment.yml
kubectl apply -f apps/account-service-deployment.yml
kubectl apply -f apps/authentication-service-deployment.yml
kubectl apply -f apps/forex-service-deployment.yml
kubectl apply -f apps/transaction-service-deployment.yml
