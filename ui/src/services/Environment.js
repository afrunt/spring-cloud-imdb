class EnvironmentService {
    apiServerUrl() {
        //return "http://localhost:7777";
        return "";
    }
}

const environmentService = new EnvironmentService();

export {EnvironmentService, environmentService};
