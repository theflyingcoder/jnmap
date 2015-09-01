var app = angular.module('jnmap', ['ngSanitize', 'ngResource']);

app.controller('ScannerCtrl', function ($scope, $http, $sce) {
    /**
     * Initiates a scan
     */
    $scope.scan = function () {
        $http.post('/scan/' + $scope.targets, "")
            .success(function (data, status, headers, config) {
                $scope.process(data);

            }).error(function (data, status, headers, config) {
                $scope.scanForm.$invalid = true;
            });
    };

    /**
     * Retrieves historical results for past scans
     */
    $scope.history = function () {
        $http.get('/scan/' + $scope.targets, "")
            .success(function (data, status, headers, config) {
                $scope.process(data);

            }).error(function (data, status, headers, config) {
                $scope.scanForm.$invalid = true;
            });
    };

    /**
     * Process scan results
     *
     * @param results
     */
    $scope.process = function (results) {
        for (var scanJobIdx = 0; scanJobIdx < results.length; scanJobIdx++) {
            var job = results[scanJobIdx];
            var jobId = job.id;
            var jobCreateTime = job.createTime;
            var target = job.target;
            for (var portIdx = 0; portIdx < job.result.ports.length; portIdx++) {
                var port = results[scanJobIdx].result.ports[portIdx];

                // Record unique ports for displaying columns
                // This is done by target
                var portNo = port.port;
                var portState = port.state;
                if (!$scope.uniquePorts[target]) {
                    $scope.uniquePorts[target] = [];
                }
                if ($scope.uniquePorts[target].indexOf(port.port) == -1) {
                    $scope.uniquePorts[target].push(portNo);
                }

                // Map job+port to state by target
                var jobPortKey = jobId + "-" + portNo;
                if (!$scope.portStates[target]) {
                    $scope.portStates[target] = {};
                }
                $scope.portStates[target][jobPortKey] = portState;

                // Map job+port to service by target
                var portService = port.service;
                if (!$scope.portServices[target]) {
                    $scope.portServices[target] = {};
                }
                $scope.portServices[target][jobPortKey] = portService;


                // Map job to create time
                $scope.scanJobCreateTime[jobId] = jobCreateTime;
            }

            // Keep track of returned scan targets
            if ($scope.scanJobTargets.indexOf(target) == -1) {
                $scope.scanJobTargets.push(target);
            }

            // Keep track of mapping between target to job id
            if ($scope.scanJobIds.indexOf(jobId) == -1) {
                $scope.scanJobIds.push(jobId);
                if (!$scope.scanJobTargetIds[target]) {
                    $scope.scanJobTargetIds[target] = [];
                }
                $scope.scanJobTargetIds[target].push(jobId);
            }
        }
        $scope.showResults = true;
    }

    /**
     * Renders result row by scan targets and job Id
     *
     * @param target
     * @param jobId
     * @returns {*}
     */
    $scope.renderResultRow = function (target, jobId) {
        var row = '<td> Scan ' + jobId + '<br/> [' + $scope.scanJobCreateTime[jobId] + ']' + '</td>';

        for (var uniquePortIdx in $scope.uniquePorts[target]) {
            var jobPortKey = jobId + '-' + $scope.uniquePorts[target][uniquePortIdx];
            var portState = $scope.portStates[target][jobPortKey];
            var portService = $scope.portServices[target][jobPortKey];
            if (!portService) portService = 'unknown';
            if (portState) {
                row = row + '<td>' + portState+ '<br/>[' + portService +']</td>';
            }
            else {
                row = row + '<td>-</td>';
            }
        }
        return $sce.trustAsHtml(row);
    };


    /**
     * Initializes/resets data.
     */
    $scope.resetReports = function () {
        $scope.showResults = false;
        $scope.scanJobTargets = [];
        $scope.scanJobIds = [];

        $scope.scanJobTargetIds = [];
        $scope.uniquePorts = [];
        $scope.portStates = [];
        $scope.portServices = [];
        $scope.job = [];
        $scope.scanJobCreateTime = [];
    }

    $scope.resetReports();

});

/**
 * Quick and dirty directive to validate scan target separated by comma, space, semicolon or pipe
 *
 */
app.directive('validTargets', function () {
    return {
        restrict: 'A',
        require: 'ngModel',
        link: function (scope, element, attrs, ctrl) {
            ctrl.$parsers.unshift(function (viewValue) {
                var invalidTarget = viewValue.split(/[\s,;|]+/).filter(validate);
                if ((invalidTarget.length == 1 && invalidTarget == "") || invalidTarget == 0) { // it is valid
                    ctrl.$setValidity('validTargets', true);
                    return viewValue;
                } else { // it is invalid, return undefined (no model update)
                    ctrl.$setValidity('validTargets', false);
                    return undefined;
                }
            });
        }
    };
});

/**
 * Validate ipv4, ipv6 and hostname
 *
 * @param str
 * @returns {boolean}
 */
validate = function (str) {
    return !(/^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])$|^(([a-zA-Z]|[a-zA-Z][a-zA-Z0-9\-]*[a-zA-Z0-9])\.)*([A-Za-z]|[A-Za-z][A-Za-z0-9\-]*[A-Za-z0-9])$|^\s*((([0-9A-Fa-f]{1,4}:){7}([0-9A-Fa-f]{1,4}|:))|(([0-9A-Fa-f]{1,4}:){6}(:[0-9A-Fa-f]{1,4}|((25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)(\.(25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)){3})|:))|(([0-9A-Fa-f]{1,4}:){5}(((:[0-9A-Fa-f]{1,4}){1,2})|:((25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)(\.(25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)){3})|:))|(([0-9A-Fa-f]{1,4}:){4}(((:[0-9A-Fa-f]{1,4}){1,3})|((:[0-9A-Fa-f]{1,4})?:((25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)(\.(25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)){3}))|:))|(([0-9A-Fa-f]{1,4}:){3}(((:[0-9A-Fa-f]{1,4}){1,4})|((:[0-9A-Fa-f]{1,4}){0,2}:((25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)(\.(25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)){3}))|:))|(([0-9A-Fa-f]{1,4}:){2}(((:[0-9A-Fa-f]{1,4}){1,5})|((:[0-9A-Fa-f]{1,4}){0,3}:((25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)(\.(25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)){3}))|:))|(([0-9A-Fa-f]{1,4}:){1}(((:[0-9A-Fa-f]{1,4}){1,6})|((:[0-9A-Fa-f]{1,4}){0,4}:((25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)(\.(25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)){3}))|:))|(:(((:[0-9A-Fa-f]{1,4}){1,7})|((:[0-9A-Fa-f]{1,4}){0,5}:((25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)(\.(25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)){3}))|:)))(%.+)?\s*$/.test(str));
};