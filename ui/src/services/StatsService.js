import {environmentService} from "./Environment";

class StatsService {

    fetchStats(serviceId) {
        return fetch(environmentService.apiServerUrl() + `/api/${serviceId}/stats`, {
            method: "GET",
            mode: "cors",
            "headers": {
                "Content-Type": "application/json"
            }
        })
            .then(response => {
                if (!response.ok) {
                    return {
                        available: false
                    }
                }
                return response.json();
            });
    }
}

const statsService = new StatsService();

export {statsService, StatsService}
