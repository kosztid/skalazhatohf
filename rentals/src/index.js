const express = require('express');
const bodyParser = require('body-parser');
const amqp = require('amqplib');
const axios = require('axios');

const app = express();
const PORT = 3000;

// Konstansok
const RABBITMQ_URL = 'amqp://rabbitmq:5672';
const QUEUE_NAME = 'equipment_status';
const STORAGE_SERVICE_URL = 'http://storage-service:8080';

let channel;

app.use(bodyParser.json());

// Alap URL-ek konstansban
const fetchFromStorageService = async (endpoint) => {
    try {
        const response = await axios.get(`${STORAGE_SERVICE_URL}${endpoint}`);
        return response.data;
    } catch (error) {
        console.error(`Error fetching from storage service: ${endpoint}`, error.message);
        throw error;
    }
};

// API végpontok
app.get('/api/equipment', async (req, res) => {
    try {
        const data = await fetchFromStorageService('/api/equipment');
        res.json(data);
    } catch (error) {
        res.status(500).send('Error fetching all equipment.');
    }
});

app.get('/api/equipment/available', async (req, res) => {
    try {
        const data = await fetchFromStorageService('/api/equipment/available');
        res.json(data);
    } catch (error) {
        res.status(500).send('Error fetching available equipment.');
    }
});

app.get('/api/equipment/available/:type', async (req, res) => {
    const { type } = req.params;
    try {
        const data = await fetchFromStorageService(`/api/equipment/available/${type}`);
        res.json(data);
    } catch (error) {
        res.status(500).send(`Error fetching available equipment by type (${type}).`);
    }
});

// RabbitMQ kapcsolat inicializálása
const initRabbitMQ = async () => {
    try {
        const connection = await amqp.connect(RABBITMQ_URL);
        channel = await connection.createChannel();
        await channel.assertQueue(QUEUE_NAME, { durable: true });
        console.log('RabbitMQ kapcsolat sikeresen létrejött.');
    } catch (error) {
        console.error('RabbitMQ kapcsolat hiba:', error.message);
    }
};

// RabbitMQ üzenetküldés
const sendStatusUpdate = async (id, status, res) => {
    const message = { id, status };

    try {
        channel.sendToQueue(QUEUE_NAME, Buffer.from(JSON.stringify(message)), {
            persistent: true,
        });
        console.log('Üzenet RabbitMQ-ba küldve:', message);
        res.send(`Status update for equipment ${id} queued successfully.`);
    } catch (error) {
        console.error('Error sending message to RabbitMQ:', error.message);
        res.status(500).send('Error sending message to RabbitMQ.');
    }
};

// Egyéb végpontok
app.get('/', (req, res) => {
    res.send('Rental Service is running!');
});

app.put('/api/equipment/:id/rent', async (req, res) => {
    const { id } = req.params;
    await sendStatusUpdate(id, 'RENTED', res);
});

app.put('/api/equipment/:id/startRepair', async (req, res) => {
    const { id } = req.params;
    await sendStatusUpdate(id, 'UNDER_REPAIR', res);
});

app.put('/api/equipment/:id/returnAvailable', async (req, res) => {
    const { id } = req.params;
    await sendStatusUpdate(id, 'AVAILABLE', res);
});

app.put('/api/equipment/:id/returnDamaged', async (req, res) => {
    const { id } = req.params;
    await sendStatusUpdate(id, 'DAMAGED', res);
});

// RabbitMQ inicializálás és szerverindítás
initRabbitMQ()
    .then(() => {
        app.listen(PORT, () => {
            console.log(`Rental Service is running on port ${PORT}`);
        });
    })
    .catch((error) => {
        console.error('RabbitMQ inicializálási hiba:', error.message);
    });