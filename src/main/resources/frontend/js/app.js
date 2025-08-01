// Define the `requestAccess` module
angular.module('requestAccess', []);

// Route for the request form
angular.module('requestAccess').config(['$routeProvider', function config($routeProvider) {
    $routeProvider
        .when('/request', {
            templateUrl: 'app/ext/request-access/templates/request/request-form.html',
            controller: 'requestFormController'
        })
        .when('/manage-requests', {
            templateUrl: 'app/ext/request-access/templates/manage/request-management.html',
            controller: 'requestManagementController'
        })
        .when('/manage-environments', {
            templateUrl: 'app/ext/request-access/templates/environments/environment-management.html',
            controller: 'environmentManagementController'
        });
}]);
