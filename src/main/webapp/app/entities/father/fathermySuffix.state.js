(function() {
    'use strict';

    angular
        .module('jHipsterApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('fathermySuffix', {
            parent: 'entity',
            url: '/fathermySuffix',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Fathers'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/father/fathersmySuffix.html',
                    controller: 'FatherMySuffixController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('fathermySuffix-detail', {
            parent: 'entity',
            url: '/fathermySuffix/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Father'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/father/fathermySuffix-detail.html',
                    controller: 'FatherMySuffixDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Father', function($stateParams, Father) {
                    return Father.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'fathermySuffix',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('fathermySuffix-detail.edit', {
            parent: 'fathermySuffix-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/father/fathermySuffix-dialog.html',
                    controller: 'FatherMySuffixDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Father', function(Father) {
                            return Father.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('fathermySuffix.new', {
            parent: 'fathermySuffix',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/father/fathermySuffix-dialog.html',
                    controller: 'FatherMySuffixDialogController',
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
                    $state.go('fathermySuffix', null, { reload: 'fathermySuffix' });
                }, function() {
                    $state.go('fathermySuffix');
                });
            }]
        })
        .state('fathermySuffix.edit', {
            parent: 'fathermySuffix',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/father/fathermySuffix-dialog.html',
                    controller: 'FatherMySuffixDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Father', function(Father) {
                            return Father.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('fathermySuffix', null, { reload: 'fathermySuffix' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('fathermySuffix.delete', {
            parent: 'fathermySuffix',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/father/fathermySuffix-delete-dialog.html',
                    controller: 'FatherMySuffixDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Father', function(Father) {
                            return Father.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('fathermySuffix', null, { reload: 'fathermySuffix' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
