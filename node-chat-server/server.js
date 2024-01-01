var express = require('express');
var app = express();
var http = require('http').Server(app);
var io = require('socket.io')(http);
var path = require("path");
// var express = requrire('express');

__dirname = path.resolve(path.dirname('')) + '/../static';

app.use(express.static(__dirname + '/'));

app.get('/', function (req, res) {
    res.sendFile(__dirname + '/index.html');
    // res.sendFile(__dirname + '/style.css');
    // res.sendFile(__dirname + '/folder/');
});

io.on('connection', function (socket) {
    socket.on('chat message', function (msg) {
        console.log(msg)
        io.emit('chat message', msg);
    });

    socket.on('leave', function(otherName) {
        io.emit('leave', otherName);
    });
});

http.listen(3000, function () {
    console.log('listening on *:3000');
});

