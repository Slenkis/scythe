const protocol = location.protocol
const port = location.port
const host = window.location.hostname

const url = protocol + "//" + host + ":" + port + "/time"

function add() {
    let input = document.getElementById("value-input");

    if (input.value < 1) {
    } else {
        let model = {minutes: input.value};

        fetch(url, {
            method: 'PUT',
            body: JSON.stringify(model),
            headers: {
                'Content-Type': 'application/json'
            }
        }).then(response => {
            window.location.reload(true);
        })
    }
}

function reset() {
    fetch(url, {
        method: 'POST'
    }).then(response => {
        window.location.reload(true);
    })
}