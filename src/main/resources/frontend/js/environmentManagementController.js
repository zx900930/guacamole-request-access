angular.module('requestAccess').controller('environmentManagementController', ['$scope', '$injector',
    function environmentManagementController($scope, $injector) {

    // Get required services
    var environmentManagementService = $injector.get('environmentManagementService');

    // List of environments
    $scope.environments = [];

    // New environment data
    $scope.newEnvironment = {};

    // Editing state
    $scope.editing = {};

    // Load environments
    function loadEnvironments() {
        environmentManagementService.getEnvironments().then(function(environments) {
            $scope.environments = environments;
        });
    }

    // Add an environment
    $scope.addEnvironment = function() {
        environmentManagementService.addEnvironment($scope.newEnvironment).then(function() {
            $scope.newEnvironment = {};
            loadEnvironments();
        });
    };

    // Edit an environment
    $scope.editEnvironment = function(environment) {
        $scope.editing[environment.environmentId] = true;
    };

    // Save an environment
    $scope.saveEnvironment = function(environment) {
        environmentManagementService.saveEnvironment(environment).then(function() {
            $scope.editing[environment.environmentId] = false;
            loadEnvironments();
        });
    };

    // Delete an environment
    $scope.deleteEnvironment = function(environment) {
        environmentManagementService.deleteEnvironment(environment.environmentId).then(loadEnvironments);
    };

    // Initial load
    loadEnvironments();

}]);
