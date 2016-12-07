(function() {
    'use strict';

    angular
        .module('talvekoolApp')
        .controller('OsalejaDeleteController',OsalejaDeleteController);

    OsalejaDeleteController.$inject = ['$uibModalInstance', 'entity', 'Osaleja'];

    function OsalejaDeleteController($uibModalInstance, entity, Osaleja) {
        var vm = this;

        vm.osaleja = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Osaleja.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
