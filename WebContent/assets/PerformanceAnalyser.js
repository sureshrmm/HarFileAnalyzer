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
        var fileUploadserviceURL = "/HarFileAnalyzer/api/getHarAnalysys/";
        var file1 = $scope.firstHARFile;
        var file2 = $scope.secondHARFile;
        var requestData = angular.toJson(requestData);
        $scope.message = "";
        $("#loader").removeClass();
        perfAnalyserService.uploadFile(file1, file2, requestData, fileUploadserviceURL, function(response) {
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
        //perfAnalyserService.readJSON("FinalJson.json", function(data){
            $scope.analyseData = response;
            $("#analysisForm").addClass("hide");
            $("#analysisReport").removeClass("hide");
            $scope.showMatchedURLsData();
        //});
    };
    $scope.showMatchedURLsData = function() {
    	console.log("****************", $scope.analyseData.matchedURLs);
        if ($scope.analyseData && $scope.analyseData.matchedURLs) {
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
    };
    $scope.showAnalyseTab = function() {
        $("#report").removeClass("active");
        $("#reportTab").removeClass("active");
        $("#analyse").addClass("active");
        $("#analyseTab").addClass("active");
        $("#analysisReport").addClass("hide");
        $("#analysisForm").removeClass("hide");
    };
    $scope.generateReport = function() {
        var requestData = {};
        var files = document.getElementById('reportFile').files;
        var serviceURL = "/HarFileAnalyzer/api/getHarReport/";
        perfAnalyserService.reportGenerate(serviceURL, files);
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
}]);
perfAnalyserApp.service('perfAnalyserService', ['$http', function ($http) {
    this.reportGenerate = function(serviceURL, data){
        $("#loader").removeClass();
        var fd = new FormData();
        for (var i in data) {
        	fd.append('files', data[i]);
        }
        $http.post(serviceURL, fd, {
            transformRequest: angular.identity,
            headers: {'Content-Type': undefined}
        }).then(function successCallback(response){
        	$("#loader").addClass("hide");
        }, function errorCallback(response) {
        	$("#loader").addClass("hide");
        });
    };
    this.uploadFile = function(file1, file2, requestData, uploadUrl, callback){
        var fd = new FormData();
        fd.append('file1', file1);
        fd.append('file2', file2);
        fd.append('requestData', requestData);
        $http.post(uploadUrl, fd, {
            transformRequest: angular.identity,
            headers: {'Content-Type': undefined}
        }).then(function successCallback(response){
        	callback(response);
        }, function errorCallback(response) {
        	callback(response);
        });
    };
    this.readJSON = function(url, callback){
        $http.get(url)
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