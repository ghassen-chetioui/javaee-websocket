const template1 = document.createElement('template')
template1.innerHTML = `
    <ul id="jobs"></ul>
`

class JobViewer extends HTMLElement {

    constructor() {
        super();
        this._shadow = this.attachShadow({ mode: 'open' });
        this._shadow.appendChild(template1.content.cloneNode(true));
        this._jobs = this._shadow.querySelector('#jobs');
        this.jobs = [];
    }

    connectedCallback() {
        this.render();
    }

    addJob(jobId) {
        this.jobs.push({ id: jobId, status: 'IN_PROGRESS' });
        this.render();
    }

    completeJob(jobId) {
        console.log(`Job ${jobId} is completed`)
        const job = this.jobs.find(job => job.id === jobId);
        job.status = 'COMPLETED';
        this.render();
    }

    render() {
        this._jobs.innerHTML = ``
        this.jobs.forEach(job => {
            const elt = document.createElement('li');
            elt.innerHTML = `${job.id}: ${job.status}`;
            this._jobs.appendChild(elt);
        })
    }

}

customElements.define("job-viewer", JobViewer);