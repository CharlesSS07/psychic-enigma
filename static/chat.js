/*
 * drag and drop image from web
 */
function allowDrop(ev) {
    ev.preventDefault();
}

function drag(ev) {
    ev.dataTransfer.setData("text", ev.target.id);
}

function drop(ev) {
    ev.preventDefault();
    var data = ev.dataTransfer.getData("text");
    if (data != '') {
        ev.target.innerHTML += `<img src='${data}'></img><br><a href=${data}>source</a>`;
    }
}


/*
 * drag and drop local file
 */
let dropArea = document.getElementById('m');
['dragenter', 'dragover', 'dragleave', 'drop'].forEach(eventName => {
    document.addEventListener(eventName, preventDefaults);
});

function preventDefaults(e) {
    e.preventDefault();
    e.stopPropagation();
}

document.addEventListener('drop', handleDrop);

function handleDrop(e) {
    let dt = e.dataTransfer;
    let files = dt.files;
    ([...files]).forEach(htmlFile);
}

function htmlFile(file) {
    let reader = new FileReader();
    reader.readAsDataURL(file);
    reader.onload = function (e) {
        imghtml = `<img src='${e.target.result}'/>`;
        document.getElementById('m').innerHTML += imghtml;
    }
}


/*
 * for sanatizing inputs
 */
function encodeHTML(string) {
    return string
        .replace(/&/g, "&amp;")
        .replace(/</g, "&lt;")
        .replace(/>/g, "&gt;")
        .replace(/"/g, "&quot;")
        .replace(/'/g, "&#039;");
}


function brightness(color) {
    var r = parseInt(color.substr(1, 2), 16);
    var g = parseInt(color.substr(3, 2), 16);
    var b = parseInt(color.substr(5, 2), 16);
    return (r + g + b) / (3 * 2.55);
}


/*
 * main runnings
 */
$(function () {

    var name = 'you';
    $('#uname').val(name);

    // append data in message format
    function showmsg(fr, content) {
        var date = new Date();
        var time = date.toString().substring(16, 21);

        var msghtml = `\
      <div class='messagec' data-sender=${fr} data-timesent=${time}>\
        <p class='sendername'><b>${fr}</b> - ${time}</p>\
        <span class='bubble'>\
            <p class='message'>${content}</p>\
        </span>\
      </div>`;
        if (fr == name) { // if it's from me
            // class it as from me
            msghtml = msghtml.replace(/class=\'/g, 'class=\'fromme ');
        }

        var lastmessage = $($('.messagec').get(-1)); // latest message
        try {
            var lasttime = lastmessage.data('timesent');
            var lastsender = lastmessage.data('sender');
        } catch (err) {
            var lasttime = null;
            var lastsender = null;
            console.log('error getting properties of previous message: ' + err);
        }

        if (fr == lastsender) { //if this messages is being sent by the same person as the last one
            $(lastmessage.find('.bubble').get(-1)).append(`<p>${content}</p>`); // add the message to the last one inside the message bubble
            msghtml = ''; // don't send a message too
        }
        // console.log('passed sendercompound');
        $("#messages").append(msghtml); // add it to the message string
    }

    const stompClient = new StompJs.Client({
        brokerURL: `ws://${window.location.host}/chat-websocket`
    });

    stompClient.onWebSocketError = (error) => {
        console.error('Error with websocket', error);
    };

    stompClient.onStompError = (frame) => {
        console.error('Broker reported error: ' + frame.headers['message']);
        console.error('Additional details: ' + frame.body);
    };

    stompClient.onConnect = (frame) => {
        // should un-gray/enable typing box


        // only send notification when tab is not active
        window.isActive = true;
        $(window).focus(function () {
            this.isActive = true;
        });
        $(window).blur(function () {
            this.isActive = false;
        });

        window.addEventListener("unload", function (event) { // when they close the window
            stompClient.publish(
                {
                    destination: '/app/chat-message',
                    body: JSON.stringify({sender: 'Server', content: name + ' left the chat.'}),
                    skipContentLengthHeader: true
                });
        });


        $('#sendform').submit(function () { // on submit of message sender
            message = $('#m').html(); // take the value they entered
            if (message.replace(/<br>/g, '').replace(/&nbsp;/g, '') == '') {
                return false; // deny if it is empty, or only newlines or spaces
            }

            stompClient.publish(
                {
                    destination:'/app/chat-message',
                    body: JSON.stringify({content: message, sender: name}),
                    skipContentLengthHeader: true
                }); // actually sends stuff
            $('#m').html(''); // empty the message box
            return false; // don't reload
        });

        stompClient.subscribe('/topic/chat-message', function (resp) { // when a message is received
            const msg = JSON.parse(resp.body);
            console.log(resp, msg);
            var sender = msg['sender']; // who sent it (who actually sent it)
            var content = msg['content'];
            console.log(sender);

            content = content
                .replaceAll('\\n', '<br>')
                .replaceAll('\n', '<br>'); // content of message

            if (sender==="Brad") { // brads responses should be seen as options the user can click on
                console.log(content);
                // callback = stompClient.publish(
                // {
                //     destination: '/app/chat-message',
                //     body: JSON.stringify({sender: 'Server', content: name + ' left the chat.'}),
                //     skipContentLengthHeader: true
                // });
                content = content.split('<br>').map(i => '<a href="x.com">'+i+'</a>').join('<br>');
                console.log(content);
            }

            showmsg(sender, content); // show it

            document.getElementById('bottom').scrollIntoView(true);
        });


    };

    stompClient.activate();
});


