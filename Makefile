#!/bin/bash
.DEFAULT_GOAL := up
up:
	docker compose up -d --build --force-recreate