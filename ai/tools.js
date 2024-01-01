


import OpenAI from 'openai';

const openai = new OpenAI({
    apiKey: 'my api key', // defaults to process.env["OPENAI_API_KEY"]
});

import { v4 as uuidv4 } from 'uuid';

class MessageDatabaseEntry {
    constructor(content, sender, timestamp = new Date()) {
        this.messageId = uuidv4(); // Using the uuid package to generate a unique ID
        this.content = content;
        this.sender = sender;
        this.timestamp = timestamp;
    }
}

class ConversationDatabaseEntry {

    constructor() {
        this.conversationId = uuidv4();
        this.created_at = Date.now();
        this.messages = [];
    }

    // Add a message to the conversation
    addMessage(message) {
        this.messages.push(message);
    }

    // Get all messages in the conversation
    getMessages() {
        return this.messages;
    }
}

class ConversationDatabase {

    constructor() {
        this.users = {};
    }

    // Add a user to the database
    addUser(userId) {
        if (!this.users[userId]) {
            this.users[userId] = {
                conversations: {},
            };
        }
    }

    newConversation(userId) {
        if (!this.users[userId]) {
            throw new Error(`Invalid userId "${userId}"`);
        }
        const user = this.users[userId];
        let conversation = new ConversationDatabaseEntry();
        user.conversations[conversation.conversationId] = conversation;
        return conversation;
    }

    getConversation(userId, conversationId) {
        if (this.users[userId]) {
            const user = this.users[userId];

            // Find the conversation
            let conversation = user.conversations[conversationId];

            if (conversation) {
                return conversation;
            } else {
                throw new Error(`Invalid conversationId "${conversationId}"`);
            }
        } else {
            throw new Error(`Invalid userId "${userId}"`);
        }
    }
}