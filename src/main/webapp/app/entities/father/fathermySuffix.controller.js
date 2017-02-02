(function() {
    'use strict';

    angular
        .module('jHipsterApp')
        .controller('FatherMySuffixController', FatherMySuffixController);

    FatherMySuffixController.$inject = ['$scope', '$state', 'Father'];

    function FatherMySuffixController ($scope, $state, Father) {
        var vm = this;

        vm.fathers = [];

        loadAll();

        function loadAll() {
            Father.query(function(result) {
                vm.fathers = result;
                vm.searchQuery = null;
            });
        }
    }
})();
