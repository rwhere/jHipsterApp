(function() {
    'use strict';

    angular
        .module('jHipsterApp')
        .controller('SonMySuffixDeleteController',SonMySuffixDeleteController);

    SonMySuffixDeleteController.$inject = ['$uibModalInstance', 'entity', 'Son'];

    function SonMySuffixDeleteController($uibModalInstance, entity, Son) {
        var vm = this;

        vm.son = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Son.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
