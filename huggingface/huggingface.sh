#!/bin/bash

# Hugging Face Shared Inference API
# https://huggingface.co/models?inference_provider=hf-inference

PROMPT="summarize: In today's Q3 planning meeting, the team discussed improving both technical reliability and customer experience."

# Build JSON payload using jq
JSON_PAYLOAD=$(jq -n --arg inputs "$PROMPT" '{
  inputs: $inputs,
  parameters: {
    max_new_tokens: 20,
    temperature: 0.7
  },
  options: {
    wait_for_model: true
  }
}')

echo "Request payload:"
echo "$JSON_PAYLOAD" | jq .

# Hugging Face model
MODEL="facebook/bart-large-cnn"

# Call Hugging Face Shared Inference API
curl "https://router.huggingface.co/hf-inference/models/$MODEL" \
  -H "Authorization: Bearer $HUGGINGFACE_API_KEY" \
  -H "Content-Type: application/json" \
  -d "$JSON_PAYLOAD"