#!/bin/bash
export DOCKER_BUILDKIT=0

.DEFAULT_GOAL := dev

.PHONY: dev build tag push webui all


all: build push

dev:
	docker compose up -d --build --force-recreate
build:
	docker compose build --no-cache
push: build
	docker compose push
webui:
	docker compose build webui