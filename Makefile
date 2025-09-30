#!/bin/bash
.DEFAULT_GOAL := dev

.PHONY: dev build tag push all

all: build tag push

dev:
	docker compose up -d --build --force-recreate
build:
	docker compose build --no-cache
tag: build
	docker tag nhentai:latest 192.168.1.236:5000/nhentai:latest
push: tag
	docker compose push