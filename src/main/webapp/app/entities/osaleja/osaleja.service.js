(function() {
    'use strict';
    angular
        .module('talvekoolApp')
        .factory('Osaleja', Osaleja);

    Osaleja.$inject = ['$resource'];

    function Osaleja ($resource) {
        var resourceUrl =  'api/osalejas/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
