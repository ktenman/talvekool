(function() {
    'use strict';

    angular
        .module('talvekoolApp')
        .controller('OsalejaDetailController', OsalejaDetailController);

    OsalejaDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Osaleja'];

    function OsalejaDetailController($scope, $rootScope, $stateParams, previousState, entity, Osaleja) {
        var vm = this;

        vm.osaleja = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('talvekoolApp:osalejaUpdate', function(event, result) {
            vm.osaleja = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
