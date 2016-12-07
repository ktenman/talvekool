(function() {
    'use strict';

    angular
        .module('talvekoolApp')
        .controller('OsalejaDialogController', OsalejaDialogController);

    OsalejaDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Osaleja'];

    function OsalejaDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Osaleja) {
        var vm = this;

        vm.osaleja = entity;
        vm.clear = clear;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.osaleja.id !== null) {
                Osaleja.update(vm.osaleja, onSaveSuccess, onSaveError);
            } else {
                Osaleja.save(vm.osaleja, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('talvekoolApp:osalejaUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
