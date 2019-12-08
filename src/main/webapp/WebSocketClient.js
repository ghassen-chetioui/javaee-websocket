class WebSocketClient {

    constructor() {
        this.ws = new WebSocket(`ws://localhost:8080/javaee-websocket/job-notifications`);
        this.ws.onopen = () => console.log("connection opened");
        this.ws.onerror = () => console.log("connection error");
        this.ws.onclose = () => console.log("connection closed");
    }

    onMessage(fn) {
        this.ws.onmessage = fn;
    }

}