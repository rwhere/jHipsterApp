(function() {
    'use strict';

    angular
        .module('jHipsterApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('daughtermySuffix', {
            parent: 'entity',
            url: '/daughtermySuffix',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Daughters'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/daughter/daughtersmySuffix.html',
                    controller: 'DaughterMySuffixController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('daughtermySuffix-detail', {
            parent: 'entity',
            url: '/daughtermySuffix/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Daughter'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/daughter/daughtermySuffix-detail.html',
                    controller: 'DaughterMySuffixDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Daughter', function($stateParams, Daughter) {
                    return Daughter.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'daughtermySuffix',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('daughtermySuffix-detail.edit', {
            parent: 'daughtermySuffix-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/daughter/daughtermySuffix-dialog.html',
                    controller: 'DaughterMySuffixDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Daughter', function(Daughter) {
                            return Daughter.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('daughtermySuffix.new', {
            parent: 'daughtermySuffix',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/daughter/daughtermySuffix-dialog.html',
                    controller: 'DaughterMySuffixDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                age: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('daughtermySuffix', null, { reload: 'daughtermySuffix' });
                }, function() {
                    $state.go('daughtermySuffix');
                });
            }]
        })
        .state('daughtermySuffix.edit', {
            parent: 'daughtermySuffix',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/daughter/daughtermySuffix-dialog.html',
                    controller: 'DaughterMySuffixDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Daughter', function(Daughter) {
                            return Daughter.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('daughtermySuffix', null, { reload: 'daughtermySuffix' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('daughtermySuffix.delete', {
            parent: 'daughtermySuffix',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/daughter/daughtermySuffix-delete-dialog.html',
                    controller: 'DaughterMySuffixDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Daughter', function(Daughter) {
                            return Daughter.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('daughtermySuffix', null, { reload: 'daughtermySuffix' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
