(function() {
    'use strict';

    angular
        .module('jHipsterApp')
        .controller('SonMySuffixController', SonMySuffixController);

    SonMySuffixController.$inject = ['$scope', '$state', 'Son'];

    function SonMySuffixController ($scope, $state, Son) {
        var vm = this;

        vm.sons = [];

        loadAll();

        function loadAll() {
            Son.query(function(result) {
                vm.sons = result;
                vm.searchQuery = null;
            });
        }
    }
})();
