angular.module('requestAccess').service('environmentManagementService', ['$http', function environmentManagementService($http) {

    /**
     * Makes a request to the REST API to get the list of all environments.
     *
     * @returns {Promise} A promise which will resolve with the list of environments.
     */
    this.getEnvironments = function() {
        return $http.get("api/session/ext/request-access/manage/environments");
    };

    /**
     * Makes a request to the REST API to add a new environment.
     *
     * @param {Object} environment The environment to add.
     * @returns {Promise} A promise which will resolve when the environment is added.
     */
    this.addEnvironment = function(environment) {
        return $http.post("api/session/ext/request-access/manage/environments", environment);
    };

    /**
     * Makes a request to the REST API to save an existing environment.
     *
     * @param {Object} environment The environment to save.
     * @returns {Promise} A promise which will resolve when the environment is saved.
     */
    this.saveEnvironment = function(environment) {
        return $http.put("api/session/ext/request-access/manage/environments/" + environment.environmentId, environment);
    };

    /**
     * Makes a request to the REST API to delete an environment.
     *
     * @param {Number} environmentId The ID of the environment to delete.
     * @returns {Promise} A promise which will resolve when the environment is deleted.
     */
    this.deleteEnvironment = function(environmentId) {
        return $http.delete("api/session/ext/request-access/manage/environments/" + environmentId);
    };

}]);
