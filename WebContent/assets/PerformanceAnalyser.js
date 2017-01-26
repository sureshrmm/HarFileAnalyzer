var perfAnalyserApp = angular.module('perfAnalyserApp', []);

perfAnalyserApp.controller('PerfAnalyserController', ['$scope', 'perfAnalyserService', function PerfAnalyserController($scope, perfAnalyserService) {
    $scope.analyseHarFiles = function() {
        var requestData = {};
        var file1 = {};
        file1.name = $scope.firstHARFileName;
        file1.iteration = $scope.firstHARFileIteration;
        requestData.file1 = file1;
        var file2 = {};
        file2.name = $scope.secondHARFileName;
        file2.iteration = $scope.secondHARFileIteration;
        requestData.file2 = file2;
        var serviceURL = "/HarFileAnalyzer/api/getHarAnalysys/";
        $scope.message = "";
        var fd = new FormData();
        fd.append('file1', $scope.firstHARFile);
        fd.append('file2', $scope.secondHARFile);
        fd.append('requestData', angular.toJson(requestData));
        $("#loader").removeClass("hide");
        perfAnalyserService.postMultiPartData(serviceURL, fd, function(response) {
            $("#loader").addClass("hide");
            if (response) {
                if (response.data.status !== "200") {
                    $scope.message = response.data.message;
                    $("#errorMessage").removeClass("hide");
                } else {
                    $scope.showAnalyseResponse(JSON.parse(response.data.jsonObject));
                }
            }
        });
    };
    $scope.showAnalyseResponse = function(response) {
        $scope.analyseData = response;
        $scope.showMatchedURLsData();
    };
    $scope.showMatchedURLsData = function() {
        if ($scope.analyseData && $scope.analyseData.matchedURLs) {
            $("#analysisForm").addClass("hide");
            $("#analysisReport").removeClass("hide");
            $("#matchedURLs").removeClass("hide");
            $("#unmatchedURLs").addClass("hide");
            $("#totalComparision").addClass("hide");
            $("#matchedURLsLink").css("font-weight","bold");
            $("#unmatchedURLsLink").css("font-weight","normal");
            $("#totalComparisionLink").css("font-weight","normal");
            $scope.release1Name = $scope.analyseData.matchedURLs.details[0].releaseName;
            $scope.release2Name = $scope.analyseData.matchedURLs.details[1].releaseName;
            $scope.release1Details = $scope.analyseData.matchedURLs.details[0].requestDetails;
            $scope.release2Details = $scope.analyseData.matchedURLs.details[1].requestDetails;
            $scope.differences  = $scope.analyseData.matchedURLs.differences;
        }
    };
    $scope.showUnmatchedURLsData = function() {
        if ($scope.analyseData && $scope.analyseData.notMatchedURLs) {
            $("#matchedURLs").addClass("hide");
            $("#unmatchedURLs").removeClass("hide");
            $("#totalComparision").addClass("hide");
            $("#matchedURLsLink").css("font-weight","normal");
            $("#unmatchedURLsLink").css("font-weight","bold");
            $("#totalComparisionLink").css("font-weight","normal");
            $scope.release1Details = $scope.analyseData.notMatchedURLs[$scope.release1Name];
            $scope.release2Details = $scope.analyseData.notMatchedURLs[$scope.release2Name];
        }
    };
    $scope.showTotalComparisionData = function() {
        if ($scope.analyseData && $scope.analyseData.notMatchedURLs) {
            $("#matchedURLs").addClass("hide");
            $("#unmatchedURLs").addClass("hide");
            $("#totalComparision").removeClass("hide");
            $("#matchedURLsLink").css("font-weight","normal");
            $("#unmatchedURLsLink").css("font-weight","normal");
            $("#totalComparisionLink").css("font-weight","bold");
            $scope.release1Details = $scope.analyseData.totalComparision[$scope.release1Name];
            $scope.release2Details = $scope.analyseData.totalComparision[$scope.release2Name];
        }
    };
    $scope.validateAnalyseHarFilesData = function() {
        $scope.showAnalyseResponse();
        var hasError = false;
        if (!$("#firstHARFile").val()) {
            $("#firstHARFileLbl").addClass("color-red");
            $("#firstHARFileText").addClass("border-red");
            hasError = true;
        } else {
            $("#firstHARFileLbl").removeClass("color-red");
            $("#firstHARFileText").removeClass("border-red");
        }
        if (!$scope.firstHARFileName) {
            $("#firstHARFileNameLbl").addClass("color-red");
            $("#firstHARFileName").addClass("border-red");
            hasError = true;
        } else {
            $("#firstHARFileNameLbl").removeClass("color-red");
            $("#firstHARFileName").removeClass("border-red");
        }
        if (!$scope.firstHARFileIteration) {
            $("#firstHARFileIterationLbl").addClass("color-red");
            $("#firstHARFileIteration").addClass("border-red");
            hasError = true;
        } else {
            $("#firstHARFileIterationLbl").removeClass("color-red");
            $("#firstHARFileIteration").removeClass("border-red");
        }
        if (!$("#secondHARFile").val()) {
            $("#secondHARFileLbl").addClass("color-red");
            $("#secondHARFileText").addClass("border-red");
            hasError = true;
        } else {
            $("#secondHARFileLbl").removeClass("color-red");
            $("#secondHARFileText").removeClass("border-red");
        }
        if (!$scope.secondHARFileName) {
            $("#secondHARFileNameLbl").addClass("color-red");
            $("#secondHARFileName").addClass("border-red");
            hasError = true;
        } else {
            $("#secondHARFileNameLbl").removeClass("color-red");
            $("#secondHARFileName").removeClass("border-red");
        }
        if (!$scope.secondHARFileIteration) {
            $("#secondHARFileIterationLbl").addClass("color-red");
            $("#secondHARFileIteration").addClass("border-red");
            hasError = true;
        } else {
            $("#secondHARFileIterationLbl").removeClass("color-red");
            $("#secondHARFileIteration").removeClass("border-red");
        }
        if (!hasError) {
            $scope.analyseHarFiles();
        }
    };
    $scope.cancel = function() {
        window.location.reload();
    };
    $scope.showReportTab = function() {
        $("#analyse").removeClass("active");
        $("#analyseTab").removeClass("active");
        $("#report").addClass("active");
        $("#reportTab").addClass("active");
        $("#reportData").addClass("hide");
        $("#reportForm").removeClass("hide");
        $("#compareReleases").removeClass("active");
        $("#compareReleasesTab").removeClass("active");
        $("#wpt").removeClass("active");
        $("#wptTab").removeClass("active");
    };
    $scope.showAnalyseTab = function() {
        $("#report").removeClass("active");
        $("#reportTab").removeClass("active");
        $("#analyse").addClass("active");
        $("#analyseTab").addClass("active");
        $("#analysisReport").addClass("hide");
        $("#analysisForm").removeClass("hide");
        $("#compareReleases").removeClass("active");
        $("#compareReleasesTab").removeClass("active");
        $("#wpt").removeClass("active");
        $("#wptTab").removeClass("active");
    };
    $scope.showWPTTab = function() {
        $("#analyse").removeClass("active");
        $("#analyseTab").removeClass("active");
        $("#report").removeClass("active");
        $("#reportTab").removeClass("active");
        $("#compareReleases").removeClass("active");
        $("#compareReleasesTab").removeClass("active");
        $("#wpt").addClass("active");
        $("#wptTab").addClass("active");
        perfAnalyserService.getService("config/wpt-config.json", function(response) {
            $scope.wptConfig = response.data;
        });
    };
    $scope.showCompareReleasesTab = function() {
         $("#analyse").removeClass("active");
        $("#analyseTab").removeClass("active");
        $("#report").removeClass("active");
        $("#reportTab").removeClass("active");
        $("#wpt").removeClass("active");
        $("#wptTab").removeClass("active");
        $("#compareReleases").addClass("active");
        $("#compareReleasesTab").addClass("active");
    };
    $scope.generateReport = function() {
        var files = document.getElementById('reportFile').files;
        var serviceURL = "/HarFileAnalyzer/api/getHarReport/";
        var fd = new FormData();
        for (var i in files) {
            fd.append('files', files[i]);
        }
        $("#loader").removeClass("hide");
        perfAnalyserService.postMultiPartData(serviceURL, fd, function(response) {
            $("#loader").addClass("hide");
            response = JSON.parse(response.data.jsonObject);
            $scope.reportData = response;
            $scope.selectedHarData = $scope.reportData[0];
            $scope.showReportData();
        });
    };
    $scope.showReportData = function() {
        $("#reportForm").addClass("hide");
        $("#reportData").removeClass("hide");
        $('#page-selection').bootpag({
            total: $scope.reportData.length,
            maxVisible : 2,
        }).on("page", function(event, num){
            $scope.selectedHarData = $scope.reportData[num-1];
            $scope.$apply();
        });
        $scope.changePageNames();
        $(".pagination li.prev").click(function() {
            $scope.changePageNames();
        });
        $(".pagination li.next").click(function() {
            $scope.changePageNames();
        });
    };
    $scope.changePageNames = function() {
        $(".pagination li").each(function() {
            var index = $(this).find("a").text();
            if (index) {
                var selectedHarData = $scope.reportData[index-1];
                if (selectedHarData) {
                    $(this).find("a").text(selectedHarData.harName);
                }
            }
        });
    };
    $scope.validateGenerateReportData = function() {
        var hasError = false;
        if (!$("#reportFile").val()) {
            $("#reportFileLbl").addClass("color-red");
            $("#reportFileText").addClass("border-red");
            hasError = true;
        } else {
            $("#reportFileLbl").removeClass("color-red");
            $("#reportFileText").removeClass("border-red");
        }
        if (!hasError) {
            $scope.generateReport();
        }
    };
    $scope.downloadHarFiles = function() {
        var requestData = {};
        requestData.wptURL = $("#wptURL").val();
        requestData.releaseName = $("#releaseName").val();
        requestData.environment = $("#selectedEnv").val();
        requestData.browser = $("#selectedBrowser").val();
        requestData.bandwidth = $("#selectedBandwidth").val();
        var serviceURL = "/HarFileAnalyzer/api/downloadHARFiles/";
        $("#loader").removeClass("hide");
        perfAnalyserService.postSerive(serviceURL, requestData, function(response) {
            $("#loader").addClass("hide");
            $scope.downloadURL = response.data.jsonObject;
            setTimeout(function(){
                $("#downloadHAR")[0].click();
            }, 500)
        });
    };
    $scope.validateDownloadHarFiles = function() {
        var hasError = false;
        if (!$("#wptURL").val()) {
            $("#wptURLLbl").addClass("color-red");
            $("#wptURL").addClass("border-red");
            hasError = true;
        } else {
            $("#wptURLLbl").removeClass("color-red");
            $("#wptURL").removeClass("border-red");
        }
        if (!$("#releaseName").val()) {
            $("#freleaseNameLbl").addClass("color-red");
            $("#releaseName").addClass("border-red");
            hasError = true;
        } else {
            $("#freleaseNameLbl").removeClass("color-red");
            $("#releaseName").removeClass("border-red");
        }
        if (!$("#selectedEnv").val()) {
            $("#selectedEnvLbl").addClass("color-red");
            $("#selectedEnv").addClass("border-red");
            hasError = true;
        } else {
            $("#selectedEnvLbl").removeClass("color-red");
            $("#selectedEnv").removeClass("border-red");
        }
        if (!$("#selectedBrowser").val()) {
            $("#selectedBrowserLbl").addClass("color-red");
            $("#selectedBrowser").addClass("border-red");
            hasError = true;
        } else {
            $("#selectedBrowserLbl").removeClass("color-red");
            $("#selectedBrowser").removeClass("border-red");
        }
        if (!$("#selectedBandwidth").val()) {
            $("#selectedBandwidthLbl").addClass("color-red");
            $("#selectedBandwidth").addClass("border-red");
            hasError = true;
        } else {
            $("#selectedBandwidthLbl").removeClass("color-red");
            $("#selectedBandwidth").removeClass("border-red");
        }
        if (!hasError) {
            $scope.downloadHarFiles();
        }
    };
    $scope.compareReleaseHarFiles = function() {
        var release1HARFiles = document.getElementById('release1HARFiles').files;
        var release2HARFiles = document.getElementById('release2HARFiles').files;
        var serviceURL = "/HarFileAnalyzer/api/compareReleases/";
        var fd = new FormData();
        for (var i in release1HARFiles) {
            fd.append('release1HARFiles', release1HARFiles[i]);
        }
        for (var i in release2HARFiles) {
            fd.append('release2HARFiles', release2HARFiles[i]);
        }
        $("#loader").removeClass("hide");
        perfAnalyserService.postMultiPartData(serviceURL, fd, function(response) {
            $("#loader").addClass("hide");
            response = JSON.parse(response.data.jsonObject);
            $scope.reportData = response;
            $scope.selectedHarData = $scope.reportData[0];
            $scope.showReportData();
        });
    };
    $scope.validateComparisonData = function() {
        var hasError = false;
        if (!$("#release1HARText").val()) {
            $("#release1HARLbl").addClass("color-red");
            $("#release1HARText").addClass("border-red");
            hasError = true;
        } else {
            $("#release1HARLbl").removeClass("color-red");
            $("#release1HARText").removeClass("border-red");
        }
        if (!$("#release2HARText").val()) {
            $("#release2HARLbl").addClass("color-red");
            $("#release2HARText").addClass("border-red");
            hasError = true;
        } else {
            $("#release2HARLbl").removeClass("color-red");
            $("#release2HARText").removeClass("border-red");
        }
        if (!hasError) {
            $scope.compareReleaseHarFiles();
        }
    };
    $scope.showWPTTab();
}]);
perfAnalyserApp.service('perfAnalyserService', ['$http', function ($http) {
    this.postMultiPartData = function(serviceURL, formData, callback){
        $http.post(serviceURL, formData, {
            transformRequest: angular.identity,
            headers: {'Content-Type': undefined}
        }).then(function successCallback(response){
            callback(response);
        }, function errorCallback(response) {
            callback(response);
        });
    };
    this.getService = function(url, callback){
        $http.get(url)
        .then(function successCallback(response){
            callback(response);
        }, function errorCallback(response) {
            callback(response);
        });
    };
    this.postSerive = function(serviceURL, requestData, callback){
        $http.post(serviceURL, requestData)
        .then(function successCallback(response){
            callback(response);
        }, function errorCallback(response) {
            callback(response);
        });
    };
}]);
perfAnalyserApp.directive('fileModel', ['$parse', function ($parse) {
    return {
        restrict: 'A',
        link: function(scope, element, attrs) {
            var model = $parse(attrs.fileModel);
            var modelSetter = model.assign;
            element.bind('change', function(){
                scope.$apply(function(){
                    modelSetter(scope, element[0].files[0]);
                });
            });
        }
    };
}]);