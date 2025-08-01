angular.module('requestAccess').controller('requestFormController', ['$scope', '$injector',
    function requestFormController($scope, $injector) {

    // Get required services
    var requestService = $injector.get('requestService');

    // Form data
    $scope.request = {};

    // Available environments
    $scope.environments = [];

    // Autocomplete for past reasons
    $scope.getPastReasons = function(viewValue) {
        return requestService.getPastReasons(viewValue);
    };

    // Load environments
    requestService.getEnvironments().then(function(environments) {
        $scope.environments = environments;
    });

}]);
