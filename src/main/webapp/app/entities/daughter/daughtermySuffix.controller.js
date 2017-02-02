(function() {
    'use strict';

    angular
        .module('jHipsterApp')
        .controller('DaughterMySuffixController', DaughterMySuffixController);

    DaughterMySuffixController.$inject = ['$scope', '$state', 'Daughter'];

    function DaughterMySuffixController ($scope, $state, Daughter) {
        var vm = this;

        vm.daughters = [];

        loadAll();

        function loadAll() {
            Daughter.query(function(result) {
                vm.daughters = result;
                vm.searchQuery = null;
            });
        }
    }
})();
