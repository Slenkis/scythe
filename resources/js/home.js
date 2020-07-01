const protocol = location.protocol
const port = location.port
const host = window.location.hostname

const url = protocol + "//" + host + ":" + port + "/api/get"

fetch(url)
    .then(response => {
        return response.json();
    })
    .then(response => {
        let minutes = response.minutes
        let hours = response.hours

        let div = document.createElement('div');
        div.className = "info"
        div.innerHTML = "<p>" + hours + " ч</p>"
            + "<p>" + minutes + " мин</p>";

        document.body.append(div);
    })