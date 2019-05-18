import {environmentService} from "./Environment";

class TitleBasicsService {
    fetchTitleBasicsById(id) {
        return fetch(environmentService.apiServerUrl() + `/api/title-basics/${id}`, {
            method: "GET",
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

    fetchGenresWithTitles() {
        return fetch(environmentService.apiServerUrl() + "/api/title-basics/genres/with-titles", {
            method: "GET",
            mode: "cors",
            "headers": {
                "Content-Type": "application/json"
            }
        })
            .then(response => {
                if (!response.ok) {
                    return [];
                }
                return response.json();
            });
    }

    clear() {
        return fetch(environmentService.apiServerUrl() + "/api/title-basics/clear", {
            method: "DELETE",
            mode: "cors",
            "headers": {
                "Content-Type": "application/json"
            }
        })
            .then(response => response.json());
    }

    fetchSearchResults(searchRequest) {
        let request = searchRequest.genre ? {
            ...searchRequest, ...{
                genres: [searchRequest.genre]
            }
        } : {...searchRequest};


        return fetch(environmentService.apiServerUrl() + "/api/title-basics/search", {
            method: "POST",
            body: JSON.stringify(request),
            mode: "cors",
            "headers": {
                "Content-Type": "application/json"
            }
        })
            .then(response => response.json());
    }
}

const titleBasicsService = new TitleBasicsService();

export {TitleBasicsService, titleBasicsService}
