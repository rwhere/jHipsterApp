(function() {
    'use strict';
    angular
        .module('jHipsterApp')
        .factory('Daughter', Daughter);

    Daughter.$inject = ['$resource'];

    function Daughter ($resource) {
        var resourceUrl =  'api/daughters/:id';

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
