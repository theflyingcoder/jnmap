var app = angular.module('jnmap', ['ngSanitize', 'ngResource']);

app.controller('ScannerCtrl', function ($scope, $http, $sce) {
    $scope.scan = function () {
        $http.post('/scan/' + $scope.targets, "")
            .success(function (data, status, headers, config) {
                $scope.process(data);

            }).error(function (data, status, headers, config) {
                $scope.status = status;
            });
    };

    $scope.history = function () {
        $http.get('/scan/' + $scope.targets, "")
            .success(function (data, status, headers, config) {
                $scope.process(data);

            }).error(function (data, status, headers, config) {
                $scope.status = status;
            });
    };


    $scope.showHide = function () {
        $scope.showResults = $scope.showResults ? false : true;
        if ($scope.showResults)  $scope.process(scanJobs);
    }


    $scope.process = function (scanJobs) {
        for (var scanJobIdx = 0; scanJobIdx < scanJobs.length; scanJobIdx++) {
            var job = scanJobs[scanJobIdx];
            var jobId = job.id;
            var jobCreateTime = job.createTime;
            var jobTarget = job.target;
            for (var portIdx = 0; portIdx < job.result.ports.length; portIdx++) {
                var port = scanJobs[scanJobIdx].result.ports[portIdx];

                // Record unique ports
                var portNo = port.port;
                var portState = port.state;

                // Keep track of all the unique ports by target
                if (!$scope.uniquePorts[jobTarget]) {
                    $scope.uniquePorts[jobTarget] = [];
                }
                if ($scope.uniquePorts[jobTarget].indexOf(port.port) == -1) {
                    $scope.uniquePorts[jobTarget].push(portNo);
                }

                // Map job+port to state
                $scope.portStates[jobId + "-" + portNo] = portState;
                // Map job to create time
                $scope.scanJobCreateTime[jobId] = jobCreateTime;
            }

            // Keep track of scan targets
            if ($scope.scanJobTargets.indexOf(jobTarget) == -1) {
                $scope.scanJobTargets.push(jobTarget);
            }

            // Keep track of mapping between target and job id
            if ($scope.scanJobIds.indexOf(jobId) == -1) {
                $scope.scanJobIds.push(jobId);
                if (!$scope.scanJobTargetIds[jobTarget]) {
                    $scope.scanJobTargetIds[jobTarget] = [];
                }
                $scope.scanJobTargetIds[jobTarget].push(jobId);
            }
        }
        $scope.showResults = true;
    }

    $scope.renderResultRow = function (scanJobTarget, jobId) {
        var row = '<td> Scan ' + jobId + ' [' + $scope.scanJobCreateTime[jobId] + ']' + '</td>';

        for (var uniquePortIdx in $scope.uniquePorts[scanJobTarget]) {
            var key = jobId + '-' + $scope.uniquePorts[scanJobTarget][uniquePortIdx];
            if ($scope.portStates[key]) {
                row = row + '<td>' + $scope.portStates[key] + '</td>';
            }
            else {
                row = row + '<td>n/a</td>';
            }
        }
        return $sce.trustAsHtml(row);
    };


    $scope.resetReports = function() {
        $scope.showResults = false;
        $scope.uniquePorts = {};
        $scope.portStates = {};
        $scope.job = {};
        $scope.scanJobTargets = [];
        $scope.scanJobIds = [];
        $scope.scanJobTargetIds = [];
        $scope.scanJobCreateTime = {};
    }

    $scope.resetReports();

});

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

validate = function (str) {
    return !(/^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])$|^(([a-zA-Z]|[a-zA-Z][a-zA-Z0-9\-]*[a-zA-Z0-9])\.)*([A-Za-z]|[A-Za-z][A-Za-z0-9\-]*[A-Za-z0-9])$|^\s*((([0-9A-Fa-f]{1,4}:){7}([0-9A-Fa-f]{1,4}|:))|(([0-9A-Fa-f]{1,4}:){6}(:[0-9A-Fa-f]{1,4}|((25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)(\.(25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)){3})|:))|(([0-9A-Fa-f]{1,4}:){5}(((:[0-9A-Fa-f]{1,4}){1,2})|:((25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)(\.(25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)){3})|:))|(([0-9A-Fa-f]{1,4}:){4}(((:[0-9A-Fa-f]{1,4}){1,3})|((:[0-9A-Fa-f]{1,4})?:((25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)(\.(25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)){3}))|:))|(([0-9A-Fa-f]{1,4}:){3}(((:[0-9A-Fa-f]{1,4}){1,4})|((:[0-9A-Fa-f]{1,4}){0,2}:((25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)(\.(25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)){3}))|:))|(([0-9A-Fa-f]{1,4}:){2}(((:[0-9A-Fa-f]{1,4}){1,5})|((:[0-9A-Fa-f]{1,4}){0,3}:((25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)(\.(25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)){3}))|:))|(([0-9A-Fa-f]{1,4}:){1}(((:[0-9A-Fa-f]{1,4}){1,6})|((:[0-9A-Fa-f]{1,4}){0,4}:((25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)(\.(25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)){3}))|:))|(:(((:[0-9A-Fa-f]{1,4}){1,7})|((:[0-9A-Fa-f]{1,4}){0,5}:((25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)(\.(25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)){3}))|:)))(%.+)?\s*$/.test(str));
};