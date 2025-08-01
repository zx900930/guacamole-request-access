angular.module('requestAccess').service('requestManagementService', ['$http', function requestManagementService($http) {

    /**
     * Makes a request to the REST API to get the list of all requests.
     *
     * @returns {Promise} A promise which will resolve with the list of requests.
     */
    this.getRequests = function() {
        return $http.get("api/session/ext/request-access/manage/requests");
    };

    /**
     * Makes a request to the REST API to approve a request.
     *
     * @param {Number} requestId The ID of the request to approve.
     * @returns {Promise} A promise which will resolve when the request is approved.
     */
    this.approveRequest = function(requestId) {
        return $http.put("api/session/ext/request-access/manage/requests/" + requestId + "/approve");
    };

    /**
     * Makes a request to the REST API to deny a request.
     *
     * @param {Number} requestId The ID of the request to deny.
     * @returns {Promise} A promise which will resolve when the request is denied.
     */
    this.denyRequest = function(requestId) {
        return $http.put("api/session/ext/request-access/manage/requests/" + requestId + "/deny");
    };

    /**
     * Makes a request to the REST API to export the list of requests.
     *
     * @param {String} format The format to export the data in (csv or xlsx).
     */
    this.exportRequests = function(format) {
        window.open("api/session/ext/request-access/manage/requests/export/" + format, "_blank");
    };

}]);
