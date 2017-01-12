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
        perfAnalyserService.uploadFile(file1,file2, requestData, fileUploadserviceURL, function(response) {
            if (response) {
                $scope.message = response.data.message;
                $("#errorMessage").removeClass("hide");
                $("#loader").addClass("hide");
                $scope.showAnalyseResponse(response);
            }
        });
    };
    $scope.showAnalyseResponse = function(response) {
        $("#analysisForm").addClass("hide");
        $("#analysisReport").removeClass("hide");
        response = JSON.parse('{"matchedURLs":{"differences":[{"sizeDiff":"0b","timeDiff":"-120ms","url":"https://www.walgreens.com/store/checkout/cart.jsp"},{"sizeDiff":"0b","timeDiff":"-54ms","url":"https://www.walgreens.com/share/jslib/jquery/jquery.min-.js"},{"sizeDiff":"0b","timeDiff":"-125ms","url":"https://www.walgreens.com/share/adaptive/walgreens/css/mobile/Walgreens-.css"},{"sizeDiff":"0b","timeDiff":"206ms","url":"https://www.walgreens.com/share/adaptive/walgreens/css/mobile/Header-Footer-.css"},{"sizeDiff":"0b","timeDiff":"237ms","url":"https://www.walgreens.com/share/adaptive/walgreens/css/mobile/Cart-.css"},{"sizeDiff":"0b","timeDiff":"-215ms","url":"https://assets.adobedtm.com/7deb8111c940df73792ccb9937476cc412e87575/satelliteLib-24aa6109.js"},{"sizeDiff":"0b","timeDiff":"234ms","url":"https://www.walgreens.com/images/adaptive/share/images/logos/walgreens-tab-logo.png"},{"sizeDiff":"0b","timeDiff":"-20ms","url":"https://www.walgreens.com/images/adaptive/share/images/icons/header-balance-reward-icon.png"},{"sizeDiff":"0b","timeDiff":"-119ms","url":"https://www.walgreens.com/share/jslib/omn_track/device-.js"},{"sizeDiff":"0b","timeDiff":"-241ms","url":"https://www.walgreens.com/share/jslib/jquery/jquery-ui.min-.js"},{"sizeDiff":"0b","timeDiff":"-236ms","url":"https://www.walgreens.com/livestyleguidenew/js/bootstrap.min-.js"},{"sizeDiff":"0b","timeDiff":"-213ms","url":"https://www.walgreens.com/livestyleguidenew/js/wag-custom-.js"},{"sizeDiff":"0b","timeDiff":"-234ms","url":"https://www.walgreens.com/share/jslib/angular/angular.min-.js"},{"sizeDiff":"0b","timeDiff":"-84ms","url":"https://www.walgreens.com/share/jslib/config/global-config-.js"},{"sizeDiff":"0b","timeDiff":"-300ms","url":"https://www.walgreens.com/share/adaptive/Framework.min-.js"},{"sizeDiff":"0b","timeDiff":"-455ms","url":"https://www.walgreens.com/share/jslib/config/common-config-.js"},{"sizeDiff":"0b","timeDiff":"-255ms","url":"https://www.walgreens.com/share/adaptive/walgreens/js/Header-Footer-.js"},{"sizeDiff":"0b","timeDiff":"-436ms","url":"https://www.walgreens.com/share/jslib/config/template-url-config-.js"},{"sizeDiff":"0b","timeDiff":"-432ms","url":"https://www.walgreens.com/share/adaptive/walgreens/js/Cart-.js"},{"sizeDiff":"0b","timeDiff":"21ms","url":"https://se.monetate.net/js/2/a-ca4ba9c7/p/walgreens.com/entry.js"},{"sizeDiff":"0b","timeDiff":"26ms","url":"https://se.monetate.net/js/3/a-ca4ba9c7/p/walgreens.com/t1457105100/f1f110b3c017de06/custom.js"},{"sizeDiff":"0b","timeDiff":"21ms","url":"https://www.walgreens.com/dtagent621_gxjnpr23t_1027.js"},{"sizeDiff":"0b","timeDiff":"217ms","url":"https://assets.adobedtm.com/7deb8111c940df73792ccb9937476cc412e87575/scripts/sa.js"},{"sizeDiff":"0b","timeDiff":"107ms","url":"https://assets.adobedtm.com/7deb8111c940df73792ccb9937476cc412e87575/s-code-contents-ebc1e337.js"},{"sizeDiff":"0b","timeDiff":"2.283s","url":"https://www.walgreens.com/images/adaptive/share/images/fonts/ATCOIcons.woff?wamem3"},{"sizeDiff":"0b","timeDiff":"1.367s","url":"https://www.walgreens.com/assets/user-info/template/UserInfo-.tmpl"},{"sizeDiff":"0b","timeDiff":"35ms","url":"https://www.walgreens.com/store/checkout/cart.jsp"},{"sizeDiff":"0b","timeDiff":"75ms","url":"https://assets.adobedtm.com/7deb8111c940df73792ccb9937476cc412e87575/scripts/sa.js"},{"sizeDiff":"0b","timeDiff":"71ms","url":"https://www.walgreens.com/share/adaptive/walgreens/js/ForeseeScript.min-.js"},{"sizeDiff":"0b","timeDiff":"65ms","url":"https://www.walgreens.com/assets/footer/template/Footer.jsp"},{"sizeDiff":"0b","timeDiff":"1.053s","url":"https://www.walgreens.com/assets/nav/template/GlobalHeaderNavigationElements_ab.jsp?ABTest=B"},{"sizeDiff":"0b","timeDiff":"915ms","url":"https://assets.adobedtm.com/7deb8111c940df73792ccb9937476cc412e87575/scripts/satellite-54bf1f4b3963630015bd0300.html"},{"sizeDiff":"0b","timeDiff":"1.202s","url":"https://www.walgreens.com/register/reg_confirmation.jsp"},{"sizeDiff":"0b","timeDiff":"-2ms","url":"https://www.walgreens.com/foresee/foresee-surveydef.js?build=5"},{"sizeDiff":"0b","timeDiff":"27ms","url":"https://www.walgreens.com/foresee/foresee-dhtml.css?build=5"},{"sizeDiff":"0b","timeDiff":"1.059s","url":"https://pixel.mathtag.com/event/img?mt_id=624551&mt_adid=109303&v1=0.00&v2=&s1=11231497341&s2="},{"sizeDiff":"0b","timeDiff":"1.068s","url":"https://smetrics.walgreens.com/b/ss/walgrns/1/JS-1.5.2-D6EF/s74274468533694?AQB=1&ndh=1&pf=1&t=4%2F2%2F2016%2012%3A51%3A0%205%20300&D=D%3D&fid=3F495E0A407F1824-1AB4F56CE8120FC7&vmt=4D4DA30D&ce=UTF-8&ns=walgrns&pageName=No%20Items%20in%20Cart%20%7C%20Shopping%20Cart%20%7C%20Walgreens&g=https%3A%2F%2Fwww.walgreens.com%2Fstore%2Fcheckout%2Fcart.jsp&cc=USD&ch=store&events=scView&products=%3B&c14=Shopping%20Cart&c37=loyalty%20sign%20up%20banner%20NOT%20shown%20%7C%20shopping%20cart&v73=320%2A568&v74=Portrait&s=320x568&c=32&j=1.6&v=N&k=Y&bw=320&bh=460&AQE=1"}],"details":[{"requestDetails":[{"timeTaken":"630ms","fileSize":"25.355469kb","url":"https://www.walgreens.com/store/checkout/cart.jsp","responseCode":200,"onload":"BEFORE"},{"timeTaken":"234ms","fileSize":"32.404297kb","url":"https://www.walgreens.com/share/jslib/jquery/jquery.min-.js","responseCode":200,"onload":"BEFORE"},{"timeTaken":"406ms","fileSize":"35.16211kb","url":"https://www.walgreens.com/share/adaptive/walgreens/css/mobile/Walgreens-.css","responseCode":200,"onload":"BEFORE"},{"timeTaken":"335ms","fileSize":"9.344727kb","url":"https://www.walgreens.com/share/adaptive/walgreens/css/mobile/Header-Footer-.css","responseCode":200,"onload":"BEFORE"},{"timeTaken":"315ms","fileSize":"2.7001953kb","url":"https://www.walgreens.com/share/adaptive/walgreens/css/mobile/Cart-.css","responseCode":200,"onload":"BEFORE"},{"timeTaken":"754ms","fileSize":"69.57715kb","url":"https://assets.adobedtm.com/7deb8111c940df73792ccb9937476cc412e87575/satelliteLib-24aa6109.js","responseCode":200,"onload":"BEFORE"},{"timeTaken":"303ms","fileSize":"3.0908203kb","url":"https://www.walgreens.com/images/adaptive/share/images/logos/walgreens-tab-logo.png","responseCode":200,"onload":"BEFORE"},{"timeTaken":"283ms","fileSize":"1.2685547kb","url":"https://www.walgreens.com/images/adaptive/share/images/icons/header-balance-reward-icon.png","responseCode":200,"onload":"BEFORE"},{"timeTaken":"292ms","fileSize":"774b","url":"https://www.walgreens.com/share/jslib/omn_track/device-.js","responseCode":200,"onload":"BEFORE"},{"timeTaken":"431ms","fileSize":"59.206055kb","url":"https://www.walgreens.com/share/jslib/jquery/jquery-ui.min-.js","responseCode":200,"onload":"BEFORE"},{"timeTaken":"431ms","fileSize":"9.330078kb","url":"https://www.walgreens.com/livestyleguidenew/js/bootstrap.min-.js","responseCode":200,"onload":"BEFORE"},{"timeTaken":"412ms","fileSize":"3.8691406kb","url":"https://www.walgreens.com/livestyleguidenew/js/wag-custom-.js","responseCode":200,"onload":"BEFORE"},{"timeTaken":"444ms","fileSize":"35.939453kb","url":"https://www.walgreens.com/share/jslib/angular/angular.min-.js","responseCode":200,"onload":"BEFORE"},{"timeTaken":"312ms","fileSize":"2.0410156kb","url":"https://www.walgreens.com/share/jslib/config/global-config-.js","responseCode":200,"onload":"BEFORE"},{"timeTaken":"540ms","fileSize":"6.3935547kb","url":"https://www.walgreens.com/share/adaptive/Framework.min-.js","responseCode":200,"onload":"BEFORE"},{"timeTaken":"749ms","fileSize":"6.5390625kb","url":"https://www.walgreens.com/share/jslib/config/common-config-.js","responseCode":200,"onload":"BEFORE"},{"timeTaken":"553ms","fileSize":"9.428711kb","url":"https://www.walgreens.com/share/adaptive/walgreens/js/Header-Footer-.js","responseCode":200,"onload":"BEFORE"},{"timeTaken":"746ms","fileSize":"24.583008kb","url":"https://www.walgreens.com/share/jslib/config/template-url-config-.js","responseCode":200,"onload":"BEFORE"},{"timeTaken":"745ms","fileSize":"15.356445kb","url":"https://www.walgreens.com/share/adaptive/walgreens/js/Cart-.js","responseCode":200,"onload":"BEFORE"},{"timeTaken":"366ms","fileSize":"11.651367kb","url":"https://se.monetate.net/js/2/a-ca4ba9c7/p/walgreens.com/entry.js","responseCode":200,"onload":"BEFORE"},{"timeTaken":"248ms","fileSize":"42.492188kb","url":"https://se.monetate.net/js/3/a-ca4ba9c7/p/walgreens.com/t1457105100/f1f110b3c017de06/custom.js","responseCode":200,"onload":"BEFORE"},{"timeTaken":"245ms","fileSize":"21.905273kb","url":"https://www.walgreens.com/dtagent621_gxjnpr23t_1027.js","responseCode":200,"onload":"BEFORE"},{"timeTaken":"57ms","fileSize":"240b","url":"https://assets.adobedtm.com/7deb8111c940df73792ccb9937476cc412e87575/scripts/sa.js","responseCode":200,"onload":"BEFORE"},{"timeTaken":"162ms","fileSize":"15.05957kb","url":"https://assets.adobedtm.com/7deb8111c940df73792ccb9937476cc412e87575/s-code-contents-ebc1e337.js","responseCode":200,"onload":"BEFORE"},{"timeTaken":"1.327s","fileSize":"49.54297kb","url":"https://www.walgreens.com/images/adaptive/share/images/fonts/ATCOIcons.woff?wamem3","responseCode":200,"onload":"BEFORE"},{"timeTaken":"156ms","fileSize":"1.5507812kb","url":"https://www.walgreens.com/assets/user-info/template/UserInfo-.tmpl","responseCode":200,"onload":"BEFORE"},{"timeTaken":"278ms","fileSize":"25.355469kb","url":"https://www.walgreens.com/store/checkout/cart.jsp","responseCode":200,"onload":"BEFORE"},{"timeTaken":"123ms","fileSize":"354b","url":"https://assets.adobedtm.com/7deb8111c940df73792ccb9937476cc412e87575/scripts/sa.js","responseCode":200,"onload":"BEFORE"},{"timeTaken":"123ms","fileSize":"27.34082kb","url":"https://www.walgreens.com/share/adaptive/walgreens/js/ForeseeScript.min-.js","responseCode":200,"onload":"BEFORE"},{"timeTaken":"124ms","fileSize":"3.5878906kb","url":"https://www.walgreens.com/assets/footer/template/Footer.jsp","responseCode":200,"onload":"BEFORE"},{"timeTaken":"770ms","fileSize":"14.774414kb","url":"https://www.walgreens.com/assets/nav/template/GlobalHeaderNavigationElements_ab.jsp?ABTest=B","responseCode":200,"onload":"BEFORE"},{"timeTaken":"842ms","fileSize":"660b","url":"https://assets.adobedtm.com/7deb8111c940df73792ccb9937476cc412e87575/scripts/satellite-54bf1f4b3963630015bd0300.html","responseCode":200,"onload":"BEFORE"},{"timeTaken":"655ms","fileSize":"306b","url":"https://www.walgreens.com/register/reg_confirmation.jsp","responseCode":200,"onload":"AFTER"},{"timeTaken":"93ms","fileSize":"3.0175781kb","url":"https://www.walgreens.com/foresee/foresee-surveydef.js?build=5","responseCode":200,"onload":"AFTER"},{"timeTaken":"35ms","fileSize":"2.1699219kb","url":"https://www.walgreens.com/foresee/foresee-dhtml.css?build=5","responseCode":200,"onload":"AFTER"},{"timeTaken":"773ms","fileSize":"0b","url":"https://pixel.mathtag.com/event/img?mt_id=624551&mt_adid=109303&v1=0.00&v2=&s1=11231497341&s2=","responseCode":302,"onload":"BEFORE"},{"timeTaken":"698ms","fileSize":"0b","url":"https://smetrics.walgreens.com/b/ss/walgrns/1/JS-1.5.2-D6EF/s74274468533694?AQB=1&ndh=1&pf=1&t=4%2F2%2F2016%2012%3A51%3A0%205%20300&D=D%3D&fid=3F495E0A407F1824-1AB4F56CE8120FC7&vmt=4D4DA30D&ce=UTF-8&ns=walgrns&pageName=No%20Items%20in%20Cart%20%7C%20Shopping%20Cart%20%7C%20Walgreens&g=https%3A%2F%2Fwww.walgreens.com%2Fstore%2Fcheckout%2Fcart.jsp&cc=USD&ch=store&events=scView&products=%3B&c14=Shopping%20Cart&c37=loyalty%20sign%20up%20banner%20NOT%20shown%20%7C%20shopping%20cart&v73=320%2A568&v74=Portrait&s=320x568&c=32&j=1.6&v=N&k=Y&bw=320&bh=460&AQE=1","responseCode":302,"onload":"BEFORE"}],"releaseName":"Suresh"},{"requestDetails":[{"timeTaken":"510ms","fileSize":"25.355469kb","url":"https://www.walgreens.com/store/checkout/cart.jsp","responseCode":200,"onload":"BEFORE"},{"timeTaken":"180ms","fileSize":"32.404297kb","url":"https://www.walgreens.com/share/jslib/jquery/jquery.min-.js","responseCode":200,"onload":"BEFORE"},{"timeTaken":"281ms","fileSize":"35.16211kb","url":"https://www.walgreens.com/share/adaptive/walgreens/css/mobile/Walgreens-.css","responseCode":200,"onload":"BEFORE"},{"timeTaken":"541ms","fileSize":"9.344727kb","url":"https://www.walgreens.com/share/adaptive/walgreens/css/mobile/Header-Footer-.css","responseCode":200,"onload":"BEFORE"},{"timeTaken":"552ms","fileSize":"2.7001953kb","url":"https://www.walgreens.com/share/adaptive/walgreens/css/mobile/Cart-.css","responseCode":200,"onload":"BEFORE"},{"timeTaken":"539ms","fileSize":"69.57715kb","url":"https://assets.adobedtm.com/7deb8111c940df73792ccb9937476cc412e87575/satelliteLib-24aa6109.js","responseCode":200,"onload":"BEFORE"},{"timeTaken":"537ms","fileSize":"3.0908203kb","url":"https://www.walgreens.com/images/adaptive/share/images/logos/walgreens-tab-logo.png","responseCode":200,"onload":"BEFORE"},{"timeTaken":"263ms","fileSize":"1.2685547kb","url":"https://www.walgreens.com/images/adaptive/share/images/icons/header-balance-reward-icon.png","responseCode":200,"onload":"BEFORE"},{"timeTaken":"173ms","fileSize":"774b","url":"https://www.walgreens.com/share/jslib/omn_track/device-.js","responseCode":200,"onload":"BEFORE"},{"timeTaken":"190ms","fileSize":"59.206055kb","url":"https://www.walgreens.com/share/jslib/jquery/jquery-ui.min-.js","responseCode":200,"onload":"BEFORE"},{"timeTaken":"195ms","fileSize":"9.330078kb","url":"https://www.walgreens.com/livestyleguidenew/js/bootstrap.min-.js","responseCode":200,"onload":"BEFORE"},{"timeTaken":"199ms","fileSize":"3.8691406kb","url":"https://www.walgreens.com/livestyleguidenew/js/wag-custom-.js","responseCode":200,"onload":"BEFORE"},{"timeTaken":"210ms","fileSize":"35.939453kb","url":"https://www.walgreens.com/share/jslib/angular/angular.min-.js","responseCode":200,"onload":"BEFORE"},{"timeTaken":"228ms","fileSize":"2.0410156kb","url":"https://www.walgreens.com/share/jslib/config/global-config-.js","responseCode":200,"onload":"BEFORE"},{"timeTaken":"240ms","fileSize":"6.3935547kb","url":"https://www.walgreens.com/share/adaptive/Framework.min-.js","responseCode":200,"onload":"BEFORE"},{"timeTaken":"294ms","fileSize":"6.5390625kb","url":"https://www.walgreens.com/share/jslib/config/common-config-.js","responseCode":200,"onload":"BEFORE"},{"timeTaken":"298ms","fileSize":"9.428711kb","url":"https://www.walgreens.com/share/adaptive/walgreens/js/Header-Footer-.js","responseCode":200,"onload":"BEFORE"},{"timeTaken":"310ms","fileSize":"24.583008kb","url":"https://www.walgreens.com/share/jslib/config/template-url-config-.js","responseCode":200,"onload":"BEFORE"},{"timeTaken":"313ms","fileSize":"15.356445kb","url":"https://www.walgreens.com/share/adaptive/walgreens/js/Cart-.js","responseCode":200,"onload":"BEFORE"},{"timeTaken":"387ms","fileSize":"11.651367kb","url":"https://se.monetate.net/js/2/a-ca4ba9c7/p/walgreens.com/entry.js","responseCode":200,"onload":"BEFORE"},{"timeTaken":"274ms","fileSize":"42.492188kb","url":"https://se.monetate.net/js/3/a-ca4ba9c7/p/walgreens.com/t1457105100/f1f110b3c017de06/custom.js","responseCode":200,"onload":"BEFORE"},{"timeTaken":"266ms","fileSize":"21.905273kb","url":"https://www.walgreens.com/dtagent621_gxjnpr23t_1027.js","responseCode":200,"onload":"BEFORE"},{"timeTaken":"274ms","fileSize":"240b","url":"https://assets.adobedtm.com/7deb8111c940df73792ccb9937476cc412e87575/scripts/sa.js","responseCode":200,"onload":"BEFORE"},{"timeTaken":"269ms","fileSize":"15.05957kb","url":"https://assets.adobedtm.com/7deb8111c940df73792ccb9937476cc412e87575/s-code-contents-ebc1e337.js","responseCode":200,"onload":"BEFORE"},{"timeTaken":"3.61s","fileSize":"49.54297kb","url":"https://www.walgreens.com/images/adaptive/share/images/fonts/ATCOIcons.woff?wamem3","responseCode":200,"onload":"BEFORE"},{"timeTaken":"1.523s","fileSize":"1.5507812kb","url":"https://www.walgreens.com/assets/user-info/template/UserInfo-.tmpl","responseCode":200,"onload":"BEFORE"},{"timeTaken":"313ms","fileSize":"25.355469kb","url":"https://www.walgreens.com/store/checkout/cart.jsp","responseCode":200,"onload":"BEFORE"},{"timeTaken":"198ms","fileSize":"354b","url":"https://assets.adobedtm.com/7deb8111c940df73792ccb9937476cc412e87575/scripts/sa.js","responseCode":200,"onload":"BEFORE"},{"timeTaken":"194ms","fileSize":"27.34082kb","url":"https://www.walgreens.com/share/adaptive/walgreens/js/ForeseeScript.min-.js","responseCode":200,"onload":"BEFORE"},{"timeTaken":"189ms","fileSize":"3.5878906kb","url":"https://www.walgreens.com/assets/footer/template/Footer.jsp","responseCode":200,"onload":"BEFORE"},{"timeTaken":"1.823s","fileSize":"14.774414kb","url":"https://www.walgreens.com/assets/nav/template/GlobalHeaderNavigationElements_ab.jsp?ABTest=B","responseCode":200,"onload":"BEFORE"},{"timeTaken":"1.757s","fileSize":"660b","url":"https://assets.adobedtm.com/7deb8111c940df73792ccb9937476cc412e87575/scripts/satellite-54bf1f4b3963630015bd0300.html","responseCode":200,"onload":"BEFORE"},{"timeTaken":"1.857s","fileSize":"306b","url":"https://www.walgreens.com/register/reg_confirmation.jsp","responseCode":200,"onload":"AFTER"},{"timeTaken":"91ms","fileSize":"3.0175781kb","url":"https://www.walgreens.com/foresee/foresee-surveydef.js?build=5","responseCode":200,"onload":"AFTER"},{"timeTaken":"62ms","fileSize":"2.1699219kb","url":"https://www.walgreens.com/foresee/foresee-dhtml.css?build=5","responseCode":200,"onload":"AFTER"},{"timeTaken":"1.832s","fileSize":"0b","url":"https://pixel.mathtag.com/event/img?mt_id=624551&mt_adid=109303&v1=0.00&v2=&s1=11231235019&s2=","responseCode":302,"onload":"BEFORE"},{"timeTaken":"1.766s","fileSize":"0b","url":"https://smetrics.walgreens.com/b/ss/walgrns/1/JS-1.5.2-D6EF/s75053848014213?AQB=1&ndh=1&pf=1&t=4%2F2%2F2016%2012%3A57%3A9%205%20300&D=D%3D&fid=4E88DEA0073AF0AD-1EFF7948EA0B6A0A&vmt=4D4DA30D&ce=UTF-8&ns=walgrns&pageName=No%20Items%20in%20Cart%20%7C%20Shopping%20Cart%20%7C%20Walgreens&g=https%3A%2F%2Fwww.walgreens.com%2Fstore%2Fcheckout%2Fcart.jsp&cc=USD&ch=store&events=scView&products=%3B&c14=Shopping%20Cart&c37=loyalty%20sign%20up%20banner%20NOT%20shown%20%7C%20shopping%20cart&v73=320%2A568&v74=Portrait&s=320x568&c=32&j=1.6&v=N&k=Y&bw=320&bh=460&AQE=1","responseCode":302,"onload":"BEFORE"}],"releaseName":"Lohesh"}]}}');
        if (response.matchedURLs) {
            $scope.release1Name = response.matchedURLs.details[0].releaseName
            $scope.release2Name = response.matchedURLs.details[1].releaseName
            $scope.release1Details = response.matchedURLs.details[0].requestDetails;
            $scope.release2Details = response.matchedURLs.details[1].requestDetails;
            $scope.differences  = response.matchedURLs.differences;
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