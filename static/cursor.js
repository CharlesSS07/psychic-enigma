
class Caret {
    constructor(textInputElement) {
        this.position = 0;
        this.textInputElement = textInputElement;
        this.caretElement = this.caretElement = $(
            '<div class="engima-cursor" style="display:block; width:0; height:0; margin:0;">' +
                '<svg class="engima-cursor-svg" width="100px" height="100px" xmlns="http://www.w3.org/2000/svg"' +
                'style="position:relative; left:-50px; top:-50px; z-index: 999999999;">' +
                    '<circle cx="50" cy="50" r="40" fill="green" />' +
                '</svg>' +
            '</div>'
        );
    }

    setCaretPosition(position) {
        this.position = position;
    }

    getCaretPosition() {
        return this.position;
    }

    getCaretElement() {
        return this.caretElement;
    }
}

class TextEditor {
    constructor(textInputElement) {
        this.caret = new Caret(textInputElement);
        this.textInputElement = textInputElement;
    }

    getCaret() {
        return this.caret;
    }

    typeText(text) {
        // very naive, missing all animation
        const pos = this.getCaret().getCaretPosition();
        const old = this.textInputElement.html();
        this.textInputElement.html(old.slice(0, pos) + text + old.slice(pos));
    }

    highlight(from, to) {
        // put a div around it with a special class to highlight
    }
}

// we can't get the location of individual characters within the text area, so this
// wraps every character with a div that will have an even attacked so we can tell
// where the cursor is dropped.
function wrapCharacters(htmlElement) {
    for (const e of htmlElement.childNodes) {
        console.log(typeof(e))
        if (e.nodeType === Node.TEXT_NODE) {
            e.textContent.split('');
        } else if (e.nodeType === Node.ELEMENT_NODE) {

        }
    }
}

function addCursor(offset) {

    const editor = $('.jqte_editor');

    editor.on('input', function() {
        editor.prime('.character').remove();
    });

    var svgElement = $(
        '<svg class="engima-cursor-svg" width="100%" height="100%" xmlns="http://www.w3.org/2000/svg" ' +
        'style="position: absolute; left: 0; top: 0; z-index: 999999999; pointer-events: none;">' +
        '<circle cx="50" cy="50" r="40" fill="green" />' +
        '</svg>'
    );

    // $('body').append(svgElement);

    var cursorElement = $(
        '<div class="engima-cursor" style="display:block; width:0; height:0; margin:0;">' +
            '<svg class="engima-cursor-svg" width="100px" height="100px" xmlns="http://www.w3.org/2000/svg"' +
                'style="position:relative; left:-50px; top:-50px; z-index: 999999999;">' +
                '<circle cx="50" cy="50" r="40" fill="green" />' +
            '</svg>' +
        '</div>'
    );

    $('div[contenteditable=true]').prepend(cursorElement);
}

$(function() {
    addCursor();
});