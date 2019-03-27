#!/usr/bin/env bash
kubectl create configmap web-cm --from-file=config/web/bootstrap.yml
kubectl create configmap crawler-service-cm --from-file=config/crawler-service/bootstrap.yml
kubectl create configmap title-basics-service-cm --from-file=config/title-basics-service/bootstrap.yml
kubectl apply -f title-basics-service-deployment.yaml
kubectl apply -f title-basics-service-lb.yaml
kubectl apply -f crawler-service-deployment.yaml
kubectl apply -f crawler-service-lb.yaml
kubectl apply -f web-deployment.yaml
kubectl apply -f web-lb.yaml