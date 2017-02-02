(function() {
    'use strict';

    angular
        .module('jHipsterApp')
        .controller('SonMySuffixDetailController', SonMySuffixDetailController);

    SonMySuffixDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Son', 'Father'];

    function SonMySuffixDetailController($scope, $rootScope, $stateParams, previousState, entity, Son, Father) {
        var vm = this;

        vm.son = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('jHipsterApp:sonUpdate', function(event, result) {
            vm.son = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
