(function() {
    'use strict';
    angular
        .module('jHipsterApp')
        .factory('Father', Father);

    Father.$inject = ['$resource'];

    function Father ($resource) {
        var resourceUrl =  'api/fathers/:id';

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
