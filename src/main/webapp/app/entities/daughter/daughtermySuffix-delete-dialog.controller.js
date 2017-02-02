(function() {
    'use strict';

    angular
        .module('jHipsterApp')
        .controller('DaughterMySuffixDeleteController',DaughterMySuffixDeleteController);

    DaughterMySuffixDeleteController.$inject = ['$uibModalInstance', 'entity', 'Daughter'];

    function DaughterMySuffixDeleteController($uibModalInstance, entity, Daughter) {
        var vm = this;

        vm.daughter = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Daughter.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
