# Meeting Notes Summariser

A production-oriented **AI Meeting Notes Summarisation System** built using **Spring Boot**, **Spring AI**, and multiple LLM providers. The project demonstrates how Generative AI can be integrated into a modern Java backend to summarize meeting discussions, extract important decisions and action items, and generate structured meeting summaries.

---

## Overview

This application provides REST APIs for **AI-powered text and meeting-notes summarisation**. It uses Spring AI's `ChatClient` abstraction to communicate with AI providers and also demonstrates direct HTTP-based model integration.

The project supports both **Groq/OpenAI-compatible APIs** and **Hugging Face**, making the architecture flexible for experimenting with different AI models and providers.

---

## Tech Stack

### Backend

* Java 25
* Spring Boot
* Spring AI
* Maven
* REST APIs


### AI / LLM

* Groq / OpenAI-compatible API
* Hugging Face
* Qwen2.5-Coder-7B-Instruct

### AI Capabilities

* Meeting Notes Summarisation
* Text Summarisation
* Structured Summary Generation
* AI Code Generation

---

## Key Features

* AI-powered Meeting Notes Summarisation
* Structured meeting summary generation
* Extracts key discussion points
* Identifies decisions made
* Generates action items
* Generates next steps
* Multiple AI provider support
* Groq/OpenAI-compatible API integration
* Hugging Face integration
* AI-powered code generation
* RESTful API design
* Environment-based API key configuration
* Modular Spring Boot architecture

---

## Project Structure

```text
Meeting_Notes_Summariser/
│
├── src/
│   └── main/
│       ├── java/
│       │   └── com/example/Meeting_Notes_Summariser/
│       │       │
│       │       ├── controller/
│       │       │   ├── AIController.java
│       │       │   └── huggingfaceController.java
│       │       │
│       │       ├── Service/
│       │       │   └── AIService.java
│       │       │
│       │       ├── config/
│       │       │   └── AIProviderConfig.java
│       │       │
│       │       └── MeetingNotesSummariserApplication.java
│       │
│       └── resources/
│           └── application.yaml
│
├── pom.xml
├── mvnw
├── mvnw.cmd
└── README.md
```

---

## System Architecture

```text
                    Client
                      │
                      ▼
               Spring Boot API
                      │
          ┌───────────┼───────────┐
          ▼           ▼           ▼
     Summariser   Meeting Notes   Hugging Face
      Endpoint      Endpoint       Endpoint
          │           │               │
          └───────────┼───────────────┘
                      ▼
                  Spring AI
                      │
             ┌────────┴────────┐
             ▼                 ▼
        Groq / OpenAI     Hugging Face
        Compatible API
             │                 │
             └────────┬────────┘
                      ▼
                AI Response
```

---

## Request Flow

1. Client sends meeting notes or text to the REST API.
2. Spring Boot receives the request through the appropriate controller.
3. The application applies the configured summarisation prompt.
4. Spring AI sends the request to the selected AI provider.
5. The LLM processes the input and generates the response.
6. The generated summary is returned to the client.

---

## API Endpoints

| Method | Endpoint                                       | Description                            |
| ------ | ---------------------------------------------- | -------------------------------------- |
| POST   | `/api/v1/openai/chat/summarizer`               | General text summarisation             |
| POST   | `/api/v1/openai/chat/summarizer-meeting-notes` | Structured meeting summarisation       |
| POST   | `/api/v1/openai/chat/summarizer-with-http`     | Summarisation using direct HTTP client |
| POST   | `/api/huggingface/chat/generate-code`          | AI code generation                     |

---

## Getting Started

### Clone the Repository

```bash
git clone https://github.com/guru340/Meeting_Notes_Summariser.git

cd Meeting_Notes_Summariser
```

---

## Environment Variables

Configure the following environment variables before running the application:

```text
GROQ_API_KEY=your_groq_api_key
GROQ_BASE_URL=your_groq_base_url
GROQ_MODEL=your_groq_model
HUGGINGFACE_API_KEY=your_huggingface_api_key
```

> Never commit API keys or other secrets directly to GitHub.

---

## Running the Application

### Using Maven

```bash
mvn spring-boot:run
```

### Using Maven Wrapper

Linux / macOS:

```bash
./mvnw spring-boot:run
```

Windows:

```bash
mvnw.cmd spring-boot:run
```

The application runs on:

```text
http://localhost:8081
```

---

## API Examples

### 1. General Summarisation

```http
POST /api/v1/openai/chat/summarizer
Content-Type: text/plain
```

Example request:

```bash
curl -X POST http://localhost:8081/api/v1/openai/chat/summarizer \
-H "Content-Type: text/plain" \
--data "The development team discussed project deadlines, database design and deployment strategy."
```

---

### 2. Meeting Notes Summarisation

```http
POST /api/v1/openai/chat/summarizer-meeting-notes
Content-Type: text/plain
```

Example request:

```bash
curl -X POST http://localhost:8081/api/v1/openai/chat/summarizer-meeting-notes \
-H "Content-Type: text/plain" \
--data "The team discussed Q3 sales targets, customer acquisition, conversion rates and assigned new targets to each representative."
```

### Response Format

```text
Meeting Summary:

- Objective:
- Key Discussion Points:
- Decisions Made:
- Action Items:
- Next Steps:
```

---

### 3. Direct HTTP Summarisation

```http
POST /api/v1/openai/chat/summarizer-with-http
Content-Type: text/plain
```

This endpoint demonstrates calling the configured AI provider directly using **Apache HttpClient 5**.

---

### 4. Hugging Face Code Generation

```http
POST /api/huggingface/chat/generate-code
Content-Type: text/plain
```

Example:

```bash
curl -X POST http://localhost:8081/api/huggingface/chat/generate-code \
-H "Content-Type: text/plain" \
--data "Create a Java method to check whether a string is a palindrome."
```

---

## AI Model Configuration

The Hugging Face integration currently uses:

```text
Qwen/Qwen2.5-Coder-7B-Instruct
```

Configuration:

```yaml
huggingface:
  chat:
    options:
      model: Qwen/Qwen2.5-Coder-7B-Instruct
      max-new-tokens: 1024
      temperature: 0.7
```

---

## Meeting Summary Format

The application generates structured meeting summaries containing:

```text
Meeting Summary:

- Objective
- Key Discussion Points
- Decisions Made
- Action Items
- Next Steps
```

The summarisation prompt is designed to:

* Keep the summary concise and easy to understand
* Avoid adding information not present in the meeting notes
* Clearly distinguish discussions, decisions and action items
* Preserve names, dates, numbers and deadlines
* Return `Not specified` when relevant information is unavailable

---

## Build

```bash
mvn clean install
```

Using Maven Wrapper:

```bash
./mvnw clean package
```

---

## Run Tests

```bash
mvn test
```
---

## Author

**Mayank Sangwani**

* GitHub: https://github.com/guru340

---

If you find this project helpful, consider giving it a ⭐ on GitHub.
