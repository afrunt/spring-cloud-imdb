#!/usr/bin/env bash
kubectl apply -f elasticsearch-deployment.yaml
kubectl apply -f elasticsearch-lb.yaml