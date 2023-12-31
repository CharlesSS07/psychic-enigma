#!/usr/bin/env bash
# conda activate ./envs/main
# perhaps set up env vars
uvicorn --log-level debug --host 0.0.0.0 --port 8000 falcon-server:app --reload
