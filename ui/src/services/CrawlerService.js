import {environmentService} from "./Environment";

class CrawlerService {
    start() {
        return fetch(environmentService.apiServerUrl() + `/api/crawler-service/start`, {
            method: "POST",
            mode: "cors",
            "headers": {
                "Content-Type": "application/json"
            }
        })
            .then(response => {
                if (!response.ok) {
                    throw Error(response.statusText);
                }
                return response.json();
            });
    }

    stop() {
        return fetch(environmentService.apiServerUrl() + `/api/crawler-service/stop`, {
            method: "POST",
            mode: "cors",
            "headers": {
                "Content-Type": "application/json"
            }
        })
            .then(response => {
                if (!response.ok) {
                    throw Error(response.statusText);
                }
                return response.json();
            });
    }
}

const crawlerService = new CrawlerService();

export {crawlerService, CrawlerService};
