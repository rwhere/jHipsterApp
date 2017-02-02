(function() {
    'use strict';

    angular
        .module('jHipsterApp')
        .controller('SonMySuffixDialogController', SonMySuffixDialogController);

    SonMySuffixDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Son', 'Father'];

    function SonMySuffixDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Son, Father) {
        var vm = this;

        vm.son = entity;
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
            if (vm.son.id !== null) {
                Son.update(vm.son, onSaveSuccess, onSaveError);
            } else {
                Son.save(vm.son, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('jHipsterApp:sonUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
