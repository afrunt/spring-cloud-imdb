#!/usr/bin/env bash
kubectl delete -f kibana-lb.yaml
kubectl delete -f kibana-rs.yaml
kubectl delete -f elasticsearch-ss.yaml
kubectl delete -f elasticsearch-service.yaml
