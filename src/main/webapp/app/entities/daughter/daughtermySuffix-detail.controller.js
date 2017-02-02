(function() {
    'use strict';

    angular
        .module('jHipsterApp')
        .controller('DaughterMySuffixDetailController', DaughterMySuffixDetailController);

    DaughterMySuffixDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Daughter', 'Father'];

    function DaughterMySuffixDetailController($scope, $rootScope, $stateParams, previousState, entity, Daughter, Father) {
        var vm = this;

        vm.daughter = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('jHipsterApp:daughterUpdate', function(event, result) {
            vm.daughter = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
