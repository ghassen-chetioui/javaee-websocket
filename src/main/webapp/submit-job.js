class SubmitJob extends HTMLElement {

    connectedCallback() {
        this.innerHTML = `<button>submit a job</button>`;
        const button = this.querySelector("button");
        button.addEventListener("click", this.handleClick);
    }

    async handleClick() {
        const response = await fetch("/javaee-websocket/resources/jobs", { method: 'POST', headers: new Headers() });
        const jobId = await response.text();

        console.log(`Job id : ${jobId}`);

        const ws = new WebSocket(`ws://localhost:8080/javaee-websocket/job-notifications/${jobId}`)
        ws.onmessage = (message) => console.log(message.data);
        ws.onopen = () => console.log("connection opened");
        ws.onerror = () => console.log("connection error");
        ws.onclose = () => console.log("connection closed");
    }

}

customElements.define("submit-job", SubmitJob)