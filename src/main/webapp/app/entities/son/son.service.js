(function() {
    'use strict';
    angular
        .module('jHipsterApp')
        .factory('Son', Son);

    Son.$inject = ['$resource'];

    function Son ($resource) {
        var resourceUrl =  'api/sons/:id';

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
