(function () {
    'use strict';

    angular
        .module('talvekoolApp')
        .controller('OsalejaController', OsalejaController);

    OsalejaController.$inject = ['$scope', '$state', 'Osaleja', 'OsalejaSearch', 'ParseLinks', 'AlertService', 'paginationConstants'];

    function OsalejaController($scope, $state, Osaleja, OsalejaSearch, ParseLinks, AlertService, paginationConstants) {
        var vm = this;


        vm.osalejas = [];
        vm.loadPage = loadPage;
        vm.itemsPerPage = paginationConstants.itemsPerPage;
        vm.page = 0;
        vm.links = {
            last: 0
        };
        vm.predicate = 'id';
        vm.reset = reset;
        vm.reverse = true;
        vm.clear = clear;
        vm.loadAll = loadAll;
        vm.search = search;
        vm.filtreeri = filtreeri;

        loadAll();

        function loadAll() {
            if (vm.currentSearch) {
                OsalejaSearch.query({
                    query: vm.currentSearch,
                    page: vm.page,
                    size: vm.itemsPerPage,
                    sort: sort()
                }, onSuccess, onError);
            } else {
                Osaleja.query({
                    page: vm.page,
                    size: vm.itemsPerPage,
                    sort: sort()
                }, onSuccess, onError);
            }
            function sort() {
                var result = [vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc')];
                if (vm.predicate !== 'id') {
                    result.push('id');
                }
                return result;
            }

            function onSuccess(data, headers) {
                vm.links = ParseLinks.parse(headers('link'));
                vm.totalItems = headers('X-Total-Count');
                for (var i = 0; i < data.length; i++) {
                    vm.osalejas.push(data[i]);
                }
                $scope.osalejad = angular.copy(vm.osalejas);
                $scope.oskustasemed = _.uniqBy(_.map($scope.osalejad, 'oskustase'));
                $scope.oskustase = _.sample($scope.oskustasemed);
                $scope.options = {
                    legend: {
                        display: true
                    }
                };
                $scope.sugu =  _.countBy($scope.osalejad, 'sugu');
                filtreeri();
            }

            function onError(error) {
                AlertService.error(error.data.message);
            }
        }

        function filtreeri() {
            $scope.filtreeritud = _.countBy(_.filter($scope.osalejad, {"oskustase": $scope.oskustase}), 'sugu');
            var arr = _.sortBy(_.toPairs($scope.filtreeritud));
            var sood = [];
            var kogus = [];
            _.forEach(arr, function (data) {
                sood.push(data[0]);
                kogus.push(data[1])
            });
            $scope.labels = sood;
            $scope.data = kogus;
        }

        function reset() {
            vm.page = 0;
            vm.osalejas = [];
            loadAll();
        }

        function loadPage(page) {
            vm.page = page;
            loadAll();
        }

        function clear() {
            vm.osalejas = [];
            vm.links = {
                last: 0
            };
            vm.page = 0;
            vm.predicate = 'id';
            vm.reverse = true;
            vm.searchQuery = null;
            vm.currentSearch = null;
            vm.loadAll();
        }

        function search(searchQuery) {
            if (!searchQuery) {
                return vm.clear();
            }
            vm.osalejas = [];
            vm.links = {
                last: 0
            };
            vm.page = 0;
            vm.predicate = '_score';
            vm.reverse = false;
            vm.currentSearch = searchQuery;
            vm.loadAll();
        }
    }
})();
