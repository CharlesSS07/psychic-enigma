# psychic-enigma
The AI text editor from the future.

## Folder Structure
The individual functions of this app were split into several folders to make the overall task simpler. Java is good for websockets, so the chat-server uses Java. Lots of AI functionality is best used via python, so that's where that went. The static folder holds the js, css, and html necessary for all client side functionality. The databases folder represents a common data base where all individual parts of the application deposit and read the respective data. To make running this beast, and all of its subservices easier, the `run.sh` file runs all the necessary executables, does checks, and starts the various demons (databases, chat-server, etc.).


