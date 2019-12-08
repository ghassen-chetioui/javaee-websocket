const template = document.createElement('template')
template.innerHTML = `
    <button>Launch job</button>
    <job-viewer></job-viewer>
`

class Application extends HTMLElement {

    constructor() {
        super();
        this._shadow = this.attachShadow({ mode: 'open' });
        this._shadow.appendChild(template.content.cloneNode(true));
        this._jobViewer = this._shadow.querySelector("job-viewer");
        this._shadow.querySelector("button").addEventListener("click", () => this.launchJob());
        const ws = new WebSocketClient();
        ws.onMessage((message) => this._jobViewer.completeJob(message.data));

    }

    connectedCallback() {
    }

    async launchJob() {
        const response = await fetch("/javaee-websocket/resources/jobs", { method: 'POST', headers: new Headers() });
        const jobId = await response.text();
        console.log(`Job launched : ${jobId}`);
        this._jobViewer.addJob(jobId);
    }

}

customElements.define("notif-application", Application);