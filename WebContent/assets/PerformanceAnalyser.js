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
                    $scope.analyseData = JSON.parse(response.data.jsonObject);
                    $scope.downloadHarAnalysysUrl = response.data.downloadUrl;
                    $scope.firstHARUrl = response.data.firstHARUrl;
                    $scope.secondHARUrl = response.data.secondHARUrl;
					$scope.release1Name = $scope.analyseData.matchedURLs.details[0].releaseName;
					$scope.release2Name = $scope.analyseData.matchedURLs.details[1].releaseName;
                    $scope.showHARComparison();
                }
            }
        });
    };
    $scope.showHARComparison = function() {
        $(".har").removeClass("har");
        $("#analysisForm").addClass("hide");
        $("#analysisReport").removeClass("hide");
        $("#matchedURLs").addClass("hide");
        $("#unmatchedURLs").addClass("hide");
        $("#totalComparision").addClass("hide");
		$("#harComparison").removeClass("hide");
        $("#compareHARView").css("font-weight","bold");
        $("#matchedURLsLink").css("font-weight","normal");
        $("#unmatchedURLsLink").css("font-weight","normal");
        $("#totalComparisionLink").css("font-weight","normal");
        $("#har1ViewerParentDiv").addClass("har").attr("data-har", $scope.firstHARUrl);
        $("#har2ViewerParentDiv").addClass("har").attr("data-har", $scope.secondHARUrl);
        $scope.constructHARViewer();
    };
    /*$scope.showAnalyseResponse = function(response) {
        $scope.analyseData = response;
        $scope.showMatchedURLsData();
    };*/
    $scope.showMatchedURLsData = function() {
        if ($scope.analyseData && $scope.analyseData.matchedURLs) {
			$("#matchedURLs").removeClass("hide");
            $("#unmatchedURLs").addClass("hide");
            $("#totalComparision").addClass("hide");
			$("#harComparison").addClass("hide");
            $("#matchedURLsLink").css("font-weight","bold");
            $("#unmatchedURLsLink").css("font-weight","normal");
            $("#totalComparisionLink").css("font-weight","normal");
            $("#compareHARView").css("font-weight","normal");
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
			$("#harComparison").addClass("hide");
            $("#matchedURLsLink").css("font-weight","normal");
            $("#unmatchedURLsLink").css("font-weight","bold");
            $("#totalComparisionLink").css("font-weight","normal");
            $("#compareHARView").css("font-weight","normal");
            $scope.release1Details = $scope.analyseData.notMatchedURLs[$scope.release1Name];
            $scope.release2Details = $scope.analyseData.notMatchedURLs[$scope.release2Name];
        }
    };
    $scope.showTotalComparisionData = function() {
        if ($scope.analyseData && $scope.analyseData.totalComparision) {
            $("#matchedURLs").addClass("hide");
            $("#unmatchedURLs").addClass("hide");
            $("#totalComparision").removeClass("hide");
			$("#harComparison").addClass("hide");
            $("#matchedURLsLink").css("font-weight","normal");
            $("#unmatchedURLsLink").css("font-weight","normal");
            $("#compareHARView").css("font-weight","normal");
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
    $scope.showWPTTab = function() {
        $(".isactive").fadeOut("fast", function() {
            $("#wpt").fadeIn("slow");
        });
        $("#analyse").removeClass("isactive");
        $("#analyseTab").removeClass("active");
        $("#report").removeClass("isactive");
        $("#reportTab").removeClass("active");
        $("#compareReleases").removeClass("isactive");
        $("#compareReleasesTab").removeClass("active");
        $("#wpt").addClass("isactive");
        $("#wptTab").addClass("active");
        $("#harViewer").removeClass("isactive");
        $("#harViewerTab").removeClass("active");
        perfAnalyserService.getService("config/wpt-config.json", function(response) {
            $scope.wptConfig = response.data;
        });
    };
    $scope.showAnalyseTab = function(fadeIn) {
        if (fadeIn !== false) {
            $(".isactive").fadeOut("fast", function() {
                $("#analyse").fadeIn("slow");
            });
        }
        $("#report").removeClass("isactive");
        $("#reportTab").removeClass("active");
        $("#analyse").addClass("isactive");
        $("#analyseTab").addClass("active");
        $("#analysisReport").addClass("hide");
        $("#analysisForm").removeClass("hide");
        $("#compareReleases").removeClass("isactive");
        $("#compareReleasesTab").removeClass("active");
        $("#wpt").removeClass("isactive");
        $("#wptTab").removeClass("active");
        $("#harViewer").removeClass("isactive");
        $("#harViewerTab").removeClass("active");
    };
    $scope.showReportTab = function(fadeIn) {
        if (fadeIn !== false) {
            $(".isactive").fadeOut("fast", function() {
                $("#report").fadeIn("slow");
            });
        }
        $("#analyse").removeClass("isactive");
        $("#analyseTab").removeClass("active");
        $("#report").addClass("isactive");
        $("#reportTab").addClass("active");
        $("#reportData").addClass("hide");
        $("#reportForm").removeClass("hide");
        $("#compareReleases").removeClass("isactive");
        $("#compareReleasesTab").removeClass("active");
        $("#wpt").removeClass("isactive");
        $("#wptTab").removeClass("active");
        $("#harViewer").removeClass("isactive");
        $("#harViewerTab").removeClass("active");
    };
    $scope.showCompareReleasesTab = function() {
        $(".isactive").fadeOut("fast", function() {
            $("#compareReleases").fadeIn("slow");
        });
        $("#analyse").removeClass("isactive");
        $("#analyseTab").removeClass("active");
        $("#report").removeClass("isactive");
        $("#reportTab").removeClass("active");
        $("#wpt").removeClass("isactive");
        $("#wptTab").removeClass("active");
        $("#harViewer").removeClass("isactive");
        $("#harViewerTab").removeClass("active");
        $("#compareReleases").addClass("isactive");
        $("#compareReleasesTab").addClass("active");
    };
    $scope.showHARViewerTab = function(fadeIn) {
        if (fadeIn !== false) {
            $(".isactive").fadeOut("fast", function() {
                $("#harViewer").fadeIn("slow");
            });
        }
        $("#analyse").removeClass("isactive");
        $("#analyseTab").removeClass("active");
        $("#report").removeClass("isactive");
        $("#reportTab").removeClass("active");
        $("#compareReleases").removeClass("isactive");
        $("#compareReleasesTab").removeClass("active");
        $("#wpt").removeClass("isactive");
        $("#wptTab").removeClass("active");
        $("#harViewer").addClass("isactive");
        $("#harViewerTab").addClass("active");
        $("#harViewerData").addClass("hide");
        $("#harViewerForm").removeClass("hide");
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
            $scope.downloadHarReportUrl = response.data.downloadUrl;
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
            $scope.downloadURL = response.data.downloadUrl;
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
    $scope.constructHARViewer = function() {
        $("#har").remove();
        var har = document.createElement("script");
        har.src = "libs/js/har.js";
        har.setAttribute("id", "har");
        har.setAttribute("async", "true");
        document.documentElement.firstChild.appendChild(har);
        $("#harViewerData").removeClass("hide");
        $("#harViewerForm").addClass("hide");
    };
    $scope.viewHARFile = function() {
        var files = document.getElementById('harViewerFile').files;
        var serviceURL = "/HarFileAnalyzer/api/getHarReport/";
        var requestData = {};
        requestData.from = "harViewer";
        var fd = new FormData();
        fd.append('files', files[0]);
        fd.append('requestData', angular.toJson(requestData));
        $("#loader").removeClass("hide");
        perfAnalyserService.postMultiPartData(serviceURL, fd, function(response) {
            $("#loader").addClass("hide");
            $(".har").removeClass("har");
            $("#harViewerParentDiv").addClass("har");
            $("#harViewerParentDiv").attr("data-har", response.data.downloadUrl);
            $scope.constructHARViewer();
        });
    };
    $scope.validateHARViewerData = function() {
        var hasError = false;
        if (!$("#harViewerFile").val()) {
            $("#harViewerFileLbl").addClass("color-red");
            $("#harViewerText").addClass("border-red");
            hasError = true;
        } else {
            $("#harViewerFileLbl").removeClass("color-red");
            $("#harViewerText").removeClass("border-red");
        }
        if (!hasError) {
            $scope.viewHARFile();
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