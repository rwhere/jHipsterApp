(function() {
    'use strict';

    angular
        .module('jHipsterApp')
        .controller('FatherMySuffixDialogController', FatherMySuffixDialogController);

    FatherMySuffixDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Father', 'Son', 'Daughter'];

    function FatherMySuffixDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Father, Son, Daughter) {
        var vm = this;

        vm.father = entity;
        vm.clear = clear;
        vm.save = save;
        vm.sons = Son.query();
        vm.daughters = Daughter.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.father.id !== null) {
                Father.update(vm.father, onSaveSuccess, onSaveError);
            } else {
                Father.save(vm.father, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('jHipsterApp:fatherUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
