(function() {
    'use strict';

    angular
        .module('jHipsterApp')
        .controller('FatherMySuffixDeleteController',FatherMySuffixDeleteController);

    FatherMySuffixDeleteController.$inject = ['$uibModalInstance', 'entity', 'Father'];

    function FatherMySuffixDeleteController($uibModalInstance, entity, Father) {
        var vm = this;

        vm.father = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Father.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
