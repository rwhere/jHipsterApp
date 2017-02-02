(function() {
    'use strict';

    angular
        .module('jHipsterApp')
        .controller('FatherMySuffixDetailController', FatherMySuffixDetailController);

    FatherMySuffixDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Father', 'Son', 'Daughter'];

    function FatherMySuffixDetailController($scope, $rootScope, $stateParams, previousState, entity, Father, Son, Daughter) {
        var vm = this;

        vm.father = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('jHipsterApp:fatherUpdate', function(event, result) {
            vm.father = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
