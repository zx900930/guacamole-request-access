angular.module('requestAccess').service('requestService', ['$http', function requestService($http) {

    /**
     * Makes a request to the REST API to get the list of available environments.
     *
     * @returns {Promise} A promise which will resolve with the list of environments.
     */
    this.getEnvironments = function() {
        return $http.get("api/session/ext/request-access/environments");
    };

    /**
     * Makes a request to the REST API to get the list of past reasons for the current user.
     *
     * @param {String} currentValue The current value of the reason field.
     * @returns {Promise} A promise which will resolve with a list of past reasons.
     */
    this.getPastReasons = function(currentValue) {
        return $http.get("api/session/ext/request-access/reasons", { params: { contains: currentValue } });
    };

}]);
