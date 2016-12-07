(function() {
    'use strict';

    angular
        .module('talvekoolApp')
        .factory('OsalejaSearch', OsalejaSearch);

    OsalejaSearch.$inject = ['$resource'];

    function OsalejaSearch($resource) {
        var resourceUrl =  'api/_search/osalejas/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
