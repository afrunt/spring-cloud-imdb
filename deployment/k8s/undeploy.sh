#!/usr/bin/env bash
kubectl delete -f web-lb.yaml
kubectl delete -f web-deployment.yaml
kubectl delete -f crawler-service-lb.yaml
kubectl delete -f crawler-service-deployment.yaml
kubectl delete -f title-basics-service-lb.yaml
kubectl delete -f title-basics-service-deployment.yaml
kubectl delete configmap web-cm
kubectl delete configmap crawler-service-cm
kubectl delete configmap title-basics-service-cm