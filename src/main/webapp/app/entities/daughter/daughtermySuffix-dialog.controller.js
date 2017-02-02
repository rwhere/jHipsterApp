(function() {
    'use strict';

    angular
        .module('jHipsterApp')
        .controller('DaughterMySuffixDialogController', DaughterMySuffixDialogController);

    DaughterMySuffixDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Daughter', 'Father'];

    function DaughterMySuffixDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Daughter, Father) {
        var vm = this;

        vm.daughter = entity;
        vm.clear = clear;
        vm.save = save;
        vm.fathers = Father.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.daughter.id !== null) {
                Daughter.update(vm.daughter, onSaveSuccess, onSaveError);
            } else {
                Daughter.save(vm.daughter, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('jHipsterApp:daughterUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
