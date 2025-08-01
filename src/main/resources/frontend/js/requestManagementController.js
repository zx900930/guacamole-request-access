angular.module('requestAccess').controller('requestManagementController', ['$scope', '$injector',
    function requestManagementController($scope, $injector) {

    // Get required services
    var requestManagementService = $injector.get('requestManagementService');

    // List of requests
    $scope.requests = [];

    // Load requests
    function loadRequests() {
        requestManagementService.getRequests().then(function(requests) {
            $scope.requests = requests;
        });
    }

    // Approve a request
    $scope.approve = function(request) {
        requestManagementService.approveRequest(request.requestId).then(loadRequests);
    };

    // Deny a request
    $scope.deny = function(request) {
        requestManagementService.denyRequest(request.requestId).then(loadRequests);
    };

    // Export data
    $scope.exportAs = function(format) {
        requestManagementService.exportRequests(format);
    };

    // Initial load
    loadRequests();

}]);
