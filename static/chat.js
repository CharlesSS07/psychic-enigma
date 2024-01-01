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

// function coloradd(color1, color2, w1, w2) {
//     var r1=parseInt(color1.substr(1,2),16);
//     var g1=parseInt(color1.substr(3,2),16);
//     var b1=parseInt(color1.substr(5,2),16);

//     var r2=parseInt(color2.substr(1,2),16);
//     var g2=parseInt(color2.substr(3,2),16);
//     var b2=parseInt(color2.substr(5,2),16);


//     return '#'+
//        ("0" + Math.min(255,Math.max(0,Math.floor(w1*r1+w2*r2))).toString(16)).slice(-2)+
//        ("0" + Math.min(255,Math.max(0,Math.floor(w1*g1+w2*g2))).toString(16)).slice(-2)+
//        ("0" + Math.min(255,Math.max(0,Math.floor(w1*b1+w2*b2))).toString(16)).slice(-2);


//     // return '#' + (w1*r1+w2*r2).toString(16) + (w1*g1+w2*g2).toString(16) + (w1*b1+w2*b2).toString(16);
// }


// var value_background = document.documentElement.style.getPropertyValue('--background');

// value_background.addEventListener("change", function() {
//   if (this.value.startsWith('#')){ // if it is a hex code
//     if (this.value.length == 4) {
//       this.value = '#' + this.value.substr(1,2) + this.value.substr(3,2) + this.value.substr(5,2); // convert 3-hex to 6-hex
//     }

//     // change text color value based on brightness
//     if (brightness(this.value) <= 50) {
//       document.documentElement.style.setProperty('--text-clr', '#EEEEEE');
//     } else {
//       document.documentElement.style.setProperty('--text-clr', '#111111');
//     }
//   }
// });


/*
 * main runnings
 */
$(function () {
    var socket = io();
    var name = 'you';
    // var name = document.cookie.replace(/(?:(?:^|.*;\s*)strifename\s*\=\s*([^;]*).*$)|^.*$/, "$1");
    // if (name != ""){
    $('#uname').val(name);
    // } else {name="silentobserver";}
    /**
     $('#login').submit(function () {
     document.documentElement.style.setProperty('--background', $('#background').val());
     document.documentElement.style.setProperty('--accent', $('#accent').val());
     document.documentElement.style.setProperty('--msg-background', $('#msg-background').val());
     document.documentElement.style.setProperty('--ui-size', $('#ui-size').val()+'px');

     document.documentElement.style.setProperty('--text-clr', (brightness($('#background').val()) < 33) ? '#EEEEEE' : '#111111');
     document.documentElement.style.setProperty('--accent-text-clr', (brightness($('#accent').val()) < 33) ? '#EEEEEE' : '#111111');
     document.documentElement.style.setProperty('--msg-text-clr', (brightness($('#msg-background').val()) < 33) ? '#EEEEEE' : '#111111');

     // if (brightness($('#background').val()) <= 50) {
     //   document.documentElement.style.setProperty('--text-clr', '#EEEEEE');
     // } else {
     //   document.documentElement.style.setProperty('--text-clr', '#111111');
     // }

     // if (brightness($('#accent').val()) <= 50) {
     //   document.documentElement.style.setProperty('--accent-text-clr', '#EEEEEE');
     // } else {
     //   document.documentElement.style.setProperty('--accent-text-clr', '#111111');
     // }

     // if (brightness($('#msg-background').val()) <= 50) {
     //   document.documentElement.style.setProperty('--msg-text-clr', '#EEEEEE');
     // } else {
     //   document.documentElement.style.setProperty('--msg-text-clr', '#111111');
     // }

     var sname = $('#uname').val(); // submitted name
     // sname = sname.replace(/ /g, '-');
     if (sname.length >= 50) { // too long
     alert('Username is too long.');
     }
     else if (sname == '' || sname == null) { // empty or null
     // do nothing, which won't let them in
     }
     else if (sname == 'Server') {
     alert('Server is not allowed as a username');
     }
     else { // name checks out
     name = sname; // make official name submitted name
     document.getElementById('m').disabled = false; // enable the message box
     document.getElementById('loginbg').style.display = 'none'; // hide login
     socket.emit('chat message', { sender: 'Server', content: name+' joined the chat.' }); // send joining message
     document.cookie = "strifename="+sname;
     $('#m').focus(); // select message bar
     }
     return false; // don't reload on submit
     }); **/

    // only send notification when tab is not active
    window.isActive = true;
    $(window).focus(function () {
        this.isActive = true;
    });
    $(window).blur(function () {
        this.isActive = false;
    });

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


    window.addEventListener("unload", function (event) { // when they close the window
        socket.emit('chat message', {sender: 'Server', content: name + ' left the chat.'});
    });


    $('#sendform').submit(function () { // on submit of message sender
        message = $('#m').html(); // take the value they entered
        if (message.replace(/<br>/g, '').replace(/&nbsp;/g, '') == '') {
            return false; // deny if it is empty, or only newlines or spaces
        }

        socket.emit('chat message', {content: message, sender: name}); // actually sends stuff
        $('#m').html(''); // empty the message box
        return false; // don't reload
    });

    socket.on('chat message', function (msg) { // when a message is received
        var date = new Date(); // get the date and time
        var time = date.toString().substring(16, 24); //extract time
        var sender = msg['sender']; // who sent it (who actually sent it)
        var content = msg['content']; // content of message
        var ffrom = sender; // who its ffrom (the display name of who sent it, or Server)
        var id;

        showmsg(ffrom, content); // show it

        /*
         * @-ing functionality
         * a message containing @{yourname} (or @all)
         * will notify you if you are not on the page.
         */
        if (content.indexOf('@' + name) != -1 || content.indexOf('@all') != -1) {
            if (window.isActive == false) {
                /* notifications */
                if (Notification.permission === "granted") {
                    var notification = new Notification(ffrom + ': ' + content);
                } else {
                    Notification.requestPermission();
                }
            }
        }
		
        /*
         * commands and stuff
         */
        if (ffrom != 'Server') { // if the message is from a user
            if (content.startsWith('/')) { // commands
                var words = content.split(' '); // array of every word in the message
                var command = words[0]; // first word (everything before the first space)
                var args = content.slice(command.length + 1).split(', '); //args are everything except the first word, split by ', '
                console.log(`Words: ${words}\nCommand: ${command}\nArgs: ${args}`);

                switch (command) {
                    case '/help': // print out possible commands
                        showmsg('Server', '\
                            /help - Show commands.<br>\
                            /alert - Tell everyone something in a pop up.<br>\
                            /ask - Ask everyone a question, and get their answers.<br>\
                            /school - get time till the end of the period.<br>\
                            /period - get the time till the end of the current period.<br>\
                        ');
                        break;

                    case '/alert':
                        alert(`${ffrom}:\n` + args[0]);
                        break;

                    case '/ask': //prompt/query everybody
                        var question = content.replace('/ask', ''); // remove /ask (get the question only)

                        // var id = 0; // just a number id

                        // while (document.getElementById(id)) { // while a question with the current id already exists
                        // 	id++; // add one to the id
                        // }

                        showmsg('Server', `${ffrom} asks:<br>"${question}"<div id="">responses:</div>`); // add the question to the messages

                        var userInput = prompt(`${ffrom} asks: ${question}`); // prompt users for input

                        if (userInput != null && userInput != '' && userInput) {
                            socket.emit('chat message', {content: userInput, sender: name});
                        }
                        break;
                }
            }
        }
        document.getElementById('bottom').scrollIntoView(true);
    });
});


