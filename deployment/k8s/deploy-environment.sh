#!/usr/bin/env bash
kubectl apply -f elasticsearch-ss.yaml
kubectl apply -f elasticsearch-service.yaml
kubectl apply -f kibana-rs.yaml
kubectl apply -f kibana-lb.yaml