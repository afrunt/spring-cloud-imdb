#!/usr/bin/env bash
kubectl delete -f elasticsearch-deployment.yaml
kubectl delete -f elasticsearch-lb.yaml
